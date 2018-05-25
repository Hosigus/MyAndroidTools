package com.hosigus.tools.options;

import com.hosigus.tools.items.JSONBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 某只机智 on 2018/5/7.
 */

public class NetOption {
    private String url;
    private Map<String, String> paramSet;
    private Class<? extends JSONBean> beanClass;


    public NetOption(String url) {
        this.url = url;
    }

    public NetOption param(String key, String value) {
        if (paramSet == null) {
            paramSet = new HashMap<>();
        }
        paramSet.put(key, value);
        return this;
    }

    public NetOption beanClass(Class<? extends JSONBean> beanClass) {
        this.beanClass = beanClass;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getParamSet() {
        return paramSet;
    }

    public Class<? extends JSONBean> getBeanClass() {
        return beanClass;
    }
}
