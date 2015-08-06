package magic.ui.duel.choice;

import magic.model.MagicCardList;
import magic.model.MagicSource;

import javax.swing.SwingUtilities;

import java.util.Collections;
import magic.model.IGameController;
import magic.ui.ScreenController;

@SuppressWarnings("serial")
public class MulliganChoicePanel extends MayChoicePanel {

    public MulliganChoicePanel(final IGameController controller, final MagicSource source, final String message, final MagicCardList hand) {
        super(controller, source, message);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                showMulliganScreen(hand);
            }
        });
    }

    private void showMulliganScreen(final MagicCardList hand) {
        Collections.sort(hand);
        ScreenController.showMulliganScreen(MulliganChoicePanel.this, hand);
    }

    public void doMulliganAction(final boolean takeMulligan) {
        setYesClicked(takeMulligan);
        getGameController().actionClicked();
    }

}
