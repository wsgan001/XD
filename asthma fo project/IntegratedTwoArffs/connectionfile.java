
package IntegratedTwoArffs;
	
import java.lang.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.io.*;


class connectionfile{
	
	


		String FileName;	// SQL 語法文字檔路徑
  	
  		String line;
  		BufferedReader br;	
	
		String QuerySQL;	// 儲存SQL查詢語言
		String DatabaseName;			
	

		//------------------------------
		// 取得資料庫內容
		FastVector dbData = new FastVector();	// 動態陣列 // 記錄「資料庫內容」
		FastVector attriData = new FastVector();	// 動態陣列 // 記錄「所有的屬性名稱」
	
	
		String tablename;		// 儲存「此次處理的table name」	
		String[] attribute;		// 「資料庫屬性名稱」(儲存「由attriData轉換而來的一維字串資料」)		
		String[][] data;		// 儲存「由dbData轉換而來的二維字串資料」
		
		
		FastVector attri_lines = new FastVector();		// 儲存「完整的attribute info」
		
		
		
	

	public connectionfile(String filename){

  		
  			//----------------------------
  			// 參數設定
  			this.FileName = filename;

			
			// System.out.println(" attri_lines.size() = "+attri_lines.size());
		
		try{
				
			//----------------------------
			// 01.
			// 讀取「指定的Arff檔」
			Load_ArffFile();
			
			
			
			//----------------------------
			// 02.
			// 處理「資料內容」
			Process_attribute();			
			
			
		
			//----------------------------
			// 03.
			// 處理「資料內容」
			Process_dbData();
			



			
		
		}catch(Exception e){
			System.out.println(" Error about connecting the database. (in connectionfile.java)" + e.toString());	
		}
		
	}
	


//-------------------------------------------------
//-------------------------------------------------
//-------------------------------------------------


	public String[] getAttribute(){			 // 取出「欄位屬性名稱」
		return attribute;	
	}
		
	public String[][] getData(){			 // 取出「資料庫內容」
		return data;	
	}

	public String getTableName(){			 // 取出「資料名稱」
		return tablename;	
	}
	
	public FastVector getAttriLines(){		// 取出「完整的attribute information」
		return  attri_lines;
	}
		

//-------------------------------------------------
//-------------------------------------------------
//-------------------------------------------------


	// 連結資料文字檔！
	public void Process_attribute(){
		
		try{
			
			attribute = new String[attriData.size()];
			
			//System.out.println(" 資料表名稱: "+tablename );
			//System.out.println(" 欄位名稱數量: "+ attribute.length);
						
			
			for(int i=0;i<attriData.size();i++){
				attribute[i] = attriData.elementAt(i);					
			}
		
		
			// 列印資料！
			/*
			for(int i=0;i<attribute.length;i++){				
				System.out.println(attribute[i]);
			}
			*/
		
		
  		}catch(Exception e){
  			System.out.println(" Error about process all the info : "+e.toString());
  		}	
  								
	}

	
	// 連結資料文字檔！
	public void Process_dbData(){
		
		try{
			
			
			data = new String[dbData.size()][];
			//System.out.println(" 資料筆數: "+dbData.size());
			
			for(int i=0;i<dbData.size();i++){
				data[i] = dbData.elementAt(i).split(",");					
			}
			
			
			
			
			System.out.println(" 更新的資料筆數: "+data.length);		
		
			// 列印資料！
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
 	
 	
 	
	// 連結資料文字檔！
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
 				
 				
 				// 避免為了取出字串超過字串索引邊界
 				
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
 					System.out.println(" table name = "+str);
 					tablename = str;
 					pos++;
 					continue;			
				}


				if(check_str_2.compareTo("@attribute")==0){
 					String tmp_line = line;
 					// System.out.println(" attribute line = "+line);
 					attri_lines.addElement(tmp_line);
 					
 					String str = getName(line, 10);
 					System.out.println(" attribute name = "+str);
 					 					
 					attriData.addElement(str);
 					pos++;

				}



				// 當flag==true時，將「@data後的每一筆記錄存入dbData」
				if(flag == true){
					//System.out.println(" line = "+line);
					dbData.addElement(line);
					
					
				}


 				// 取資料內容!
 				if(check_str_3.compareTo("@data")==0){
 					System.out.println(" @data = "+line);
 					// 避免將"@data"存入資料裡
 					flag=true;
 				}
 			
         		}
         		
         		

         		br.close();


			System.out.println(" dbData.size() = "+dbData.size());
         		         		      		
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
		
		// 回傳資訊
		return str;
	}

}



