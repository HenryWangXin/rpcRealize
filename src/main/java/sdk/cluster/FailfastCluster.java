package sdk.cluster;

import sdk.Directory;
import sdk.Invoker;

/**
 * FailfastCluster简介
 * 快速失败集群容错
 * @author wangxin119
 * @date 2020-08-29 23:46
 */
public class FailfastCluster implements Cluster {

    public final static String NAME = "failfast";

    public <T> Invoker<T> join(Directory directory,String clusterName)  {
        return new FailfastClusterInvoker(directory);
    }

}

