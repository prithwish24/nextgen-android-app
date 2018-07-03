package com.nextgen.carrental.app.model;

/**
 * Base class for all the widgets on confirmation page
 * @author Prithwish
 */

public class BaseWidget <T> {
    private String title;
    private T widget;

    public BaseWidget(T widget) {
        this.widget = widget;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public T getWidget() {
        return widget;
    }

}
