package Health_Sys;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;


public class patient_info_ui extends JPanel
{		
	public String PATIENT_INFO_FILENAME="PersonData/sqlfile_outputfile_�򥻸�ƻP�������.txt";		
	public String ATTRIBUTE_NAMES="CHK_NO,�f����,�ͤ�,�ˬd��,�f�v,BMI,�ֽ�,���~,���,���`�N�ƶ�,�V�ʯ�,�V�R��,�Ҫ���,�ݳ��~��,�ũ�,��Ŧ,��Ŧ,�����~��,�����Ͳ�,�������E,����Ĳ�E,�xŦ,��Ŧ,��Ŧ,���ѷ�,���@��,�`�x�Ϯg,�L���Ϯg,�B�ʥ\��,�Pı�\��,���,�|��,�٦פΦٸx";
	public JLabel attribute_jl=new JLabel();
	public JLabel header_jl;
	Vector<Vector<String>> all_reports=new Vector<Vector<String>>();
	public patient_info_ui()
	{
		this.setLayout(new BorderLayout());
		header_jl=new JLabel("<html><table width=100% border=1><tr><th><b>�f�w�򥻸��</b></td></tr></table></html>");
		this.add(header_jl,BorderLayout.NORTH);
		this.add(new JScrollPane(attribute_jl),BorderLayout.CENTER);
		set_patient("");
	}
		
	public void set_file(String x)
	{
		this.PATIENT_INFO_FILENAME=x;
	}
	
	public void set_patient(String no)
	{
		try
		{
			all_reports.clear();
			BufferedReader br=new BufferedReader(new FileReader(PATIENT_INFO_FILENAME));
			String buffer="";
			//this.remove(header_jl);
			//this.remove(attribute_jl);
			this.removeAll();
			Vector<String> column_names=new Vector<String>();
			Vector<String> attributes=new Vector<String>();
			String names[]=ATTRIBUTE_NAMES.split(",");
			int count=0;
			String info_string="<html><table width=100% border=1>";
			for(int i=0;i<names.length;i++)
			{
				column_names.add(names[i]);
				attributes.add(names[i]+":\t");
			}
			while((buffer=br.readLine())!=null)
			{
				String all_items[]=buffer.split(",");
				if(all_items[1].equals(no))
				{
					count++;
					Vector<String> temp_vector=new Vector<String>();
					for(int i=0;i<all_items.length && i<attributes.size();i++)
					{
						attributes.set(i,attributes.get(i)+all_items[i]+",");
						temp_vector.add(all_items[i]);
					}
					all_reports.add(temp_vector);
				}
			}
			
			for(int i=0;i<attributes.size();i++)
			{
				String temp_string=attributes.get(i);
				if(i==0)
					info_string+="<tr>";
				else if(i%3==0)
					info_string+="</tr><tr>";
				info_string+="<th>"+temp_string.substring(0,temp_string.indexOf(":"))+"</th><td>"+temp_string.substring(temp_string.indexOf(":")+1)+"</td>";
			}
			info_string+="</tr>";
			info_string+="</table></html>";
			attribute_jl=new JLabel(info_string);
			br.close();
			
			header_jl=new JLabel("<html><b>�����̰򥻸��("+no+") "+count+"�� "+attributes.size()+"�� ���˸��</b></html>");
			this.add(header_jl,BorderLayout.NORTH);
			//this.add(new JScrollPane(attribute_jl),BorderLayout.CENTER);
			
			
			DefaultTableModel dtm=my_show_table(all_reports,column_names);
			JTable data_table=new JTable(dtm);
			this.add(new JScrollPane(data_table),BorderLayout.CENTER);
			
			this.revalidate();
			this.updateUI();
		}
		catch(Exception e)
		{
			System.out.println("patient_info_ui set_patient exception:"+e);
		}
	}
	
	private DefaultTableModel my_show_table(Vector<Vector<String>> data,Vector<String>  col)
	{
		Vector<String> new_col=new Vector<String>();
		Vector<Vector<String>> new_data=new Vector<Vector<String>>();
		try
		{			
			new_col.add("�˴�����\\�˴�����");
			for(int i=0;i<data.size();i++)
			{
				new_col.add(Integer.toString(i+1));
			}
			for(int c=0;c<col.size();c++)
			{
				Vector<String> new_temp_vector=new Vector<String>();
				new_temp_vector.add(col.get(c));
				for(int i=0;i<data.size();i++)
				{
					Vector<String> temp_vector=data.get(i);
					new_temp_vector.add(temp_vector.get(c));
				}
				new_data.add(new_temp_vector);
			}
		}
		catch(Exception e)
		{
			System.out.println("patient_info_ui my_show_table exception:"+e);
		}
		return (new DefaultTableModel(new_data,new_col));
	}
}