package sequential_pattern;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class feature_mine_ui extends JFrame
{	
	JLabel df_jl=new JLabel("Data Filename");
	JLabel ms_jl=new JLabel("Minimum Support");
	JLabel ll_jl=new JLabel("Length Limit");
	JLabel sp_jl=new JLabel("Features Filename");
	JLabel op_jl=new JLabel("Output Filename");
	JLabel ps_jl=new JLabel("Pattern SN Filename");
	JLabel tf_jl=new JLabel("Table Format Filename");
	JTextField df_jtf=new JTextField("",20);
	JTextField ms_jtf=new JTextField("",20);
	JTextField ll_jtf=new JTextField("",20);
	JTextField sp_jtf=new JTextField("",20);
	JTextField op_jtf=new JTextField("",20);
	JTextField ps_jtf=new JTextField("",20);
	JTextField tf_jtf=new JTextField("",20);
	JButton fopen_button=new JButton("...");
	JButton feature_mine_button=new JButton("FeatureMine");
	JButton replace_button=new JButton("Pattern Replace");
	JButton table_format_button=new JButton("Table Format");
	String label_string[]={"Data Filename","Output Filename",""};
	feature_mine_ui_action_adapter action_listener;
	
	public feature_mine_ui()
	{
		super("Feature Mine");
		ui_init();
		this.validate();
	}
	
	private void ui_init()
	{
		try
		{
			this.setLayout(null);
			
			action_listener=new feature_mine_ui_action_adapter(this);
			
			
			
			df_jl.setBounds(0,0,300,25);
			df_jtf.setBounds(0,25,250,25);
			fopen_button.setBounds(250,25,50,25);
			ms_jl.setBounds(0,50,300,25);
			ms_jtf.setBounds(0,75,300,25);
			ll_jl.setBounds(0,100,300,25);
			ll_jtf.setBounds(0,125,300,25);
			feature_mine_button.setBounds(0,150,300,25);
			this.add(df_jl);
			this.add(df_jtf);
			this.add(fopen_button);
			this.add(ms_jl);
			this.add(ms_jtf);
			this.add(ll_jl);
			this.add(ll_jtf);
			this.add(feature_mine_button);
			fopen_button.addActionListener(action_listener);
			feature_mine_button.addActionListener(action_listener);
			
			sp_jl.setBounds(0,175,300,25);
			sp_jtf.setBounds(0,200,300,25);
			sp_jtf.setBackground(Color.yellow);
			sp_jtf.setEditable(false);
			this.add(sp_jl);
			this.add(sp_jtf);			
			
			op_jl.setBounds(0,225,300,25);
			op_jtf.setBounds(0,250,300,25);
			ps_jl.setBounds(0,275,300,25);
			ps_jtf.setBounds(0,300,300,25);
			ps_jtf.setBackground(Color.yellow);
			this.add(op_jl);
			this.add(op_jtf);
			this.add(ps_jl);
			this.add(ps_jtf);			
			
			replace_button.setBounds(0,325,300,25);
			this.add(replace_button);
			replace_button.addActionListener(action_listener);
			
			tf_jl.setBounds(0,350,300,25);
			tf_jtf.setBounds(0,375,300,25);
			tf_jtf.setBackground(Color.yellow);
			this.add(tf_jl);
			this.add(tf_jtf);
			
			table_format_button.setBounds(0,400,300,25);
			this.add(table_format_button);
			table_format_button.addActionListener(action_listener);
			
			this.setVisible(true);
			this.setSize(300,475);
		}
		catch(Exception e)
		{
			System.out.println("feautre_mine_ui ui_init exception:"+e);
		}
	}
	
	public void action(ActionEvent ev)
	{
		try
		{
			Object temp=ev.getSource();
			if(temp.equals(fopen_button))
			{
				JFileChooser a=new JFileChooser("./");
				int r=a.showOpenDialog(this);
				if(r==JFileChooser.APPROVE_OPTION)
				{
					df_jtf.setText(a.getSelectedFile().getAbsolutePath());
				}
			}
			else if(temp.equals(feature_mine_button))
			{
				feature_mine a=new feature_mine(df_jtf.getText(),(new Double(ms_jtf.getText())).doubleValue(),(new Integer(ll_jtf.getText())).intValue());
				a.action();
				sp_jtf.setText(a.result_filename);
			}
			else if(temp.equals(replace_button))
			{
				ps_jtf.setText(op_jtf.getText()+"_ps");
				pattern_replace a=new pattern_replace(sp_jtf.getText(),df_jtf.getText(),op_jtf.getText(),ps_jtf.getText());
			}
			else if(temp.equals(table_format_button))
			{
				tf_jtf.setText(op_jtf.getText()+"_tb");
				pattern_replace.table_format(op_jtf.getText(),tf_jtf.getText());
			}
		}
		catch(Exception e)
		{
			System.out.println("feature_mine action exception:"+e);
		}
	}
}

class feature_mine_ui_action_adapter implements ActionListener
{
	feature_mine_ui adaptee;
	feature_mine_ui_action_adapter(feature_mine_ui adaptee)
	{
		this.adaptee=adaptee;
	}
	public void actionPerformed(ActionEvent e)
	{
		adaptee.action(e);
	}
}
