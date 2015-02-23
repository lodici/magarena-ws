package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicSacrificeTargetPicker;

public class MagicDevourTrigger extends MagicWhenComesIntoPlayTrigger {

    private final int amount;

    public MagicDevourTrigger(final int amount) {
        this.amount = amount;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent perm, final MagicPayedCost payedCost) {
        final MagicTargetChoice targetChoice = MagicTargetChoice.Other("a creature to sacrifice", perm);
        return new MagicEvent(
            perm,
            new MagicMayChoice(
                "Sacrifice a creature to " + perm + "?",
                targetChoice
            ),
            MagicSacrificeTargetPicker.create(),
            this,
            "You may$ sacrifice a creature$ to SN."
        );
    }

    @Override
    public boolean usesStack() {
        return false;
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        if (event.isYes()) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final MagicPermanent permanent = event.getPermanent();
                    game.doAction(new MagicSacrificeAction(creature));
                    game.doAction(new MagicChangeCountersAction(
                        permanent,
                        MagicCounterType.PlusOne,
                        amount
                    ));
                    game.addEvent(executeTrigger(
                        game,
                        permanent,
                        MagicPayedCost.NO_COST
                    ));
                }
            });
        }
    }
}
