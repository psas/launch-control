package widgets;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;

public class StripChart extends JComponent
{
	protected static final Dimension MIN_CHART_DIM = new Dimension(300,50); 
	
	protected ArrayList ts = new ArrayList();
	protected ArrayList ys = new ArrayList();

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

	private float constructPath(GeneralPath path)
	{
		float old;
		synchronized(ts)
		{
			ListIterator ti = ts.listIterator(ts.size());
			ListIterator yi = ys.listIterator(ys.size());

			float t;
			float now = previousFloat(ti);
			old = now - getTimeScale();

			path.moveTo(now, previousFloat(yi));
			while(ti.hasPrevious() && (t = previousFloat(ti)) > old)
				path.lineTo(t, previousFloat(yi));
		}

		return old;
	}

	protected void paintComponent(Graphics g_base)
	{
		if(ts.size() == 0)
			return;

		Graphics2D g = (Graphics2D) g_base;
		AffineTransform oldTransform = g.getTransform();
		GeneralPath path = new GeneralPath();
		float old = constructPath(path);

		Dimension size = getSize();
		/* Note: scaling Y by negative factor because Java2D's
		 * Y axis is positive down, and we want positive up. */
		g.scale(size.width / getTimeScale(), -size.height / (maxY - minY));

		g.translate(-old, -maxY);

		g.draw(path);
		g.setTransform(oldTransform);
	}
}
