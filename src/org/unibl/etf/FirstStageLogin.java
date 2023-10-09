package org.unibl.etf;



import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.unibl.etf.Utils.DigitalCertificateUtils;
import org.unibl.etf.Utils.UtilsConfig;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.Date;
import java.util.ArrayList;


@SuppressWarnings("serial")
public class FirstStageLogin extends JFrame{
	 JTable jt;
	public FirstStageLogin() {

		final JFrame f=this;
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setSize(400,400);
		this.setTitle("Crypto");
		this.setResizable(false);
		getContentPane().setLayout(null);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("lock.png"));
		
		JLabel messageLabel = new JLabel("Please choose you certificate");
		messageLabel.setBounds(77, 57, 241, 14);
		getContentPane().add(messageLabel);
		
		JLabel titleLabel = new JLabel("Crypto");
		titleLabel.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 20));
		titleLabel.setBounds(155, 24, 155, 33);
		getContentPane().add(titleLabel);
		
		JLabel questionLabel = new JLabel("No account?");
		questionLabel.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 11));
		questionLabel.setBounds(290, 306, 75, 14);
		getContentPane().add(questionLabel);
		
		JLabel registerLabel = new JLabel("Register");
		registerLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				@SuppressWarnings("unused")
				Registration register=new Registration();
				f.dispose();
			}
		});
		registerLabel.setForeground(Color.BLUE);
		registerLabel.setFont(new Font("Liberation Sans", Font.PLAIN, 12));
		registerLabel.setBounds(303, 332, 75, 14);
		getContentPane().add(registerLabel);
		
		
		//TABELA SA SERTIFIKTIMA
		
        DefaultTableModel model = createTableModel();
        jt = new JTable(model);
        jt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = jt.getSelectedRow();
                int y = jt.getSelectedColumn();
                File file = new File(UtilsConfig.CERTS_PATH + "\\" + jt.getModel().getValueAt(x, y));
                String fileName=file.getName().substring(6,file.getName().length()-4);
                if(UtilsConfig.dataBase.getUser(fileName).getNumOfWrongEntries()==3) {
                	JOptionPane.showMessageDialog(f,"Certificate Suspended!","Error",JOptionPane.ERROR_MESSAGE);
                	new Registration();
                	f.dispose();
                	
                }else
                //***AKO JE SERTIFIKAT VALIDAN PRVI KORAK AUTENTIKACIJE JE USPJESAN***
                if(DigitalCertificateUtils.checkCertValidity(fileName)){
                	new SecondStageLogin(fileName);
                	f.dispose();
                }
                else JOptionPane.showMessageDialog(f,"Invalid Certificate!","Error",JOptionPane.ERROR_MESSAGE);
                
            }
        });
        JScrollPane scrollPane = new JScrollPane(jt);
        scrollPane.setBounds(12, 73, 378, 221);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        getContentPane().add(scrollPane);
	
	}

  
    private DefaultTableModel createTableModel() {
    	
        String[] col= {"Naziv","Datum"};
        getContentPane().setLayout(null);
        File folder = new File(UtilsConfig.CERTS_PATH);
        File[] fileList = folder.listFiles();
        ArrayList<File> fajlArray = new ArrayList<File>();
        int num=0;
        for (File f : fileList) {
            if (f.getName().contains("crt")) {
                num++;
                fajlArray.add(f);
            }

        }
        Object[][] o = new Object[num][2];
        for (int i = 0; i < num; i++) {
            o[i][0] = fajlArray.get(i).getName();
            Date date=new Date(fajlArray.get(i).lastModified());
            @SuppressWarnings("deprecation")
			String dateString=date.toLocaleString();
            o[i][1]=dateString;
        }
        return new DefaultTableModel(o,col);
    }
	
	
}
