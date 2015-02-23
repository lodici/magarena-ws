package magic.exception;

import magic.model.MagicDeck;

@SuppressWarnings("serial")
public class InvalidDeckException extends Exception {

    public InvalidDeckException(final MagicDeck deck) {
        super("[" + deck.getName()+ "] " + deck.getDescription().replace("\n", " "));
    }

    public InvalidDeckException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
