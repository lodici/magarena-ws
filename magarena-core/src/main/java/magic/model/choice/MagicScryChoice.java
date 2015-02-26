package magic.model.choice;

import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.exception.UndoClickedException;
import java.util.ArrayList;
import java.util.List;
import magic.model.IUIGameController;

public class MagicScryChoice extends MagicMayChoice {
    public MagicScryChoice() {
        super("Move this card from the top of your library to the bottom?");
    }
    
    @Override
    public List<Object[]> getArtificialChoiceResults(
            final MagicGame game,
            final MagicEvent event,
            final MagicPlayer player,
            final MagicSource source) {
        
        if (player.getLibrary().isEmpty()) {
            final List<Object[]> choiceResultsList=new ArrayList<>();
            choiceResultsList.add(new Object[]{NO_CHOICE});
            return choiceResultsList;
        } else {
            return NO_OTHER_CHOICE_RESULTS;
        }
    }

    @Override
    public Object[] getPlayerChoiceResults(
            final IUIGameController controller,
            final MagicGame game,
            final MagicPlayer player,
            final MagicSource source) throws UndoClickedException {
        
        final Object[] choiceResults=new Object[1];
        choiceResults[0]=NO_CHOICE;
        
        if (player.getLibrary().isEmpty()) {
            return choiceResults;
        }
        
        final MagicCardList cards = new MagicCardList();
        cards.add(player.getLibrary().getCardAtTop());
        controller.showCards(cards);

        controller.disableActionButton(false);
        controller.clearCards();

        if (controller.getMayChoice(source, getDescription())) {
            choiceResults[0]=YES_CHOICE;
        }

        return choiceResults;
    }
}
