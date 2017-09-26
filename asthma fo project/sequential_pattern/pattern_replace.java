package sequential_pattern;
import java.io.*;
import java.util.Vector;
import java.util.Collections;

import data_format.*;

public class pattern_replace
{
	public String PATTERN_FILENAME;
	public String DATA_FILENAME;	
	public String OUTPUT_FILENAME;	
	public String PATTERN_SN_FILENAME;
	
	private BufferedWriter output_bw=null;
	private BufferedWriter pattern_sn_bw=null;
	public int pattern_sn=0;
	public sp_tree_node ROOT=new sp_tree_node();
	
	public pattern_replace(String pf,String df,String of,String ps)
	{
		PATTERN_FILENAME=pf;
		DATA_FILENAME=df;
		OUTPUT_FILENAME=of;
		PATTERN_SN_FILENAME=ps;
		replace_action();
	}
	
	private void replace_action()
	{
		try
		{
			String buffer="";
			String title="";
			BufferedReader br=new BufferedReader(new FileReader(PATTERN_FILENAME));
			pattern_sn_bw=new BufferedWriter(new FileWriter(PATTERN_SN_FILENAME));
			output_bw=new BufferedWriter(new FileWriter(OUTPUT_FILENAME));
			while((buffer=br.readLine())!=null)
			{
				if(buffer.indexOf("#")==0)
				{
					title=buffer;
				}
				else
				{
					if(title.indexOf("#Large")>=0)
					{
						pattern_sn_bw.write(pattern_sn+":"+buffer+"\n");
						ROOT.path_build_with_sn( (transaction_parser.sequence_score_parser(buffer)).ITEM_SET,pattern_sn);
						pattern_sn++;
					}
				}
			}
						
			replace_seq(output_bw);
			
			br.close();
			output_bw.close();
			pattern_sn_bw.close();
		}
		catch(Exception e)
		{
			System.out.println("pattern_replace replace_action exception:"+e);
		}
	}
	
	//If we know pattern_sn file and want to trasform a new dataset into featured format.=============================================START
	public pattern_replace(String ps,String df,String of)
	{
		PATTERN_FILENAME="";
		DATA_FILENAME=df;
		OUTPUT_FILENAME=of;
		PATTERN_SN_FILENAME=ps;
		replace_action2();
	}
	
	private void replace_action2()
	{
		try
		{
			String buffer="";
			String title="";
			BufferedReader br=new BufferedReader(new FileReader(PATTERN_SN_FILENAME));			
			output_bw=new BufferedWriter(new FileWriter(OUTPUT_FILENAME));
			while((buffer=br.readLine())!=null)
			{
				if(buffer.indexOf(":")>0)
				{
					int pattern_sn=Integer.parseInt(buffer.substring(0,buffer.indexOf(":")));					
					buffer=buffer.substring(buffer.indexOf(":")+1);
					ROOT.path_build_with_sn( (transaction_parser.sequence_score_parser(buffer)).ITEM_SET,pattern_sn);
				}
			}
						
			replace_seq(output_bw);
			
			br.close();
			output_bw.close();
		}
		catch(Exception e)
		{
			System.out.println("pattern_replace replace_action2 exception:"+e);
		}
	}
	//If we know pattern_sn file and want to trasform a new dataset into featured format.=============================================END
	
	private void replace_seq(BufferedWriter output_bw)
	{
//		long start_time=System.currentTimeMillis();
		try
		{
			BufferedReader data_br=new BufferedReader(new FileReader(DATA_FILENAME));
			String buffer="";
			int transaction_count=1;
			while((buffer=data_br.readLine())!=null)
			{
				Vector<Integer> result=new Vector<Integer>();
				transaction temp_t=transaction_parser.class_sequence_parser(buffer);
				//System.out.println(buffer);
				boolean trace_flg=ROOT.pattern_trace_for_sn( (temp_t).ITEM_SET,0,result);
				String temp_string=temp_t.CLASS_LABEL+":";
				if(trace_flg)
				{
					for(int i=0;i<result.size();i++)
					{
						temp_string+=Integer.toString(result.get(i))+",";
					}
				}
				output_bw.write(temp_string+"\n");
				transaction_count++;
			}
			data_br.close();
		}
		catch(Exception e)
		{
			System.out.println("pattern_replace replace_seq  exception:"+e);
		}
//		System.out.println("replace sequence execution time:"+(System.currentTimeMillis() - start_time));
	}
	
	public static void table_format(String df,String of)
	{
		//for weka classification format
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(df));			
			String buffer="";
			Vector<String> all_element_set=new Vector<String>();
			Vector<String> all_class_value=new Vector<String>();
			while((buffer=br.readLine())!=null)
			{
				if(buffer.indexOf(",")<0)
					buffer=buffer+",";
				transaction temp_tran=transaction_parser.auto_parser(buffer);
				Vector<String> temp_set=temp_tran.ITEM_SET;
				if(!all_class_value.contains(temp_tran.CLASS_LABEL))
					all_class_value.add(temp_tran.CLASS_LABEL);
				for(int i=0;i<temp_set.size();i++)
				{
					if(!all_element_set.contains(temp_set.get(i)))
						all_element_set.add(temp_set.get(i));					
				}
			}
			br.close();
			Collections.sort(all_class_value);
			br=new BufferedReader(new FileReader(df));
			BufferedWriter bw=new BufferedWriter(new FileWriter(of));
			bw.write("@relation "+df+"\n");
			for(int i=0;i<all_element_set.size();i++)
			{
				bw.write("@attribute A"+i+" numeric\n");
			}
			bw.write("@attribute CLASS {");
			bw.write("C"+all_class_value.get(0));
			for(int i=1;i<all_class_value.size();i++)
				bw.write(",C"+all_class_value.get(i));
			bw.write("}\n");
			
			bw.write("\n@data\n\n");
			
			while((buffer=br.readLine())!=null)
			{
				if(buffer.indexOf(",")<0)
					buffer=buffer+",";
				transaction temp_transaction=transaction_parser.auto_parser(buffer);
				Vector<String> temp_set=temp_transaction.ITEM_SET;
				String temp_string="";
				for(int i=0;i<all_element_set.size();i++)
				{
					if(temp_set.contains(all_element_set.get(i)))
						temp_string=temp_string+"1,";
					else
						temp_string=temp_string+"0,";
				}				
				bw.write(temp_string+"C"+temp_transaction.CLASS_LABEL+"\n");
			}			
			br.close();
			bw.close();
		}
		catch(Exception e)
		{
			System.out.println("pattern_replace table_format exception"+e);
		}
	}

	public static void table_format(String df,String rf,String of)
	{
		//for weka classification format
//		long start_time= System.currentTimeMillis();
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(df));			
			String buffer="";
			Vector<String> all_element_set=new Vector<String>();
			Vector<String> all_class_value=new Vector<String>();
			while((buffer=br.readLine())!=null)
			{
				String temp_class=buffer.substring(0,buffer.indexOf(":"));
				if(!all_class_value.contains(temp_class))
					all_class_value.add(temp_class);
			}
			br.close();
			Collections.sort(all_class_value);
			br=new BufferedReader(new FileReader(rf));
			while((buffer=br.readLine())!=null)
			{
				String temp_key=buffer.substring(0,buffer.indexOf(":"));
				//System.out.println(temp_key);
				if(!all_element_set.contains(temp_key))
						all_element_set.add(temp_key);					
			}
			br.close();
			br=new BufferedReader(new FileReader(df));
			BufferedWriter bw=new BufferedWriter(new FileWriter(of));
			bw.write("@relation "+df+"\n");
			for(int i=0;i<all_element_set.size();i++)
			{
				bw.write("@attribute A"+i+" numeric\n");
			}
			bw.write("@attribute CLASS {");
			bw.write("C"+all_class_value.get(0));
			for(int i=1;i<all_class_value.size();i++)
				bw.write(",C"+all_class_value.get(i));
			bw.write("}\n");
			
			bw.write("\n@data\n\n");
//			System.out.println("Pattern replace attributes setting execution time:"+(System.currentTimeMillis()-start_time));
			while((buffer=br.readLine())!=null)
			{
				if(buffer.indexOf(",")<0)
					buffer=buffer+",";
				transaction temp_transaction=transaction_parser.auto_parser(buffer);
				Vector<String> temp_set=temp_transaction.ITEM_SET;
				String temp_string="";
				int[] temp_array=new int[all_element_set.size()];
				/*for(int i=temp_array.length;i++)
					temp_array[i]=0;*/
				for(int i=0;i<temp_set.size();i++)
				{
					int temp_index=0;
					try
					{
						temp_index=Integer.parseInt((String)temp_set.get(i));
						temp_array[temp_index]=1;
					}
					catch(Exception e)
					{
					}					
				}
				for(int i=0;i<temp_array.length;i++)
				{
					bw.write(temp_array[i]+",");
				}
				bw.write(temp_string+"C"+temp_transaction.CLASS_LABEL+"\n");
			}			
			br.close();
			bw.close();
		}
		catch(Exception e)
		{
			System.out.println("pattern_replace table_format exception"+e);
		}
	}
	
	//for test
	public static void main(String args[])
	{
		//args[] {pattern_filename data_filename output_filename pattern_sn_filename}
		long start_time=System.currentTimeMillis();
		if(args[0].equals("-t"))
		{
			pattern_replace.table_format(args[1], args[2]);
		}
		else if(args[0].equals("-rt"))
		{
			//pattern must reference another file
			pattern_replace.table_format(args[1], args[2],args[3]);
		}
		else if(args[0].equals("for_testing"))
		{
			//If there is pattern_sn file, input: pattern_sn_filename,data_filename,output_filename
			pattern_replace a=new pattern_replace(args[1],args[2],args[3]);
		}
		else
		{
			pattern_replace a=new pattern_replace(args[0],args[1],args[2],args[3]);
		}
		//pattern_replace a=new pattern_replace("sp_temp46181.txt","test_one","of","ps");
		System.out.println("pattern_replace waste time:"+(System.currentTimeMillis()-start_time));
	}
}
