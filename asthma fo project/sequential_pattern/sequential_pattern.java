package sequential_pattern;
import java.io.*;
import java.util.Vector;
import java.util.HashMap;
import java.util.Iterator;

import data_format.*;

public class sequential_pattern
{
	public String DATASET_FILENAME=""; //filename
	public Vector<sp_tree_node> ROOT_SET;
	public Vector<String> FREQUENT_ITEMSET;
	public int TRANSACTION_COUNT;
	public double SUPPORT;
	public int SUPPORT_COUNT;
	public String result_filename="";
	private BufferedWriter result_bw;
	
	public boolean ITEM_REPEAT=true;
	
	public sequential_pattern(String f,double s)
	{
		DATASET_FILENAME=f;
		TRANSACTION_COUNT=0;
		SUPPORT=s;
		SUPPORT_COUNT=0;
		ROOT_SET=new Vector<sp_tree_node>();
		FREQUENT_ITEMSET=new Vector<String>();
	}
		
	public void start()
	{		
		create_result_file();
		preprocess();		
		sequential_pattern_mining();
	}
	
	private void create_result_file()
	{
		try
		{
			File result_file=File.createTempFile("sp_temp",".txt");
			result_file.deleteOnExit();
			result_filename=result_file.getAbsolutePath();
			result_bw=new BufferedWriter(new FileWriter(result_file));
		}
		catch(Exception e)
		{
			System.out.println("sequential_pattern create_result_file exception:"+e);
		}
	}
	
	public void preprocess()
	{
		try
		{
			HashMap<String,Integer> f_itemset=new HashMap<String,Integer>();
			BufferedReader br=new BufferedReader(new FileReader(DATASET_FILENAME));
			
			Vector<Integer> freq_count=new Vector<Integer>();
			String buffer="";
			TRANSACTION_COUNT=0;
			while(true)
			{
				buffer=br.readLine();
				if(buffer==null)
					break;
				transaction temp=transaction_parser.class_sequence_parser(buffer);
				Vector<String> temp_item_set=temp.ITEM_SET;
				/*for(int i=0;i<temp_item_set.size();i++)
				{
					if(f_itemset.containsKey((String)temp_item_set.get(i)))
					{
						int temp_c=((Integer)f_itemset.get((String)temp_item_set.get(i))).intValue();
						f_itemset.put((String)temp_item_set.get(i),new Integer(temp_c+1));
					}
					else
						f_itemset.put((String)temp_item_set.get(i),new Integer(1));
				}*/
				for(int i=0;i<temp_item_set.size();i++)
				{
					if(!f_itemset.containsKey(temp_item_set.get(i)))
						f_itemset.put((String)temp_item_set.get(i), new Integer(0));
				}
				Iterator ir=f_itemset.keySet().iterator();
				while(ir.hasNext())
				{
					String temp_item=(String)ir.next();
					if(temp_item_set.contains(temp_item))
					{
						int temp_c=((Integer)f_itemset.get(temp_item)).intValue();
						f_itemset.put(temp_item,new Integer(temp_c+1));
					}
				}
				TRANSACTION_COUNT++;
			}
			//System.out.println(TRANSACTION_COUNT);
			
			SUPPORT_COUNT=(int)(TRANSACTION_COUNT*SUPPORT);
			result_bw.write("#Transaction Count:"+TRANSACTION_COUNT+"\n");
			result_bw.write("#SUPPORT:"+SUPPORT_COUNT+"\n");
			//System.out.println("SUPPORT:"+SUPPORT_COUNT);	//for debug
			
			Iterator ir=f_itemset.keySet().iterator();
			while(ir.hasNext())
			{
				String temp_item=(String)ir.next();
				if( (Integer)(f_itemset.get(temp_item)).intValue() >= SUPPORT_COUNT && SUPPORT_COUNT!=0)
				{
					freq_count.add((Integer)(f_itemset.get(temp_item)));
					FREQUENT_ITEMSET.add(temp_item);
				}
			}
			
			result_bw.write("#Large 1\n");
			//System.out.println("#Large 1:"+FREQUENT_ITEMSET.size());	//for debug
			for(int i=0;i<FREQUENT_ITEMSET.size();i++)
			{
				result_bw.write(FREQUENT_ITEMSET.get(i)+":"+freq_count.get(i)+"\n");
				//System.out.println(FREQUENT_ITEMSET.get(i));	//for debug
			}
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("sequential pattern start exception:"+e);
		}		
	}
	
	public sp_tree_node build_large_1()
	{
		sp_tree_node result=new sp_tree_node();
		try
		{
			for(int i=0;i<FREQUENT_ITEMSET.size();i++)
				result.path_build((String)FREQUENT_ITEMSET.get(i));
		}
		catch(Exception e)
		{
			System.out.println("sequential_pattern build_large_1 exception:"+e);
		}
		return result;
	}
	
	public boolean build_candidate_large_n(sp_tree_node sub_root)
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
			System.out.println("sequential_pattern build_candidate_large_n exception:"+e);
		}
		return result;
	}
	
	public boolean build_large_n(sp_tree_node sub_root)
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
				sub_root.pattern_trace_for_counting((transaction_parser.class_sequence_parser(buffer)).ITEM_SET,0,transaction_count);
				//sub_root.pattern_trace((transaction_parser.class_sequence_parser(buffer)).ITEM_SET,0);
				transaction_count++;
			}
			br.close();			
		}
		catch(Exception e)
		{
			System.out.println("sequential_pattern build_large_n exception:"+e);
		}
		//if prune=false, it means no large itemset.
		return prune(sub_root);
	}
	
	public void output_large_n(sp_tree_node sub_root)
	{
		try
		{
			output_large_n(sub_root,"");
		}
		catch(Exception e)
		{
			System.out.println("sequential_pattern output_large_n exception:"+e);
		}
	}
	
	private void output_large_n(sp_tree_node sub_root,String seq)
	{
		try
		{
			if(!sub_root.CONTENT.equals(""))
				seq+=sub_root.CONTENT+",";
			if(sub_root.is_leaf())
			{
				result_bw.write(seq+":"+sub_root.COUNT+"\n");
				//System.out.println(seq);
			}
			else
			{
				Iterator ir=sub_root.CHILDREN.keySet().iterator();
				while(ir.hasNext())
				{					
					output_large_n((sp_tree_node)sub_root.CHILDREN.get((String)ir.next()),seq);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("sequential_pattern output_large_n exception:"+e);
		}
	}
	
	public boolean prune(sp_tree_node sub_root)
	{
		try
		{
			if(sub_root.is_leaf())
			{
				if(sub_root.COUNT>=SUPPORT_COUNT && SUPPORT_COUNT!=0)
					return true;
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
					boolean temp_b=prune((sp_tree_node)sub_root.CHILDREN.get(temp));
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
			System.out.println("sequential_pattern prune exception:"+e);
		}
		return false;
	}
	
	public void sequential_pattern_mining()
	{
		try
		{			
			sp_tree_node temp_root=build_large_1();
			if(temp_root.CHILDREN.size()==0)
			{
				result_bw.close();
				return;
			}
			int level=2;
			while(true)
			{
				//System.out.println("level"+level+"===============");
				if( !build_candidate_large_n(temp_root) )
					break;
					
				result_bw.write("#Candidate "+level+"\n");
				//System.out.println("#Candidate "+level);
				
				output_large_n(temp_root); //show candidates				
				if( ! build_large_n(temp_root) )
					break;
					
				result_bw.write("#Large "+level+"\n");
				//System.out.println("#Large "+level);
				
				output_large_n(temp_root);				
				level++;
			}
			
			result_bw.close();
		}	
		catch(Exception e)
		{
			System.out.println("sequential_pattern sequential_pattern_mining exception:"+e);
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
			System.out.println("sequential_pattern save_result_to_file exception:"+e);
		}
	}
	
	public static void main(String args[])
	{
		sequential_pattern a=new sequential_pattern(args[0],Double.parseDouble(args[1]));
		a.start();
	}
}
