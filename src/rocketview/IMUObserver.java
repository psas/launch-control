package rocketview;

import cansocket.*;
import stripchart.*;

import com.jrefinery.chart.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

class IMUObserver extends JPanel implements Observer
{
	protected static final Font TITLE_FONT = new Font("SansSerif", Font.PLAIN, 10);
	protected final Dimension preferredSize = new Dimension(400, 80);

	protected final int IMU_ACCEL = 0;
	protected final int IMU_GYRO = 1;

	protected final StreamXYDataset data[][];
	protected final String title[][] = {
		{
			"X",
			"Y",
			"Z",
		},
		{
			"Pitch",
			"Yaw",
			"Roll",
		},
	};
	protected final int freq[] = { 250, 100, };
	protected final int HIST_LEN = 30;
	protected int msgCount[] = { 0, 0, };

	public IMUObserver()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		data = new StreamXYDataset[title.length][];
		for(int i = 0; i < data.length; ++i)
		{
			data[i] = new StreamXYDataset[title[i].length];
			for(int j = 0; j < data[i].length; ++j)
			{
				data[i][j] = new StreamXYDataset(freq[i] * HIST_LEN);
				add(createChart(title[i][j], data[i][j]));
			}
		}
	}

	public void update(Observable o, Object arg)
	{
		CanMessage msg = (CanMessage) arg;
		switch(msg.getId())
		{
		case CanBusIDs.IMUAccel:
			addValues(IMU_ACCEL, msg);
			break;
		case CanBusIDs.IMUGyro:
			addValues(IMU_GYRO, msg);
			break;
		}
	}

	protected void addValues(int type, CanMessage msg)
	{
		Integer x = new Integer(msgCount[type]++);
		for(int i = 0; i < data[type].length; ++i)
		{
			Short y = new Short(msg.getData16(i));
			data[type][i].addXYValue(x, y);
		}
	}

	protected JComponent createChart(String title, StreamXYDataset data)
	{
		NumberAxis xAxis;
		NumberAxis yAxis;
		XYPlot plot;
		JFreeChart chart;
		ChartPanel chartpanel;
		JPanel panel;

		xAxis = new HorizontalNumberAxis(null);
		xAxis.setAutoRangeIncludesZero(false);
		xAxis.setTickLabelsVisible(false);

		yAxis = new VerticalNumberAxis(null);
		yAxis.setRange(0, 4096);
		yAxis.setTickLabelsVisible(false);

		plot = new XYPlot(data, xAxis, yAxis);
		plot.setRenderer(new StandardXYItemRenderer(StandardXYItemRenderer.LINES));

		chart = new JFreeChart(null, TITLE_FONT, plot, false);
		chartpanel = new ChartPanel(chart);
		chartpanel.setPreferredSize(preferredSize);

		// workaround: ChartPanel renders wrong with borders.
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(chartpanel);

		panel.setBorder(new TitledBorder(title));
		return panel;
	}
}
