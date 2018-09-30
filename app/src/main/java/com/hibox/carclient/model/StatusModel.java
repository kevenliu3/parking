package com.hibox.carclient.model;

/**
 * Everyday is another day, keep going.
 * Created by Ramo
 * email:   327300401@qq.com
 * date:    2018/09/20 18:51
 * desc:
 */
public class StatusModel {

    /**
     * id : 1
     * leftCameraStatus : 0
     * middleCameraStatus : 0
     * rightCameraStatus : 0
     * keepAliveTime : 1537440159000
     * forbid : 1
     * startTime : 1537440160000
     */

    private int id;
    private int leftCameraStatus;
    private int middleCameraStatus;
    private int rightCameraStatus;
    private long keepAliveTime;
    private int forbid;
    private long startTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLeftCameraStatus() {
        return leftCameraStatus;
    }

    public void setLeftCameraStatus(int leftCameraStatus) {
        this.leftCameraStatus = leftCameraStatus;
    }

    public int getMiddleCameraStatus() {
        return middleCameraStatus;
    }

    public void setMiddleCameraStatus(int middleCameraStatus) {
        this.middleCameraStatus = middleCameraStatus;
    }

    public int getRightCameraStatus() {
        return rightCameraStatus;
    }

    public void setRightCameraStatus(int rightCameraStatus) {
        this.rightCameraStatus = rightCameraStatus;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public int getForbid() {
        return forbid;
    }

    public void setForbid(int forbid) {
        this.forbid = forbid;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
