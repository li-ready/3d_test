package com.com.Gameinstance;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.com.itf.Role;

public class chess implements Role {
 private Vector3 vector3temp;
 private ModelInstance body;
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
public void TranslateByVector3(Vector3 translate)
{
 body.transform.trn(translate);
}
 @Override
 public ModelInstance GetBody() {
  return body;
 }
}
