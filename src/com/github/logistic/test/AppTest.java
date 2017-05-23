package com.github.logistic.test;


import com.alibaba.fastjson.JSON;
import com.github.logistic.manager.Logistic;
import com.github.logistic.utils.LogisticUtils;


/**
 * Unit test for simple App.
 */
public class AppTest {


    
    public static void main(String[] args) {

//        Logistic logistic = LogisticUtils.getKdniaoLogisticManager().query("", "884919001536936702");
        Logistic logistic = LogisticUtils.getKdniaoLogisticManager().queryByCode("884919001536936");
        System.out.println(JSON.toJSONString(logistic));
	}
}
