package rocketview.ui;

import rocketview.util.Args;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.metal.MetalLookAndFeel;

import java.util.*;

import java.awt.*;

public final class UiUtil
{
    /**
       Adds a label and a corresponding value to a container.

       <p>This function adds a label and a read-only view of its
       corresponding value to a container.  The name appears on the
       left, followed by a colon and a space, and finally by the value
       on a single row.

       <p>If the value is greater than {@link
       UiConsts.MAX_LABEL_LENGTH}, it is truncated, an ellipsis is
       appended, and the whole value is set to the corresponding
       tooltip.

       @param container The container that the name/value pair will be
       added to.
       @param name The text of the label.
       @param value The corresponding possibly null value to be
       displayed.
       @param c A constraint to be used in the placement of the name.
       This constraint is also used for the placement of value
       except that gridx is incremented by one and if weightOnDisplay
       is true weightx isn't used.
       @param weightOnDisplay If true, excess space is given to the
       value and not the label by specifying a weightx of 1.0.

       @return The JLabel that was created for the value.
       
       @throws IllegalArgumentException If container isn't managed by
       a GridBagLayout.
    */
    public static JLabel addSimpleDisplayField(Container container, String name, 
                                        Object value, GridBagConstraints c, 
                                        boolean weightOnDisplay) 
    {
        Args.checkForGridBagLayout(container);

        JLabel valueLabel = new JLabel("");

        container.add(new JLabel(name + ": "), c);

        if (value != null)
        {
            String sValue = value.toString();
            StringBuffer sbValue = new StringBuffer(sValue);

            if (sbValue.length() > UiConstants.MAX_LABEL_LENGTH)
            {
                sbValue.setLength(UiConstants.MAX_LABEL_LENGTH);
                sbValue.insert(UiConstants.MAX_LABEL_LENGTH - 3, "...");
            }

            valueLabel.setText(sbValue.toString());
            valueLabel.setToolTipText(sValue);
        }

        c.gridx = c.gridx + 1;
        c.weightx = weightOnDisplay ? 1.0 : 0.0;
        container.add(valueLabel, c);

        return valueLabel;
    }

    /**
       Adds any number of name value pairs to a container.
       
       Each name value pair is added to the container in the
       fashion described in {@link addSimpleDisplayField}.  The order
       in which they are displayed is the iteration order of
       nameValuePairs.

       @param container The container that he name value pairs will be
       added to.
       @param nameValuePairs A map keyed by String names that to a
       possibly null reference or an object.

       @throws IllegalArgumentException If container isn't managed by
       a GridBagLayout.
    */
    public static void addSimpleDisplayFields(Container container, 
            Map nameValuePairs)
    {
        Args.checkForGridBagLayout(container);

        GridBagConstraints c = getConstraints(0, 0);
        int rowCnt = 0;

        for (Iterator i = nameValuePairs.entrySet().iterator(); i.hasNext(); )
        {
            Map.Entry entry = (Map.Entry)i.next();

            c.gridx = 0;
            c.gridy = rowCnt++;
            addSimpleDisplayField(container, entry.getKey().toString(), 
                                   entry.getValue().toString(), c, true);
        }        
    }

    /**
       Adds a JLabel and JTextField pair to a Container.

       This method creates a typical JLabel/JTextField pair that is
       needed for data entry purposes and adds them to a Container.
       
       <p>The JLabel is placed just to the left of the JTextField; the
       weightx of the JTextField depends of the weightx value of the
       c; the given mnemonic value is setup to give focus to the
       JTextField; and a colon and an empty space are appended to the
       name.
    
       @param container The Container that is managed by a
       GridBagLayout that the JLabel and JTextField will be added to.
       @param name The text to used to label the JTextField.
       @param initialValue An initial value for the JTextField.
       @param mnemonic The KeyEvent field to be used as the mnemonic
       for the JLabel
       @param c The constraints used to place the JLabel in the
       Container.  Also, the values of c are used to place the
       JTextField as well except that gridx is incremented by one,
       and, if the weightx of c is 0.0, the weightx applied to the
       JTextField will be 1.0.
       @param tooltip A possibly null value to be used as the tooltip
       for the JTextField.  If the value is null, no tooltip will be
       displayed.

       @return The TextField that was created.

       @throws IllegalArgumentException If container isn't managed by
       a GridBagLayout.
    */
    public static JTextField addSimpleEntryField(Container container, 
            String name, String initialValue, int mnemonic, 
            GridBagConstraints c, String tooltip)
    {
        Args.checkForGridBagLayout(container);
        Args.checkForNull(name);

        JTextField textField = new JTextField(UiConstants.SIMPLE_FIELD_WIDTH);
        JLabel label = new JLabel(name + ": "); 

        label.setDisplayedMnemonic(mnemonic);
        label.setLabelFor(textField);
        container.add(label, c);

        if (initialValue != null)
            textField.setText(initialValue);

        if (tooltip != null)
            textField.setToolTipText(tooltip);

        if (c.weightx == 0.0)
            c.weightx = 1.0;

        c.gridx++;
        container.add(textField, c);                

        return textField;
    }

    /**
       Labels a JCompoent and adds it to a Container.

       This function places a JComponent into a Container with an
       accompanying JLabel.  It is intended to turn code like this

       <p><pre>
           JLabel foo = new JLabel("Foo: ");
           foo.setDisplayedMnemonic(KeyEvent.VK_F);
           somePane.add(foo, getConstraints(2,0));
    
           DefaultComboBoxModel model  = new DefaultComboBoxModel(anArray);
           this.comboBox = new JComboBox(model);
           foo.setLabelFor(this.comboBox);
           somePane.add(this.comboBox, getConstraints(2,1) );
       </pre>

       <p>into code like this

       <p><pre>
           DefaultComboBoxModel model  = new DefaultComboBoxModel(anArray);
           this.comboBox = new JComboBox(model);
           addSimpleComponent(somePane, "Foo", KeyEvent.VK_F, 
                              getConstraints(2,0), this.comboBox);            
       </pre>

       @param container The Container that is managed by a
       GridBagLayout that the JLabel and JTextField will be added to.
       @param name The text to used to label the JComponent.
       @param mnemonic The KeyEvent field to be used as the mnemonic
       for the JLabel
       @param c The constraints used to place the JLabel in the
       Container.  Also, the values of c are used to place the
       JCompoent as well except that gridx is incremented by one, and,
       if the weightx of c is 0.0, the weightx applied to the
       JCompoent will be 1.0.
       @param component A non-null component to be added to the
       Container.

       @throws IllegalArgumentException If the Container isn't managed
       by a GridBagLayout.
       @throws NullPointerException If name or component are null.
     */
    public static void addSimpleComponent(Container container, String name, 
            int mnemonic, GridBagConstraints c, JComponent component)
    {
        Args.checkForGridBagLayout(container);
        Args.checkForNull(name);
        Args.checkForNull(component);

        JLabel label = new JLabel(name + ": ");
        
        label.setDisplayedMnemonic(mnemonic);
        label.setLabelFor(component);
        container.add(label, c);

        if (c.weightx == 0.0)
            c.weightx = 1.0;

        c.gridx++;
        container.add(component, c);
    }

    /**
       Packs, centers, and shows the given Window on the screen.

       If the size of the window is greater than that of the user's
       screen, the window will use the entire display.

       @param window The window to be adjusted.
    */
    public static void centerAndShow(Window window)
    {
        Dimension screenSize = window.getToolkit().getScreenSize();
        int width, height, x, y;

        window.pack();

        width = window.getWidth();
        height = window.getHeight();

        if (height > screenSize.height)
            height = screenSize.height;

        if (width > screenSize.width)
            width = screenSize.width;

        x = (screenSize.width - width) / 2;
        y = (screenSize.height - height) / 2;

        window.setLocation(x, y);
        window.setVisible(true);
    }

    /**
       Packs, centers the Dialog with respect to its parent, and shows it.
 
       If centering the Dialog causes any part of it to be off the
       screen, the Dialog will instead be places off-center near its
       parent.

       @param dialog A Dialog to be adjusted.  Its getParent method
       must not return null.

       @throws NullPointerException If the Dialog object's getParent
       method returns null, a NullPointerException will be thrown.
    */
    public static void centerOnParentAndShow(Window dialog)
    {
        Container parent = dialog.getParent();

        Args.checkForNull(parent);

        dialog.pack();
        
        Dimension parentSize = parent.getSize(),
                  screenSize = dialog.getToolkit().getScreenSize(),
                  childSize = dialog.getSize();
        Point parentLocation = parent.getLocation();
        int width = childSize.width,
            height = childSize.height,
            x = parentLocation.x + (parentSize.width - width) / 2,
            y = parentLocation.y + (parentSize.height - height) / 2;

        if (x < 0)
            x = 0;

        if (y < 0)
            y = 0;

        if (x + width > screenSize.width)
            x = screenSize.width - width;

        if (y + height > screenSize.height)
            y = screenSize.height - childSize.height;

        dialog.setLocation(x, y);
        dialog.setVisible(true);        
    }

    /**
       Creates a column of equally sized JButtons that are uniformly spaced.

       @param buttons A collection of JButtons to use to build the column.

       @return A JComponent that only has a border on the left.  To
       apply a border on the right as well, use {@link 
       com.travisspencer.ui.UiUtil#getStandardBorder()}.

       @see {com.travisspencer.ui.UiUtil#getStandardBorder()}
       @see <a href="http://java.sun.com/products/jlf/ed1/dg/hign.htm#46791">
       Java Look and Feel Specifications</a>
     */
    public static JComponent getCommandColumn(java.util.List buttons)
    {
        if (buttons.isEmpty())
            return new JLabel();
        
        Box retval = Box.createVerticalBox();
        Border leftBorder = BorderFactory.createEmptyBorder(0, 
                UiConstants.THREE_SPACES, 0, 0);

        equalizeSizes(buttons);

        for (Iterator i = buttons.iterator(); i.hasNext();)
        {            
            JButton button = (JButton)i.next();

            retval.add(button);

            // Only add an extra spacer if there are more buttons to come.
            if (i.hasNext())
                retval.add(Box.createRigidArea(new Dimension(0, 
                        UiConstants.ONE_SPACE)));
        }

        retval.setBorder(leftBorder);
        retval.add(Box.createVerticalGlue());

        return retval;
    }

    public static JComponent getCommandRow(java.util.List buttons)
    {
        if (buttons.isEmpty())
            return new JLabel();

        Box retval = Box.createHorizontalBox();
        Border topBorder = BorderFactory.createEmptyBorder(
                UiConstants.THREE_SPACES, 0, 0, 0);

        equalizeSizes(buttons);

        retval.add(Box.createHorizontalGlue());

        //        for (JButton button : buttons)
        for (Iterator i = buttons.iterator(); i.hasNext();)
        {
            JButton button = (JButton)i.next();

            retval.add(button);

            if (i.hasNext())
                retval.add(Box.createRigidArea(new Dimension(
                        UiConstants.ONE_SPACE, 0)));
        }
        
        retval.setBorder(topBorder);
        
        return retval;
    }

    /**
       Returns a constraint with useful defaults.

       The resulting constraint will have these values:

       <ul>
       <li>gridx, gridy -- set to x and y
       <li>anchor -- GridBagConstraints.WEST
       <li>insets -- Insets(0, 0, 0, {@link UiConstants.ONE_SPACE})
       </ul>

       <p>All other values are taken from the defaults of
       GridBagConstraints.

       @param The Y-coordinate that the resulting constraint should
       have.  It should be in the range (0..10).
       @param The X-coordinate that the resulting constraint should
       have.  It should be in the range (0..10).

       @see {@link java.awt.GridBagConstraints}

    */
    public static GridBagConstraints getConstraints(int y, int x)
    {
        // TODO: Make sure x and y are within the range 0..10.

        GridBagConstraints c = new GridBagConstraints();

        c.gridx = x;
        c.gridy = y;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 0, 0, UiConstants.ONE_SPACE);

        return c;
    }
    
    public static GridBagConstraints getConstraints(int x, int y, int width, 
            int height)
    {
        GridBagConstraints c = getConstraints(x, y);

        c.gridwidth = width;
        c.gridheight = height;

        return c;
    }

    /**
       Returns a border standard sized border.

       According to the <a
       href="http://java.sun.com/products/jlf/ed1/dg/higl.htm#45541">
       Java Look and Feel specifications</a>, "you must provide
       [spacing] between the borders of the dialog box and the
       components in the dialog box."  This function returns such a
       border that is initialized on all four sides to {@link
       com.travisspencer.ui.UiConstants.STANDARD_BORDER}.

       @return The standard sized border.
    */
    public static Border getStandardBorder()
    {
        int s = UiConstants.STANDARD_BORDER;

        return BorderFactory.createEmptyBorder(s, s, s, s);
    }

    /**
       Adds glue to the last row of a JPanle's GridBag layout manager.

       Adds an invisible component that will stretch to take up any
       extra space to the last row of a JPanel when it is resized.
       The glue can be thought of more a spring, but the term glue is
       used for consitency with the the JDK.

       <p>This method is especially useful when layout components in a
       grid where horizontal expansion is desired, but vertical
       stretching isn't.

       @param panel A panel that is managed by a GridBagLayout and
       doesn't contain any components with a wieghty of any value by
       0,0.
       @param lastRowIndex The index number of the last row in the
       panel's layout manager where the glue will be inserted.

       @throws IllegalArgumentException If panel isn't
       managed by a GridBagLayout.
    */
    public static void addVerticleGridGlue(JPanel panel, int lastRowIndex)
    {
        Args.checkForGridBagLayout(panel);

        GridBagConstraints c = getConstraints(lastRowIndex, 0);

        c.weighty = 1.0;
        c.fill = GridBagConstraints.VERTICAL;
        panel.add(new JLabel(), c);
    }

    /**
       Sets the components in components to the same size.

       Sets the preferred and maximum size of each component to the
       same value.

       @param components A list of components to be resized.
    */
    public static void equalizeSizes(java.util.List components)
    {
        double maxWidth = 0, maxHeight = 0;
        Dimension d = new Dimension();

        for (Iterator e = components.iterator(); 
             e.hasNext(); /*EMPTY*/)
        {
            d = ((JComponent) e.next()).getPreferredSize();
            double curWidth = d.getWidth(), curHeight = d.getHeight();

            if (curWidth > maxWidth)
                maxWidth = curWidth;

            if (curHeight > maxHeight)
                maxHeight = curHeight;
        }

        d.setSize(maxWidth, maxHeight);

        for (Iterator e = components.iterator(); e.hasNext(); /*EMPTY*/)
        {
            JComponent c = (JComponent)e.next();

            c.setPreferredSize(d);
            c.setMaximumSize(d);
        }
    }

    /**
       Creates an alternative to the multi-line label.

       This function produces a component with superior spacing and
       wrapping than a standard multi-line JLabel that can be used to
       display lengthy strings.

       @param text A String that is not null, not empty after trimming
       spaces from both sides and doesn't contain any new lines.

       @return A JTextArea that contains the text, is wrapped on word
       boundaries as determined by the JTextArea, has a little extra
       space on each side of the text, is non-editable, and is the
       same color as the look-and-feel's menu background color.

       @throws IllegalArgumentException If text contains a newline,
       this exception is thrown.
    */
    public static JTextArea getStandardTextArea(String text)
    {
        //        Util.textHasContent(text);

        if (text.indexOf(System.getProperty("line.separator").toString()) != -1)
            throw new IllegalArgumentException(
                "The text must not contain a new line");

        JTextArea retval = new JTextArea(text);

        retval.setEditable(false);
        retval.setWrapStyleWord(true);
        retval.setLineWrap(true);
        retval.setMargin(new Insets(0, 5, 0, 5));
        retval.setBackground(MetalLookAndFeel.getMenuBackground());

        return retval;
    }

    /*
      Creates an alternative to the multi-line label using the
      newlines in the specified text.

      @param text A string that is not null and non-empty after
      trimming spaces from both sides.

      @return A JTextArea that contains the text, is wrapped on word
       boundaries as determined by the specified text, is
       non-editable, and is the same color as the look-and-feel's menu
       background color.
    */
    public static JTextArea getStandardTextAreaHardNewLines(String text)
    {
        //        Util.textHasContent(text);

        JTextArea retval = new JTextArea(text.trim());
        
        retval.setEditable(false);
        retval.setWrapStyleWord(true);
        retval.setMargin(new Insets(0, 5, 0, 5));
        retval.setBackground(MetalLookAndFeel.getMenuBackground());

        return retval;
    }

    private UiUtil()
    {
        /*EMPTY*/; 
    }
}
