package magic.model.event;

import magic.model.MagicSource;
import magic.model.condition.MagicCondition;

import java.util.regex.Matcher;

public abstract class MagicSourceEvent {
    private final MagicRuleEventAction rule;
    private final Matcher matcher;

    public MagicSourceEvent(final MagicRuleEventAction aRule, final Matcher aMatcher) {
        rule = aRule;
        matcher = aMatcher;
    }

    public abstract MagicEvent getEvent(final MagicSource source);

    public MagicCondition[] getConditions() {
        return rule.getConditions(matcher);
    }

    public MagicTiming getTiming() {
        return rule.getTiming(matcher);
    }
    
    public String getName() {
        return rule.getName(matcher);
    }
    
    public boolean isIndependent() {
        return rule.isIndependent();
    }
}
