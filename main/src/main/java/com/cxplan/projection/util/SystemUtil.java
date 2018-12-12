package com.cxplan.projection.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * Created by Kenny Liu on 2018-05-31
 */
public class SystemUtil {

    private static final Logger logger = LoggerFactory.getLogger("system");
    public final static String separator = File.separator;
    public static final String APPLICATION_NAME;
    public static final String CONFIG_PATH;
    private static final Properties systemProperties;

    static {
        String tmpPath = System.getProperty("config.path");
        if (tmpPath == null) {
            tmpPath = "conf";
            System.setProperty("config.path", tmpPath);
        }
        CONFIG_PATH = tmpPath;

        systemProperties = loadPropertyFile(CONFIG_PATH + separator + "application.properties");
        String appName = (String)systemProperties.get("name");
        APPLICATION_NAME = appName;
    }

    public final static String basePath = System.getProperty("user.home") + separator + "." + APPLICATION_NAME + separator;
    public final static String DEFAULT_CACHE = basePath + "cache";

    public static String SETTING_FILE = basePath + "System.setting";

    public static String SETTING_SHORTCUT = basePath + "shortcut.setting";

    public static void checkUserDirectory() {
        File file = new File(basePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * Return the property value matched with specified name.
     * @param name the property name.
     */
    public static String getSystemProperty(String name) {
        return systemProperties.getProperty(name);
    }
    /**
     * Validate whether current os is mac os x
     *
     * @return true if os is mac, otherwise false.
     */
    public static boolean isMac() {
        String osName = System.getProperty("os.name");
        osName = osName.toLowerCase();
        if (osName.indexOf("mac") > -1) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * Validate whether current os is window
     *
     * @return true if os is mac, otherwise false.
     */
    public static boolean isWindow() {
        String osName = System.getProperty("os.name");
        osName = osName.toLowerCase();
        if (osName.indexOf("window") > -1) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * Validate whether current os is linux
     *
     * @return true if os is mac, otherwise false.
     */
    public static boolean isLinux() {
        String osName = System.getProperty("os.name");
        osName = osName.toLowerCase();
        if (osName.indexOf("linux") > -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Create file object for specified relative path,
     * The created file will be placed in user home directory.
     * And the parent directory of returned file will be created also if doesn't exist.
     */
    public static File createConfigFile(String path) {
        File file = new File(basePath, path);
        if (!file.exists()) {
            File dirs = file.getParentFile();
            if (!dirs.exists()) {
                dirs.mkdirs();
            }
        }

        return file;
    }

    public static void deleteBySystem(String path) throws IOException {
        if (isWindow()) {
            Runtime.getRuntime().exec("cmd /c rd/s/q " + path);
        } else {
            throw new UnsupportedEncodingException("当前系统不支持");
        }
    }

    public static void info(String message) {
        logger.info(message);
    }
    public static void info(String pattern, Object... param) {
        logger.info(pattern, param);
    }

    public static void error(String msg) {
        logger.error(msg);
    }

    public static void error(String msg, Throwable e) {
        logger.error(msg, e);
    }

    public static Properties loadPropertyFile(String path) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(path));
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        if (inputStream == null) {
            return null;
        }
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return properties;
    }
}