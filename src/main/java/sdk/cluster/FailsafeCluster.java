package sdk.cluster;

import sdk.Directory;
import sdk.Invoker;

public class FailsafeCluster implements Cluster {

    public final static String NAME = "failfast";

    public <T> Invoker<T> join(Directory directory,String clusterName)  {
        return new FailfastClusterInvoker(directory);
    }

}

