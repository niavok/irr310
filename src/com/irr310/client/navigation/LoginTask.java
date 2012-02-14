package com.irr310.client.navigation;

import com.irr310.client.GameClient;
import com.irr310.client.network.request.LoginRequest;
import com.irr310.client.network.request.SignupRequest;
import com.irr310.common.network.protocol.LoginResponseMessage;
import com.irr310.common.network.protocol.ShipListRequestMessage;
import com.irr310.common.network.protocol.SignupResponseMessage;

public class LoginTask extends Task{

    private final String login;
    private final String password;

    public LoginTask(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public void start() {
        new Thread() {

            @Override
            public void run() {

                if (LoginManager.isLogged()) {
                    
                }

                LoginRequest loginRequest = new LoginRequest(login, password);
                loginRequest.sendAndWait(GameClient.getInstance().getNetWorkEngine());

                LoginResponseMessage m = loginRequest.getResponseMessage();
                if (!m.success) {
                    System.out.println("login failed: " + m.reason);
                    return;
                }
                System.out.println("login successful");

                LoginManager.localPlayer = GameClient.getInstance().getWorld().loadPlayer(m.player);

                GameClient.getInstance().getNetWorkEngine().send(new ShipListRequestMessage());
                complete();
            }

        }.start();
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