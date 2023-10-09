package org.unibl.etf;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JTextField;
import org.unibl.etf.Exceptions.UserExistsException;
import org.unibl.etf.Utils.DigitalCertificateUtils;
import org.unibl.etf.Utils.UtilsConfig;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.Color;

@SuppressWarnings("serial")
public class Registration extends JFrame{
	private JTextField usernameField;
	private JTextField organizationField;
	private JTextField organizationUnitField;
	private JTextField cityField;
	private JTextField stateField;
	private JTextField countryField;
	private JPasswordField passwordField;
	private JPasswordField reEnterPassField;
	
	
	private JButton registerBtn;
	private final JFrame frame=this;
	
	public Registration() {
		this.setResizable(false);
		getContentPane().setFont(new Font("Dialog", Font.PLAIN, 12));
		this.setTitle("Registration");
		getContentPane().setLayout(null);
		
		usernameField = new JTextField();
		usernameField.getMargin().set(0, 0, 0, 20);
		usernameField.setBounds(168, 25, 170, 25);
		getContentPane().add(usernameField);
		usernameField.setColumns(10);
		
		
		passwordField = new JPasswordField();
		passwordField.setBounds(168, 78, 170, 25);
		getContentPane().add(passwordField);
		
		reEnterPassField = new JPasswordField();
		reEnterPassField.setBounds(168, 127, 170, 25);
		getContentPane().add(reEnterPassField);
		
		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		usernameLabel.setBounds(51, 28, 75, 21);
		
		getContentPane().add(usernameLabel);
		
		JLabel passLabel = new JLabel("Password:");
		passLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		passLabel.setBounds(51, 77, 75, 21);
		getContentPane().add(passLabel);
		
		JLabel reEnterPassLabel = new JLabel("Re-Enter password:");
		reEnterPassLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		reEnterPassLabel.setBounds(27, 126, 114, 21);
		getContentPane().add(reEnterPassLabel);
		
		JLabel organizationLabel = new JLabel("Organization:");
		organizationLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		organizationLabel.setBounds(51, 176, 75, 21);
		getContentPane().add(organizationLabel);
		
		organizationField = new JTextField();
		organizationField.setColumns(10);
		organizationField.setBounds(168, 175, 170, 25);
		organizationField.setText("ETF_BL");
		getContentPane().add(organizationField);
		
		JLabel organizationUnitLabel = new JLabel("OrganizationUnit:");
		organizationUnitLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		organizationUnitLabel.setBounds(40, 227, 101, 21);
		getContentPane().add(organizationUnitLabel);
		
		organizationUnitField = new JTextField();
		organizationUnitField.setColumns(10);
		organizationUnitField.setBounds(168, 224, 170, 25);
		organizationUnitField.setText("ETF");
		getContentPane().add(organizationUnitField);
		
		JLabel cityLabel = new JLabel("City:");
		cityLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		cityLabel.setBounds(51, 276, 75, 21);
		getContentPane().add(cityLabel);
		
		cityField = new JTextField();
		cityField.setColumns(10);
		cityField.setBounds(168, 275, 170, 25);
		cityField.setText("BL");
		getContentPane().add(cityField);
		
		JLabel stateLabel = new JLabel("State:");
		stateLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		stateLabel.setBounds(51, 321, 75, 21);
		getContentPane().add(stateLabel);
		
		stateField = new JTextField();
		stateField.setColumns(10);
		stateField.setBounds(168, 318, 170, 25);
		stateField.setText("RS");
		getContentPane().add(stateField);
		
		JLabel countryLabel = new JLabel("Country:");
		countryLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		countryLabel.setBounds(51, 370, 75, 21);
		getContentPane().add(countryLabel);
		
		countryField = new JTextField();
		countryField.setColumns(10);
		countryField.setBounds(168, 369, 170, 25);
		countryField.setText("BA");
		getContentPane().add(countryField);
		
		registerBtn = new JButton("Register");
		registerBtn.setBounds(168, 423, 133, 27);
		setRegisterBtnAction();
		getContentPane().add(registerBtn);
		
		JLabel astrixUser = new JLabel("*");
		astrixUser.setFont(new Font("Tahoma", Font.PLAIN, 13));
		astrixUser.setForeground(Color.RED);
		astrixUser.setBounds(27, 28, 20, 19);
		getContentPane().add(astrixUser);
		
		JLabel astrixPass = new JLabel("*");
		astrixPass.setFont(new Font("Tahoma", Font.PLAIN, 13));
		astrixPass.setForeground(Color.RED);
		astrixPass.setBounds(27, 81, 20, 19);
		getContentPane().add(astrixPass);
		
		JLabel astrixRePass = new JLabel("*");
		astrixRePass.setFont(new Font("Tahoma", Font.PLAIN, 13));
		astrixRePass.setForeground(Color.RED);
		astrixRePass.setBounds(10, 130, 20, 19);
		getContentPane().add(astrixRePass);
		
		
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("lock.png"));
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
		this.setSize(400,500);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public void setRegisterBtnAction() {
		registerBtn.addMouseListener(new MouseAdapter() {
        	@Override
            public void mousePressed(MouseEvent e) {
        		
        		new Thread(new Runnable() {
					public void run() {
						
						try {
							registerNewUser();
						} catch (UserExistsException ec) {
							
							JOptionPane.showMessageDialog(frame,ec.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
						}
					}
				}).start();
						
        	}
        });
	}
	public void registerNewUser() throws UserExistsException {
		
		//inicijalizacija
		String CN=usernameField.getText().toString();
		String PASS=String.valueOf(passwordField.getPassword());
		String O=organizationField.getText().toString();
		String OU=organizationUnitField.getText().toString();
		String L=cityField.getText().toString();
		String ST=stateField.getText().toString();
		String C=countryField.getText().toString();
		String rePass=String.valueOf(reEnterPassField.getPassword());
		
		if("".equals(CN) || "".equals(PASS) || "".equals(rePass)  ) {
			JOptionPane.showMessageDialog(frame,"Mandatory field empty!","Error",JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(!PASS.equals(rePass)) {
			JOptionPane.showMessageDialog(frame,"asswords do not match!","Error",JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(userExists(CN)) {
			JOptionPane.showMessageDialog(frame,"Username already in use!","Error",JOptionPane.ERROR_MESSAGE);
			return;
				
		}
		
		DigitalCertificateUtils.createCertificateRequest(CN, O, OU, L, ST, C);
		DigitalCertificateUtils.signCertificateRequest(CN);
		UtilsConfig.dataBase.addToDB(CN, PASS);
		
		@SuppressWarnings("unused")
		FirstStageLogin secondStage=new FirstStageLogin();
		frame.dispose();
		
		
		
	            
		
	}
	private boolean userExists(String name) {
		File file=new File(UtilsConfig.CERTS_PATH+"/"+name+".crt");
		if(file.exists() && !file.isDirectory())
			return true;
		return false;
	}
	



}
