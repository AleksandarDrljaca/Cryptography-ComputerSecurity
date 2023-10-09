package org.unibl.etf;
import java.awt.Dimension;

import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import org.unibl.etf.Exceptions.UnAutohrizedChangeException;
import org.unibl.etf.User.User;
import org.unibl.etf.Utils.DigitalEnvelope;
import org.unibl.etf.Utils.FileHandler;
import org.unibl.etf.Utils.UtilsConfig;
import javax.swing.JButton;


@SuppressWarnings("serial")
public class MainWindow extends JFrame{
	private JTable jt;
	private User currentUser;
	private String choosenFile="";
	
	public MainWindow(User user) {
		this.currentUser=user;
		final String directory="./FileSystem/"+currentUser.getUsername()+"/";
		this.setResizable(false);
		final JFrame frame=this;
		int num=0;
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		getContentPane().setLayout(null);
		this.setSize(600,600);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("lock.png"));
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        
        JButton downloadBtn = new JButton("Download");
        downloadBtn.setBounds(420, 421, 117, 25);
        getContentPane().add(downloadBtn);
        downloadBtn.setVisible(false);

        String[] col= {"Naziv","Datum"};
        

        getContentPane().setLayout(null);
        File folder = new File(directory);


        File[] fileList = folder.listFiles();


        ArrayList<File> fajlArray = new ArrayList<File>();

        for (File f : fileList) {
            if (f.isDirectory()) {
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
        DefaultTableModel model = new DefaultTableModel(o, col);
        jt = new JTable(model);
        jt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = jt.getSelectedRow();
                int y = jt.getSelectedColumn();
                File file = new File(directory + "/" + jt.getModel().getValueAt(x, y));
               System.out.println(file.getPath());
               choosenFile=file.getPath();
               downloadBtn.setVisible(true);
                
            }
        });

        JScrollPane scrollPane = new JScrollPane(jt);
        scrollPane.setBounds(12, 12, 574, 289);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        getContentPane().add(scrollPane);
        
        JButton uploadBtn = new JButton("Upload");
        uploadBtn.setBounds(59, 421, 117, 25);
        getContentPane().add(uploadBtn);
        //--------------------
        //event za Upload dugme
        uploadBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser fileChooser=new JFileChooser();
				 int status = fileChooser.showOpenDialog(null);

				    if (status == JFileChooser.APPROVE_OPTION) {
				      File selectedFile = fileChooser.getSelectedFile();
				      String fajl=DigitalEnvelope.extractFileName(selectedFile.getPath());
				      UtilsConfig.executeCommand("mkdir "+directory+fajl,false);
				      FileHandler.split(selectedFile, currentUser);
				      new MainWindow(currentUser);
				      frame.dispose();
				      
				    } else if (status == JFileChooser.CANCEL_OPTION) {
				      System.out.println("canceled");

				    }
			
				
			}
		});
        //event za Download dugme
        downloadBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				try {
					FileHandler.read(choosenFile, user);
				}  catch (UnAutohrizedChangeException|IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		//------------------------
	}

	
	/*public static void main(String[] args) {
		@SuppressWarnings("unused")
		MainWindow mw=new MainWindow(UtilsConfig.dataBase.getUser("Aco"));
	}*/
}
