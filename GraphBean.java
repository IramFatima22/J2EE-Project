package com.uic.edu;

import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class GraphBean {
	private String field;
	private double value;
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public static JFreeChart createBarChart(double[] score, int totalMarks) {
	DefaultCategoryDataset dataset= new DefaultCategoryDataset();
	ArrayList<Double> scoreA=new ArrayList<Double>();
	ArrayList<Double> scoreB=new ArrayList<Double>();
	ArrayList<Double> scoreC=new ArrayList<Double>();
	ArrayList<Double> scoreD=new ArrayList<Double>();
	ArrayList<Double> scoreF=new ArrayList<Double>();
	
	
	
	//double scoreA=new double[]
	
	
	
	for(int i=0;i<score.length;i++)
	{
		if((score[i]/totalMarks)>=.9)
		{
			scoreA.add(score[i]);
			//dataset.addValue(score[i], "Score A", "Student"+(i+1));
		}
		else if((score[i]/totalMarks)>=.8)
		{
			scoreB.add(score[i]);
			//dataset.addValue(score[i], "Score B", "Student"+(i+1));
		}
		else if((score[i]/totalMarks)>=.7)
		{
			scoreC.add(score[i]);
			//dataset.addValue(score[i], "Score C", "Student"+(i+1));
		}
		else if((score[i]/totalMarks)>=.5)
		{
			scoreD.add(score[i]);
			//dataset.addValue(score[i], "Score D", "Student"+(i+1));
		}
		else
		{
			scoreF.add(score[i]);
			//dataset.addValue(score[i], "Score F", "Student"+(i+1));
		}
		
	//dataset.addValue(4.0, "S1", "C2");
	//System.out.println(score[i]);
	}
	int k=0;
	if(scoreA!=null)
	{
		
		for(int i1=0;i1<scoreA.size();i1++)
		{
			k++;
			dataset.addValue(scoreA.get(i1), "Grade A", "S"+k+":"+scoreA.get(i1));
		}
	}
	if(scoreB!=null)
	{
		
		for(int i1=0;i1<scoreB.size();i1++)
		{
			k++;
			dataset.addValue(scoreB.get(i1), "Grade B", "S"+k+":"+scoreB.get(i1));
		}
	}
	
	if(scoreC!=null)
	{
		
		for(int i1=0;i1<scoreC.size();i1++)
		{
			k++;
			dataset.addValue(scoreC.get(i1), "Grade C", "S"+k+":"+scoreC.get(i1));
		}
	}
	if(scoreD!=null)
	{
		for(int i1=0;i1<scoreD.size();i1++)
		{
			k++;
			dataset.addValue(scoreD.get(i1), "Grade D", "S"+k+":"+scoreD.get(i1));
		}
	}
	if(scoreF!=null)
	{
		for(int i1=0;i1<scoreF.size();i1++)
		{
			k++;
			dataset.addValue(scoreF.get(i1), "Grade F", "S"+k+":"+scoreF.get(i1));
		}
	}
	
	
	
	JFreeChart chart= ChartFactory
	.createBarChart3D("Bar Chart","Category","Marks",dataset,PlotOrientation.VERTICAL,true,true,false);
	return chart;
	}
	
	
	public static JFreeChart createPieChart(double[] score) {
	// create a 
	//dataset
	//...
	DefaultPieDataset data= new DefaultPieDataset();
	for(int i=0;i<score.length;i++)
	{
	data.setValue("Student"+(i+1),score[i]);
	//dataset.addValue(4.0, "S1", "C2");
	System.out.println(score[i]);
	}
	
	JFreeChart chart= ChartFactory.createPieChart("Pie Chart", data, true, true, false);
	return chart;
	}

}
