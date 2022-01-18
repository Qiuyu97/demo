package com.qiuyu.demo.utils;

import com.qiuyu.demo.config.SnowFlakeProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Component
public class SnowFlake {
    @Autowired
    private SnowFlakeProperties snowFlakeProperties;
    private static Map<String, String> ids = new HashMap<>();

    private static Logger logger = LoggerFactory.getLogger(SnowFlake.class);

    @PostConstruct
    public void init() {
        ids = snowFlakeProperties.getIds();
    }

    /**
     * 起始时间戳
     */
    private final long startStamp = 1512125149000L;
    /**
     * 时间戳位数
     */
    private final long timeStampBits = 41L;
    /**
     * 机器id所占的位数
     */
    private final long workerIdBits = 4L;
    /**
     * 序列号所占的位数
     */
    private final long sequenceBits = 2L;
    /**
     * 时间戳最大值
     */
    private final long maxTimeStamp = -1L ^ (-1L << timeStampBits);
    /**
     * 机器id的最大值
     */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
    /**
     * 序列号的最大值
     */
    private final long maxSequence = -1L ^ (-1L << sequenceBits);
    /**
     * 机器id左移的位数
     */
    private final long workerIdShift = sequenceBits;
    /**
     * 时间戳左移的位数
     */
    private final long timeStampShift = workerIdShift + workerIdBits;

    private long workerId;
    private long sequence = 0L;
    private long lastTimeStamp = -1L;

    private static class SingletonHolder {
        private static SnowFlake snowFlake;
        static {
            String ip = getIp();
            String id = ids.getOrDefault(ip, "1");
            logger.info("host ip: " + ip);
            logger.info("host snowFlake id: " + id);
            snowFlake = new SnowFlake(Long.valueOf(id));
        }
    }

    private SnowFlake() {}

    private SnowFlake(long workerId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker id can't be greater than %d or less than 0", maxWorkerId));
        }
        this.workerId = workerId;
    }

    public static SnowFlake getInstance() {
        return SingletonHolder.snowFlake;
    }

    /**
     * 获得下一个ID
     * @return
     */
    public synchronized long nextId() {
        long currentTimeStamp = timeGen();

        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过，这个时间应该抛出异常
        if (currentTimeStamp < lastTimeStamp) {
            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimeStamp - currentTimeStamp));
        }

        // 如果是同一毫秒生成的，则进行序列自增
        if (lastTimeStamp == currentTimeStamp) {
            sequence = (sequence + 1) & maxSequence;
            // 同一毫秒内序列溢出
            if (sequence == 0) {
                // 阻塞到下一个毫秒，获得新的时间戳
                currentTimeStamp = tilNextMillis(lastTimeStamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimeStamp = currentTimeStamp;
        return ((currentTimeStamp - startStamp) & maxTimeStamp) << timeStampShift    // 时间戳部分
                | (workerId << workerIdShift)                       // 机器id部分
                | sequence;                                         // 序列号部分
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimeStamp
     * @return
     */
    private long tilNextMillis(long lastTimeStamp) {
        long timeStamp = timeGen();
        while (timeStamp <= lastTimeStamp) {
            timeStamp = timeGen();
        }
        return timeStamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     * @return
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }

    private static String getIp() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("获取ip地址失败" + e);
        }
        return null;
    }

    public static void main(String[] args) {
        SnowFlake snowFlake = new SnowFlake(1);
        int n = 100;
        Set<String> set = new HashSet<>();
//        List<String> set = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            long id = snowFlake.nextId();
            String s = String.valueOf(id);
            set.add(s);
            System.out.println(Long.toBinaryString(id));
            System.out.println(Long.toBinaryString(id).length());
            System.out.println(s);
            System.out.println(s.length());
        }
        System.out.println(set.size());
    }
}
