package progressive_pattern;
import java.io.*;
import java.util.Vector;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

public class VP_node
{
	public String ID="";
	public Vector<String> ITEM_SET=new Vector<String>();
	public HashMap<String,Double> SCORE_TABLE=new HashMap<String,Double>();
	public boolean SCOREABLE=true; // If this variable is false , the score of this node is no use.
	public double SUPPORT=0;
	public int LAYER=0;
	public int LABEL=-1; //for counting
	public static boolean GAP_FLG=false;
	public static int GAP=1;
	public VP_node PARENT=null;
	public Vector<Integer> SN_set=new Vector<Integer>();
	public HashMap<String, VP_node> CHILDREN=new HashMap<String, VP_node>();

	//Different from sp_tree_node. In this node, the information of a progressive sequence must be recorded
	//But the progressive pattern is represented by a label.
	//Use this node can generate multivariate progressive pattern cross different variables.
	//
	//Basic information of one VP_node: ID, ITEM_SET(optional), SUPPORT, SCORE_TABLE, PARENT(optional), CHILDREN(optional)
	public VP_node(String info)
	{
		information_parsing(info);
	}
	
	//for root
	public VP_node()
	{
	}

	private void information_parsing(String buffer)
	{
		try
		{
			ID=buffer.substring(0,buffer.indexOf(":"));
			String seq_support=buffer.substring(buffer.indexOf(":")+1,buffer.indexOf("["));
			String seq=seq_support.substring(0,seq_support.lastIndexOf(":"));
			String items[]=seq.split(",");
			for(int i=0;i<items.length;i++)
			{
				if(items[i].equals(""))
					continue;
				ITEM_SET.add(items[i]);
			}
			String scores=buffer.substring(buffer.indexOf("[")+1,buffer.indexOf("]"));
			SUPPORT=Double.parseDouble(seq_support.substring(seq_support.lastIndexOf(":")+1));
			String class_score[]=scores.split(",");
			for(int i=0;i<class_score.length;i++)
			{
				if(class_score[i].equals(""))
					continue;
				String temp_class=class_score[i].substring(0,class_score[i].indexOf(":"));
				String temp_score=class_score[i].substring(class_score[i].indexOf(":")+1);
				SCORE_TABLE.put(temp_class,Double.parseDouble(temp_score)/SUPPORT);
			}
		}
		catch(Exception e)
		{
			System.out.println("VP_node information_parsing exception:"+e);
		}
	}

	public VP_node(String a, VP_node p)
	{
		if(a.indexOf(":")>0)
			information_parsing(a);
		else
			ID=a;
		this.LAYER=p.LAYER+1;
		PARENT=p;
	}

	public String toString()
	{
		String result="";
		try
		{
			result+=ID+":";
			for(int i=0;i<ITEM_SET.size();i++)
				result+=ITEM_SET.get(i)+",";
			result+=":"+Double.toString(SUPPORT);
			Iterator ir=SCORE_TABLE.keySet().iterator();
			result+="[";
			while(ir.hasNext())
			{
				String temp_class=(String)ir.next();
				result+=temp_class+":"+SCORE_TABLE.get(temp_class)+",";
			}
			result+="]";
		}
		catch(Exception e)
		{
			System.out.println("VP_node toString exception:"+e);
		}
		return result;
	}

	public void initial_for_prediction()
	{
		try
		{
			this.LABEL=-1;
			Iterator ir=CHILDREN.keySet().iterator();
			while(ir.hasNext())
			{
				VP_node temp_node=(VP_node)CHILDREN.get((String)ir.next());
				temp_node.initial_for_prediction();
			}
		}
		catch(Exception e)
		{
			System.out.println("VP_node initial_for_prediction exception:"+e);
		}
	}

	public boolean remove_redundancy(int cc)
	{
		// delete if result is true;
		boolean result=false;
	        try
		{
			if(SCORE_TABLE.size()>=cc)
			{
///                              result=true;
				SCOREABLE=false;
			}
			Iterator ir=CHILDREN.keySet().iterator();
//                      Vector<String> remove_set=new Vector<String>();
			while(ir.hasNext())
			{
				String temp_key=(String)ir.next();
				VP_node temp_node=(VP_node)CHILDREN.get(temp_key);
				boolean temp_r=temp_node.remove_redundancy(cc);
				result=result&&temp_r;
//                              if(temp_r)
//                                      remove_set.add(temp_key);
			}
//                      for(int i=0;i<remove_set.size();i++)
//                              CHILDREN.remove((String)remove_set.get(i));
		}
		catch(Exception e)
		{
			System.out.println("VP_node remove_redundancy exception:"+e);
		}
		return result;
	}

	private String find_max_key(HashMap<String,Double> c)
	{
		double max=0.0;
		String result="";
		try
		{
			Iterator ir=c.keySet().iterator();
			while(ir.hasNext())
			{
				String temp=(String)ir.next();
				if(((Double)c.get(temp)).doubleValue()>max)
				{
					max=((Double)c.get(temp)).doubleValue();
					result=temp;
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("VP_node find_max_key exception:"+e);
		}
		return result;
	}

	//Sometimes the sum of score of classes are not equals to the support
	//some class count maybe no recorded in.
	private double sum_of_score()
	{
		double result=0.0;
		try
		{
			Iterator ir=SCORE_TABLE.keySet().iterator();
			while(ir.hasNext())
			{
				result+=((Double)SCORE_TABLE.get((String)ir.next())).doubleValue();
			}
		}
		catch(Exception e)
		{
			System.out.println("VP_node sum_of_score exception:"+e);
		}
		return result;
	}

	private double get_confidence(String target_class)
	{
		double result=0.0;
		try
		{
			Iterator ir=SCORE_TABLE.keySet().iterator();
			while(ir.hasNext())
			{
				result+=((Double)SCORE_TABLE.get((String)ir.next())).doubleValue();
			}
			if(SUPPORT>result) //This means the not all count put in SCORE_TABLE.
				result=SUPPORT;
			result=((Double)SCORE_TABLE.get(target_class)).doubleValue()/result;
		}
		catch(Exception e)
		{
			System.out.println("VP_node get_confidence exception:"+e);
		}
		return result;
	}

	public double get_highest_confidence()
	{
		double result=0.0;
		double max_count=0;
		try
		{
			Iterator ir=SCORE_TABLE.keySet().iterator();
			while(ir.hasNext())
			{
				double temp_score=((Double)SCORE_TABLE.get((String)ir.next())).doubleValue();
				result+=temp_score;
				if(temp_score>max_count)
					max_count=temp_score;
			}
			if(SUPPORT>result) //This means the not all count put in SCORE_TABLE.
				result=SUPPORT;
			result=max_count/result;
		}
		catch(Exception e)
		{
			System.out.println("VP_node get_confidence exception:"+e);
		}
		return result;
	}

	public boolean path_build_with_support_and_class_score(Vector<String> a,int s,HashMap<String,Double> cs)
	{
		return path_build_with_support_and_class_score(a,(double)s,cs);
	}

	public boolean path_build_with_support_and_class_score(Vector<String> a,double s,HashMap<String,Double> cs)
	{
		try
		{
			if(a.size()==0)
			{
				SCORE_TABLE.putAll(cs);
				return true;
			}
			String temp=(String)a.remove(0);
			VP_node temp_node=null;
			if(!CHILDREN.containsKey(temp))
			{
				temp_node=new VP_node(temp,this);
				temp_node.LAYER=this.LAYER+1;
				CHILDREN.put(temp,temp_node);
			}
			else
				temp_node=(VP_node)CHILDREN.get(temp);
			return temp_node.path_build_with_support_and_class_score(a,s,cs);

		}
		catch(Exception e)
		{
			System.out.println("VP_node path_build_with class_score exception:"+e);
		}
		return false;
	}

	public boolean extend_all_leaves() // without repeat Ex. A->A->A->B , that will be A->B only.
	{
		boolean result=false;
		try
		{
			Iterator all_child=CHILDREN.keySet().iterator();
			boolean level2_flg=true;
			while(all_child.hasNext())
			{
				VP_node temp_node=(VP_node)CHILDREN.get(all_child.next());
				level2_flg=level2_flg&&temp_node.is_leaf();
			}
			if(level2_flg)
			{
				if(CHILDREN.size()==1)
					return false;
				Iterator m_child=CHILDREN.keySet().iterator();
				Vector<String> temp_set=new Vector<String>();
				while(m_child.hasNext())
					temp_set.add((String)m_child.next());
				Vector<String> remove_list=new Vector<String>();
				//System.out.println("[Debug] tree_node.size():"+temp_set.size());
				for(int i=0;i<temp_set.size();i++)
				{
					VP_node temp_node=(VP_node)CHILDREN.get(temp_set.get(i));
					for(int j=0;j<temp_set.size();j++)
					{
						if(i!=j) // NO REPEAT!!=======================
						{
							VP_node temp_node2=(VP_node)CHILDREN.get(temp_set.get(j));
							//if(temp_node.is_possible_pair(temp_node2))
							if(temp_node.is_class_match(temp_node2))
							{
								//If there is one node extended successfully, that is success!!
								//result=(temp_node.path_build_with_copy(temp_node2))||result;   //Dangerous writing type.
								boolean bl=temp_node.path_build_with_copy(temp_node2);
								result=result || bl;
							}
						}
					}
					if(temp_node.CHILDREN.size()==0)
						remove_list.add(temp_set.get(i));
				}
				//System.out.println("[Debug] remove_list.size():"+remove_list.size());
				for(int i=0;i<remove_list.size();i++)
				{
					CHILDREN.remove(remove_list.get(i));
				}
				if(CHILDREN.size()==0)
					result=false;
			}
			else
			{
				all_child=CHILDREN.keySet().iterator();
				result=true;
				Vector<String> remove_list=new Vector<String>();
				while(all_child.hasNext())
				{
					String temp_key=(String)all_child.next();
					if(!CHILDREN.get(temp_key).extend_all_leaves())
						remove_list.add(temp_key);
				}
				for(int i=0;i<remove_list.size();i++)
					CHILDREN.remove(remove_list.get(i));
				if(CHILDREN.size()==0)
					result=false;
			}
		}
		catch(Exception e)
		{
			System.out.println("VP_node extend_all_leaves exception:"+e);
		}
		return result;
	}

        public boolean path_build(VP_node a)
	{
		try
		{
			if(!CHILDREN.containsKey(a.ID))
			{
				CHILDREN.put(a.ID,a);
				return true;
			}
			else
				return false;
		}
		catch(Exception e)
		{
			System.out.println("VP_node path_build exception:"+e);
		}
		return false;
	}

        public boolean path_build_with_copy(VP_node a)
	{
		try
		{
			VP_node new_one=new VP_node(a.toString(),this);
			if(!CHILDREN.containsKey(a.ID))
			{
				CHILDREN.put(new_one.ID,new_one);
				return true;
			}
			else
				return false;
		}
		catch(Exception e)
		{
			System.out.println("VP_node path_build_with_copy exception:"+e);
			System.out.println("Error: "+a.toString());
		}
		return false;
	}

        public boolean path_build(String a)
	{
		try
		{
			if(!CHILDREN.containsKey(a))
			{
				CHILDREN.put(a,new VP_node(a,this));
				return true;
			}
			else
				return false;
		}
		catch(Exception e)
		{
			System.out.println("VP_node path_build exception:"+e);
		}
		return false;
	}

	public boolean path_build(Vector<String> a)
	{
		try
		{
			if(a.size()==0)
				return true;
			String temp=(String)a.remove(0);
			VP_node temp_node=null;
			if(!CHILDREN.containsKey(temp))
			{
				temp_node=new VP_node(temp,this);
				CHILDREN.put(temp,temp_node);
			}
			else
				temp_node=(VP_node)CHILDREN.get(temp);
			
			return temp_node.path_build(a);
		}
		catch(Exception e)
		{
			System.out.println("VP_node build_path exception:"+e);
		}
		return false;
	}
	
	public boolean path_build(String a[])
	{
		Vector<String> r=new Vector<String>();
		try
		{
			for(int i=0;i<a.length;i++)
				r.add(a[i]);
		}
		catch(Exception e)
		{
			System.out.println("VP_node build_path exception:"+e);
		}
		return path_build(r);
	}

	public boolean is_leaf()
	{
		if(CHILDREN.size()==0)
			return true;
		return false;
	}



	public boolean is_class_match(VP_node a)
	{
		boolean result=false;
		try
		{
			Set self_set=SCORE_TABLE.keySet();
			Set another_set=a.SCORE_TABLE.keySet();
			if(self_set.size()!=another_set.size())
				return false;
			if(self_set.equals(another_set))
				result=true;
		}
		catch(Exception e)
		{
			System.out.println("VP_node is_class_match exception:"+e);
		}
		return result;
	}

	public boolean is_possible_pair(VP_node a)
	{
		try
		{
			Set self_set=SCORE_TABLE.keySet();
			Set another_set=a.SCORE_TABLE.keySet();
			//If there is one class contained by a is true
			Iterator ir=another_set.iterator();
			while(ir.hasNext())
				if(self_set.contains(ir.next()))
					return true;
		}
		catch(Exception e)
		{
			System.out.println("VP_node is_possible_pair exception:"+e);
		}
		return false;
	}

	public double similarity(VP_node another_one)
	{
		double result=0.0;
		try
		{
			Iterator ir=SCORE_TABLE.keySet().iterator();
			HashMap<String,Double> copy_one=new HashMap<String,Double>();
			copy_one.putAll(another_one.SCORE_TABLE);
			double class_count=0.0;
			while(ir.hasNext())
			{
				String temp_class=(String)ir.next();
				double temp_score=(SCORE_TABLE.get(temp_class)).doubleValue();
				if(copy_one.get(temp_class)!=null)
				{
					temp_score=temp_score-(copy_one.get(temp_class)).doubleValue();
					copy_one.remove(temp_class);
				}
				result+=temp_score*temp_score;
				class_count+=1.0;
			}
			ir=copy_one.keySet().iterator();
			while(ir.hasNext())
			{
				String temp_class=(String)ir.next();
				double temp_score=(SCORE_TABLE.get(temp_class)).doubleValue();
				result+=temp_score*temp_score;
				class_count+=1.0;
			}
			result=result/class_count;
			result=Math.pow(result,0.5);
		}
		catch(Exception e)
		{
			System.out.println("VP_node similarity exception:"+e);
		}
		return result;
	}

	private int show_depth()
	{
		int result=0;
		VP_node temp=this;
		try
		{
			
			while(!temp.ID.equals(""))
			{
				temp=temp.PARENT;
				result++;
			}
			return result;
		}
		catch(Exception e)
		{
			System.out.println("show_depth:"+e);
		}
		return -1;
	}

	//We recorde the time point replace index of "a" for general sequential pattern mining.
	public boolean pattern_trace_for_counting(mp_transaction a,int time_point,int cn)
	{
//in this step , we can pass elements of "a" , we don't need got full match. But, a must include the subsequence which absolutely match      with the path to leaves.
		boolean result=false;
		try
		{
			//System.out.println("[Debug]NODE:"+this.ID+". Children:"+CHILDREN.keySet());
			//System.out.println("[Debug] time_point:"+time_point+" / a.HAPPEND_TIMES.size():"+a.HAPPEN_TIMES.size());
			//int temp_depth=show_depth();
			//System.out.println("[Depth]"+temp_depth);
			if(is_leaf())
			{
				result=true;
				//System.out.print("!");
				if(this.LABEL!=cn)
				{
					//System.out.print("@");
					SUPPORT++;
					this.LABEL=cn;
					if(SCORE_TABLE.get(a.CLASS_LABEL)!=null)
					{		
						//System.out.print("#");
						double cv=((Double)SCORE_TABLE.get(a.CLASS_LABEL)).doubleValue()+1.0;
						SCORE_TABLE.put(a.CLASS_LABEL,new Double(cv));
					}
					else
						SCORE_TABLE.put(a.CLASS_LABEL,new Double(1.0));
				}
//				if(this.ID.equals("t1v87"))
//					System.out.println("SUPPORT:"+SUPPORT+":"+SCORE_TABLE);
			}
			else
			{
				//not a leaf
				if(time_point>=a.HAPPEN_TIMES.size())
				{
					//System.out.println("QQ");
					result=false;
				}
				else
				{
					//System.out.println("GG");
					Vector<String> match_list=new Vector<String>();
					result=false;
					int stop_time_point=time_point;
					if(ID.equals(""))
						stop_time_point=a.HAPPEN_TIMES.size();
					else
					{
						if(GAP_FLG)
							stop_time_point=time_point+GAP;
						if(stop_time_point>a.HAPPEN_TIMES.size())
							stop_time_point=a.HAPPEN_TIMES.size();
					}
					//System.out.println("GG2:"+time_point+"~"+stop_time_point);
					//for(int i=time_point;i<a.HAPPEN_TIMES.size();i++)
					//{
					for(int i=time_point;i<stop_time_point;i++)
					{
						//String temp=(String)a.get(index);
						Vector<pp_item> temp_set=(Vector<pp_item>)a.get_item_at_time_point(i);
						//if(cn==0)
						//	System.out.println("Time Point "+i+" :"+temp_set+" =>"+CHILDREN.keySet());
						for(int pi=0;pi<temp_set.size();pi++)
						{
							String temp_id=((pp_item)temp_set.get(pi)).NAME;
						//	if(temp_depth==1)
						//		System.out.println("D1::"+temp_id+"::"+CHILDREN.keySet());
							if(match_list.contains(temp_id))
								continue;
							if(CHILDREN.containsKey(temp_id))
							{
								//if(((VP_node)CHILDREN.get(temp_id)).is_leaf())
								//{
								//	System.out.println(temp_set+"||"+((VP_node)CHILDREN.get(temp_id)).CHILDREN.keySet());
								//}
								boolean temp_b=((VP_node)CHILDREN.get(temp_id)).pattern_trace_for_counting(a,(i+1),cn);
								result=result || temp_b;
								if(temp_b)
									match_list.add(temp_id);
							}
						}
					}
					if(match_list.size()>0)
					{
						//System.out.println(cn+":"+this.ID+" Matched List:"+match_list);
						SUPPORT++;
						double temp_class_score=1.0;
						if(SCORE_TABLE.containsKey(a.CLASS_LABEL))
							temp_class_score+=((Double)SCORE_TABLE.get(a.CLASS_LABEL)).doubleValue();
						SCORE_TABLE.put(a.CLASS_LABEL,new Double(temp_class_score));
						result=true;
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("VP_node pattern_trace_for_counting exception:"+e);
		}
		return result;
	}
		    

	public boolean pattern_trace_for_length(Vector<String> a,int index,HashMap<String,Double> cs)
	{
		return  pattern_trace_for_length(a,index,cs,-1,"");
	}
	
	public boolean pattern_trace_for_length(Vector<String> a,int index,HashMap<String,Double> cs,String pp)
	{
		return  pattern_trace_for_length(a,index,cs,-1,pp);
	}
	
	public boolean pattern_trace_for_length(Vector<String> a,int index,HashMap<String,Double> cs,int tn)
	{
		return  pattern_trace_for_length(a,index,cs,tn,"");
	}

	 public boolean pattern_trace_for_length(Vector<String> a,int index,HashMap<String,Double> cs,int tn,String pp)
	 {
//in this step , we can pass elements of "a" , we don't need got full match. But, a must include the subsequence which absolutely match with the path to leaves.
		boolean result=false;
		try
		{
			//count the score
			if(SCORE_TABLE.size()!=0 && (tn<0 || (tn>=0 && tn!=LABEL)))
			{
				result=true;
				LABEL=tn;
				if(SCOREABLE)
				{
					Iterator ir=SCORE_TABLE.keySet().iterator();
					while(ir.hasNext())
					{
						String temp_key=(String)ir.next();
						//Get Score!!!
						//Length as Score - type 1
						double tts=(double)this.LAYER; //Length
						//Length*Confdience as score - type 2
						//double tts=(double)this.LAYER*get_confidence(temp_key);  //Length*confidence
						//Support*Confidence as score - typ 3
						//double tts=((Double)SCORE_TABLE.get(temp_key)).doubleValue()*get_confidence(temp_key);  //confidence*support
						//GainRatio*Confidence as Score - type 4
						//double tts=get_gainratio()*get_confidence(temp_key); //GainRatio*Confidence
						//
						//Compound Score = Conf*GainRatio*Support/class_count
						//double tts=((Double)SCORE_TABLE.get(temp_key)).doubleValue()/sum_of_score();
						//tts*=get_gainratio();
						//tts*=get_confidence(temp_key);
						//tts/=(double)SCORE_TABLE.size();
						
						if(cs.get(temp_key)!=null)
						{
							double ts=((Double)cs.get(temp_key)).doubleValue();
							ts+=tts;
							cs.put(temp_key,new Double(ts));
						}
						else
						{
							cs.put(temp_key,new Double(tts));
						}
					}
					System.out.println(pp+"---"+SCORE_TABLE);
				}
			}

			//Keep tracing
			if(is_leaf())
			{
				//result=true;
				//sn_set.add(new Integer(SN));
				//SUPPORT++;
			}
			else
			{
				//not a leaf
				if(index==a.size())
				{
					result=false;
				}
				else
				{
					Vector<String> match_list=new Vector<String>();
					result=false;
					for(int i=index;i<a.size();i++)
					{
						String temp=(String)a.get(i);
						if(match_list.contains(temp))
							continue;
						if(CHILDREN.containsKey(temp))
						{
							boolean temp_b=((VP_node)CHILDREN.get(temp)).pattern_trace_for_length(a,(i+1),cs,tn,pp+temp+",");
							result=result || temp_b;
							if(temp_b)
								match_list.add(temp);
						}
					}
					if(match_list.size()>0)
					{
					//SUPPORT++;
						result=true;
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("VP_node pattern_trace_for_length exception:"+e);
		}
		return result;
	}

	public boolean pattern_trace_for_class_score(mp_transaction a,int index,HashMap<String,Double> cs)
	{
//in this step , we can pass elements of "a" , we don't need got full match. But, a must include the subsequence which absolutely match with the path to leaves.
		boolean result=false;
		try
		{
			//count the score
			if(SCORE_TABLE.size()!=0)
			{
				result=true;
				//LABEL=tn;
				if(SCOREABLE)
				{
					Iterator ir=SCORE_TABLE.keySet().iterator();
					while(ir.hasNext())
					{
						String temp_key=(String)ir.next();
						//Get Score!!!
						//Length as Score - type 1
						double tts=(double)this.LAYER; //Length
						//Length*Confdience as score - type 2
						//double tts=(double)this.LAYER*get_confidence(temp_key);  //Length*confidence
						//Support*Confidence as score - typ 3
						//double tts=((Double)SCORE_TABLE.get(temp_key)).doubleValue()*get_confidence(temp_key);  //confidence*support
						//GainRatio*Confidence as Score - type 4
						//double tts=get_gainratio()*get_confidence(temp_key); //GainRatio*Confidence
						//
						//Compound Score = Conf*GainRatio*Support/class_count
						//double tts=((Double)SCORE_TABLE.get(temp_key)).doubleValue()/sum_of_score();
						//tts*=get_gainratio();
						tts*=get_confidence(temp_key);
						//tts/=(double)SCORE_TABLE.size();
						
						if(cs.get(temp_key)!=null)
						{
							double ts=((Double)cs.get(temp_key)).doubleValue();
							//type 1, add all
							//ts+=tts;
							//type 2 , Just take bigest!
							if(tts>ts)
								ts=tts;
							cs.put(temp_key,new Double(ts));
						}
						else
						{
							cs.put(temp_key,new Double(tts));
						}
					}
					//System.out.println(pp+"---"+SCORE_TABLE);
				}
			}

			//Keep tracing
			if(is_leaf())
			{
				//result=true;
				//sn_set.add(new Integer(SN));
				//SUPPORT++;
			}
			else
			{
				//not a leaf
				if(index==a.size())
				{
					result=false;
				}
				else
				{
					Vector<String> match_list=new Vector<String>();
					result=false;
					for(int i=index;i<a.HAPPEN_TIMES.size();i++)
					{
						Vector<pp_item> temp_set=a.get_item_at_time_point(i);
						for(int ti=0;ti<temp_set.size();ti++)
						{
							String temp=((pp_item)temp_set.get(ti)).NAME;
							if(match_list.contains(temp))
								continue;
							if(CHILDREN.containsKey(temp))
							{
								boolean temp_b=((VP_node)CHILDREN.get(temp)).pattern_trace_for_class_score(a,(i+1),cs);
								result=result || temp_b;
								if(temp_b)
									match_list.add(temp);
							}
						}
					}
					if(match_list.size()>0)
					{
					//SUPPORT++;
						result=true;
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("VP_node pattern_trace_for_length exception:"+e);
		}
		return result;
	}

	//Have not been implemented yet
	private double get_gainratio()
	{
		return 1.0;
	}

	public boolean path_build_with_sn(Vector<String> a,int sn)
	{
		try
		{
		//System.out.print(sn+",");
			if(a.size()==0)
			{
				SN_set.add(new Integer(sn));
				//System.out.println("gogogo"+SN);
				return true;
			}
			String temp=(String)a.remove(0);
			VP_node temp_node=null;
			if(!CHILDREN.containsKey(temp))
			{
				temp_node=new VP_node(temp,this);
				CHILDREN.put(temp,temp_node);
			}
			else
				temp_node=(VP_node)CHILDREN.get(temp);

			return temp_node.path_build_with_sn(a,sn);
		}
		catch(Exception e)
		{
			System.out.println("VP_node build_path_with_sn exception:"+e);
		}
		return false;
	}

	public boolean pattern_trace_for_sn(Vector<String> a,int index,Vector<Integer> sn_set)
	{
//in this step , we can pass elements of "a" , we don't need got full match. But, a must include the subsequence which absolutely match      with the path to leaves.
		try
		{
			if(SN_set.size()!=0)
			{
				for(int i=0;i<SN_set.size();i++)
					sn_set.add((Integer)SN_set.get(i));
			}
			Vector<String> match_list=new Vector<String>();
			for(int i=index;i<a.size();i++)
			{
				//String temp=(String)a.get(index);
				String temp=(String)a.get(i);
				if(match_list.contains(temp))
					continue;
				if(CHILDREN.containsKey(temp))
				{
					boolean temp_b=((VP_node)CHILDREN.get(temp)).pattern_trace_for_sn(a,(i+1),sn_set);
					if(temp_b)
						match_list.add(temp);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("VP_node pattern_trace_with_sn exception:"+e);
			return false;
		}
		return true;
	}

	public boolean clear_all_count()
	{
		try
		{
			this.SUPPORT=0;
			if(!is_leaf())
			{
				Iterator ir=CHILDREN.keySet().iterator();
				while(ir.hasNext())
				{
					((VP_node)CHILDREN.get((String)ir.next())).clear_all_count();
				}
			}
			else
			{
				SUPPORT=0;
				SCORE_TABLE.clear();
			}
		}
		catch(Exception e)
		{
			System.out.println("VP_node clear_all_count exception:"+e);
			return false;
		}
		return true;
	}
}
