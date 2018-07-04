
package com.pychen0918.dramalistdemo.model.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DramaDataContainer {

    @SerializedName("data")
    @Expose
    private List<DramaData> data = null;

    public List<DramaData> getData() {
        return data;
    }

    public void setData(List<DramaData> data) {
        this.data = data;
    }

}
