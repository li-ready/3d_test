package com.Net;

import com.example.xiangqiNet;
import com.example.xiangqitest;

import java.io.IOException;

public class NetThread extends Thread{
    private NetManagerC Net1;
    private xiangqiNet game;
    @Override
    public void run() {
        super.run();
        game.setNetmanager(Net1);
        Net1.Setgame(game);
    }
    public NetThread(NetManagerC Net3, xiangqiNet game4){
        game=game4;
        Net1=Net3;
    }
}
