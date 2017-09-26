package sequential_pattern;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Iterator;

import data_format.*;

public class sp_panel extends JPanel
{	
	JFrame PARENT=null;
	JPanel input_jp=new JPanel(new GridLayout(3,3));
	JLabel filename_jl=new JLabel("Filename");
	JLabel support_jl=new JLabel("Support");
	JTextField filename=new JTextField("candidateArray_SP_flag_q.txt");
	JTextField support=new JTextField("0.3");
	JButton load_jb=new JButton("...");
	JButton action_jb=new JButton("GO");
	JPanel replace_jp=new JPanel(new GridLayout(2,5));
	JLabel pattern_sn_jl=new JLabel("Pattern_filename");
	JTextField pattern_sn_filename=new JTextField("pattern_sn_file");
	JButton pattern_jb=new JButton("...");
	JLabel output_jl=new JLabel("Output_filename");
	JTextField output_filename=new JTextField("output_file");
	JButton output_jb=new JButton("...");
	JButton replace_jb=new JButton("Replace");
	JCheckBox item_repeat=new JCheckBox("Allowed Item(Value) Repeat",false);
	
	JLabel arff_jl=new JLabel("Arff Output Filename:");
	JTextField arff_jtf=new JTextField("arff_output_file.arff");
	
	JPanel save_jp=new JPanel(new GridLayout(1,2));
	JButton save_all_jb=new JButton("Save All Result");
	JButton save_jb=new JButton("Save Sequential Pattern Result");
	
	JTabbedPane result_report=new JTabbedPane();
	sp_panel_listener my_listener;
	private sequential_pattern mining_object;
	
	public sp_panel()
	{
	//	super("Sequential Pattern GUI");
		this.setLayout(new BorderLayout());
		ui_initial();				
	}
	
	public sp_panel(JFrame p)
	{
	//	super("Sequential Pattern GUI");
		this.setLayout(new BorderLayout());
		PARENT=p;
		ui_initial();		
	}
	
	private void ui_initial()
	{
		try
		{			
			my_listener=new sp_panel_listener(this);
			input_jp.add(filename_jl);
			input_jp.add(filename);
			input_jp.add(load_jb);
			input_jp.add(support_jl);
			input_jp.add(support);
			input_jp.add(action_jb);
			input_jp.add(item_repeat);
			load_jb.addActionListener(my_listener);
			action_jb.addActionListener(my_listener);		
			replace_jp.add(pattern_sn_jl);
			replace_jp.add(pattern_sn_filename);
			replace_jp.add(pattern_jb);
			replace_jp.add(output_jl);
			replace_jp.add(output_filename);
			replace_jp.add(output_jb);
			replace_jp.add(arff_jl);
			replace_jp.add(arff_jtf);
			replace_jp.add(replace_jb);
			pattern_jb.addActionListener(my_listener);
			output_jb.addActionListener(my_listener);
			replace_jb.addActionListener(my_listener);
			//save_jp.add(save_all_jb);
			//save_jp.add(save_jb);
			//save_all_jb.addActionListener(my_listener);
			//save_jb.addActionListener(my_listener);
			
			this.add(input_jp,BorderLayout.NORTH);
			this.add(result_report,BorderLayout.CENTER);
			//JPanel under_jp=new JPanel(new GridLayout(2,1));
			//under_jp.add(save_jp);
			//under_jp.add(replace_jp);
			//this.add(under_jp,BorderLayout.SOUTH);	
			this.add(replace_jp,BorderLayout.SOUTH);			
		}
		catch(Exception e)
		{
			System.out.println("sp_panel ui_initial exception:"+e);
		}
	}
	
	public void some_action(ActionEvent ev)
	{
		try
		{
			Object temp=ev.getSource();
			if(temp.equals(load_jb))
			{
				JFileChooser jfc=new JFileChooser(new File("./"));
				int result=jfc.showOpenDialog(this);
				if(result==JFileChooser.APPROVE_OPTION)
					filename.setText(jfc.getSelectedFile().getAbsolutePath());
			}
			else if(temp.equals(pattern_jb))
			{
				JFileChooser jfc=new JFileChooser(new File("./"));
				int result=jfc.showSaveDialog(this);
				if(result==JFileChooser.APPROVE_OPTION)
					pattern_sn_filename.setText(jfc.getSelectedFile().getAbsolutePath());
			}
			else if(temp.equals(output_jb))
			{
				JFileChooser jfc=new JFileChooser(new File("./"));
				int result=jfc.showSaveDialog(this);
				if(result==JFileChooser.APPROVE_OPTION)
					output_filename.setText(jfc.getSelectedFile().getAbsolutePath());
			}
			else if(temp.equals(action_jb))
			{
				mining_object=new sequential_pattern(filename.getText(),Double.parseDouble(support.getText()));
				mining_object.ITEM_REPEAT=item_repeat.isSelected();
				mining_object.start();
				show_result(mining_object.result_filename);
			}
			else if(temp.equals(replace_jb))
			{
				pattern_replace replacer=new pattern_replace(mining_object.result_filename,filename.getText(),output_filename.getText(),pattern_sn_filename.getText());
				System.out.println(mining_object.result_filename+","+filename.getText()+","+output_filename.getText()+","+pattern_sn_filename.getText());
				
				if(!arff_jtf.getText().equals(""))
				{
					pattern_replace.table_format(output_filename.getText(),arff_jtf.getText());
				}
			}
			else if(temp.equals(save_all_jb))
			{
			/*	JFileChooser jfc=new JFileChooser();
				int result=jfc.showSaveDialog(this);
				if(result==JFileChooser.APPROVE_OPTION)
				{		
					save_file_to(mining_object,jfc.getSelectedFile().getAbsolutePath());
				}
			*/				
			}
			else if(temp.equals(save_all_jb))
			{
			}
		}
		catch(Exception e)
		{
			System.out.println("sp_panel some_action exception:"+e);
		}
	}
	
	private void show_result(String f)
	{
		try
		{
			if(!(new File(f)).isFile())
				return;			
			
			BufferedReader br=new BufferedReader(new FileReader(f));
			String buffer="";
			JTextArea all_report=new JTextArea();
			all_report.append(f+"\n=======================================\n");
			HashMap<String,String> all_seq=new HashMap<String,String>();
			String seq_buffer="";
			String seq_title="";
			while(true)
			{
				buffer=br.readLine();
				if(buffer==null || buffer.indexOf("#Large")>=0 || buffer.indexOf("#Candidate")>=0)
				{
					if(!seq_title.equals(""))
						all_seq.put(seq_title,seq_buffer);
					if(buffer==null)
						break;
					seq_title=buffer;
					seq_buffer="";
				}
				else
					seq_buffer+=buffer+"\n";
				all_report.append(buffer+"\n");
			}
			result_report.removeAll();
			result_report.add(new JScrollPane(all_report),"All");
			
			Iterator ir=all_seq.keySet().iterator();
			while(ir.hasNext())
			{
				String temp_title=(String)ir.next();
				JTextArea temp=new JTextArea((String)all_seq.get(temp_title));
				result_report.add(new JScrollPane(temp),temp_title);
			}			
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("sp_panel show_result exception:"+e);
		}
	}
	
	public static void main(String args[])
	{
		sp_panel a=new sp_panel();

		int my_width=700;
		int my_height=600;		
		JFrame f=new JFrame();
		f.getContentPane().add(a,BorderLayout.CENTER);
		//center frame==================================
		Toolkit tk=Toolkit.getDefaultToolkit();
		Dimension wsize=tk.getScreenSize();
		my_width=(int)(wsize.width*0.8);
		my_height=(int)(wsize.height*0.8);
		f.setSize(my_width,my_height);
		Point start_position=new Point(wsize.width/2-f.getWidth()/2,wsize.height/2-f.getHeight()/2);
		f.setLocation(start_position.x,start_position.y);
		//center frame==================================end					
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

class sp_panel_listener implements java.awt.event.ActionListener
{
	sp_panel adaptee;
	public sp_panel_listener(sp_panel adaptee)
	{
		this.adaptee=adaptee;
	}
	public void actionPerformed(ActionEvent e)
	{
		adaptee.some_action(e);
	}
}
