package com.chezacasino.casino.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrialsModel {
    @SerializedName("num")
    @Expose
    private Integer num;

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
