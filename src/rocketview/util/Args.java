package rocketview.util;

import java.awt.Container;
import java.awt.GridBagLayout;

public final class Args
{
    /**
       Insures that a Container is managed by a GridBagLayout.

       @throws IllegalArgumentException If the Container isn't managed
       by a GridBagLayout.
    */
    public static void checkForGridBagLayout(Container c) 
        throws IllegalArgumentException
    {
        if (! (c.getLayout() instanceof GridBagLayout))
        {
            final StringBuffer errorMsg = new StringBuffer();

            errorMsg.append("The Container must be managed by a GridBagLayout. ")
                .append("The one supplied is managed by a ")
                .append(c.getLayout().getClass().getName()); 

            throw new IllegalArgumentException(errorMsg.toString());
        }
    }

    /**
       Insures that an Object is not null and throws a
       NullPointerException if it is.

       @throws NullPointerException If the Object is null.
    */
    public static void checkForNull(Object o)
    {
        if (o == null)
        {
            String errorMsg = "The object cannot be null";

            throw new NullPointerException(errorMsg);
        }
    }

    private Args()
    {
        /*EMPTY*/ ;
    }
}
