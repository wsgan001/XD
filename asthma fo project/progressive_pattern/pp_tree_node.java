package progressive_pattern;
import java.util.*;

public class pp_tree_node
{
	public pp_tree_node PARENT=null;
	public HashMap<String,pp_tree_node> CHILDREN=new HashMap<String,pp_tree_node>();
	//public HashMap<String,Integer> SCORE_TABLE=new HashMap<String,Integer>();
	public HashMap<String,Double> SCORE_TABLE=new HashMap<String,Double>(); //for score and class_count
	public String CONTENT="";
	public int COUNT=0;
	private Vector<Integer> SN_set=new Vector<Integer>(); //for pattern relpace
	private int LABEL=-1; //for pattern counting
	public int LAYER=0;
	private boolean SCOREABLE=true;	

	public static boolean MOTIF_MODE=false;
	public static int MOTIF_LENGTH=0;
	public static int MOTIF_TOLERANCE=0;

//	public void MAX_GAP=-1;

	public pp_tree_node()
	{		
		CONTENT="";
	}
	
	public pp_tree_node(String t)
	{		
		CONTENT=t;
	}
	
	public pp_tree_node(String t,pp_tree_node p)
	{
		PARENT=p;
		CONTENT=t;
	}
	
	public static void set_motif(int l,int t)
	{
		MOTIF_MODE=true;
		MOTIF_LENGTH=l;
		MOTIF_TOLERANCE=t;
	}

	public void initial_for_prediction()
	{
		try
		{
			this.LABEL=-1;
			Iterator ir=CHILDREN.keySet().iterator();
			while(ir.hasNext())
			{
				pp_tree_node temp_node=(pp_tree_node)CHILDREN.get((String)ir.next());
				temp_node.initial_for_prediction();
			}
		}
		catch(Exception e)
		{
			System.out.println("pp_tree_node initial_for_prediction exception:"+e);
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
//				result=true;
				SCOREABLE=false;				
			}
			Iterator ir=CHILDREN.keySet().iterator();
//			Vector<String> remove_set=new Vector<String>();
			while(ir.hasNext())
			{
				String temp_key=(String)ir.next();
				pp_tree_node temp_node=(pp_tree_node)CHILDREN.get(temp_key);
				boolean temp_r=temp_node.remove_redundancy(cc);
				result=result&&temp_r;
//				if(temp_r)
//					remove_set.add(temp_key);
			}
//			for(int i=0;i<remove_set.size();i++)
//				CHILDREN.remove((String)remove_set.get(i));
		}
		catch(Exception e)
		{
			System.out.println("pp_tree_node remove_redundancy exception:"+e);
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
                        System.out.println("pp_tree_node find_max_key exception:"+e);
                }
                return result;
        }


	public boolean path_build_with_class_score(Vector<String> a,String cl,int s)
	{
		return path_build_with_class_score(a,cl,(double)s);
	}
	
	public boolean path_build_with_class_score(Vector<String> a,String cl,double s)
	{
		try
		{
			if(a.size()==0)
			{
				SCORE_TABLE.put(cl,new Double(s));
				return true;
			}
			String temp=(String)a.remove(0);
			pp_tree_node temp_node=null;
			if(!CHILDREN.containsKey(temp))
			{
				temp_node=new pp_tree_node(temp,this);
				temp_node.LAYER=this.LAYER+1;
				CHILDREN.put(temp,temp_node);
			}
			else
				temp_node=(pp_tree_node)CHILDREN.get(temp);
			
			return temp_node.path_build_with_class_score(a,cl,s);
		}
		catch(Exception e)
		{
			System.out.println("pp_tree_node build_path exception:"+e);
		}
		return false;		
	}

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
			System.out.println("pp_tree_node sum_of_score exception:"+e);
		}
		return result;
	}	

	private double get_confidence(String key)
	{
		double result=0.0;
		try
		{
			Iterator ir=SCORE_TABLE.keySet().iterator();
			while(ir.hasNext())
			{
				result+=((Double)SCORE_TABLE.get((String)ir.next())).doubleValue();
			}
			result=((Double)SCORE_TABLE.get(key)).doubleValue()/result;
		}
		catch(Exception e)
		{
			System.out.println("pp_tree_node get_confidence exception:"+e);
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
			double tts=(double)this.LAYER;
			//double tts=(double)this.LAYER*get_confidence(temp_key);
			//double tts=((Double)SCORE_TABLE.get(temp_key)).doubleValue()*get_confidence(temp_key);
			//double tts=get_gainratio()*get_confidence(temp_key);
						//double tts=1/(double)SCORE_TABLE.size();
						//double tts=((Double)SCORE_TABLE.get(temp_key)).doubleValue()/sum_of_score();
				//		tts*=((Double)SCORE_TABLE.get(temp_key)).doubleValue()/sum_of_score()*((Double)SCORE_TABLE.get(temp_key)).doubleValue();
				//		tts*=get_gainratio();
				//		tts*=get_confidence(temp_key);
				//		tts/=(double)SCORE_TABLE.size();
	                                        if(cs.get(temp_key)!=null)
        	                                {
                	                                double ts=((Double)cs.get(temp_key)).doubleValue();
                        	                        ts+=tts;
                                	                //System.out.println(ts);
                                        	        cs.put(temp_key,new Double(ts));
	                                        }
        	                                else
                	                        {
                        	                        //System.out.println((Integer)SCORE_TABLE.get(temp_key));
                                	                //cs.put(temp_key,(Integer)SCORE_TABLE.get(temp_key).intValue()); 
                                        	        cs.put(temp_key,new Double(tts));
                                                	//cs.put(temp_key,new Integer(1));
	                                        }
                                	}
	                                System.out.println(pp+"---"+SCORE_TABLE);
				}
                        }
                        if(is_leaf())
                        {
                                //result=true;
                                //sn_set.add(new Integer(SN));
                                //COUNT++;
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
                                                        boolean temp_b=((pp_tree_node)CHILDREN.get(temp)).pattern_trace_for_length(a,(i+1),cs,tn,pp+temp+",");
                                                        result=result || temp_b;
                                                        if(temp_b)
                                                                match_list.add(temp);
                                                }
                                        }
                                        if(match_list.size()>0)
                                        {
                                                //COUNT++;
                                                result=true;
                                        }
                                }
                        }
                }
                catch(Exception e)
                {
                        System.out.println("pp_tree_node pattern_trace_for_length exception:"+e);
                }
                return result;
        }

        public boolean pattern_trace_for_all_score(Vector<String> a,int index,HashMap<String,Double> cs)
        {
                return  pattern_trace_for_all_score(a,index,cs,-1,"");
        }

        public boolean pattern_trace_for_all_score(Vector<String> a,int index,HashMap<String,Double> cs,String pp)
        {
                return  pattern_trace_for_all_score(a,index,cs,-1,pp);
        }

        public boolean pattern_trace_for_all_score(Vector<String> a,int index,HashMap<String,Double> cs,int tn)
        {
                return  pattern_trace_for_all_score(a,index,cs,tn,"");
        }
	
        public boolean pattern_trace_for_all_score(Vector<String> a,int index,HashMap<String,Double> cs,int tn,String pp)
        {
                //in this step , we can pass elements of "a" , we don't need got full match. But, a must include the subsequence which absolutely match with the path to leaves.
                boolean result=false;
                try
                {
                        if(SCORE_TABLE.size()!=0 && (tn<0 || (tn>=0 && tn!=LABEL)))
                        {
				System.out.print(LABEL+"/"+tn);
                                result=true;
				LABEL=tn;
				if(SCOREABLE)
				{
	                                Iterator ir=SCORE_TABLE.keySet().iterator();
	                                while(ir.hasNext())
        	                        {
                	                        String temp_key=(String)ir.next();
						double tts=(double)this.LAYER;
						//double tts=((Double)SCORE_TABLE.get(temp_key)).doubleValue()/sum_of_score();
						//tts*=((Double)SCORE_TABLE.get(temp_key)).doubleValue()/sum_of_score()*((Double)SCORE_TABLE.get(temp_key)).doubleValue();
						tts*=get_gainratio();
						tts*=get_confidence(temp_key);
						tts/=(double)SCORE_TABLE.size();
	                                        if(cs.get(temp_key)!=null)
        	                                {
                	                                double ts=((Double)cs.get(temp_key)).doubleValue();
                        	                        ts+=tts;
                                	                //System.out.println(ts);
                                        	        cs.put(temp_key,new Double(ts));
	                                        }
        	                                else
                	                        {
                        	                        //System.out.println((Integer)SCORE_TABLE.get(temp_key));
                                	                //cs.put(temp_key,(Integer)SCORE_TABLE.get(temp_key).intValue()); 
                                        	        cs.put(temp_key,new Double(tts));
                                                	//cs.put(temp_key,new Integer(1));
	                                        }
                                	}
	                                System.out.println(pp+"---"+SCORE_TABLE);
				}
                        }
                        if(is_leaf())
                        {
                                //result=true;
                                //sn_set.add(new Integer(SN));
                                //COUNT++;
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
                                                        boolean temp_b=((pp_tree_node)CHILDREN.get(temp)).pattern_trace_for_all_score(a,(i+1),cs,tn,pp+temp+",");
                                                        result=result || temp_b;
                                                        if(temp_b)
                                                                match_list.add(temp);
                                                }
                                        }
                                        if(match_list.size()>0)
                                        {
                                                //COUNT++;
                                                result=true;
                                        }
                                }
                        }
                }
                catch(Exception e)
                {
                        System.out.println("pp_tree_node pattern_trace_for_all_score exception:"+e);
                }
                return result;
        }
        
        public boolean pattern_trace_for_class_score(Vector<String> a,int index,HashMap<String,Double> cs)
        {
                return  pattern_trace_for_class_score(a,index,cs,-1,"");
        }

        public boolean pattern_trace_for_class_score(Vector<String> a,int index,HashMap<String,Double> cs,String pp)
        {
                return  pattern_trace_for_class_score(a,index,cs,-1,pp);
        }

	public boolean pattern_trace_for_class_score(Vector<String> a,int index,HashMap<String,Double> cs,int tn)
	{
		return  pattern_trace_for_class_score(a,index,cs,tn,"");
	}
	
	public boolean pattern_trace_for_class_score(Vector<String> a,int index,HashMap<String,Double> cs,int tn,String pp)
	{
		//in this step , we can pass elements of "a" , we don't need got full match. But, a must include the subsequence which absolutely match with the path to leaves.
		boolean result=false;
		try
		{
                        //if(SCORE_TABLE.size()!=0 && SCOREABLE && (tn>=0 && tn!=LABEL))
                        if(SCORE_TABLE.size()!=0 && (tn<0 || (tn>=0 && tn!=LABEL)))
			{
				result=true;	
				LABEL=tn;
				if(SCOREABLE)
				{
/*
					String temp_k=find_max_key(SCORE_TABLE);
					double tts=((Double)SCORE_TABLE.get(temp_k)).doubleValue();
                                        if(cs.get(temp_k)!=null)
                                        {
                                        	double ts=((Double)cs.get(temp_k)).doubleValue();
                                                if(tts>ts)
                                                        ts=tts;
                                                cs.put(temp_k,new Double(ts));
                                        }
                                        else
                                        {
                                                cs.put(temp_k,new Double(tts));
                                        }
*/

					Iterator ir=SCORE_TABLE.keySet().iterator();
					while(ir.hasNext())
					{
						String temp_key=(String)ir.next();
						//int tts=(Integer)SCORE_TABLE.get(temp_key).intValue()/SCORE_TABLE.size();
						double tts=((Double)SCORE_TABLE.get(temp_key)).doubleValue();
//						int tts=1;
						if(cs.get(temp_key)!=null)
						{
							double ts=((Double)cs.get(temp_key)).doubleValue();
//							ts+=tts;
							if(tts>ts)
								ts=tts;
							//System.out.println(ts);
							cs.put(temp_key,new Double(ts));
						}
						else
						{
							//System.out.println((Integer)SCORE_TABLE.get(temp_key));
							//cs.put(temp_key,(Integer)SCORE_TABLE.get(temp_key).intValue());						
							cs.put(temp_key,new Double(tts));
							//cs.put(temp_key,new Integer(1));
						}
					}
					System.out.println(pp+"---"+SCORE_TABLE);  

				}
			}
			if(is_leaf())
			{
				//result=true;
				//sn_set.add(new Integer(SN));
				//COUNT++;
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
							boolean temp_b=((pp_tree_node)CHILDREN.get(temp)).pattern_trace_for_class_score(a,(i+1),cs,tn,pp+temp+",");
							result=result || temp_b;
							if(temp_b)
								match_list.add(temp);
						}
					}
					if(match_list.size()>0)
					{						
						//COUNT++;
						result=true;
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("pp_tree_node pattern_trace exception:"+e);
		}
		return result;
	}	

        public boolean pattern_trace_for_length_x_gainratio(Vector<String> a,int index,HashMap<String,Double> cs)
        {
                return pattern_trace_for_length_x_gainratio(a,index,cs,-1,"");
        }

        public boolean pattern_trace_for_length_x_gainratio(Vector<String> a,int index,HashMap<String,Double> cs,String pp)
        {
                return pattern_trace_for_length_x_gainratio(a,index,cs,-1,pp);
        }

	public boolean pattern_trace_for_length_x_gainratio(Vector<String> a,int index,HashMap<String,Double> cs,int tn)
	{
		return pattern_trace_for_length_x_gainratio(a,index,cs,-1,"");
	}

	public boolean pattern_trace_for_length_x_gainratio(Vector<String> a,int index,HashMap<String,Double> cs,int tn,String pp)
	{
		//in this step , we can pass elements of "a" , we don't need got full match. But, a must include the subsequence which absolutely match with the path to leaves.
		boolean result=false;
		try
		{
                        //if(SCORE_TABLE.size()!=0 && SCOREABLE && (tn>=0 && tn!=LABEL))
                        if(SCORE_TABLE.size()!=0 && (tn<0 || (tn>=0 && tn!=LABEL)))
			{
				result=true;		
				LABEL=tn;
				if(SCOREABLE)
				{
					double gr=get_gainratio();
					Iterator ir=SCORE_TABLE.keySet().iterator();
					while(ir.hasNext())
					{
						String temp_key=(String)ir.next();
						//System.out.print(pp+" ");
						//int tts=(Integer)SCORE_TABLE.get(temp_key).intValue()/SCORE_TABLE.size();
						double tts=this.LAYER*gr;
						//int tts=1;
						if(cs.get(temp_key)!=null)
						{
							double ts=((Double)cs.get(temp_key)).doubleValue();						
							ts+=tts;
							//System.out.println(ts);
							cs.put(temp_key,new Double(ts));
						}
						else
						{
							//System.out.println((Integer)SCORE_TABLE.get(temp_key));
							//cs.put(temp_key,(Integer)SCORE_TABLE.get(temp_key).intValue());						
							cs.put(temp_key,new Double(tts));
							//cs.put(temp_key,new Integer(1));
						}
					}
				System.out.println(pp+"~~~~"+SCORE_TABLE);
				}
			}
			if(is_leaf())
			{
				//result=true;
				//sn_set.add(new Integer(SN));
				//COUNT++;
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
							boolean temp_b=((pp_tree_node)CHILDREN.get(temp)).pattern_trace_for_length_x_gainratio(a,(i+1),cs,tn,pp+temp+",");
							result=result || temp_b;
							if(temp_b)
								match_list.add(temp);
						}
					}
					if(match_list.size()>0)
					{						
						//COUNT++;
						result=true;
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("pp_tree_node pattern_trace_for_length_x_gainratio exception:"+e);
		}
		return result;
	}

	private double get_gainratio()
	{
		double result=1.0;
		try
		{
			Iterator ir=SCORE_TABLE.keySet().iterator();
			double yp=-1.0;
			double np=-1.0;
			double yr[]=new double[SCORE_TABLE.size()];
			int yr_i=0;
			double y_sum=0.0;
			double n_sum=0.0;
			while(ir.hasNext())
			{
				yr[yr_i]=((Double)SCORE_TABLE.get((String)ir.next())).doubleValue();
				y_sum+=yr[yr_i];
				n_sum+=(1.0-yr[yr_i]);
				yr_i++;
			}
			yp=yp*y_sum/(double)yr.length;
			double temp_sum=0.0;
			for(int i=0;i<yr.length;i++)
				temp_sum+=(yr[i]/y_sum)*Math.log10(yr[i]/y_sum);
			yp=yp*temp_sum;

			np=np*n_sum/(double)yr.length;
			temp_sum=0.0;
			for(int i=0;i<yr.length;i++)
				temp_sum+=((1.0-yr[i])/n_sum)*Math.log10((1.0-yr[i])/n_sum);
			np=np*temp_sum;

			result=result-(yp+np);
		}
		catch(Exception e)
		{
			System.out.println("pp_tree_node get_gainratio exception:"+e);
		}
		return result;
	}
	
        public boolean pattern_trace_for_class_score_x_gainratio(Vector<String> a,int index,HashMap<String,Double> cs)
        {
                return pattern_trace_for_class_score_x_gainratio(a,index,cs,-1,"");
        }

        public boolean pattern_trace_for_class_score_x_gainratio(Vector<String> a,int index,HashMap<String,Double> cs,String pp)
        {
                return pattern_trace_for_class_score_x_gainratio(a,index,cs,-1,pp);
        }

	public boolean pattern_trace_for_class_score_x_gainratio(Vector<String> a,int index,HashMap<String,Double> cs,int tn)
	{
		return pattern_trace_for_class_score_x_gainratio(a,index,cs,tn,"");
	}
	
	public boolean pattern_trace_for_class_score_x_gainratio(Vector<String> a,int index,HashMap<String,Double> cs,int tn,String pp)
	{
		//in this step , we can pass elements of "a" , we don't need got full match. But, a must include the subsequence which absolutely match with the path to leaves.
		boolean result=false;
		try
		{
                        //if(SCORE_TABLE.size()!=0 && SCOREABLE && (tn>=0 && tn!=LABEL))
                        if(SCORE_TABLE.size()!=0 && (tn<0 || (tn>=0 && tn!=LABEL)))
			{
				result=true;
				LABEL=tn;
				if(SCOREABLE)
				{
					double gr=get_gainratio();
					Iterator ir=SCORE_TABLE.keySet().iterator();
					while(ir.hasNext())
					{
						String temp_key=(String)ir.next();
						//System.out.print(pp+" ");
						//int tts=(Integer)SCORE_TABLE.get(temp_key).intValue()/SCORE_TABLE.size();
						double tts=((Double)SCORE_TABLE.get(temp_key)).doubleValue();
						tts=tts*gr;
//						tts=gr;
						//int tts=1;
						if(cs.get(temp_key)!=null)
						{
							double ts=((Double)cs.get(temp_key)).doubleValue();						
							ts+=tts;
							//System.out.println(ts);
							cs.put(temp_key,new Double(ts));
						}
						else
						{
							//System.out.println((Integer)SCORE_TABLE.get(temp_key));
							//cs.put(temp_key,(Integer)SCORE_TABLE.get(temp_key).intValue());						
							cs.put(temp_key,new Double(tts));
							//cs.put(temp_key,new Integer(1));
						}
					}
					System.out.println(pp+"~~~~"+SCORE_TABLE);
				}
			}
			if(is_leaf())
			{
				//result=true;
				//sn_set.add(new Integer(SN));
				//COUNT++;
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
							boolean temp_b=((pp_tree_node)CHILDREN.get(temp)).pattern_trace_for_class_score_x_gainratio(a,(i+1),cs,tn,pp+temp+",");
							result=result || temp_b;
							if(temp_b)
								match_list.add(temp);
						}
					}
					if(match_list.size()>0)
					{						
						//COUNT++;
						result=true;
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("pp_tree_node pattern_trace exception:"+e);
		}
		return result;
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
			pp_tree_node temp_node=null;
			if(!CHILDREN.containsKey(temp))
			{
				temp_node=new pp_tree_node(temp,this);
				CHILDREN.put(temp,temp_node);
			}
			else
				temp_node=(pp_tree_node)CHILDREN.get(temp);
			
			return temp_node.path_build_with_sn(a,sn);
		}
		catch(Exception e)
		{
			System.out.println("pp_tree_node build_path exception:"+e);
		}
		return false;
	}
	
	public boolean pattern_trace_for_sn(Vector<String> a,int index,Vector<Integer> sn_set)
	{
		//in this step , we can pass elements of "a" , we don't need got full match. But, a must include the subsequence which absolutely match with the path to leaves.
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
					boolean temp_b=((pp_tree_node)CHILDREN.get(temp)).pattern_trace_for_sn(a,(i+1),sn_set);
					if(temp_b)
						match_list.add(temp);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("pp_tree_node pattern_trace exception:"+e);
			return false;
		}
		return true;
	}	
	
	public boolean pattern_trace_for_sn_and_pattern_location(Vector<String> a,int index,Vector<String> sn_set,String loc)
	{
		//in this step , we can pass elements of "a" , we don't need got full match. But, a must include the subsequence which absolutely match with the path to leaves.
		try
		{			
			if(SN_set.size()!=0)
			{
				for(int i=0;i<SN_set.size();i++)
					sn_set.add(((Integer)SN_set.get(i)).toString()+"("+loc+")");
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
					//loc+=Integer.toString(i)+",";
					boolean temp_b=((pp_tree_node)CHILDREN.get(temp)).pattern_trace_for_sn_and_pattern_location(a,(i+1),sn_set,loc+Integer.toString(i)+"~");
					if(temp_b)
						match_list.add(temp);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("pp_tree_node pattern_trace_with_sn_and_pattern_location exception:"+e);
			return false;
		}
		return true;
	}	
/*	public boolean pattern_trace_for_sn_and_location(Vector<String> a,int index,Vector<String> sn_set)
	{
		//in this step , we can pass elements of "a" , we don't need got full match. But, a must include the subsequence which absolutely match with the path to leaves.
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
					boolean temp_b=((pp_tree_node)CHILDREN.get(temp)).pattern_trace_for_sn(a,(i+1),sn_set);
					if(temp_b)
						match_list.add(temp);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("pp_tree_node pattern_trace exception:"+e);
			return false;
		}
		return true;
	}	
*/
	public boolean path_build(Vector<String> a)
	{
		try
		{
			if(a.size()==0)
				return true;
			String temp=(String)a.remove(0);
			pp_tree_node temp_node=null;
			if(!CHILDREN.containsKey(temp))
			{
				temp_node=new pp_tree_node(temp,this);
				CHILDREN.put(temp,temp_node);
			}
			else
				temp_node=(pp_tree_node)CHILDREN.get(temp);
			
			return temp_node.path_build(a);
		}
		catch(Exception e)
		{
			System.out.println("pp_tree_node build_path exception:"+e);
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
			System.out.println("pp_tree_node build_path exception:"+e);
		}
		return path_build(r);
	}

	public boolean path_build(String a)
	{
		try
		{
			if(!CHILDREN.containsKey(a))
			{
				CHILDREN.put(a,new pp_tree_node(a,this));
				return true;
			}
			else
				return false;
		}
		catch(Exception e)
		{
			System.out.println("pp_tree_node build_path exception:"+e);
		}
		return false;
	}
	
	public boolean path_build_with_class_count(String a,HashMap<String,Integer> cc) //with class count , which the class has larger count
	{
		try
		{
			if(!CHILDREN.containsKey(a))
			{
				pp_tree_node temp_tree_node=new pp_tree_node(a,this);
				Iterator ir=cc.keySet().iterator();
				while(ir.hasNext())
				{
					String temp_class_name=(String)ir.next();
					temp_tree_node.SCORE_TABLE.put(temp_class_name,new Double((double)((Integer)cc.get(temp_class_name)).intValue()));
				}
				CHILDREN.put(a,temp_tree_node);
				return true;
			}
			else
				return false;
		}
		catch(Exception e)
		{
			System.out.println("pp_tree_node build_path_with_class_count exception:"+e);
		}
		return false;
	}
	
	public boolean is_leaf()
	{
		if(CHILDREN.size()==0)
			return true;
		return false;
	}
	
	public boolean pattern_trace_for_counting(Vector<String> a,int index,int cn,String class_label)
	{
		//in this step , we can pass elements of "a" , we don't need got full match. But, a must include the subsequence which absolutely match with the path to leaves.
		boolean result=false;
		try
		{
			if(is_leaf())
			{
				result=true;
				if(this.LABEL!=cn)
				{
					COUNT++;
					this.LABEL=cn;
					if(SCORE_TABLE.containsKey(class_label))
					{
						double temp_value=((Double)SCORE_TABLE.get(class_label)).doubleValue()+1.0;
						SCORE_TABLE.put(class_label,new Double(temp_value));
					}
					else
					{
						SCORE_TABLE.put(class_label,new Double(1.0));
					}
				}
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
						//String temp=(String)a.get(index);
						String temp=(String)a.get(i);
						if(match_list.contains(temp))
							continue;
						if(CHILDREN.containsKey(temp))
						{
							boolean temp_b=((pp_tree_node)CHILDREN.get(temp)).pattern_trace_for_counting(a,(i+1),cn,class_label);
							result=result || temp_b;
							if(temp_b)
								match_list.add(temp);
						}
					}
					if(match_list.size()>0)
					{						
						COUNT++;
						result=true;
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("pp_tree_node pattern_trace_for_counting exception:"+e);
		}
		return result;
	}
	
	public boolean pattern_trace(Vector<String> a,int index)
	{
		//in this step , we can pass elements of "a" , we don't need got full match. But, a must include the subsequence which absolutely match with the path to leaves.
		boolean result=false;
		try
		{
			if(is_leaf())
			{
				result=true;
				COUNT++;
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
						//String temp=(String)a.get(index);
						String temp=(String)a.get(i);
						if(match_list.contains(temp))
							continue;
						if(CHILDREN.containsKey(temp))
						{
							boolean temp_b=((pp_tree_node)CHILDREN.get(temp)).pattern_trace(a,(i+1));
							result=result || temp_b;
							if(temp_b)
								match_list.add(temp);
						}
					}
					if(match_list.size()>0)
					{						
						COUNT++;
						result=true;
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("pp_tree_node pattern_trace exception:"+e);
		}
		return result;
	}
	
	public boolean extend_all_leaves()
	{
		//Just extends all leaves (There is no deletion in this action)
		//If the node is level2 node , we just extends all its leaves
		boolean result=true;
		try
		{
			Iterator all_child=CHILDREN.keySet().iterator();
			boolean level2_flg=true;
			while(all_child.hasNext())
			{
				pp_tree_node temp_node=(pp_tree_node)CHILDREN.get(all_child.next());
				level2_flg=level2_flg&&temp_node.is_leaf();
			}
			if(level2_flg)
			{
				if(CHILDREN.size()<1)	//this is for prunning ABC+ABC=>ABCC
					result=false;
				else
				{
					Iterator m_child=CHILDREN.keySet().iterator();
					Vector<String> temp_set=new Vector<String>();
					while(m_child.hasNext())
						temp_set.add((String)m_child.next());
						
					for(int i=0;i<temp_set.size();i++)
					{
						pp_tree_node temp_node=(pp_tree_node)CHILDREN.get(temp_set.get(i));
						for(int j=0;j<temp_set.size();j++)
						{
//This line is a no-mark for "change pattern"
//							if(j!=i)
								temp_node.path_build(temp_set.get(j));
						}
					}
					result=true;
				}			
			}
			else
			{				
				all_child=CHILDREN.keySet().iterator();
//				Vector<String> remove_list=new Vector<String>();
				result=true;
//				result=false;
				while(all_child.hasNext())
				{
					String temp_key=(String)all_child.next();
					CHILDREN.get(temp_key).extend_all_leaves();
//					boolean temp_result=CHILDREN.get(temp_key).extend_all_leaves();
//					if(!temp_result)
//						remove_list.add(temp_key);
//					result=result  || temp_result ;
				}
//				for(int i=0;i<remove_list.size();i++)
//					CHILDREN.remove((String)remove_list.get(i));
				if(CHILDREN.size()==0)
					result=false;
//				if(!result)
//				{
//					for(int i=0;i<remove_list.size();i++)
//						System.out.print(remove_list.get(i)+",");
//				}
			}
		}
		catch(Exception e)
		{
			System.out.println("pp_tree_node extend_all_leaves exception:"+e);
		}
		return result;
	}

	//This is written for CBS old version.
	public boolean extend_all_leaves_without_repeat()
	{
		//Just extends all leaves (There is no deletion in this action)
		//If the node is level2 node , we just extends all its leaves
		boolean result=true;
		try
		{
			Iterator all_child=CHILDREN.keySet().iterator();
			boolean level2_flg=true;
			while(all_child.hasNext())
			{
				pp_tree_node temp_node=(pp_tree_node)CHILDREN.get(all_child.next());
				level2_flg=level2_flg&&temp_node.is_leaf();
			}
			if(level2_flg)
			{
				if(CHILDREN.size()<=1)	//this is for prunning ABC+ABC=>ABCC
					result=false;
				else
				{
					Iterator m_child=CHILDREN.keySet().iterator();
					Vector<String> temp_set=new Vector<String>();
					while(m_child.hasNext())
						temp_set.add((String)m_child.next());
						
					for(int i=0;i<temp_set.size();i++)
					{
						pp_tree_node temp_node=(pp_tree_node)CHILDREN.get(temp_set.get(i));
						for(int j=0;j<temp_set.size();j++)
						{
							if(j!=i)
								temp_node.path_build(temp_set.get(j));
						}
					}
					result=true;
				}			
			}
			else
			{				
				all_child=CHILDREN.keySet().iterator();
				Vector<String> remove_list=new Vector<String>();
//				result=true;
				result=false;
				while(all_child.hasNext())
				{
					String temp_key=(String)all_child.next();
//					CHILDREN.get(temp_key).extend_all_leaves_without_repeat();
					boolean temp_result=CHILDREN.get(temp_key).extend_all_leaves_without_repeat();
					if(!temp_result)
						remove_list.add(temp_key);
					result=result  || temp_result ;
				}
				for(int i=0;i<remove_list.size();i++)
					CHILDREN.remove((String)remove_list.get(i));
				if(CHILDREN.size()==0)
					result=false;
//				if(!result)
//				{
//					for(int i=0;i<remove_list.size();i++)
//						System.out.print(remove_list.get(i)+",");
//				}
			}
		}
		catch(Exception e)
		{
			System.out.println("pp_tree_node extend_all_leaves_without_repeat exception:"+e);
		}
		return result;
	}	
	
	public void clear_all_count()
	{
		try
		{
			this.COUNT=0;
			if(!is_leaf())
			{
				Iterator ir=CHILDREN.keySet().iterator();
				while(ir.hasNext())
				{
					((pp_tree_node)CHILDREN.get((String)ir.next())).clear_all_count();
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("pp_tree_node clear_all_count exception:"+e);
		}
	}
	
	//for build a tree
	public int add_all(Vector<Vector<String>> a)
	{
		int success_count=0;
		try
		{
			for(int i=0;i<a.size();i++)
			{
				if(path_build((Vector<String>)a.get(i)))
					success_count++;
			}
		}
		catch(Exception e)
		{
			System.out.println("pp_tree_node add_all exception:"+e);
		}
		return success_count;
	}
	
	public void remove_all()
	{
		try
		{
			PARENT=null;
			CONTENT="";
			COUNT=0;
			CHILDREN.clear();
		}
		catch(Exception e)
		{
			System.out.println("pp_tree_node remove_all exception:"+e);
		}
	}

	public boolean pattern_trace_for_sn_without_just(Vector<String> a, int index)
	{
		//in this step , we can pass elements of "a" , we don't need got full match. But, a must include the subsequence which absolutely match with the path to leaves.
		try
		{			
			if(SN_set.size()!=0 && index!=a.size())
			{
				return true;
			}
			boolean result=false;
			Vector<String> match_list=new Vector<String>();
			for(int i=index;i<a.size();i++)
			{
				String temp=(String)a.get(i);
				if(match_list.contains(temp))
					continue;
				if(CHILDREN.containsKey(temp))
				{
					boolean temp_b=((pp_tree_node)CHILDREN.get(temp)).pattern_trace_for_sn_without_just(a,(i+1));
					if(temp_b)
						match_list.add(temp);
					result=result||temp_b;
				}
			}
			return result;
		}
		catch(Exception e)
		{
			System.out.println("pp_tree_node pattern_trace_for_sn_without_just exception:"+e);			
		}
		return false;
	}
}
