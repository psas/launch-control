/* --------------------
* StreamXYDataset.java
* --------------------
* Original Author:  David Cassard;
* Contributor(s):   -;
*
* $Id$
*
* Changes
* --------------------------
* 29-Jun-2002 : Created with SampleXYDataset as a model (dgc);
*/

import com.jrefinery.data.*;
import com.jrefinery.chart.*;

/**
 * A streaming dataset for an XY plot:
 *      AbstractSeriesDataset and XYDataset are from JFreeChart
 *      CanRenderer is for messages from the CANBus
 */
public class StreamXYDataset extends AbstractSeriesDataset implements XYDataset
{

	// array to hold incoming data messages, which acts as a circular buffer
	//   empty: earliest = next
	//   full: earliest = (next+1)%x.length
	protected final Number x[];
	protected final Number y[];

	// indexes into data array
	private int earliest = 0;       // earliest data item received
	private int next = 0;           // next data item to be received

	/**
	 * Default constructor.
	 */
	public StreamXYDataset()
	{
		this(10);
	}

	public StreamXYDataset(int qty)
	{
		x = new Number[qty];
		y = new Number[qty];
	}

	/**
	 * Returns the x-value for the specified series and item.  Series are numbered 0, 1, ...
	 * @param series The index (zero-based) of the series;
	 * @param item The index (zero-based) of the required item;
	 * @return The x-value for the specified series and item.
	 */
	public Number getXValue(int series, int item)
	{
		return x[(earliest + item) % x.length];
	}

	/**
	 * Returns the y-value for the specified series and item.  Series are numbered 0, 1, ...
	 * @param series The index (zero-based) of the series;
	 * @param item The index (zero-based) of the required item;
	 * @return The y-value for the specified series and item.
	 */
	public Number getYValue(int series, int item)
	{
		return y[(earliest + item) % y.length];
	}

	/**
	 * Returns the number of series in the data source.
	 * @return The number of series in the data source.
	 */
	public int getSeriesCount()
	{
		return 1;
	}

	/**
	 * Returns the name of the series.
	 * @param series The index (zero-based) of the series;
	 * @return The name of the series.
	 */
	public String getSeriesName(int series)
	{
		return "...le series...";
	}

	/**
	 * Return number of items in view
	 *
	 * Returns the number of items in the specified series.
	 * @param series The index (zero-based) of the series;
	 * @return The number of items in the specified series.
	 */
	public int getItemCount(int series)
	{
		return (next - earliest + x.length) % x.length;
	}

	public void addXYValue(Number xv, Number yv)
	{
		if ( earliest == ((next + 1) % x.length) )
			earliest = (earliest + 1) % x.length;   // drop earliest

		x[ next ] = xv;                     // insert at next
		y[ next ] = yv;                     // insert at next
		next = (next + 1) % x.length;       // increment next

		// tell listeners
		notifyListeners( new DatasetChangeEvent( this ) );
	}
}   // end of class StreamXYDataset
