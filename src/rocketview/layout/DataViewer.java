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

    DataViewer(CanListener listener) {
        super();
        
        setContentPane(getContentPanel());
        listener.addObserver(this);
        
        dispatch = listener;        
        initialized = true;               
    }
    
    public void update(Observable observer, Object arg) {
        if (arg instanceof CanMessage) {
            CanMessage msg = (CanMessage) arg;
            
            switch (msg.getId())
            {
                case CanBusIDs.FC_GPS_TIME >> 5:
                    DisplayUpdater.updateGpsTime();
                    break;     
                case CanBusIDs.IMU_ACCEL_DATA:
                    DisplayUpdater.updateAcceleration();
                    //setZ(msg.getData16(2));
                    break;
                case CanBusIDs.FC_REPORT_STATE:
                    DisplayUpdater.updateState();
                    //setState((int) msg.getData8(0) & 0xff);
                    break;
                case CanBusIDs.FC_REPORT_NODE_STATUS:
                    DisplayUpdater.updateNodeStatus();
                    //detailDisplay.setStates(msg.getBody());
                    break;
                case CanBusIDs.FC_REPORT_IMPORTANCE_MASK:
                    DisplayUpdater.updateImportanceMask();
                    //detailDisplay.setMask(msg.getBody());
                    break;
                case CanBusIDs.FC_REPORT_LINK_QUALITY:
                    DisplayUpdater.updateLinkQuality();
                    //setQuality(msg.getData16(0), msg.getData16(1));
                    break;   
                case CanBusIDs.FC_GPS_HEIGHT:
                    DisplayUpdater.updateGpsHeight();
                    //updateHeightDisplay();
                    break;
                case CanBusIDs.FC_GPS_LATLON >> 5:
                    DisplayUpdater.updateGpsLatlon();
                    //updateGpsDisplay();
                    break;
                case CanBusIDs.APS_DATA_VOLTS:
                    DisplayUpdater.updateVolts();
                    break;                  
                case CanBusIDs.APS_DATA_AMPS:
                    DisplayUpdater.updateAmps();
                    break;
                case CanBusIDs.APS_DATA_CHARGE:
                    DisplayUpdater.updateCharge();
                    break;
                case CanBusIDs.PWR_REPORT_CHARGER:
                    DisplayUpdater.updateCharger();
                    break;
                case CanBusIDs.UMB_REPORT_SHORE_POWER:
                    DisplayUpdater.updateShorePower();
                    break;
                case CanBusIDs.UMB_REPORT_CONNECTOR:
                    DisplayUpdater.updateConnector();
                    break;
                case CanBusIDs.APS_REPORT_SWITCH_1:
                    DisplayUpdater.updateSwitch1();
                    break;
                case CanBusIDs.APS_REPORT_SWITCH_2:
                    DisplayUpdater.updateSwitch2();
                    break;
                case CanBusIDs.APS_REPORT_SWITCH_3:
                    DisplayUpdater.updateSwitch3();
                    break;  
                case CanBusIDs.APS_REPORT_SWITCH_4:
                    DisplayUpdater.updateSwitch4();                        
                    break;                    
            }
        }
    }
    
    static private class DisplayUpdater
    {
        static private void updateGpsTime() {
            System.err.println("updateGpsTime");
        }   
    
        static private void updateAcceleration() {
            System.err.println("updateAcceleration");
        }
        
        static private void updateState() {
            System.err.println("updateState");
        }
    
        static private void updateNodeStatus() {
            System.err.println("updateNodeStatus");
        }
        
        static private void updateImportanceMask() {
            System.err.println("updateImportanceMask");
        }
    
        static private void updateLinkQuality() {
            System.err.println("updateLinkQuality");
        }
        
        static private void updateGpsHeight() {
            System.err.println("updateGpsHeight");
        }
    
        static private void updateGpsLatlon() {
            System.err.println("updateGpsLatlon");
        }
    
        static private void updateVolts() {
            System.err.println("updateVolts");
        }
    
        static private void updateAmps() {
            System.err.println("updateAmps");
        }
    
        static private void updateCharge() {
            System.err.println("updateCharge");
        }
    
        static private void updateCharger() {
            System.err.println("updateCharger");
        }
    
        static private void updateShorePower() {
            System.err.println("updateShorePower");
        }
    
        static private void updateConnector() {
            System.err.println("updateConnector");
        }
    
        static private void updateSwitch1() {
            System.err.println("updateSwitch1");        
        }
    
        static private void updateSwitch2() {
            System.err.println("updateSwitch2");
        }
    
        static private void updateSwitch3() {
            System.err.println("updateSwitch3");
        }
    
        static private void updateSwitch4() {
            System.err.println("updateSwitch4");
        }
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
        initializeSubsystemInfoLabels(subsystemInfoPanel);

        return subsystemInfoPanel;
    }

    private void initializeSubsystemInfoLabels(JPanel container) {
        assert ! initialized;

        GridBagConstraints c = UiUtil.getConstraints(0, 0);
        int rowCnt = 0;
        
        // FIXME: This whole array of nested classes and stuff isn't needed.  
        // Just make a 1/2 dozen or so function calls to addSimpleDisplayField 
        // and call it good. 
        SubsystemLabelValuePair[] values = {
                new SubsystemLabelValuePair(gpsLabel, "GPS"),
                new SubsystemLabelValuePair(altitudeLabel, "Altitude"),
                new SubsystemLabelValuePair(apsBusLabel, "APS Bus", "xx.xxV x.xxA"),
                new SubsystemLabelValuePair(battLabel, "Batt", "xx.xxx/AHr"),
                new SubsystemLabelValuePair(umbLabel, "UMB Connected"),
                new SubsystemLabelValuePair(shorePowerLabel, "Shore Power"),
                new SubsystemLabelValuePair(powerToFCLabel, "Power to S1 (FC)"),
                new SubsystemLabelValuePair(powerToCanLabel, "Power to S2 (CAN)"),
                new SubsystemLabelValuePair(powerToAtvLabel, "Power to S3 (ATV Pwr Amp)"),
                new SubsystemLabelValuePair(powerToWifiLabel, "Power to S4 (Wifi pwr Amp)"),
        };
        
        for (int i = 0; i < values.length; i++) {
            c.gridx = 0;
            c.gridy = rowCnt++;
            values[i].label = UiUtil.addSimpleDisplayField(container, 
                    values[i].name, values[i].value, c, true);    
        }             
        
        UiUtil.addVerticleGridGlue(container, values.length);
    }

    private class SubsystemLabelValuePair 
    {
        private JLabel label;
        private String name, value;
        
        private SubsystemLabelValuePair(JLabel labelRef, String name, 
                String initialValue) 
        {
            label = labelRef;
            this.name = name;
            this.value = initialValue;
        }
        
        private SubsystemLabelValuePair(JLabel labelRef, String name) {
            this(labelRef, name, "Unknown");
        }
    }
    
    private JPanel getCanDataTable() {
        assert ! initialized;

        JPanel canDataTable = new CanDataTable();

        return canDataTable;
    }

    public static void main(String[] args) throws IOException {
        StringBuffer tmpDir = new StringBuffer(System.getProperty("java.io.tmpdir"));
        String log = tmpDir.append(File.separatorChar).append("dv.log").toString();
        CanSocket socket = new LogCanSocket(new UDPCanSocket(), log); 
        CanListener listener = new CanListener(socket);
        DataViewer frame = new DataViewer(listener);
        Dimension screenSize = frame.getToolkit().getScreenSize();

        frame.setTitle("Rocketview DataViewer Driver");
        frame.setBounds(screenSize.width / 4, screenSize.height / 4,
                screenSize.width / 2, screenSize.height / 2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        listener.run();
    }
}
