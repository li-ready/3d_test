package com.com.AbstractInstance;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.com.itf.Shape;

public abstract class BaseShape implements Shape {
    protected final static Vector3 out = new Vector3();


    public BaseShape() {

    }
}