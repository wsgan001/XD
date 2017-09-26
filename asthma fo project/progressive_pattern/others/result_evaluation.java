package progressive_pattern;
import java.io.*;

public class result_evaluation
{
	HashMap<String,Integer> SCORE_TABLE=new HashMap<String,Integer>();
	public result_evaluation(String s)
	{
		parse(s);
	}
	private void parse(String s)
	{
		try
		{
		}
		catch(Exception e)
		{
			System.out.println("result_evaluation parse exception:"+e);
		}
	}

	public result evaluation(HashMap<String,Integer> s)
	{
		SCORE_TABLE.putAll(s);
	}

	public void all_class_report()
	{
		try
		{
		}
		catch(Exception e)
		{
			System.out.println("result_evaluation all_class_report exception:"+e);
		}
	}

	public static void main(String args[])
	{
		result_evaluation a=new result_evaluation(args[0]);
	}
}
