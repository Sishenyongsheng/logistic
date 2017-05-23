package com.github.logistic.manager;

import com.github.logistic.enumerate.TracesOrder;

/**
 * 物流Manager
 */
public interface LogisticManager extends Order {

    /**
     * 是否支持当前物流公司代码
     * @param shipperCompanyCode
     * @return
     */
    boolean supports(String shipperCompanyCode);

    /**
     * 物流查询，返回物流对象。其他物流通用接入
     * @param shipperCompanyCode 物流公司代码
     * @param logisticCode
     * @return
     */
    Logistic query(String shipperCompanyCode, String logisticCode, TracesOrder tracesOrder);

    /**
     * 物流查询，返回物流对象。其他物流通用接入
     * 返回的跟踪的排序为：{@link TracesOrder#DESC}
     * @param shipperCompanyCode 物流公司代码
     * @param logisticCode 物流号
     * @return
     */
    Logistic query(String shipperCompanyCode, String logisticCode);
    
    
    /****
     * 物流查询，返回物流对象。
     * 只根据物流号查询
     * @param logisticCode
     * @return
     */
    Logistic queryByCode(String logisticCode,TracesOrder tracesOrder);
    
    
    /****
     * 物流查询，返回物流对象。
     * 返回的跟踪的排序为：{@link TracesOrder#DESC}
     * @param logisticCode
     * @return
     */
    Logistic queryByCode(String logisticCode);
    
    
}
