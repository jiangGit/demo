package com.bt.pja.common.result.popups;

/**
 * Created by chenjiapeng on 2015/6/26 0026.
 */
public abstract class Popups {
    protected int type;
    private String content;

    public Popups(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }
}
