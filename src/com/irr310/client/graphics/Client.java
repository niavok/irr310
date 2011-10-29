package com.irr310.client.graphics;

import javax.swing.UIManager;

public class Client {

	private static LoginForm loginForm;

	public static void main(String[] args) {

		setLookAndFeel();

		loginForm = new LoginForm();
		loginForm.setLocationRelativeTo(loginForm.getParent());
		loginForm.setVisible(true);
	}

	private static void setLookAndFeel() {
		for (UIManager.LookAndFeelInfo laf : UIManager
				.getInstalledLookAndFeels()) {
			if ("Nimbus".equals(laf.getName())) {
				try {
					UIManager.setLookAndFeel(laf.getClassName());

				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		}
	}
}
