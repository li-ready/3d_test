package com.tools;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public abstract class stools {
    private stools() {}
    public static String assetaddress(String a) {
        return "assets/"+a;
    }
    public static Vector3 GetModelInstanceLocation(ModelInstance model)
    {
        ModelInstance instance = new ModelInstance(model); // create an instance of the model
        BoundingBox bounds = new BoundingBox(); // create a bounding box to hold the bounds of the model
        instance.calculateBoundingBox(bounds); // calculate the bounds of the model
        Vector3 out =new Vector3(0,0,0);
        Vector3 center = bounds.getCenter(out); // get the center point of the bounding box
        return center;
    }
}
