package data_format;
import java.io.*;

public class training_balance
{
	public static void main(String args[])
	{
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(args[0]));
			BufferedWriter bw=new BufferedWriter(new FileWriter("temp_training.tmp"));
			String buffer="";
			String class_label="";
			String temp_instance="";
			while(true)
			{
				buffer=br.readLine();
				if(buffer==null || buffer.trim().equals(""))
				{
					//System.out.println(temp_instance);
					if(class_label.equals("1:"))
					{
						for(int i=0;i<20;i++)
						{
							bw.write(temp_instance+"\n");
						}
					}
					else
						bw.write(temp_instance+"\n");

					if(buffer==null)
						break;
					temp_instance="";
					class_label="";
				}
				else
				{
					if(buffer.indexOf("1:")==0)
						class_label="1:";
					else if(buffer.indexOf("0:")==0)
						class_label="0:";
					temp_instance+=buffer+"\n";
				}
			}
			bw.close();
			br.close();

			//remove
			(new File(args[0])).delete();
			//rename
			(new File("temp_training.tmp")).renameTo(new File(args[0]));
			//	System.out.println("Kerker");
		}
		catch(Exception e)
		{
			System.out.println("training_balance exception:"+e);
		}
	}
}
