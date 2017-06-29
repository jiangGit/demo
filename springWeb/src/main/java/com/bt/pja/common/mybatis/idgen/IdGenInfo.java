package com.bt.pja.common.mybatis.idgen;

/**
 * Created by chenjiapeng on 2015/6/24 0024.
 */
public class IdGenInfo {
    private String name;
    private long current;
    private int step;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
