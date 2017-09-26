package Classifier;

	
import java.lang.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.io.*;


class load_combined_arff_file{
	
	


		String FileName;	// SQL �y�k��r�ɸ��|
  	
  		String line;
  		BufferedReader br;	
	
		String QuerySQL;	// �x�sSQL�d�߻y��
		String DatabaseName;			
	

		//------------------------------
		// ���o��Ʈw���e
		FastVector dbData = new FastVector();	// �ʺA�}�C // �O���u��Ʈw���e�v
		FastVector attriData = new FastVector();	// �ʺA�}�C // �O���u�Ҧ����ݩʦW�١v
	
	
		String tablename;		// �x�s�u�����B�z��table name�v	
		String[] attribute;		// �u��Ʈw�ݩʦW�١v(�x�s�u��attriData�ഫ�ӨӪ��@���r���ơv)		
		String[][] data;		// �x�s�u��dbData�ഫ�ӨӪ��G���r���ơv
		
		
		
		FastVector attri_lines = new FastVector();		// �x�s�u���㪺attribute info�v
		
		
		
	

	public load_combined_arff_file(String filename){

  		
  			//----------------------------
  			// �ѼƳ]�w
  			this.FileName = filename;

			
			// System.out.println(" attri_lines.size() = "+attri_lines.size());
		
		try{
				
			//----------------------------
			// 01.
			// Ū���u���w��Arff�ɡv
			Load_ArffFile();
			
			
			
			//----------------------------
			// 02.
			// �B�z�u��Ƥ��e�v
			Process_attribute();			
			
			
		
			//----------------------------
			// 03.
			// �B�z�u��Ƥ��e�v
			Process_dbData();
			



			
		
		}catch(Exception e){
			System.out.println(" Error about connecting the database. (in connectionfile.java)" + e.toString());	
		}
		
	}
	


//-------------------------------------------------
//-------------------------------------------------
//-------------------------------------------------


	public String[] getAttribute(){			 // ���X�u����ݩʦW�١v
		return attribute;	
	}
		
	public String[][] getData(){			 // ���X�u��Ʈw���e�v
		return data;	
	}

	public String getTableName(){			 // ���X�u��ƦW�١v
		return tablename;	
	}
	
	public FastVector getAttriLines(){		// ���X�u���㪺attribute information�v
		return  attri_lines;
	}
	public FastVector getDataLines(){		// ���X�u���㪺 data information�v
		return dbData;
	}

	public FastVector getdbData(){
		return dbData;	
	}

//-------------------------------------------------
//-------------------------------------------------
//-------------------------------------------------


	// �s����Ƥ�r�ɡI
	public void Process_attribute(){
		
		try{
			
			attribute = new String[attriData.size()];
			
			//System.out.println(" ��ƪ��W��: "+tablename );
			//System.out.println(" ���W�ټƶq: "+ attribute.length);
						
			
			for(int i=0;i<attriData.size();i++){
				attribute[i] = attriData.elementAt(i);					
			}
		
		
			// �C�L��ơI
			/*
			for(int i=0;i<attribute.length;i++){				
				System.out.println(attribute[i]);
			}
			*/
		
		
  		}catch(Exception e){
  			System.out.println(" Error about process all the info : "+e.toString());
  		}	
  								
	}

	
	// �s����Ƥ�r�ɡI
	public void Process_dbData(){
		
		try{
			
			
			data = new String[dbData.size()][];
			//System.out.println(" ��Ƶ���: "+dbData.size());
			
			for(int i=0;i<dbData.size();i++){
				data[i] = dbData.elementAt(i).split(",");					
			}
			
			
			
			
			System.out.println(" ��s����Ƶ���: "+data.length);		
		
			// �C�L��ơI
			/*
			for(int i=0;i<data.length;i++){
				for(int j=0;j<data[i].length;j++){
					System.out.print(data[i][j]+" ");	
				}System.out.println();
			}
			*/
										
  		}catch(Exception e){
  			System.out.println(" Error about process all the info : "+e.toString());
  		}	
  		
	}
 	
 	
 	
	// �s����Ƥ�r�ɡI
	public void Load_ArffFile(){
  		
  		try{
  		
  		
  			// System.out.println(FileName+".arff");
  			dbData = new FastVector();
  			
         		//br = new BufferedReader(new FileReader(FileName+".arff"));         	         		
         		br = new BufferedReader(new FileReader(FileName));
         		int pos=0;         		
         		int count =0;         		
         		         		

         		boolean flag =false ;
         		

		
         		while ((line=br.readLine())!=null){
 				//System.out.println(line);
 				
 				if(line.length()==0 || line=="")
 					continue;


 				String check_str_1 ="";	// check "@relation"
 				String check_str_2 ="";	// check "@attribute"
 				String check_str_3 ="";	// check "@data"
 				
 				
 				// �קK���F���X�r��W�L�r��������
 				
 				if(line.charAt(0)=='@'){
 					if(line.length()>9){
 					
 						for(int i=0;i<9;i++){
 							check_str_1 += line.charAt(i);
						}
				
						for(int i=0;i<10;i++){
 							check_str_2 += line.charAt(i);
						}
					}
				
				
					for(int i=0;i<5;i++){
 						check_str_3 += line.charAt(i);
					}
				}
				
				
				//System.out.println(" check_str_1 = "+check_str_1+"|| check_str_2 = "+check_str_2 +"|| check_str_3 = "+check_str_3);


				if(check_str_1.compareTo("@relation")==0){					
					String str = getName(line, 9);
 					//System.out.println(" table name = "+str);
 					tablename = str;
 					pos++;
 					continue;			
				}


				if(check_str_2.compareTo("@attribute")==0){
 					String tmp_line = line;
 					// System.out.println(" attribute line = "+line);
 					attri_lines.addElement(tmp_line);
 					
 					String str = getName(line, 10);
 					//System.out.println(" attribute name = "+str);
 					 					
 					attriData.addElement(str);
 					pos++;

				}



				// ��flag==true�ɡA�N�u@data�᪺�C�@���O���s�JdbData�v
				if(flag == true){
					//System.out.println(" line = "+line);
					dbData.addElement(line);
					
					
				}


 				// ����Ƥ��e!
 				if(check_str_3.compareTo("@data")==0){
 					//System.out.println(" @data = "+line);
 					// �קK�N"@data"�s�J��Ƹ�
 					flag=true;
 				}
 			
         		}


         		br.close();

			System.out.println(" dbData.size() = "+dbData.size());
			System.out.println(" attri_lines.size() = "+attri_lines.size());
         		       		      		
  		}catch(Exception e){
  			System.out.println(" Error about conntecting the database : "+e.toString());
  		}	
  		
  	}
	
	
	public String getName(String line_str, int p){
		
		String str="";
		
		try{	
			//System.out.println("=============================");
			//System.out.println(" before str = "+str);
 			
 			String[] tmp = line.split(" ");
 			str=tmp[1];
 			
 			/*
 			System.out.println(" str = "+str);
 				 
 			for(int j=0;j<tmp.length;j++){
 				System.out.println(" tmp["+j+"] = "+tmp[j]);	
 			} 
 			
 			System.out.println(" after str = "+str);
 			System.out.println("=============================");
			*/
 		}	
		catch(Exception e){
			System.out.println(" Error about getting name info: "+e.toString());	
		}		
		
		// �^�Ǹ�T
		return str;
	}

}


