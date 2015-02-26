package magic.ui.screen.interfaces;

import java.nio.file.Path;
import magic.data.DeckType;
import magic.model.MagicDeck;

public interface IDeckConsumer {
    void setDeck(String deckName, DeckType deckType);
    void setDeck(MagicDeck deck, Path deckPath);
    //void setRandomDeck(String randomDeck);
}
