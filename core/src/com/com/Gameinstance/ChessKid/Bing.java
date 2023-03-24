package com.com.Gameinstance.ChessKid;

import com.badlogic.gdx.graphics.g3d.Model;
import com.com.Gameinstance.chess;

public class Bing extends chess {
    public Bing(Model model, String rootNode, boolean mergeTransform) {
        super(model, rootNode, mergeTransform);
    }
    @Override
    public boolean zouqiJudge(int x,int y,int[][] fenbu){
        boolean temp=false;
        int x1=0;int y1=0;
        boolean guo=guoxian(x,y);
        if(guo){
            y1=(y>0?-1:1)+y-getQipany();
        }
        else {
            y1 = y - getQipany();
        }
        //待删除
        System.out.println("isAcross= "+guo);
        System.out.println("qipanX= "+getQipanx()+" qipanY= "+getQipany());
        x1=x-getQipanx();
        System.out.println("x1,y1= "+x1+" "+y1);//待删除
        if((isCamp()&&y1<0)|(!isCamp()&&y1>0))return false;
        if(x1*y1!=0)return false;
        if((isCamp()&&(getQipany()<0)&&y1!=1)|(!isCamp()&&(getQipany()>0)&&y1!=-1))return false;
        //if(x1+y1>1|x1+y1<-1)return false;
        return true;
        }
    public boolean takeqiJudge(chess chessP,int[][] fenbu){
        return zouqiJudge(chessP.getQipanx(),chessP.getQipany(),fenbu);
    }
}
