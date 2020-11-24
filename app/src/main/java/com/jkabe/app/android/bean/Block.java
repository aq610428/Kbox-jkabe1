package com.jkabe.app.android.bean;

import java.io.Serializable;

/**
 * @author: zt
 * @date: 2020/11/24
 * @name:Block
 */
public class Block implements Serializable {
    public Block(String name, int drawable) {
        this.name=name;
        this.drawable=drawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    private String name;
    private int drawable;

}
