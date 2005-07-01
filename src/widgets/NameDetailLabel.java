package widgets;

import javax.swing.*;

public class NameDetailLabel extends JLabel
{
	protected String name;
	protected String detail;

	public NameDetailLabel(String name)
	{
		super(name);
		this.name = name;
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
