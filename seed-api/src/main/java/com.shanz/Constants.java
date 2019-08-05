package com.shanz;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName:Constants
 * @Description: TODO
 * @Author: shanz
 * @Date: 2019/2/15 13:58
 * @Version:1.0
 **/
public interface Constants {

    Charset UTF_8 = StandardCharsets.UTF_8;
    byte[] EMPTY_BYTES = new byte[0];
    String HTTP_HEAD_READ_TIMEOUT = "readTimeout";
    String EMPTY_STRING = "";
    String ANY_HOST = "0.0.0.0";
    String KICK_CHANNEL_PREFIX = "/lion/kick/";

    static String getKickChannel(String hostAndPort) {
        return KICK_CHANNEL_PREFIX + hostAndPort;
    }
}
