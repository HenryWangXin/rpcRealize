package sdk.cluster;

import sdk.Directory;
import sdk.Invoker;

/**
 * Cluster简介
 *
 * @author wangxin119
 * @date 2020-08-29 23:46
 */
public interface Cluster {
    <T> Invoker<T> join(Directory directory, String clusterName);
}
