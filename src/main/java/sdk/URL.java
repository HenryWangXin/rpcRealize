package sdk;

import java.io.Serializable;
import java.util.Objects;


/**
 *
 */
public class URL implements Serializable{
    private String hostname;
    private Integer port;
    private Integer weight;
    private String protocal;

    public URL(String hostname, Integer port, String protocal,Integer weight) {
        this.hostname = hostname;
        this.port = port;
        this.protocal = protocal;
        this.weight = weight;
    }
    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getProtocal() {
        return protocal;
    }

    public void setProtocal(String protocal) {
        this.protocal = protocal;
    }


}
