package com.zy.entity;
import java.io.Serializable;
import java.util.Date;
/***
*   
*/
public class PdmanDbVersion implements Serializable {
    //
    private String DBVERSION;
    //
    private String VERSIONDESC;
    //
    private String CREATEDTIME;
    //get set 方法
    public void setDBVERSION (String  DBVERSION){
        this.DBVERSION=DBVERSION;
    }
    public  String getDBVERSION(){
        return this.DBVERSION;
    }
    public void setVERSIONDESC (String  VERSIONDESC){
        this.VERSIONDESC=VERSIONDESC;
    }
    public  String getVERSIONDESC(){
        return this.VERSIONDESC;
    }
    public void setCREATEDTIME (String  CREATEDTIME){
        this.CREATEDTIME=CREATEDTIME;
    }
    public  String getCREATEDTIME(){
        return this.CREATEDTIME;
    }
}
