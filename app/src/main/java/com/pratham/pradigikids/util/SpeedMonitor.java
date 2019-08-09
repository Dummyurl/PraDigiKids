package com.pratham.pradigikids.util;

public class SpeedMonitor {
    static final double NANOS_PER_SECOND = 1000000000.0;  //1秒=10亿nanoseconds
    static final double BYTES_PER_MIB = 1024 * 1024;    //1M=1024*1024byte
    static final double BYTES_PER_KB = 1024;
    static final String BYTE_SUFFIX = "B/s";
    static final String KB_SUFFIX = "KB/s";
    static final String MIB_SUFFIX = "M/s";
    static double speed = 0;
    static String suffix = BYTE_SUFFIX;
    private static long totalRead = 0;
    private static long lastSpeedCountTime = 0;

    public static String compute(int length) {
        totalRead += length;
        long curTime = System.nanoTime();
        if (lastSpeedCountTime == 0) {
            lastSpeedCountTime = curTime;
        }
        if (curTime >= lastSpeedCountTime + NANOS_PER_SECOND) {
            if (totalRead < BYTES_PER_KB) {
                speed = NANOS_PER_SECOND * totalRead / (curTime - lastSpeedCountTime);
                suffix = BYTE_SUFFIX;
            } else if (totalRead >= BYTES_PER_KB && totalRead < BYTES_PER_MIB) {
                speed = NANOS_PER_SECOND * totalRead / BYTES_PER_KB / (curTime - lastSpeedCountTime);
                suffix = KB_SUFFIX;
            } else if (totalRead >= BYTES_PER_MIB) {
                speed = NANOS_PER_SECOND * totalRead / BYTES_PER_MIB / (curTime - lastSpeedCountTime);
                suffix = MIB_SUFFIX;
            }
            lastSpeedCountTime = curTime;
            totalRead = 0;
        }
        return (((double) Math.round(speed * 100) / 100) + suffix);
    }
}
