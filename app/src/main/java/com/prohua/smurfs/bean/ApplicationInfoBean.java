package com.prohua.smurfs.bean;

/**
 * ApplicationInfoBean 设备参数（暂没用）
 * Created by Deep on 2017/4/20 0020.
 */

public class ApplicationInfoBean {

    /*
    		'appid': 		plus.runtime.appid,
			'sys_version':	plus.os.version,
			'version':		inf.version,
			'imei':			plus.device.imei,
			'platform':		plus.os.name,
			'model':		plus.device.model,
			'app_version': 	plus.runtime.version,
			'plus_version': plus.runtime.innerVersion, //基座版本号
			'network_type':	'' + plus.networkinfo.getCurrentType(),
			'clientid':plus.push.getClientInfo().clientid,
			'iToken': plus.push.getClientInfo().token
     */

    // 版本名称 如：HBuilder
    private String appId;
    // 手机系统版本 如：4.4.2
    private String sysVersion;
    // 软件版本 2.0.1
    private String version;
    // 设备标识 8632...
    private String imei;
    // 平台 如：Android
    private String platform;
    // 手机型号
    private String model;
    // 客户端版本号
    private String appVersion;
    // 基座版本号
    private String plusVersion;
    // 网络类型 未知；0，未连接：1，有线网络：2，WIFI：3，2G：4，3G：5，4G：6
    private String netWorkType;
    // 推送服务令牌（设备唯一标识），用于标识推送信息接收者身份
    private String clientId;
    // 设备令牌（iOS设备唯一标识），用于APNS服务推送中标识设备的身份
    private String iToken;

    public ApplicationInfoBean(String appId, String sysVersion, String version, String imei, String platform, String model, String appVersion, String plusVersion, String netWorkType, String clientId, String iToken) {
        this.appId = appId;
        this.sysVersion = sysVersion;
        this.version = version;
        this.imei = imei;
        this.platform = platform;
        this.model = model;
        this.appVersion = appVersion;
        this.plusVersion = plusVersion;
        this.netWorkType = netWorkType;
        this.clientId = clientId;
        this.iToken = iToken;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSysVersion() {
        return sysVersion;
    }

    public void setSysVersion(String sysVersion) {
        this.sysVersion = sysVersion;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getPlusVersion() {
        return plusVersion;
    }

    public void setPlusVersion(String plusVersion) {
        this.plusVersion = plusVersion;
    }

    public String getNetWorkType() {
        return netWorkType;
    }

    public void setNetWorkType(String netWorkType) {
        this.netWorkType = netWorkType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getiToken() {
        return iToken;
    }

    public void setiToken(String iToken) {
        this.iToken = iToken;
    }
}
