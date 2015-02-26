package magic.model.action;

import magic.model.MagicGame;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicTriggerOnStack;

public class MagicPutStateTriggerOnStackAction extends MagicAction {

    private final MagicEvent event;

    public MagicPutStateTriggerOnStackAction(final MagicEvent aEvent) {
        event = aEvent;
    }

    @Override
    public void doAction(final MagicGame game) {
        if (game.getStack().hasItem(event.getSource(), event.getChoiceDescription()) == false) {
            game.doAction(new MagicPutItemOnStackAction(new MagicTriggerOnStack(event)));
        }
    }

    @Override
    public void undoAction(final MagicGame game) {
        //do nothing
    }
}
