package com.com.Gameinstance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.com.itf.Role;

public class chess implements Role {
 private boolean isAnimation=false;
 private float animation进度;
 private float pathAnimationspeed=0.6f;
 private Vector3 vector3out;
 public Vector3 vector3temp;
 private ModelInstance body;
 private Array<Vector3> vector3Arraytemp;
 private Vector3[] pathdataset;
 public chess()
 {
  vector3out=new Vector3();
  vector3temp=new Vector3();
  animation进度=0;




   body=null;
 }
public chess(ModelInstance instance)
{
 this();
 body=instance;
 Vector3 translation=body.transform.getTranslation(vector3out);
 pathdataset=new Vector3[40];
 for (int i = 0; i < 3; i++) {
  pathdataset[i]=new Vector3 (translation.add(0,0,0));
 }
 translation.add(0,6,0);
 for(int i=3;i<36;i++)
 {
  pathdataset[i]=new Vector3 (translation.add(-1,0,0));
 }
 translation.add(1,-6,0);
 for (int i = 36; i < 40; i++) {
  pathdataset[i]=new Vector3 (translation.add(0,0,0));
 }
 //实验,默认给出的路径
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
public void startPathAnimation()
{
  if(!isAnimation)
  {
   animation进度=0;
   isAnimation=true;
  }
}
public void runPathAnimation(float delatTime,boolean continuous) {
 if (isAnimation) {
  animation进度 += delatTime * pathAnimationspeed;
  if (animation进度 >= 1f) {
   animation进度 = 1f;
   isAnimation = false;
  }

  //CatmullRomSpline.calculate(vector3out, animation进度, pathdataset, continuous, vector3temp);

  BSpline.calculate(vector3out, animation进度, pathdataset,3, continuous, vector3temp);
 /* {
   int i = (int) (animation进度 * 40);
   if (animation进度 == 1f)
    i = 39;
   vector3out.set(pathdataset[i]);
  }*/
  body.transform.setTranslation(vector3out);
 }
}
public void setPathDateset()
{
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
