package com.zy.resource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 读取file-upload-dev、prod.properties的类
 */
@Component
@ConfigurationProperties(prefix = "file")
//@PropertySource("classpath:file-upload-dev.properties")
@PropertySource("classpath:file-upload-prod.properties")
public class FileUpload {
    private String imageUserFaceLocation;
    private String imageServerUrl;

    public String getImageServerUrl() {
        return imageServerUrl;
    }

    public void setImageServerUrl(String imageServerUrl) {
        this.imageServerUrl = imageServerUrl;
    }

    public String getImageUserFaceLocation() {
        return imageUserFaceLocation;
    }

    public void setImageUserFaceLocation(String imageUserFaceLocation) {
        this.imageUserFaceLocation = imageUserFaceLocation;
    }
}
