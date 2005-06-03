package rocketview.layout;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class CanDataTable extends JPanel
{
    TableModel model;
    JTable table;
    
    CanDataTable() {
        super(new BorderLayout());
        
        JScrollPane contentScrollPane = new JScrollPane();        
        
        model = new CanDataTable.CanDataModel();
        table = new JTable(model);
        
        contentScrollPane.setViewportView(table);
        add(contentScrollPane);
    }
    
    private class CanDataModel extends AbstractTableModel
    {
        private ArrayList data = new ArrayList();
        final private String[] columnNames = { "Can ID", "Length", "Data", };        

        public void addRow(Object[] row) {
                data.add(row);
                fireTableRowsInserted(0, getRowCount() - 1);
        }

        public int getColumnCount() {
            return columnNames.length;
        }
        
        public int getRowCount() {
            return data.size();
        }
        
        public String getColumnName(int col) {
            return columnNames[col];
        }
        
        public Object getValueAt(int row, int col) {
            return ((Object[])data.get(row))[col];
        }
        
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }        
    }
}
