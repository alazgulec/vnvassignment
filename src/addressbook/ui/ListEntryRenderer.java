package addressbook.ui;

import java.awt.Component;
import javax.swing.JList;
import javax.swing.DefaultListCellRenderer;

/**
 *
 * @author John O'Conner
 *
 */
public class ListEntryRenderer extends DefaultListCellRenderer {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Creates a new instance of ListEntryRenderer
     */
    public ListEntryRenderer() {
    }

    public Component getListCellRendererComponent(JList list, Object value, 
            int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        ListEntry entry = (ListEntry) value;
        this.setText(entry.getName());
        return this;
    }
    
}
