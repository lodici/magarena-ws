package magic.ui.player;

import magic.model.player.AiPlayer;
import magic.model.player.PlayerProfile;
import magic.ui.widget.FontsAndBorders;
import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.Color;

/**
 * Composite JPanel which displays player name and if applicable,
 * player type and attributes on separate lines.
 *
 */
@SuppressWarnings("serial")
public class PlayerDetailsPanel extends JPanel {

    private final static Color DEFAULT_TEXT_COLOR = Color.WHITE;

    private final MigLayout migLayout = new MigLayout();
    private final JLabel playerNameLabel = new JLabel();
    private final JLabel playerTypeLabel = new JLabel();
    private final JLabel playerAttributesLabel = new JLabel();

    // CTR
    public PlayerDetailsPanel(final PlayerProfile player, final Color foreColor) {
        setLookAndFeel(foreColor);
        setPlayer(player);
    }
    public PlayerDetailsPanel(final PlayerProfile player) {
        setLookAndFeel(DEFAULT_TEXT_COLOR);
        setPlayer(player);
    }
    public PlayerDetailsPanel() {
        setLookAndFeel(DEFAULT_TEXT_COLOR);
    }

    private void setLookAndFeel(final Color foreColor) {
        setOpaque(false);
        setForeground(foreColor);
        setLayout(migLayout);
        // player name label
        playerNameLabel.setFont(FontsAndBorders.FONT3);
        playerNameLabel.setForeground(foreColor);
        playerNameLabel.setVerticalAlignment(SwingConstants.TOP);
        // player type label
        playerTypeLabel.setFont(FontsAndBorders.FONT0);
        playerTypeLabel.setForeground(foreColor);
        // player attributes label
        playerAttributesLabel.setFont(FontsAndBorders.FONT0);
        playerAttributesLabel.setForeground(foreColor);
    }

    public void setPlayer(final PlayerProfile player) {
        playerNameLabel.setText(player.getPlayerName());
        playerTypeLabel.setText(getPlayerType(player));
        playerAttributesLabel.setText(getPlayerAttributes(player));
        refreshLayout();
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("insets 0, gap 0, flowy");
        add(playerTypeLabel, "w 100%");
        add(playerNameLabel, "w 100%");
        add(playerAttributesLabel, "w 100%");
    }

    private String getPlayerType(final PlayerProfile player) {
        return PlayerProfile.isAiPlayer(player) ? "AI : " + ((AiPlayer)player).getAiType() : "";
    }

    private String getPlayerAttributes(final PlayerProfile player) {
        if (PlayerProfile.isAiPlayer(player)) {
            final AiPlayer aiPlayer = (AiPlayer)player;
            return "Level: " + aiPlayer.getAiLevel() + "  Extra Life: " + aiPlayer.getExtraLife();
        } else {
            return "";
        }
    }

}
