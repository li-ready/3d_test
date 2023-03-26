package com.itf;

public interface ServerNetListener {
public void zouqi(int ORole_id, int x, int y, int client_id);
public void takeqi(int ORole_id, int PRole_id, int client_id);
public void RefreshAsk(int client_id);
public void Client_Disconnected(float time, int client_id);
public void Client_AskDisconnected(int client_id);
public void ExtendData(String data, int client_id);
public int connet_setclient_id();
}
