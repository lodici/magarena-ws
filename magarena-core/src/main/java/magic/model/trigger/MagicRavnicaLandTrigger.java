package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicTapAction;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;

public class MagicRavnicaLandTrigger extends MagicWhenComesIntoPlayTrigger {

    private static final MagicRavnicaLandTrigger INSTANCE = new MagicRavnicaLandTrigger();

    private MagicRavnicaLandTrigger() {}

    public static MagicRavnicaLandTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
        if (permanent.getController().getLife() < 2) {
            game.doAction(MagicTapAction.Enters(permanent));
            return MagicEvent.NONE;
        } else {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(),
                this,
                "PN may$ pay 2 life. If you don't, "+permanent.getName()+" enters the battlefield tapped."
            );
        }
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        if (event.isYes()) {
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),-2));
        } else {
            game.doAction(MagicTapAction.Enters(event.getPermanent()));
        }
    }

    @Override
    public boolean usesStack() {
        return false;
    }
}
