package data_format;
import java.io.*;
import java.util.Vector;

public class data_segment {

	/**
	 * @param args
	 */
	public String FILENAME="";
	public data_segment(String f)
	{
		FILENAME=f;
	}
	
	public void segment_with_sw(int ws,int ss,String of)
	{
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(FILENAME));
			BufferedWriter bw=new BufferedWriter(new FileWriter(of));
			String buffer="";
			while((buffer=br.readLine())!=null)
			{
				transaction temp_tran=transaction_parser.basic_parser(buffer,"CS");
				Vector<String> all_item=temp_tran.ITEM_SET;
				for(int i=0;i==0 || (i+ws)<all_item.size();i+=ss)
				{
					bw.write(temp_tran.CLASS_LABEL+":");
					for(int j=i;j<i+ws && j<all_item.size();j++)
					{
						bw.write(all_item.get(j)+",");
					}
					bw.write("\n");
				}
			}
			br.close();
			bw.close();
		}
		catch(Exception e)
		{
			System.out.println("data_segment segment_with_sw exception:"+e);
		}
	}
	
	
	public static void main(String[] args)
	{
		if(args.length==0)
		{
			System.out.println("data_segment [input_filename] [window_size] [step_size] [output_file]");
		}
		else if(args.length==4)
		{
			data_segment a=new data_segment(args[0]);
			a.segment_with_sw(Integer.parseInt(args[1]), Integer.parseInt(args[2]), args[3]);
		}
	}

}
