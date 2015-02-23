package magic.model;

import magic.model.event.MagicActivation;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicPermanentActivation;
import magic.model.trigger.MagicTrigger;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MagicAbilityList implements MagicAbilityStore {
    private List<MagicAbility> abilities = 
        new LinkedList<MagicAbility>();

    private List<MagicTrigger<?>> triggers = 
        new LinkedList<MagicTrigger<?>>();

    private List<MagicActivation<MagicPermanent>> permActivations =
        new LinkedList<MagicActivation<MagicPermanent>>();

    private List<MagicManaActivation> manaActivations =
        new LinkedList<MagicManaActivation>();

    public void add(final MagicChangeCardDefinition ccd) {
        if (ccd instanceof MagicPermanentActivation) {
            permActivations.add((MagicPermanentActivation)ccd);
        } else if (ccd instanceof MagicManaActivation) {
            manaActivations.add((MagicManaActivation)ccd);
        } else if (ccd instanceof MagicTrigger<?>) {
            triggers.add((MagicTrigger<?>)ccd);
        } else {
            throw new RuntimeException("unknown given ability \"" + ccd + "\"");
        }
    }

    public MagicAbility getFirst() {
        return abilities.get(0);
    }
    
    public void addAbility(final MagicAbility ability) {
        abilities.add(ability);
    }

    public void giveAbility(final MagicPermanent permanent, final Set<MagicAbility> flags) {
        flags.addAll(abilities);

        for (final MagicActivation<MagicPermanent> permAct : permActivations) {
            permanent.addAbility(permAct);
        }
        for (final MagicManaActivation manaAct : manaActivations) {
            permanent.addAbility(manaAct);
        }
        for (final MagicTrigger<?> trigger : triggers) {
            permanent.addAbility(trigger);
        }
    }
    
    public void loseAbility(final MagicPermanent permanent, final Set<MagicAbility> flags) {
        flags.removeAll(abilities);
    }
}
