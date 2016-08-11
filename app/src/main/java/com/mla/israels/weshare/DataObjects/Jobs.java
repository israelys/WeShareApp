package com.mla.israels.weshare.DataObjects;

/**
 * Created by Israel Sameach on 05/08/2016.
 */
public enum Jobs {
    Lecturer(1),
    Teacher(2),
    Kindergarden_Teacher(3),
    Assistant(4);

    private final int value;

    private Jobs(int value) {
        this.value = value;
    }
}
