package rocketview;

import cansocket.*;
import stripchart.*;

/* import jfreechart classes */
import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.*;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

/*----------------------------------------------------------------
 * Handles message ids IMUAccel & IMUGyro
 * Updates the 6 stripcharts
 *
 * for IMUAccel expect 3 short ints (6 bytes total): x, y, z
 * for IMUGyro expect 3 short ints (6 bytes total): pitch, yaw, roll
 *
 * chart vertical axis expects values 0-4096 opaque units
 */
class IMUObserver extends JPanel implements Observer
{
	// protected static final Font TITLE_FONT =
	// 	new Font("SansSerif", Font.PLAIN, 10);
	protected static final Font TITLE_FONT =
	    new Font( "Dialog", Font.BOLD, 12 );

	// protected final Dimension preferredSize = new Dimension(400, 80);
	// protected final Dimension preferredSize = new Dimension(500, 100);
	protected final Dimension preferredSize = new Dimension(500, 80);

	// first subscript in arrays is one of these
	protected final int IMU_ACCEL = 0;
	protected final int IMU_GYRO = 1;

	protected final StreamXYDataset data[][];
	protected final String title[][] = {
		{ "X", "Y", "Z", },
		{ "Pitch", "Yaw", "Roll", }
	};

	// low limit, graph vertical axis
	protected final int vLow[][] = {
		{ -5, -5, -15 },
		{ 0, 0, 0 }
	};

	// high limit, graph vertical axis
	protected final int vHigh[][] = {
		{ 5, 5, 15 },
		{ 4096, 4096, 4096 }
	};

	protected final int freq[] = { 250, 100, };
	// protected final int HIST_LEN = 10;
	protected final int HIST_LEN = 1;
	protected int msgCount[] = { 0, 0, };

	// constants to convert raw values to G's
	// subscript 0: XYZ; subscript 1: pitch/yaw/roll
	protected final double Gzero[][] = {
	    { 2400.45, 2462.06, 1918.72 },
	    { 0.0, 0.0, 0.0 }
	};

	protected final double Ggain[][] = {
	    { 392.8, 386.9, 77.0 },
	    { 0.0, 0.0, 0.0 }
	};

	protected final double Qzero[] = { 1907.53, 0.0 };
	protected final double Qgain[] = { 75.4, 0.0 };

	public IMUObserver()
	{
	    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	    data = new StreamXYDataset[title.length][];
	    for(int i = 0; i < data.length; ++i)
	    {
		data[i] = new StreamXYDataset[title[i].length];
		for(int j = 0; j < data[i].length; ++j)
		{
		    int len = freq[i] * HIST_LEN;
		    data[i][j] = new StreamXYDataset(len);
		    this.add(createChart(title[i][j], data[i][j], len, i, j));
		}
	    }
	}

	public void update(Observable o, Object arg)
	{
		CanMessage msg = (CanMessage) arg;
		switch(msg.getId11())
		{
		case CanBusIDs.IMUAccel:
			// System.out.println( "imuaccel" );
			addGValues(IMU_ACCEL, msg);
			break;
		case CanBusIDs.IMUGyro:
			// System.out.println( "imugyro" );
			addRawValues(IMU_GYRO, msg);
			break;
		default:	// message not for me; ignore
		}
	}

	// use conversion constants to convert X/Y/Z to G's
	// h = horizontal axis; v = vertical axis
	protected void addGValues(int type, CanMessage msg)
	{
		Integer h = new Integer(msgCount[type]++);
		for(int i = 0; i < data[type].length; ++i)
		{
		    short raw = (short) (msg.getData16(i));
		    short vShort = (short)
			(((double) raw - Gzero[type][i]) / Ggain[type][i]);
		    Short v = new Short( vShort );

		    // System.out.println( "type:  " + type
			// + "  index: " + i
			// + "    raw: " + raw
			// + "   zero: " + Gzero[type][i]
			// + "   gain: " + Ggain[type][i]
			// + "   h: " + h
			// + "   v: " + v );
		    data[type][i].addXYValue( h, v );
		}
	}

	// until we get conversion constants, Yaw/Pitch/Roll
	// just display raw values
	protected void addRawValues(int type, CanMessage msg)
	{
		Integer x = new Integer(msgCount[type]++);
		for(int i = 0; i < data[type].length; ++i)
		{
		    Short y = new Short(msg.getData16(i));
		    // System.out.println( "type:  " + type
			// + "  index: " + i
			// + "   x: " + x
			// + "   y: " + y );
		    data[type][i].addXYValue(x, y);
		}
	}

	// Here we create each chart and set all settings
	protected JComponent createChart(
		String title, StreamXYDataset data, int len,
		int type, int num )
	{
		NumberAxis xAxis;
		NumberAxis yAxis;
		XYPlot plot;
		JFreeChart chart;
		ChartPanel chartpanel;
		JPanel panel;

		xAxis = new HorizontalNumberAxis(null);
		// xAxis.setAutoRangeIncludesZero(false);
		xAxis.setAutoRangeIncludesZero(true);
		xAxis.setFixedAutoRange(len);
		xAxis.setTickLabelsVisible(false);
		// xAxis.setTickLabelsVisible( true );

		yAxis = new VerticalNumberAxis(null);
		yAxis.setRange(vLow[type][num], vHigh[type][num]);
		// yAxis.setTickLabelsVisible(false);
		yAxis.setTickLabelsVisible( true);

		plot = new XYPlot(data, xAxis, yAxis);
		plot.setRenderer(
		    new StandardXYItemRenderer(
			StandardXYItemRenderer.LINES));

		// this is how it should work
		// chart = new JFreeChart(title, TITLE_FONT, plot, false);
		// chartpanel = new ChartPanel(chart);
		// chartpanel.setPreferredSize(preferredSize);
		// return chartpanel;

		// but ChartPanel renders wrong with borders so the
		// workaround is to create chart with null title
		// and put it in a JPanel with a title
		chart = new JFreeChart(null, TITLE_FONT, plot, false);
		chartpanel = new ChartPanel(chart);
		chartpanel.setPreferredSize(preferredSize);

		panel = new JPanel();
		panel.setBorder(new TitledBorder(title));
		panel.add(chartpanel);
		return panel;
	}
}
