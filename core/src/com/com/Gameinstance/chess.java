package com.com.Gameinstance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.com.itf.Role;
import com.tools.stools;

public class chess implements Role {
 private int qipanx;
 private int qipany;
 private boolean isAnimation=false;
 private float animation进度;
 private float pathAnimationspeed=1f;
 private Vector3 vector3out;
 public Vector3 vector3temp;
 private ModelInstance body;
 private Array<Vector3> vector3Arraytemp;
 private Vector3[] pathdataset;
 private BSpline<Vector3> myBspline;
 public void setqipanLocation(int x,int y){
   qipanx=x;qipany=y;
 }
 public void qipanLocationRefresh()
 {
  vector3out=stools.qipanLocationToLocation(qipanx,qipany,vector3out);
  body.transform.setTranslation(vector3out);
 }
public void jump(int x,int y)
{
  qipanx=x;qipany=y;
  body.transform.setTranslation(stools.qipanLocationToLocation(x,y,vector3out));
}
 public chess()
 {
  myBspline=new BSpline<Vector3>();
  vector3out=new Vector3();
  vector3temp=new Vector3();
  animation进度=0;
   body=null;
 }
public chess(ModelInstance instance)
{
 this();
 body=instance;
 pathdataset=new Vector3[40];
 for (int i = 0; i < 40; i++) {
  pathdataset[i]=new Vector3();
 }
 //实验,默认给出的路径
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
 return body.transform.getTranslation(vector3temp);
}
 @Override
 public void Role(ModelInstance modelInstance) {
  body=modelInstance;
 }

 @Override
 public void SetBody(ModelInstance modelInstance) {
  body=modelInstance;
 }
public void TranslateByDirection(Vector3 direction,float length){
  body.transform.trn(direction.setLength(length));

}
public void runPathAnimation(float delatTime,boolean continuous) {
 if (isAnimation) {
  animation进度 += delatTime * pathAnimationspeed;
  if (animation进度 >= 1f) {
   animation进度 = 1f;
  }
 myBspline.valueAt(vector3out,animation进度);
  /*CatmullRomSpline.calculate(vector3out, animation进度, pathdataset, continuous, vector3temp);

  BSpline.calculate(vector3out, animation进度, pathdataset,3, continuous, vector3temp);*/
 /* {
   int i = (int) (animation进度 * 40);
   if (animation进度 == 1f)
    i = 39;
   vector3out.set(pathdataset[i]);
  }*/
  body.transform.setTranslation(vector3out);
  if (animation进度 == 1f) {
   animation进度=0;
   isAnimation = false;
  }
 }
}
public void TranslateByVector3(Vector3 translate)
{
 body.transform.trn(translate);
}
 @Override
 public ModelInstance GetBody() {
  return body;
 }
}
