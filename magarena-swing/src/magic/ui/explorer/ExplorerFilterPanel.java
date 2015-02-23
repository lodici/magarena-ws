package magic.ui.explorer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import magic.ui.IconImages;
import magic.data.CardDefinitions;
import magic.data.CubeDefinitions;
import magic.data.MagicFormats;
import magic.data.MagicSetDefinitions;
import magic.data.MagicSets;
import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicRarity;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.ui.dialog.DownloadImagesDialog;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.ButtonControlledPopup;
import magic.ui.widget.CardPoolTextFilter;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

public class ExplorerFilterPanel extends TexturedPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final String[] COST_VALUES = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};
    private static final String[] FILTER_CHOICES = {"Match any selected", "Match all selected", "Exclude selected"};
    private static final Color TEXT_COLOR = ThemeFactory.getInstance().getCurrentTheme().getTextColor();
    private static final Dimension POPUP_CHECKBOXES_SIZE = new Dimension(200, 150);
    private static final Dimension BUTTON_HOLDER_PANEL_SIZE = new Dimension(100, 36);

    private final MigLayout layout = new MigLayout();
    private final ExplorerDeckEditorPanel explorerPanel;

    // formats
    private ButtonControlledPopup formatsPopup;
    private JCheckBox[] formatsCheckBoxes;
    private JRadioButton[] formatsFilterChoices;
    // status
    private ButtonControlledPopup statusPopup;
    private JCheckBox[] statusCheckBoxes;
    private JRadioButton[] statusFilterChoices;
    // sets
    private ButtonControlledPopup setsPopup;
    private JCheckBox[] setsCheckBoxes;
    private JRadioButton[] setsFilterChoices;
    // cube
    private ButtonControlledPopup cubePopup;
    private JCheckBox[] cubeCheckBoxes;
    private JRadioButton[] cubeFilterChoices;
    //type
    private ButtonControlledPopup typePopup;
    private JCheckBox[] typeCheckBoxes;
    private JRadioButton[] typeFilterChoices;
    // color
    private ButtonControlledPopup colorPopup;
    private JCheckBox[] colorCheckBoxes;
    private JRadioButton[] colorFilterChoices;
    // mana cost
    private ButtonControlledPopup costPopup;
    private JCheckBox[] costCheckBoxes;
    private JRadioButton[] costFilterChoices;
    // sub type
    private ButtonControlledPopup subtypePopup;
    private JCheckBox[] subtypeCheckBoxes;
    private JRadioButton[] subtypeFilterChoices;
    // rarity
    private ButtonControlledPopup rarityPopup;
    private JCheckBox[] rarityCheckBoxes;
    private JRadioButton[] rarityFilterChoices;
    // oracle text
    private ButtonControlledPopup oraclePopup;
    private CardPoolTextFilter nameTextField;
    // ...
    private JButton resetButton;

    private int playableCards = 0;
    private int missingCards = 0;

    private boolean disableUpdate; // so when we change several filters, it doesn't update until the end

    public ExplorerFilterPanel(final ExplorerDeckEditorPanel explorerPanel) {

        this.explorerPanel=explorerPanel;

        disableUpdate = false;

        setBackground(FontsAndBorders.IMENUOVERLAY_BACKGROUND_COLOR);

        layout.setLayoutConstraints("flowy, wrap 2, gap 4");
        setLayout(layout);

        addCubeFilter();
        addFormatsFilter();
        addSetsFilter();
        addCardTypeFilter();
        addCardSubtypeFilter();
        addCardColorFilter();
        addManaCostFilter();
        addCardRarityFilter();
        addStatusFilter();
        addOracleFilter();
        addDummyFilterButton();
        addResetButton();

    }

    private void addDummyFilterButton() {
        final JButton btn = new JButton();
        btn.setVisible(false);
        btn.setPreferredSize(BUTTON_HOLDER_PANEL_SIZE);
        add(btn);
    }

    private void addFormatsFilter() {
        formatsPopup = addFilterPopupPanel("Format");
        formatsCheckBoxes = new JCheckBox[MagicFormats.values().length];
        formatsFilterChoices = new JRadioButton[FILTER_CHOICES.length];
        final String[] filterValues = MagicFormats.getFilterValues();
        populateCheckboxPopup(formatsPopup, filterValues, formatsCheckBoxes, formatsFilterChoices, false);
    }

    private void addStatusFilter() {
        final String[] filterValues = getStatusFilterValues();
        statusPopup = addFilterPopupPanel("Status");
        statusCheckBoxes = new JCheckBox[filterValues.length];
        statusFilterChoices = new JRadioButton[FILTER_CHOICES.length];
        populateCheckboxPopup(statusPopup, filterValues, statusCheckBoxes, statusFilterChoices, false);
    }

    private String[] getStatusFilterValues() {
        if (!explorerPanel.isDeckEditor()) {
            return new String[] {"New cards", "Playable", "Unimplemented", "Script file missing"};
        } else {
            return new String[] {"New cards"};
        }
    }

    private void addSetsFilter() {
        setsPopup = addFilterPopupPanel("Set");
        setsCheckBoxes = new JCheckBox[MagicSets.values().length];
        setsFilterChoices = new JRadioButton[FILTER_CHOICES.length];
        final String[] filterValues = MagicSetDefinitions.getFilterValues();
        populateCheckboxPopup(setsPopup, filterValues, setsCheckBoxes, setsFilterChoices, false);
    }

    private void addCubeFilter() {
        cubePopup = addFilterPopupPanel("Cube");
        final String[] filterValues = CubeDefinitions.getFilterValues();
        cubeCheckBoxes = new JCheckBox[filterValues.length];
        cubeFilterChoices = new JRadioButton[FILTER_CHOICES.length];
        populateCheckboxPopup(cubePopup, filterValues, cubeCheckBoxes, cubeFilterChoices, false);
    }

    private ButtonControlledPopup addFilterPopupPanel(final String title, final String tooltip) {
        final JButton selectButton = new JButton(title);
        selectButton.setToolTipText(tooltip);
        selectButton.setFont(FontsAndBorders.FONT1);
        selectButton.setPreferredSize(BUTTON_HOLDER_PANEL_SIZE);
        add(selectButton, "w " + BUTTON_HOLDER_PANEL_SIZE.width + "!");

        final ButtonControlledPopup pop = new ButtonControlledPopup(selectButton, title, title);
        pop.setLayout(new BoxLayout(pop, BoxLayout.Y_AXIS));
        selectButton.addActionListener(new PopupCloser(pop));
        return pop;
    }

    private ButtonControlledPopup addFilterPopupPanel(final String title) {
        return addFilterPopupPanel(title, null);
    }

    private class PopupCloser implements ActionListener {
        private final ButtonControlledPopup p;

        public PopupCloser(final ButtonControlledPopup p) {
            this.p = p;
        }

        @Override
        public void actionPerformed(final ActionEvent event) {
            // close all other popups except for our own button's
            if (p != cubePopup) {
                cubePopup.hidePopup();
            }
            if (p != typePopup) {
                typePopup.hidePopup();
            }
            if (p != colorPopup) {
                colorPopup.hidePopup();
            }
            if (p != costPopup) {
                costPopup.hidePopup();
            }
            if (p != subtypePopup) {
                subtypePopup.hidePopup();
            }
            if (p != rarityPopup) {
                rarityPopup.hidePopup();
            }
        }
    }

    private void populateCheckboxPopup(final ButtonControlledPopup popup, final Object[] checkboxValues, final JCheckBox[] newCheckboxes, final JRadioButton[] newFilterButtons, final boolean hideAND) {

        final JPanel checkboxesPanel = new JPanel(new MigLayout("flowy, insets 2"));
        checkboxesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkboxesPanel.setOpaque(false);

        for (int i=0;i<checkboxValues.length;i++) {
            newCheckboxes[i]=new JCheckBox(checkboxValues[i].toString().replace('_', ' '));
            newCheckboxes[i].addActionListener(this);
            newCheckboxes[i].setOpaque(false);
            newCheckboxes[i].setForeground(TEXT_COLOR);
            newCheckboxes[i].setFocusPainted(true);
            newCheckboxes[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            checkboxesPanel.add(newCheckboxes[i]);
        }

        final JScrollPane scrollPane = new JScrollPane(checkboxesPanel);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setBorder(FontsAndBorders.DOWN_BORDER);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setPreferredSize(POPUP_CHECKBOXES_SIZE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);
        popup.add(scrollPane);

        final ButtonGroup bg = new ButtonGroup();
        for (int i = 0; i < FILTER_CHOICES.length; i++) {
            newFilterButtons[i] = new JRadioButton(FILTER_CHOICES[i]);
            newFilterButtons[i].addActionListener(this);
            newFilterButtons[i].setOpaque(false);
            newFilterButtons[i].setForeground(TEXT_COLOR);
            newFilterButtons[i].setFocusPainted(true);
            newFilterButtons[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            if (i == 0) {
                newFilterButtons[i].setSelected(true);
            } else if (i == 1) {
                newFilterButtons[i].setVisible(!hideAND);
            }
            bg.add(newFilterButtons[i]);
            popup.add(newFilterButtons[i]);
        }
    }

    /**
     * @param cardDefinition
     * @return
     */
    private boolean filter(final MagicCardDefinition cardDefinition) {

        if (cardDefinition.isToken()) {
            return false;
        }

        // search text in name, abilities, type, text, etc.
        if (nameTextField.getSearchTerms().size() > 0) {
          for (String searchTerm : nameTextField.getSearchTerms()) {
              if (!cardDefinition.hasText(searchTerm)) {
                  return false;
              }
          }
        }

        // cube
        if (!filterCheckboxes(cardDefinition, cubeCheckBoxes, cubeFilterChoices,
            new CardChecker() {
                @Override
                public boolean checkCard(final MagicCardDefinition card, final int i) {
                    final String cubeName = cubeCheckBoxes[i].getText();
                    return CubeDefinitions.isCardInCube(card, cubeName);
                }
            })) {
            return false;
        }

        // format
        if (!filterCheckboxes(cardDefinition, formatsCheckBoxes, formatsFilterChoices,
            new CardChecker() {
                @Override
                public boolean checkCard(final MagicCardDefinition card, final int i) {
                    final MagicFormats magicFormat  = MagicFormats.values()[i];
                    return MagicFormats.isCardLegal(card, magicFormat);
                }
            })) {
            return false;
        }

        // sets
        if (!filterCheckboxes(cardDefinition, setsCheckBoxes, setsFilterChoices,
            new CardChecker() {
                @Override
                public boolean checkCard(final MagicCardDefinition card, final int i) {
                    final MagicSets magicSet  = MagicSets.values()[i];
                    return  MagicSetDefinitions.isCardInSet(card, magicSet);
                }
            })) {
            return false;
        }

        // type
        if (!filterCheckboxes(cardDefinition, typeCheckBoxes, typeFilterChoices,
            new CardChecker() {
                @Override
                public boolean checkCard(final MagicCardDefinition card, final int i) {
                    return card.hasType(MagicType.FILTER_TYPES.toArray(new MagicType[0])[i]);
                }
            })) {
            return false;
        }

        // color
        if (!filterCheckboxes(cardDefinition, colorCheckBoxes, colorFilterChoices,
            new CardChecker() {
                @Override
                public boolean checkCard(final MagicCardDefinition card, final int i) {
                    return card.hasColor(MagicColor.values()[i]);
                }
            })) {
            return false;
        }

        // cost
        if (!filterCheckboxes(cardDefinition, costCheckBoxes, costFilterChoices,
            new CardChecker() {
                @Override
                public boolean checkCard(final MagicCardDefinition card, final int i) {
                    return card.hasConvertedCost(Integer.parseInt(COST_VALUES[i]));
                }
            })) {
            return false;
        }

        // subtype
        if (!filterCheckboxes(cardDefinition, subtypeCheckBoxes, subtypeFilterChoices,
            new CardChecker() {
                @Override
                public boolean checkCard(final MagicCardDefinition card, final int i) {
                    return card.hasSubType(MagicSubType.values()[i]);
                }
            })) {
            return false;
        }

        // rarity
        if (!filterCheckboxes(cardDefinition, rarityCheckBoxes, rarityFilterChoices,
            new CardChecker() {
                @Override
                public boolean checkCard(final MagicCardDefinition card, final int i) {
                    return card.isRarity(MagicRarity.values()[i]);
                }
            })) {
            return false;
        }

        // status
        if (!filterCheckboxes(cardDefinition, statusCheckBoxes, statusFilterChoices,
                new CardChecker() {
                    @Override
                    public boolean checkCard(final MagicCardDefinition card, final int i) {
                        final String status = statusCheckBoxes[i].getText();
                        switch (status) {
                            case "New cards":
                                return DownloadImagesDialog.isCardInDownloadsLog(card);
                            case "Playable":
                                return CardDefinitions.isCardPlayable(card);
                            case "Unimplemented":
                                return CardDefinitions.isCardMissing(card);
                            case "Script file missing":
                                return card.IsScriptFileMissing();
                            default:
                                return true;
                        }
                    }
                })) {
            return false;
        }

        return true;
    }

    private boolean filterCheckboxes(final MagicCardDefinition cardDefinition, final JCheckBox[] checkboxes, final JRadioButton[] filterButtons, final CardChecker func) {
        boolean somethingSelected = false;
        boolean resultOR = false;
        boolean resultAND = true;

        for (int i=0; i < checkboxes.length; i++) {
            if (checkboxes[i].isSelected()) {
                somethingSelected = true;
                if (!func.checkCard(cardDefinition, i)) {
                    resultAND = false;
                } else {
                    resultOR = true;
                }
            }
        }
        if (filterButtons[2].isSelected()) {
            // exclude selected
            return !resultOR;
        }
        if (!somethingSelected) {
            // didn't choose to exclude and nothing selected, so don't filter
            return true;
        } else {
            // otherwise return OR or AND result
            return (filterButtons[0].isSelected() && resultOR) || (filterButtons[1].isSelected() && resultAND);
        }
    }

    private interface CardChecker {
        public boolean checkCard(MagicCardDefinition card, int i);
    }

    public List<MagicCardDefinition> getCardDefinitions(final boolean isDeckEditor) {

        final List<MagicCardDefinition> cardDefinitions = new ArrayList<>();

        final List<MagicCardDefinition> cards = isDeckEditor ?
                CardDefinitions.getDefaultPlayableCardDefs() :
                CardDefinitions.getAllCards();

        missingCards = 0;
        playableCards = 0;
        for (final MagicCardDefinition cardDefinition : cards) {
            if (!cardDefinition.isHidden() || !isDeckEditor) {
                if (filter(cardDefinition)) {
                    cardDefinitions.add(cardDefinition);
                    if (cardDefinition.isMissing()) {
                        missingCards++;
                    } else {
                        playableCards++;
                    }
                }
            }
        }
        return cardDefinitions;
    }

    public void resetFilters() {
        disableUpdate = true; // ignore any events caused by resetting filters

        closePopups();

        unselectFilterSet(cubeCheckBoxes, cubeFilterChoices);
        unselectFilterSet(formatsCheckBoxes, formatsFilterChoices);
        unselectFilterSet(setsCheckBoxes, setsFilterChoices);
        unselectFilterSet(typeCheckBoxes, typeFilterChoices);
        unselectFilterSet(colorCheckBoxes, colorFilterChoices);
        unselectFilterSet(costCheckBoxes, costFilterChoices);
        unselectFilterSet(subtypeCheckBoxes, subtypeFilterChoices);
        unselectFilterSet(rarityCheckBoxes, rarityFilterChoices);
        unselectFilterSet(statusCheckBoxes, statusFilterChoices);

        nameTextField.setText("");

        disableUpdate = false;
    }

    private void unselectFilterSet(final JCheckBox[] boxes, final JRadioButton[] filterButtons) {
        // uncheck all checkboxes
        for (JCheckBox checkbox : boxes) {
            checkbox.setSelected(false);
        }
        // reset to first option
        filterButtons[0].setSelected(true);
    }

    public void closePopups() {
        cubePopup.hidePopup();
        formatsPopup.hidePopup();
        setsPopup.hidePopup();
        typePopup.hidePopup();
        colorPopup.hidePopup();
        costPopup.hidePopup();
        subtypePopup.hidePopup();
        rarityPopup.hidePopup();
        oraclePopup.hidePopup();
        statusPopup.hidePopup();
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        final Component c = (Component)event.getSource();
        c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (event.getSource() == resetButton) {
            resetFilters();
        }
        if (!disableUpdate) {
            explorerPanel.updateCardPool();
        }
        c.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    private void addCardTypeFilter() {
        typePopup = addFilterPopupPanel("Type");
        typeCheckBoxes = new JCheckBox[MagicType.FILTER_TYPES.size()];
        typeFilterChoices = new JRadioButton[FILTER_CHOICES.length];
        populateCheckboxPopup(typePopup, MagicType.FILTER_TYPES.toArray(), typeCheckBoxes, typeFilterChoices, false);
    }

    private void addOracleFilter() {
        oraclePopup = addFilterPopupPanel("Search", "Searches name, type, subtype and oracle text.");
        oraclePopup.setPopupSize(260, 38);
        nameTextField = new CardPoolTextFilter(explorerPanel);
        oraclePopup.add(nameTextField);
    }

    private void addCardColorFilter() {
        colorPopup = addFilterPopupPanel("Color");
        colorCheckBoxes=new JCheckBox[MagicColor.NR_COLORS];
        final JPanel colorsPanel=new JPanel();
        colorsPanel.setLayout(new BoxLayout(colorsPanel, BoxLayout.X_AXIS));
        colorsPanel.setBorder(FontsAndBorders.DOWN_BORDER);
        colorsPanel.setOpaque(false);
        colorPopup.setPopupSize(280, 90);
        for (int i = 0; i < MagicColor.NR_COLORS; i++) {
            final MagicColor color = MagicColor.values()[i];
            final JPanel colorPanel=new JPanel();
            colorPanel.setOpaque(false);
            colorCheckBoxes[i]=new JCheckBox("",false);
            colorCheckBoxes[i].addActionListener(this);
            colorCheckBoxes[i].setOpaque(false);
            colorCheckBoxes[i].setFocusPainted(true);
            colorCheckBoxes[i].setAlignmentY(Component.CENTER_ALIGNMENT);
            colorCheckBoxes[i].setActionCommand(Character.toString(color.getSymbol()));
            colorPanel.add(colorCheckBoxes[i]);
            colorPanel.add(new JLabel(IconImages.getIcon(color.getManaType())));
            colorsPanel.add(colorPanel);
        }
        colorsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        colorPopup.add(colorsPanel);

        final ButtonGroup colorFilterBg = new ButtonGroup();
        colorFilterChoices = new JRadioButton[FILTER_CHOICES.length];
        for (int i = 0; i < FILTER_CHOICES.length; i++) {
            colorFilterChoices[i] = new JRadioButton(FILTER_CHOICES[i]);
            colorFilterChoices[i].addActionListener(this);
            colorFilterChoices[i].setOpaque(false);
            colorFilterChoices[i].setForeground(TEXT_COLOR);
            colorFilterChoices[i].setFocusPainted(true);
            colorFilterChoices[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            if (i == 0) {
                colorFilterChoices[i].setSelected(true);
            }
            colorFilterBg.add(colorFilterChoices[i]);
            colorPopup.add(colorFilterChoices[i]);
        }
    }

    private void addManaCostFilter() {
        costPopup = addFilterPopupPanel("Mana Cost");
        costCheckBoxes = new JCheckBox[COST_VALUES.length];
        costFilterChoices = new JRadioButton[FILTER_CHOICES.length];
        populateCheckboxPopup(costPopup, COST_VALUES, costCheckBoxes, costFilterChoices, true);
    }

    private void addCardSubtypeFilter() {
        subtypePopup = addFilterPopupPanel("Subtype");
        subtypeCheckBoxes = new JCheckBox[MagicSubType.values().length];
        subtypeFilterChoices = new JRadioButton[FILTER_CHOICES.length];
        populateCheckboxPopup(subtypePopup, MagicSubType.values(), subtypeCheckBoxes, subtypeFilterChoices, false);
    }

    private void addCardRarityFilter() {
        rarityPopup = addFilterPopupPanel("Rarity");
        rarityCheckBoxes = new JCheckBox[MagicRarity.values().length];
        rarityFilterChoices = new JRadioButton[FILTER_CHOICES.length];
        populateCheckboxPopup(rarityPopup, MagicRarity.values(), rarityCheckBoxes, rarityFilterChoices, true);
    }

    private void addResetButton() {
        resetButton = new JButton("Reset");
        resetButton.setToolTipText("Clears all filters");
        resetButton.setFont(new Font("dialog", Font.BOLD, 12));
        resetButton.setForeground(new Color(127, 23 ,23));
        resetButton.addActionListener(this);
        resetButton.setPreferredSize(BUTTON_HOLDER_PANEL_SIZE);
        add(resetButton, "w " + BUTTON_HOLDER_PANEL_SIZE.width + "!");
    }

    public int getPlayableCardCount() {
        return playableCards;
    }

    public int getMissingCardCount() {
        return missingCards;
    }

}
