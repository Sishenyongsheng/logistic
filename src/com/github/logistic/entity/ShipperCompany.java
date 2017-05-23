package com.github.logistic.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 快递公司
 */
public class ShipperCompany {

	@JSONField(name = "ShipperName")
    private String shipperName;
	@JSONField(name = "ShipperCode")
    private String shipperCode;

    public ShipperCompany(String shipperCode, String shipperName) {
        this.shipperName = shipperName;
        this.shipperCode = shipperCode;
    }


    public ShipperCompany() {
		super();
	}


	/**
     * 快递名称
     * @return
     */
	public String getShipperName() {
		return shipperName;
	}


	public void setShipperName(String shipperName) {
		this.shipperName = shipperName;
	}

	/**
     * 快递公司代码
     * @return
     */
	public String getShipperCode() {
		return shipperCode;
	}


	public void setShipperCode(String shipperCode) {
		this.shipperCode = shipperCode;
	}
    
    
    
}
