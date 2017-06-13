package br.com.abril.ndsled.swing;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ComboBoxRenderer extends JLabel implements ListCellRenderer<Object> {
	private static final long serialVersionUID = 3714306933883106596L;

	public ComboBoxRenderer(){
        setOpaque(true);
    }

    public Component getListCellRendererComponent(JList<?> list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {

        setText(value.toString());

        Color background = Color.white;
        Color foreground = Color.black;

        // check if this cell represents the current DnD drop location
        JList.DropLocation dropLocation = list.getDropLocation();

        if (dropLocation != null
                && !dropLocation.isInsert()
                && dropLocation.getIndex() == index) {



        // check if this cell is selected
        } else if (isSelected) {
            background = Color.RED;
            foreground = Color.WHITE;

        // unselected, and not the DnD drop location
        } else {
        };

        setBackground(background);
        setForeground(foreground);

        return this;
    }
}

