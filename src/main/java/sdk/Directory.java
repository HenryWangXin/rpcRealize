package sdk;

import sdk.Invoker;

import java.util.List;

public class Directory {
    private volatile List<Invoker<?>> invokers;

    public Directory(List<Invoker<?>> invokers) {
        this.invokers = invokers;
    }

    public List<Invoker<?>> getInvokers() {
        return invokers;
    }

    public void setInvokers(List<Invoker<?>> invokers) {
        this.invokers = invokers;
    }
}
