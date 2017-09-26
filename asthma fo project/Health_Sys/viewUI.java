package Health_Sys;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;


public class viewUI extends JFrame{
	JPanel contentPane;
	SpringLayout springLayout1 = new SpringLayout();
   
	JPanel centerPanel = new JPanel();    //centerPanel  filePanel  btnPanel
	JPanel filePanel = new JPanel();
	JPanel btnPanel = new JPanel();
   
	JFileChooser fileChooser = new JFileChooser();   //filePanel���e
	JLabel fileLabel = new JLabel("�s���ɮפ��e");
	JTextField fileTextField = new JTextField("");
	JButton fileBtn = new JButton("����ɮ�");
	SpringLayout fileSpringLayout = new SpringLayout();
	
	TextArea txa=new TextArea();     //centerPanel���e
   
	JButton viewBtn = new JButton("��ƹw��");       //btnPanel���e
	JButton returnBtn = new JButton("�^�D�e��");
	SpringLayout btnSpringLayout = new SpringLayout();
	
	JFrame menuStart;
	JTable dataTable;
	JScrollPane jScrollPane = new JScrollPane();				
	int rowNum, columnNum;

	
	public viewUI(JFrame menuStart){		
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			this.menuStart = menuStart;
			jbInit();
		} catch(Exception e)  { e.printStackTrace(); }
	}
   
   
	private void jbInit() throws Exception{		//Component initialization 
		this.setSize(450, 350);
		this.setTitle("�s���ɮ�");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();   //frame location
		Dimension frameSize = this.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
      
		this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		this.setVisible(true);
		viewBtn.setEnabled(false);
		
		
		contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(springLayout1);
		
		setFilePanel();
		setBtnPanel();
      
		centerPanel.setPreferredSize(new Dimension(400, 200));	//centerPanel�t�m�Τj�p
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(txa, BorderLayout.CENTER);
      
		contentPane.add(filePanel);		//contentPane�[�JfilePanel  centerPanel  btnPanel
		contentPane.add(centerPanel);
		contentPane.add(btnPanel);
		
		//springLayout1�ƪ�filePanel  centerPanel  btnPanel
		springLayout1.putConstraint(SpringLayout.WEST, filePanel, 10, SpringLayout.WEST, contentPane);
		springLayout1.putConstraint(SpringLayout.NORTH, filePanel, 10, SpringLayout.NORTH, contentPane);
		springLayout1.putConstraint(SpringLayout.WEST, centerPanel, 20, SpringLayout.WEST, contentPane);
		springLayout1.putConstraint(SpringLayout.NORTH, centerPanel, 0, SpringLayout.SOUTH, filePanel); 
		springLayout1.putConstraint(SpringLayout.WEST, btnPanel, 10, SpringLayout.WEST, contentPane);
		springLayout1.putConstraint(SpringLayout.NORTH, btnPanel, 10, SpringLayout.SOUTH, centerPanel);
	}
    
   
	protected void setFilePanel(){		//FilePanel�t�m
		final viewUI adaptee = this;
   	  
		fileBtn.addActionListener(new java.awt.event.ActionListener() {		//fileBtn function
			public void actionPerformed(ActionEvent e) {
				if(fileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(adaptee)){	         
					txa.setText("");
					jScrollPane.getViewport().removeAll();
					dataTable = null;
					centerPanel.removeAll();
					centerPanel.add(txa, BorderLayout.CENTER);
					centerPanel.updateUI();	
					fileTextField.setText(fileChooser.getSelectedFile().getPath());
					openFile(fileChooser.getSelectedFile().getPath());	         
					viewBtn.setEnabled(true);				
				}            
			}
		});
   	
		fileTextField.setPreferredSize(new Dimension(200, 25));	
		filePanel.setPreferredSize(new Dimension(400, 40));		
		//filePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.green, 1), ""));
		filePanel.setLayout(fileSpringLayout);
		filePanel.add(fileLabel);
		filePanel.add(fileTextField);
		filePanel.add(fileBtn);
   	  
		fileSpringLayout.putConstraint(SpringLayout.WEST, fileLabel, 10, SpringLayout.WEST, filePanel);
		fileSpringLayout.putConstraint(SpringLayout.NORTH, fileLabel, 0, SpringLayout.NORTH, filePanel);
		fileSpringLayout.putConstraint(SpringLayout.WEST, fileTextField, 10, SpringLayout.EAST, fileLabel);
		fileSpringLayout.putConstraint(SpringLayout.NORTH, fileTextField, 0, SpringLayout.NORTH, fileLabel);         	  
		fileSpringLayout.putConstraint(SpringLayout.WEST, fileBtn, 10, SpringLayout.EAST, fileTextField);
		fileSpringLayout.putConstraint(SpringLayout.NORTH, fileBtn, 0, SpringLayout.NORTH, fileTextField);         	  
	}
   

	protected void setBtnPanel(){		//BtnPanel�t�m
        final viewUI adaptee = this;
		
		viewBtn.addActionListener(new java.awt.event.ActionListener() {		//viewBtn function
			public void actionPerformed(ActionEvent e) {
				dataTable = browseFile();
				jScrollPane.getViewport().add(dataTable);
				centerPanel.remove(txa);
				centerPanel.add(jScrollPane, BorderLayout.CENTER);
				centerPanel.updateUI();
			}
		});
		
		returnBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
			   	dispose();
			    menuStart.setVisible(true);
			}
		}); 	  
		  	
		btnPanel.setPreferredSize(new Dimension(400, 40));		//btnPanel�t�m�Τj�p
		//btnPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.green, 1), ""));
		btnPanel.setLayout(btnSpringLayout);
		btnPanel.add(viewBtn);
		btnPanel.add(returnBtn);
		
		btnSpringLayout.putConstraint(SpringLayout.WEST, viewBtn, 200, SpringLayout.WEST, btnPanel);
		btnSpringLayout.putConstraint(SpringLayout.NORTH, viewBtn, 10, SpringLayout.NORTH, btnPanel);
		btnSpringLayout.putConstraint(SpringLayout.WEST, returnBtn, 25, SpringLayout.EAST, viewBtn);
		btnSpringLayout.putConstraint(SpringLayout.NORTH, returnBtn, 10, SpringLayout.NORTH, btnPanel);	
	}


	protected void openFile(String fname){		//Ū���ɮ�
		try{ 
			File file = new File(fname);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			int lineCounter = 0;
			String str;
			String[] attr;
			int max = 0;
			
			while(br.ready()){
				str = br.readLine();
				attr = str.split(",");
				lineCounter++;
				
				if(attr.length > max){
					max = attr.length;
				}
			}
			txa.setText("");
			txa.append("�έp���\r\n");
			txa.append("=========================\r\n");
			txa.append("��Ƶ���(�H��)�G " + lineCounter + "��\r\n");
			txa.append("�̦h�����˦���(�ǦC����)�G " + (max-1) + "��\r\n");
			
			rowNum = lineCounter;
			columnNum = max;
			
			br.close();
			fr.close();
			file = null;
		} catch(IOException ioe){};
	}
	
	protected JTable browseFile(){
		JTable jTable;
		String[] title = new String[columnNum];
		String[][] data = new String[rowNum][columnNum];
		
		title[0] = "�f����";
		for(int count=1 ; count<columnNum ; count++){
			title[count] = "��" + count + "��";
		}
		
		try{
			File file = new File(fileTextField.getText());
			FileReader fr = new FileReader(file);	
			BufferedReader br = new BufferedReader(fr);
			StringTokenizer st;
			int count;
			int lineCounter = 0;
						
			while(br.ready()){
				st = new StringTokenizer(br.readLine(), ",");
				
				count = 0;
				while(st.hasMoreTokens()){
					data[lineCounter][count++] = st.nextToken();
				}
				lineCounter++;
			}
			br.close();
			fr.close();
			file = null;
		}
		catch(IOException e){
			e.getMessage();
			e.printStackTrace();
		}
		
		jTable = new JTable(data, title);
		jTable.setAutoResizeMode(0);
		
		return jTable;
	}
	
	
	protected void processWindowEvent(WindowEvent e) {		//��������
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			int reply = -1;
			
			reply = JOptionPane.showConfirmDialog(null,"�T�w�n���}���{����?","�T�{����", JOptionPane.YES_NO_OPTION);
			if(reply == 0){
				System.exit(0);
			}
			else{
				this.setVisible(true);
			}
		}
	}
}