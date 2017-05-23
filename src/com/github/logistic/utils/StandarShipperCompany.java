package com.github.logistic.utils;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.github.logistic.entity.ShipperCompany;

/**
 * 标准StandarCompany对应关系
 */
public class StandarShipperCompany {

    private static String PATH = "recources/kuaidi.standar.properties";

    private static Map<String, ShipperCompany> STANDAR = new ConcurrentHashMap<>();

    static {
        Properties properties = PropertiesUtils.load(PATH, StandarShipperCompany.class.getClassLoader());
        ShipperCompany company = null;
        for(Map.Entry<Object, Object> entry : properties.entrySet()) {
            company = new ShipperCompany(entry.getKey().toString(), entry.getValue().toString());
            STANDAR.put(company.getShipperCode(), company);
        }
        STANDAR = Collections.unmodifiableMap(STANDAR);
    }

    public static ShipperCompany getStandarShipperCompany(String key) {
        return STANDAR.get(key);
    }

    public static void main(String[] args) {
        System.out.println(STANDAR);
    }

}
