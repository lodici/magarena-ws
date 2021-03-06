package magic.ui.cardtable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import magic.data.GeneralConfig;
import magic.model.MagicCardDefinition;
import magic.model.MagicManaCost;
import magic.ui.widget.CostPanel;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import magic.ui.widget.TitleBar;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class CardTablePanel extends TexturedPanel {

    // fired when selection changes.
    public static final String CP_CARD_SELECTED = "cardTableSelection";
    // fired on mouse event.
    public static final String CP_CARD_LCLICKED = "cardLeftClicked";
    public static final String CP_CARD_RCLICKED = "cardRightClicked";
    public static final String CP_CARD_DCLICKED = "cardDoubleClicked";
    
    // renderer that centers the contents of a column.
    static final DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    static { centerRenderer.setHorizontalAlignment(SwingConstants.CENTER); }    

    private static final Color GRID_COLOR = new Color(194, 197, 203);
    private static final int ROW_HEIGHT = 20; //pixels

    private final MigLayout migLayout = new MigLayout();
    private final JScrollPane scrollpane = new JScrollPane();
    private final CardTableModel tableModel;
    private JTable table;
    private final ListSelectionModel selectionModel;

    private final TitleBar titleBar;
    private List<MagicCardDefinition> lastSelectedCards;
    private boolean isAdjusting = false;
    private int lastSelectedRow = -1;

    public CardTablePanel(final List<MagicCardDefinition> defs) {
        this(defs, "");
    }

    public CardTablePanel(final List<MagicCardDefinition> defs, final String title) {

        setBackground(FontsAndBorders.TRANSLUCENT_WHITE_STRONG);

        this.tableModel = new CardTableModel(defs);

        this.table = new JTable(tableModel) {
            private final Color defaultForeColor = getForeground();
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                final MagicCardDefinition card = tableModel.getCardDef(row);
                final boolean isRowSelected = table.isRowSelected(row);
                if (isRowSelected) {
                    c.setForeground(table.getSelectionForeground());
                } else {
                    c.setForeground(card.isInvalid() ? Color.GRAY : defaultForeColor);
                }
                return c;
            }
        };
        this.selectionModel = table.getSelectionModel();
        this.lastSelectedCards = new ArrayList<>();

        table.setDefaultRenderer(Object.class, new HideCellFocusRenderer());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // otherwise horizontal scrollbar won't work
        table.setRowHeight(ROW_HEIGHT);
        table.setGridColor(GRID_COLOR);
        
        final TableColumnModel model = table.getColumnModel();
        setColumnWidths(model);

        // center contents of columns.
        table.getColumn("#").setCellRenderer(centerRenderer);
        table.getColumn("P").setCellRenderer(centerRenderer);
        table.getColumn("T").setCellRenderer(centerRenderer);

        // center the column header captions.
        ((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        // special renderer for mana symbols
        model.getColumn(CardTableModel.COST_COLUMN_INDEX).setCellRenderer(new ManaCostCellRenderer());


        // listener to sort on column header click
        final JTableHeader header = table.getTableHeader();
        header.addMouseListener(new ColumnListener());
        header.setReorderingAllowed(true);

        // add table to scroll pane
        scrollpane.setViewportView(table);
        scrollpane.setBorder(FontsAndBorders.NO_BORDER);
        scrollpane.setOpaque(false);
        scrollpane.getViewport().setOpaque(false);

        // add title
        titleBar = new TitleBar(title);

        table.getSelectionModel().addListSelectionListener(getTableListSelectionListener());
        table.addMouseListener(getTableMouseAdapter());
        if (!GeneralConfig.getInstance().isPreviewCardOnSelect()) {
            table.addMouseMotionListener(new RowMouseOverListener());
        }
        
        setLayout(migLayout);
        refreshLayout();
        
    }

    private ListSelectionListener getTableListSelectionListener() {
        return new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                isAdjusting = e.getValueIsAdjusting();
                if (!isAdjusting) {
                    firePropertyChange(CP_CARD_SELECTED, false, true);
                }
            }
        };
    }

    private MouseAdapter getTableMouseAdapter() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!isAdjusting) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        if (hasDoubleClickListeners() && e.getClickCount() == 2) {
                            firePropertyChange(CP_CARD_DCLICKED, false, true);
                        } else {
                            firePropertyChange(CP_CARD_LCLICKED, false, true);
                        }
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        final Point p = e.getPoint();
                        final int rowNumber = table.rowAtPoint(p);
                        final boolean isRowSelected = table.isRowSelected(rowNumber);
                        if (!isRowSelected) {
                            table.getSelectionModel().setSelectionInterval(rowNumber, rowNumber);
                        } else {
                            firePropertyChange(CP_CARD_RCLICKED, false, true);
                        }
                    }
                }
            }
        };
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("flowy, insets 0, gap 0");
        add(titleBar, "w 100%, h 26!, hidemode 3");
        add(scrollpane, "w 100%, h 100%");
    }

    private void setColumnWidths(final TableColumnModel model) {
        for (int i = 0; i < model.getColumnCount(); i++) {
            model.getColumn(i).setMinWidth(CardTableModel.COLUMN_MIN_WIDTHS[i]);
            model.getColumn(i).setPreferredWidth(CardTableModel.COLUMN_MIN_WIDTHS[i]);
        }
    }

    public void setDeckEditorSelectionMode() {
        //table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    public List<MagicCardDefinition> getSelectedCards() {
        final List<MagicCardDefinition> selectedCards = new ArrayList<>();
        for (final int row : table.getSelectedRows()) {
            final MagicCardDefinition card = tableModel.getCardDef(row);
            if (card != null) {
                selectedCards.add(card);
            }
        }
        return selectedCards;
    }

    private void reselectLastCards() {
        // select previous card if possible
        if (lastSelectedCards.size() > 0) {
            final List<MagicCardDefinition> newSelectedCards = new ArrayList<>();
            for (final MagicCardDefinition card : lastSelectedCards) {
                final int index = tableModel.findCardIndex(card);
                if (index != -1) {
                    // previous card still in list
                    table.getSelectionModel().addSelectionInterval(index,index);
                    newSelectedCards.add(card);
                }
            }
            lastSelectedCards = newSelectedCards;
        } else {
            setSelectedRow();
        }
    }

    private void setSelectedRow() {
        if (table.getRowCount() > 0) {
            table.setRowSelectionInterval(0, 0);
        }
    }

    public void setCards(final List<MagicCardDefinition> defs) {
        final boolean isRowSelected = table.getSelectedRow() != -1;
        tableModel.setCards(defs);
        table.tableChanged(new TableModelEvent(tableModel));
        table.repaint();
        if (isRowSelected) {
            reselectLastCards();
        }
    }

    public void setTitle(final String title) {
        titleBar.setText(title);
    }

    public void setHeaderVisible(boolean b) {
        titleBar.setVisible(b);
        refreshLayout();
    }

    public void clearSelection() {
        table.clearSelection();
    }

    public JTable getDeckTable() {
        return table;
    }

    public void setDeckTable(JTable aDeckTable) {
        this.table = aDeckTable;
        scrollpane.setViewportView(table);
    }

    public JTable getTable() {
        return table;
    }

    private class ColumnListener extends MouseAdapter {
        @Override
        public void mouseClicked(final MouseEvent e) {
            final TableColumnModel colModel = table.getColumnModel();
            final int columnModelIndex = colModel.getColumnIndexAtX(e.getX());
            final int modelIndex = colModel.getColumn(columnModelIndex).getModelIndex();

            if (modelIndex < 0) {
                return;
            }

            // sort
            tableModel.sort(modelIndex);

            // redraw
            table.tableChanged(new TableModelEvent(tableModel));
            table.repaint();

            reselectLastCards();
        }
    }

    private class RowMouseOverListener extends MouseAdapter {
        @Override
        public void mouseMoved(final MouseEvent e) {
            final Point p = e.getPoint();
            final int row = table.rowAtPoint(p);
            if (row != lastSelectedRow) {
                lastSelectedRow = row;
            }
        }
    }

    private static class ManaCostCellRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 113245L;

        @Override
        public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int col) {

            final MagicCardDefinition card = ((CardTableModel)table.getModel()).getCardDef(row);
            final CostPanel myRender = new CostPanel(card.isLand() || !card.isValid() ? null : (MagicManaCost)value);

            // match border and background formatting with default
            final JComponent defaultRender = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

            myRender.setOpaque(defaultRender.isOpaque());
            myRender.setBorder(defaultRender.getBorder());

            if (isSelected) {
                myRender.setForeground(table.getSelectionForeground());
                myRender.setBackground(table.getSelectionBackground());
            } else {
                myRender.setForeground(getForeground());
                // We have to create a new color object because Nimbus returns
                // a color of type DerivedColor, which behaves strange, not sure
                // why.
                myRender.setBackground(new Color(getBackground().getRed(), getBackground().getGreen(), getBackground().getBlue()));

            }
            myRender.setBorder(noFocusBorder);
            return myRender;
        }
    }

    private class HideCellFocusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBorder(noFocusBorder);
            return this;
        }
    }

    private boolean hasDoubleClickListeners() {
        return getPropertyChangeListeners(CP_CARD_DCLICKED).length > 0;
    }

    public void selectFirstRow() {
        if (table.getRowCount() > 0) {
            table.setRowSelectionInterval(0, 0);
            firePropertyChange(CP_CARD_SELECTED, false, true);
        }
    }

    public void setSelectedCard(MagicCardDefinition aCard) {
        final int index = tableModel.findCardIndex(aCard);
        if (index != -1 && getSelectedCards().contains(aCard) == false) {
            table.getSelectionModel().addSelectionInterval(index, index);
        } else if (tableModel.getRowCount() > 0) {
            table.getSelectionModel().addSelectionInterval(0, 0);
        }
    }
    
    public TitleBar getTitleBar() {
        return titleBar;
    }
    
    public void showCardCount(final boolean b) {
        tableModel.showCardCount(b);
    }

}
