package sequential_pattern;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Iterator;

import data_format.*;

public class csp_panel extends JPanel
{
	JTextField input_text=new JTextField("",20);
	JButton input_jb=new JButton("...");
	JTextField output_text=new JTextField("",20);
	JTextField min_sup=new JTextField("0.3",20);
	JButton act_jb=new JButton("Mining CSP");
	
	DefaultListModel source_list_model=new DefaultListModel();
	JList source_list=new JList(source_list_model);
	JTextArea info=new JTextArea();
	DefaultListModel result_list_model=new DefaultListModel();
	JList result_list=new JList(result_list_model);
	
	JTextField output_dir=new JTextField("tmp",20);
	JButton output_jb=new JButton("Create Feature-Attribute Files");
	JTextField arff_dir=new JTextField("tmp",20);
	JButton arff_jb=new JButton("Create Feature-Attribute Files");	
	
	public csp_panel()
	{
		this.setLayout(new BorderLayout());
		csp_panel_adaptor my_listener=new csp_panel_adaptor(this);
		
		JPanel input_jp=new JPanel(new GridLayout(2,3));
		input_jp.add(new JLabel("Input File(s):"));
		input_jp.add(input_text);
		input_jp.add(input_jb);
		input_jb.addActionListener(my_listener);
		input_jp.add(new JLabel("Minimum Support:"));
		input_jp.add(min_sup);
		input_jp.add(act_jb);
		act_jb.addActionListener(my_listener);
		this.add(input_jp,BorderLayout.NORTH);
		
		JScrollPane info_jsp=new JScrollPane(info);
		this.add(info_jsp,BorderLayout.CENTER);
		JScrollPane result_list_jsp=new JScrollPane(result_list);
		//result_list.setFixedCellWidth( 150 );
		this.add(result_list_jsp,BorderLayout.EAST);
		JScrollPane source_list_jsp=new JScrollPane(source_list);
		//source_list.setFixedCellWidth( 150 );
		this.add(source_list_jsp,BorderLayout.WEST);
		
		JPanel down_jp=new JPanel(new GridLayout(2,1));
		JPanel output_jp=new JPanel(new FlowLayout(FlowLayout.LEFT));
		output_jp.add(new JLabel("Output Directory"));
		output_jp.add(output_text);
		output_jp.add(output_jb);
		output_jb.addActionListener(my_listener);
		down_jp.add(output_jp);
		
		JPanel replace_jp=new JPanel(new FlowLayout(FlowLayout.LEFT));
		replace_jp.add(new JLabel("ARFF Output Directory:"));
		
		down_jp.add(replace_jp);
		this.add(down_jp,BorderLayout.SOUTH);
		
	}
	
	public void actions(ActionEvent ev)
	{
		try
		{
			if(ev.getSource().equals(input_jb))
			{
				JFileChooser jfc=new JFileChooser(new File("./"));				
				jfc.setMultiSelectionEnabled(true);
				jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int result=jfc.showOpenDialog(this);
				if(result==JFileChooser.APPROVE_OPTION)
				{
					File[] all_selected_files=jfc.getSelectedFiles();
					if(all_selected_files.length==1)
					{
						if(all_selected_files[0].isDirectory())
						{
							String[] all_files_in_dir=all_selected_files[0].list();
							input_text.setText(all_selected_files[0].getAbsolutePath());
							for(int i=0;i<all_files_in_dir.length;i++)
							{
								source_list_model.addElement(all_files_in_dir[i]);							
							}
						}
						else
							input_text.setText(all_selected_files[0].getAbsolutePath());
					}
					else
					{
						String text="";
						boolean dir_flg=false;
						for(int i=0;i<all_selected_files.length;i++)
						{
							if(all_selected_files[i].isDirectory())
							{
								JOptionPane.showMessageDialog(this,"Cannot Process Multi-Directory!","Input Format Error",JOptionPane.ERROR_MESSAGE);
								return;
							}
							source_list_model.addElement(all_selected_files[i].getAbsolutePath());
							text+=all_selected_files[i].getName()+";";
								
						}
						input_text.setText(text);
					}
				}
			}
			else if(ev.getSource().equals(output_jb))
			{
			}
		}
		catch(Exception e)
		{
			System.out.println("csp_panel actions exception:"+e);
		}
	}
	
	public static void main(String args[])
	{
		csp_panel b=new csp_panel();
		JFrame a=new JFrame("Class Sequential Pattern Miner");
		a.setLayout(new BorderLayout());
		a.add(b,BorderLayout.CENTER);
		a.setVisible(true);
		a.setSize(800,600);
		a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

class csp_panel_adaptor implements ActionListener
{
	csp_panel adaptee;
	public csp_panel_adaptor(csp_panel adaptee)
	{
		this.adaptee=adaptee;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		adaptee.actions(e);
	}
}