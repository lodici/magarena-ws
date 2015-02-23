package magic.ui.duel.viewer;

import magic.ui.IconImages;
import magic.ui.SwingGameController;
import magic.ui.theme.ThemeFactory;
import magic.ui.widget.FontsAndBorders;
import magic.ui.widget.PanelButton;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Set;
import magic.data.MagicIcon;

public class BasicLandPermanentButton extends PanelButton implements ChoiceViewer {

    private static final long serialVersionUID = 1L;

    private final PermanentViewerInfo permanentInfo;
    private final SwingGameController controller;
    private final JPanel landPanel;

    public BasicLandPermanentButton(final PermanentViewerInfo permanentInfo,final SwingGameController controller) {

        this.permanentInfo=permanentInfo;
        this.controller=controller;

        landPanel=new JPanel(new BorderLayout());
        landPanel.setOpaque(false);
        landPanel.setBorder(FontsAndBorders.NO_TARGET_BORDER);

        final JLabel manaLabel=new JLabel();
        manaLabel.setHorizontalAlignment(JLabel.CENTER);
        manaLabel.setPreferredSize(new Dimension(0,30));
        manaLabel.setIcon(IconImages.getIcon(permanentInfo.manaColor));
        landPanel.add(manaLabel,BorderLayout.CENTER);

        final JLabel tappedLabel = new JLabel(permanentInfo.tapped ? IconImages.getIcon(MagicIcon.MANA_TAPPED) : null);
        tappedLabel.setPreferredSize(new Dimension(0,16));
        landPanel.add(tappedLabel,BorderLayout.SOUTH);

        setComponent(landPanel);
        showValidChoices(controller.getValidChoices());
    }

    @Override
    public void mouseClicked() {

        controller.processClick(permanentInfo.permanent);
    }

    @Override
    public void mouseEntered() {}

    @Override
    public void showValidChoices(final Set<?> validChoices) {

        setValid(validChoices.contains(permanentInfo.permanent));
    }

    @Override
    public Color getValidColor() {

        return ThemeFactory.getInstance().getCurrentTheme().getChoiceColor();
    }
}
