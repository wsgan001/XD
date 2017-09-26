package Health_Sys;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.swing.event.*;


public class personalUI extends JFrame {
	JPanel contentPane;
	JPanel modulePanel = new JPanel();
	SpringLayout springLayout1 = new SpringLayout();
   	SpringLayout moduleLayout = new SpringLayout();
   
	JFileChooser fileChooser = new JFileChooser();   //���ɳ���
	JLabel fileLabel = new JLabel("�ӤH�ɮ�");
	JTextField fileTextField = new JTextField("");
	JButton fileBtn = new JButton("����ɮ�");
	String fileNameStr = "";
   	
   	JScrollPane jScrollPane = new JScrollPane();	//�Ҳտﶵ
   	JCheckBox check1 = new JCheckBox("�}���f");    
   	JCheckBox check2 = new JCheckBox("�x�f");
   	JCheckBox check3 = new JCheckBox("�ߦ�ޯe�f");
   	JCheckBox check4 = new JCheckBox("");
   	JCheckBox check5 = new JCheckBox("");
   	JCheckBox check6 = new JCheckBox("");
   	JCheckBox check7 = new JCheckBox("");
	JButton selectBtn = new JButton("��ܥ���");
	JButton cancelBtn = new JButton("�������");
	
	JButton submitBtn = new JButton("�i��w��");
	JButton returnBtn = new JButton("�^�D�e��");
	
	JFrame menuStart;

	
	public personalUI(JFrame menuStart) {		
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			this.menuStart = menuStart;
			jbInit();
		} catch(Exception e)  { e.printStackTrace(); }
	}
   
   
	private void jbInit() throws Exception  {		//Component initialization 
		this.setSize(500,450);
		this.setTitle("�ӤH�e�f�w��");
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
		
		contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(springLayout1);
		
		final personalUI adaptee = this;

		fileBtn.addActionListener(new java.awt.event.ActionListener() {		//����
			public void actionPerformed(ActionEvent e) {
		   	if(fileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(adaptee)){
		     		fileNameStr = fileChooser.getSelectedFile().getPath();
        			fileTextField.setText(fileNameStr);
				}
			}
		}); 
		
		returnBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
			   	dispose();
			    menuStart.setVisible(true);
			}
		}); 
        
        fileTextField.setPreferredSize(new Dimension(300, 25));	//fileTextField�j�p
      	contentPane.add(fileLabel);
		contentPane.add(fileTextField);
		contentPane.add(fileBtn);
		contentPane.add(jScrollPane);
		//contentPane.add(modulePanel);
		contentPane.add(submitBtn);
		contentPane.add(returnBtn);
		
		moduleLayout();
				
		//springLayout1�ƪ�����
		springLayout1.putConstraint(SpringLayout.WEST, fileLabel, 20, SpringLayout.WEST, contentPane);
		springLayout1.putConstraint(SpringLayout.NORTH, fileLabel, 25, SpringLayout.NORTH, contentPane);
		springLayout1.putConstraint(SpringLayout.WEST, fileTextField, 10, SpringLayout.EAST, fileLabel);
		springLayout1.putConstraint(SpringLayout.NORTH, fileTextField, 20, SpringLayout.NORTH, contentPane); 
		springLayout1.putConstraint(SpringLayout.WEST, fileBtn, 10, SpringLayout.EAST, fileTextField);
		springLayout1.putConstraint(SpringLayout.NORTH, fileBtn, 20, SpringLayout.NORTH, contentPane);
		springLayout1.putConstraint(SpringLayout.WEST, jScrollPane,20, SpringLayout.WEST, contentPane);
		springLayout1.putConstraint(SpringLayout.NORTH, jScrollPane, 70, SpringLayout.NORTH, contentPane);
		springLayout1.putConstraint(SpringLayout.EAST, submitBtn, -120, SpringLayout.EAST, contentPane);
		springLayout1.putConstraint(SpringLayout.NORTH, submitBtn, 20, SpringLayout.SOUTH, jScrollPane);
		springLayout1.putConstraint(SpringLayout.EAST, returnBtn, -20, SpringLayout.EAST, contentPane);
		springLayout1.putConstraint(SpringLayout.NORTH, returnBtn, 20, SpringLayout.SOUTH, jScrollPane);		
	}
    
   	private void moduleLayout() throws Exception  {
		   	
		selectBtn.addActionListener(new java.awt.event.ActionListener() {	//����
			public void actionPerformed(ActionEvent e) {
			   	check1.setSelected(true);
			   	check2.setSelected(true);
			   	check3.setSelected(true);
			   	check4.setSelected(true);
			   	check5.setSelected(true);
			   	check6.setSelected(true);
			   	check7.setSelected(true);
			}
		}); 

		cancelBtn.addActionListener(new java.awt.event.ActionListener() {	//�������
			public void actionPerformed(ActionEvent e) {
			   	check1.setSelected(false);
			   	check2.setSelected(false);
			   	check3.setSelected(false);
			   	check4.setSelected(false);
			   	check5.setSelected(false);
			   	check6.setSelected(false);
			   	check7.setSelected(false);
			}
		}); 		
		
		jScrollPane.setPreferredSize(new Dimension(450, 230));		//modulePanel�j�p
		jScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.ORANGE, 1), "�w���Ҳտ��"));
		jScrollPane.getViewport().add(modulePanel);
		
		modulePanel.setPreferredSize(new Dimension(450, 330));
		modulePanel.setLayout(moduleLayout);
		//modulePanel.add(sbV);
		modulePanel.add(check1);
		modulePanel.add(check2);
		modulePanel.add(check3);
		modulePanel.add(check4);
		modulePanel.add(check5);
		modulePanel.add(check6);
		modulePanel.add(check7);
		modulePanel.add(selectBtn);
		modulePanel.add(cancelBtn);
		
		moduleLayout.putConstraint(SpringLayout.EAST, selectBtn, -120, SpringLayout.EAST, modulePanel);
		moduleLayout.putConstraint(SpringLayout.NORTH, selectBtn, 10, SpringLayout.NORTH, modulePanel);
		moduleLayout.putConstraint(SpringLayout.EAST, cancelBtn, -20, SpringLayout.EAST, modulePanel);
		moduleLayout.putConstraint(SpringLayout.NORTH, cancelBtn, 10, SpringLayout.NORTH, modulePanel);
		moduleLayout.putConstraint(SpringLayout.WEST, check1, 20, SpringLayout.WEST, modulePanel);
		moduleLayout.putConstraint(SpringLayout.NORTH, check1, 30, SpringLayout.NORTH, modulePanel);
		moduleLayout.putConstraint(SpringLayout.WEST, check2, 20, SpringLayout.WEST, modulePanel);
		moduleLayout.putConstraint(SpringLayout.NORTH, check2, 10, SpringLayout.SOUTH, check1);
		moduleLayout.putConstraint(SpringLayout.WEST, check3, 20, SpringLayout.WEST, modulePanel);
		moduleLayout.putConstraint(SpringLayout.NORTH, check3, 10, SpringLayout.SOUTH, check2);
		moduleLayout.putConstraint(SpringLayout.WEST, check4, 20, SpringLayout.WEST, modulePanel);
		moduleLayout.putConstraint(SpringLayout.NORTH, check4, 10, SpringLayout.SOUTH, check3);
		moduleLayout.putConstraint(SpringLayout.WEST, check5, 20, SpringLayout.WEST, modulePanel);
		moduleLayout.putConstraint(SpringLayout.NORTH, check5, 10, SpringLayout.SOUTH, check4);
		moduleLayout.putConstraint(SpringLayout.WEST, check6, 20, SpringLayout.WEST, modulePanel);
		moduleLayout.putConstraint(SpringLayout.NORTH, check6, 10, SpringLayout.SOUTH, check5);
		moduleLayout.putConstraint(SpringLayout.WEST, check7, 20, SpringLayout.WEST, modulePanel);
		moduleLayout.putConstraint(SpringLayout.NORTH, check7, 10, SpringLayout.SOUTH, check6);	  
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