package magic.ui.duel.viewer;

import java.awt.Dimension;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import magic.model.MagicGame;
import magic.ui.SwingGameController;
import magic.ui.widget.TexturedPanel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class GameStatusPanel extends TexturedPanel implements ChangeListener {

    private final MigLayout migLayout = new MigLayout("insets 0, gap 0");
    private final UserActionPanel userActionPanel;
    private final SwingGameController controller;
    private boolean isNewTurnNotification = false;
    private final NewTurnPanel newTurnPanel;
    private final TurnStatusPanel turnStatusPanel;


    public GameStatusPanel(final SwingGameController controller) {

        this.controller = controller;

        // create UI components
        userActionPanel = new UserActionPanel(controller);
        newTurnPanel = new NewTurnPanel();
        turnStatusPanel = new TurnStatusPanel(controller);

        setPreferredSize(new Dimension(0,60));
        setMinimumSize(getPreferredSize());

        setLookAndFeel();
        refreshLayout();
    }

    private void refreshLayout() {
        removeAll();
        setLayout(migLayout);
        add(isNewTurnNotification ? newTurnPanel : turnStatusPanel, "w 100%, h 100%");
        revalidate();
        repaint();
    }

    private void setLookAndFeel() {
        userActionPanel.setOpaque(false);
    }

    public UserActionPanel getUserActionPanel() {
        return userActionPanel;
    }

    public void update() {
        turnStatusPanel.refresh(controller.getGame(), userActionPanel.getMagicPhaseType());
    }

    @Override
    public void stateChanged(final ChangeEvent e) {
        update();
    }

    public void showNewTurnNotification(final MagicGame game) {
        assert SwingUtilities.isEventDispatchThread();
        isNewTurnNotification = true;
        newTurnPanel.refreshData(game);
        refreshLayout();
    }

    public void hideNewTurnNotification() {
        assert SwingUtilities.isEventDispatchThread();
        isNewTurnNotification = false;
        refreshLayout();
    }

}
