package com.itf;

import java.io.IOException;

public interface ServerNetSender {
    public void zouqi(int ORole_id, int x, int y);
    public void takeqi(int ORole_id, int PRole_id);
    public void Refresh(String refresh_array);
    public void ExtendData(String data);
    public void setclient_id(int id);
    public void client_Mustdisconnect(String data);
    public void connect() throws IOException;
}
