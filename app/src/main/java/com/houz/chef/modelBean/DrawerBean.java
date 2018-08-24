package com.houz.chef.modelBean;

/**
 * Created by jay on 19-08-2017.
 */

public class DrawerBean {
    private String name;
    private int notiCount;

    public DrawerBean(String name, int count) {
        this.name = name;
        this.notiCount = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNotiCount() {
        return notiCount;
    }

    public void setNotiCount(int notiCount) {
        this.notiCount = notiCount;
    }
}
