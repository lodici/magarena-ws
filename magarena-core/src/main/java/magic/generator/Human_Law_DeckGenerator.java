package magic.generator;

import magic.model.MagicCardDefinition;
import magic.model.MagicCondensedDeck;
import magic.model.MagicDeckProfile;
import magic.model.MagicSubType;

public class Human_Law_DeckGenerator extends RandomDeckGenerator {

    private static final String colorText = "w";
    private static final String[] cards = {
        "Champion of the Parish",
        "Champion of the Parish",
        "Champion of the Parish",
        "Elite Vanguard",
        "Gideon's Lawkeeper",
        "Hero of Bladehold",
        "Hero of Bladehold",
        "Hero of Bladehold",
        "Mirran Crusader",
        "Mirran Crusader",
        "Angelic Destiny",
        "Angelic Destiny",
        "Honor of the Pure",
        "Honor of the Pure",
        "Day of Judgment"
    };

    public String getColorText() {
        return colorText;
    }

    public int getMinRarity() {
        return 2;
    }

    public boolean acceptPossibleSpellCard(final MagicCardDefinition card) {
        return !card.isCreature() || card.hasSubType(MagicSubType.Human);
    }

    public void addRequiredSpells(final MagicCondensedDeck deck) {
        addRequiredCards(deck, cards);
    }

    public void setColors(final MagicDeckProfile profile) {
        profile.setColors(getColorText());
    }
}
