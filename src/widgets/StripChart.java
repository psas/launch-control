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

	protected void paintComponent(Graphics g)
	{
		if (ts.size() == 0)
			return;

		int i;
		int[] lx, ly;

		synchronized(ts)
		{
			final float now = ((Float) ts.get(ts.size() - 1)).floatValue();
			float old;
			ListIterator ti, yi;

			// remove old points that will never be drawn again
			old = now - getMaxTimeScale();
			ti = ts.listIterator();
			while(ti.hasNext())
			{
				float cur = ((Float) ti.next()).floatValue();
				if(cur < now && cur > old)
					break;
			}

			int lastold = ti.previousIndex();
			ts.subList(0, lastold).clear();
			ys.subList(0, lastold).clear();

			// start at the most recent point and draw backwards in time
			old = now - getTimeScale();
			lx = new int[ts.size()];
			ly = new int[ys.size()];

			Insets insets = getInsets();
			int width = getWidth() - insets.left - insets.right;
			int height = getHeight() - insets.top - insets.bottom;

			// scale to the window
			float sx =  width / getTimeScale();
			float sy = -height / (maxY - minY);
			float tx = -old * sx + insets.left + 0.5f;
			float ty = -maxY * sy + insets.top + 0.5f;

			ti = ts.listIterator(ts.size());
			yi = ys.listIterator(ys.size());

			for(i = 0; ti.hasPrevious(); ++i)
			{
				float t = ((Float) ti.previous()).floatValue();
				float y = ((Float) yi.previous()).floatValue();
				if(t <= old)
					break;
				lx[i] = (int) (t * sx + tx);
				ly[i] = (int) (y * sy + ty);
			}
		}
		g.drawPolyline(lx, ly, i);
	}
}
