package com.zjh.utils;
import java.util.*;

/**
 * 订单号生成器
 */
public class OrderNoGenerator {
    private int size;
    private int length;
    private List<String> orderNoList;
    private Set<String> orderNoSet;

    /**
     * 订单号生成器
     * <p>
     * 为了保证生成性能需满足条件size<10^length/4
     *
     * @param size   保证连续不重读的数
     * @param length 生成随机数的长度
     */
    public OrderNoGenerator(int size, int length) {
        this.size = size;
        this.length = length;
        AssertUtils.requireTrue(size < Math.pow(10, length) / 4, "参数不符合要求");
        orderNoList = new ArrayList<>();
        orderNoSet = new HashSet<>();
    }

    /**
     * 获取不重复的随机数
     *
     * @return
     */
    public synchronized String generatorOrderNo() {
        String randomNumber = RandomUtils.randomNumber(length);
        while (orderNoSet.contains(randomNumber)) {
            randomNumber = RandomUtils.randomNumber(length);
        }
        orderNoList.add(randomNumber);
        orderNoSet.add(randomNumber);
        reduce();
        return randomNumber;
    }

    private void reduce() {
        if (orderNoList.size() >= size * 2) {
            List<String> removes = orderNoList.subList(0, size);
            orderNoSet.removeAll(removes);
            removes.clear();
        }
    }
    
  //测试代码 
    private static int counts = 0;
    private static Object lock = 0;
    public static void main(String[] args) {
        //订单号格式为 yyyyMMddhhmmss+3位随机数
        //假设连续生成200单
        OrderNoGenerator generator = new OrderNoGenerator(20, 5);
        Map<String, String> map = new HashMap<>();
        for (int x = 0; x < 10; x++) {
            new Thread(() -> {
                int count = 0;
                for (int j = 0; j < 100; j++) {
                    for (int i = 0; i < 200; i++) {
                        String randomNumber = generator.generatorOrderNo();
                        if(i%4 == 0) {
                        	//System.out.println(randomNumber);
                        }
                        synchronized (lock) {
                            counts++;
                            if (counts > 200) {
                                map.clear();
                            }
                            if (map.containsKey(randomNumber)) {
                                count++;
                                System.out.println("订单号重复");
                            }
                            map.put(randomNumber, "");
                        }
                    }

                }
                System.out.println("线程"+Thread.currentThread().getId()+"订单号重复" + count + "次");
            }).start();
        }

    }
    
}