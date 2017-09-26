
package IntegratedTwoArffs;
	
import java.lang.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.io.*;


public class combine_2arffs{


	String[] attribute1;			// ���X�u����ݩʦW�١v
	String[][] data1;			// ���X�u��Ʈw���e�v
	String TableName1;			// ���X�u��ƦW�١v
	FastVector attri_lines1;		// ���X�u���㪺attribute information�v
		

	String[] attribute2;			// ���X�u����ݩʦW�١v
	String[][] data2;			// ���X�u��Ʈw���e�v
	String TableName2;			// ���X�u��ƦW�١v
	FastVector attri_lines2;		// ���X�u���㪺attribute information�v
	
	
	String[][] combined_data;		// �s��u���arff�ɵ��X�᪺�ɮסv


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


	public String[][] getCombinedData(){	// �^�ǡu ��X�᪺��� ��T�v
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
			// �Ĥ@��arff���ɮסA�u���X n-1 ���ݩ�
			// (�����̫�@��class�A�o��class��arff2��class�ӨM�w)
			for(int i=0;i<data1.length;i++){
				for(int j=0;j<data1[i].length-1;j++){
					combined_data[i][j] = data1[i][j];
				}
			}

			

                        System.out.println("Exception 2");
			// �ĤG��arff���ɮסA���X n ���ݩ�
			// (�����[�W�@��class�A�o��class��arff2��class�ӨM�w!)
			for(int i=0;i<data2.length;i++){
				for(int j=0;j<data2[i].length;j++){					
					combined_data[i][j+(data1[0].length-1)] = data2[i][j];
					System.out.println(i+","+(j+(data1[0].length-1))+"<="+i+","+j);
				}
			}
			


			
			// �C�L���G!
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
