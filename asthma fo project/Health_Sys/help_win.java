package Health_Sys;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.URL;

public class help_win extends JFrame
{
	//JEditorPane jep=new JEditorPane("mailto:lobby@idb.csie.ncku.edu.tw");
	JLabel sys_label=new JLabel(new ImageIcon("img/logo.jpg"));
	JTextArea sys_content=new JTextArea("HAS",4,23);
	JButton ug_bt=new JButton("System Document and User guide");
	JLabel author_label=new JLabel("Presented by IDB, NCKU");
	JButton mail_bt=new JButton("Contact With Us");
	JButton OK_bt=new JButton("OK");
	bt_adapter bt_listener;
	String OS_NAME="WINDOWS";	
	String SYSTEM_INTRODUCTION="";
	
	public help_win() throws Exception
	{						
		super("Help");		
		os_judgement();
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().setBackground(Color.white);	
		JPanel main_jp=new JPanel(null);
		main_jp.setBackground(Color.white);
		bt_listener=new bt_adapter(this);
				
		JPanel text_jp=new JPanel(new FlowLayout(FlowLayout.LEFT));
		sys_content.setText(SYSTEM_INTRODUCTION);
		text_jp.add(new JScrollPane(sys_content, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER ));
		text_jp.setBounds(0,0,300,80);
		
		
		
		
		JPanel link_jp=new JPanel(new GridLayout(4,1));
		
			ug_bt.setBorder(null);		
			ug_bt.setForeground(Color.blue);
			ug_bt.setBackground(Color.white);
			JPanel ug_jp1=new JPanel(new FlowLayout(FlowLayout.LEFT));
			ug_jp1.add(ug_bt);
		link_jp.add(ug_jp1);		
					
			JPanel mail_jp1=new JPanel(new FlowLayout(FlowLayout.LEFT));
			author_label.setFont(new java.awt.Font("Times New Roman", 1, 14));
			mail_jp1.add(author_label);			
		link_jp.add(mail_jp1);
			
			JPanel mail_jp2=new JPanel(new FlowLayout(FlowLayout.LEFT));
			mail_bt.setBorder(null);
			mail_bt.setForeground(Color.blue);
			mail_bt.setBackground(Color.white);					
			mail_jp2.add(mail_bt);
		link_jp.add(mail_jp2);		
		
		link_jp.setBounds(0,80,280,100);

		ug_bt.addActionListener(bt_listener);
		mail_bt.addActionListener(bt_listener);
		OK_bt.addActionListener(bt_listener);

		JPanel ok_jp=new JPanel(new FlowLayout(FlowLayout.RIGHT));
		ok_jp.add(OK_bt);		
				
		text_jp.setBackground(Color.white);
		link_jp.setBackground(Color.white);
		ug_jp1.setBackground(Color.white);
		mail_jp1.setBackground(Color.white);
		mail_jp2.setBackground(Color.white);
		ok_jp.setBackground(Color.white);
		
		main_jp.add(text_jp);		
		main_jp.add(link_jp);

		this.getContentPane().add(sys_label,BorderLayout.NORTH);
		this.getContentPane().add(main_jp,BorderLayout.CENTER);
		this.getContentPane().add(ok_jp,BorderLayout.SOUTH);
				
		setSize(300,320);
		setVisible(true);
		
		//center frame==================================
		Toolkit tk=Toolkit.getDefaultToolkit();
		Dimension wsize=tk.getScreenSize();
		Point start_position=new Point(wsize.width/2-getPreferredSize().width/2,wsize.height/2-getPreferredSize().height/2);
		setLocation(start_position.x,start_position.y);
		//center frame==================================end		
	}
	
	private void os_judgement()
	{
		try
		{
			if(new File("doc/system_introduction").isFile())
			{
				BufferedReader br=new BufferedReader(new FileReader("doc/system_introduction"));
				String buffer="";
				while((buffer=br.readLine())!=null)
					SYSTEM_INTRODUCTION+=buffer+"\n";
				br.close();
			}
			String os=System.getProperty("os.name");
			if(os!=null && os.startsWith("Windows"))
				OS_NAME="WINDOWS";
			else
				OS_NAME="OTHER_OS";
		}
		catch(Exception e)
		{
			System.out.println("help_win os_judgement exception:"+e);
		}
	}
	
	public void action(ActionEvent ev)
	{
		try
		{
			Object temp_obj=ev.getSource();
			Process p;
			if(temp_obj.equals(ug_bt))
			{
				if(OS_NAME.equals("WINDOWS"))
					p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler doc/index.html");
				else
					p = Runtime.getRuntime().exec("netscape -remote openURL doc/index.html");
			}
			else if(temp_obj.equals(mail_bt))
			{
				p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler mailto:lobby@idb.csie.ncku.edu.tw");
			}
			else if(temp_obj.equals(OK_bt))
				this.dispose();
		}
		catch(Exception e)
		{
			System.out.println("help_win action exception:"+e);
		}
	}
}

class bt_adapter implements ActionListener
{
	help_win adaptee;
	public bt_adapter(help_win adaptee)
	{
		this.adaptee=adaptee;
	}
	public void actionPerformed(ActionEvent e)
	{
		adaptee.action(e);
	}
}