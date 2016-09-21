package com.uic.edu;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.math3.stat.StatUtils;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

@ManagedBean
@SessionScoped
public class ActionBeanGraph {
	//double values [] ;
	double[] notAttempted_Array;
	double[] correct_Array;
	double[] wrong_Array;
	private double[] score;
	boolean renderBar=false;
	boolean renderPie=false;
	//private ArrayList<Double> valueList1=new ArrayList<Double>();
	private ArrayList<GraphBean> valueList1=new ArrayList<GraphBean>();
	private ArrayList<GraphBean> valueList2=new ArrayList<GraphBean>();
	private ArrayList<GraphBean> valueList3=new ArrayList<GraphBean>();
	
	private ActionBeanInstructor actionBeanInstructor;
	File chart1;
	String url;
	private String checkChartType;
	private FacesContext context;
	private int totalMarks;
	
	@PostConstruct
	public void init() {
		context = FacesContext.getCurrentInstance();
		Map<String, Object> m =context.getExternalContext().getSessionMap();
		actionBeanInstructor = (ActionBeanInstructor) m.get("actionBeanInstructor");
		notAttempted_Array=actionBeanInstructor.getNotTakenPerQues();
		correct_Array=actionBeanInstructor.getCorrectPerQues();
		wrong_Array=actionBeanInstructor.getWrongPerQues();
		//score=new Double[];
		
	}
	
	
	public String getUrl() {
		
		if(checkChartType.equalsIgnoreCase("bar"))
		{
		url= "temp"+"/bar"+".png";
		}
		if(checkChartType.equalsIgnoreCase("pie"))
		{
			url= "temp"+"/pie"+".png";
		}
		
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public boolean isRenderBar() {
		return renderBar;
	}

	public void setRenderBar(boolean renderBar) {
		this.renderBar = renderBar;
	}

	public boolean isRenderPie() {
		return renderPie;
	}

	public void setRenderPie(boolean renderPie) {
		this.renderPie = renderPie;
	}

	public ArrayList<GraphBean> getValueList1() {
		score=actionBeanInstructor.getScore();
		totalMarks=actionBeanInstructor.getTotalMarks();
		
		double minValue = StatUtils.min(score);
		double maxValue = StatUtils.max(score);
		double mean = StatUtils.mean(score);
		double variance = StatUtils.variance(score, mean);
		double std = Math.sqrt(variance);
		double median = StatUtils.percentile(score, 50.0);
		double q1 = StatUtils.percentile(score, 25.0);
		double q3 = StatUtils.percentile(score, 75.0);
		double iqr = q3 -q1;
		double range = maxValue-minValue;
		valueList1=new ArrayList<GraphBean>();
		//valueList=new ArrayList<Double>();
		GraphBean gb=new GraphBean();
		gb.setField("Min Marks");
		gb.setValue(minValue);
		valueList1.add(gb);
		
		gb=new GraphBean();
		gb.setField("First Quartile");
		gb.setValue(q1);
		valueList1.add(gb);
		
		gb=new GraphBean();
		gb.setField("Median");
		gb.setValue(median);
		valueList1.add(gb);
		
		gb=new GraphBean();
		gb.setField("Third Quartile");
		gb.setValue(q3);
		valueList1.add(gb);
		
		gb=new GraphBean();
		gb.setField("Max Marks");
		gb.setValue(maxValue);
		valueList1.add(gb);
		
		
		
		return valueList1;
	}


	public void setValueList1(ArrayList<GraphBean> valueList1) {
		this.valueList1 = valueList1;
	}


	public ArrayList<GraphBean> getValueList2() {
		
		score=actionBeanInstructor.getScore();
		totalMarks=actionBeanInstructor.getTotalMarks();
		
		double minValue = StatUtils.min(score);
		double maxValue = StatUtils.max(score);
		double mean = StatUtils.mean(score);
		double variance = StatUtils.variance(score, mean);
		double std = Math.sqrt(variance);
		double median = StatUtils.percentile(score, 50.0);
		double q1 = StatUtils.percentile(score, 25.0);
		double q3 = StatUtils.percentile(score, 75.0);
		double iqr = q3 -q1;
		double range = maxValue-minValue;
		valueList2=new ArrayList<GraphBean>();
		GraphBean gb=new GraphBean();
		gb.setField("Median");
		gb.setValue(median);
		valueList2.add(gb);
		
		
		
		gb=new GraphBean();
		gb.setField("Mean");
		gb.setValue(mean);
		valueList2.add(gb);
	
		
		
		return valueList2;
	}


	public void setValueList2(ArrayList<GraphBean> valueList2) {
		this.valueList2 = valueList2;
	}


	public ArrayList<GraphBean> getValueList3() {
		score=actionBeanInstructor.getScore();
		totalMarks=actionBeanInstructor.getTotalMarks();
		
		double minValue = StatUtils.min(score);
		double maxValue = StatUtils.max(score);
		double mean = StatUtils.mean(score);
		double variance = StatUtils.variance(score, mean);
		double std = Math.sqrt(variance);
		double median = StatUtils.percentile(score, 50.0);
		double q1 = StatUtils.percentile(score, 25.0);
		double q3 = StatUtils.percentile(score, 75.0);
		double iqr = q3 -q1;
		double range = maxValue-minValue;
		valueList3=new ArrayList<GraphBean>();
		//valueList=new ArrayList<Double>();
		GraphBean gb=new GraphBean();
		
		
		gb=new GraphBean();
		gb.setField("Variance");
		gb.setValue(variance);
		valueList3.add(gb);
		
		gb=new GraphBean();
		gb.setField("Standard Deviation");
		gb.setValue(std);
		valueList3.add(gb);	
		
		gb=new GraphBean();
		gb.setField("Inter Quartile Range");
		gb.setValue(iqr);
		valueList3.add(gb);
		
		gb=new GraphBean();
		gb.setField("Range");
		gb.setValue(range);
		valueList3.add(gb);
		return valueList3;
	}


	public void setValueList3(ArrayList<GraphBean> valueList3) {
		
		
		
		this.valueList3 = valueList3;
	}


	
	
	public String backToInstructor()
	{
		renderBar=false;
		score=new double[1];
		return "/InstructorPage.jsp?faces-redirect=true";
	}
	
	
	public File getChart1() {
		return chart1;
	}

	public void setChart1(File chart1) {
		this.chart1 = chart1;
	}

	public String createBar()
	{
		checkChartType="Bar";
		renderBar=true;
		  
	      try {
			JFreeChart chart=GraphBean.createBarChart(score,totalMarks);
			context = FacesContext.getCurrentInstance();
			String path = context.getExternalContext().getRealPath("/temp");
			System.out.println("p :"+path);
			ByteArrayOutputStream s = new ByteArrayOutputStream();
			chart1= new File(path+"/"+"bar"+".png");
			ChartUtilities.saveChartAsJPEG(chart1, chart, 600, 450);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "SUCCESS";
	}
	
	public String createPie()
	{
		checkChartType="Pie";
		renderBar=true;
		  
	      try {
			JFreeChart chart=GraphBean.createPieChart(score);
			context = FacesContext.getCurrentInstance();
			String path = context.getExternalContext().getRealPath("/temp");
			chart1= new File(path+"/"+"pie"+".png");
			ChartUtilities.saveChartAsJPEG(chart1, chart, 600, 450);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "SUCCESS";
	}

}
