package com.Gameinstance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.itf.Shape;
import com.tools.stools;

public class chess extends ModelInstance {
 public int getRole_id() {
  return Role_id;
 }

 public void setRole_id(int role_id) {
  Role_id = role_id;
 }

 private int Role_id=0;
 public boolean isCamp() {
  return camp;
 }
 //走棋判断,需要子类重写
public boolean zouqiJudge(int x,int y,int[][] fenbu){return false;}
//吃棋判断,需要子类重写
public boolean takeqiJudge(chess chessP,int[][] fenbu){return false;}
 public void setCamp(boolean camp) {
  this.camp = camp;
 }
 private boolean camp=true;//true:红方,false:黑方
 private int qipanx;
public int getArrayQx(){
 return qipanx+4;
}
 public int getArrayQy(){
  return qipany+5;
 }
 public void setArrayQx(int x){
  qipanx=x-4;
 }public void setArrayQy(int y){
  qipanx=y-5;
 }
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
 private Bezier<Vector3> bezier;
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
 public boolean guoxian(int x,int y)
 {
  return qipany*y<0;
 }//过线则为true
 public boolean jiugongge(int x,int y)
 {
  return ((x<2)&&(x>-2)&&((y*(camp?-1:1)>2)));
 }//在九宫格内则为true
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
 pathdataset=new Vector3[4];
 for (int i = 0; i < 4; i++) {
  pathdataset[i]=new Vector3();
 }
 //实验,默认给出的路径
 bezier=new Bezier<Vector3>();
 vector3out=new Vector3();
 vector3temp=new Vector3();
 animation进度=0;
}

 public void createPathAnimation(int x,int y,int[][] fenbu)
 {
  /*{//获取精确时间,待删除
   long timestamp = System.currentTimeMillis();
   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
   String date = sdf.format(new Date(timestamp));
   System.out.println(date);
  }*/
  fenbu[getArrayQx()][getArrayQy()]=0;

  //输出目的棋盘坐标,待删除
  //System.out.println("Qx= "+x+"  Qy= "+y);
  if(!isAnimation) {
   isAnimation=true;
   vector3out = stools.qipanLocationToLocation(x, y, vector3out);
   pathdataset= stools.CreateQipanTralationKeyVector(pathdataset,qipanx,qipany,x,y,vector3out);
   //输出关键帧序列,待删除
   /*System.out.println("动画的关键帧如下:");
   for (Vector3 v : pathdataset) {
    System.out.println("x= "+v.x+"  y= "+v.y+"  z= "+v.z);
   }*/
   //myBspline=new BSpline<Vector3>(pathdataset,3,false);
   bezier.set(pathdataset);
   qipanx = x;
   qipany = y;
   fenbu[getArrayQx()][getArrayQy()]=  camp?1:-1;
   /*{//获取精确时间,待删除
    long timestamp = System.currentTimeMillis();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    String date = sdf.format(new Date(timestamp));
    System.out.println(date);
   }*/
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
  bezier.derivativeAt(vector3out, animation进度);
  animation进度 += Gdx.graphics.getDeltaTime() * pathAnimationspeed;
  if (animation进度 >= 1f) {
   animation进度 = 1f;
  }
 bezier.valueAt(vector3out,animation进度);

  this.transform.setTranslation(vector3out);
  if (animation进度 == 1f) {
   //输出动画终点坐标,待删除
   //System.out.println("x= "+this.transform.getTranslation(vector3out).x+"  y=  "+this.transform.getTranslation(vector3out).x+"  z=  "+this.transform.getTranslation(vector3out).z);
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
