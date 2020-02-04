package com.example.android.library.cameraview.model;

import java.io.Serializable;


public class TakePhotoOptions implements Serializable {
    /**
     * 是否使用TakePhoto自带的相册进行图片选择，默认不使用，但选择多张图片会使用
     */
    private boolean withOwnGallery;
    /**
     * 是对拍的照片进行旋转角度纠正
     */
    private boolean correctImage;

    private TakePhotoOptions() {
    }

    public boolean isWithOwnGallery() {
        return withOwnGallery;
    }

    public void setWithOwnGallery(boolean withOwnGallery) {
        this.withOwnGallery = withOwnGallery;
    }

    public boolean isCorrectImage() {
        return correctImage;
    }

    public void setCorrectImage(boolean correctImage) {
        this.correctImage = correctImage;
    }

    public static class Builder {
        private TakePhotoOptions options;

        public Builder() {
            this.options = new TakePhotoOptions();
        }

        public Builder setWithOwnGallery(boolean withOwnGallery) {
            options.setWithOwnGallery(withOwnGallery);
            return this;
        }

        public Builder setCorrectImage(boolean isCorrectImage) {
            options.setCorrectImage(isCorrectImage);
            return this;
        }

        public TakePhotoOptions create() {
            return options;
        }
    }
}
