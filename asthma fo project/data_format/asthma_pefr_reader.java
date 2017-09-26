package data_format;
import java.io.*;
import java.util.Vector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collections;
import java.util.Comparator;

//generate positive dataset and negative dataset
public class asthma_pefr_reader
{
	String FILENAME="";
	String WEATHER_FILENAME="";
	String POLLUTANT_FILENAME="";
	public boolean ORDER_CHECK=false;
	Vector<String> column_names=new Vector<String>();
	public asthma_pefr_reader(String f)
	{
		FILENAME=f;
		ORDER_CHECK=time_check();	//if true, this data is order by time.
		if(!ORDER_CHECK)
			FILENAME=generate_reordered_file(FILENAME);
	}
	
	public asthma_pefr_reader(String f,String wf,String pf)
	{
		FILENAME=f;
		WEATHER_FILENAME=wf;
		POLLUTANT_FILENAME=pf;
		ORDER_CHECK=time_check();	//if true, this data is order by time.
		if(!ORDER_CHECK)
			FILENAME=generate_reordered_file(FILENAME);
	}
	
	
	//output each attribute into a file in my time series file format
	public void output(String output_dir)
	{		
		try
		{			
			if(!(new File(output_dir)).isDirectory())
			{
				(new File(output_dir)).mkdir();
			}
			BufferedReader br=new BufferedReader(new FileReader(FILENAME));
			String buffer="";
			HashMap<String,Vector<String>> all_data=new HashMap<String,Vector<String>>();
			Vector<String> temp_user_data=null;
			buffer=br.readLine(); // read column...
			while((buffer=br.readLine())!=null)
			{
				String[] temp_itemset=instance_parse(buffer);
				temp_user_data=null;
				if(all_data.containsKey(temp_itemset[0]))
				{
					temp_user_data=all_data.get(temp_itemset[0]);
					for(int i=0;i<temp_user_data.size();i++)
						temp_user_data.set(i,temp_user_data.get(i)+","+temp_itemset[i+1]);
				}
				else
				{
					temp_user_data=new Vector<String>();
					for(int i=1;i<temp_itemset.length;i++)		//do not record ssn
						temp_user_data.add(temp_itemset[i]);
				}
				all_data.put(temp_itemset[0],temp_user_data);
			}
			br.close();
			
			//write all patient ssn in ssn_set
			Iterator ir=all_data.keySet().iterator();
			Vector<String> all_ssn=new Vector<String>();
			BufferedWriter temp_bw=new BufferedWriter(new FileWriter(new File(new File(output_dir),"ssn")));
			while(ir.hasNext())
			{
				String temp_ssn=(String)ir.next();
				all_ssn.add(temp_ssn);
				//System.out.print(temp_ssn+","); //Just print how many patient
				temp_bw.write(temp_ssn+"\n");
			}
			temp_bw.close();
			
			//prepare weather and pollutant data
			//generate file for each time series in "integrated"
			weather_data_reader wdr=new weather_data_reader(WEATHER_FILENAME);
			pollutant_data_reader pdr=new pollutant_data_reader(POLLUTANT_FILENAME);
			
			temp_bw=new BufferedWriter(new FileWriter(new File(new File(output_dir),"integrated")));
			
			//column names
			temp_bw.write("#Attribute:");
			for(int i=0;i<column_names.size();i++)
				temp_bw.write(column_names.get(i)+",");

			for(int i=0;i<wdr.COLUMN_NAMES.size();i++)
				temp_bw.write(wdr.COLUMN_NAMES.get(i)+",");

			for(int i=0;i<pdr.COLUMN_NAMES.size();i++)
				temp_bw.write(pdr.COLUMN_NAMES.get(i)+",");
			temp_bw.write("\n");
			
			for(int i=0;i<all_ssn.size();i++)
			{
				String temp_ssn=all_ssn.get(i);
				String temp_time_sequence="";				
				Vector<String> temp_vector=all_data.get(temp_ssn);
				temp_bw.write(temp_ssn+":\n");
				for(int j=0;j<temp_vector.size();j++)
				{
					temp_bw.write(temp_vector.get(j)+";\n");  // actually, my first one is column_names' second one
					if(column_names.get(j+1).equals("date"))	//column_name has one more attribute "ssn", so index must add 1.
					{
					//	System.out.println(temp_vector.get(j));
						temp_time_sequence=temp_vector.get(j);
					}
				}
				
				if(wdr.READY)
				{
					Vector<String> temp_weather_vector=wdr.get_weather_chain(temp_time_sequence);
					for(int w=0;w<temp_weather_vector.size();w++)
						temp_bw.write(temp_weather_vector.get(w)+";\n");
				}
				if(pdr.READY)
				{
					Vector<String> temp_pollutant_vector=pdr.get_pollutant_chain(temp_time_sequence);
					for(int p=0;p<temp_pollutant_vector.size();p++)
						temp_bw.write(temp_pollutant_vector.get(p)+";\n");
				}
					
				temp_bw.write("\n");
			}
			temp_bw.close();
			
			//generate all files in output dir
			for(int i=1;i<column_names.size();i++)  //from 1 , actually we do not need ssn sequence.
			{
				temp_bw=new BufferedWriter(new FileWriter(new File(new File(output_dir),column_names.get(i))));
				for(int j=0;j<all_ssn.size();j++)
				{
					String temp_ssn=all_ssn.get(j);
					Vector<String> temp_vector=all_data.get(temp_ssn);
					temp_bw.write(temp_ssn+":"+temp_vector.get(i-1)+"\n");  // actually, my first one is column_names' second one
				}
				temp_bw.close();
			}
					
			//generate abnormal set and normal set
			//cut_time_series_into_np_files();
		}
		catch(Exception e)
		{
			System.out.println("asthma_pefr_reader output exception:"+e);
		}
	}
	
	private String[] instance_parse(String a)
	{		
		try
		{
			String[] result=a.split(",");
			for(int i=0;i<result.length;i++)
				result[i]=result[i].replace("\"","").trim();
			return result;
		}
		catch(Exception e)
		{
			System.out.println("asthma_pefr_reader instance_parse exception:"+e);
		}
		return null;
	}
	
	public boolean time_check()
	{
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(FILENAME));
			String buffer="";
			buffer=br.readLine();
			column_parse(buffer);
			long pre=0;
			while((buffer=br.readLine())!=null)
			{
				String[] items=buffer.split(",");				
				long temp=Long.parseLong(items[8].replace("\"","").trim());
				if(temp<pre)
				{
					return false;
				}
				pre=temp;
			}
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("asthma_pefr_reader time_check exception:"+e);
		}
		return true;
	}
	
	private void column_parse(String s)
	{
		try
		{
			column_names.clear();
			String[] all_items=s.split(",");
			for(int i=0;i<all_items.length;i++)
			{				
				column_names.add(all_items[i].replace("\"","").trim());
			}
		}
		catch(Exception e)
		{
			System.out.println("asthma_pef_reader column_parse exception:"+e);
		}
	}
	
	private long get_time(String d)
	{
		long result=0;
		try
		{
			int date_index=column_names.indexOf("date");
			String[] all_items=d.split(",");
			result=Long.parseLong(all_items[date_index].replace("\"","").trim());
		}
		catch(Exception e)
		{
			System.out.println("asthma_pefr_reader get_time exception:"+e);
		}
		return result;
	}
	
	public String generate_reordered_file(String f)
	{
		String result="";
		try
		{
			Vector<String> all_instances=new Vector<String>();
			if(!(new File("tmp")).isDirectory())
			{
				(new File("tmp")).mkdir();
				result="tmp/reordered_asthma_pefr";
			}
			BufferedReader br=new BufferedReader(new FileReader(f));
			BufferedWriter bw=new BufferedWriter(new FileWriter(result));
			String buffer="";
			//read
			while((buffer=br.readLine())!=null)
			{
				all_instances.add(buffer);
			}
			//sort
			Collections.sort(all_instances, new Comparator<String>()
				{
					public int compare(String a, String b)
					{
						return (int)((get_time(a)-get_time(b))%10);
					}
				}
			);
			//write			
			for(int i=0;i<all_instances.size();i++)
			{
				bw.write(all_instances.get(i)+"\n");
			}
			bw.close();
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("asthma_pefr_reader generate_reorder_file exception:"+e);
		}
		return result;
	}
	
	public static void main(String args[])
	{
		if(args.length==2)
		{
			asthma_pefr_reader a=new asthma_pefr_reader(args[0]);
			a.output(args[1]);
		}
		else if(args.length==4)
		{
			asthma_pefr_reader a=new asthma_pefr_reader(args[0],args[1],args[2]);
			a.output(args[3]);
		}
		else
		{
			System.out.println("Please input two parameters [asthma_pefr_filename, output_dir] or four parameter [asthma_pefr_filename. weather_filename, pollutant_filename, output_dir].");
		}
	}
}