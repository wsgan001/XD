package data_format;
import java.io.*;

public class format_changer
{
	public format_changer()
	{
	}
	
	public void arff_to_mine(String s,int ci)
	{
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(s));
			BufferedWriter bw=new BufferedWriter(new FileWriter(s+".min"));
			String buffer="";
			while((buffer=br.readLine())!=null)
			{
				if(buffer.trim().indexOf("@")!=0)
				{
					if(ci>=0)
					{
						transaction tmp_tran=transaction_parser.auto_parser(buffer);
						String tmp_c=(tmp_tran.ITEM_SET.get(ci)).toString();
						tmp_tran.ITEM_SET.remove(ci);
						bw.write(tmp_c+":");
						for(int i=0;i<tmp_tran.ITEM_SET.size();i++)
							bw.write((tmp_tran.ITEM_SET.get(i)).toString()+",");
						bw.write("\n");
					}
					else
					{
						bw.write(buffer+",\n");
					}
				}
			}
			br.close();
			bw.close();
		}
		catch(Exception e)
		{
			System.out.println("format_changer arff_to_mine exception:"+e);
		}
	}

	public static void main(String args[])
	{
		format_changer a=new format_changer();
		a.arff_to_mine(args[0],0);
	}
}
