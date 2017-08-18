package com.nvcomputers.ten.imagechooser.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kbibek on 2/11/16.
 */
public class ChosenImages {
    private String fileThumbnail;
    private List<ChosenImage> images;

    public ChosenImages() {
        this.images = new ArrayList<>();
    }

    public void addImage(ChosenImage image) {
        this.images.add(image);
    }

    public int size(){
        return images.size();
    }

    public ChosenImage getImage(int index){
        return images.get(index);
    }
    public String getFileThumbnail() {
        return fileThumbnail;
    }
}
