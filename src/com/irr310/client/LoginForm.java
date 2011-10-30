package com.irr310.client;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;



public class LoginForm extends JFrame {

	// Login
	private JTextField email;
	private JPasswordField password;
	private JButton loginButton;

	// Signup
	private JTextField signupEmail;
	private JPasswordField signupFirstPassword;
	private JPasswordField signupSecondPassword;
	private JButton signupButton;

	// Server
	private JTextField server;

	public LoginForm() {

		setTitle("IRR310");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		JComponent panel = buildPanel();
		getContentPane().add(panel);
		pack();

	}

	private void initComponents() {
		email = new JTextField();
		password = new JPasswordField();
		signupEmail = new JTextField();
		signupFirstPassword = new JPasswordField();
		signupSecondPassword = new JPasswordField();
		server = new JTextField();
		loginButton = new JButton("Login");
		signupButton = new JButton("Signup");
		
		loginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("click on login");
				GameClient.getInstance().loginTask(email.getText(), password.getText());
			}
		});
		
		signupButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("click on login");
                String password1 = new String(signupFirstPassword.getPassword());
                String password2 = new String(signupSecondPassword.getPassword());
                if(password1.equals(password2)) {
                    GameClient.getInstance().signupTask(signupEmail.getText(), password1);
                }
            }
        });
		
	}

	public JComponent buildPanel() {
		initComponents();

		JPanel panel = new JPanel();
		
		BoxLayout mainLayout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
		panel.setLayout(mainLayout);
		
		panel.add(new JLabel("Login"));
		
		GridLayout loginLayout = new GridLayout(2,2);
		JPanel loginPanel = new JPanel(loginLayout);
		loginPanel.add(new JLabel("Email"));
		loginPanel.add(email);
		
		loginPanel.add(new JLabel("Password"));
		loginPanel.add(password);
		panel.add(loginPanel);
		
		panel.add(loginButton);
		
		panel.add(new JLabel("Signup"));
		
		GridLayout signupLayout = new GridLayout(3,2);
		JPanel signupPanel = new JPanel(signupLayout);
		signupPanel.add(new JLabel("Email"));
		signupPanel.add(signupEmail);
		
		signupPanel.add(new JLabel("Password"));
		signupPanel.add(signupFirstPassword);
		signupPanel.add(new JLabel("Password again"));
		signupPanel.add(signupSecondPassword);
		panel.add(signupPanel);
		
		panel.add(signupButton);
		
		panel.add(new JLabel("Server"));
		
		
		GridLayout serverLayout = new GridLayout(1,2);
		JPanel serverPanel = new JPanel(serverLayout);
		serverPanel.add(new JLabel("Host"));
		serverPanel.add(server);
		
		panel.add(serverPanel);
		
		

		return panel;
	}

}
