package stripchart;

/* ===============
* JStripChart.java
* ===============
* A class to implement strip charts; relies heavily on JFreeChart open source
* d.cassard 7/2/02
*/

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;   // only used for dumping height/width

import java.util.Date;
import java.util.ResourceBundle;
import java.util.Properties;

import javax.swing.*;

import com.jrefinery.data.*;
import com.jrefinery.chart.*;
import com.jrefinery.chart.combination.*;
import com.jrefinery.chart.data.*;
import com.jrefinery.chart.demo.*;

/**
 * A JStripChart is a JFreeChartPanel
 */
public class JStripChart extends JFreeChartPanel
{

	// constants
	// title font not really used since we pass a null title
	private static final Font TITLE_FONT = new Font("SansSerif", Font.PLAIN, 10);

	// some defaults
	private static final String JSC_DEFAULT_PREFERRED_WIDTH = "680";
	private static final String JSC_DEFAULT_PREFERRED_HEIGHT = "420";
	private static final String JSC_DEFAULT_MINIMUM_WIDTH = "300";
	private static final String JSC_DEFAULT_MINIMUM_HEIGHT = "200";



	// Creates a strip chart panel; no arguments, all defaults
	public JStripChart()
	{
		this( null, null, null, null, null );
	}   // end of JStripChart minimal constructor


	// Creates a strip chart panel; title and data argument
	public JStripChart(String title,
	                   StreamXYDataset data)
	{
		this( title, null, null, data, null );
	}   // end of JStripChart title,data constructor


	// Creates a strip chart panel; 3 arguments
	public JStripChart( String title,
	                    StreamXYDataset data,
	                    XYPlot plot)
	{
		this( title, null, null, data, plot );
	}   // end of JStripChart constructor

	// Creates a strip chart panel; maximal arguments
	public JStripChart( String title, String domain, String range,
	                    StreamXYDataset data,
	                    XYPlot plot)
	{
		super(createJFreeChart( title, domain, range, data, plot ));
	}   // end of JStripChart constructor

	// Creates a strip chart panel; properties instead of arguments
	public JStripChart( Properties property,
	                    StreamXYDataset data,
	                    XYPlot plot)
	{
		super(createJFreeChartProp( property, data, plot ));

		Integer prefWidth;  // preferred
		Integer prefHeight;
		Integer minWidth;   // minimum draw area
		Integer minHeight;

		prefWidth = new Integer( property.getProperty( "pref_width", JSC_DEFAULT_PREFERRED_WIDTH ) );

		prefHeight = new Integer( property.getProperty( "pref_height", JSC_DEFAULT_PREFERRED_HEIGHT ) );

		minWidth = new Integer( property.getProperty( "min_width", JSC_DEFAULT_MINIMUM_WIDTH ) );

		minHeight = new Integer( property.getProperty( "min_height", JSC_DEFAULT_MINIMUM_HEIGHT ) );

		// set drawing areas
		this.setPreferredSize( new Dimension( prefWidth.intValue(), prefHeight.intValue() ) );
		this.minimumDrawArea = new Rectangle2D.Double(0, 0, minWidth.intValue(), minHeight.intValue());

	}   // end of JStripChart constructor


	// arrange any defaults and create the chart
	private static JFreeChart createJFreeChartProp( Properties property,
	        StreamXYDataset data,
	        XYPlot plot)
	{
		// local copy of arguments
		String title;   // chart title; from property file; put this in range argument
		// final String domain = "time";  // domain is always time
		// String range;   // dont use; chart title instead

		StreamXYDataset theData;    // data set; passed in or defaulted empty here

		XYPlot thePlot;
		NumberAxis xAxis;
		NumberAxis yAxis;

		JFreeChart chart;   // chart returned

		//-- get each property value from given Properties; else apply default value

		title = property.getProperty( "title", "JStripChart default title" );


		// either accept the given data set, or create a new (empty) one
		if ( data == null)
		{
			// create a dataset
			theData = new StreamXYDataset();
		}
		else
		{
			theData = data;
		}

		// either accept the given plot, or create a new one
		if ( plot == null)
		{
			// create an XYPlot
			xAxis = new HorizontalNumberAxis( null );
			xAxis.setAutoRangeIncludesZero( false );
			xAxis.setTickLabelsVisible( true );
			// xAxis.setCrosshairVisible( true );

			yAxis = new VerticalNumberAxis( title );
			yAxis.setAutoRangeIncludesZero( false );
			yAxis.setTickLabelsVisible( true );
			yAxis.setLabelInsets( new Insets( 0, 0, 1, 0));
			// yAxis.setCrosshairVisible( true );

			thePlot = new XYPlot(xAxis, yAxis);
			thePlot.setXYItemRenderer(new StandardXYItemRenderer(StandardXYItemRenderer.LINES));
			// thePlot.setXYItemRenderer(new StandardXYItemRenderer(StandardXYItemRenderer.SHAPES));
			// thePlot.setXYItemRenderer(new StandardXYItemRenderer(StandardXYItemRenderer.SHAPES_AND_LINES));
			thePlot.setInsets( new Insets( 0, 0, 0, 0 ) );
		}
		else
		{
			thePlot = plot;
			// needed to keep debug/dump happy
			xAxis = (NumberAxis) plot.getDomainAxis();
			yAxis = (NumberAxis) plot.getRangeAxis();
		}

		// create a chart associated with everything
		chart = new JFreeChart( theData, thePlot, null, TITLE_FONT, false );

		/* remove
		// debug printout
		if( title.equals( "X <units>" ) ) {
		    System.out.println( "" );
		    System.out.println( "chart title: " + title );
		    System.out.println( "----------------------------------------" );
		    Dimension pref = this.getPreferredSize();
		    System.out.println( "preferred width: " + pref.getWidth() );
		    System.out.println( "preferred height: " + pref.getHeight() );

		    Rectangle2D da = this.getMinimumDrawArea();
		    System.out.println( "min draw width: " + da.getWidth() );
		    System.out.println( "min draw height: " + da.getHeight() );
		}

		Dimension pref = this.getPreferredSize();
		System.out.println( "preferred width: " + pref.getWidth() );
		System.out.println( "preferred height: " + pref.getHeight() );

		Rectangle2D da = this.getMinimumDrawArea();
		System.out.println( "min draw width: " + da.getWidth() );
		System.out.println( "min draw height: " + da.getHeight() );

		if( xAxis.isAutoTickUnitSelection() ) {
		    System.out.println( "xAxis tick unit selection: true" );
		}
		if( yAxis.isAutoTickUnitSelection() ) {
		    System.out.println( "yAxis tick unit selection: true" );
		}
		if( xAxis.isGridLinesVisible() ) {
		    System.out.println( "xAxis grid lines visible: true" );
		}
		if( yAxis.isGridLinesVisible() ) {
		    System.out.println( "yAxis grid lines visible: true" );
		}

		Insets inset = xAxis.getLabelInsets();
		System.out.println( "x-axis label insets: " + inset.toString() );
		inset = xAxis.getTickLabelInsets();
		System.out.println( "x-axis tick label insets: " + inset.toString() );

		inset = yAxis.getLabelInsets();
		System.out.println( "y-axis label insets: " + inset.toString() );
		inset = yAxis.getTickLabelInsets();
		System.out.println( "y-axis tick label insets: " + inset.toString() );

		// end debug printout
		 */ // end remove

		// return the chart
		return ( chart );

	} // end of createJFreeChartProp


	// arrange any defaults and create the chart
	private static JFreeChart createJFreeChart( String title, String domain, String range,
	        StreamXYDataset data, XYPlot plot)
	{
		// local copy of arguments
		String theTitle;
		String theDomain;
		String theRange;
		StreamXYDataset theData;
		XYPlot thePlot;
		NumberAxis xAxis;
		NumberAxis yAxis;

		if ( title == null)
		{
			theTitle = "JStripChart default title";
		}
		else
		{
			theTitle = title;
		}

		if ( domain == null)
		{
			theDomain = "JStripChart default domain";
		}
		else
		{
			theDomain = domain;
		}

		if ( range == null)
		{
			theRange = "JStripChart default range";
		}
		else
		{
			theRange = range;
		}

		if ( data == null)
		{
			// create a dataset
			theData = new StreamXYDataset();
		}
		else
		{
			theData = data;
		}

		if ( plot == null)
		{
			// create an XYPlot
			xAxis = new HorizontalNumberAxis( domain );
			xAxis.setAutoRangeIncludesZero( false );
			xAxis.setTickLabelsVisible( true );

			yAxis = new VerticalNumberAxis( range );
			yAxis.setAutoRangeIncludesZero( false );
			yAxis.setTickLabelsVisible( true );

			thePlot = new XYPlot(xAxis, yAxis);
			thePlot.setXYItemRenderer(new StandardXYItemRenderer(StandardXYItemRenderer.LINES));
		}
		else
		{
			thePlot = plot;
		}

		// return a chart associated with everything
		return ( new JFreeChart( theData, thePlot, theTitle, JFreeChart.DEFAULT_TITLE_FONT, false ) );

	} // end of createJFreeChart

}   // end of class JStripChart

