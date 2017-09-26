import java.io.*;
import java.util.Vector;

public class file_item_extraction
{
	String DATA_DIRECTORY="";
	String PATTERN_DIRECTORY="";
	Vector<String> all_items=new Vector<String>();
	public file_item_extraction(String data_dir,String pattern_dir,String of)
	{
		DATA_DIRECTORY=data_dir;
		PATTERN_DIRECTORY=pattern_dir;
		
		do_extraction(of);
	}
	
	
	private void do_extraction(String output_filename)
	{
		try
		{
			BufferedWriter bw=new BufferedWriter(new FileWriter(output_filename));
			
			bw.close();
		}
		catch(Exception e)
		{
			System.out.println("file_item_extraction do_extraction exception:"+e);
		}
	}
}