package magic.game.state;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import magic.ai.MagicAI;
import magic.ai.MagicAIImpl;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerDefinition;

public final class GameStateSnapshot {
    private GameStateSnapshot() {}

    public static GameState getGameState(final MagicGame game) {

        final GameState gameState = new GameState();

        gameState.setDifficulty(game.getDuel().getDifficulty());
        // will always be 0 since it is not possible to save when AI has priority.
        gameState.setStartPlayerIndex(game.getPriorityPlayer().getIndex());

        // Save each player's state.
        for (int i = 0; i < game.getPlayers().length; i++) {
            saveGamePlayerState(i, gameState, game);
        }

        return gameState;
    }

    private static String getAiType(final MagicAI ai) {
        if (ai != null) {
            for (MagicAIImpl aiType : MagicAIImpl.SUPPORTED_AIS) {
                if (aiType.getAI() == ai) {
                    return aiType.name();
                }
            }
        }
        return "";
    }

    private static void saveGamePlayerState(final int playerIndex, final GameState gameState, final MagicGame game) {
        final MagicDuel duel = game.getDuel();
        final MagicPlayerDefinition playerDef = duel.getPlayer(playerIndex);
        final GamePlayerState gamePlayerState = gameState.getPlayer(playerIndex);
        gamePlayerState.setName(playerDef.getName());
//        gamePlayerState.setFace(playerDef.getAvatar().getFace());
        gamePlayerState.setDeckProfileColors(playerDef.getDeckProfile().getColorText());
        if (playerDef.isArtificial()) {
            gamePlayerState.setAiType(getAiType(duel.getAIs()[playerIndex]));
        }
        final MagicPlayer player = game.getPlayer(playerIndex);
        gamePlayerState.setLife(player.getLife());
        savePlayerLibraryState(player, gamePlayerState);
        savePlayerHandState(player, gamePlayerState);
        savePlayerPermanentsState(player, gamePlayerState);
        savePlayerGraveyardState(player, gamePlayerState);
        savePlayerExiledState(player, gamePlayerState);
    }

    private static void savePlayerPermanentsState(final MagicPlayer player, final GamePlayerState gamePlayerState) {
        final Map<GameCardState, Integer> cards = new HashMap<>();
        for (final MagicPermanent card : player.getPermanents()) {
            final GameCardState tsCard = new GameCardState(card.getCardDefinition().getFullName(), 0, card.isTapped());
            updateCardCount2(tsCard, cards);
        }
        for (final GameCardState card : cards.keySet()) {
            gamePlayerState.addToPermanents(card.getCardName(), card.isTapped(), (int)cards.get(card));
        }
    }

    private static void savePlayerGraveyardState(final MagicPlayer player, final GamePlayerState gamePlayerState) {
        final Map<MagicCardDefinition, Integer> cards = getZoneCardDefs(player.getGraveyard());
        for (MagicCardDefinition cardDef : cards.keySet()) {
            gamePlayerState.addToGraveyard(cardDef.getName(), (int)cards.get(cardDef));
        }
    }

    private static void savePlayerHandState(final MagicPlayer player, final GamePlayerState gamePlayerState) {
        final Map<MagicCardDefinition, Integer> cards = getZoneCardDefs(player.getHand());
        for (MagicCardDefinition cardDef : cards.keySet()) {
            gamePlayerState.addToHand(cardDef.getName(), (int)cards.get(cardDef));
        }
    }

    private static void savePlayerLibraryState(final MagicPlayer player, final GamePlayerState gamePlayerState) {
        final Map<MagicCardDefinition, Integer> cards = getZoneCardDefs(player.getLibrary());
        for (MagicCardDefinition cardDef : cards.keySet()) {
            gamePlayerState.addToLibrary(cardDef.getName(), (int)cards.get(cardDef));
        }
    }

    private static void savePlayerExiledState(final MagicPlayer player, final GamePlayerState gamePlayerState) {
        final Map<MagicCardDefinition, Integer> cards = getZoneCardDefs(player.getExile());
        for (MagicCardDefinition cardDef : cards.keySet()) {
            gamePlayerState.addToExiled(cardDef.getName(), (int)cards.get(cardDef));
        }
    }

    private static Map<MagicCardDefinition, Integer> getZoneCardDefs(final List<MagicCard> cards) {
        final Map<MagicCardDefinition, Integer> cardDefs = new HashMap<>();
        for (MagicCard card : cards) {
            updateCardCount(card.getCardDefinition(), cardDefs);
        }
        return cardDefs;
    }
 
    private static void updateCardCount(final MagicCardDefinition cardDef, final Map<MagicCardDefinition, Integer> cardDefs) {
        if (cardDefs.containsKey(cardDef)) {
            int count = cardDefs.get(cardDef);
            cardDefs.remove(cardDef);
            count++;
            cardDefs.put(cardDef, count);
        } else {
            cardDefs.put(cardDef, 1);
        }
    }
    private static void updateCardCount2(final GameCardState c, final Map<GameCardState, Integer> cards) {
        if (cards.containsKey(c)) {
            int count = cards.get(c);
            cards.remove(c);
            count++;
            cards.put(c, count);
        } else {
            cards.put(c, 1);
        }
    }

}
