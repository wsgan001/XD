package data_format;
import java.io.*;

public class compress_value
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
				bw.write(temp_tran.CLASS_LABEL+":");
				String temp_item="";
				for(int i=0;i<temp_tran.ITEM_SET.size();i++)
				{
					if(!((String)temp_tran.ITEM_SET.get(i)).equals(temp_item))
					{
						bw.write((String)temp_tran.ITEM_SET.get(i)+",");
						temp_item=(String)temp_tran.ITEM_SET.get(i);
					}
				}
				bw.write("\n");
			}
			bw.close();
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("compress value exception:"+e);
		}
	}
}
