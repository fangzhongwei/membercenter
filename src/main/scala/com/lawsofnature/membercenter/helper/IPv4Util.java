package com.lawsofnature.membercenter.helper;

import java.util.StringTokenizer;

/**
 * Created by fangzhongwei on 2016/11/21.
 */
public class IPv4Util {
    public static void main(String[] args) {
        System.out.println("iptoLong2 : " + ipToLong("156.236.90.75"));
        System.out.println("longToIp2 : " + longToIp(2632735307L));
    }

    public static long ipToLong(String ipAddress) {
        long result = 0;
        String[] ipAddressInArray = new String[4];

        final StringTokenizer stringTokenizer = new StringTokenizer(ipAddress, ".");
        int index = 0;
        while (stringTokenizer.hasMoreTokens()) {
            ipAddressInArray[index++] = stringTokenizer.nextToken();
        }

        for (int i = 3; i >= 0; i--) {
            long ip = Long.parseLong(ipAddressInArray[3 - i]);
            result |= ip << (i * 8);
        }
        return result;
    }

    public static String longToIp(long ip) {
        final StringBuilder sb = new StringBuilder(15);
        for (int i = 0; i < 4; i++) {
            sb.insert(0, Long.toString(ip & 0xff));
            if (i < 3) {
                sb.insert(0, '.');
            }
            ip = ip >> 8;
        }
        return sb.toString();
    }
}