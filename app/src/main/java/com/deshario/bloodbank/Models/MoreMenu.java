package com.deshario.bloodbank.Models;

import android.graphics.drawable.Drawable;

public class MoreMenu {
    private String title;
    private String desc;
    private Drawable icon;

    public MoreMenu() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
