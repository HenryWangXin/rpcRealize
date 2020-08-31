package sdk.protocol.http;

import org.apache.commons.io.IOUtils;
import sdk.Invocation;
import sdk.Invoker;
import sdk.transport.Transport;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * HttpClient简介
 *
 * @author wangxin119
 * @date 2020-08-19 10:55
 */
public class HttpClient implements Transport {
    private URL url;


    public String send(String hostname, Integer port, Invocation invocation) {
        try {
            URL url = new URL("http", hostname, port, "/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(invocation);
            oos.flush();
            oos.close();

            InputStream inputStream = connection.getInputStream();
            String result = IOUtils.toString(inputStream, "utf-8");
            return result;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void start(String hostname, Integer port) {
        try {
            url = new URL("http", hostname, port, "/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String send(Invocation invocation) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(invocation);
            oos.flush();
            oos.close();
            InputStream inputStream = connection.getInputStream();
            String result = IOUtils.toString(inputStream, "utf-8");
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
