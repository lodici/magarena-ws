package magic.ui.duel.viewer;

import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.stack.MagicItemOnStack;

import javax.swing.ImageIcon;
import magic.data.MagicIcon;
import magic.model.stack.MagicAbilityOnStack;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicTriggerOnStack;
import magic.ui.IconImages;

public class StackViewerInfo {

    public final MagicItemOnStack itemOnStack;
    public final MagicCardDefinition cardDefinition;
    public final String name;
    public final ImageIcon icon;
    public final String description;
    public final boolean visible;

    public StackViewerInfo(final MagicGame game,final MagicItemOnStack itemOnStack) {
        this.itemOnStack=itemOnStack;
        cardDefinition=itemOnStack.getSource().getCardDefinition();
        name=itemOnStack.getName();
        description=itemOnStack.getDescription();
        visible=itemOnStack.getController()==game.getVisiblePlayer();
        icon = getIcon(itemOnStack);
    }

    private ImageIcon getIcon(final MagicItemOnStack itemOnStack) {
        if (itemOnStack instanceof MagicAbilityOnStack) {
            return IconImages.getIcon(MagicIcon.ABILITY);
        } else if (itemOnStack instanceof MagicCardOnStack) {
            return IconImages.getIcon(itemOnStack.getCardDefinition());
        } else if (itemOnStack instanceof MagicTriggerOnStack) {
            return IconImages.getIcon(MagicIcon.TRIGGER);
        }
        return null;
    }
}
