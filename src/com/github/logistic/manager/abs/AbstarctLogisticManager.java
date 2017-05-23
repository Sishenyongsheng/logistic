package com.github.logistic.manager.abs;

import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.github.logistic.entity.ShipperCompany;
import com.github.logistic.entity.UnityLogistic;
import com.github.logistic.enumerate.Status;
import com.github.logistic.enumerate.TracesOrder;
import com.github.logistic.manager.Logistic;
import com.github.logistic.manager.LogisticManager;

/**
 * 抽象实现LogisticManager
 */
public abstract class AbstarctLogisticManager implements LogisticManager {

    @Override
    public boolean supports(String shipperCompanyCode) {
        if(getProperties() != null) {
            return getProperties().getProperty(shipperCompanyCode) != null;
        }
        return false;
    }

    @Override
    public Logistic query(String shipperCompanyCode, String logisticCode, TracesOrder tracesOrder) {
    	Validate.notBlank(logisticCode, "物流号不能为空");
        Validate.notBlank(shipperCompanyCode, "物流公司代码不能为空");
        
        if(tracesOrder == null) {
            tracesOrder = TracesOrder.DESC;
        }
        return doQuery(shipperCompanyCode, logisticCode, tracesOrder);
    }
    
    
	@Override
	public Logistic queryByCode(String logisticCode, TracesOrder tracesOrder) {

		List<ShipperCompany> shipperCompanys = getOrderTracesByJson(logisticCode);
		if(shipperCompanys.size() == 1){
			ShipperCompany shipperCompany = shipperCompanys.get(0);
			if(tracesOrder == null) {
		            tracesOrder = TracesOrder.DESC;
	        }
	        return this.query(shipperCompany.getShipperCode(), logisticCode, tracesOrder);
		}else{
			Validate.notBlank(logisticCode, "请输入正确的物流号");
			UnityLogistic logistic = new UnityLogistic();
			logistic.setStatus(Status.NO_RESULT);
			return logistic;
		}
	}
	
	@Override
	public Logistic queryByCode(String logisticCode) {
		return queryByCode(logisticCode,  TracesOrder.DESC);
	}


    @Override
    public Logistic query(String shipperCompanyCode, String logisticCode) {
        return query(shipperCompanyCode, logisticCode, TracesOrder.DESC);
    }

    
    public abstract Logistic doQuery(String shipperCompanyCode, String logisticCode, TracesOrder tracesOrder);

    public abstract List<ShipperCompany> getOrderTracesByJson(String logisticCode);
    
    @Override
    public int order() {
        return 0;
    }

    /**
     * 如果实现了该方法，可以不用重写{@link AbstarctLogisticManager#supports(String)}方法， 否则需要重写 supports方法。
     */
    protected Properties getProperties() {
        return new Properties();
    }
}
