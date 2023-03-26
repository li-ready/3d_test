package com.itf;

import java.io.IOException;

public interface ClientNetSender {
    public void zouqi(int ORole_id,int x,int y);
    public void connect(int port)throws IOException;
    public void takeqi(int ORole_id,int PRole_id);
    public void AskRefresh();
    public void ExtendData(String data);
}
