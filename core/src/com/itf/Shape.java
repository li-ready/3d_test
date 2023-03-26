package com.itf;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.collision.Ray;

public interface Shape {//所有形状的接口,需要实现在屏幕上是否可见方法,是否被射中方法
    public abstract boolean isVisible(Matrix4 transform, Camera cam);
    /** @return -1 on no intersection, or when there is an intersection: the squared distance between the center of this
     * object and the point on the ray closest to this object when there is intersection. */
    public abstract float intersects(Matrix4 transform, Ray ray);
}//形状的基础抽象类可以有成员变量和非抽象方法,对派生类的行为和成员变量都做了限制