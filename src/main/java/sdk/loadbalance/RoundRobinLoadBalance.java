package sdk.loadbalance;

import sdk.Invoker;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * RoundRobinLoadBalance简介
 * 轮训选择
 * @author wangxin119
 * @date 2020-08-29 23:57
 */
public class RoundRobinLoadBalance implements LoadBalance {
    private static ConcurrentMap<Integer, ConcurrentMap<Integer, Integer>> weightIndexMap = new ConcurrentHashMap<Integer, ConcurrentMap<Integer, Integer>>();
    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers) {
        //1.初始化
        if (weightIndexMap.size() == 0) {
            for (int index = 0; index < invokers.size(); index++) {
                ConcurrentMap weightMap = new ConcurrentHashMap<Integer, Integer>();
                weightMap.put(invokers.get(index).getUrl().getWeight(), 0);
                weightIndexMap.put(index, weightMap);
            }
        }
        //2.j加自己
        weightIndexMap.forEach((key, map) -> {
            for (Map.Entry<Integer, Integer> vo : map.entrySet()) {
                vo.setValue(vo.getKey() + vo.getValue());
            }
        });
        int maxKey = 0;
        int maxValue = Integer.MIN_VALUE;
        //3 选最大
        for (Map.Entry<Integer, ConcurrentMap<Integer, Integer>> entry : weightIndexMap.entrySet()) {
            Integer key = entry.getKey();
            ConcurrentMap<Integer, Integer> map = entry.getValue();
            for (Map.Entry<Integer, Integer> vo : map.entrySet()) {
                if (vo.getValue() > maxValue) {
                    maxValue = vo.getValue();
                    maxKey = key;
                }else if(vo.getValue() == maxValue && key<maxKey){
                    maxValue = vo.getValue();
                    maxKey = key;
                }
            }
        }
        // 4 变负数
        ConcurrentMap<Integer, Integer> map = weightIndexMap.get(maxKey);
        for (Map.Entry<Integer, Integer> vo : map.entrySet()) {
            vo.setValue(0 - vo.getKey());
        }
        return invokers.get(maxKey);
    }
/*
    public static void main(String[] args) {
        List<Invoker<String>> invokers = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Invoker invoker = new Invoker();
            URL url = new URL("120.0.0." + i, i, "p" + i, i);
            invoker.setUrl(url);
            invokers.add(invoker);
        }
        System.out.println(JSONObject.toJSONString(invokers));
        for (int i = 0; i < 24; i++) {
            Invoker<String> select = select(invokers);
            System.out.println(JSONObject.toJSONString(select));
        }
    }*/
}
