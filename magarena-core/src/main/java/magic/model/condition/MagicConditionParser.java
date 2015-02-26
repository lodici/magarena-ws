package magic.model.condition;

import magic.model.ARG;
import magic.model.MagicCounterType;
import magic.model.MagicAbility;
import magic.model.target.MagicTargetFilterFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MagicConditionParser {
            
    YouControl("you control a(n)? " + ARG.WORDRUN) {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicConditionFactory.YouControl(
                MagicTargetFilterFactory.singlePermanent(ARG.wordrun(arg))
            );
        }
    },
    OpponentControl("an opponent controls a(n)? " + ARG.WORDRUN) {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicConditionFactory.OpponentControl(
                MagicTargetFilterFactory.singlePermanent(ARG.wordrun(arg))
            );
        }
    },
    DefenderControl("defending player controls a(n)? " + ARG.WORDRUN) {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicConditionFactory.OpponentControl(
                MagicTargetFilterFactory.singlePermanent(ARG.wordrun(arg))
            );
        }
    },
    YouControlAnother("you control another " + ARG.WORDRUN) {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicConditionFactory.YouControlAnother(
                MagicTargetFilterFactory.singlePermanent(ARG.wordrun(arg))
            );
        }
    },
    ControlAtLeast("you control " + ARG.AMOUNT + " or more " + ARG.WORDRUN) {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicConditionFactory.YouControlAtLeast(
                MagicTargetFilterFactory.multiple(ARG.wordrun(arg)),
                ARG.amount(arg)
            );
        }
    },
    ControlNone("you control no " + ARG.WORDRUN) {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicConditionFactory.YouControlNone(
                MagicTargetFilterFactory.multiple(ARG.wordrun(arg))
            );
        }
    },
    Threshold("seven or more cards are in your graveyard") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.THRESHOLD_CONDITION;
        }
    },
    ExactlySeven("you have exactly seven cards in hand") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.EXACTLY_SEVEN_CARDS_IN_HAND_CONDITION;
        }
    },
    HandSize("you have " + ARG.AMOUNT + " or more cards in hand") {
        public MagicCondition toCondition(final Matcher arg) {
            final int amount = ARG.amount(arg);
            return MagicConditionFactory.HandAtLeast(amount);
        }
    },
    Hellbent("you have no cards in hand") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.HELLBENT;
        }
    },
    OpponentHellbent("an opponent has no cards in hand") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.OPPONENT_HELLBENT;
        }
    },
    CountersAtLeast("(SN|it) has " + ARG.AMOUNT + " or more " + ARG.WORD1 + " counters on it") {
        public MagicCondition toCondition(final Matcher arg) {
            final int amount = ARG.amount(arg);
            final MagicCounterType counterType = MagicCounterType.getCounterRaw(ARG.word1(arg));
            return MagicConditionFactory.CounterAtLeast(counterType, amount);
        }
    },
    CountersAtLeastAlt("there are " + ARG.AMOUNT + " or more " + ARG.WORD1 + " counters on SN") {
        public MagicCondition toCondition(final Matcher arg) {
            final int amount = ARG.amount(arg);
            final MagicCounterType counterType = MagicCounterType.getCounterRaw(ARG.word1(arg));
            return MagicConditionFactory.CounterAtLeast(counterType, amount);
        }
    },
    CountersAtLeastOne("(SN|it) has a(n)? " + ARG.WORD1 + " counter on it") {
        public MagicCondition toCondition(final Matcher arg) {
            final MagicCounterType counterType = MagicCounterType.getCounterRaw(ARG.word1(arg));
            return MagicConditionFactory.CounterAtLeast(counterType, 1);
        }
    },
    CountersEqual("there (is|are) exactly " + ARG.AMOUNT + " " + ARG.WORD1 + " counter(s)? on SN") {
        public MagicCondition toCondition(final Matcher arg) {
            final int amount = ARG.amount(arg);
            final MagicCounterType counterType = MagicCounterType.getCounterRaw(ARG.word1(arg));
            return MagicConditionFactory.CounterEqual(counterType, amount);
        }
    },
    CountersNone("SN has no " + ARG.WORD1 + " counters on it") {
        public MagicCondition toCondition(final Matcher arg) {
            final MagicCounterType counterType = MagicCounterType.getCounterRaw(ARG.word1(arg));
            return MagicConditionFactory.CounterEqual(counterType, 0);
        }
    },
    IsEquipped("(SN is|it's) equipped") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.IS_EQUIPPED;
        }
    },
    IsEnchanted("(SN is|it's) enchanted") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.IS_ENCHANTED;
        }
    },
    IsUntapped("(SN is|it's) untapped") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.UNTAPPED_CONDITION;
        }
    },
    IsTapped("(SN is|it's) tapped") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.TAPPED_CONDITION;
        }
    },
    IsMonstrous("SN is monstrous") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.IS_MONSTROUS_CONDITION;
        }
    },
    IsBlocked("it's blocked") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.IS_BLOCKED_CONDITION;
        }
    },
    IsAttacking("it's attacking") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.IS_ATTACKING_CONDITION;
        }
    },
    AttackingAlone("it's attacking alone") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.IS_ATTACKING_ALONE_CONDITION;
        }
    },
    HasDefender("it has defender") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicConditionFactory.HasAbility(MagicAbility.Defender);
        }
    },
    NoCardsInGraveyard("there are no cards in your graveyard") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.EMPTY_GRAVEYARD_CONDITION;
        }
    },
    LibraryWithLEQ20Cards("a library has twenty or fewer cards in it") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.LIBRARY_HAS_20_OR_LESS_CARDS_CONDITION;
        }
    },
    OpponentGraveyardWithGEQ10Cards("an opponent has ten or more cards in his or her graveyard") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.OPP_GRAVEYARD_WITH_10_OR_MORE_CARDS_CONDTITION;
        }
    },
    OpponentNotControlWhiteOrBlueCreature("no opponent controls a white or blue creature") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.OPP_NOT_CONTROL_WHITE_OR_BLUE_CREATURE_CONDITION;
        }
    },
    MostCardsInHand("you have more cards in hand than each opponent") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.MOST_CARDS_IN_HAND_CONDITION;
        }
    },
    WarriorCardInGraveyard("a Warrior card is in your graveyard") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.HAS_WARRIOR_IN_GRAVEYARD;
        }
    },
    ArtifactCardInGraveyard("an artifact card is in your graveyard") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.HAS_ARTIFACT_IN_GRAVEYARD;
        }
    },
    NoOpponentCreatures("no opponent controls a creature") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.OPP_NOT_CONTROL_CREATURE_CONDITION;
        }
    },
    IsYourTurn("it's your turn") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.YOUR_TURN_CONDITION;
        }
    },
    IsNotYourTurn("it's not your turn") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.NOT_YOUR_TURN_CONDITION;
        }
    },
    YouLifeOrMore("you have "+ARG.NUMBER+" or more life") {
        public MagicCondition toCondition(final Matcher arg) {
            final int amount = ARG.number(arg);
            return MagicConditionFactory.YouLifeAtLeast(amount);
        }
    },
    YouLifeOrLess("you have "+ARG.NUMBER+" or less life") {
        public MagicCondition toCondition(final Matcher arg) {
            final int amount = ARG.number(arg);
            return MagicConditionFactory.YouLifeOrLess(amount);
        }
    },
    You30LifeOrMoreOpponent10LifeOrLess("you have 30 or more life and an opponent has 10 or less life") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.YOU_30_OR_MORE_OPPPONENT_10_OR_LESS_LIFE;
        }
    },
    OpponentTenLifeOrLess("an opponent has 10 or less life") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.OPPONENT_TEN_OR_LESS_LIFE;
        }
    },
    NoSpellsCastLastTurn("no spells were cast last turn") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.NO_SPELLS_CAST_LAST_TURN;
        }
    },
    TwoOrMoreSpellsCastByPlayerLastTurn("a player cast two or more spells last turn") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.TWO_OR_MORE_SPELLS_CAST_BY_PLAYER_LAST_TURN;
        }
    },
    Once("once each turn") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.ABILITY_ONCE_CONDITION;
        }
    },
    Twice("no more than twice each turn") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.ABILITY_TWICE_CONDITION;
        }
    },
    Thrice("no more than three times each turn") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.ABILITY_THRICE_CONDITION;
        }
    },
    BeforeYourAttack("during your turn, before attackers are declared") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.BEFORE_YOUR_ATTACK_CONDITION;
        }
    },
    BeenAttacked("you've been attacked this step") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.BEEN_ATTACKED;
        }
    },
    DuringAttack("during the declare attackers step") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.DECLARE_ATTACKERS;
        }
    },
    YourTurn("during your turn") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.YOUR_TURN_CONDITION;
        }
    },
    YourUpkeep("during your upkeep") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.YOUR_UPKEEP_CONDITION;
        }
    },
    OpponentsUpkeep("during an opponent's upkeep") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.OPPONENTS_UPKEEP_CONDITION;
        }
    },
    Sorcery("any time you could cast a sorcery") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.SORCERY_CONDITION;
        }
    },
    DuringCombat("during combat") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.DURING_COMBAT;
        }
    },
    NoneOnBattlefield("no " + ARG.WORDRUN + " are on the battlefield") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicConditionFactory.BattlefieldEqual(
                MagicTargetFilterFactory.multiple(ARG.wordrun(arg)), 0
            );
        }
    },
    NoneOnBattlefieldAlt("there are no " + ARG.WORDRUN + " on the battlefield") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicConditionFactory.BattlefieldEqual(
                MagicTargetFilterFactory.multiple(ARG.wordrun(arg)), 0
            );
        }
    },
    AtLeastOneOnBattlefield("there is (a|an) " + ARG.WORDRUN + " on the battlefield") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicConditionFactory.BattlefieldAtLeast(
                MagicTargetFilterFactory.singlePermanent(ARG.wordrun(arg)), 1
            );
        }
    },
    FiveOrMoreIslands("there are " + ARG.AMOUNT + " or more " + ARG.WORDRUN + " on the battlefield") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicConditionFactory.BattlefieldAtLeast(
                MagicTargetFilterFactory.multiple(ARG.wordrun(arg)), ARG.amount(arg)
            );
        }
    },
    MoreCreaturesThanDefending("you control more creatures than defending player") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.MORE_CREATURES_THAN_DEFENDING;
        }
    },
    MoreCreaturesThanAttacking("you control more creatures than attacking player") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.MORE_CREATURES_THAN_ATTACKING;
        }
    },
    MoreLandsThanDefending("you control more lands than defending player") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.MORE_LANDS_THAN_DEFENDING;
        }
    },
    MoreLandsThanAttacking("you control more lands than attacking player") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.MORE_LANDS_THAN_ATTACKING;
        }
    },
    Morbid("a creature died this turn") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.CREATURE_DIED_THIS_TURN;
        }
    },
    YouGainedLifeOrMore("you gained "+ARG.NUMBER+" or more life this turn") {
        public MagicCondition toCondition(final Matcher arg) {
            final int amount = ARG.number(arg);
            return MagicConditionFactory.YouGainLifeOrMore(amount);
        }
    },
    OpponentGainedLife("an opponent gained life this turn") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicConditionFactory.OpponentGainLifeOrMore(1);
        }
    },
    OpponentLostLife("an opponent lost life this turn") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicConditionFactory.OpponentLoseLifeOrMore(1);
        }
    },
    OpponentLostLifeOrMore("an opponent lost "+ARG.NUMBER+" or more life this turn") {
        public MagicCondition toCondition(final Matcher arg) {
            final int amount = ARG.number(arg);
            return MagicConditionFactory.OpponentLoseLifeOrMore(amount);
        }
    },
    YouAttackedWithCreature("you attacked with a creature this turn") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.YOU_ATTACKED_WITH_CREATURE;
        }
    },
    CreatureInAGraveyard("there is a creature card in a graveyard") {
        public MagicCondition toCondition(final Matcher arg) {
            return MagicCondition.CREATURE_IN_A_GRAVEYARD;
        }
    }
    ;

    private final Pattern pattern;
    
    private MagicConditionParser(final String regex) {
        pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }
    
    public Matcher matcher(final String rule) {
        return pattern.matcher(rule);
    }

    public abstract MagicCondition toCondition(final Matcher arg);
    
    public static final MagicCondition build(final String cost) {
        for (final MagicConditionParser rule : values()) {
            final Matcher matcher = rule.matcher(cost);
            if (matcher.matches()) {
                return rule.toCondition(matcher);
            }
        }
        throw new RuntimeException("unknown condition \"" + cost + "\"");
    }
    
    public static MagicCondition[] buildCast(final String costs) {
        final String[] splitCosts = costs.split(" and ");
        final MagicCondition[] conds = new MagicCondition[splitCosts.length + 1];
        conds[0] = MagicCondition.CARD_CONDITION;
        for (int i = 0; i < splitCosts.length; i++) {
            final boolean aiOnly = splitCosts[i].startsWith("with AI ");
            final String processed = splitCosts[i]
                .replaceFirst("^with AI ", "")
                .replaceFirst("^only ", "")
                .replaceFirst("^if ", "")
                .replaceFirst("\\.$", "");
            final MagicCondition cond = build(processed);
            conds[i + 1] = aiOnly ? new MagicArtificialCondition(cond) : cond;
        }
        return conds;
    }
}
