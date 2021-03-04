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
@PropertySource("classpath:file-upload.properties")
public class FileUpload {
    private String imageUserFaceLocation;
    private String imageServerUrl;
    private String resource;
    private String basePath;

    private String ftpIp;
    private int sftpPort;
    private String sftpUsername;
    private String sftpPassword;
    private String sftpBasePath;


    private String ftpHttpPath;
    private int ftpHttpPort;

    public String getFtpHttpPath() {
        return ftpHttpPath;
    }

    public void setFtpHttpPath(String ftpHttpPath) {
        this.ftpHttpPath = ftpHttpPath;
    }

    public int getFtpHttpPort() {
        return ftpHttpPort;
    }

    public void setFtpHttpPort(int ftpHttpPort) {
        this.ftpHttpPort = ftpHttpPort;
    }

    public String getFtpIp() {
        return ftpIp;
    }

    public void setFtpIp(String ftpIp) {
        this.ftpIp = ftpIp;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public int getSftpPort() {
        return sftpPort;
    }

    public void setSftpPort(int sftpPort) {
        this.sftpPort = sftpPort;
    }

    public String getSftpUsername() {
        return sftpUsername;
    }

    public void setSftpUsername(String sftpUsername) {
        this.sftpUsername = sftpUsername;
    }

    public String getSftpPassword() {
        return sftpPassword;
    }

    public void setSftpPassword(String sftpPassword) {
        this.sftpPassword = sftpPassword;
    }

    public String getSftpBasePath() {
        return sftpBasePath;
    }

    public void setSftpBasePath(String sftpBasePath) {
        this.sftpBasePath = sftpBasePath;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

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
