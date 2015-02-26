package magic.test;

import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerDefinition;
import magic.model.phase.MagicMainPhase;

class TestLegendaries extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=new MagicDuel();
        duel.setDifficulty(6);

        final MagicDeckProfile profile=new MagicDeckProfile("bgruw");
        final MagicPlayerDefinition player1=new MagicPlayerDefinition("Player", false, profile);
        final MagicPlayerDefinition player2=new MagicPlayerDefinition("Computer", true, profile);
        duel.setPlayers(new MagicPlayerDefinition[]{player1, player2});
        duel.setStartPlayer(0);

        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(9);
        addToLibrary(P, "Plains", 10);
        createPermanent(game, P, "Karakas", false, 1);
        createPermanent(game, P, "Kor Haven", false, 1);
        createPermanent(game, P, "Maze of Ith", false, 1);
        createPermanent(game, P, "Mikokoro, Center of the Sea", false, 1);
        createPermanent(game, P, "Miren, the Moaning Well", false, 1);
        createPermanent(game, P, "Okina, Temple to the Grandfathers", false, 1);
        createPermanent(game, P, "Azusa, Lost but Seeking", false, 1);
        createPermanent(game, P, "Captain Sisay", false, 1);
        createPermanent(game, P, "Knight of the Reliquary", false, 1);
        createPermanent(game, P, "Kokusho, the Evening Star", false, 1);
        addToHand(P, "Kor Haven", 1);
        addToHand(P, "Maze of Ith", 1);
        addToHand(P, "Mikokoro, Center of the Sea", 1);
        addToHand(P, "Miren, the Moaning Well", 1);
        addToHand(P, "Okina, Temple to the Grandfathers", 1);


        P = opponent;

        P.setLife(9);
        addToLibrary(P, "Plains", 10);
        createPermanent(game, P, "Karakas", false, 1);
        createPermanent(game, P, "Kor Haven", false, 1);
        createPermanent(game, P, "Maze of Ith", false, 1);
        createPermanent(game, P, "Mikokoro, Center of the Sea", false, 1);
        createPermanent(game, P, "Miren, the Moaning Well", false, 1);
        createPermanent(game, P, "Okina, Temple to the Grandfathers", false, 1);
        createPermanent(game, P, "Azusa, Lost but Seeking", false, 1);
        createPermanent(game, P, "Captain Sisay", false, 1);
        createPermanent(game, P, "Knight of the Reliquary", false, 1);
        createPermanent(game, P, "Kokusho, the Evening Star", false, 1);
        addToHand(P, "Kor Haven", 1);
        addToHand(P, "Maze of Ith", 1);
        addToHand(P, "Mikokoro, Center of the Sea", 1);
        addToHand(P, "Miren, the Moaning Well", 1);
        addToHand(P, "Okina, Temple to the Grandfathers", 1);

        return game;
    }
}
