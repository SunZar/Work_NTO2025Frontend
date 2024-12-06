package ru.myitschool.work.ui;

import com.google.gson.annotations.SerializedName;

public class Door {
    @SerializedName("value")
    private String code;

    public Door(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
