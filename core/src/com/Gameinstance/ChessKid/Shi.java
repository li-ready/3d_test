package com.Gameinstance.ChessKid;

import com.badlogic.gdx.graphics.g3d.Model;
import com.Gameinstance.chess;

public class Shi extends chess {

    public Shi(Model model, String rootNode, boolean mergeTransform) {
        super(model, rootNode, mergeTransform);
    }

    @Override
    public boolean zouqiJudge(int x,int y,int[][] fenbu){
        if(!jiugongge(x,y))return false;
        int y1=y-getQipany();int x1=x-getQipanx();
        if(x1*x1*y1*y1!=1)return false;
        boolean temp=false;
        return true;}
    public boolean takeqiJudge(chess chessP,int[][] fenbu){
        return  zouqiJudge(chessP.getQipanx(),chessP.getQipany(),fenbu);
    }
}
