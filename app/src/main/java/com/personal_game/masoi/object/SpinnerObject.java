package com.personal_game.masoi.object;

public class SpinnerObject {
    private String spinnerItemName;
    private int spinnerItemImg;

    public SpinnerObject(String spinnerItemName) {
        this.spinnerItemName = spinnerItemName;
    }

    public SpinnerObject(String spinnerItemName, int spinnerItemImg) {
        this.spinnerItemName = spinnerItemName;
        this.spinnerItemImg = spinnerItemImg;
    }

    public String getSpinnerItemName() {
        return spinnerItemName;
    }

    public int getSpinnerItemImg() {
        return spinnerItemImg;
    }
}
