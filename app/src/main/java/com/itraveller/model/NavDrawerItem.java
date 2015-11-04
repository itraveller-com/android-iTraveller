package com.itraveller.model;


public class NavDrawerItem {
    private boolean showNotify;
    private String title;
    private int icon;

    public NavDrawerItem() {

    }

    public int getIcon(){
        return this.icon;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }

    public boolean isShowNotify(){
        return showNotify;
    }

    public void setShowNotify(boolean shownotify){
        this.showNotify=shownotify;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
