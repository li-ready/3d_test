package com.com.Gameinstance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.com.itf.Role;

public class chess implements Role {
 private Vector3 vector3temp;
 private ModelInstance body;
 private Array<Vector3> translationQueue=new Array<Vector3>();
 public chess()
 {
  vector3temp=new Vector3();
   body=null;
 }
public chess(ModelInstance instance)
{
 this();
 body=instance;
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
public void 走棋(int x,int y)
{
  vector3temp= this.body.transform.getTranslation(vector3temp);

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
