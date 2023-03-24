package com.com.Gameinstance.ChessKid;

import com.badlogic.gdx.graphics.g3d.Model;
import com.com.Gameinstance.chess;

public class Che extends chess {
    public Che(Model model, String rootNode, boolean mergeTransform) {
        super(model, rootNode, mergeTransform);
    }
    @Override
    public boolean zouqiJudge(int x,int y,int[][] fenbu){
        //待删除
       /* for (int i = 0; i < 9; i++) {
            for (int j = 0; j <11 ; j++) {
                System.out.print(fenbu[i][j]+" ");
            }
            System.out.println();
        }*/
        int y1=y-getQipany();int x1=x-getQipanx();
        //System.out.println("x1= "+x1+"  y1= "+y1);//待删除
        if(x1*y1!=0)return false;
        boolean xoy=x1!=0;//x为移动方向则为true
        boolean zf=x1+y1>0;
        int ix=getArrayQx();int iy=getArrayQy();
        int j=xoy?x1:y1;
        //System.out.println("xoy= "+xoy+"  zf= "+zf+"  j= "+j);//待删除
        j=j>0?j:-j;
        for (int i = 1; i <j ; i++) {
            if(xoy) {
                if (fenbu[getArrayQx() + i * (zf ? 1 : -1)][getArrayQy()] != 0) return false;
            }
            else {
                if (fenbu[getArrayQx()][getArrayQy() + i * (zf ? 1 : -1)] != 0) return false;
            }
            }

        return true;}
    public boolean takeqiJudge(chess chessP,int[][] fenbu){
        return zouqiJudge(chessP.getQipanx(),chessP.getQipany(),fenbu);
    }
}
