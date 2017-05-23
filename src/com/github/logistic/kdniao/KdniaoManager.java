package com.github.logistic.kdniao;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.IOUtils;
import com.github.logistic.entity.ShipperCompany;
import com.github.logistic.entity.Trace;
import com.github.logistic.entity.UnityLogistic;
import com.github.logistic.enumerate.State;
import com.github.logistic.enumerate.Status;
import com.github.logistic.enumerate.TracesOrder;
import com.github.logistic.kdniao.entity.KdniaoROrderData;
import com.github.logistic.kdniao.entity.KdniaoRdata;
import com.github.logistic.kdniao.entity.KdniaoRdataTrace;
import com.github.logistic.kdniao.entity.RequestData;
import com.github.logistic.manager.Logistic;
import com.github.logistic.manager.abs.AbstarctLogisticManager;
import com.github.logistic.utils.PropertiesUtils;
import com.github.logistic.utils.StandarShipperCompany;
/**
 * 快递鸟Manager，提供快递查询等方法。
 */
public class KdniaoManager extends AbstarctLogisticManager {

    private static final Properties PROP = PropertiesUtils.load("com/github/logistic/kdniao/shipper.properties");

    private static final Logger LOG = LoggerFactory.getLogger(KdniaoManager.class);

    private String requestUrl = "http://api.kdniao.cc/Ebusiness/EbusinessOrderHandle.aspx";
    
    private String eBusinessID;
    private String appKey;

    public KdniaoManager(String eBusinessID, String appKey) {
        this.eBusinessID = eBusinessID;
        this.appKey = appKey;
    }

    public KdniaoManager(String requestUrl, String eBusinessID, String appKey) {
        this.requestUrl = requestUrl;
        this.eBusinessID = eBusinessID;
        this.appKey = appKey;
    }
    
	/**
     * Json方式 单号识别
     */
	public List<ShipperCompany> getOrderTracesByJson(String logisticCode){
		 //设置基础信息
        UnityLogistic logistic = new UnityLogistic();
		
		String requestData = JSON.toJSONString(new RequestData(logisticCode));
		//requestType:2002 查询方式
		List<NameValuePair> nvps = getNameValuePars(requestData,"2002");
		String body = null;
        try {
        	//请求地址
            body = doPost(getRequestUrl(), nvps);
        } catch (IOException e) {
            //网络问题，捕获异常，是否需要重试！！！
            LOG.error("请求接口网络出错", e);
            logistic.setStatus(Status.ERROR);
        }
        LOG.info(body);
        //接收返回数据
        KdniaoROrderData returnData = JSON.parseObject(body, KdniaoROrderData.class);
        
		return returnData.getShippers();
	}
	
	
    /***
     * 
     * shipperCompanyCode 物流公司代码
     * logisticCode 快递单号
     * tracesOrder 排序方式
     */
    @Override
    public Logistic doQuery(String shipperCompanyCode, String logisticCode, TracesOrder tracesOrder) {
        //设置基础信息
        UnityLogistic logistic = new UnityLogistic();
        logistic.setCode(shipperCompanyCode);
        //根据物流公司代码在Properties文件查询
        String selfShipperCode = getProperties().getProperty(shipperCompanyCode);
        //根据Code查询物流公司名称
        ShipperCompany shipperCompany = StandarShipperCompany.getStandarShipperCompany(selfShipperCode);
        //初始化物流公司新
        logistic.setShipperCompany(new ShipperCompany(shipperCompanyCode, shipperCompany.getShipperName()));

        //组装请求基础信息
        String requestData = JSON.toJSONString(new RequestData(selfShipperCode, logisticCode));
        //RequestType：请求指令类型
        List<NameValuePair> nvps = getNameValuePars(requestData,"1002");
        String body = null;
        try {
        	//请求地址
            body = doPost(getRequestUrl(), nvps);
        } catch (IOException e) {
            //网络问题，捕获异常，是否需要重试！！！
            LOG.error("请求接口网络出错", e);
            logistic.setStatus(Status.ERROR);
            return logistic;
        }

        LOG.info(body);
        //接收返回数据
        KdniaoRdata returnData = JSON.parseObject(body, KdniaoRdata.class);
        boolean isSuccess = false;
        if(returnData.isSuccess() && returnData.getState() != null) {
            logistic.setStatus(Status.SUCCESS);
            isSuccess = true;
        } else  {
            logistic.setStatus(Status.NO_RESULT);
        }
        logistic.setMessage(returnData.getReason());
        if(!isSuccess) {
            return logistic;
        }
        logistic.setState(getState(returnData.getState()));
        if(returnData.getTraces() != null && !returnData.getTraces().isEmpty()) {
            for(KdniaoRdataTrace trace : returnData.getTraces()) {
                logistic.addTrace(new Trace(trace.getAcceptTime(), trace.getAcceptStation()));
            }
            if(tracesOrder.isDesc()) {
                Collections.reverse(logistic.getTraces());
            }
        }
        return logistic;
    }


    private State getState(String state) {
        if(state == null) {
            return State.UNKOWN;
        }
        switch (state) {
            case "2" :
                return State.WAY;
            case "3" :
                return State.SIGNIN;
            case "4" :
                return State.DIFFICULT;
            default:
                LOG.error("快递鸟接口返回未知状态 [{}]", state);
                return State.UNKOWN;
        }
    }

    private String doPost(String url, List<NameValuePair> nvps) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        String body = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            response = httpclient.execute(httpPost);

            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() != 200) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            body = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        } finally {
            IOUtils.close(response);
            IOUtils.close(httpclient);
        }
        return body;
    }

    @Override
    protected Properties getProperties() {
        return PROP;
    }


    private List<NameValuePair> getNameValuePars(String requestData,String requestType) {
        List<NameValuePair> nvps = new ArrayList<>(5);
        try {
            nvps.add(new BasicNameValuePair("RequestData", URLEncoder.encode(requestData, "UTF-8")));
            String dataSign = encrypt(requestData, getAppKey());
            nvps.add(new BasicNameValuePair("DataSign", URLEncoder.encode(dataSign, "UTF-8")));
        } catch (UnsupportedEncodingException e) {
            // not happened!!
        }
        nvps.add(new BasicNameValuePair("EBusinessID", geteBusinessID()));
        nvps.add(new BasicNameValuePair("RequestType", requestType));
        nvps.add(new BasicNameValuePair("DataType", "2"));
        return nvps;
    }

    private String encrypt(String content, String keyValue) {
        if (keyValue != null) {
            return Base64.encodeBase64String(StringUtils.getBytesUtf8(DigestUtils.md5Hex(content + keyValue)));
        }
        return Base64.encodeBase64String(DigestUtils.md5(content));
    }


    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String geteBusinessID() {
        return eBusinessID;
    }

    public void seteBusinessID(String eBusinessID) {
        this.eBusinessID = eBusinessID;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

}

