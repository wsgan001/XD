package data_format;
import java.io.*;
import java.util.Vector;

public class asthma_segment_processing
{
	String INTEGRATED_FILENAME="";
	public int observation_interval=5;
	public Vector<String> VARIABLE_NAMES=new Vector<String>();
	public int grade_var_index=0;
	public int instruction_var_index=0;
	
	public int grade_index=17;
	public int asthma_index=18;
	
	public Vector<String> VALID_VARIABLE_NAMES=new Vector<String>();
	public Vector<String> attribute_need_sax=new Vector<String>();
		
	public asthma_segment_processing(String f)
	{
		INTEGRATED_FILENAME=f;
	}
	
	private void do_sax(int s,String f)
	{
		attribute_need_sax.clear();
		attribute_need_sax.add("reference");
		attribute_need_sax.add("day_record");
		attribute_need_sax.add("night_record");
		attribute_need_sax.add("night_pefr");
		attribute_need_sax.add("平均相對濕度(%)Relative Humidity");
		attribute_need_sax.add("平均氣溫(℃)Temperature");
		attribute_need_sax.add("極端最高氣溫(℃)Absolute Maximum Temperature");
		attribute_need_sax.add("極端最低氣溫(℃)Absolute Minimum Temperature");	
		attribute_need_sax.add("PSI");
		attribute_need_sax.add("SO2");
		attribute_need_sax.add("NO2");
		attribute_need_sax.add("O3");
		attribute_need_sax.add("CO");
		attribute_need_sax.add("PM10");
		attribute_need_sax.add("NO");
		attribute_need_sax.add("NitroOxy");
		attribute_need_sax.add("NHHC");
		attribute_need_sax.add("HydraCarbon");
		attribute_need_sax.add("HydraCarbon2");
		attribute_need_sax.add("Temp");

		try
		{
			//separate files
			Vector<String> ts_files=separate_ts_files(f);
			System.out.println(ts_files);
			//sax each file
			for(int i=0;i<attribute_need_sax.size();i++)
			{
				String temp_name=attribute_need_sax.get(i);				
				int temp_index=VALID_VARIABLE_NAMES.indexOf(temp_name)+1;
				String temp_filename=ts_files.get(temp_index);
				System.out.println(temp_name+" "+temp_index+" "+temp_filename);
				sax a=new sax();
				a.sax_file(temp_filename,s);
				ts_files.setElementAt(temp_filename+"_saxed",temp_index);
			}
			//combine
			ts_file_combine(ts_files,f);
		}
		catch(Exception e)
		{
			System.out.println("asthma_segment_processing do_sax exception:"+e);
		}
	}
	
	private void ts_file_combine(Vector<String> ts_files, String target_filename)
	{
		try
		{
			Vector<BufferedReader> br_set=new Vector<BufferedReader>();
			BufferedWriter bw=new BufferedWriter(new FileWriter(target_filename));
			for(int i=0;i<ts_files.size();i++)
			{
				BufferedReader temp_br=new BufferedReader(new FileReader(ts_files.get(i)));
				br_set.add(temp_br);
			}
			
			while(true)
			{
				String buffer="";
				for(int i=0;i<br_set.size();i++)
				{
					buffer=((BufferedReader)br_set.get(i)).readLine();
					if(buffer==null)
						break;
					if(i==0)
						bw.write(buffer+"\n");
					else
						bw.write(buffer+";\n");
				}
				bw.write("\n");
				if(buffer==null)
					break;
			}
			
			for(int i=0;i<ts_files.size();i++)
			{
				((BufferedReader)br_set.get(i)).close();
			}
			bw.close();
		}
		catch(Exception e)
		{
			System.out.println("asthma_segment_processing ts_file_combine exception:"+e);
		}
	}
	
	private Vector<String> separate_ts_files(String f)
	{
		Vector<String> result=new Vector<String>();
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(f));
			String buffer="";
			int v_index=0;
			Vector<BufferedWriter> bw_set=new Vector<BufferedWriter>();
			File temp_sn_f=File.createTempFile("asthma_sn",".tmp",new File("tmp"));
			result.add(temp_sn_f.getAbsolutePath());
			bw_set.add(new BufferedWriter(new FileWriter(temp_sn_f.getAbsolutePath())));
			for(int i=0;i<VALID_VARIABLE_NAMES.size();i++)
			{
				File temp_f=File.createTempFile("asthma_ts",".tmp",new File("tmp"));
				result.add(temp_f.getAbsolutePath());
				bw_set.add(new BufferedWriter(new FileWriter(temp_f.getAbsolutePath())));
			}
			while(true)
			{
				buffer=br.readLine();
				if(buffer==null || buffer.equals(""))
				{
					if(buffer==null)
						break;
					v_index=0;
				}
				else
				{
					if(f.indexOf("#")==0)
						continue;
					else
					{
						BufferedWriter temp_bw=(BufferedWriter)bw_set.get(v_index);
						if(buffer.indexOf(";")>=0)
							buffer=buffer.substring(0,buffer.indexOf(";"));
						temp_bw.write(buffer+"\n");
					}
					v_index++;
				}
			}
			for(int i=0;i<bw_set.size();i++)
			{
				((BufferedWriter)bw_set.get(i)).close();
			}
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("asthma_segment_processing separate_ts_files exception:"+e);
		}
		return result;
	}
	
	public void segment_and_separate(String segment_filename)
	{
		segment_and_separate(5,segment_filename);
	}
	
	public void segment_and_separate(int sax,String segment_filename)
	{
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(INTEGRATED_FILENAME));
			String buffer="";
			String instance_buffer="";
			BufferedWriter bw=new BufferedWriter(new FileWriter(segment_filename));
			while(true)
			{
				buffer=br.readLine();
				if(buffer==null || buffer.trim().equals(""))
				{
					if(!instance_buffer.equals(""))
					{
						output_segments(instance_buffer,bw);
						instance_buffer="";
					}
					if(buffer==null)
						break;
				}			
				else
				{
					if(buffer.indexOf("#")==0)
						parse_variable_names(buffer);
					else
						instance_buffer+=buffer;
				}
			}
			bw.close();
			br.close();
			
			do_sax(sax,segment_filename);
		}
		catch(Exception e)
		{
			System.out.println("asthma_segment_processing segment_and_separate exception:"+e);
		}
	}
	
	private void parse_variable_names(String s)
	{
		VARIABLE_NAMES.clear();
		try
		{
			s=s.substring(s.indexOf(":")+1);
			String[] all_items=s.split(",");
			for(int i=0;i<all_items.length;i++)
			{
				VARIABLE_NAMES.add(all_items[i]);
				VALID_VARIABLE_NAMES.add(all_items[i]);
				//sn is not one of the variable, so "index" must -1
				if(all_items[i].equals("grade")) grade_index=i-1;
				if(all_items[i].equals("asthma")) asthma_index=i-1;
			}
			
			VARIABLE_NAMES.remove("ssn");
			VALID_VARIABLE_NAMES.remove("ssn");
		}
		catch(Exception e)
		{
			System.out.println("asthma_segment_processing parse_variable_names exception:"+e);
		}
	}
	
	public void output_segments(String instance_string, BufferedWriter bw)
	{
		try
		{
/*			VARIABLE_NAMES.remove("PH");
			VALID_VARIABLE_NAMES.remove("PH");
			VARIABLE_NAMES.remove("grade");
			VALID_VARIABLE_NAMES.remove("grade");
			VARIABLE_NAMES.remove("asthma");
			VALID_VARIABLE_NAMES.remove("asthma");
			VARIABLE_NAMES.remove("a_rhinitis");
			VALID_VARIABLE_NAMES.remove("a_rhinitis");
			VARIABLE_NAMES.remove("d_ssn");
			VALID_VARIABLE_NAMES.remove("d_ssn");
			VARIABLE_NAMES.remove("date");
			VALID_VARIABLE_NAMES.remove("date");
			VARIABLE_NAMES.remove("監測日期");
			VALID_VARIABLE_NAMES.remove("監測日期");*/

			VALID_VARIABLE_NAMES.remove("PH");

			VALID_VARIABLE_NAMES.remove("grade");

			VALID_VARIABLE_NAMES.remove("asthma");

			VALID_VARIABLE_NAMES.remove("a_rhinitis");

			VALID_VARIABLE_NAMES.remove("d_ssn");

			VALID_VARIABLE_NAMES.remove("date");

			VALID_VARIABLE_NAMES.remove("監測日期");			

			Vector<String> DATA=parse_instance_data(instance_string);
			Vector<Integer> risk_time_points=get_risk_points(DATA);
			//must remove from end to start (avoid reorder situation)
			for(int i=VARIABLE_NAMES.size()-1;i>=0;i--)
			{
				if(!VALID_VARIABLE_NAMES.contains(VARIABLE_NAMES.get(i)))
				{
					DATA.remove(i);
				}
			}
			
			String temp_transaction_string="";
			int instance_length=get_shorted_var_length(DATA);
			System.out.println(instance_length);
			for(int segment_index=0;segment_index<instance_length-observation_interval;segment_index++)
			{
				int end_index=segment_index+observation_interval;
				if(risk_time_points.contains(new Integer(end_index)))
				{
					temp_transaction_string="1:\n";
					for(int data_index=0;data_index<DATA.size();data_index++)
					{
						String[] temp_values=((String)DATA.get(data_index)).split(",");													
						for(int i=segment_index;i<end_index;i++)
							temp_transaction_string+=temp_values[i]+",";
						temp_transaction_string+=";\n";
					}
					//===move segment_index to end_index+1 
					segment_index=end_index; //it will be added one by for loop.
					temp_transaction_string+="\n";
				}
				else
				{
					temp_transaction_string="0:\n";
					for(int data_index=0;data_index<DATA.size();data_index++)
					{
						String[] temp_values=((String)DATA.get(data_index)).split(",");							
						for(int i=segment_index;i<end_index;i++)
							temp_transaction_string+=temp_values[i]+",";
						temp_transaction_string+=";\n";
					}
					temp_transaction_string+="\n";
				}
				bw.write(temp_transaction_string);
			}
		}
		catch(Exception e)
		{
			System.out.println("asthma_segment_processing output_segments exception:"+e);
		}
	}
	
	private int get_shorted_var_length(Vector<String> d)
	{
		int result=-1;
		try
		{
			//System.out.print("L:");
			for(int i=0;i<d.size();i++)
			{
				String[] vars=((String)d.get(i)).split(",");
				//System.out.print(vars.length+",");
				if(vars.length<result || result==-1)
					result=vars.length;
			}
			//System.out.println();
		}
		catch(Exception e)
		{
			System.out.println("asthma_segment_processing get_shortest_var_length exception:"+e);
		}
		return result;
	}
	
	private Vector<String> parse_instance_data(String content)
	{
		//add fake one to avoid "an empty last element"
		content+="?;";
		Vector<String> result=new Vector<String>();
		try
		{
			content=content.substring(content.indexOf(":")+1);
			String[] var_strings=content.split(";");
			//System.out.println(var_strings.length);
			for(int i=0;i<var_strings.length;i++)
				result.add(var_strings[i]);
		}
		catch(Exception e)
		{
			System.out.println("asthma_segment_processing parse_instance_data exception:"+e);
		}
		//remove fake one
		result.remove(result.size()-1);
		return result;
	}
	
	private Vector<Integer> get_risk_points(Vector<String> DATA)
	{
		Vector<Integer> risk_time_points=new Vector<Integer>();
		try
		{
			int check_index=17;
			String[] value_set1=((String)DATA.get(grade_index)).split(",");
			String[] value_set2=((String)DATA.get(asthma_index)).split(",");
			//System.out.print("risk:");
			for(int i=0;i<value_set1.length;i++)
			{
				//System.out.print("["+value_set1[i]+"]");
				if(!value_set1[i].equals("green"))
				{
					if(!value_set2[i].equals("不需服藥") && !value_set2[i].equals("吸入保養藥或早晚口服保養藥(擇一)"))
					{	
						//System.out.print(i+",");
						risk_time_points.add(new Integer(i));
					}
				}
			}
			//System.out.println();
		}
		catch(Exception e)
		{
			System.out.println("asthma_segment_processing segment_and_separate exception:"+e);
		}
		return risk_time_points;
	}
	
	public static void main(String args[])
	{
		asthma_segment_processing a=new asthma_segment_processing(args[0]);
		if(args.length==2)
			a.segment_and_separate(args[1]);
		else if(args.length==3)
			a.segment_and_separate(Integer.parseInt(args[1]),args[2]);
	}
}


/*
class integrated_instance
{
	public Vector<String> DATA=new Vector<String>();
	private Vector<Integer> risk_time_points=new Vector<Integer>();
	public PATIENT_ID="";
	public integrated_instance(String s)
	{
		PATIENT_ID=s.substring(0,s.indexOf(":"));
		s=s.substring(s.indexOf(":")+1);
		get_risk_points();
	}
	
	
	
	public void output_segments(int observation_interval, BufferedWriter bw)
	{
		try
		{			
			String temp_transaction_string="";
			for(int segment_index=0;segment_index<temp_values.length-observation_interval;segment_index++)
			{
				int end_index=segment_index+observaton_interval;
				if(risk_time_points.contains(new Integer(end_index)))
				{
					temp_transaction_string="1:\n";
					for(int data_index=0;data_index<DATA.size();data_index++)
					{
						String temp_values=((String)DATA.get(data_index)).split(",");													
						for(int i=0;i<end_index;i++)
							temp_transaction_string+=temp_values[i]+",";
						temp_transaction_string+=";\n";
					}
					//===move segment_index to end_index+1 
					segment_index=end_index; //it will be added one by for loop.
					temp_transaction_string+="\n";
				}
				else
				{
					temp_transaction_string="0:\n";
					for(int data_index=0;data_index<DATA.size();data_index++)
					{
						String temp_values=((String)DATA.get(data_index)).split(",");							
						//===move segment_index to end_index+1 
						for(int i=0;i<end_index;i++)
							temp_transaction_string+=temp_values[i]+",";
						temp_transaction_string+=";\n";
					}
					temp_transaction_string+="\n";
				}
				bw.write(temp_transaction_string);
			}
		}
		catch(Exception e)
		{
			System.out.println("integrated_instance output_segments exception:"+e);
		}
	}
}
*/