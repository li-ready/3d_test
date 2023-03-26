package com.Gameinstance.ChessKid;

import com.badlogic.gdx.graphics.g3d.Model;
import com.Gameinstance.chess;

public class Ma extends chess {
    public Ma(Model model, String rootNode, boolean mergeTransform) {
        super(model, rootNode, mergeTransform);
    }
    //走棋判断的实现

    @Override
    public boolean zouqiJudge(int x,int y,int[][] fenbu){
        boolean temp=false;
        boolean guo=guoxian(x, y);
        int x1=0,y1=0;
        if(guo){
            y1=(y>0?-1:1)+y-getQipany();
        }
        else {
            y1 = y - getQipany();
        }
        //System.out.println("x= "+x+"  y= "+y);
        x1=x-getQipanx();
        System.out.println("x1= "+x1+"  y1= "+y1);
        if((x1==2)&&(y1==1|y1==-1))
            temp=fenbu[getArrayQx()+1][getArrayQy()]==0;
        if((x1==-2)&&(y1==1|y1==-1))
            temp=fenbu[getArrayQx()-1][getArrayQy()]==0;
        if((y1==2)&&(x1==1|x1==-1))
            temp=fenbu[getArrayQx()][getArrayQy()+1+(getQipany()==-1?1:0)]==0;
        if((y1==-2)&&(x1==1|x1==-1))
            temp=fenbu[getArrayQx()][getArrayQy()-1+(getQipany()==1?-1:0)]==0;
        return temp;}
    //吃棋的实现
    @Override
    public boolean takeqiJudge(chess chessP,int[][] fenbu){
        return zouqiJudge(chessP.getQipanx(),chessP.getQipany(),fenbu);}
}
