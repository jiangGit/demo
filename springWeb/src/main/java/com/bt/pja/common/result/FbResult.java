package com.bt.pja.common.result;



import com.bt.pja.common.result.popups.Dialog;
import com.bt.pja.common.result.popups.Popups;
import com.bt.pja.common.result.popups.Toast;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

public class FbResult {
    int code;
    Popups popups;
    Object data;

    private FbResult(int code) {
        this.code = code;
    }

    private FbResult(Object data) {
        this.data = data;
    }

    public Popups getPopups() {
        return popups;
    }

    public FbResult setPopups(Popups popups) {
        this.popups = popups;
        return this;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public boolean success() {
        return code >= 0;
    }

    public boolean error() {
        return !success();
    }

    public FbResult toast(String content) {
        setPopups(new Toast(content));
        return this;
    }

    public FbResult simpleDialog(String title, String content) {
        return setPopups(new Dialog(title, content).addButtons(Dialog.Button.BUTTON_CLOSE));
    }

    public ModelAndView toView(String name, HttpServletResponse response) {
        return toView(name, 200, response);
    }

    public ModelAndView toView(String name, int status, HttpServletResponse response/* 302, 301 */) {
        response.setStatus(status);
        return toView(name);
    }

    public ModelAndView toView(String name) {
        ModelAndView modelAndView = new ModelAndView(name);
        modelAndView.addObject("code", getCode())
                .addObject("popups", getPopups())
                .addObject("data", getData());
        return modelAndView;
    }

    public static FbResult SUCCESS(Object data) {
        return new FbResult(data);
    }

    public static FbResult SUCCESS() {
        return new FbResult(null);
    }

    public static FbResult ERROR(int code) {
        return new FbResult(code);
    }

    public static FbResult ERROR(ResultCode resultCode) {
        FbResult fbResult = new FbResult(resultCode.getCode());
        if (!StringUtils.isBlank(resultCode.getDetail())) {
            fbResult.toast(resultCode.getDetail());
        }
        return fbResult;
    }

    public static FbResult ERROR(ResultCode resultCode, Object data) {
        FbResult fbResult = new FbResult(resultCode.getCode());
        if (!StringUtils.isBlank(resultCode.getDetail())) {
            fbResult.toast(resultCode.getDetail());
        }
        fbResult.setData(data);
        return fbResult;
    }
}
