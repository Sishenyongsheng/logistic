package com.github.logistic.kdniao.entity;


import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.logistic.entity.ShipperCompany;


/***
 * 快递鸟 返回 单号识别
 * @author Administrator
 *
 */
public class KdniaoROrderData {

	@JSONField(name = "EBusinessID")
	private String eBusinessID;
	@JSONField(name = "LogisticCode")
	private String logisticCode;
	@JSONField(name="Success")
    private boolean success;
	@JSONField(name="Code")
	private Integer code;
	@JSONField(name="Shippers")
	private List<ShipperCompany> shippers;
	
	
	
	public KdniaoROrderData() {
		super();
	}
	public String geteBusinessID() {
		return eBusinessID;
	}
	public void seteBusinessID(String eBusinessID) {
		this.eBusinessID = eBusinessID;
	}
	public String getLogisticCode() {
		return logisticCode;
	}
	public void setLogisticCode(String logisticCode) {
		this.logisticCode = logisticCode;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public List<ShipperCompany> getShippers() {
		return shippers;
	}
	public void setShippers(List<ShipperCompany> shippers) {
		this.shippers = shippers;
	}


	
	
}
