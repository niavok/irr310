package com.irr310.common.tools;

import java.text.DecimalFormat;
import java.util.Stack;

public class Log {
    
    private static Stack<LogBegin> logBeginStack = new Stack<Log.LogBegin>(); 
    private static long startTime = System.currentTimeMillis();
    private static int indent = 0;
    
    
    public static void perfBegin(String log) {
        
        if(!logBeginStack.isEmpty() && !logBeginStack.firstElement().isComposed()) {
            logBeginStack.firstElement().setComposed(true);
            indent++;
            System.out.println();
        }
        
        long time = System.currentTimeMillis() - startTime;
        System.out.print("["+formatTime(time)+"] PERF"+ generateIndent()+" > "+log);
        logBeginStack.push(new LogBegin(log, time));
    }

   

    public static void perfEnd() {
        
        
        
        
        long time = System.currentTimeMillis() - startTime;
        LogBegin logBegin = logBeginStack.pop();
        
        if(logBegin.isComposed()) {
            indent--;
            System.out.println("["+formatTime(time)+"] PERF"+ generateIndent()+" < "+logBegin.getLog() + " <- "+formatTime(time-logBegin.getBeginTime()));
        } else {
            System.out.println(" -> "+formatTime(time-logBegin.getBeginTime()));    
        }
        
        
    }
    
    
    private static String formatTime(long time) {
        DecimalFormat format = new DecimalFormat("0000.0000");
        return format.format(((double) time) /1000.0);
    }
    
    private static String generateIndent() {
        String indentString = "";
        
        for(int i = 0; i < indent; i++ ) {
            indentString += "    ";
        }
        return indentString;
    }
    
    private static class LogBegin {
        
        private final String log;
        private final long beginTime;
        private boolean composed;
        
        public LogBegin(String log, long beginTime) {
            this.log = log;
            this.beginTime = beginTime;
            this.composed = false;
        }
        
        public void setComposed(boolean composed) {
            this.composed = composed;
        }

        public boolean isComposed() {
            return composed;
        }

        public long getBeginTime() {
            return beginTime;
        }
        
        public String getLog() {
            return log;
        }
    }
}
