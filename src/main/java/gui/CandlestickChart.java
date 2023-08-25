package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SegmentedTimeline;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.OHLCDataItem;
import org.jfree.data.xy.OHLCDataset;

import program.MyCandlestickRenderer;
import program.ReadExcelFile;

/**
 * 
 * @author Patrik Turcani Inspiration:
 * @author Nicolas11
 *         https://futures.io/platforms-indicators/31633-java-candlestick-chart-jfreechart.html
 */
public class CandlestickChart extends JPanel {

	/**
	 * Constructor responsible for generating Candlestick charts
	 * 
	 * @param name
	 * @param sUrl
	 */
	public CandlestickChart(String name, String sUrl) {
		super();

		List<OHLCDataItem> dataFeed = new ArrayList<OHLCDataItem>();
		try {
			String strUrl = sUrl;
			URL url = new URL(strUrl);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			DateFormat df = new SimpleDateFormat("y-M-d");

			String inputLine;
			in.readLine();
			while ((inputLine = in.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(inputLine, ",");

				Date date = df.parse(st.nextToken());
				double open;
				double high;
				double low;
				double close;
				double volume;
				try {
					open = Double.parseDouble(st.nextToken());
					high = Double.parseDouble(st.nextToken());
					low = Double.parseDouble(st.nextToken());
					close = Double.parseDouble(st.nextToken());
					volume = Double.parseDouble(st.nextToken());
				} catch (Exception e) {
					open = 0;
					high = 0;
					low = 0;
					close = 0;
					volume = 0;
				}

				OHLCDataItem item = new OHLCDataItem(date, open, high, low, close, volume);
				dataFeed.add(item);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Produce usable data and chart
		OHLCDataItem[] data = dataFeed.toArray(new OHLCDataItem[dataFeed.size()]);
		OHLCDataset dataset = new DefaultOHLCDataset(name, data);
		JFreeChart chart = ChartFactory.createCandlestickChart(name, "Time", "Price", dataset, false);
		chart.setBackgroundPaint(Color.DARK_GRAY);
		// remove outline from candles
		chart.getXYPlot().setRenderer(new MyCandlestickRenderer());

		// Grid Design
		XYPlot plot = (XYPlot) chart.getPlot();

		plot.setBackgroundPaint(Color.DARK_GRAY);
		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.lightGray);
		plot.setRangeGridlinePaint(Color.lightGray);

		((NumberAxis) plot.getRangeAxis()).setAutoRangeIncludesZero(false);
		if (ReadExcelFile.getSize(sUrl) < 255) {
			((DateAxis) plot.getDomainAxis()).setTimeline(SegmentedTimeline.newMondayThroughFridayTimeline());
		}
		((CandlestickRenderer) plot.getRenderer()).setDrawVolume(false);
		((CandlestickRenderer) plot.getRenderer()).setMaxCandleWidthInMilliseconds(8000000);

		// Initialise frame for the chart
		JFrame myFrame = new JFrame();
		myFrame.setResizable(true);
		myFrame.add(new ChartPanel(chart), BorderLayout.CENTER);
		Toolkit kit = Toolkit.getDefaultToolkit();
		Insets insets = kit.getScreenInsets(myFrame.getGraphicsConfiguration());
		Dimension screen = kit.getScreenSize();
		myFrame.setSize((int) (screen.getWidth() - insets.left - insets.right),
				(int) (screen.getHeight() - insets.top - insets.bottom));
		myFrame.setLocation((int) (insets.left), (int) (insets.top));
		myFrame.setVisible(true);
	}

}
