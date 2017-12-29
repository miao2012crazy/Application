package com.xialan.app.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/26.
 */

public class ProductTabBean {

    /**
     * code : S
     * data : ["精品美妆","化妆护肤","美容美发","养生保健","生活用品"]
     */
    private String code;
    private List<String> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
