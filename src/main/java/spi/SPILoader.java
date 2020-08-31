package spi;



import sdk.cluster.Cluster;
import sdk.Directory;
import sdk.protocol.Protocol;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SPILoader<T> {
    /**
     *
     */
    private final Class<?> type;

    private static final ConcurrentMap<Class<?>, SPILoader<?>> SPI_LOADERS = new ConcurrentHashMap<Class<?>, SPILoader<?>>();

    private final ConcurrentMap<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();

    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();

    private static final ConcurrentMap<Class<?>, Object> SPI_INSTANCES = new ConcurrentHashMap<Class<?>, Object>();

    /**
     * @param type
     */
    private SPILoader(Class<?> type) {
        this.type = type;
    }

    /**
     * @param type
     * @param <T>
     * @return
     */
    public static <T> SPILoader<T> getExtensionLoader(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Extension type == null");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Extension type(" + type + ") is not interface!");
        }
        SPILoader<T> loader = (SPILoader<T>) SPI_LOADERS.get(type);
        if (loader == null) {
            SPI_LOADERS.putIfAbsent(type, new SPILoader<T>(type));
            loader = (SPILoader<T>) SPI_LOADERS.get(type);
        }
        return loader;
    }

    public T getExtension(String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Extension name == null");
        }
        Holder<Object> holder = cachedInstances.get(name);
        if (holder == null) {
            cachedInstances.putIfAbsent(name, new Holder<Object>());
            holder = cachedInstances.get(name);
        }
        Object instance = holder.get();
        if (instance == null) {
            synchronized (holder) {
                instance = holder.get();
                if (instance == null) {
                    instance = createExtension(name);
                    holder.set(instance);
                }
            }
        }
        return (T) instance;
    }

    private Map<String, Class<?>> getExtensionClasses() {
        Map<String, Class<?>> classes = cachedClasses.get();
        if (classes == null) {
            synchronized (cachedClasses) {
                classes = cachedClasses.get();
                if (classes == null) {
                    classes = loadExtensionClasses();
                    cachedClasses.set(classes);
                }
            }
        }
        return classes;
    }


    private T createExtension(String name) {
        Class<?> clazz = getExtensionClasses().get(name);
        if (clazz == null) {
            System.out.println("[createExtension] 没有发现类");
        }

        T instance = (T) SPI_INSTANCES.get(clazz);
        if (instance == null) {
            try {
                SPI_INSTANCES.putIfAbsent(clazz, clazz.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
            instance = (T) SPI_INSTANCES.get(clazz);
        }
            /*
            //反射
            injectExtension(instance);
            //AOP
            Set<Class<?>> wrapperClasses = cachedWrapperClasses;
            if (wrapperClasses != null && !wrapperClasses.isEmpty()) {
                for (Class<?> wrapperClass : wrapperClasses) {
                    instance = injectExtension((T) wrapperClass.getConstructor(type).newInstance(instance));
                }
            }
            */
        return instance;

    }

    private Map<String, Class<?>> loadExtensionClasses() {
        String servicesDirectory = "META-INF/services/";

        Map<String, Class<?>> extensionClassesMap = new HashMap<>();
        loadDirectory(extensionClassesMap, servicesDirectory, type.getName());

        return extensionClassesMap;
    }

    /**
     * 从指定目录加载需要的扩展点
     * (没有先判断文件是否存在,直接交给ClassLoader去处理了)
     *
     * @param extensionClasses 扩展点信息
     * @param dir              目录
     * @param type             扩展点文件名(必须与扩展接口路径一致?)
     */
    private void loadDirectory(Map<String, Class<?>> extensionClasses, String dir, String type) {
        String fileName = dir + type;
        Enumeration<URL> urls;
        try {
            ClassLoader classLoader = SPILoader.class.getClassLoader();
            if (classLoader != null) {
                urls = classLoader.getResources(fileName);
            } else {
                urls = ClassLoader.getSystemResources(fileName);
            }
            if (urls != null) {
                while (urls.hasMoreElements()) {
                    java.net.URL resourceURL = urls.nextElement();
                    loadResource(extensionClasses, classLoader, resourceURL);
                }
            }
        } catch (Throwable t) {

        }
    }

    private void loadResource(Map<String, Class<?>> extensionClasses, ClassLoader classLoader, java.net.URL resourceURL) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceURL.openStream(), "utf-8"));
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    final int ci = line.indexOf('#');
                    if (ci >= 0) {
                        line = line.substring(0, ci);
                    }
                    line = line.trim();
                    if (line.length() > 0) {
                        try {
                            String name = null;
                            int indexOf = line.indexOf('=');
                            if (indexOf > 0) {
                                name = line.substring(0, indexOf).trim();
                                line = line.substring(indexOf + 1).trim();
                            }
                            if (line.length() > 0) {
                                Class<?> clazz = Class.forName(line, true, classLoader);
                                Class<?> c = extensionClasses.get(name);
                                if (c == null) {
                                    extensionClasses.put(name, clazz);
                                } else if (c != clazz) {
                                    throw new IllegalStateException("Duplicate extension");
                                }
                            }
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }
            } finally {
                reader.close();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public <T> T getAdaptiveExtension() {
        return (T) java.lang.reflect.Proxy.newProxyInstance(
                type.getClassLoader()
                , new Class[]{type},
                (proxy, method, args) -> {
                    System.out.println("[wangxin]:" + method.getName());
                    if ("export".equals(method.getName()) && args[0] instanceof sdk.Invoker) {
                        sdk.Invoker invoker = (sdk.Invoker) args[0];
                        String protocolStr = invoker.getUrl().getProtocal();
                        Protocol realProtocol = SPILoader.getExtensionLoader(Protocol.class).getExtension(protocolStr);
                        realProtocol.export(invoker);
                    }

                    if ("refer".equals(method.getName()) && args[1] instanceof sdk.URL) {
                        Class<T> arg = (Class<T>) args[0];
                        sdk.URL  url = (sdk.URL) args[1];
                        String protocolStr = url.getProtocal();
                        Protocol realProtocol = SPILoader.getExtensionLoader(Protocol.class).getExtension(protocolStr);
                        return realProtocol.refer(arg,url);
                    }

                    if ("join".equals(method.getName()) && args[0] instanceof Directory && args[1] instanceof String) {
                        Directory directory = (Directory) args[0];
                        String clusterName = (String)args[1];
                        /*directory.getInvokers()
                        String protocolStr = invoker.getUrl().getProtocal();*/
                        Cluster realCluster = SPILoader.getExtensionLoader(Cluster.class).getExtension(clusterName);
                        return realCluster.join(directory,"");
                    }

                    return null;
                });
    }


}
