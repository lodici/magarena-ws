package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.AddTriggerAction;
import magic.model.action.ChangeCardDestinationAction;
import magic.model.action.MoveCardAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;

public class MagicCipherEvent extends MagicEvent {

    public MagicCipherEvent(final MagicSource source,final MagicPlayer player) {
        super(
            source,
            player,
            new MagicMayChoice(
                "Exile " + source + " encoded on a creature you control?",
                MagicTargetChoice.A_CREATURE_YOU_CONTROL
            ),
            EVENT_ACTION,
            "PN may$ exile SN encoded on a creature$ you control."
        );
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new ChangeCardDestinationAction(event.getCardOnStack(), MagicLocationType.Exile));
                event.processTargetPermanent(game, new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creatureToEncode) {
                        game.doAction(new AddTriggerAction(
                            creatureToEncode,
                            MagicWhenDamageIsDealtTrigger.Cipher(event.getCardOnStack().getCardDefinition())
                        ));
                    }
                });
            } else {
                game.doAction(new ChangeCardDestinationAction(event.getCardOnStack(), MagicLocationType.Graveyard));
            }
            game.doAction(new MoveCardAction(event.getCardOnStack()));
        }
    };
}
