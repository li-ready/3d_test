package com.Gameinstance.ChessKid;

import com.badlogic.gdx.graphics.g3d.Model;
import com.Gameinstance.chess;

public class Jiang extends chess {

    public Jiang(Model model, String rootNode, boolean mergeTransform) {
        super(model, rootNode, mergeTransform);
    }

    @Override
    public boolean zouqiJudge(int x,int y,int[][] fenbu){
        boolean temp=false;
        int x1=0;int y1=0;
        if(!jiugongge(x,y))return false;
        //待删除
        y1 = y - getQipany();
        x1=x-getQipanx();
        System.out.println("x1,y1= "+x1+" "+y1);//待删除
        //if((isCamp()&&y1<0)|(!isCamp()&&y1>0))return false;
        if(x1*y1!=0)return false;
        if(x1+y1==1|x1+y1==-1)return true;
        return false;
    }
    public boolean takeqiJudge(chess chessP,int[][] fenbu){
        return zouqiJudge(chessP.getQipanx(),chessP.getQipany(),fenbu);
    }
}
