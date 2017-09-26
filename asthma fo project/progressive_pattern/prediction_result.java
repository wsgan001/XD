package progressive_pattern;
import java.io.*;
import java.util.*;

public class prediction_result
{
	public HashMap<String,Integer> CONFUSION_MATRIX;
	public int COUNT=0;
	public int CORRECT=0;
	public prediction_result(HashMap<String,Integer> co)
	{
		CONFUSION_MATRIX=co;
	}

	public double get_accuracy()
	{
		if(COUNT!=0)
			return CORRECT*100.0/(double)COUNT;
		else
			return 0.0;
	}

	public String toString()
	{
		String result="Accuracy:"+get_accuracy()+" CONFUSION MATRIX:"+CONFUSION_MATRIX+" ,RECALL:"+list_each_recall()+" ,HIT RATE:"+hit_rate();
		return result;
	}

	public String list_each_recall()
	{
		String result="";
		try
		{
			Vector<String> class_set=new Vector<String>();
			HashMap<String,Integer> SUM_SET=new HashMap<String,Integer>();
			HashMap<String,Integer> SUB_SET=new HashMap<String,Integer>();
			Iterator ir=CONFUSION_MATRIX.keySet().iterator();
			while(ir.hasNext())
			{
				String item_name=(String)ir.next();
				int score=(CONFUSION_MATRIX.get(item_name)).intValue();
				String class_label=item_name.substring(item_name.indexOf("C")+1,item_name.indexOf("/"));
				if(!class_set.contains(class_label))
					class_set.add(class_label);
				String predicted_class=item_name.substring(item_name.indexOf("P")+1);
				if(predicted_class.equals(class_label))
				{
					if(SUB_SET.containsKey(class_label))
					{
						int temp_v=score+(SUB_SET.get(class_label)).intValue();
						SUB_SET.put(class_label,new Integer(temp_v));
					}
					else
						SUB_SET.put(class_label,new Integer(score));
				}
				if(SUM_SET.containsKey(class_label))
				{
					int temp_v=score+(SUM_SET.get(class_label)).intValue();
					SUM_SET.put(class_label,new Integer(temp_v));
				}
				else
					SUM_SET.put(class_label,new Integer(score));
			}

			for(int i=0;i<class_set.size();i++)
			{
				if(SUM_SET.get(class_set.get(i))!=null && SUB_SET.get(class_set.get(i))!=null)
				{
					int temp_sum=(SUM_SET.get(class_set.get(i))).intValue();
					int temp_sub=(SUB_SET.get(class_set.get(i))).intValue();
					result=result+" Class"+class_set.get(i)+":"+(temp_sub/(double)temp_sum)+",";
				}
				else
					result=result+" Class"+class_set.get(i)+":?,";
			}
		}
		catch(Exception e)
		{
			System.out.println("prediction_result list_each_recall exception:"+e);
		}
		return result;
	}

	public double get_recall(String class_label)
	{
		int SUM=0;
		int SUB=0;
		try
		{
			Iterator ir=CONFUSION_MATRIX.keySet().iterator();
			while(ir.hasNext())
			{
				String item_name=(String)ir.next();
				int score=(CONFUSION_MATRIX.get(item_name)).intValue();
				if(item_name.indexOf("C"+class_label)>=0)
				{
					String predicted_class=item_name.substring(item_name.indexOf("P")+1);
					if(predicted_class.equals(class_label))
						SUB+=score;
					SUM+=score;
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("prediction_result get_recall exception:"+e);
		}
		return (double)SUB/(double)SUM;
	}

	public double hit_rate()
	{
		int SUM=0;
		//int SUB=0;
		try
		{
			Iterator ir=CONFUSION_MATRIX.keySet().iterator();
			while(ir.hasNext())
			{
				String item_name=(String)ir.next();
				int score=(CONFUSION_MATRIX.get(item_name)).intValue();
				String class_label=item_name.substring(item_name.indexOf("C")+1,item_name.indexOf("/"));
				String predicted_class=item_name.substring(item_name.indexOf("P")+1);
				//if(predicted_class.equals(class_label))
				//	SUB+=score;
				SUM+=score;
			}
		}
		catch(Exception e)
		{
			System.out.println("prediction_result hit_rate exception:"+e);
		}
		return (double)SUM/(double)COUNT;
	}
}
