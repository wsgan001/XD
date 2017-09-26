package Health_Sys;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.swing.event.*;


public class All extends JFrame{
	JPanel contentPane;
	JPanel basicPanel = new JPanel();
	JPanel modulePanel = new JPanel();
	JTabbedPane JTab=new JTabbedPane();
	SpringLayout basicLayout = new SpringLayout();
	SpringLayout moduleLayout = new SpringLayout();
	JButton browseBtn = new JButton("檔案瀏覽");
	JButton searchBtn = new JButton("病歷號搜尋");
	JButton exitBtn1 = new JButton("離開");
	JButton moduleBtn = new JButton("建構預測模組");
	JButton personalBtn = new JButton("個人疾病預測");
	JButton exitBtn2 = new JButton("離開");
	
	
	public All() {
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			jbInit();
		} 
		catch(Exception e)  { 
			e.printStackTrace(); 
		}
	}
		
	private void jbInit() throws Exception  { 
		this.setSize(450,300);
		this.setTitle("所有功能一覽表");
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
		contentPane.add(JTab, BorderLayout.CENTER);
		basicLayout();
		moduleLayout();
		JTab.addTab("基本功能", basicPanel);
		JTab.addTab("疾病模組", modulePanel);	
	}
		
	private void basicLayout() throws Exception  {	
	
		exitBtn1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int reply = -1;
				reply = JOptionPane.showConfirmDialog(null,"確定要離開此程式嗎?","確認視窗", JOptionPane.YES_NO_OPTION);
				if(reply == 0){
					System.exit(0);
				}
			}	
		} );
	
		basicPanel.setLayout(basicLayout); 
		basicPanel.add(browseBtn);
		basicPanel.add(searchBtn);
		basicPanel.add(exitBtn1);
		
		basicLayout.putConstraint(SpringLayout.WEST, browseBtn, 20, SpringLayout.WEST, basicPanel);
		basicLayout.putConstraint(SpringLayout.NORTH, browseBtn, 20, SpringLayout.NORTH, basicPanel);
		basicLayout.putConstraint(SpringLayout.WEST, searchBtn, 20, SpringLayout.EAST, browseBtn);
		basicLayout.putConstraint(SpringLayout.NORTH, searchBtn, 20, SpringLayout.NORTH, basicPanel);
		basicLayout.putConstraint(SpringLayout.EAST, exitBtn1, -20, SpringLayout.EAST, basicPanel);
		basicLayout.putConstraint(SpringLayout.SOUTH, exitBtn1, -20, SpringLayout.SOUTH, basicPanel);
	}	

	private void moduleLayout() throws Exception  {
		
		exitBtn2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int reply = -1;
				reply = JOptionPane.showConfirmDialog(null,"確定要離開此程式嗎?","確認視窗", JOptionPane.YES_NO_OPTION);
				if(reply == 0){
					System.exit(0);
				}
			}	
		} );
		
		modulePanel.setLayout(moduleLayout); 
		modulePanel.add(moduleBtn);
		modulePanel.add(personalBtn);
		modulePanel.add(exitBtn2);
		
		moduleLayout.putConstraint(SpringLayout.WEST, moduleBtn, 20, SpringLayout.WEST, modulePanel);
		moduleLayout.putConstraint(SpringLayout.NORTH, moduleBtn, 20, SpringLayout.NORTH, modulePanel);
		moduleLayout.putConstraint(SpringLayout.WEST, personalBtn, 20, SpringLayout.EAST, moduleBtn);
		moduleLayout.putConstraint(SpringLayout.NORTH, personalBtn, 20, SpringLayout.NORTH, modulePanel);
		moduleLayout.putConstraint(SpringLayout.EAST, exitBtn2, -20, SpringLayout.EAST, modulePanel);
		moduleLayout.putConstraint(SpringLayout.SOUTH, exitBtn2, -20, SpringLayout.SOUTH, modulePanel);
	}
	
	protected void processWindowEvent(WindowEvent e) {		//關閉視窗
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			int reply = -1;
			
			reply = JOptionPane.showConfirmDialog(null,"確定要離開此程式嗎?","確認視窗", JOptionPane.YES_NO_OPTION);
			if(reply == 0){
				System.exit(0);
			}
			else{
				this.setVisible(true);
			}
		}
	}
}