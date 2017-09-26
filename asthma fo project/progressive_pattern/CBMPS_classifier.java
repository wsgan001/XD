package progressive_pattern;
import java.io.*;
import java.util.*;

//Input Multivariate Progressive Pattern File (composite_mpp.MPP_filename) than building a tree based pattern storage for classification.
//Input Feature Transformed File (composite_mpp.transformed_file) to evaluate its accuracy with the tree-based classifier.

public class CBMPS_classifier
{
	public String MPP_FILENAME="";
	public VP_node ROOT=null;
	public boolean DETAIL_DISPLAY=false;

	public CBMPS_classifier(String pf)
	{
		MPP_FILENAME=pf;
		ROOT=classifier_building();
	}

	public VP_node classifier_building()
	{
		VP_node result=new VP_node();
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(MPP_FILENAME));
			String buffer="";
			String label="#Large"; //Default read all pattern
			while((buffer=br.readLine())!=null)
			{
				if(buffer.indexOf("#")==0)
					label=buffer;
				else
				{
					if(label.indexOf("#Large")>=0)
					{
						mpp temp_pattern=new mpp(buffer);
						result.path_build_with_support_and_class_score(temp_pattern.ITEM_SET,temp_pattern.SUPPORT,temp_pattern.SCORE_TABLE);
					}
				}
			}
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("classifier_building exception:"+e);
		}
		return result;
	}

/*	public String prediction(String content) 
	{
		return prediction(new mp_transaction(content));
	}*/

	public String prediction(mp_transaction c)
	{
		String result="";
		try
		{
			HashMap<String,Double> st=new HashMap<String,Double>();
			ROOT.pattern_trace_for_class_score(c,0,st);
			result=get_highest_score_class(st);
			if(DETAIL_DISPLAY)
				System.out.println("SCORE_TABLE:"+st);
		}
		catch(Exception e)
		{
			System.out.println("CBMPS_classifier prediction exception:"+e);
		}
		return result;
	}

	public prediction_result batch_prediction(String filename)
	{
		HashMap<String,Integer> confusion_matrix=new HashMap<String,Integer>();
		prediction_result result=new prediction_result(confusion_matrix);
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(filename));
			String buffer="";
			while((buffer=br.readLine())!=null)
			{
				if(DETAIL_DISPLAY)
					System.out.println(buffer);
				mp_transaction temp_tran=new mp_transaction(buffer);
				String pr=prediction(temp_tran);
				String pr_report="C"+temp_tran.CLASS_LABEL+"/P"+pr;   // Ex. C1/P2  = class 1 is predicted as class 2.
				if(DETAIL_DISPLAY)
					System.out.println("=>"+pr_report);
				if(pr.equals(temp_tran.CLASS_LABEL))
					result.CORRECT++;

				if(confusion_matrix.containsKey(pr_report))
				{
					int temp_c=((Integer)confusion_matrix.get(pr_report)).intValue()+1;
					confusion_matrix.put(pr_report,new Integer(temp_c));
				}
				else
					confusion_matrix.put(pr_report,new Integer(1));				
				result.COUNT++;
			}
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("CBMPS_classifier batch_prediction exception:"+e);
		}
		return result;
	}

	private String get_highest_score_class(HashMap<String,Double> st)
	{
		String result="";
		try
		{
			double max=-1.0;
			Iterator ir=st.keySet().iterator();
			while(ir.hasNext())
			{
				String temp_class=(String)ir.next();
				double temp_score=((Double)st.get(temp_class)).doubleValue();
				if(temp_score>max)
				{
					max=temp_score;
					result=temp_class;
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("CBMPS_classifier get_highest_score_class exception:"+e);
		}
		return result;
	}
	
	public static void main(String args[])
	{
		//Format: CBMPS_classifier [composite_mpp.MPP_FILENAME] [file1 for prediction,composite_mpp.transformed_file] [file2] ....
		CBMPS_classifier a=new CBMPS_classifier(args[0]);
		for(int i=1;i<args.length;i++)
		{
			prediction_result r=a.batch_prediction(args[i]);
			System.out.println(args[i]+" Accuracy:"+r.get_accuracy()+" "+r.CONFUSION_MATRIX);
		}
	}
}

class mpp
{
	public Vector<String> ITEM_SET=new Vector<String>();
	public double SUPPORT;
	public HashMap<String,Double> SCORE_TABLE=new HashMap<String,Double>();
	public mpp(String content)
	{
		String layer1[]=content.split(":");
		String items[]=layer1[0].split(",");
		for(int i=0;i<items.length;i++)
		{
			if(!items[i].equals(""))
				ITEM_SET.add(items[i]);
		}
		SUPPORT=Double.parseDouble(layer1[1]);
		String class_set=layer1[2];
		class_set=class_set.substring(class_set.indexOf("{")+1,class_set.indexOf("}"));
		String scores[]=class_set.split(",");
		for(int i=0;i<scores.length;i++)
		{
			if(!scores[i].equals(""))
			{
				SCORE_TABLE.put(scores[i].substring(0,scores[i].indexOf("=")).trim(),Double.parseDouble(scores[i].substring(scores[i].indexOf("=")+1)));
			}
		}
	}
}
