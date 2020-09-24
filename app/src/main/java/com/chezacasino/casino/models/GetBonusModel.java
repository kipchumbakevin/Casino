package com.chezacasino.casino.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetBonusModel {
    @SerializedName("num")
    @Expose
    private Integer num;
    @SerializedName("amo")
    @Expose
    private Integer amo;

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
    public Integer getAmo() {
        return amo;
    }

    public void setAmo(Integer amo) {
        this.amo = amo;
    }
}
