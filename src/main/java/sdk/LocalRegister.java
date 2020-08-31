package sdk;

import java.util.HashMap;
import java.util.Map;


/**
 *
 */
public class LocalRegister {

    private static Map<String, Object> exporterMap = new HashMap<>();

    public static void register(String interfaceName, Object obj) {
        exporterMap.put(interfaceName, obj);
    }

    public static Object get(String interfaceName) {
        return exporterMap.get(interfaceName);
    }
}
