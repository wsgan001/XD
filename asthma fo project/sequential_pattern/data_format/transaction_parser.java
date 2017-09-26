package data_format;
import java.io.*;
import java.util.Vector;

public class transaction_parser
{	
	//[class]:[sequence1];[sequence2];...;[sequenceM];
	//[sequencex]= v1,v2,v3,...,vN,
	public static mv_transaction class_msequence_parser(String content)
	{
		mv_transaction result=new mv_transaction();
		//System.out.println(content);
		try
		{
			result.CLASS_LABEL=content.substring(0,content.indexOf(":")).trim();
			content=content.substring(content.indexOf(":")+1);
			Vector<Vector<String>> temp_itemset=new Vector<Vector<String>>();
			while(content.length()>0)
			{
				if(content.indexOf(";")>0)
				{
					transaction tt=transaction_parser.sequence_parser(content.substring(0,content.indexOf(";")));
					temp_itemset.add(tt.ITEM_SET);
				}
				else
				{
					//if there is only one sequence
					transaction tt=transaction_parser.sequence_parser(content);
					temp_itemset.add(tt.ITEM_SET);
					break;
				}
				content=content.substring(content.indexOf(";")+1);
			}
			result.ITEM_SET=temp_itemset;
		}
		catch(Exception e)
		{
			System.out.println("transaction_parser class_msequence_parser exception:"+e);
			System.out.println(content);
		}
		return result;
	}
	
	//[uid]:[class]:[itemset]
	public static transaction user_class_sequence_parser(String content)
	{
		Vector<String> sequence=new Vector<String>();
		String class_label="";
		String user_id="";
		transaction result;
		try
		{
			int first_break=content.indexOf(":");
			int second_break=content.indexOf(":");
			class_label=content.substring(0,first_break);
			user_id=content.substring(first_break+1,second_break);
			result=sequence_parser(content.substring(second_break+1));
			result.CLASS_LABEL=class_label;
			result.USER_ID=user_id;
		}
		catch(Exception e)
		{
			System.out.println("transaction parser user_class_sequence exception:"+e);
			return  null;
		}
		return result;
	}
	
	public static transaction auto_parser(String content) //just for CS, S, CSS, and SS four types
	{
		try
		{
			int first_break=content.indexOf(":");
			int last_break=content.lastIndexOf(":");
			int first_separator=content.indexOf(",");
			
			if(first_separator==-1)
			{
				//just one item
				if(first_break==last_break)
				{
					//Maybe CS or SS, but generally CS have ","
					return sequence_score_parser(content);
				}
				else
				{
					//System.out.println("CSS");
					return class_sequence_score_parser(content);
				}
			}
			else if(first_break==last_break)
			{
				if(first_break==-1)
				{
//					System.out.println("S");
					return sequence_parser(content);
				}
				else if(first_break<first_separator)
				{
//					System.out.println("CS");
					return class_sequence_parser(content);
				}
				else if(first_break>first_separator)
				{
//					System.out.println("SS");
					return sequence_score_parser(content);
				}
			}
			else if(first_break>first_separator && first_separator>last_break)
			{
				//System.out.println("CSS");
				return class_sequence_score_parser(content);
			}
		}
		catch(Exception e)
		{
			System.out.println("transaction parser auto_parser exception:"+e);
		}
		return null;
	}
	
	public static transaction basic_parser(String content,String type)
	{
		try
		{
			if(type.equals("CS"))
				return class_sequence_parser(content);
			else if(type.equals("S"))
				return sequence_parser(content);
			else if(type.equals("CSS"))
				return class_sequence_score_parser(content);
			else if(type.equals("SS"))
				return sequence_score_parser(content);
		}
		catch(Exception e)
		{
			System.out.println("transaction_parser basic_parser exception:"+e);
		}
		return null;
	}
	
// parser 1  , for time-series with class label  [format]     CLASS_LABEL:V1,V2,V3,...,Vn
	public static transaction class_sequence_parser(String content)
	{
		Vector<String> result=new Vector<String>();
		String class_label="";
		try
		{
			if(content.indexOf(":")>=0)
			{
				class_label=content.substring(0,content.indexOf(":"));
				content=content.substring(content.indexOf(":")+1);
			}
			if(content.indexOf(",")>=0)
			{
				String[] all_item=content.split(",");
				for(int i=0;i<all_item.length;i++)
					result.add(all_item[i]);
			}
		}
		catch(Exception e)
		{
			System.out.println("transaction_parser class_sequence_parser exception:"+e);
		}
		return (new transaction(result,class_label));
	}

//parser 2 , fot pure time-series        [format]  V1,V2,V3,...,Vn
	public static transaction sequence_parser(String content)
	{
		Vector<String> result=new Vector<String>();
		try
		{
			while(content.indexOf(",")>=0)
			{
				result.add(content.substring(0,content.indexOf(",")));
				content=content.substring(content.indexOf(",")+1);
			}
			if(content.length()!=0)
				result.add(content);			
		}
		catch(Exception e)
		{
			System.out.println("transaction_parser sequence_parser exception:"+e);
		}
		return (new transaction(result));
	}


// parser 3  , for time-series with class label and Score(support)  [format]     CLASS_LABEL:V1,V2,V3,...,Vn:Score
	public static transaction class_sequence_score_parser(String content)
	{
		Vector<String> result=new Vector<String>();
		String class_label=new String();
		double score=0.0;
		try
		{
			if(content.indexOf(":")>=0)
			{
				class_label=content.substring(0,content.indexOf(":"));
				content=content.substring(content.indexOf(":")+1);
			}
			if(content.indexOf(":")>=0)
			{
				score=Double.parseDouble(content.substring(content.indexOf(":")+1));
				content=content.substring(0,content.indexOf(":"));
			}
			while(content.indexOf(",")>=0)
			{
				result.add(content.substring(0,content.indexOf(",")));
				content=content.substring(content.indexOf(",")+1);
			}
			if(content.length()!=0)
				result.add(content);
		}
		catch(Exception e)
		{
			System.out.println("transaction_parser class_sequence_score_parser exception:"+e);
		}
		return (new transaction(result,class_label,score));
	}
	
// parser 4  , for time-series with Score(support)  [format]     V1,V2,V3,...,Vn:Score
	public static transaction sequence_score_parser(String content)
	{
		Vector<String> result=new Vector<String>();
		double score=0.0;
		try
		{
			if(content.indexOf(":")>=0)
			{
				score=Double.parseDouble(content.substring(content.indexOf(":")+1));
				content=content.substring(0,content.indexOf(":"));
			}
			while(content.indexOf(",")>=0)
			{
				result.add(content.substring(0,content.indexOf(",")));
				content=content.substring(content.indexOf(",")+1);
			}
			if(content.length()!=0)
				result.add(content);
		}
		catch(Exception e)
		{
			System.out.println("transaction_parser sequence_score_parser exception:"+e);
			System.out.println(content);
		}
		return (new transaction(result,score));
	}
	
// parser 5 , for time-series with mt class and sequence [format]  C1:C2:C3...:V1,V2,V3...,Vn
/*
	public static transaction mt_class_sequence_parser(String content)
	{
		Vector<String> result=new Vector<String>();
		try
		{
		}
		catch(Exception e)
		{
			System.out.println("transaction_parser mt_class_sequence_parser exception:"+e);
		}
	}
*/
//Test part=============================================================	
	public static void main(String args[])
	{
		transaction t=transaction_parser.class_sequence_parser("1:0,0,9,77,5,");
		Vector<String> r=t.ITEM_SET;
		for(int i=0;i<r.size();i++)
		{
			System.out.println(r.get(i));
		}
		System.out.println(t.CLASS_LABEL);
	}
}
