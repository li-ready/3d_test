package com.Gameinstance;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.itf.Shape;

public class Sphere implements Shape {
    public float radius;
    private Vector3 out;
    public Sphere(float f) {
        out=new Vector3();
        radius = f;
    }

    @Override
    public boolean isVisible(Matrix4 transform, Camera cam) {
        return cam.frustum.sphereInFrustum(transform.getTranslation(out), radius);
    }

    @Override
    public float intersects(Matrix4 transform, Ray ray) {
        transform.getTranslation(out);
        final float len = ray.direction.dot(out.x-ray.origin.x, out.y-ray.origin.y, out.z-ray.origin.z);
        if (len < 0f)
            return -1f;
        float dist2 = out.dst2(ray.origin.x+ray.direction.x*len, ray.origin.y+ray.direction.y*len, ray.origin.z+ray.direction.z*len);
        return (dist2 <= radius * radius) ? dist2 : -1f;
    }
}