package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MoveCardAction;
import magic.model.action.RemoveCardAction;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;

public class MagicExileTopLibraryEvent extends MagicEvent{
    
    private final MagicCondition cond;

    public MagicExileTopLibraryEvent(final MagicSource source, final int amount) {
        this(source, source.getController(), amount);
    }
    
    public MagicExileTopLibraryEvent(final MagicSource source, final MagicPlayer player, final int amount) {
        super(
            source,
            player,
            amount,
            EVENT_ACTION,
            "PN exiles the top RN cards of his or her library."
        );
        cond = MagicConditionFactory.LibraryAtLeast(amount);
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCardList exile = new MagicCardList(event.getPlayer().getLibrary().getCardsFromTop(event.getRefInt()));
            for (final MagicCard card : exile) {
                game.doAction(new RemoveCardAction(card,MagicLocationType.OwnersLibrary));
                game.doAction(new MoveCardAction(card,MagicLocationType.OwnersLibrary,MagicLocationType.Exile));
            }
        }
    };
    
    @Override
    public boolean isSatisfied() {
        return cond.accept(getSource()) && super.isSatisfied();
    }
}
