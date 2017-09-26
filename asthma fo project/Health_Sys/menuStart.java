package Health_Sys;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;


public class menuStart extends JFrame implements ActionListener{
   static menuStart frame=new menuStart();
   static Label lab=new Label("**�ӤH���d���R�t��**",Label.CENTER);
   static MenuBar mb=new MenuBar();    // �إ�MenuBar����
   static Menu menu1=new Menu("�\��");
   static Menu menu2=new Menu("���}");
   static MenuItem mi1=new MenuItem("�s���ɮ�");
   static MenuItem mi2=new MenuItem("�f�����j�M");
   static MenuItem mi3=new MenuItem("�غc�w���Ҳ�");
   static MenuItem mi4=new MenuItem("�ӤH�e�f�w��");
   static MenuItem mi6=new MenuItem("�Ҧ��\��@����");
   static MenuItem mi7=new MenuItem("Close window");
   static MenuItem mi8=new MenuItem("test");
   
   public static void main(String args[]){
      	/*try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());   //�e���t��
		} catch(Exception e) { e.printStackTrace(); }	*/
	
		Icon gif1 = new ImageIcon("pi1.gif");
		Icon gif2 = new ImageIcon("pi2.jpg");
		JButton moduleBtn = new JButton("�غc�w���Ҳ�",gif1);
		JButton personalBtn = new JButton("�ӤH���I�w��",gif2);
	
	
      frame.setTitle("�ӤH���d���R�t��");
      mb.add(menu1);    
      mb.add(menu2);
      //menu1.add(mi1);
      //menu1.add(mi2);
      menu1.add(mi3);
      menu1.add(mi4);
     //menu1.add(mi6);
      menu2.add(mi7); 
      //menu1.add(mi8);
      
      mi1.addActionListener(frame);   // �]�wfrm��mi1���ƥ��ť��
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
   
   public void actionPerformed(ActionEvent e){  // �ƥ�B�z���{���X
      
      final JFrame adaptee = this;
      
      MenuItem mi=(MenuItem) e.getSource();  // ���oĲ�o�ƥ󪺪���
      if(mi==mi1){         // mi1Ĳ�o�ƥ�
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
      else if(mi==mi7)       	//���}�ﶵ
       {
			int reply = -1;
				
			reply = JOptionPane.showConfirmDialog(null,"�T�w�n���}���{����?","�T�{����", JOptionPane.YES_NO_OPTION);
			if(reply == 0){
				System.exit(0);	
			}			
		}
   }
   
   
   	protected void processWindowEvent(WindowEvent e){		//��������
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING){
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
