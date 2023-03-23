package com.tools;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.BSpline;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import java.time.temporal.Temporal;

public abstract class stools {
    private stools() {}
    private Vector3 temp1=new Vector3();
    private Vector3 temp2=new Vector3();
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
    public static Vector3 qipanLocationToLocation(int x,int y,Vector3 out)
    {
        float lineoffset5=-2f;
        float lineoffset4=-1f;
        float lineoffset3=0;
        float lineoffset2=0;
        float lineoffset1=1f;
        float nlineoffset5=-2.2f;
        float nlineoffset4=-1.6f;
        float nlineoffset3=-0.7f;
        float nlineoffset2=-0.1f;
        float nlineoffset1=0.2f;
        if(y>0)
            out.set(-6.67928f,20.87766f,0f);
            else
            out.set(6.67928f,20.87766f,0f);
            out.add(((float) y)*10.787155f,0,((float) x)*10.787155f);
            switch (y){
                case 5:out.add(lineoffset5,0,0);break;
                case 4:out.add(lineoffset4,0,0);break;
                case 3:out.add(lineoffset3,0,0);break;
                case 2:out.add(lineoffset2,0,0);break;
                case 1:out.add(lineoffset1,0,0);break;
                case -5:out.add(-nlineoffset5,0,0);break;
                case -4:out.add(-nlineoffset4,0,0);break;
                case -3:out.add(-nlineoffset3,0,0);break;
                case -2:out.add(-nlineoffset2,0,0);break;
                case -1:out.add(-nlineoffset1,0,0);break;

            }
            return out;
    }
    public static Vector3 qipanLocationToLocation(boolean RB,int num, Vector3 out)
    {
        if(RB)
        {

        }
        return out;
    }
    public static Vector3[] CreateQipanTralationKeyVector(Vector3[] dataset,int s_x,int s_y,int e_x,int e_y,Vector3 temp) {
        dataset[0].set(qipanLocationToLocation(s_x, s_y, temp));
        dataset[39].set(qipanLocationToLocation(e_x, e_y, temp));
        dataset[2].set(dataset[39]);
        dataset[2].sub(dataset[0]);
        System.out.println("temp向量的值为 x= "+dataset[2].x+" y= "+dataset[2].y+" z= "+dataset[2].z);
        dataset[2].scl(1f/34f);
        temp.set(dataset[2]);
        System.out.println("temp向量的值为 x= "+temp.x+" y= "+temp.y+" z= "+temp.z);
        for (int i = 0; i < 3; i++) {
            dataset[i].set(dataset[0]);
        }
        dataset[3].set(dataset[0]);
        dataset[3].add(0, 6, 0);
        for (int i = 4; i < 37; i++) {
            dataset[i].set(dataset[i - 1]);
            dataset[i].add(temp);
        }
        for (int i = 37; i < 40; i++) {
            dataset[i].set(dataset[39]);
        }
        return dataset;
    }
}
