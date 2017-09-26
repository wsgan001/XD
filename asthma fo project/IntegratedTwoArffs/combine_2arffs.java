
package IntegratedTwoArffs;
	
import java.lang.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.io.*;


public class combine_2arffs{


	String[] attribute1;			// 取出「欄位屬性名稱」
	String[][] data1;			// 取出「資料庫內容」
	String TableName1;			// 取出「資料名稱」
	FastVector attri_lines1;		// 取出「完整的attribute information」
		

	String[] attribute2;			// 取出「欄位屬性名稱」
	String[][] data2;			// 取出「資料庫內容」
	String TableName2;			// 取出「資料名稱」
	FastVector attri_lines2;		// 取出「完整的attribute information」
	
	
	String[][] combined_data;		// 存放「兩個arff檔結合後的檔案」


	public combine_2arffs(String[] attribute1, String[][] data1, FastVector attri_lines1, String[] attribute2, String[][] data2, FastVector attri_lines2){
		
		try{
		
			this.attribute1 = attribute1;
			this.data1 = data1;
			this.attri_lines1 = attri_lines1;
		
			this.attribute2 = attribute2;
			this.data2 = data2;
			this.attri_lines2 = attri_lines2;
		
		System.out.println("===========================================================");	
			System.out.println(" attri_lnes1.size() = "+ attri_lines1.size());
			System.out.println(" attribute1.length = "+ attribute1.length);
			System.out.println(" data1.length = "+ data1.length);
		System.out.println("===========================================================");	
			System.out.println(" attri_lnes2.size() = "+ attri_lines2.size());
			System.out.println(" attribute2.length = "+ attribute2.length);
			System.out.println(" data2.length = "+ data2.length);
		System.out.println("===========================================================");	
		
		
			combine_data();
		
		
		}catch(Exception e){
			System.out.println(" Error about combining the two arffs files (in combine_2arffs.java) : "+e.toString());	
		}	
		
		
		
	}


//=======================================================================================
//=======================================================================================
//=======================================================================================


	public String[][] getCombinedData(){	// 回傳「 整合後的資料 資訊」
		return combined_data;
	}

//=======================================================================================
//=======================================================================================
//=======================================================================================


	public void combine_data(){
		
		try{
			System.out.println("Exception 0");
			combined_data = new String[data1.length][data1[0].length+data2[0].length-1];
			
			System.out.println(" data1.length = "+data1.length);
			System.out.println(" data2.length = "+data2.length);
			
			System.out.println(" data1[0].length = "+data1[0].length);
			System.out.println(" data2[0].length = "+data2[0].length);
			
			
			System.out.println("Exception 1");
			// 第一個arff的檔案，只取出 n-1 個屬性
			// (扣除最後一個class，這個class由arff2的class來決定)
			for(int i=0;i<data1.length;i++){
				for(int j=0;j<data1[i].length-1;j++){
					combined_data[i][j] = data1[i][j];
				}
			}

			

                        System.out.println("Exception 2");
			// 第二個arff的檔案，取出 n 個屬性
			// (必須加上一個class，這個class由arff2的class來決定!)
			for(int i=0;i<data2.length;i++){
				for(int j=0;j<data2[i].length;j++){					
					combined_data[i][j+(data1[0].length-1)] = data2[i][j];
					System.out.println(i+","+(j+(data1[0].length-1))+"<="+i+","+j);
				}
			}
			


			
			// 列印結果!
			/*
			for(int i=0;i<combined_data.length;i++){
				for(int j=0;j<combined_data[i].length;j++){		
					System.out.print(" "+combined_data[i][j]+" ");
					
				}
				System.out.println("");
				System.out.println("==========================================");
			}
			*/
			
			
			
		}catch(Exception e){
			System.out.println(" Error about combining the data (in combine_2arffs.java ): "+e.toString());	
		}		
	}


}
