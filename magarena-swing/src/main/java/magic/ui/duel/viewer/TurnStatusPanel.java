package magic.ui.duel.viewer;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import magic.model.MagicGame;
import magic.model.phase.MagicPhaseType;
import magic.ui.MagicStyle;
import magic.ui.SwingGameController;
import magic.ui.theme.Theme;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class TurnStatusPanel extends JPanel {

    private final MigLayout miglayout = new MigLayout();
    private final TurnTitlePanel turnTitlePanel;
    private final PhaseStepViewer phaseStepViewer = new PhaseStepViewer();

    public TurnStatusPanel(final SwingGameController controller) {
        this.turnTitlePanel = new TurnTitlePanel(controller);
        setLookAndFeel();
        setLayout(miglayout);
        refreshLayout();
    }

    private void setLookAndFeel() {
        setOpaque(true);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        setBackground(MagicStyle.getTheme().getColor(Theme.COLOR_TITLE_BACKGROUND));
        //
        phaseStepViewer.setOpaque(false);
    }

    private void refreshLayout() {
        miglayout.setLayoutConstraints("insets 0 0 2 0, gap 0 3, flowy");
        miglayout.setColumnConstraints("fill");
        removeAll();
        add(turnTitlePanel);
        add(phaseStepViewer);
    }

    public void refresh(final MagicGame game, final MagicPhaseType phaseStep) {
        turnTitlePanel.refresh(game);
        phaseStepViewer.setPhaseStep(phaseStep);
    }

}
