package com.sistemosoft.muslimline;

/**
 * Created by appaz on 11/22/16.
 */
public class ModelClass {
    private String titleToDisplay;
    private String imageToDisplay;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageToDisplay() {
        return imageToDisplay;
    }

    public void setImageToDisplay(String imageToDisplay) {
        this.imageToDisplay = imageToDisplay;
    }

    public String getTitleToDisplay() {
        return titleToDisplay;
    }

    public void setTitleToDisplay(String titleToDisplay) {
        this.titleToDisplay = titleToDisplay;
    }
}
