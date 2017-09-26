package health_exam;

import java.io.*;
import java.util.Vector;
import java.util.HashMap;
import java.util.*;
import java.lang.*;



public class my_format_extraction
{
	public Vector<String> all_patient_id=new Vector<String>();
	
	//default in the same directory
	public my_format_extraction()
	{
		save_basic_info();
		combine_health_info();			
	}
	
	//point base file and the directory for id-sorting
	public my_format_extraction(String base_file,String change_dir)
	{
		save_basic_info(base_file);
		combine_health_info(change_dir);			
	}
		
	private void save_basic_info()
	{
		save_basic_info("sqlfile_outputfile_基本資料與身體評估.txt");
	}
	
	private void save_basic_info(String f)
	{	
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(f));
			String buffer="";
			while((buffer=br.readLine())!=null)
			{
				String[] itemset=buffer.split(",");
				if(!all_patient_id.contains(itemset[1]))
					all_patient_id.add(itemset[1]);
			}
			br.close();
			
			System.out.println(" 確認數量 all_patient_id.size() = "+ all_patient_id.size());
		}
		catch(Exception e)
		{
			System.out.println("my_format_exception save_basic_info exception:"+e);
		}
	}
	
	private void combine_health_info()
	{
		combine_health_info("./");
	}
	
	private void combine_health_info(String dir)
	{
		try
		{
			File basic_dir=new File(dir);			
			String[] all_filename=basic_dir.list();
			for(int j=0;j<all_filename.length;j++)
			{
				if(all_filename[j].indexOf("timeseries")>0)
				{
					//String temp_filename=(File.createTempFile("ts"+Integer.toString(j)+"_",".tmp",new File("tmp"))).getName();										
					do_sort((new File(basic_dir,all_filename[j])).getAbsolutePath(),(new File(basic_dir,"tmp/"+all_filename[j])).getAbsolutePath());
					System.out.println((new File(basic_dir,all_filename[j])).getAbsolutePath()+"=>"+(new File(basic_dir,"tmp/"+all_filename[j])).getAbsolutePath());
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("my_format_extraction combin_health_info exception:"+e);
		}
	}
	
	private void do_sort(String input_filename,String output_filename)
	{
		try
		{
			HashMap<String,String> all_transaction=new HashMap<String,String>();
			BufferedReader br=new BufferedReader(new FileReader(input_filename));
			String buffer="";
			while((buffer=br.readLine())!=null)
			{
				String[] itemset=buffer.split(",");
				all_transaction.put(itemset[0],buffer);
			}
			br.close();
			BufferedWriter bw=new BufferedWriter(new FileWriter(output_filename));
			
			// 20091002
			// 國誠撰寫「排序功能」
			Collections.sort(all_patient_id);
			
			for(int i=0;i<all_patient_id.size();i++)
			{
				String key_value=all_patient_id.get(i);
				if(key_value.equals("?") ||  key_value.equals("病歷號"))
					continue;
				if(all_transaction.get(key_value)!=null)
				{
					String all_values[]=((String)all_transaction.get(key_value)).split(",");
					if(all_values.length>=1)
						bw.write(all_values[0]+",");
					for(int vi=1;vi<all_values.length;vi++)
					{
						if(!all_values[vi].equals("?"))
							bw.write(all_values[vi]+",");
					}
				}
				else
					bw.write(key_value+",");
				bw.write("\n");
			}
			bw.close();
		}
		catch(Exception e)
		{
			System.out.println("my_format_extraction do_sort exception:"+e);
		}
	}
	
	
	// 20091024
	// 原主方法!
	public static void main(String args[])
	{
		if(args.length==0)
		{
			my_format_extraction a=new my_format_extraction();
		}
		else
		{
			my_format_extraction a=new my_format_extraction(args[0],args[1]);
		}
	}
	
	
	
	// 20091024
	// 取代「主方法」的「建構子」
	// public my_format_extraction(String )
	
	
	
	
}