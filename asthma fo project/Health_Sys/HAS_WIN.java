package Health_Sys;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.Collections;
import classifier_gui.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class HAS_WIN extends JFrame
{
	JButton has_jb=new JButton("HAS");
	JButton builder=new JButton("痚痜箇代家舱ミ");
	JButton predictor=new JButton("て痚痜箇代");
	JButton model_list=new JButton("痚痜家舱");
	JButton helper=new JButton("?");
	HAS_WIN_adapter my_listener;
	
	JDesktopPane desktop=new JDesktopPane();
	JToolBar tools=new JToolBar("Major Tools");

	public HAS_WIN()
	{
		super("て胺眃浪琩戈痚痜箇代╰参");
		/*this.getContentPane().setLayout(new GridLayout(1,2));
		my_listener=new HAS_WIN_adapter(this);
		builder.addActionListener(my_listener);
		predictor.addActionListener(my_listener);
		this.getContentPane().add(builder);
		this.getContentPane().add(predictor);*/
		
		system_prepare();		
		
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(desktop,BorderLayout.CENTER);
		my_listener=new HAS_WIN_adapter(this);
		has_jb.setForeground(Color.green);
		has_jb.setBackground(Color.black);
		has_jb.setFont(new Font("Arial", Font.BOLD, 20));
		tools.add(has_jb);
		tools.add(builder);
		tools.add(predictor);
		tools.add(model_list);
		tools.add(helper);
		has_jb.addActionListener(my_listener);
		builder.addActionListener(my_listener);
		predictor.addActionListener(my_listener);
		model_list.addActionListener(my_listener);
		helper.addActionListener(my_listener);
		this.getContentPane().add(tools,BorderLayout.NORTH);
		
		
		//this.setSize(500,400);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//set Frame center
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();   //frame location
		this.setSize((int)(screenSize.getWidth()*2/3),(int)(screenSize.getHeight()*2/3));
		Dimension frameSize = this.getSize();
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	}
	
	private void system_prepare()
	{
		try
		{
			File temp_dir=new File("tmp");
			File[] temp_file_list=temp_dir.listFiles();
			for(int i=0;i<temp_file_list.length;i++)
				temp_file_list[i].delete();
			File log_dir=new File("log");
			File[] log_file_list=log_dir.listFiles();
			for(int i=0;i<log_file_list.length;i++)
				log_file_list[i].delete();
			
			this.setIconImage((new ImageIcon("img/has.jpg")).getImage());
		}
		catch(Exception e)
		{
			System.out.println("HAS_WIN system_prepare exception:"+e);
		}
	}
	
	public void create_builder()
	{
		try
		{
			classifierUI classifier_if=new classifierUI(this);			
			//Dimension size_d=get_if_size();
			Dimension location=get_if_location();
			//classifier_if.setSize(size_d);
			classifier_if.setSize(500,550);
			classifier_if.setLocation((int)location.getWidth(),(int)location.getHeight());
			desktop.add(classifier_if);
			classifier_if.setSelected(true);		
		}
		catch(Exception e)
		{
			System.out.println("HAS_WIN create_builder exception:"+e);
		}
	}
	
	public void create_predictor()
	{
		try
		{
			prediction_ui predictor_if=new prediction_ui();
			Dimension size_d=get_if_size();
			Dimension location=get_if_location();
			predictor_if.setSize(size_d);
			predictor_if.setLocation((int)location.getWidth(),(int)location.getHeight());
			desktop.add(predictor_if);
			predictor_if.setSelected(true);
		}
		catch(Exception e)
		{
			System.out.println("HAS_WIN create_builder exception:"+e);
		}
	}
	
	public void create_tree_graph(String tree_graph, String mapping_info)
	{
		try
		{
			tree_internal_gui my_tree=new tree_internal_gui("Tree View");
			my_tree.draw_tree(tree_graph);
			
			my_tree.setVisible(true);
			Dimension size_d=get_if_size();
			Dimension location=get_if_location();
			my_tree.setSize(size_d);
			my_tree.setLocation((int)location.getWidth(),(int)location.getHeight());
			desktop.add(my_tree);
			my_tree.setSelected(true);
		}
		catch(Exception e)
		{
			System.out.println("HAS_WIN create tree graph exception:"+e);
		}
	}
	
	public void create_text_tree(String tree_graph, String mapping_info)
	{
		try
		{
			text_tree_internal_gui my_tree=new text_tree_internal_gui(tree_graph);
			my_tree.setVisible(true);
			Dimension size_d=get_if_size();
			Dimension location=get_if_location();
			my_tree.setSize(size_d);
			my_tree.setLocation((int)location.getWidth(),(int)location.getHeight());
			desktop.add(my_tree);
			my_tree.setSelected(true);
		}
		catch(Exception e)
		{
			System.out.println("HAS_WIN create tree graph exception:"+e);
		}
	}
	
	public void create_helper()
	{
		try
		{
			help_win a=new help_win();
		}
		catch(Exception e)
		{
			System.out.println("HAS_WIN create helper exception:"+e);
		}
	}
	
	public void create_model_list()
	{
		try
		{
			model_list_win lister=new model_list_win(this);
			lister.setVisible(true);
			Dimension size_d=get_if_size();
			Dimension location=get_if_location();
			lister.setSize(size_d);
			lister.setLocation((int)location.getWidth(),(int)location.getHeight());
			desktop.add(lister);
			lister.setSelected(true);
		}
		catch(Exception e)
		{
			System.out.println("HAS_WIN create_list exception:"+e);
		}
	}
	
	private Dimension get_if_size()
	{
		Dimension result=new Dimension();
		try
		{
			int if_count=desktop.getAllFrames().length;
			int x=desktop.getWidth();
			int y=desktop.getHeight();
			
			result.setSize(x*4/5.0,y*4/5.0);
		}
		catch(Exception e)
		{
			System.out.println("HAS_WIN get_if_size exception:"+e);
		}
		return result;
	}

	private Dimension get_if_location()
	{
		Dimension result=new Dimension();
		try
		{
			int if_count=desktop.getAllFrames().length;
			int x=desktop.getWidth();
			int y=desktop.getHeight();
			x=if_count*25%x;
			y=if_count*25%y;
			result.setSize(x,y);
		}
		catch(Exception e)
		{
			System.out.println("HAS_WIN get_if_location exception:"+e);
		}
		return result;
	}
	
	public void actions(ActionEvent ev)
	{
		try
		{
			if(ev.getSource().equals(builder))
				create_builder();
			else if(ev.getSource().equals(predictor))			
				create_predictor();
			else if(ev.getSource().equals(model_list))
				create_model_list();
			else if(ev.getSource().equals(helper))
				create_helper();
			else if(ev.getSource().equals(has_jb))
			{
				Process p;
				String os=System.getProperty("os.name");
				if(os!=null && os.startsWith("Windows"))
					p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler doc/index.html");
				else
					p = Runtime.getRuntime().exec("netscape -remote openURL doc/index.html");
			}
		}
		catch(Exception e)
		{
			System.out.println("HAS_WIN actions exception:"+e);
		}
	}
	
	public static void main(String args[])
	{
		HAS_WIN a=new HAS_WIN();
	}
}

class HAS_WIN_adapter implements ActionListener
{
	HAS_WIN adaptee;
	public HAS_WIN_adapter(HAS_WIN adaptee)
	{
		this.adaptee=adaptee;
	}
	
	public void actionPerformed(ActionEvent ev)
	{
		adaptee.actions(ev);
	}
}