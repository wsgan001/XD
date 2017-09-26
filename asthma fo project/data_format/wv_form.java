package data_format;
import java.io.*;
import java.util.Vector;

public class wv_form
{
	public static void main(String args[])
	{
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(args[0]));
			BufferedWriter bw=new BufferedWriter(new FileWriter(args[1]));
			String buffer="";
			while((buffer=br.readLine())!=null)
			{
				transaction temp_tran=transaction_parser.auto_parser(buffer);
				Vector is=temp_tran.ITEM_SET;
				bw.write(is.get(is.size()-1)+":");
				for(int i=0;i<is.size()-1;i++)
				{
					bw.write(is.get(i)+",");
				}
				bw.write("\n");
			}
			bw.close();
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("wv_form exception:"+e);
		}
	}
}
