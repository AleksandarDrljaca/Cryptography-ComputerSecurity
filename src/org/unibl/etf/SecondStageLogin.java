package org.unibl.etf;

import java.awt.Dimension;

import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.security.NoSuchAlgorithmException;

import javax.swing.JFrame;
import javax.swing.JTextField;

import org.unibl.etf.User.User;
import org.unibl.etf.Utils.DigitalCertificateUtils;
import org.unibl.etf.Utils.HashUtils;

import org.unibl.etf.Utils.UtilsConfig;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import java.awt.Font;

@SuppressWarnings("serial")
public class SecondStageLogin extends JFrame{
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginBtn = new JButton("Log in");
	private String firstStageLoginCert;
	public SecondStageLogin(String userCert) {
		firstStageLoginCert=userCert;
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("lock.png"));
		this.setTitle("Login");
		this.setSize(400,400);
		this.setResizable(false);
		getContentPane().setLayout(null);
		
		usernameField = new JTextField();
		usernameField.setBounds(143, 90, 160, 25);
		getContentPane().add(usernameField);
		
		
		passwordField = new JPasswordField();
		passwordField.setBounds(143, 152, 160, 25);
		getContentPane().add(passwordField);
		
		
		loginBtn.setFont(new Font("Tahoma", Font.PLAIN, 12));
		loginBtn.setBounds(143, 236, 90, 25);
		getContentPane().add(loginBtn);
		
		
		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		usernameLabel.setBounds(40, 95, 70, 14);
		getContentPane().add(usernameLabel);
		
		JLabel passLabel = new JLabel("Password:");
		passLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		passLabel.setBounds(40, 155, 70, 14);
		getContentPane().add(passLabel);
		
		this.setVisible(true);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        
        addLoginBtnAction();
        
		
	}
	public boolean loginCredentialsValid() {
		String name=usernameField.getText();
		if(UtilsConfig.dataBase.userExists(name)) {
			User currUser=UtilsConfig.dataBase.getUser(name);
			String passHash=null;
			try {
				passHash=HashUtils.getHash(String.valueOf(passwordField.getPassword()));
			} catch (NoSuchAlgorithmException e) {
				
				e.printStackTrace();
			}
			if(currUser.getPassword().equals(passHash) && currUser.getUsername().equals(name)) return true;
			
		}
		return false;
	
	}
	private void addLoginBtnAction() {
		
		final JFrame frame=this;
		loginBtn.addMouseListener(new MouseAdapter() {
        	@Override
            public void mousePressed(MouseEvent e) {
        		if(UtilsConfig.dataBase.userExists(usernameField.getText())) {
        			User currUser=UtilsConfig.dataBase.getUser(usernameField.getText());
            		System.out.println(currUser);
            		int num=currUser.getNumOfWrongEntries();
            		System.out.println(num);
            		try {
            			if(num<2) {
            				if(!loginCredentialsValid()) {
                    			JOptionPane.showMessageDialog(frame,"Wrong credentials!","Error",JOptionPane.ERROR_MESSAGE);
                    			num++;
                    			currUser.setNumOfWrongEntries(num);
                    			//UtilsConfig.dataBase.serializeList();
            					
                    		}else if(!DigitalCertificateUtils.certBelongsTo(firstStageLoginCert,currUser.getUsername())) {
                    			JOptionPane.showMessageDialog(frame,"Not your certificate!","Error",JOptionPane.ERROR_MESSAGE);
                    			System.exit(1);
                    		}
            				else {
            					new MainWindow(currUser);
            					frame.dispose();
            				}
            				
            			}else if(num==2) {
            					JOptionPane.showMessageDialog(frame,"Your certificate is suspended!\n You can try one more time to retrieve your cert \n otherwise you must register again!","Error",JOptionPane.ERROR_MESSAGE);
            					num++;
            					currUser.setNumOfWrongEntries(num);
            					UtilsConfig.dataBase.serializeList();
            					System.out.println("pre revoke0");
            					DigitalCertificateUtils.revokeCertificate(usernameField.getText());
            					System.out.println("revoke");
            					
            			}
            			else if(num==3) {
            				if(loginCredentialsValid()) {
            					DigitalCertificateUtils.reactivateCert(currUser.getUsername());
            					currUser.setNumOfWrongEntries(0);
            					UtilsConfig.dataBase.serializeList();
            					new FirstStageLogin();
            				}
            				else {
            					new Registration();
                				frame.dispose();
            				}
            			}
            			
            		}catch(Exception e1) {
            			e1.printStackTrace();
            		}
        		}else {
        			JOptionPane.showMessageDialog(frame,"No such user!","Error",JOptionPane.ERROR_MESSAGE);
        		}
        	


            }
        });
	}
	
	
	
}
