package widgets;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;

public class StripChart extends JComponent
{
	protected static final Dimension MIN_CHART_DIM = new Dimension(300,50); 
	
	protected LinkedList ts = new LinkedList();
	protected LinkedList ys = new LinkedList();

	protected float timeScale = 30; /* seconds */
	protected float maxTimeScale = 60; /* seconds */
	protected float minY = -20;
	protected float maxY = 20;
	
	
	public Dimension getPreferredSize() 
	{
		return MIN_CHART_DIM;
	}

	public float getTimeScale()
	{
		return timeScale;
	}

	public void setTimeScale(float timeScale)
	{
		this.timeScale = timeScale;
	}

	public float getMaxTimeScale()
	{
		return maxTimeScale;
	}

	public void setMaxTimeScale(float maxTimeScale)
	{
		this.maxTimeScale = maxTimeScale;
	}

	public void setYRange(float minY, float maxY)
	{
		this.minY = minY;
		this.maxY = maxY;
	}

	public void addPoint(float t, float y)
	{
		synchronized(ts)
		{
			ts.add(new Float(t));
			ys.add(new Float(y));
		}
		repaint();
	}

	private float previousFloat(ListIterator it)
	{
		return ((Float) it.previous()).floatValue();
	}

	protected void paintComponent(Graphics g)
	{
		if (ts.size() == 0)
			return;

		synchronized(ts)
		{
			// start at the most recent point and draw backwards in time
			ListIterator ti = ts.listIterator(ts.size());
			ListIterator yi = ys.listIterator(ys.size());

			final float now = previousFloat(ti);
			float old = now - getTimeScale();
			int x1, y1, x2, y2;

			Dimension size = getSize();

			// scale to the window
			float sx =  size.width / getTimeScale();
			float sy = -size.height / (maxY - minY);
			float tx = -old * sx;
			float ty = -maxY * sy;

			float t = now;
			x1 = (int) (t * sx + tx + 0.5);
			y1 = (int) (previousFloat(yi) * sy + ty + 0.5);
			while(ti.hasPrevious() && (t = previousFloat(ti)) > old) 
			{
				x2 = (int) (t * sx + tx + 0.5);
				y2 = (int) (previousFloat(yi) * sy + ty + 0.5);
				g.drawLine (x1,y1, x2,y2);
				x1 = x2;
				y1 = y2;
			}
			yi = null;

			old = now - getMaxTimeScale();
			ti = ts.listIterator();
			while(ti.hasNext())
			{
				float cur = ((Float) ti.next()).floatValue();
				if(cur < now && cur > old)
					break;
			}

			// remove old points that will never be drawn again
			int lastold = ti.previousIndex();
			ts.subList(0, lastold).clear();
			ys.subList(0, lastold).clear();
		}
	}
}
