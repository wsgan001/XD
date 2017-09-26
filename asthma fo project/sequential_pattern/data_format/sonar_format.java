package data_format;
import java.io.*;

public class sonar_format
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
				transaction temp_t=transaction_parser.auto_parser(buffer);
				String class_label=(String)temp_t.ITEM_SET.get(temp_t.ITEM_SET.size()-1);
				if(class_label.equals("R"))
					bw.write("0:");
				else
					bw.write("1:");
				for(int i=0;i<temp_t.ITEM_SET.size()-1;i++)
					bw.write(temp_t.ITEM_SET.get(i)+",");
				bw.write("\n");
			}
			br.close();
			bw.close();
		}
		catch(Exception e)
		{
			System.out.println("sonar_format exception:"+e);
		}
	}
}
