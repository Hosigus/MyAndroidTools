package com.hosigus.tools.items;

import com.hosigus.tools.interfaces.Parserable;

/**
 * Created by 某只机智 on 2018/5/7.
 */

public abstract class JSONBean implements Parserable {
    private String json;

    public void setJson(String json) {
        this.json = json;
    }

    public String getJson() {
        return json;
    }
}
