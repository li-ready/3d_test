package com.com.Gameinstance;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.com.itf.Shape;
import com.tools.stools;

public class chess extends ModelInstance {
 public boolean isCamp() {
  return camp;
 }
 //走棋判断,需要子类重写
public boolean zouqiJudge(int x,int y){return false;}
 public void setCamp(boolean camp) {
  this.camp = camp;
 }

 private boolean camp=true;//true:红方,false:黑方
 private int qipanx;

 public int getQipanx() {
  return qipanx;
 }

 public void setQipanx(int qipanx) {
  this.qipanx = qipanx;
 }

 public int getQipany() {
  return qipany;
 }

 public void setQipany(int qipany) {
  this.qipany = qipany;
 }

 private int qipany;
 private boolean isAnimation=false;
 private float animation进度;

 public float getPathAnimationspeed() {
  return pathAnimationspeed;
 }

 private float pathAnimationspeed=1f;
 private Vector3 vector3out;
 public Vector3 vector3temp;
 private Array<Vector3> vector3Arraytemp;
 private Vector3[] pathdataset;
 private BSpline<Vector3> myBspline;
 private Shape vishape;
 public boolean isVisible(Camera cam) {
  return vishape == null ? false : vishape.isVisible(transform, cam);
 }
 public float intersects(Ray ray) {
  return vishape == null ? -1f : vishape.intersects(transform, ray);
 }
 public void setqipanLocation(int x,int y){
   qipanx=x;qipany=y;
 }
 public void qipanLocationRefresh()
 {
  vector3out=stools.qipanLocationToLocation(qipanx,qipany,vector3out);
  this.transform.setTranslation(vector3out);
 }
public void jump(int x,int y)
{
  qipanx=x;qipany=y;
  this.transform.setTranslation(stools.qipanLocationToLocation(x,y,vector3out));
}

public chess(Model model, String rootNode, boolean mergeTransform)
{
 super(model, rootNode, mergeTransform);
 //使用一整个模型,用字符串找到模型的一个独立节点,是否用节点在模型原始位置来构造自己
 vishape=new Sphere(4f);
 pathdataset=new Vector3[40];
 for (int i = 0; i < 40; i++) {
  pathdataset[i]=new Vector3();
 }
 //实验,默认给出的路径
 myBspline=new BSpline<Vector3>();
 vector3out=new Vector3();
 vector3temp=new Vector3();
 animation进度=0;
}

 public void createPathAnimation(int x,int y)
 {
  if(!isAnimation) {
   isAnimation=true;
   vector3out = stools.qipanLocationToLocation(x, y, vector3out);
   pathdataset= stools.CreateQipanTralationKeyVector(pathdataset,qipanx,qipany,x,y,vector3out);
   System.out.println("动画的关键帧如下:");
   for (Vector3 v : pathdataset) {
    System.out.println("x= "+v.x+"  y= "+v.y+"  z= "+v.z);
   }
   myBspline=new BSpline<Vector3>(pathdataset,3,false);
   qipanx = x;
   qipany = y;
  }
 }
public Vector3 GetLocation()
{
 return this.transform.getTranslation(vector3temp);
}
public void TranslateByDirection(Vector3 direction,float length){
  this.transform.trn(direction.setLength(length));

}
public void runPathAnimation(float delatTime,boolean continuous) {
 if (isAnimation) {
  animation进度 += delatTime * pathAnimationspeed;
  if (animation进度 >= 1f) {
   animation进度 = 1f;
  }
 myBspline.valueAt(vector3out,animation进度);

  this.transform.setTranslation(vector3out);
  if (animation进度 == 1f) {
   animation进度=0;
   isAnimation = false;
  }
 }
}
public void TranslateByVector3(Vector3 translate)
{
 this.transform.trn(translate);
}

}
