package com.irr310.i3d;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Handler {

    Queue<Message> messages = new LinkedBlockingQueue<Message>();
    
    public boolean hasMessages() {
        return !messages.isEmpty();
    }

    public Message getMessage() {
        return messages.poll();
    }
    
    public void send(Message message) {
        messages.add(message);
    }

    public Message obtainMessage(int what) {
        Message message =  new Message();
        message.handler = this;
        message.what = what;
        return message;
    }
}
