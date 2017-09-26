package data_format;
import java.io.*;

public class cb
{
	public static void main(String args[])
	{
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(args[0]));
			BufferedReader br2=new BufferedReader(new FileReader(args[1]));
			BufferedWriter bw=new BufferedWriter(new FileWriter(args[2]));
			String buffer="";
			while((buffer=br.readLine())!=null)
			{
				bw.write(buffer+"\n");
			}
			while((buffer=br2.readLine())!=null)
			{
				bw.write(buffer+"\n");
			}
			br.close();
			br2.close();
			bw.close();
		}
		catch(Exception e)
		{
			System.out.println("cb exception:"+e);
		}
	}
}
