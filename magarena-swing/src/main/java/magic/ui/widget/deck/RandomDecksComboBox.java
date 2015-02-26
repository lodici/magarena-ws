package magic.ui.widget.deck;

import magic.data.DeckGenerators;
import magic.ui.IconImages;
import magic.model.MagicColor;
import magic.ui.widget.FontsAndBorders;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Vector;
import magic.data.MagicIcon;

@SuppressWarnings("serial")
public class RandomDecksComboBox extends JComboBox<String> implements ListCellRenderer<String> {

    private static final String SEPARATOR = "----";

    private String lastSelected;

    public RandomDecksComboBox(final String colors) {

        setRenderer(this);

        final Vector<String> items = new Vector<String>();
        items.add("bug");
        items.add("bur");
        items.add("buw");
        items.add("bgr");
        items.add("bgw");
        items.add("brw");
        items.add("ugw");
        items.add("ugr");
        items.add("urw");
        items.add("grw");
        items.add("***");
        items.add("bu");
        items.add("bg");
        items.add("br");
        items.add("bw");
        items.add("ug");
        items.add("ur");
        items.add("uw");
        items.add("gr");
        items.add("gw");
        items.add("rw");
        items.add("**");
        items.add("b");
        items.add("u");
        items.add("g");
        items.add("r");
        items.add("w");
        items.add("*");
        items.add("@");

        if (DeckGenerators.getInstance().getNrGenerators() > 0) {
            items.add(SEPARATOR);
            for (final String generatorName : DeckGenerators.getInstance().getGeneratorNames()) {
                items.add(generatorName);
            }
        }

        setModel(new DefaultComboBoxModel<String>(items));
        setSelectedItem(colors);
        lastSelected = colors;
        this.setFocusable(false);
        addActionListener(this);
    }

    @Override
    public String getSelectedItem() {
        return getItemAt(getSelectedIndex());
    }

    @Override
    public Component getListCellRendererComponent(
            final JList<? extends String> list,
            final String selectedVal,
            final int index,
            final boolean isSelected,
            final boolean cellHasFocus) {
        if (selectedVal.equals(SEPARATOR)) {
            return new javax.swing.JSeparator(javax.swing.JSeparator.HORIZONTAL);
        } else if (DeckGenerators.getInstance().getGeneratorNames().contains(selectedVal)) {
            final JPanel panel=new JPanel(new GridLayout(1,1));
            panel.setBorder(FontsAndBorders.EMPTY_BORDER);
            if (isSelected) {
                panel.setBackground(Color.LIGHT_GRAY);
            }

            final JLabel label = new JLabel(selectedVal, JLabel.CENTER);
            label.setFont(FontsAndBorders.FONT1);
            panel.add(label);

            return panel;
        } else {
            final JPanel panel=new JPanel(new GridLayout(1,3));
            for (int i=0;i<selectedVal.length();i++) {

                final char ch = selectedVal.charAt(i);
                final ImageIcon icon;
                switch (ch) {
                    case '*': icon=IconImages.getIcon(MagicIcon.ANY); break;
                    case '@': icon=IconImages.getIcon(MagicIcon.FOLDER); break;
                    default:  icon=IconImages.getIcon(MagicColor.getColor(ch)); break;
                }
                panel.add(new JLabel(icon));
            }
            panel.setBorder(FontsAndBorders.EMPTY_BORDER);
            if (isSelected) {
                panel.setBackground(Color.LIGHT_GRAY);
            }
            return panel;
        }
    }

    public void actionPerformed(final ActionEvent e) {
        final String tempItem = getSelectedItem();

        if (SEPARATOR.equals(tempItem)) {
            // don't select separator
            setSelectedItem(lastSelected);
        } else {
            lastSelected = tempItem;
        }
    }
}
