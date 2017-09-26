package progressive_pattern;
import java.io.*;
import java.util.Vector;
import java.util.HashMap;
import java.util.Iterator;

public class VPS
{
	String PROGRESSIVE_PATTERN_FILENAME="";
	HashMap<String,VP_node> all_node=new HashMap<String,VP_node>();
	HashMap<String,Double> all_score_table=new HashMap<String,Double>();
	public VPS(String integrated_pattern_file)
	{
		PROGRESSIVE_PATTERN_FILENAME=integrated_pattern_file;
		load_patterns();
	}

	//load each pattern as one VP_node
	public void load_patterns()
	{
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(PROGRESSIVE_PATTERN_FILENAME));
			String buffer="";
			while((buffer=br.readLine())!=null)
			{
				VP_node a=new VP_node(buffer);
				all_node.put(a.ID,a);
			//	score_add(all_score_table,a.SCORE_TABLE);
			}
			br.close();
			//System.out.println(all_score_table);
		}
		catch(Exception e)
		{
			System.out.println("VPS load_patterns exception:"+e);
		}
	}

	//output file
/*	public String generate_multivariate_progressive_pattern(String TRAINING_FILENAME,String TESTING_FILENAME)
	{
		String result="";
		try
		{
			result=(File.createTempFile("MPPF",".tmp",new File("tmp"))).getAbsolutePath();
			VP_node root=build_arge_1();
			int level=2;
			while(true)
			{
				if(!build_candidate_n(root))
					break;
				output_candidate(level,root,result);
				if()
				
			}
			join_feature_with_the_same_class_set();
		}
		catch(Exception e)
		{
			System.out.println("VPD generate_multivariate_progressive_pattern exception:"+e);
		}
		return result;
	}
*/

	//Absolutely Match with brute force
	public double check_file(String input_file)
	{
		double result=0.0;
		try
		{
			double count=0.0;
			double correct=0.0;
			BufferedReader br=new BufferedReader(new FileReader(input_file));
			String buffer="";
			int g=0;
			while((buffer=br.readLine())!=null)
			{
				String temp_class=buffer.substring(0,buffer.indexOf(":"));
				String temp_sequence=buffer.substring(buffer.indexOf(":")+1);
				String each_item[]=temp_sequence.split(",");
				HashMap<String,Double> temp_score=new HashMap<String,Double>();

				double highest_conf=-1.0;
				String highest_class="";

				for(int i=0;i<each_item.length;i++)
				{
					String temp_item=each_item[i].substring(0,each_item[i].indexOf("("));

					if(g++==0 && i==0)
						System.out.println(temp_item+"::::"+all_node.get(temp_item));

					if(all_node.get(temp_item)!=null)
					{
						VP_node temp_node=all_node.get(temp_item);
						//if(temp_node.ITEM_SET.size()==1)
						//	continue;
						String sp_class=get_highest_class(temp_node.SCORE_TABLE);
						double sp_score=(temp_node.SCORE_TABLE.get(sp_class)).doubleValue();
						if(highest_conf<0 || highest_conf<sp_score)
							highest_class=sp_class;

						//if(temp_node.ITEM_SET.size()!=1)
							score_add(temp_score,temp_node.SCORE_TABLE);
					}
				}
				String predicted_class=get_highest_class(temp_score);
				//String predicted_class=highest_class;
				System.out.println(temp_score+"R/P:"+temp_class+"/"+predicted_class);
				if(temp_class.equals(predicted_class))
				{
					correct+=1.0;
					System.out.println("===================================="+correct);
				}
				count+=1.0;
			}
			br.close();
			result=correct/count;
		}
		catch(Exception e)
		{
			System.out.println("VPS check_file exception:"+e);
		}
		return result;
	}

	public String get_highest_class(HashMap<String,Double> s)
	{
		String result="";
		try
		{
			Iterator ir=s.keySet().iterator();
			double highest_score=-1.0;
			while(ir.hasNext())
			{
				String temp_class=(String)ir.next();
				double temp_score=(s.get(temp_class)).doubleValue();
				if(all_score_table.get(temp_class)!=null)
					temp_score=temp_score/(all_score_table.get(temp_class)).doubleValue();
				if(highest_score<0 || temp_score>highest_score)
				{
					result=temp_class;
					highest_score=temp_score;
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("VPS get_highest_class exception:"+e);
		}
		return result;
	}

	public void score_add(HashMap<String,Double> target,HashMap<String,Double> newone)
	{
		try
		{
			Iterator ir=newone.keySet().iterator();
			while(ir.hasNext())
			{
				String temp_class=(String)ir.next();
				double temp_score=(newone.get(temp_class)).doubleValue();
				if(target.get(temp_class)!=null)
				{
					temp_score+=(target.get(temp_class)).doubleValue();
				}
				target.put(temp_class, new Double(temp_score));

			}
		}
		catch(Exception e)
		{
			System.out.println("VPS check_file exception:"+e);
		}

	}

	//I want to know which nodes can be link as a candidate.
	public String get_all_possible_pairs()
	{
		String result="";
		try
		{
			result=(File.createTempFile("pp_can",".tmp",new File("tmp"))).getAbsolutePath();
			Vector<String> vp_name_set=new Vector<String>();
			Iterator ir=all_node.keySet().iterator();
			while(ir.hasNext())
			{
				vp_name_set.add((String)ir.next());
			}
			BufferedWriter bw=new BufferedWriter(new FileWriter(result));
			for(int i=0;i<vp_name_set.size();i++)
			{
				VP_node i_node=all_node.get(vp_name_set.get(i));
				for(int j=i+1;j<all_node.size();j++)
				{
					VP_node j_node=all_node.get(vp_name_set.get(j));
					if(i_node.is_possible_pair(j_node))
					{
						bw.write(i_node.ID+","+j_node.ID+"\n");
					}
				}
			}
			bw.close();
		}
		catch(Exception e)
		{
			System.out.println("VPS get_all_possible_pairs exception:"+e);
		}
		return result;
	}

	public static void main(String args[])
	{
		//input single time series progressive_pattern as features.
		VPS a=new VPS(args[0]);
		//Input file for check accuracy
		for(int i=1;i<args.length;i++)
		{
			System.out.println(args[i]+" result:");
			System.out.println(a.check_file(args[i]));
			System.out.println("all_possible_pair:"+a.get_all_possible_pairs());
		}
	}
}

