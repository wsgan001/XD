package health_exam;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;

public class data_transformer extends JPanel
{
	JPanel input_jp=new JPanel(new BorderLayout());
	JLabel jl1=new JLabel("Class Target File:");
	JTextField class_file_jtf=new JTextField("",10);
	JButton class_file_jb=new JButton("...");
	JTextField lower_bound=new JTextField("",5);
	JTextField upper_bound=new JTextField("",5);
	DefaultListModel file_list_model=new DefaultListModel();
	JList file_list=new JList(file_list_model);
	JScrollPane file_list_jp=new JScrollPane(file_list);
	JPanel function_jp=new JPanel(new FlowLayout(FlowLayout.RIGHT));
	JTextField output_directory=new JTextField("tmp",10);
	JButton add_jb=new JButton("Add");
	JButton remove_jb=new JButton("Remove");
	JCheckBox show_detail=new JCheckBox("Show detail of data processing",true);
	JButton act_jb=new JButton("Class Install!");
	
	
	// 呼叫此方法!!!
	class_install agent=new class_install();
	
	
	
	
	JTextArea output_area=new JTextArea(20,20);
	JScrollPane output_jp=new JScrollPane(output_area);	
	
	public data_transformer()
	{
		setLayout(new BorderLayout());
		add(input_jp,BorderLayout.WEST);
		output_area.setLineWrap(true);
		add(output_jp,BorderLayout.CENTER);		
		data_transformer_adaptor my_listener=new data_transformer_adaptor(this);
		
		JPanel top_input_jp=new JPanel(new GridLayout(2,1));
		JPanel input_subjp1=new JPanel(new FlowLayout(FlowLayout.LEFT));
		input_subjp1.add(jl1);
		input_subjp1.add(class_file_jtf);
		input_subjp1.add(class_file_jb);
		class_file_jb.addActionListener(my_listener);
		//input_subjp1.setAlignmentX(Component.LEFT_ALIGNMENT);
		//input_subjp1.setAlignmentY(Component.TOP_ALIGNMENT);		
		top_input_jp.add(input_subjp1);
		
		JPanel input_subjp2=new JPanel(new FlowLayout(FlowLayout.LEFT));
		input_subjp2.add(new JLabel("Healthy Range:"));
		input_subjp2.add(lower_bound);
		input_subjp2.add(new JLabel("< Health <"));
		input_subjp2.add(upper_bound);
		//input_subjp2.setAlignmentX(Component.LEFT_ALIGNMENT);
		//input_subjp2.setAlignmentY(Component.TOP_ALIGNMENT);
		top_input_jp.add(input_subjp2);
		input_jp.add(top_input_jp,BorderLayout.NORTH);
		
		file_list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);		
		//file_list_jp.setAlignmentX(Component.LEFT_ALIGNMENT);
		//file_list_jp.setAlignmentY(Component.TOP_ALIGNMENT);
		input_jp.add(file_list_jp,BorderLayout.CENTER);
		
		JPanel down_input_jp=new JPanel(new GridLayout(2,1));
		
		function_jp.add(show_detail);
		function_jp.add(add_jb);
		function_jp.add(remove_jb);
		add_jb.addActionListener(my_listener);
		remove_jb.addActionListener(my_listener);
		//function_jp.setAlignmentX(Component.LEFT_ALIGNMENT);
		//function_jp.setAlignmentY(Component.TOP_ALIGNMENT);
		down_input_jp.add(function_jp);
		
		JPanel of_jp=new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel temp_of_jl=new JLabel("Output Directory:");
		of_jp.add(temp_of_jl);
		of_jp.add(output_directory);
		of_jp.add(act_jb);
		act_jb.addActionListener(my_listener);
		//of_jp.setAlignmentX(Component.LEFT_ALIGNMENT);
		//of_jp.setAlignmentY(Component.TOP_ALIGNMENT);
		down_input_jp.add(of_jp);	
		input_jp.add(down_input_jp,BorderLayout.SOUTH);	
	}       
	
	
	public void actions(ActionEvent ev)
	{
		try
		{
			if(ev.getSource().equals(class_file_jb))
			{
				JFileChooser jfc=new JFileChooser(new File("./"));
				int result=jfc.showOpenDialog(this);
				if(result==JFileChooser.APPROVE_OPTION)
				{
					class_file_jtf.setText(jfc.getSelectedFile().getAbsolutePath());
					output_area.append("Class Count:"+agent.set_class_file(class_file_jtf.getText(),-1)+"\n");
					output_area.append(agent.class_set.toString()+"\n");
				}
			}
			else if(ev.getSource().equals(add_jb))
			{
				JFileChooser jfc=new JFileChooser(new File("./"));
				jfc.setMultiSelectionEnabled(true);
				int result=jfc.showOpenDialog(this);
				if(result==JFileChooser.APPROVE_OPTION)
				{
					File[] all_selected_files=jfc.getSelectedFiles();
					for(int i=0;i<all_selected_files.length;i++)
						file_list_model.addElement(all_selected_files[i].getAbsolutePath());
				}
			}
			else if(ev.getSource().equals(remove_jb))
			{
				int[] all_selected_indices=file_list.getSelectedIndices();
				Vector<Object> all_remove_items=new Vector<Object>();
				for(int i=all_selected_indices.length-1;i>=0;i--)
					all_remove_items.add(file_list_model.getElementAt(all_selected_indices[i]));
				for(int i=0;i<all_remove_items.size();i++)
					file_list_model.removeElement(all_remove_items.get(i));
			}
			else if(ev.getSource().equals(act_jb))
			{
				
				agent.set_class_file(class_file_jtf.getText(),-1); //do again to ensure the correctness
				
				
				if(!upper_bound.getText().trim().equals("") && !lower_bound.getText().trim().equals(""))
				{
					// 設定Range
					agent.set_a_health_range(Double.parseDouble(upper_bound.getText()),Double.parseDouble(lower_bound.getText()));
					
					
					output_area.append("After health range setting, Class Count:"+agent.class_set.size()+"\n");
					output_area.append(agent.class_set.toString()+"\n");
				}
				else
				{
					output_area.append("[Warning] Class will be installed without Health Range Setting!");
				}
				for(int i=0;i<file_list_model.size();i++)
				{
					String of_name=(new File((String)file_list_model.getElementAt(i))).getName();
					//System.out.println(of_name);
					if(of_name.lastIndexOf(".")>0)
						of_name=of_name.substring(0,of_name.lastIndexOf("."));
					//System.out.println(of_name);
					String temp_filename="";
					if(!output_directory.getText().trim().equals(""))
						temp_filename=(File.createTempFile(of_name,".txt",new File(output_directory.getText()))).getAbsolutePath();
					else
						temp_filename=(File.createTempFile(of_name,".txt",new File("tmp"))).getAbsolutePath();
					//System.out.println(temp_filename);
					if(show_detail.isSelected())
						output_area.append(file_list_model.getElementAt(i)+"=>"+temp_filename+"\n");
					
					// 將class植入至每個 time series 裡！
					agent.install((String)file_list_model.getElementAt(i),temp_filename);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("data_transformer actions exception:"+e);
		}
	}
	
	public static void main(String args[])
	{
		JFrame a=new JFrame("Data Transformer");
		a.add(new data_transformer(),BorderLayout.CENTER);
		a.setVisible(true);
		a.setSize(800,600);
		a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}

class data_transformer_adaptor implements ActionListener
{
	data_transformer adaptee;
	public data_transformer_adaptor(data_transformer adaptee)
	{
		this.adaptee=adaptee;
	}
	public void actionPerformed(ActionEvent e)
	{
		adaptee.actions(e);
	}
}
