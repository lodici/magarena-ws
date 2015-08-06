package magic.ui.duel.viewer;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Set;

import magic.ui.IconImages;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.ui.SwingGameController;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.CostPanel;
import magic.ui.widget.PanelButton;
import magic.ui.utility.MagicStyle;

public class CardButton extends PanelButton implements ChoiceViewer {
    private static final long serialVersionUID = 1L;

    private final SwingGameController controller;
    private final MagicCard card;
    private final boolean showCost;
    private final JLabel nameLabel;

    public CardButton(final SwingGameController controller, final MagicCard card, final int lineHeight, final boolean showCost) {
        super();
        this.controller = controller;
        this.card       = card;
        this.showCost   = showCost;

        final JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(0, lineHeight));
        setComponent(mainPanel);

        final MagicCardDefinition cardDefinition = card.getCardDefinition();

        final CostPanel costPanel;
        if (this.showCost && !cardDefinition.isLand()) {
            costPanel = new CostPanel(card.getCost());
        } else {
            costPanel = new CostPanel(null);
        }

        nameLabel = new JLabel(cardDefinition.getName());
        nameLabel.setForeground(MagicStyle.getRarityColor(cardDefinition));

        final JLabel typeLabel = new JLabel(IconImages.getIcon(cardDefinition));
        typeLabel.setPreferredSize(new Dimension(24,0));

        mainPanel.add(costPanel,BorderLayout.WEST);
        mainPanel.add(nameLabel,BorderLayout.CENTER);
        mainPanel.add(typeLabel,BorderLayout.EAST);
    }

    @Override
    public void mouseClicked() {
        this.controller.processClick(card);
    }

    @Override
    public void mouseEntered() {}

    @Override
    public void showValidChoices(final Set<?> validChoices) {
        setValid(validChoices.contains(card));
    }

    @Override
    public Color getValidColor() {
        return ThemeFactory.getInstance().getCurrentTheme().getChoiceColor();
    }
}
