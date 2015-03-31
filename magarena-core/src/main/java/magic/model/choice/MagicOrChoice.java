package magic.model.choice;

import magic.data.GeneralConfig;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicEvent;
import magic.exception.UndoClickedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import magic.model.IUIGameController;

public class MagicOrChoice extends MagicChoice {

    private final MagicChoice[] choices;

    public MagicOrChoice(final String description,final MagicChoice... aChoices) {
        super(description);
        choices = aChoices;
    }

    public MagicOrChoice(final MagicChoice... choices) {
        this("Choose the mode.", choices);
    }
    
    @Override
    public MagicTargetChoice getTargetChoice(final Object[] chosen) {
        final int idx = (Integer)chosen[0] - 1;
        return (MagicTargetChoice)choices[idx];
    }

    @Override
    public boolean hasOptions(final MagicGame game,final MagicPlayer player,final MagicSource source,final boolean hints) {
        for (final MagicChoice choice: choices) {
            if (choice.hasOptions(game, player, source, hints)) {
                return true;
            }
        }
        return false;
    }

    @Override
    Collection<Object> getArtificialOptions(
            final MagicGame game,
            final MagicEvent event,
            final MagicPlayer player,
            final MagicSource source) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Object[]> getArtificialChoiceResults(
            final MagicGame game,
            final MagicEvent event,
            final MagicPlayer player,
            final MagicSource source) {

        final List<Object[]> choiceResultsList=new ArrayList<>();
        for (int i = 0; i < choices.length; i++) {
            if (choices[i].hasOptions(game,player,source,true)) {
                for (final Object obj : choices[i].getArtificialOptions(game,event,player,source)) {
                    choiceResultsList.add(new Object[] {
                        i + 1,
                        obj
                    });
                }
                if (choices[i].isValid() == false) {
                    choiceResultsList.add(new Object[] {
                        i + 1
                    });
                }
            }
        }
       
        if (choiceResultsList.isEmpty()) {
            choiceResultsList.add(new Object[]{0});
        }

        return choiceResultsList;
    }

    @Override
    public Object[] getPlayerChoiceResults(
            final IUIGameController controller,
            final MagicGame game,
            final MagicPlayer player,
            final MagicSource source) throws UndoClickedException {
        
        final boolean hints = GeneralConfig.getInstance().getSmartTarget();
        final List<Integer> availableModes = new ArrayList<>();
        for (int i = 0; i < choices.length; i++) {
            if (choices[i].hasOptions(game,player,source,hints)) {
                availableModes.add(i + 1);
            }
        }

        if (availableModes.isEmpty()) {
            return new Object[]{0};
        }

        controller.disableActionButton(false);
        final int mode = controller.getModeChoice(source, availableModes);
        return choices[mode - 1].isValid() ?
            new Object[] {
                mode,
                choices[mode - 1].getPlayerChoiceResults(controller,game,player,source)[0]
            }:
            new Object[] {
                mode
            };
    }
}
