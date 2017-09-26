package progressive_pattern;
import java.io.*;
import java.util.Vector;
import java.util.HashMap;
import java.util.Iterator;

import data_format.*;

// all result will output into a file.

public class progressive_pattern
{
	public String DATASET_FILENAME=""; //filename
	//public Vector<pp_tree_node> ROOT_SET;
	public HashMap<String,item_state> FREQUENT_ITEM;  //item with the state
	public HashMap<String,Integer> CLASS_COUNT;
	public int TRANSACTION_COUNT;
	public double SUPPORT;
	public int SUPPORT_COUNT;
	public String result_filename="";
	private BufferedWriter result_bw;

//	public boolean ATTRIBUTE_FORMAT_TRANSFORM=false;	// If this is true, this program will generate the pattern attrubte file, in which all generated pattern are attributes and each sequence(time-series) will transform into a attribute set.
//	private int PATTERN_INDEX=0;	// This is use for counting the number of the generated patterns;
//	public String ATTTRIBUTE_FORMAT_FILENAME=""; //If [ATTRIBUTE_FORMAT_TRANSFORM=true] this filename will not be "".
	
	public double BASIC_CONFIDENCE=0.0;
	public double FULL_CONFIDENCE=0.5;
	
	public boolean ITEM_REPEAT=false;
	
	public progressive_pattern(String f,double s,double c1,double c2)
	{
		DATASET_FILENAME=f;
		TRANSACTION_COUNT=0;
		SUPPORT=s;
		SUPPORT_COUNT=0;
		BASIC_CONFIDENCE=c1;
		FULL_CONFIDENCE=c2;
		//ROOT_SET=new Vector<pp_tree_node>();
		FREQUENT_ITEM=new HashMap<String,item_state>();
		CLASS_COUNT=new HashMap<String,Integer>();
	}
		
	public void start()
	{		
		create_result_file();
		preprocess();		
		progressive_pattern_mining();
	}	
	
	/*create a file for storage of mined patterns*/
	private void create_result_file()
	{
		try
		{
			File result_file=File.createTempFile("pp_temp",".txt",new File("tmp"));
			//result_file.deleteOnExit();    //It will let file be deleted when program over
			result_filename=result_file.getAbsolutePath();
			System.out.println("Progressive Pattern File:"+result_filename);
			result_bw=new BufferedWriter(new FileWriter(result_file));
/*
			if(ATTRIBUTE_FORMAT_TRANSFORM)
			{
				PATTERN_INDEX=0;
				File p_file=File.createTempFile("PA_TEMP",".txt",new File("tmp"));
				ATTRIBUTE_FORMAT_FILENAME=p_file.getAbsolutePath();
				System.out.println("ATTRIBUTE_FORMAT_FILENAME:"+ATTRIBUTE_FORMAT_FILENAME);
			}
*/
		}
		catch(Exception e)
		{
			System.out.println("progressive_pattern create_result_file exception:"+e);
		}
	}
	
	public class item_state
	{
		public String item_name="";
		public int support=0;
		public double confidence=-1.0;
		public HashMap<String,Integer> class_count=new HashMap<String,Integer>();
		public Vector<Integer> coverage=new Vector<Integer>(); //transaction#
		public item_state(String i)
		{}
		public void add(String c)
		{
			if(class_count.containsKey(c))
			{
				int x=((Integer)class_count.get(c)).intValue()+1;
				class_count.put(c,new Integer(x));
			}
			else
			{
				class_count.put(c,new Integer(1));
			}
			support++;
		}
		public double get_highest_confidence()
		{
			int k=0;
			if(confidence>=0)
				return confidence;
			else
			{
				try
				{
					Iterator ir=class_count.keySet().iterator();				
					while(ir.hasNext())
					{
						int u=((Integer)class_count.get((String)ir.next())).intValue();
						if(u>k || k==0)
							k=u;
					}				
				}
				catch(Exception e)
				{
					System.out.println("item_state get_highest_confidence exception:"+e);
				}
				return k/(double)support;
			}
		}		
	}
	
	/*Find all frequent and high-confident one*/
	public void preprocess()
	{
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(DATASET_FILENAME));
			//Vector<Integer> freq_count=new Vector<Integer>();
			String buffer="";
			TRANSACTION_COUNT=0;		
			//Count all firstly
			while((buffer=br.readLine())!=null)
			{
				transaction temp_transaction=transaction_parser.class_sequence_parser(buffer);
				for(int i=0;i<temp_transaction.ITEM_SET.size();i++)
				{
					String temp_item_name=(String)temp_transaction.ITEM_SET.get(i);
					item_state temp_item_state=null;
					if(FREQUENT_ITEM.containsKey(temp_item_name))
						temp_item_state=(item_state)FREQUENT_ITEM.get(temp_item_name);
					else
						temp_item_state=new item_state(temp_item_name);						
					temp_item_state.add(temp_transaction.CLASS_LABEL);
					FREQUENT_ITEM.put(temp_item_name,temp_item_state);
				}
				if(CLASS_COUNT.containsKey(temp_transaction.CLASS_LABEL))
				{
					int cc=((Integer)CLASS_COUNT.get(temp_transaction.CLASS_LABEL)).intValue()+1;
					CLASS_COUNT.put(temp_transaction.CLASS_LABEL,new Integer(cc));
				}
				else
					CLASS_COUNT.put(temp_transaction.CLASS_LABEL,new Integer(1));
				TRANSACTION_COUNT++;
			}
			System.out.println("Transaction Count:"+TRANSACTION_COUNT);
			System.out.println("Class Count:"+CLASS_COUNT);
			
			SUPPORT_COUNT=(int)(TRANSACTION_COUNT*SUPPORT);
			br.close();
			
			result_bw.write("#Large 1\n");
			//System.out.println("#Large 1");
			Iterator item_ir=FREQUENT_ITEM.keySet().iterator();
			Vector<String> remove_item_set=new Vector<String>();
			while(item_ir.hasNext())
			{
				String temp_item_name=(String)item_ir.next();				
				item_state temp_item_state=(item_state)FREQUENT_ITEM.get(temp_item_name);
				if(temp_item_state.support>=SUPPORT_COUNT)
				{
					String feature_string="";
					//result_bw.write(temp_item_name+":"+temp_item_state.support);
					feature_string+=temp_item_name+":"+temp_item_state.support;
					Iterator ir=temp_item_state.class_count.keySet().iterator();
					Vector<String> remove_set=new Vector<String>();
					//result_bw.write("[");
					feature_string+="[";
					while(ir.hasNext())
					{
						String class_name=(String)ir.next();
						int cv=((Integer)temp_item_state.class_count.get(class_name)).intValue();
						if((cv/(double)temp_item_state.support)>=BASIC_CONFIDENCE)
						{
							//result_bw.write(class_name+":"+temp_item_state.class_count.get(class_name)+",");
							feature_string+=class_name+":"+temp_item_state.class_count.get(class_name)+",";
						}
						else
							remove_set.add(class_name);
					}
					//result_bw.write("]\n");
					feature_string+="]\n";
					for(int i=0;i<remove_set.size();i++)
					{
						temp_item_state.class_count.remove(remove_set.get(i));
					}
					if(temp_item_state.class_count.size()!=0)
						result_bw.write(feature_string);
				}
				else
				{
					remove_item_set.add(temp_item_name);
				}
				//System.out.println(FREQUENT_ITEM.get(i));
			}
			for(int i=0;i<remove_item_set.size();i++)
				FREQUENT_ITEM.remove(remove_item_set.get(i));
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("sequential pattern start exception:"+e);
		}		
	}
	
	public pp_tree_node build_large_1()
	{
		pp_tree_node result=new pp_tree_node();
		try
		{
			Iterator ir=FREQUENT_ITEM.keySet().iterator();
			while(ir.hasNext())
			{
				String temp_item_name=(String)ir.next();
				item_state temp_item_state=FREQUENT_ITEM.get(temp_item_name);
				//result.path_build(temp_item_name,temp_item_state.class_count);
				result.path_build_with_class_count(temp_item_name,temp_item_state.class_count);
			}
			//for(int i=0;i<FREQUENT_ITEM.size();i++)
			//	result.path_build((String)FREQUENT_ITEM.get(i));
		}
		catch(Exception e)
		{
			System.out.println("progressive_pattern build_large_1 exception:"+e);
		}
		return result;
	}
	
	public boolean build_candidate_large_n(pp_tree_node sub_root)
	{
		boolean result=false;
		try
		{
			//System.out.println(sub_root.CHILDREN.size());
			if(ITEM_REPEAT)
				result=sub_root.extend_all_leaves();
			else
				result=sub_root.extend_all_leaves_without_repeat();
			sub_root.clear_all_count();
		}
		catch(Exception e)
		{
			System.out.println("progressive_pattern build_candidate_large_n exception:"+e);
		}
		return result;
	}
	
	public boolean build_large_n(pp_tree_node sub_root)
	{
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(DATASET_FILENAME));
			String buffer="";
			int transaction_count=0;
			while(true)
			{
				buffer=br.readLine();
				if(buffer==null)
					break;
				transaction temp_transaction=transaction_parser.class_sequence_parser(buffer);
				sub_root.pattern_trace_for_counting(temp_transaction.ITEM_SET,0,transaction_count,temp_transaction.CLASS_LABEL);
				//sub_root.pattern_trace((transaction_parser.class_sequence_parser(buffer)).ITEM_SET,0);
				transaction_count++;
			}
			br.close();			
		}
		catch(Exception e)
		{
			System.out.println("progressive_pattern build_large_n exception:"+e);
		}
		//if prune=false, it means no large itemset.
		return prune(sub_root);
	}
	
	public void output_large_n(pp_tree_node sub_root)
	{
		try
		{
			output_large_n(sub_root,"");
		}
		catch(Exception e)
		{
			System.out.println("progressive_pattern output_large_n exception:"+e);
		}
	}
	
	private void output_large_n(pp_tree_node sub_root,String seq)
	{
		try
		{
			if(!sub_root.CONTENT.equals(""))
				seq+=sub_root.CONTENT+",";
			if(sub_root.is_leaf())
			{
				result_bw.write(seq+":"+sub_root.COUNT);
				Iterator ir=sub_root.SCORE_TABLE.keySet().iterator();
				result_bw.write("[");
				while(ir.hasNext())
				{
					String temp_class_name=(String)ir.next();
					result_bw.write(temp_class_name+":"+sub_root.SCORE_TABLE.get(temp_class_name)+",");
				}
				result_bw.write("]");
				result_bw.write("\n");
				//System.out.println(seq);
			}
			else
			{
				Iterator ir=sub_root.CHILDREN.keySet().iterator();
				while(ir.hasNext())
				{					
					output_large_n((pp_tree_node)sub_root.CHILDREN.get((String)ir.next()),seq);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("progressive_pattern output_large_n exception:"+e);
		}
	}

/*	public double get_gainratio(HashMap<String,Double> t)
	{
		try
		{

		}
		catch(Exception e)
		{
			System.out.println("progressive_pattern get_gainratio exception:"+e);
		}
	}
*/	
	//remove "false" node
	public boolean prune(pp_tree_node sub_root)
	{
		try
		{
			if(sub_root.is_leaf())
			{
				if(sub_root.COUNT>=SUPPORT_COUNT)
				{
					HashMap<String,Double> parent_confidence=sub_root.PARENT.SCORE_TABLE;
					//if we use progressive gainratio
//					if(get_gainratio(sub_root.SCORE_TABLE,sub_root.COUNT)>=get_gainratio(sub_root.PARENT.SCORE_TABLE,sub_root.PARENT.COUNT))
//						return true;
					//if we use progressive confidence
					Vector<String> remove_list=new Vector<String>();
					Iterator ir=sub_root.SCORE_TABLE.keySet().iterator();
					while(ir.hasNext())
					{
						String class_name=(String)ir.next();

						double this_v=((Double)sub_root.SCORE_TABLE.get(class_name)).doubleValue()/(double)sub_root.COUNT;
						if(parent_confidence.containsKey(class_name))
						{
							double pre_v=((Double)parent_confidence.get(class_name)).doubleValue()/(double)sub_root.PARENT.COUNT;
							if(this_v<=pre_v)
								remove_list.add(class_name);
						}
						else
							remove_list.add(class_name);
					}
//					if(sub_root.PARENT.CONTENT.equals("24"))
//							System.out.println(remove_list);			
					for(int i=0;i<remove_list.size();i++)
						sub_root.SCORE_TABLE.remove(remove_list.get(i));
					if(sub_root.SCORE_TABLE.size()!=0)
						return true;
				}
				return false;
			}
			else
			{
				boolean result=false;
				Iterator ir=sub_root.CHILDREN.keySet().iterator();
				Vector<String> remove_list=new Vector<String>();
				while(ir.hasNext())
				{
					String temp=(String)ir.next();
					boolean temp_b=prune((pp_tree_node)sub_root.CHILDREN.get(temp));
					if(!temp_b)
						remove_list.add(temp);
					result=result||temp_b;
				}
				for(int i=0;i<remove_list.size();i++)
					sub_root.CHILDREN.remove((String)remove_list.get(i));
				//System.out.println(result);
				return result;
			}
		}
		catch(Exception e)
		{
			System.out.println("progressive_pattern prune exception:"+e);
		}
		return false;
	}

	private double get_gainratio(HashMap cc,int c)
	{
		double result=0.0;
		try
		{
			double all_count=(double)c;
			double have_value=0.0;
			double no_value=0.0;

			double have_p=all_count/(double)TRANSACTION_COUNT;
			double no_p=(TRANSACTION_COUNT-all_count)/(double)TRANSACTION_COUNT;

			Iterator ir=cc.keySet().iterator();
			while(ir.hasNext())
			{
				String class_name=(String)ir.next();
				double a1=((Double)cc.get(class_name)).doubleValue()/all_count;
				double a2=((CLASS_COUNT.get(class_name)).intValue()-((Double)cc.get(class_name)).doubleValue())/(double)TRANSACTION_COUNT;
				have_value+=a1*Math.log(a1);
				no_value+=a2*Math.log(a2);
			}
			result=1-(have_p*(-1*have_value)+no_p*(-1*no_value));
			result=result/(-1*(have_p*Math.log(have_p)+no_p*Math.log(no_p)));
		}
		catch(Exception e)
		{
			System.out.println("progressive_pattern get_gainratio exception:"+e);
		}
		return result;
	}
	
	public void progressive_pattern_mining()
	{
		try
		{			
			pp_tree_node temp_root=build_large_1();
			if(temp_root.CHILDREN.size()==0)
			{
				result_bw.close();
				return;
			}
			int level=2;
			while(true)
			{
				if( !build_candidate_large_n(temp_root) )
					break;
					
				result_bw.write("#Candidate "+level+"\n");
				//System.out.println("#Candidate "+level);
				
				output_large_n(temp_root); //show candidates				
				if( ! build_large_n(temp_root) )
					break;
					
				result_bw.write("#Large "+level+"\n");
				System.out.println("#Large "+level);
				output_large_n(temp_root);
				level++;
			}
			
			result_bw.close();
		}	
		catch(Exception e)
		{
			System.out.println("progressive_pattern progressive_pattern_mining exception:"+e);
		}
	}
	
	public void save_result_to_file(String fn)
	{
		try
		{
			if((new File(result_filename)).isFile())
			{
				BufferedReader br=new BufferedReader(new FileReader(result_filename));
				BufferedWriter bw=new BufferedWriter(new FileWriter(fn));
				String buffer="";
				while((buffer=br.readLine())!=null)
				{
					bw.write(buffer+"\n");
				}
				bw.close();
				br.close();
				
			}
		}
		catch(Exception e)
		{
			System.out.println("progressive_pattern save_result_to_file exception:"+e);
		}
	}
	
	public static void main(String args[])
	{
		progressive_pattern a=new progressive_pattern(args[0],Double.parseDouble(args[1]),Double.parseDouble(args[2]),Double.parseDouble(args[3]));
		a.start();
	}
}
