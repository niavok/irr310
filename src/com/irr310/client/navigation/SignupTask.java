package com.irr310.client.navigation;


public class SignupTask extends Task{

    private final String login;
    private final String password;

    public SignupTask(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public void start() {
//        new Thread() {
//
//            @Override
//            public void run() {
//
//                SignupRequest signupRequest = new SignupRequest(login, password);
//                signupRequest.sendAndWait(GameClient.getInstance().getNetWorkEngine());
//
//                SignupResponseMessage m = signupRequest.getResponseMessage();
//                if (m.success) {
//                    System.out.println("signup successful");
//                } else {
//                    System.out.println("signup failed: " + m.reason);
//                }
//                complete();
//            }
//
//        }.start();
    }

}
