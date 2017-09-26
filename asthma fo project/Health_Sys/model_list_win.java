package Health_Sys;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;
import java.util.HashMap;
import java.util.Iterator;


public class model_list_win extends JInternalFrame
{
	public String SELECTED_MODEL_NAME="";
	
	JPanel list_jp=new JPanel();
	Vector<String> model_list=new Vector<String>();
	Vector<JToggleButton> model_jb_list=new Vector<JToggleButton>();
	//HAS_WIN PARENT=null;
	public JScrollPane info_js=new JScrollPane();
	public JTextArea info=new JTextArea("");
	model_list_win_adapter my_listener=null;
	JPanel function_jp=new JPanel();
	HashMap<String,String> mapping_table=new HashMap<String,String>();
	
	HAS_WIN PARENT=null;
	
	public model_list_win(HAS_WIN p)
	{		
		super("疾病模組列表",true,true,true,true);
		PARENT=p;
		data_init();
		ui_init();		
	}
	
	private void data_init()
	{
		try
		{
			SELECTED_MODEL_NAME="";
			File model_dir=new File("PredictionModels");
			String[] models=model_dir.list();
			model_list.clear();
			for(int i=0;i<models.length;i++)
			{
				model_list.add(models[i]);
			}
		}
		catch(Exception e)
		{
			System.out.println("model_list_win data_init exception:"+e);
		}
	}
	
	private void ui_init()
	{
		try
		{
			info.setText("");
			list_jp.removeAll();
			function_jp.removeAll();
			this.revalidate();			
			
			model_jb_list.clear();
			my_listener=new model_list_win_adapter(this);
			this.setLayout(new BorderLayout());
			list_jp.setLayout(new FlowLayout(FlowLayout.LEFT));			
			for(int i=0;i<model_list.size();i++)
			{
				JToggleButton temp_jb=new JToggleButton(model_list.get(i),false);
				temp_jb.addActionListener(my_listener);
				temp_jb.setSize(100,25);
				list_jp.add(temp_jb);
				model_jb_list.add(temp_jb);
			}
			this.add(list_jp,BorderLayout.NORTH);
			info_js.setViewportView(info);
			this.add(info_js,BorderLayout.CENTER);
			function_jp.setLayout(new FlowLayout(FlowLayout.RIGHT));
			if(model_list.size()==0)
				info.append("尚未建立任何預測模組");
		}
		catch(Exception e)
		{
			System.out.println("model_list_win ui_init exception:"+e);
		}	
	}
	
	public void actions(ActionEvent ev)
	{
		try
		{
			Object temp_ob=ev.getSource();
			if(model_jb_list.contains(temp_ob))
			{
				for(int i=0;i<model_jb_list.size();i++)
				{
					((JToggleButton)model_jb_list.get(i)).setSelected(false);
				}
				JToggleButton temp_jb=(JToggleButton)temp_ob;
				temp_jb.setSelected(true);
				File model_dir=new File("PredictionModels/"+temp_jb.getText());
				info.setText("Selected Model:"+temp_jb.getText()+"\n\n");
				SELECTED_MODEL_NAME=temp_jb.getText();
				
				show_model_info();
				show_text_tree();
				
				generate_function_jp();
				
//				info_js.getVerticalScrollBar().setValue(info_js.getVerticalScrollBar().getMinimum());
//				info_js.getHorizontalScrollBar().setValue(info_js.getHorizontalScrollBar().getMinimum());
				this.updateUI();
			}
		}
		catch(Exception e)
		{
			System.out.println("model_list_win actions exception:"+e);
		}
	}
	
	private void show_model_info()
	{
		try
		{
			BufferedReader br=new BufferedReader(new FileReader("PredictionModels/"+SELECTED_MODEL_NAME+"/"+SELECTED_MODEL_NAME+".info"));
			String buffer="";
			while((buffer=br.readLine())!=null)
			{
				info.append(buffer+"\n");
			}
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("model_list_win show_text_tree exception:"+e);
		}
	}
	
	private void show_text_tree()
	{
		try
		{
			BufferedReader br=new BufferedReader(new FileReader("PredictionModels/"+SELECTED_MODEL_NAME+"/"+SELECTED_MODEL_NAME+".tree"));
			String buffer="";
			info.append("\nText Tree ==================\n");
			while((buffer=br.readLine())!=null)
			{
				info.append(buffer+"\n");
			}
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("model_list_win show_text_tree exception:"+e);
		}
	}
	
	private void generate_function_jp()
	{
		try
		{
			JButton view_tree=new JButton("樹狀圖檢視");
			JButton delete_model=new JButton("刪除此預測模組");
			//JButton test=new JButton("捲");
			view_tree.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e)
					{						
						//PARENT.create_tree_graph("PredictionModels/"+SELECTED_MODEL_NAME+"/"+SELECTED_MODEL_NAME+".tree","PredictionModels/"+SELECTED_MODEL_NAME+"/"+SELECTED_MODEL_NAME+"_MappingInfo.txt");
						PARENT.create_text_tree("PredictionModels/"+SELECTED_MODEL_NAME+"/"+SELECTED_MODEL_NAME+".tree","PredictionModels/"+SELECTED_MODEL_NAME+"/"+SELECTED_MODEL_NAME+"_MappingInfo.txt");
					}
				}
			);
			delete_model.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e)
					{						
						delete_model(SELECTED_MODEL_NAME);
						info.append("Model Deleted: "+SELECTED_MODEL_NAME);
						data_init();
						ui_init();
					}
				}
			);
			
			function_jp.removeAll();
			function_jp.add(view_tree);
			function_jp.add(delete_model);
			function_jp.revalidate();
			this.add(function_jp,BorderLayout.SOUTH);
			this.updateUI();
		}
		catch(Exception e)
		{
			System.out.println("model_list_win generate_function_jp exception:"+e);
		}
	}
	
	public void delete_model(String model_name)
	{
		try
		{
			File data_dir=new File(model_name);
			File model_dir=new File("PredictionModels/"+model_name);
			
			just_delete(data_dir);
			just_delete(model_dir);
		}
		catch(Exception e)
		{
			System.out.println("model_list_win delete_model exception:"+e);
		}
	}
	
	private void just_delete(File a)
	{
		try
		{
			if(a.isDirectory())
			{
				File[] all_files=a.listFiles();
				for(int i=0;i<all_files.length;i++)
					just_delete(all_files[i]);
			}
			a.delete();
		}
		catch(Exception e)
		{
			System.out.println("model_list_win delete_dir exception:"+e);
		}
	}
}

class model_list_win_adapter implements ActionListener
{
	model_list_win adaptee;
	public model_list_win_adapter(model_list_win adaptee)
	{
		this.adaptee=adaptee;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		adaptee.actions(e);
	}
}