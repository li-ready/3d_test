package com.Gameinstance.ChessKid;

import com.badlogic.gdx.graphics.g3d.Model;
import com.Gameinstance.chess;

public class Pao extends chess {
    public Pao(Model model, String rootNode, boolean mergeTransform) {
        super(model, rootNode, mergeTransform);
    }
    @Override
    public boolean zouqiJudge(int x,int y,int[][] fenbu){
        int y1=y-getQipany();int x1=x-getQipanx();
        if(x1*y1!=0)return false;
        boolean xoy=x1!=0;//x为移动方向则为true
        boolean zf=x1+y1>0;
        int ix=getArrayQx();int iy=getArrayQy();
        int j=xoy?x1:y1;
        j=j>0?j:-j;

        for (int i = 1; i <j ; i++) {
            /*{//获取精确时间,待删除
                long timestamp = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                String date = sdf.format(new Date(timestamp));
                System.out.println(date);
            }*/
            if(xoy) {
                if (fenbu[getArrayQx() + i * (zf ? 1 : -1)][getArrayQy()] != 0) return false;
            }
            else {
                if (fenbu[getArrayQx()][getArrayQy() + i * (zf ? 1 : -1)] != 0) return false;
            }
            /*{//获取精确时间,待删除
                long timestamp = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                String date = sdf.format(new Date(timestamp));
                System.out.println(date);
            }*/
        }

        return true;
    }
    public boolean takeqiJudge(chess chessP,int[][] fenbu){
        int y1=chessP.getQipany()-getQipany();int x1=chessP.getQipanx()-getQipanx();
        if(x1*y1!=0)return false;
        boolean xoy=x1!=0;//x为移动方向则为true
        boolean zf=x1+y1>0;
        int ix=getArrayQx();int iy=getArrayQy();
        int j=xoy?x1:y1;
        j=j>0?j:-j;
        int count=0;
        for (int i = 1; i <j ; i++) {
            if(xoy) {
                if (fenbu[getArrayQx() + i * (zf ? 1 : -1)][getArrayQy()] != 0) {
                    count++;
                }
            }
                else
                {if(fenbu[getArrayQx()][getArrayQy() + i * (zf ? 1 : -1)] != 0)count++;}
        }
        return count==1;
    }
}
