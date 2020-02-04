package com.example.android.library.cameraview.model;

import java.util.ArrayList;


public class TResult {
    private ArrayList<TImage> images;
    private TImage image;

    public static TResult of(TImage image) {
        ArrayList<TImage> images = new ArrayList<>(1);
        images.add(image);
        return new TResult(images);
    }

    public static TResult of(ArrayList<TImage> images) {
        return new TResult(images);
    }

    private TResult(ArrayList<TImage> images) {
        this.images = images;
        if (images != null && !images.isEmpty()) {
            this.image = images.get(0);
        }
    }

    public ArrayList<TImage> getImages() {
        return images;
    }

    public void setImages(ArrayList<TImage> images) {
        this.images = images;
    }

    public TImage getImage() {
        return image;
    }

    public void setImage(TImage image) {
        this.image = image;
    }
}
