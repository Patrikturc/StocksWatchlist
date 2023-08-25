package program;

import java.awt.Paint;

import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;

/**
 * Class to adjust the design of candlesticks within the chart
 * 
 * @author twj Inspiration:
 *         https://stackoverflow.com/questions/21103801/how-do-i-set-the-body-and-border-colors-to-be-the-same-in-a-jfreechart-candlesti
 * 
 */
public class MyCandlestickRenderer extends CandlestickRenderer {

	@Override
	public Paint getItemPaint(int row, int column) {

		// determine up or down candle
		XYDataset dataset = getPlot().getDataset();
		OHLCDataset highLowData = (OHLCDataset) dataset;
		int series = row, item = column;
		Number yOpen = highLowData.getOpen(series, item);
		Number yClose = highLowData.getClose(series, item);
		boolean isUpCandle = yClose.doubleValue() > yOpen.doubleValue();

		// return the same color as that used to fill the candle
		if (isUpCandle) {
			return getUpPaint();
		} else {
			return getDownPaint();
		}
	}
}