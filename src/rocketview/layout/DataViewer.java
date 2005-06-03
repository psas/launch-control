package rocketview.layout;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class DataViewer extends JFrame
{   
    boolean initialized;
    
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
        BoxLayout layoutMgr = new BoxLayout(generalDataPanel, BoxLayout.Y_AXIS);
        
        generalDataPanel.setLayout(layoutMgr);
        
        return generalDataPanel;        
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
