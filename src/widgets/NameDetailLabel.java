package widgets;

import javax.swing.*;

public class NameDetailLabel extends JLabel
{
	protected String name;
	protected String detail;

	//Blank icon forces alignment of all labels, even those without
	//real graphics.
	protected ImageIcon blank = new ImageIcon(ClassLoader.getSystemResource("widgets/blankicon.png"));

	public NameDetailLabel(String name)
	{
		super(name);
		setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		this.name = name;
		setIcon(blank);
	}

	public NameDetailLabel(String name, String detail)
	{
		this(name);
		setDetail(detail);
	}

	public void setText(String name)
	{
		this.name = name;
		update();
	}

	public void setDetail(String detail)
	{
		this.detail = detail;
		update();
	}

	private void update()
	{
		if(detail == null)
			super.setText(name);
		else
			super.setText(name + ": " + detail);
	}
}
