/**
 * 
 */
package com.yuhen.saleapp.sell;

import java.util.ArrayList;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.yuhen.saleapp.R;
import com.yuhen.saleapp.activity.BaseActivity;

/**
 * 销售管理
 * @author ps
 *
 */
public class SellMgrActivity extends BaseActivity {

	private View sell_order_chart;
	private View everyday_money_chart;
	private View everyday_client_chart;
	private View everyday_collect_chart;
	
	@Override
	public void initView() {
		super.initView();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sell);
		initView();
		head_center_text.setText("销售管理");
		
		sell_order_chart = findViewById(R.id.sell_order_chart);
		everyday_money_chart = findViewById(R.id.everyday_money_chart);
		everyday_client_chart = findViewById(R.id.everyday_client_chart);
		everyday_collect_chart = findViewById(R.id.everyday_collect_chart);
		
		View []chartView = {sell_order_chart,everyday_money_chart,everyday_client_chart,everyday_collect_chart};
		int []titles = {R.string.sell_order_text,R.string.everyday_money_text,
				R.string.everyday_client_text,R.string.everyday_collect_text};
		for (int i = 0; i < chartView.length; i++) {
			TextView textView = (TextView)chartView[i].findViewById(R.id.order_all_text);
			textView.setText(titles[i]);
			
			LinearLayout chartLayout = (LinearLayout)chartView[i].findViewById(R.id.ll_chart);
			initChart(chartLayout);
		}
	}
	
	public void initChart(LinearLayout chartLayout) {
		String[] titles = new String[] { "昨天", "今天" };
		// 存放柱状图两个序列的值
		ArrayList<double[]> value = new ArrayList<double[]>();
		double[] d1 = new double[] { 0.1, 0.3, 0.7, 0.8, 0.5 };
		double[] d2 = new double[] { 0.2, 0.3, 0.4, 0.8, 0.6 };
		value.add(d1);
		value.add(d2);
		// 两个状的颜色
		int[] colors = { Color.BLUE, Color.GREEN };

		// 为li1添加柱状图
		chartLayout.addView(
				xychar(titles, value, colors, 6, 1, new double[] { 2007,
						2012.5, 0, 1 }, new int[] { 2008, 2009, 2010, 2011,
						2012 }, "订单成交量对比", true),
				new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT));
	}
	public GraphicalView xychar(String[] titles, ArrayList<double[]> value,
			int[] colors, int x, int y, double[] range, int[] xLable,
			String xtitle, boolean f) {
		// 多个渲染
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		// 多个序列的数据集
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		// 构建数据集以及渲染
		for (int i = 0; i < titles.length; i++) {

			XYSeries series = new XYSeries(titles[i]);
			double[] yLable = value.get(i);
			for (int j = 0; j < yLable.length; j++) {
				series.add(xLable[j], yLable[j]);
			}
			dataset.addSeries(series);
			XYSeriesRenderer xyRenderer = new XYSeriesRenderer();
			// 设置颜色
			xyRenderer.setColor(colors[i]);
			// 设置点的样式 //
			xyRenderer.setPointStyle(PointStyle.SQUARE);
			// 将要绘制的点添加到坐标绘制中

			renderer.addSeriesRenderer(xyRenderer);
		}
		// 设置x轴标签数
		renderer.setXLabels(x);
		// 设置Y轴标签数
		renderer.setYLabels(y);
		// 设置x轴的最大值
		renderer.setXAxisMax(x - 0.5);
		// 设置轴的颜色
		renderer.setAxesColor(Color.BLACK);
		// 设置x轴和y轴的标签对齐方式
		renderer.setXLabelsAlign(Align.CENTER);
		renderer.setYLabelsAlign(Align.RIGHT);
		// 设置现实网格
		renderer.setShowGrid(true);

		renderer.setShowAxes(true);
		// 设置条形图之间的距离
		renderer.setBarSpacing(0.2);
		renderer.setInScroll(false);
		renderer.setPanEnabled(false, false);
		renderer.setClickEnabled(false);
		// 设置x轴和y轴标签的颜色
		renderer.setXLabelsColor(Color.RED);
		renderer.setYLabelsColor(0, Color.RED);

		int length = renderer.getSeriesRendererCount();
		// 设置图标的标题
		renderer.setChartTitle(xtitle);
		renderer.setLabelsColor(Color.RED);

		// 设置图例的字体大小
		renderer.setLegendTextSize(18);
		// 设置x轴和y轴的最大最小值
		renderer.setRange(range);
		renderer.setMarginsColor(0x00888888);
		for (int i = 0; i < length; i++) {
			SimpleSeriesRenderer ssr = renderer.getSeriesRendererAt(i);
			ssr.setChartValuesTextAlign(Align.RIGHT);
			ssr.setChartValuesTextSize(12);
			ssr.setDisplayChartValues(f);
		}
		GraphicalView mChartView = ChartFactory.getBarChartView(
				getApplicationContext(), dataset, renderer, Type.DEFAULT);
		return mChartView;
	}
}
