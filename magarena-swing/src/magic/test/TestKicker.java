
package magic.test;

import magic.model.MagicDeckProfile;
import magic.model.MagicDuel;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerDefinition;
import magic.model.phase.MagicMainPhase;

class TestKicker extends TestGameBuilder {
    public MagicGame getGame() {
        final MagicDuel duel=new MagicDuel();
        duel.setDifficulty(6);

        final MagicDeckProfile profile=new MagicDeckProfile("bgruw");
        final MagicPlayerDefinition player1=new MagicPlayerDefinition("Player",false,profile);
        final MagicPlayerDefinition player2=new MagicPlayerDefinition("Computer",true,profile);
        duel.setPlayers(new MagicPlayerDefinition[]{player1,player2});
        duel.setStartPlayer(0);

        final MagicGame game=duel.nextGame();
        game.setPhase(MagicMainPhase.getFirstInstance());
        final MagicPlayer player=game.getPlayer(0);
        final MagicPlayer opponent=game.getPlayer(1);

        MagicPlayer P = player;

        P.setLife(1);
        addToLibrary(P, "Plains", 10);
        createPermanent(game,P,"Rupture Spire",false,8);
        createPermanent(game,P,"Thieving Magpie",false,1);
        addToHand(P,"Ravaging Riftwurm",1);
        addToHand(P,"Sphinx of Lost Truths",1);
        addToHand(P,"Wolfbriar Elemental",1);
        addToHand(P,"Pincer Spider",1);
        addToHand(P,"Pouncing Kavu",1);
        addToHand(P,"Pouncing Wurm",1);
        addToHand(P,"Gatekeeper of Malakir",1);
        addToHand(P,"Lightkeeper of Emeria",1);
        addToHand(P,"Bloodhusk Ritualist",1);
        addToHand(P,"Deathforge Shaman",1);
        addToHand(P,"Into the Roil",1);

        P = opponent;

        P.setLife(20);
        addToLibrary(P, "Plains", 10);
        createPermanent(game,P,"Rupture Spire",false,8);
        createPermanent(game,P,"Thieving Magpie",false,1);
        addToHand(P, "Plains", 7);

        return game;
    }
}
