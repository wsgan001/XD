package sequential_pattern;
import java.io.*;
import java.util.HashMap;
import health_exam.*;
import java.util.Vector;

public class basic_mts_rule_match
{
	HashMap<String,String> pattern_file_table=new HashMap<String,String>();
	//input log/file_logs
	public basic_mts_rule_match(String f)
	{
		load_pattern_files(f);
	}
	
	public void load_pattern_files(String f)
	{
		try
		{
			pattern_file_table.clear();
			BufferedReader br=new BufferedReader(new FileReader(f));
			String buffer="";
			String temp_filename="";
			String temp_pattern_sn_filename="";
			while((buffer=br.readLine())!=null)
			{
				if(!buffer.trim().equals(""))
				{
					if(buffer.indexOf("file path")>0)
					{
						temp_filename=buffer.substring(buffer.indexOf("]")+1).trim();
					}
					else if(buffer.indexOf("pattern_sn_filename")>0)
					{	
						temp_pattern_sn_filename=buffer.substring(buffer.indexOf("]")+1).trim();
					}
				}
				else
				{
					if(!temp_filename.equals("") && !temp_pattern_sn_filename.equals(""))
					{
						temp_filename=(new File(temp_filename)).getName();
						
						pattern_file_table.put(temp_filename,temp_pattern_sn_filename);
						temp_filename="";
						temp_pattern_sn_filename="";
					}
				}
			}
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("basic_mts_rule_match load_rules exception:"+e);
		}
	}
	
	public Vector<String> load_file_order(String f)
	{
		Vector<String> result=new Vector<String>();
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(f));
			String buffer="";
			while((buffer=br.readLine())!=null)
			{
				File a=new File(buffer);
				result.add(a.getName());
			}
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("basic_mts_rule_match load_file_order exception:"+e);
		}
		return result;
	}
	
	public String get_match_result(String order_file,String data_file)
	{
		//It's possible that there are many instances in one data_file
		String result="";
		try
		{
			Vector<String> file_order=load_file_order(order_file);
			HashMap<String, String> file_arff=new HashMap<String,String>();
			BufferedReader br=new BufferedReader(new FileReader(data_file));
			String buffer="";
			Vector<String> ts_set=new Vector<String>();
			String temp_class_label="";
			int variable_index=0;
			while(true)
			{
				buffer=br.readLine();
				if(buffer==null)
				{
					break;
				}
				else
				{
					if(buffer.indexOf(":")>=0)
					{
						temp_class_label=buffer.substring(0,buffer.indexOf(":"));
						variable_index=0;
					}
					else
					{
						if(ts_set.size()>variable_index)
							ts_set.set(variable_index,ts_set.get(variable_index)+buffer+"\n");
						else
							ts_set.add(buffer+"\n");
						variable_index++;
					}			
				}
			}
			for(int i=0;i<ts_set.size();i++)
			{
				//build arff file for each ts
				String temp_data_filename=make_temp_file(ts_set.get(i));
				File temp_output_file=File.createTempFile("arff_output",".tmp",new File("tmp/"));
				temp_output_file.deleteOnExit();
				File temp_arff_output_file=File.createTempFile("arff_output",".tmp",new File("tmp/"));
				//temp_arff_output_file.deleteOnExit();
				String temp_variable_name="ts"+i;
				if(pattern_file_table.get(temp_variable_name)!=null)
				{
					String temp_pattern_sn_filename=pattern_file_table.get(temp_variable_name);
					pattern_replace a=new pattern_replace(temp_pattern_sn_filename,temp_data_filename,temp_output_file.getAbsolutePath());
					pattern_replace.table_format(temp_output_file.getAbsolutePath(),temp_pattern_sn_filename,temp_arff_output_file.getAbsolutePath());
					file_arff.put(temp_variable_name,temp_arff_output_file.getAbsolutePath());
				}
				(new File(temp_data_filename)).deleteOnExit();
				/*
				//If there is pattern_sn file, input: pattern_sn_filename,data_filename,output_filename
				String temp_filename=buffer.substring(0,buffer.indexOf(":"));
				String pattern=buffer.substring(buffer.indexOf(":")+1);
				String temp_data_filename=make_temp_file(pattern);
				File temp_output_file=File.createTempFile("output_",".tmp",new File("tmp/"));
				temp_output_file.deleteOnExit();
				File temp_arff_output_file=File.createTempFile("arff_output",".tmp",new File("tmp/"));
				temp_arff_output_file.deleteOnExit();
				//System.out.println(temp_filename);
				if(pattern_file_table.get(temp_filename)!=null)
				{
					//System.out.println(temp_filename+"="+pattern_file_table.get(temp_filename));
					String temp_pattern_sn_filename=pattern_file_table.get(temp_filename);
					pattern_replace a=new pattern_replace(temp_pattern_sn_filename,temp_data_filename,temp_output_file.getAbsolutePath());
					pattern_replace.table_format(temp_output_file.getAbsolutePath(),temp_pattern_sn_filename,temp_arff_output_file.getAbsolutePath());
					System.out.println(pattern_file_table.get(temp_filename));
					System.out.println(temp_data_filename);
					System.out.println(temp_arff_output_file.getAbsolutePath()+"\n");
					
					file_arff.put(temp_filename,temp_arff_output_file.getAbsolutePath());
				}
				(new File(temp_data_filename)).deleteOnExit();*/
			}
			//get arff in order
			Vector<String> arff_order=new Vector<String>();
			for(int i=0;i<file_order.size();i++)
			{
				if(file_arff.get(file_order.get(i))!=null)
				{
					System.out.println(file_order.get(i)+"=>"+file_arff.get(file_order.get(i)));
					arff_order.add(file_arff.get(file_order.get(i)));
				}
			}
			
			arff_together temp_arff_agent=new arff_together();
			temp_arff_agent.join(arff_order,data_file+"_seq.arff");
						
			br.close();
			result=data_file+"_seq.arff";
		}
		catch(Exception e)
		{
			System.out.println("basic_mts_rule_match get_match exception:"+e);
		}
		return result;
	}
	
	public String make_temp_file(String c)
	{
		String result="";
		try
		{
			File data_temp=File.createTempFile("data",".tmp",new File("tmp/"));
			//data_temp.deleteOnExit();
			BufferedWriter bw=new BufferedWriter(new FileWriter(data_temp.getAbsolutePath()));
			bw.write(c+"\n");
			bw.close();
			result=data_temp.getAbsolutePath();
		}
		catch(Exception e)
		{
			System.out.println("basic_mts_rule_match make_temp_file exception:"+e);
		}
		return result;
	}
	
	public static void main(String args[])
	{
		//parameter: [疾病名稱] [受測者病歷號]
		basic_mts_rule_match a=new basic_mts_rule_match("log/"+args[0]+"_file_logs");
		System.out.println(a.get_match_result("log/"+args[0]+"_order_logs",args[1]));
	}
}