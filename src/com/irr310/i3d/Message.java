package com.irr310.i3d;

public class Message {

    public int what;
    public Handler handler;
    public void send() {
        handler.send(this);
    }

}
