package magic.ui.deck.editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import magic.data.CardLegality;
import magic.data.MagicFormat;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.utility.DeckUtils;

public class CardsLegalityTableModel implements TableModel {

    private static final Comparator<CardLegalityInfo> NAME_COMPARATOR_DESC = new Comparator<CardLegalityInfo>() {
        @Override
        public int compare(final CardLegalityInfo cfl1, final CardLegalityInfo cfl2) {
            return cfl1.getCardName().compareTo(cfl2.getCardName());
        }
    };

    /**
     * List of event listeners. These listeners wait for something to happen
     * with the table so that they can react. This is a must!
     */
    private final ArrayList<TableModelListener> listeners = new ArrayList<>();

    @Override
    public void addTableModelListener(final TableModelListener l) {
        if (listeners.contains(l) == false) {
            listeners.add(l);
        }
    }

    @Override
    public void removeTableModelListener(final TableModelListener l) {
        listeners.remove(l);
    }

    static final String[] COLUMN_NAMES = {
        "#",    // 0
        "Name"  // 1
    };

    static final int[] COLUMN_MIN_WIDTHS = {
        30, // 0 #
        180 // 1 name
    };

    public static final int COST_COLUMN_INDEX = 2;

    private List<CardLegalityInfo> cardLegalityList = new ArrayList<>();
    private final Comparator<CardLegalityInfo> comp;

    public CardsLegalityTableModel() {
        this.comp = NAME_COMPARATOR_DESC;
    }

    public MagicCardDefinition getCardDef(final int row) {
        if (row < 0 || row >= cardLegalityList.size()) {
            return null;
        }
        return cardLegalityList.get(row).getCard();
    }

    public void showDeckLegality(final MagicDeck aDeck, final MagicFormat aFormat) {
        if (aFormat != null) {
            cardLegalityList = getCardsLegalityList(aDeck, aFormat);
            if (comp != null) {
                Collections.sort(cardLegalityList, comp);
            }
        }
    }

    @Override
    public Class<?> getColumnClass(final int columnIndex) {
        return String.class;
    }

    @Override
    public int getColumnCount(){
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(final int columnIndex){
        return COLUMN_NAMES[columnIndex];
    }

    @Override
    public int getRowCount() {
        return cardLegalityList.size();
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        if (columnIndex == 1) {
            return cardLegalityList.get(rowIndex).getCardName();
        } else {
            return "";
        }
    }

    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return false;
    }

    @Override
    public void setValueAt(final Object value, final int rowIndex, final int columnIndex) {
    }

    CardLegalityInfo getCardLegality(int row) {
        return cardLegalityList.get(row);
    }

    private List<CardLegalityInfo> getCardsLegalityList(final MagicDeck aDeck, final MagicFormat aFormat) {
        final List<CardLegalityInfo> cardsLegalityList = new ArrayList<>();
        for (MagicCardDefinition card : DeckUtils.getDistinctCards(aDeck)) {
            final int cardCountCheck = card.isLand() ? 1 : aDeck.getCardCount(card);
            final CardLegality legality = aFormat.getCardLegality(card, cardCountCheck);
            final CardLegalityInfo cardLegality = new CardLegalityInfo(card, legality, aFormat);
            cardsLegalityList.add(cardLegality);
        }
        return cardsLegalityList;
    }

}

