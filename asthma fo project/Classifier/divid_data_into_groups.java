package Classifier;

import java.lang.*;
import java.util.*;
import java.io.*;

public class divid_data_into_groups{

	
	
	String[][] data;	// �H�u�r���v�Ϲj�᪺��l���
	
	Vector<String> health_data = new Vector<String>();
	Vector<String> unhealth_data = new Vector<String>();


	FastVector dbData;	// ��l���

	//Vector<String> attach_unhealth_data = new Vector<String>();



	ElementaryTable health_testing_data = new ElementaryTable();	
	ElementaryTable unhealth_testing_data = new ElementaryTable();
	
	Vector<String> training_data = new Vector<String>();
	Vector<String> testing_data = new Vector<String>();


	Vector<String> unbalance_training_data = new Vector<String>();


	// �O���u�üƻP���šv�᪺��ƶ�
	Vector<String> random_unbalance_training_data = new Vector<String>();
	Vector<String> random_balance_training_data = new Vector<String>();

	// �O���u�üƻP���šv�᪺��ƶ�
	Vector<String> random_balance_testing_data = new Vector<String>();
	Vector<String> balance_testing_data = new Vector<String>();



	double training_rate = 0.7;

	private Vector<String> health_label_set;
	private Vector<String> unhealth_label_set;


	public divid_data_into_groups(String[][] data, FastVector dbData, double training_rate){
		health_label_set=new Vector<String>();
		health_label_set.add("C2");
		unhealth_label_set=new Vector<String>();
		unhealth_label_set.add("C1");
		unhealth_label_set.add("C3");
		initial(data,dbData,training_rate);
	}
	
	public divid_data_into_groups(String[][] data, FastVector dbData, double training_rate,Vector<String> hls,Vector<String> uhls){
		health_label_set=hls;
		unhealth_label_set=uhls;
		initial(data,dbData,training_rate);
	}
	
	public void initial(String[][] data, FastVector dbData, double training_rate)
	{
		try{
			this.training_rate = training_rate;
			this.data = data;
			this.dbData = dbData;
			
			System.out.println(" data.length = "+data.length);			
			System.out.println(" dbData.length = "+dbData.size());


			//-------------------------------
			// 01.
			// ���N�u��l��ơv���ά��uhealth data�v�M�uunhealth data�v
			divid_health_and_unhealth_data();
			
			
			//-------------------------------
			// 02.
			// �N health group ���� 70% �M 30%
			divid_health_group_to_training_and_testing_data();
			
			
			//-------------------------------
			// 03.
			// �N unhealth group ���� 70% �M 30%
			divid_unhealth_group_to_training_and_testing_data();
			
			
			//-------------------------------			
			// 04.
			// �ϧO�X testing �P training �� data
			gen_training_and_testing_data();
			
			
			//-------------------------------			
			// 05.
			// �i�� training data �� balance
			gen_training_unhealth_data();
			
			
			//-------------------------------			
			// 06.
			// �i�� unbalance and balance training data �� �üƱƧ�
			perform_balance_training_new_romdam_data();			

			//-------------------------------			
			// 08.
			// �i�� training data �� balance
			gen_testing_unhealth_data();
			
			
			//-------------------------------			
			// 09.
			// �i�� training data �� �üƱƧ�
		 	perform_testing_new_romdam_data();
						
			
			//-------------------------------			
			// 07.
			// �i�� unbalance training data �� �üƱƧ�
			perform_unbalance_training_new_romdam_data();
	
			
		
		}catch(Exception e){
			System.out.println(" Error about dividing the data (in divid_data_into_groups.java):"+e.toString());
		}	
		
	}

//--------------------------------------------------
//--------------------------------------------------
//--------------------------------------------------


	public Vector<String> getHealthData(){			// �^�ǡu health Data�v
		return health_data;	
	}
	
	
	public Vector<String> getUnHealthData(){		// �^�ǡu Unhealth Data�v
		return unhealth_data;	
	}

/*
	public Vector<String> getUnbalanceTrainingData(){	// �^�ǡu Training Data�v
		return training_data;	
	}
*/	
	public Vector<String> getTestingData(){		// �^�ǡu Testing Data�v
		return testing_data;	
	}
	

	public Vector<String> getUnbalanceTrainingData(){	// �^�ǡu Unbalance Data�v
		return random_unbalance_training_data;	
	}	
	
	
	public Vector<String> getBalanceTrainingData(){	// �^�ǡu Balance Data�v
		return random_balance_training_data;	
	}	



	public Vector<String> getBalanceTestingData(){	// �^�ǡu Balance Data�v
		return random_balance_testing_data;	
	}


		
//--------------------------------------------------
//--------------------------------------------------
//--------------------------------------------------


	public void perform_unbalance_training_new_romdam_data(){
		

		try{
		
			Vector<String> training_random_id = new Vector<String>();
			ElementaryTable training_id = new ElementaryTable();
			
			
			
			
			//int random_num = (int)((double)unbalance_training_data.size()*0.7);
			int random_num = (int)((double)unbalance_training_data.size()*training_rate);
			
			
			for(int i=0;i<random_num;i++){
				
				
				int tmp_id = (int)(Math.random()*(double)unbalance_training_data.size());
				
				char id = (char)tmp_id;
										
				while(tmp_id<=0 || training_id.ContainsKey(id)){
						
					tmp_id =(int)(Math.random()*(double)unbalance_training_data.size());
						
					id = (char)tmp_id;
					
						
				}// while
					
				// �N�����O���[�J��hash table��!
				training_id.add(id, 1);				
				training_random_id.add(String.valueOf((int)id));
				
			}
			
			
			for(int i=0;i<training_random_id.size();i++){
				int tmp_id = Integer.parseInt(training_random_id.get(i));
				random_unbalance_training_data.add(unbalance_training_data.get(tmp_id));				
			}
			
			for(int i=0;i<unbalance_training_data.size();i++){
				if(training_id.ContainsKey((char)i)){
				}else{
					random_unbalance_training_data.add(unbalance_training_data.get(i));						
				}				
			}
			
			
			System.out.println(" ******************************************************** ");
			System.out.println(" training_id.size()  = "+training_id.size() );
			System.out.println(" unbalance_training_data.size()  = "+unbalance_training_data.size() );
			System.out.println(" random_unbalance_training_data.size()  = "+random_unbalance_training_data.size() );
			System.out.println(" ******************************************************** ");
			
		
		}catch(Exception e){
			System.out.println(" Error about performing randomly the training data (in divid_data_into_groups.java ): "+e.toString());	
		}
				
	}



	public void perform_testing_new_romdam_data(){
		

		try{
		
			Vector<String> testing_random_id = new Vector<String>();
			ElementaryTable testing_id = new ElementaryTable();
			
			
			
			
			//int random_num = (int)((double)balance_testing_data.size()*0.7);
			int random_num = (int)((double)balance_testing_data.size()*training_rate);
			
			
			for(int i=0;i<random_num;i++){
				
				
				int tmp_id = (int)(Math.random()*(double)balance_testing_data.size());
				
				char id = (char)tmp_id;
										
				while(tmp_id<=0 || testing_id.ContainsKey(id)){
						
					tmp_id =(int)(Math.random()*(double)balance_testing_data.size());
						
					id = (char)tmp_id;
					
						
				}// while
					
				// �N�����O���[�J��hash table��!
				testing_id.add(id, 1);				
				testing_random_id.add(String.valueOf((int)id));
				
			}
			
			
			for(int i=0;i<testing_random_id.size();i++){
				int tmp_id = Integer.parseInt(testing_random_id.get(i));
				random_balance_testing_data.add(balance_testing_data.get(tmp_id));				
			}
			
			for(int i=0;i<testing_data.size();i++){
				if(testing_id.ContainsKey((char)i)){
				}else{
					random_balance_testing_data.add(balance_testing_data.get(i));						
				}				
			}
			
			/*
			System.out.println(" ******************************************************** ");
			System.out.println(" testing_id.size()  = "+testing_id.size() );
			System.out.println(" balance_testing_data.size()  = "+balance_testing_data.size() );
			System.out.println(" random_balance_testing_data.size()  = "+random_balance_testing_data.size() );
			System.out.println(" ******************************************************** ");
			*/
		
		}catch(Exception e){
			System.out.println(" Error about performing randomly the training data (in divid_data_into_groups.java ): "+e.toString());	
		}
				
	}





	public void gen_testing_unhealth_data(){
		
		try{
		
			Vector<String> testing_unhealth_data = new Vector<String>();
			
			
			
			
			// 01.
			// �����Xtraining data�̪� unhealth data
			for(int i=0;i<testing_data.size();i++){
				String[] tmp_data = testing_data.get(i).split(",");
				
				// 02.			
				// �btraining data�̪������d��ƶ�
				// �u�]���b C1, C3 (���]�A C?)	
						
				//if(tmp_data[tmp_data.length-1].compareTo("C1")==0 || tmp_data[tmp_data.length-1].compareTo("C3")==0){
				if(unhealth_label_set.contains(tmp_data[tmp_data.length-1])){
					testing_unhealth_data.add(testing_data.get(i));
				}
			}


			
		
			// �p��btraining����Ƹ̡Ahealth data �M unhealth data�� �ƶq�t��
			int testing_health_num =  testing_data.size()-testing_unhealth_data.size();
			int diff_num = testing_health_num - testing_unhealth_data.size();

			/*
			System.out.println(" testing_data.size() = "+testing_data.size());
			System.out.println(" testing_health_num = "+testing_health_num);
			System.out.println(" testing_unhealth_data.size() = "+testing_unhealth_data.size());
			System.out.println(" diff_num = "+diff_num);
			*/


			// 02.
			// �N�o�� training �̪� unhealth data �üƩ���ܻP training �̪� health data �ۦP
			
			// ���N�����testing data�[�J�� balance_testing_data��
			for(int i=0;i<testing_data.size();i++){
				balance_testing_data.add(testing_data.get(i)); 
			}
			
			// �A�N�u�üƩ�X��random testing data �[�J�ܡubalance_testing_data�v��
			for(int i=0;i<diff_num;i++){
											
				int tmp_id =(int)(Math.random()*(double)testing_unhealth_data.size());						
				balance_testing_data.add(testing_unhealth_data.get(tmp_id));
										
			}


			/*
			System.out.println(" ******************************************************** ");
			System.out.println(" balance_testing_data.size()  = "+balance_testing_data.size() );			
			System.out.println(" ******************************************************** ");
			*/

		}catch(Exception e){
			System.out.println(" Error about generating the training data and testing data (in divid_data_into_groups.java ): "+e.toString());	
		}
	}







	public void perform_balance_training_new_romdam_data(){
		

		try{
		
			Vector<String> training_random_id = new Vector<String>();
			ElementaryTable training_id = new ElementaryTable();
			
			
			
			
			int random_num = (int)((double)training_data.size()*0.7);
			//String[] training_random_id = new String[]
			
			for(int i=0;i<random_num;i++){
				
				
				int tmp_id = (int)(Math.random()*(double)training_data.size());
				
				char id = (char)tmp_id;
										
				while(tmp_id<=0 || training_id.ContainsKey(id)){
						
					tmp_id =(int)(Math.random()*(double)training_data.size());
						
					id = (char)tmp_id;
					
						
				}// while
					
				// �N�����O���[�J��hash table��!
				training_id.add(id, 1);				
				training_random_id.add(String.valueOf((int)id));
				//training_random_id.add(id);
			}
			
			
			for(int i=0;i<training_random_id.size();i++){
				int tmp_id = Integer.parseInt(training_random_id.get(i));
				random_balance_training_data.add(training_data.get(tmp_id));				
			}
			
			for(int i=0;i<training_data.size();i++){
				if(training_id.ContainsKey((char)i)){
				}else{
					random_balance_training_data.add(training_data.get(i));	
				}				
			}
			
			
			/*
			System.out.println(" ******************************************************** ");
			System.out.println(" training_id.size()  = "+training_id.size() );
			System.out.println(" random_balance_training_data.size()  = "+random_balance_training_data.size() );
			System.out.println(" ******************************************************** ");
			*/
		
		}catch(Exception e){
			System.out.println(" Error about performing randomly the training data (in divid_data_into_groups.java ): "+e.toString());	
		}
				
	}






	public void gen_training_unhealth_data(){
		
		try{
		
			Vector<String> training_unhealth_data = new Vector<String>();
			
			
			
			
			// 01.
			// �����Xtraining data�̪� unhealth data
			for(int i=0;i<training_data.size();i++){
				String[] tmp_data = training_data.get(i).split(",");
				
				// 02.			
				// �btraining data�̪������d��ƶ�
				// �u�]���b C1, C3 (���]�A C?)	
						
				//if(tmp_data[tmp_data.length-1].compareTo("C1")==0 || tmp_data[tmp_data.length-1].compareTo("C3")==0){
				if(unhealth_label_set.contains(tmp_data[tmp_data.length-1])){
					training_unhealth_data.add(training_data.get(i));
				}
			}


			
		
			// �p��btraining����Ƹ̡Ahealth data �M unhealth data�� �ƶq�t��
			int training_health_num =  training_data.size()-training_unhealth_data.size();
			int diff_num = training_health_num - training_unhealth_data.size();

			/*
			System.out.println(" training_data.size() = "+training_data.size());
			System.out.println(" training_health_num = "+training_health_num);
			System.out.println(" training_unhealth_data.size() = "+training_unhealth_data.size());
			System.out.println(" diff_num = "+diff_num);
			*/


			// 02.
			// �N�o�� training �̪� unhealth data �üƩ���ܻP training �̪� health data �ۦP
			
			for(int i=0;i<diff_num;i++){
											
				int tmp_id =(int)(Math.random()*(double)training_unhealth_data.size());						
				training_data.add(training_unhealth_data.get(tmp_id));
										
			}

			/*
			// 03.
			// �N�utraining data�̪� new unhealth data ��J�� training data�v��
			for(int i=0;i<training_unhealth_data.size();i++){
				training_data.add(training_unhealth_data.get(i));
			}*/


			
			System.out.println(" ******************************************************** ");
			System.out.println(" balance training_data.size()  = "+training_data.size() );			
			System.out.println(" ******************************************************** ");
			

		}catch(Exception e){
			System.out.println(" Error about generating the training data and testing data (in divid_data_into_groups.java ): "+e.toString());	
		}
	}




	public void gen_training_and_testing_data(){
		
		try{
		
			// �q health data �̧�X training data �M testing data
			/*
			System.out.println(" health_data.size() = "+health_data.size());
			
			
			System.out.println(" ******************************************************** ");
			System.out.println(" training_data.size()  = "+training_data.size() );
			System.out.println(" testing_data.size() = "+testing_data.size());
			System.out.println(" ******************************************************** ");
			*/
			
			
			for(int i=0;i<health_data.size();i++){
				
				if(health_testing_data.ContainsKey((char)i)){
					testing_data.add(health_data.get(i));
				}else{
					training_data.add(health_data.get(i));
					unbalance_training_data.add(health_data.get(i));
				}
			}
			
			/*
			System.out.println(" ******************************************************** ");
			System.out.println(" training_data.size()  = "+training_data.size() );
			System.out.println(" testing_data.size() = "+testing_data.size());
			System.out.println(" ******************************************************** ");
			*/
			
			// �q unhealth data �̧�X training data �M testing data
			
			System.out.println(" unhealth_data.size() = "+unhealth_data.size());
			for(int i=0;i<unhealth_data.size();i++){
				
				if(unhealth_testing_data.ContainsKey((char)i)){
					testing_data.add(unhealth_data.get(i));
				}else{
					training_data.add(unhealth_data.get(i));
					unbalance_training_data.add(unhealth_data.get(i));
				}
			}			



			System.out.println(" ******************************************************** ");
			System.out.println(" health_data.size() + unhealth_data.size() (���]�AC?�����) = "+(health_data.size()+unhealth_data.size()));
			System.out.println(" training_data.size()  = "+training_data.size() );
			System.out.println(" testing_data.size() = "+testing_data.size());
			System.out.println(" unbalance_training_data.size() = "+unbalance_training_data.size());
			System.out.println(" ******************************************************** ");

				




		}catch(Exception e){
			System.out.println(" Error about generating the training balance data (in divid_data_into_groups.java):"+e.toString());	
		}		
	}



	public void divid_unhealth_group_to_training_and_testing_data(){
		
		
		try{
				
				//int unhealth_testing_data_num = (int)((double)unhealth_data.size()*0.3);
				int unhealth_testing_data_num = (int)((double)unhealth_data.size()*(1-training_rate));


				for(int i=0;i<unhealth_testing_data_num;i++){


					int tmp_unhealth_testing_id =(int)(Math.random()*(double)unhealth_data.size());
					
					// �D���d��ƶ������ǽs��: number id					
					//String unhealth_id ="";
					//unhealth_id += tmp_unhealth_testing_id;
					char unhealth_id = (char)tmp_unhealth_testing_id;
										
					while(tmp_unhealth_testing_id<=0 || unhealth_testing_data.ContainsKey(unhealth_id)){
						
						tmp_unhealth_testing_id =(int)(Math.random()*(double)unhealth_data.size());
						
						unhealth_id = (char)tmp_unhealth_testing_id;
						//unhealth_id = String.valueOf(tmp_unhealth_testing_id);						
						
					}// while
					
					// �N�����O���[�J��hash table��!
					unhealth_testing_data.add(unhealth_id, 1);
					
				}
				
				
				/*
				System.out.println(" ******************************************************** ");
				System.out.println(" unhealth_data_num  = "+unhealth_data.size());
				System.out.println(" unhealth_testing_data_num  = "+unhealth_testing_data_num );
				System.out.println(" unhealth_testing_data.size() = "+unhealth_testing_data.size());
				System.out.println(" ******************************************************** ");
				*/

		}catch(Exception e){
			System.out.println(" Error about dividing unhealth data into training and testing data in the health group: "+e.toString());		
		}
	}
	
	public void divid_health_group_to_training_and_testing_data(){
		
		
		try{
				
				//int health_testing_data_num = (int)((double)health_data.size()*0.3);
				int health_testing_data_num = (int)((double)health_data.size()*(1-training_rate));


				for(int i=0;i<health_testing_data_num;i++){


					int tmp_health_testing_id =(int)(Math.random()*(double)health_data.size());
					
					// ���d��ƶ������ǽs��: number id					
					//String health_id ="";
					//health_id += tmp_health_testing_id;
					char health_id = (char)tmp_health_testing_id;
					
										
					while(tmp_health_testing_id<=0 || health_testing_data.ContainsKey(health_id)){
						
						tmp_health_testing_id =(int)(Math.random()*(double)health_data.size());
						
						health_id = (char)tmp_health_testing_id;
						//health_id = String.valueOf(tmp_health_testing_id);						
						
					}// while
					
					// �N�����O���[�J��hash table��!
					health_testing_data.add(health_id, 1);
					
				}
				
				
				System.out.println(" ******************************************************** ");
				System.out.println(" health_data_num  = "+health_data.size());
				System.out.println(" health_testing_data_num  = "+health_testing_data_num );
				System.out.println(" health_testing_data.size() = "+health_testing_data.size());
				System.out.println(" ******************************************************** ");


		}catch(Exception e){
			System.out.println(" Error about dividing health data into training and testing data in the health group: "+e.toString());		
		}
	}



	public void divid_health_and_unhealth_data(){
		
		
		try{
	
			
			
			for(int i=0;i<data.length;i++){
				
				
				// 01.
				// ���d�����				
				//if(data[i][data[i].length-1].compareTo("C2")==0){
				if(health_label_set.contains(data[i][data[i].length-1])){
					health_data.add(dbData.elementAt(i));					
				}
				
				
				// 02.			
				// �����d�����	
				// �u�]���b C1, C3 (���]�A C?)
				//if(data[i][data[i].length-1].compareTo("C1")==0 || data[i][data[i].length-1].compareTo("C3")==0){
				if(unhealth_label_set.contains(data[i][data[i].length-1])){
					unhealth_data.add(dbData.elementAt(i));
				}
				
				
			}
			
			
			
			System.out.println(" health_data.size() = "+health_data.size() );
			System.out.println(" unhealth_data.size() = "+unhealth_data.size() );
		
		
		}catch(Exception e){
			System.out.println(" Error about dividing the data into the two gorups health and unhealth (in divid_data_into_groups.java):"+e.toString());
		}	
		
		
	}

}