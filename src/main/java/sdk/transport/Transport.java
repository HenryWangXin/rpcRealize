package sdk.transport;

import sdk.Invocation;

public interface Transport {

    void start(String hostname, Integer port);

    /**
     *
     * @param invocation
     * @return
     */
    String send(Invocation invocation);


}
