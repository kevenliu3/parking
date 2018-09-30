package com.hibox.carclient.model;

import java.util.List;

/**
 * Everyday is another day, keep going.
 * Created by Ramo
 * email:   327300401@qq.com
 * date:    2018/09/20 16:58
 * desc:
 */
public class ResultModel {

    /**
     * aaData : [{"id":6,"deviceid":1,"carnum":"鄂AC4C68","type":1,"ossurl":"https://illegal-video.oss-cn-shanghai.aliyuncs.com/1537415057571_114325","starttimestamp":1537408239983,"endtimestamp":1537408280064,"createtime":1537415078000,"sectionnumber":"213","stoptime":null},{"id":7,"deviceid":1,"carnum":"冀AC4C68","type":1,"ossurl":"https://illegal-video.oss-cn-shanghai.aliyuncs.com/1537415058744_97962","starttimestamp":1537408239983,"endtimestamp":1537408280064,"createtime":1537415078000,"sectionnumber":"213","stoptime":null},{"id":8,"deviceid":1,"carnum":"鄂A9B593","type":1,"ossurl":"https://illegal-video.oss-cn-shanghai.aliyuncs.com/1537415068518_72637","starttimestamp":1537408239983,"endtimestamp":1537408280064,"createtime":1537415078000,"sectionnumber":"213","stoptime":null},{"id":9,"deviceid":1,"carnum":"冀A9B593","type":1,"ossurl":"https://illegal-video.oss-cn-shanghai.aliyuncs.com/1537415071870_67837","starttimestamp":1537408239983,"endtimestamp":1537408280064,"createtime":1537415078000,"sectionnumber":"213","stoptime":null},{"id":10,"deviceid":1,"carnum":"鄂AP7779","type":1,"ossurl":"https://illegal-video.oss-cn-shanghai.aliyuncs.com/1537415073223_70490","starttimestamp":1537408239983,"endtimestamp":1537408280064,"createtime":1537415078000,"sectionnumber":"213","stoptime":null},{"id":11,"deviceid":1,"carnum":"鄂AP7779","type":3,"ossurl":"https://illegal-video.oss-cn-shanghai.aliyuncs.com/1537415073223_70490","starttimestamp":1537408239983,"endtimestamp":1537508280064,"createtime":1537415078000,"sectionnumber":"213","stoptime":null}]
     * iTotalDisplayRecords : 6
     * iTotalRecords : 6
     * sEcho : null
     */

    private int iTotalDisplayRecords;
    private int iTotalRecords;
    private List<CarModel> aaData;

    public int getITotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    public void setITotalDisplayRecords(int iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public int getITotalRecords() {
        return iTotalRecords;
    }

    public void setITotalRecords(int iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }


    public List<CarModel> getAaData() {
        return aaData;
    }

    public void setAaData(List<CarModel> aaData) {
        this.aaData = aaData;
    }

}
