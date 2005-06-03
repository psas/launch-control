package rocketview.layout;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import cansocket.*;

import rocketview.ui.UiUtil;

public class DataViewer extends JFrame implements Observer
{
    boolean initialized;
    CanListener dispatch;

    private JLabel altitudeLabel, gpsLabel, apsBusLabel, umbLabel,
            shorePowerLabel, powerToFCLabel, powerToCanLabel,
            powerToAtvLabel, powerToWifiLabel, battLabel;
    // FIXME: Figure out a way to lay this out without the extra array.  
    // This seems unnecessary.  
    private JLabel[] labels = { 
            gpsLabel, altitudeLabel, apsBusLabel, battLabel, umbLabel,
            shorePowerLabel, powerToFCLabel, powerToCanLabel, 
            powerToAtvLabel, powerToWifiLabel,
    }; 

    DataViewer(CanListener listener) {
        super();

        setContentPane(getContentPanel());
        listener.addObserver(this);
        
        dispatch = listener;        
        initialized = true;               
    }
    
    public void update(Observable observer, Object arg) {
        
    }

    private JComponent getContentPanel() {
        assert ! initialized;

        JSplitPane contentPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                getSubsystemInfoPanel(), getCanDataTable());

        return contentPanel;
    }

    private JPanel getSubsystemInfoPanel() {
        assert ! initialized;

        JPanel subsystemInfoPanel = new JPanel();
        LayoutManager layoutMgr = new GridBagLayout();

        subsystemInfoPanel.setLayout(layoutMgr);
        initializeGeneralDataLabels(subsystemInfoPanel);

        return subsystemInfoPanel;
    }

    private void initializeGeneralDataLabels(JPanel container) {
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
        
        UiUtil.addVerticleGridGlue(container, labels.length);
    }

    private JPanel getCanDataTable() {
        assert ! initialized;

        JPanel canDataTable = new CanDataTable();

        return canDataTable;
    }

    public static void main(String[] args) throws IOException {
        StringBuffer tmpDir = new StringBuffer(System.getProperty("java.io.tmpdir"));
        String log = tmpDir.append(File.pathSeparator).append("dv.log").toString();
        CanSocket socket = new LogCanSocket(new UDPCanSocket(), log); 
        CanListener listener = new CanListener(socket);
        DataViewer frame = new DataViewer(listener);
        Dimension screenSize = frame.getToolkit().getScreenSize();

        frame.setTitle("Rocketview DataViewer Driver");
        frame.setBounds(screenSize.width / 4, screenSize.height / 4,
                screenSize.width / 2, screenSize.height / 2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
