package com.github.logistic.utils;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.github.logistic.manager.exception.LogisticException;

/**
 * Created by chongdi.yang on 2016/8/8.
 */
public abstract class PropertiesUtils {

    /**
     * 加载相对于classpath的文件 path 不要加前缀 /
     * @param path
     * @param classLoader
     * @return
     */
    public static Properties load(String path, ClassLoader classLoader) {
        Properties properties = new Properties();
        InputStream is = classLoader.getResourceAsStream(path);
        try {
            properties.load(is);
        } catch (IOException e) {
            throw new LogisticException("load properties fail", e);
        } finally {
            try {
                if(is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }
        }
        return properties;
    }


    public static Properties load(String path) {
        return load(path, PropertiesUtils.class.getClassLoader());
    }
}
