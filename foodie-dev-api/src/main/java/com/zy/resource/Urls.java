package com.zy.resource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 读取url-dev、prod.properties的类
 */
@Component
@ConfigurationProperties(prefix = "url")
//@PropertySource("classpath:file-upload-dev.properties")
@PropertySource("classpath:url-prod.properties")
public class Urls {
    private String paymentUrl;
    private String payReturnUrl;

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getPayReturnUrl() {
        return payReturnUrl;
    }

    public void setPayReturnUrl(String payReturnUrl) {
        this.payReturnUrl = payReturnUrl;
    }
}
