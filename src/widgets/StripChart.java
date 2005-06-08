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

			float old = t - maxTimeScale;
			ListIterator it = ts.listIterator();
			while(it.hasNext())
			{
				float cur = ((Float) it.next()).floatValue();
				if(cur < t && cur > old)
					break;
			}
			int lastold = it.previousIndex();
			ts.subList(0, lastold).clear();
			ys.subList(0, lastold).clear();
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
			ListIterator ti = ts.listIterator(ts.size());
			ListIterator yi = ys.listIterator(ys.size());

			float t;
			float now = previousFloat(ti);
			float old = now - getTimeScale();
			float x1, y1, x2, y2;

			Dimension size = getSize();

			float sx =  size.width / getTimeScale();
			float sy = -size.height / (maxY - minY);
			float tx = -old * sx;
			float ty = -maxY * sy;

			x1 = now;
			y1 = previousFloat(yi);
			while(ti.hasPrevious() && (t = previousFloat(ti)) > old) 
			{
				x2 = t;
				y2 = previousFloat(yi);
				g.drawLine ((int) (x1 * sx + tx + 0.5),
					    (int) (y1 * sy + ty + 0.5),
					    (int) (x2 * sx + tx + 0.5),
					    (int) (y2 * sy + ty + 0.5));
				x1 = x2;
				y1 = y2;
			}
		}
	}
}
