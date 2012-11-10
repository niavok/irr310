package com.irr310.client.navigation;


public class LoginTask extends Task{

    private final String login;
    private final String password;

    public LoginTask(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public void start() {
//        new Thread() {
//
//            @Override
//            public void run() {
//
//                if (LoginManager.isLogged()) {
//                    
//                }
//
//                LoginRequest loginRequest = new LoginRequest(login, password);
//                loginRequest.sendAndWait(GameClient.getInstance().getNetWorkEngine());
//
//                LoginResponseMessage m = loginRequest.getResponseMessage();
//                if (!m.success) {
//                    System.out.println("login failed: " + m.reason);
//                    return;
//                }
//                System.out.println("login successful");
//
//                LoginManager.localPlayer = GameClient.getInstance().getWorld().loadPlayer(m.player);
//
//                
//                
//                FetchShipListRequest fetchShipListRequest = new FetchShipListRequest();
//                fetchShipListRequest.sendAndWait(GameClient.getInstance().getNetWorkEngine());
//                
//                //TODO, find the right camera
//                GameClient.getInstance().getNetWorkEngine().send(new CameraViewObjectListRequestMessage());
//                
//                complete();
//            }
//
//        }.start();
    }

    
    public void loginTask(final String login, final String password) {
        new Thread() {

            @Override
            public void run() {
                

                /*
                 * for (ShipView ship :
                 * fetchShipListRequest.getResponseMessage().shipsList) {
                 * sendToAll(new AddShipEvent(ship)); }
                 */

            }
        }.start();

    }
}
