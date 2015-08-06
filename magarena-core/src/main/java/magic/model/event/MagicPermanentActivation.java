package magic.model.event;

import magic.model.MagicCardDefinition;
import magic.model.MagicChangeCardDefinition;
import magic.model.MagicCopyMap;
import magic.model.MagicCopyable;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.ARG;
import magic.model.action.AddStaticAction;
import magic.model.action.PutItemOnStackAction;
import magic.model.choice.MagicChoice;
import magic.model.condition.MagicCondition;
import magic.model.mstatic.MagicStatic;
import magic.model.stack.MagicAbilityOnStack;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class MagicPermanentActivation extends MagicActivation<MagicPermanent> implements MagicChangeCardDefinition, MagicCopyable {

    public MagicPermanentActivation(final MagicActivationHints hints, final String txt) {
        super(MagicActivation.NO_COND,hints,txt);
    }

    public MagicPermanentActivation(final MagicCondition[] conditions, final MagicActivationHints hints, final String txt) {
        super(conditions,hints,txt);
    }

    @Override
    public boolean usesStack() {
        return true;
    }

    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            this,
            EVENT_ACTION,
            "Play activated ability of SN."
        );
    }
    
    @Override
    public final boolean canPlay(final MagicGame game, final MagicPlayer player, final MagicPermanent source, final boolean useHints) {
        boolean canPlay = super.canPlay(game, player, source, useHints);
       
        // More complex check that first executes events without choice, then check conditions of the others
        if (canPlay && source.producesMana()) {
            game.snapshot();
            for (final MagicEvent event : getCostEvent(source)) {
                if (event.hasChoice() == false) {
                    game.executeEvent(event, MagicEvent.NO_CHOICE_RESULTS);
                }
            }
            for (final MagicEvent event : getCostEvent(source)) {
                if (event.hasChoice() == true && event.isSatisfied() == false) {
                    canPlay = false;
                    break;
                }
            }
            game.restore();
        }

        return canPlay;
    }

    @Override
    public MagicCopyable copy(final MagicCopyMap copyMap) {
        return this;
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public final void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanentActivation permanentActivation = event.getRefPermanentActivation();
            final MagicPermanent permanent = event.getPermanent();
            final MagicAbilityOnStack abilityOnStack = new MagicAbilityOnStack(
                permanentActivation,
                permanent,
                game.getPayedCost()
            );
            game.doAction(new PutItemOnStackAction(abilityOnStack));
        }
    };

    @Override
    public final MagicChoice getChoice(final MagicPermanent source) {
        return getPermanentEvent(source,MagicPayedCost.NO_COST).getChoice();
    }

    public abstract Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source);

    public abstract MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost);

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        throw new RuntimeException(getClass() + " did not override executeEvent");
    }

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addAct(this);
    }
    
    public static final MagicPermanentActivation create(final String act) {
        final String[] token = act.split(ARG.COLON, 2);
       
        // build the actual costs
        final String costs = token[0];
        final List<MagicMatchedCostEvent> matchedCostEvents = MagicRegularCostEvent.build(costs);
        assert matchedCostEvents.size() > 0;

        // add restriction as a MagicMatchedCostEvent
        final String[] part = token[1].split(ActivationRestriction);
        if (part.length > 1) {
            matchedCostEvents.addAll(MagicCondition.build(part[1]));
        }

        // parse the effect        
        final String rule = part[0];
        final MagicSourceEvent sourceEvent = MagicRuleEventAction.create(rule);

        boolean isIndependent = sourceEvent.isIndependent();
        for (final MagicMatchedCostEvent matched : matchedCostEvents) {
            isIndependent &= matched.isIndependent();
        }

        return new MagicPermanentActivation(
            sourceEvent.getConditions(),
            new MagicActivationHints(
                sourceEvent.getTiming(),
                isIndependent
            ),
            sourceEvent.getName()
        ) {
            @Override
            public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
                final List<MagicEvent> costEvents = new LinkedList<MagicEvent>();
                for (final MagicMatchedCostEvent matched : matchedCostEvents) {
                    costEvents.add(matched.getEvent(source));
                }
                return costEvents;
            }
       
            @Override
            public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
                return sourceEvent.getEvent(source);
            }
        };
    }

    public static final MagicPermanentActivation SwitchPT(final MagicManaCost cost) {
        return new MagicPermanentActivation(
            new MagicActivationHints(MagicTiming.Pump),
            "Switch"
        ) {
            @Override
            public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
                return Arrays.asList(new MagicPayManaCostEvent(source,cost));
            }
            @Override
            public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
                return new MagicEvent(
                    source,
                    this,
                    "Switch SN's power and toughness until end of turn."
                );
            }
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new AddStaticAction(event.getPermanent(), MagicStatic.SwitchPT));
            }
        };
    }
}
