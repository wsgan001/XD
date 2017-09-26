package sequential_pattern;
import java.io.*;
import data_format.*;
import sequential_pattern.*;

import java.util.Iterator;
import java.util.Vector;
import java.util.HashMap;

//must input the sequential pattern mined separately by class label
//each sequential pattern must followed by its support
public class feature_mine
{
	String FILENAME="";
	int LEN_LIMIT=10;
	double SUPPORT=0.0;
	sp_tree_node my_root;
	public String result_filename="";
	public feature_mine(String f,double sup,int len)
	{
		FILENAME=f;
		SUPPORT=sup;
		LEN_LIMIT=len;
		result_filename=f+"_fm_result";
		if((new File(result_filename)).isFile())
			(new File(result_filename)).delete();
	}
	
	public void action()
	{
		try
		{
			HashMap<String,String> all_class_file=sep_data();
			Iterator ir=all_class_file.keySet().iterator();
			while(ir.hasNext())
			{
				String temp_cl=(String)ir.next();
				sequential_pattern my_sp_mining=new sequential_pattern((String)all_class_file.get(temp_cl),SUPPORT);
				my_sp_mining.ITEM_REPEAT=true;
				my_sp_mining.start();
				append_to_result(temp_cl,my_sp_mining.result_filename);
			}
			prune_sp();
		}
		catch(Exception e)
		{
			System.out.println("feature mine action exception:"+e);
		}
	}

	private void prune_sp()
	{
		try
		{
			//find out the patterns just appear in one class data
			//And this pattern cannot be cover by others
			//We collect all must-remove patterns subsumed by these exlite patterns
			String elite_pattern_file=extract_elite_pattern();
			//remove all pattern in result file which is recorded in remove_pattern_file
			prune_specific_patterns_with(elite_pattern_file);			
		}
		catch(Exception e)
		{
			System.out.println("feature_mine prune_sp exception:"+e);
		}
		
	}

	private void prune_specific_patterns_with(String epf)
	{
		long start_time=System.currentTimeMillis();
		try
		{
			System.out.println("Prune by elite pattern file:"+epf);
			BufferedReader br=new BufferedReader(new FileReader(epf));
			String buffer="";
			sp_tree_node root=new sp_tree_node();
			//System.out.println("100% accuracy sequence:");
			int prune_count=0;
			int temp_sn=0;
			while((buffer=br.readLine())!=null)
			{
				//System.out.println(buffer);			
				transaction temp_t=transaction_parser.auto_parser(buffer);
				root.path_build_with_sn(temp_t.ITEM_SET, temp_sn++);
			}
			br.close();
			br=new BufferedReader(new FileReader(result_filename));
			BufferedWriter bw=new BufferedWriter(new FileWriter("temptemp"));			
			boolean read_flg=false;
			boolean write_flg=false;
			while((buffer=br.readLine())!=null)
			{
				if(buffer.indexOf("#")==0)
				{
    				if(buffer.indexOf("#Large")==0)
    					{
    						read_flg=true;
    						write_flg=false;
    					}
    				else if(buffer.indexOf("#Candidate")==0)
    					{
	    					read_flg=false;
    						write_flg=false;
    					}
    				//else if(buffer.indexOf("#CLASS LABEL:")==0)
    				bw.write(buffer+"\n");
				}
				else
				{
					if(read_flg)
					{
						transaction temp_t=transaction_parser.auto_parser(buffer);
						//Vector<Integer> temp_sn_set=new Vector<Integer>();
						//root.pattern_trace_for_sn(temp_t.ITEM_SET,0,temp_sn_set);
						if(root.pattern_trace_for_sn_without_just(temp_t.ITEM_SET, 0))
						//if(temp_sn_set.size()>=0)
						{
							write_flg=false;
							prune_count++;
						}
						else
							write_flg=true;
					}
				}
				if(write_flg)
				{
					transaction temp_tran=transaction_parser.basic_parser(buffer, "SS");
					if(temp_tran.ITEM_SET.size()<=LEN_LIMIT)						
						bw.write(buffer+"\n");
				}
			}
			br.close();
			bw.close();
			
			System.out.println("Prune rule count"+prune_count);
			
			(new File(result_filename)).delete();
			(new File("temptemp")).renameTo(new File(result_filename));
		}
		catch(Exception e)
		{
			System.out.println("feature_mine prune_specific_patterns_with exception:"+e);
		}
		long end_time=System.currentTimeMillis();		
		System.out.println("prune sequence execution time:"+(end_time-start_time));		
	}

	private String extract_elite_pattern()
	{
		String r="";
		long start_time=System.currentTimeMillis();
		try
		{
			r=File.createTempFile("cpx", ".ptn").getName();
			r="tmp/"+r;
			
			BufferedReader br=new BufferedReader(new FileReader(result_filename));
			String buffer="";
			//String temp_cl="";
			boolean read_flg=false;
			Vector<String> class_pattern_set=new Vector<String>();
			Vector<String> repeat_pattern_set=new Vector<String>();
			while((buffer=br.readLine())!=null)
			{
				//if(buffer.indexOf("#CLASS LABEL")==0)
					//temp_cl=buffer.substring(buffer.indexOf(":")+1);
				//else if(buffer.indexOf("#Large")==0)
				if(buffer.indexOf("#")==0)
				{
    				if(buffer.indexOf("#Large")==0)
    					read_flg=true;
    				else if(buffer.indexOf("#Candidate")==0)
    					read_flg=false;
				}
				else
				{
					if(read_flg==true)
					{
						String temp_pattern=buffer.substring(0,buffer.indexOf(":"));
						if(repeat_pattern_set.contains(temp_pattern))
						{
							//Yes, I have this repeat pattern. It must be deleted!
						}
						else
						{
							if(class_pattern_set.contains(temp_pattern))
							{
								class_pattern_set.remove(temp_pattern);
								repeat_pattern_set.add(temp_pattern);
							}
							else
								class_pattern_set.add(temp_pattern);
						}
					}
				}				
			}
			br.close();
			remove_super_seq(class_pattern_set);
			select_elite_one(class_pattern_set);
			BufferedWriter bw=new BufferedWriter(new FileWriter(r));
			for(int i=0;i<class_pattern_set.size();i++)
			{
			    bw.write(class_pattern_set.get(i)+"\n");
			}
			bw.close();
		}
		catch(Exception e)
		{
			System.out.println("feature_mine extract_class_pattern exception:"+e);
		}		
		long end_time=System.currentTimeMillis();
		System.out.println("Find prune sequence:"+(end_time-start_time));
		return r;
	}

	private void select_elite_one(Vector<String> class_pattern_set)
	{
		try
		{
			sp_tree_node temp_root=new sp_tree_node();
			for(int i=0;i<class_pattern_set.size();i++)
			{
				transaction temp_tran=transaction_parser.auto_parser((String)class_pattern_set.get(i));
				temp_root.path_build_with_sn(temp_tran.ITEM_SET, i);
			}
			BufferedReader br=new BufferedReader(new FileReader(FILENAME));
			String buffer="";
			HashMap<Integer,Vector<String>> sn_cn=new HashMap<Integer,Vector<String>>();
			while((buffer=br.readLine())!=null)
			{
				transaction temp_tran=transaction_parser.auto_parser(buffer);
				Vector<Integer> x=new Vector<Integer>();
				temp_root.pattern_trace_for_sn(temp_tran.ITEM_SET, 0, x);
				for(int i=0;i<x.size();i++)
				{
					if(sn_cn.get((Integer)x.get(i))==null)
					{
						Vector<String> nv=new Vector<String>();
						nv.add(temp_tran.CLASS_LABEL);
						sn_cn.put((Integer)x.get(i),nv);
					}
					else
					{
						Vector<String> nv=(Vector<String>)sn_cn.get((Integer)x.get(i));
						if(!nv.contains(temp_tran.CLASS_LABEL))
							nv.add(temp_tran.CLASS_LABEL);
					}
				}
			}
			br.close();
			
			Vector<String> csp_clone=new Vector<String>();
			for(int i=0;i<class_pattern_set.size();i++)
			{
				if(sn_cn.get(new Integer(i))!=null)
				{
					Vector<String> k=sn_cn.get(new Integer(i));
					if(k.size()==1)
						csp_clone.add((String)class_pattern_set.get(i));
				}
			}
			System.out.println(class_pattern_set.size()+"~~~~~~~~~~~"+csp_clone.size());
			class_pattern_set.clear();
			class_pattern_set.addAll(csp_clone);
		}
		catch(Exception e)
		{
			System.out.println("feature mine select_elite_one exception:"+e);
		}
	}

	private Vector<String> get_remove_seq(Vector<String> cps)
	{
		long start_time=System.currentTimeMillis();
		Vector<String> result=new Vector<String>();
		try
		{
//			Version 1==========================
			sp_tree_node root=new sp_tree_node();
			for(int i=0;i<cps.size();i++)
			{
				transaction temp_t=transaction_parser.auto_parser((String)cps.get(i));
				root.path_build_with_sn(temp_t.ITEM_SET, i);
			}
			for(int i=0;i<cps.size();i++)
			{
				transaction temp_t=transaction_parser.auto_parser((String)cps.get(i));
				Vector<Integer> r=new Vector<Integer>();
				root.pattern_trace_for_sn(temp_t.ITEM_SET, 0, r);
				if(r.size()>1)
					result.add((String)cps.get(i));
			}

		}
		catch(Exception e)
		{
			System.out.println("feature_mine remove_subseq exception:"+e);
		}
		System.out.println("get_remove_seq:"+(System.currentTimeMillis()-start_time));
		return result;
	}

	private void remove_super_seq(Vector<String> cps)
	{
		long start_time=System.currentTimeMillis();
		try
		{
//			Version 1==========================
			sp_tree_node root=new sp_tree_node();
			for(int i=0;i<cps.size();i++)
			{
				transaction temp_t=transaction_parser.auto_parser((String)cps.get(i));
				root.path_build_with_sn(temp_t.ITEM_SET, i);
			}
			for(int i=0;i<cps.size();i++)
			{
				transaction temp_t=transaction_parser.auto_parser((String)cps.get(i));
				Vector<Integer> r=new Vector<Integer>();
				root.pattern_trace_for_sn(temp_t.ITEM_SET, 0, r);
				if(r.size()>1)
				{
					cps.remove(i);
					i--;
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("feature_mine remove_super_seq exception:"+e);
		}
		System.out.println("remove_super_seq:"+(System.currentTimeMillis()-start_time));
	}
	
	private boolean sub_seq(String s1, String s2)
	{
		try
		{
			if(s1.indexOf(",")<0)
				s1=s1+",";
			if(s2.indexOf(",")<0)
				s2=s2+",";
			Vector<String> sv1=transaction_parser.auto_parser(s1).ITEM_SET;
			Vector<String> sv2=transaction_parser.auto_parser(s2).ITEM_SET;
			if(sv1.size()>sv2.size())
				return false;
			int sv1_index=0;			
			for(int i=0;i<sv2.size();i++)
			{
				if((sv2.get(i)).equals(sv1.get(sv1_index)))
					sv1_index++;
				if(sv1_index>=sv1.size())
					return true;
			}
		}
		catch(Exception e)
		{
			System.out.println("feature_mine sub_seq exception:"+e);
		}
		return false;
	}

	private void append_to_result(String cl,String f)
	{
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(f));
			BufferedWriter bw=new BufferedWriter(new FileWriter(result_filename,true));
			bw.write("#CLASS LABEL:"+cl+"\n");
			String buffer="";
			while((buffer=br.readLine())!=null)
			{
				bw.write(buffer+"\n");
                        }
			br.close();
			bw.close();
                }
		catch(Exception e)
                {
			System.out.println("feature mine append_to_result exception:"+e);
                }
        }
	
	private HashMap<String,String> sep_data()
	{
		HashMap<String,String> result=new HashMap<String,String>();
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(FILENAME));
			String buffer="";
			HashMap<String,BufferedWriter> clset=new HashMap<String,BufferedWriter>();
			while((buffer=br.readLine())!=null)
			{
				transaction temp_tran=transaction_parser.auto_parser(buffer);
				String temp_cl=temp_tran.CLASS_LABEL;
				if(clset.get(temp_cl)!=null)
				{
					BufferedWriter temp_bw=(BufferedWriter)clset.get(temp_cl);
					temp_bw.write(buffer+"\n");
				}
				else
				{
					String temp_filename=File.createTempFile("cf_"+temp_cl,".temp").getName();
					temp_filename="tmp/"+temp_filename;
					System.out.println("class "+temp_cl+":"+temp_filename);
					result.put(temp_cl,temp_filename);
					BufferedWriter temp_bw=new BufferedWriter(new FileWriter(temp_filename));
					clset.put(temp_cl,temp_bw);
					temp_bw.write(buffer+"\n");
				}
			}
			Iterator ir=clset.keySet().iterator();
			while(ir.hasNext())
			{
				BufferedWriter temp_bw=(BufferedWriter)clset.get(ir.next());
				temp_bw.close();
			}
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("feature_mine sep_data"+e);
		}
		return result;
	}

	//for test
	public static void main(String args[])
	{
		long start_time=System.currentTimeMillis();
		System.out.println("feature_mine filename support length_limit");
		feature_mine a=new feature_mine(args[0],Double.parseDouble(args[1]),Integer.parseInt(args[2]));
		a.action();
		System.out.println("FeatureMine result file:"+a.result_filename);
		System.out.println("FeatureMine execution time:"+(System.currentTimeMillis()-start_time));
	}
}
