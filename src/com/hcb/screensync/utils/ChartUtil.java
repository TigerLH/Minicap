package com.hcb.screensync.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.hcb.screensync.bean.ProfermanceInfo;

/** 
 * @author  linhong: 
 * @date 2016��6��12�� ����7:54:43 
 * @Description: TODO
 * @version 1.0  
 */
public class ChartUtil {
	private static final String PIC_PATH = "TestResult"+File.separator+"Chart.png";
	
	public static void main(String[] args) {
		List<ProfermanceInfo> pcinfo = new ArrayList<ProfermanceInfo>();
		ProfermanceInfo pro = new ProfermanceInfo();
		pro.setCpuUsed(10);
		pro.setMemFree(100);
		pro.setMemUsed(88);
		pro.setNetUsed(30);
		ProfermanceInfo pro1 = new ProfermanceInfo();
		pro1.setCpuUsed(60);
		pro1.setMemFree(200);
		pro1.setMemUsed(40);
		pro1.setNetUsed(80);
		pcinfo.add(pro);
		pcinfo.add(pro1);
		ChartToPicture("com.wlqq",pcinfo);
	}
	
	
	
	public static void ChartToPicture(String pakcageName,List<ProfermanceInfo> pcinfo){
		StandardChartTheme standardChartTheme=new StandardChartTheme("CN");  
	    //���ñ�������  
	    standardChartTheme.setExtraLargeFont(new Font("����",Font.BOLD,20));  
	    //����ͼ��������  
	    standardChartTheme.setRegularFont(new Font("����",Font.PLAIN,12));
	    //�������������  
	    standardChartTheme.setLargeFont(new Font("����",Font.PLAIN,15));
	    ChartFactory.setChartTheme(standardChartTheme);
	    
		/**
		 * ��ȡ����
		 */
		XYSeriesCollection dataset1 = new XYSeriesCollection(); 
		XYSeriesCollection dataset2 = new XYSeriesCollection(); 
		XYSeriesCollection dataset3 = new XYSeriesCollection(); 
		XYSeries xyserie1 = new XYSeries("MemUsed");  
		XYSeries xyserie2 = new XYSeries("MemFree");
		XYSeries xyserie3 = new XYSeries("NetInfo");
		XYSeries xyserie4 = new XYSeries("CpuInfo");
		for(int i=0;i<pcinfo.size();i++){
			xyserie1.add(i, pcinfo.get(i).getMemUsed());
			xyserie2.add(i, pcinfo.get(i).getMemFree());
			xyserie3.add(i, pcinfo.get(i).getNetUsed());
			xyserie4.add(i, pcinfo.get(i).getCpuUsed());
		}
		dataset1.addSeries(xyserie1);
		dataset1.addSeries(xyserie2);
		dataset2.addSeries(xyserie3);
		dataset3.addSeries(xyserie4);
		JFreeChart chart = ChartFactory.createXYLineChart("��������"+"("+pakcageName+")", "������", "�ڴ�(MB)", dataset1);
		chart.setBackgroundPaint(new Color(119, 136, 238));
		chart.setTextAntiAlias(false);//�����
		XYPlot plot = chart.getXYPlot();
		plot.getRenderer().setStroke(new BasicStroke(2F)); //����������ϸ
		plot.setBackgroundPaint(new Color(119, 136, 238));    
		// ��ӵ�2��Y��  
        NumberAxis axis2 = new NumberAxis("����ʹ��(KB)");
        axis2.setAxisLinePaint(Color.YELLOW);
        axis2.setLabelPaint(Color.YELLOW);
        axis2.setTickLabelPaint(Color.YELLOW);
        
        plot.setRangeAxis(1, axis2);
        plot.setDataset(1, dataset2);
        plot.mapDatasetToRangeAxis(1, 1);
        // -- �޸ĵ�2��������ʾЧ��
        XYLineAndShapeRenderer render2 = new XYLineAndShapeRenderer();
        render2.setStroke(new BasicStroke(2F));
        render2.setSeriesPaint(0, Color.YELLOW); 
        plot.setRenderer(1, render2);  
        
        
        
    	// ��ӵ�2��Y��  
        NumberAxis axis3 = new NumberAxis("CPUʹ����");
        axis3.setAxisLinePaint(Color.GREEN);
        axis3.setLabelPaint(Color.GREEN);
        axis3.setTickLabelPaint(Color.GREEN);
          
        plot.setRangeAxis(2, axis3);
        plot.setDataset(2, dataset3);
        plot.mapDatasetToRangeAxis(2, 2);
        // -- �޸ĵ�3��������ʾЧ��
        XYLineAndShapeRenderer render3 = new XYLineAndShapeRenderer();
        render3.setStroke(new BasicStroke(2F));
        render3.setSeriesPaint(0, Color.GREEN); 
        plot.setRenderer(2, render3);
		try {
			ChartUtilities.saveChartAsPNG(new File(PIC_PATH), chart, 800, 400);
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}  
	}
}
