package spi;


import sdk.protocol.Protocol;

public class test {
    public static void main(String[] args) {
        SPILoader<Protocol> spiLoader = SPILoader.getExtensionLoader(Protocol.class);
        Protocol dubbo = spiLoader.getExtension("dubbo");
        System.out.println(dubbo.testSpi());
    }
}
