package rocketview.layout;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import rocketview.ui.UiUtil;

public class DataViewer extends JFrame
{
    boolean initialized;

    private JLabel altitudeLabel, gpsLabel, apsBusLabel, umbLabel,
            shorePowerLabel, powerToFCLabel, powerToCanLabel,
            powerToAtvLabel, powerToWifiLabel, battLabel;
    private JLabel[] labels = { 
            gpsLabel, altitudeLabel, apsBusLabel, battLabel, umbLabel,
            shorePowerLabel, powerToFCLabel, powerToCanLabel, 
            powerToAtvLabel, powerToWifiLabel,
    }; 

    DataViewer() {
        super();

        this.setContentPane(getContentPanel());
        initialized = true;
    }

    private JPanel getContentPanel() {
        assert ! initialized;

        JPanel contentPanel = new JPanel(new BorderLayout());

        contentPanel.add(getGeneralDataPanel(), BorderLayout.WEST);
        contentPanel.add(getCanDataTable(), BorderLayout.CENTER);

        return contentPanel;
    }

    private JPanel getGeneralDataPanel() {
        assert ! initialized;

        JPanel generalDataPanel = new JPanel();
        LayoutManager layoutMgr = new GridBagLayout();

        generalDataPanel.setLayout(layoutMgr);
        initializeGeneralDataLabels(generalDataPanel);
        
        for (int i = 0; i < labels.length; i++)
            generalDataPanel.add(labels[i]);
        
        /*
        generalDataPanel.add(altitudeLabel);
        generalDataPanel.add(gpsLabel);
        generalDataPanel.add(apsBusLabel);
        generalDataPanel.add(battLabel);
        generalDataPanel.add(umbLabel);
        generalDataPanel.add(shorePowerLabel);
        generalDataPanel.add(powerToFCLabel);
        generalDataPanel.add(powerToCanLabel);
        generalDataPanel.add(powerToAtvLabel);
        generalDataPanel.add(powerToWifiLabel);*/

        return generalDataPanel;
    }

    private void initializeGeneralDataLabels(Container container) {
        assert ! initialized;

        final String u = "Unknown";
        GridBagConstraints c = UiUtil.getConstraints(0, 0);
        int rowCnt = 0;
        // FIXME: Order matters here. These need to corresponde to the order in 
        // which they apeare in the `labels' array :(
        String[][] nameValuePairs = {
                {"GPS", u},
                {"Altitude", u},    
                {"APS Bus", "xx.xxV x.xxA"},
                {"Batt", "xx.xxx/AHr"},
                {"UMB Connected", u},
                {"Shore Power", u},
                {"Power to S1 (FC)", u},
                {"Power to S2 (CAN)", u},
                {"Power to S3 (ATV Pwr Amp)", u},
                {"Power to S4 (Wifi pwr Amp)", u},
        };              

        for (int i = 0; i < nameValuePairs.length; i++) {
            c.gridx = 0;
            c.gridy = rowCnt++;
            labels[i] = UiUtil.addSimpleDisplayField(container, 
                    nameValuePairs[i][0], nameValuePairs[i][1], c, true);
        }                
    }

    private JPanel getCanDataTable() {
        assert ! initialized;

        JPanel canDataTable = new JPanel();

        return canDataTable;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ex) {
            /*EMPTY*/ ; // See "Thinking in Java" 3rd edition page 852.
        }

        JFrame frame = new DataViewer();
        Dimension screenSize = frame.getToolkit().getScreenSize();

        // Initialize the object.
        frame.setTitle("Rocketview DataViewer Driver");
        frame.setBounds(screenSize.width / 4, screenSize.height / 4,
        screenSize.width / 2, screenSize.height / 2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.pack();
        frame.setVisible(true);
    }
}
