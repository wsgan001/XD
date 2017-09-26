package Health_Sys;

import java.lang.*;
import java.util.*;
import java.io.*;



public class load_parameters{
	
	
	Vector<String> all_parameters = new Vector<String>();
	
	
	public load_parameters(){
		
		try{	
		
			BufferedReader br = new BufferedReader(new FileReader("./input_parameters.ini"));
  			String line="";
  			
  			
        		while ((line=br.readLine())!=null){
     					
     				all_parameters.add(line);
             		}

         		br.close();				
			
			//Print();
		
		}catch(Exception e){
			System.out.println(" Error about loading parameters (in HAS.java): "+e.toString());
		}
		
	}	
	
	
	
//-----------------------------------------------------------
//-----------------------------------------------------------
//-----------------------------------------------------------

	public Vector<String> getParameters(){
		return 	all_parameters;
	}
	
//-----------------------------------------------------------
//-----------------------------------------------------------
//-----------------------------------------------------------
	
	
	

	public void Print(){
		
		try{
		
			System.out.println("=======================================");
			System.out.println("		Parameters Print");
			
			for(int i=0;i<all_parameters.size();i++)
			{
				String para =  all_parameters.get(i);
				System.out.println(" Parameter "+(i+1)+": "+para);
			}
		
			System.out.println("=======================================");
		
		
		}catch(Exception e){
			System.out.println(" Error about printing all the parameters (in HAS.java): "+e.toString());
		}
		
		
	}	
	
	
}

