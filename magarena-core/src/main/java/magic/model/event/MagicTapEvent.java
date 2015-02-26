package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicTapAction;
import magic.model.condition.MagicCondition;

public class MagicTapEvent extends MagicEvent {

    private static final MagicCondition[] conds = new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION};

    public MagicTapEvent(final MagicPermanent permanent) {
        super(
            permanent,
            EVENT_ACTION,
            "Tap SN."
        );
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicTapAction(event.getPermanent()));
        }
    };

    @Override
    public MagicCondition[] getConditions() {
        return conds;
    }
}
