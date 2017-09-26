package Health_Sys;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;


public class menuStart extends JFrame implements ActionListener{
   static menuStart frame=new menuStart();
   static Label lab=new Label("**個人健康分析系統**",Label.CENTER);
   static MenuBar mb=new MenuBar();    // 建立MenuBar物件
   static Menu menu1=new Menu("功能");
   static Menu menu2=new Menu("離開");
   static MenuItem mi1=new MenuItem("瀏覽檔案");
   static MenuItem mi2=new MenuItem("病歷號搜尋");
   static MenuItem mi3=new MenuItem("建構預測模組");
   static MenuItem mi4=new MenuItem("個人疾病預測");
   static MenuItem mi6=new MenuItem("所有功能一覽表");
   static MenuItem mi7=new MenuItem("Close window");
   static MenuItem mi8=new MenuItem("test");
   
   public static void main(String args[]){
      	/*try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());   //畫面差異
		} catch(Exception e) { e.printStackTrace(); }	*/
	
		Icon gif1 = new ImageIcon("pi1.gif");
		Icon gif2 = new ImageIcon("pi2.jpg");
		JButton moduleBtn = new JButton("建構預測模組",gif1);
		JButton personalBtn = new JButton("個人風險預測",gif2);
	
	
      frame.setTitle("個人健康分析系統");
      mb.add(menu1);    
      mb.add(menu2);
      //menu1.add(mi1);
      //menu1.add(mi2);
      menu1.add(mi3);
      menu1.add(mi4);
     //menu1.add(mi6);
      menu2.add(mi7); 
      //menu1.add(mi8);
      
      mi1.addActionListener(frame);   // 設定frm為mi1的事件傾聽者
      mi2.addActionListener(frame);   
      mi3.addActionListener(frame);
      mi4.addActionListener(frame);  
      //mi6.addActionListener(frame);
      mi7.addActionListener(frame); 
      mi8.addActionListener(frame);
      
      //picture = new JLabel(createImageIcon("pi1.gif"));
      //picture.setPreferredSize(new Dimension(450,280));
      
      lab.setFont(new Font("Dialog",Font.PLAIN,24));
      frame.add(lab);
      //frame.add(picture,BorderLayout.CENTER);
      //frame.add(moduleBtn);
      //frame.add(personalBtn);
      frame.setSize(500,300);
      frame.setMenuBar(mb);
      frame.setVisible(true);
      
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();   //frame location
	  Dimension frameSize = frame.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
      	frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	
   }
   
   public void actionPerformed(ActionEvent e){  // 事件處理的程式碼
      
      final JFrame adaptee = this;
      
      MenuItem mi=(MenuItem) e.getSource();  // 取得觸發事件的物件
      if(mi==mi1){         // mi1觸發事件
         new viewUI(adaptee);
         this.setVisible(false);
      }     
      else if(mi==mi2){
      	 new SearchUI(adaptee);
         this.setVisible(false);
      }
      else if(mi==mi3){
 		 new classifierUI(adaptee);
         this.setVisible(false); 
      }  
      else if(mi==mi4){
 		 //new personalUI(adaptee);
 		 new prediction_ui();
         this.setVisible(false); 
      } 
      /*else if(mi==mi6){
 		 new All();
         this.setVisible(false); 
      } */  
      
      /*else if(mi==mi8){
 		 new startHAS(adaptee);
         this.setVisible(false); 
      } */
      else if(mi==mi7)       	//離開選項
       {
			int reply = -1;
				
			reply = JOptionPane.showConfirmDialog(null,"確定要離開此程式嗎?","確認視窗", JOptionPane.YES_NO_OPTION);
			if(reply == 0){
				System.exit(0);	
			}			
		}
   }
   
   
   	protected void processWindowEvent(WindowEvent e){		//關閉視窗
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING){
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
