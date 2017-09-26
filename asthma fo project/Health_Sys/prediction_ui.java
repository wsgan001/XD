package Health_Sys;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class prediction_ui extends JInternalFrame
{	
	patient_list patient_jp;
	disease_model_list models_jp;
	patient_info_ui info_jp;
	JTextArea jta=new JTextArea();
	public String target_patient="";
	
	public prediction_ui()
	{
		super("¯e¯f¹w´ú",true,true,true,true);
		models_jp=new disease_model_list(this);
		patient_jp=new patient_list(this);
		this.getContentPane().add(patient_jp,BorderLayout.WEST);
		JPanel right_jp=new JPanel(new BorderLayout());		
		//right_jp.add(jta,BorderLayout.NORTH);
		info_jp=new patient_info_ui();
		right_jp.add(info_jp,BorderLayout.NORTH);		
		right_jp.add(models_jp,BorderLayout.CENTER);
		this.getContentPane().add(right_jp,BorderLayout.CENTER);
		
		setVisible(true);
		//setSize(800,600);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void get_param(String c)
	{
		jta.append(c);
	}
	
	public void set_patient(String no)
	{
		info_jp.set_patient(no);
		models_jp.set_patient(no);
	}
	
	public static void main(String args[])
	{
		prediction_ui a=new prediction_ui();
	}
}