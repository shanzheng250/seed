
package com.shanz.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 */
public interface Logs {
    boolean logInit = init();

    static boolean init() {
        if (logInit) return true;
//        System.setProperty("log.home", CC.META-INF.log_dir);
//        System.setProperty("log.root.level", CC.META-INF.log_level);
//        System.setProperty("logback.configurationFile", CC.META-INF.log_conf_path);
        LoggerFactory
                .getLogger("console");
//                .info(CC.META-INF.cfg.root().render(ConfigRenderOptions.concise().setFormatted(true)));
        return true;
    }

    Logger Console = LoggerFactory.getLogger("console"),

    CONN = LoggerFactory.getLogger("META-INF.conn.log"),

    MONITOR = LoggerFactory.getLogger("META-INF.monitor.log"),

    PUSH = LoggerFactory.getLogger("META-INF.push.log"),

    HB = LoggerFactory.getLogger("META-INF.heartbeat.log"),

    CACHE = LoggerFactory.getLogger("META-INF.cache.log"),

    RSD = LoggerFactory.getLogger("META-INF.srd.log"),

    HTTP = LoggerFactory.getLogger("META-INF.http.log"),

    PROFILE = LoggerFactory.getLogger("META-INF.profile.log");
}
