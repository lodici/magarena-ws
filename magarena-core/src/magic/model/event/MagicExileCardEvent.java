package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;

public class MagicExileCardEvent extends MagicEvent {

    private final MagicCondition[] conds;

    public MagicExileCardEvent(final MagicSource source, final MagicTargetChoice targetChoice) {
        this(source, source.getController(), targetChoice);
    }

    public MagicExileCardEvent(
            final MagicSource source,
            final MagicPlayer player,
            final MagicTargetChoice targetChoice) {
        super(
            source,
            player,
            targetChoice,
            EVENT_ACTION,
            "Choose " + targetChoice.getTargetDescription() + "$."
        );
        conds = new MagicCondition[]{MagicConditionFactory.HasOptions(player, targetChoice)};
    }

    private static final MagicEventAction EVENT_ACTION = new MagicEventAction() {
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game,new MagicCardAction() {
                public void doAction(final MagicCard card) {
                    final MagicLocationType fromLocation=card.getLocation();
                    game.doAction(new MagicRemoveCardAction(
                        card,
                        fromLocation
                    ));
                    game.doAction(new MagicMoveCardAction(
                        card,
                        fromLocation,
                        MagicLocationType.Exile
                    ));
                }
            });
        }
    };

    @Override
    public MagicCondition[] getConditions() {
        return conds;
    }
}
