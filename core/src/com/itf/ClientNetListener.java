package com.itf;

public interface ClientNetListener {
public void NetZouqi(int ORole_id,int x,int y);
public void NetTakeqi(int ORole_id,int PRole_id);
public void NetRefresh();
public void NetExtendData(String data);
}
