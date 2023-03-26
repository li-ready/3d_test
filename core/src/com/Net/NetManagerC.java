package com.Net;

import com.example.xiangqiNet;
import com.example.xiangqiNet;
import com.itf.ClientNetSender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetManagerC implements ClientNetSender {
    private int client_id=1000;
    private BufferedReader in;
    private PrintWriter out;
    private xiangqiNet game;
    private String inputLine;
    private String temp1;
    private String temp2;
    private Socket socket;
    public int port;

    public NetManagerC(){

    }
    public void Setgame(xiangqiNet game3){
        game=game3;
        while(true)
        {
            jieshou();
        }
    }
    public String jia括号int(int ss){
        temp1="["+ss+"]";
        return temp1;
    }
    @Override
    public void zouqi(int ORole_id, int x, int y) {
        out.println(client_id+ "Zouqi000:"+ORole_id+","+x+","+y);
    }

    @Override
    public void takeqi(int ORole_id, int PRole_id) {
        out.println(client_id+"Takeqi00:"+ORole_id+","+PRole_id);
    }

    @Override
    public void AskRefresh() {

    }

    @Override
    public void ExtendData(String data) {

    }
    @Override
    public void connect(int port1) throws IOException {
        port =port1;
        socket = new Socket("localhost", port1);
        System.out.println("Connected to server on port "+port1+"...");

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        send_this_id();
        /*if((inputLine=in.readLine())!=null){
            System.out.println("inputLine "+inputLine);
            client_id=Integer.parseInt(inputLine.substring(0,4));
        }*/
    }
    public boolean send_this_id(){
        out.println(client_id+ "Idsend00:"+client_id);
        return true;
    }
    public void jieshou() {
        try {
            while ((inputLine = in.readLine()) != null) {
                System.out.println("accept"+game.RB+"  "+inputLine);//待删除
                temp1=inputLine.substring(0,8);
                System.out.println("temp1="+temp1);
                int i=inputLine.indexOf(":");
                if(i!=-1){
                    temp2=inputLine.substring(i+1,inputLine.length());
                    System.out.println("temp2="+temp2);
                }
                switch (temp1){
                    case "refresh0"://server.RefreshAsk(client_id);
                        break;
                    case "Zouqi000":{
                        int x=0;int y=0;
                        String[] result = temp2.split(",");
                        if(result.length>2) {
                            game.NetZouqi(Integer.parseInt(result[0]), Integer.parseInt(result[1]), Integer.parseInt(result[2]));
                        }
                        break;
                    }
                    case "Takeqi00":{
                        String[] result = temp2.split(",");
                        game.NetTakeqi(Integer.parseInt(result[0]), Integer.parseInt(result[1]));
                        break;
                    }
                    case "ExData00":{
                        //server.ExtendData(temp2,client_id);break;
                    }
                    case "Adisconn":{
                        //server.Client_AskDisconnected(client_id);
                    }
                    case "RBset000":{
                        game.RB=(Integer.parseInt(temp2)==0);
                        System.out.println(game.RB);
                        }break;
                    case "Idset000":{
                        client_id=Integer.parseInt(temp2);
                    }break;
                }

            }
        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Error closing client socket: " + e.getMessage());
            }
        }
    }
}
