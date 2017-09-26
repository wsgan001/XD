package Health_Sys;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.Collections;

public class patient_list extends JPanel
{
	public JTextField search_jtf=new JTextField("",10);
	public JButton search_button=new JButton("篩選");
	public JPanel list_panel;
	DefaultListModel patient_list_model=new DefaultListModel();
	JList patient_list=new JList(patient_list_model);
	
	patient_list_adapter button_listener;
	patient_list_select_adapter select_listener;
	patient_list_key_adapter key_listener;
	Vector<String> all_patients=new Vector<String>();
	
	public prediction_ui PARENT=null;
	
	public patient_list()
	{
		init_ui();
		load_patients();
	}
	
	public patient_list(prediction_ui p)
	{
		PARENT=p;
		init_ui();
		load_patients();
	}
	
	public void init_ui()
	{
		this.setLayout(new BorderLayout());
		JPanel top_jp=new JPanel(new FlowLayout(FlowLayout.CENTER));
		button_listener=new patient_list_adapter(this);
		key_listener=new patient_list_key_adapter(this);
		top_jp.add(search_jtf);
		this.addKeyListener(key_listener);
		search_button.addActionListener(button_listener);
		top_jp.add(search_button);
		this.add(top_jp,BorderLayout.NORTH);
		patient_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		select_listener=new patient_list_select_adapter(this);
		patient_list.addListSelectionListener(select_listener);
		this.add(new JScrollPane(patient_list),BorderLayout.CENTER);
	}
	
	public void load_patients()	
	{
		try
		{
			all_patients.clear();
			BufferedReader br=new BufferedReader(new FileReader("PersonData/sqlfile_outputfile_基本資料與身體評估.txt"));
			String buffer="";
			while((buffer=br.readLine())!=null)
			{
				String all_items[]=buffer.split(",");
				if(!all_patients.contains(all_items[1]))
					all_patients.add(all_items[1]);
			}
			br.close();
			list_patients();
		}
		catch(Exception e)
		{
			System.out.println("patient_list load_patients execption:"+e);
		}
	}
	
	public void list_patients()	
	{
		String keyword=search_jtf.getText();
		try
		{
			patient_list_model.removeAllElements();			
			int y=0;
			Collections.sort(all_patients);
			for(int i=0;i<all_patients.size();i++)
			{
				String temp_no=all_patients.get(i);
				if(temp_no.indexOf(keyword)>=0)
				{
					patient_list_model.addElement(temp_no);
				}
			}			
			patient_list.revalidate();
			patient_list.updateUI();
			this.updateUI();
		}
		catch(Exception e)
		{
			System.out.println("patient_list list_patients execption:"+e);
		}
	}
	
	public void actions(ActionEvent ev)
	{
		try
		{
			if(ev.getSource().equals(search_button))
			{
				list_patients();
			}
		}
		catch(Exception e)
		{
			System.out.println("patient_list actions exception:"+e);
		}
	}
	
	public void select_action(ListSelectionEvent  ev)
	{
		try
		{
			int index=patient_list.getSelectedIndex();
			if(index>=0)
			{
				PARENT.set_patient((String)patient_list.getSelectedValue());
			}
		}
		catch(Exception e)
		{
			System.out.println("patient_list select_actions exception:"+e);
		}		
	}
}

class patient_list_select_adapter implements ListSelectionListener 
{
	patient_list adaptee;
	public patient_list_select_adapter(patient_list adaptee)
	{
		this.adaptee=adaptee;
	}
	
	public void valueChanged(ListSelectionEvent e)
	{
		adaptee.select_action(e);
	}
}

class patient_list_adapter implements ActionListener
{
	patient_list adaptee;
	public patient_list_adapter(patient_list adaptee)
	{
		this.adaptee=adaptee;
	}
	
	public void actionPerformed(ActionEvent ev)
	{
		adaptee.actions(ev);
	}
}

class patient_list_key_adapter extends KeyAdapter
{
	patient_list adaptee;
	public patient_list_key_adapter(patient_list adaptee)
	{
		this.adaptee=adaptee;
	}
	
	public void keyReleased(KeyEvent ke)
	{
		System.out.println("2");
		if(ke.getKeyCode()==KeyEvent.VK_ENTER)
		{			
			adaptee.load_patients();
		}
	}
}