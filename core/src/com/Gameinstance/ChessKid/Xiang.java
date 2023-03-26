package com.Gameinstance.ChessKid;

import com.badlogic.gdx.graphics.g3d.Model;
import com.Gameinstance.chess;

public class Xiang extends chess {
    public Xiang(Model model, String rootNode, boolean mergeTransform) {
        super(model, rootNode, mergeTransform);
    }
    @Override
    public boolean zouqiJudge(int x,int y,int[][] fenbu){
        boolean temp=false;
        if(guoxian(x,y))return false;
        int y1=y-getQipany();int x1=x-getQipanx();
        if((x1*x1!=4)|(y1*y1)!=4)return false;
        if(fenbu[getArrayQx()+x1/2][getArrayQy()+y1/2]!=0)return false;
        return true;}
    public boolean takeqiJudge(chess chessP,int[][] fenbu){
        return zouqiJudge(chessP.getQipanx(),chessP.getQipany(),fenbu);
    }
}
