package com.github.logistic.entity;

import java.util.ArrayList;
import java.util.List;

import com.github.logistic.enumerate.State;
import com.github.logistic.enumerate.Status;
import com.github.logistic.manager.Logistic;

/**
 * 统一的物流出口实现
 */
public class UnityLogistic implements Logistic {
	/** 状态 */
    private Status status;
    private String message;
    /** 物流公司代码 */
    private String code;
    /** 物流单号 */
    private State state;
    /** 物流公司信息 */
    private ShipperCompany shipperCompany;
    /** 快件信息 */
    private List<Trace> traces = new ArrayList<>();

    public UnityLogistic() {
    }

    public UnityLogistic(Status status, String message, String code, State state, ShipperCompany shipperCompany) {
        this.status = status;
        this.message = message;
        this.code = code;
        this.state = state;
        this.shipperCompany = shipperCompany;
    }

    public UnityLogistic(Status status, String message, String code, State state, ShipperCompany shipperCompany, List<Trace> traces) {
        this.status = status;
        this.message = message;
        this.code = code;
        this.state = state;
        this.shipperCompany = shipperCompany;
        this.traces = traces;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public ShipperCompany shipperCompany() {
        return getShipperCompany();
    }

    public void setState(State state) {
        this.state = state;
    }

    public ShipperCompany getShipperCompany() {
        return shipperCompany;
    }

    public void setShipperCompany(ShipperCompany shipperCompany) {
        this.shipperCompany = shipperCompany;
    }

    @Override
    public List<Trace> getTraces() {
        return traces;
    }

    public void setTraces(List<Trace> traces) {
        this.traces = traces;
    }

    public void addTrace(Trace trace) {
        getTraces().add(trace);
    }
}
