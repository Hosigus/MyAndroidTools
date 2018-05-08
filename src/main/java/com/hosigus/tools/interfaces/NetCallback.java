package com.hosigus.tools.interfaces;

import com.hosigus.tools.items.JSONBean;

/**
 * Created by 某只机智 on 2018/5/7.
 */

public interface NetCallback {
    void onSuccess(JSONBean data);
    void onFail(Exception e);
}
