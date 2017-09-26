package Health_Sys;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import data_format.*;
import Single_Health_Data_Prediction.*;
import java.math.BigDecimal;

public class disease_model_list extends JPanel
{
	public JTextField search_jtf=new JTextField("",10);
	public JButton search_button=new JButton("Search");
	public JPanel list_panel=new JPanel();
	Vector<disease_panel> model_set=new Vector<disease_panel>();
	disease_model_list_adapter button_listener;
	disease_model_list_key_adapter key_listener;
	public JButton predict_selected_jb=new JButton("預測勾選疾病");
	
	public String patient_no="";
	
	public prediction_ui PARENT=null;
	
	public disease_model_list()
	{
		init_ui();
		load_disease_model();
	}
	
	public disease_model_list(prediction_ui p)
	{
		PARENT=p;
		init_ui();
		load_disease_model();
	}
	
	public void init_ui()
	{
		this.setLayout(new BorderLayout());
		JPanel top_jp=new JPanel(new FlowLayout(FlowLayout.CENTER));
		button_listener=new disease_model_list_adapter(this);
		key_listener=new disease_model_list_key_adapter(this);
		top_jp.add(search_jtf);
		this.addKeyListener(key_listener);
		search_button.addActionListener(button_listener);
		top_jp.add(search_button);
		this.add(top_jp,BorderLayout.NORTH);
		list_panel.setLayout(new BoxLayout(list_panel,BoxLayout.Y_AXIS));
		this.add(new JScrollPane(list_panel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED ),BorderLayout.CENTER);
		predict_selected_jb.addActionListener(button_listener);
		this.add(predict_selected_jb,BorderLayout.SOUTH);
	}
	
	public void set_patient(String no)
	{
		try
		{
			patient_no=no;
			for(int i=0;i<model_set.size();i++)
				(model_set.get(i)).set_patient(no);
		}
		catch(Exception e)
		{
			System.out.println("disease_model_list set_patient exception:"+e);
		}
	}
	
	public void load_disease_model()	
	{
		String keyword=search_jtf.getText();
		try
		{
			File model_folder=new File("./PredictionModels");
			File models[]=model_folder.listFiles();
			list_panel.removeAll();
			for(int i=0;i<models.length;i++)
			{
				if(models[i].isDirectory() && models[i].getName().indexOf(keyword)>=0)
				{
					disease_panel temp_jp=new disease_panel(models[i].getName());
					model_set.add(temp_jp);
					list_panel.add(temp_jp);
				}
			}
			
			//list_panel.revalidate();			
			list_panel.updateUI();
		}
		catch(Exception e)
		{
			System.out.println("disease_model_list load_disease_model execption:"+e);
		}
	}
	
	public void actions(ActionEvent ev)
	{
		try
		{
			if(ev.getSource().equals(search_button))
			{
				load_disease_model();
			}
			else if(ev.getSource().equals(predict_selected_jb))
			{
				for(int i=0;i<model_set.size();i++)
				{
					disease_panel temp_jp=model_set.get(i);
					if(temp_jp.jcb.isSelected())
						temp_jp.data_prepare();
				}
				PersonHealthPrediction a=new PersonHealthPrediction("Single_Health_Data_Prediction/pred_input",patient_no);				
				for(int i=0;i<model_set.size();i++)
				{
					disease_panel temp_jp=model_set.get(i);
					if(temp_jp.jcb.isSelected())
					{
						if(a.RankHealthRiskName.contains(temp_jp.disease_name))
						{
							int index=a.RankHealthRiskName.indexOf(temp_jp.disease_name);
							//String temp_output=Double.toString(Double.parseDouble(a.RankHealthRiskConf.get(index))*100.0);
							//temp_output=temp_output.substring(0,temp_output.indexOf(".")+2);
							//temp_jp.report.append("獲得此種疾病的風險為: "+temp_output+"%\n");
							BigDecimal bd =new BigDecimal(Double.parseDouble(a.RankHealthRiskConf.get(index))*100.0);
							temp_jp.report.setText("獲得此種疾病的風險為: "+ bd.setScale(2,BigDecimal.ROUND_HALF_UP) +"%\n");
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("disease_model_list actions exception:"+e);
		}
	}
}

class disease_model_list_adapter implements ActionListener
{
	disease_model_list adaptee;
	public disease_model_list_adapter(disease_model_list adaptee)
	{
		this.adaptee=adaptee;
	}
	
	public void actionPerformed(ActionEvent ev)
	{
		adaptee.actions(ev);
	}
}

class disease_model_list_key_adapter extends KeyAdapter
{
	disease_model_list adaptee;
	public disease_model_list_key_adapter(disease_model_list adaptee)
	{
		this.adaptee=adaptee;
	}
	
	public void keyReleased(KeyEvent ke)
	{
		if(ke.getKeyCode()==KeyEvent.VK_ENTER)
		{			
			adaptee.load_disease_model();
		}
	}
}

class disease_panel extends JPanel
{
	public String disease_name="";
	public JCheckBox jcb;
	public JTextField report;
	public JButton predict_jb;
	String patient_no="";
	public disease_panel(String dn)
	{
		this.setLayout(new BorderLayout());
		disease_name=dn;
		jcb=new JCheckBox(disease_name,true);
		this.add(jcb,BorderLayout.WEST);
		//report=new JTextArea(5,10);
		//this.add(new JScrollPane(report),BorderLayout.CENTER);
		report=new JTextField(disease_name+" 預測機制");
		this.add(report,BorderLayout.CENTER);
		predict_jb=new JButton("只預測"+disease_name);
		this.add(predict_jb,BorderLayout.EAST);		
		predict_jb.addActionListener(new disease_panel_adapter(this));
		predict_jb.setEnabled(false);
	}	
	
	public void data_prepare()
	{
		try
		{
			//BufferedReader br=new BufferedReader(new FileReader("log/"+disease_name+"_log"));
			//br.readLine();
			//String path=br.readLine();
			//br.close();
			//String path=disease_name+"資料";
//String path="PredictionModels/"+disease_name+"/data"; //==========for future
			String path=disease_name;
			get_health_data a=new get_health_data(path);
			a.get_one_patient(patient_no);
			a.transform_into_arff(disease_name,"tmp/"+patient_no+"_"+disease_name+".arff");
			
			//copy file
			File inputFile = new File("tmp/"+patient_no+"_"+disease_name+".arff ");
	    		File outputFile = new File("Single_Health_Data_Prediction/pred_input/"+patient_no+"_"+disease_name+".arff ");
	
	    		FileInputStream in = new FileInputStream(inputFile);
	    		FileOutputStream out = new FileOutputStream(outputFile);
	    		byte[] buf = new byte[1024];
			int i = 0;
			while ((i = in.read(buf)) != -1)
			{
				out.write(buf, 0, i);
			}
			in.close();
			out.close();
		}
		catch(Exception e)
		{
			System.out.println("disease_panel data_prepare exception:"+e);
		}
	}
	
	public void predict()
	{
		try
		{
			data_prepare();
			PersonHealthPrediction a=new PersonHealthPrediction("Single_Health_Data_Prediction/pred_input",patient_no);
			if(a.RankHealthRiskName.contains(disease_name))
			{
				int index=a.RankHealthRiskName.indexOf(disease_name);
				BigDecimal bd =new BigDecimal(Double.parseDouble(a.RankHealthRiskConf.get(index))*100.0);
				report.setText("獲得此種疾病的風險為: "+ bd.setScale(2,BigDecimal.ROUND_HALF_UP) +"%\n");
				//report.append("獲得此種疾病的風險為: "+Double.toString(Double.parseDouble(a.RankHealthRiskConf.get(index))*100.0)+"%\n");
			}
		}
		catch(Exception e)
		{
			System.out.println("disease_panel predict exception:"+e);
		}
	}
	
	public void set_patient(String no)
	{
		patient_no=no;
		predict_jb.setEnabled(true);
		report.setText("");
	}
}

class disease_panel_adapter implements ActionListener
{
	disease_panel adaptee;
	public disease_panel_adapter(disease_panel adaptee)
	{
		this.adaptee=adaptee;
	}
	
	public void actionPerformed(ActionEvent ev)
	{
		adaptee.predict();
	}
}