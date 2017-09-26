package Health_Sys;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.swing.event.*;


public class moduleResult extends JFrame {
	
	/*
	JPanel contentPane;
	JLabel txa=new JLabel("");	
   
   
	public moduleResult(double a_c,double a_p,double a_r,double b_c,double b_p,double b_r)throws Exception  {		//Component initialization 
		this.setSize(450,300);
		this.setTitle("結果");
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
		contentPane.setLayout(new BorderLayout());
		contentPane.add(txa,BorderLayout.CENTER);
		
		txa.setText("<html><table width=100%><tr><th>unbalance_accuracy</th><td>"+a_c+"</td></tr>"+
				   "<tr><th>unbalance_precision</th><td>"+a_p+"</td></tr>"+
				   "<tr><th>unbalance_recall</th><td>"+a_r+"</td></tr>"+
				   "<tr><th>balance_accuracy</th><td>"+b_c+"</td></tr>"+
				   "<tr><th>balance_precision</th><td>"+b_p+"</td></tr>"+
				   "<tr><th>balance_recall</th><td>"+b_r+"</td></tr></table></html>");
	}*/
	
	public moduleResult(String f)
	{
		this.setSize(450,300);
		this.setTitle("模組建立結果報告");
		JTextArea report=new JTextArea("");
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(f));
			String buffer="";
			while((buffer=br.readLine())!=null)
			{
				report.append(buffer+"\n");
			}
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("moduleResult exception:"+e);
		}
		this.getContentPane().add(new JScrollPane(report),BorderLayout.CENTER);				
		JButton close_jb=new JButton("OK");
		close_jb.addActionListener(
			new ActionListener()
			{
					public void actionPerformed(ActionEvent e)
					{close();}
			});
		this.getContentPane().add(close_jb,BorderLayout.SOUTH);
		
		this.setVisible(true);
	}
	
	private void close()
	{
		this.dispose();
	}
}