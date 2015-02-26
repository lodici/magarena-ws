package magic.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum MagicManaType {

    Colorless("colorless","{1}"),
    Black("black","{B}"),
    Blue("blue","{U}"),
    Green("green","{G}"),
    Red("red","{R}"),
    White("white","{W}"),
    NONE("none","{N}"),
    ;

    public static final List<MagicManaType> ALL_COLORS = Collections.unmodifiableList(Arrays.asList(
        Black,Blue,Green,Red,White));
    public static final List<MagicManaType> ALL_TYPES = Collections.unmodifiableList(Arrays.asList(
        Colorless,Black,Blue,Green,Red,White)); // Colorless must be in front.

    public static final int NR_OF_TYPES = ALL_TYPES.size();

    private final String name;
    private final String text;

    private MagicManaType(final String name, final String text) {
        this.name=name;
        this.text=text;
    }

    public boolean isValid() {
        return this != MagicManaType.NONE;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public static MagicManaType get(final String name) {
        for (final MagicManaType type : values()) {
            if (type.toString().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new RuntimeException("unknown mana type \"" + name + "\"");
    }

    public static List<MagicManaType> getList(final String name) {
        if ("{1}".equals(name)) {
            return Arrays.asList(Colorless);
        }
        if ("one mana of any color".equals(name)) {
            return ALL_TYPES;
        }
        final String[] tokens = name.split(" or ");
        final MagicManaType[] types = new MagicManaType[tokens.length + 1];
        types[0] = Colorless;
        for (int i = 0; i < tokens.length; i++) {
            types[i + 1] = get(tokens[i]);
        }
        return Arrays.asList(types);
    }

    @Override
    public String toString() {
        return text;
    }

    public MagicColor getColor() {
        switch (this) {
            case Black: return MagicColor.Black;
            case Blue: return MagicColor.Blue;
            case Green: return MagicColor.Green;
            case Red: return MagicColor.Red;
            case White: return MagicColor.White;
        }
        throw new RuntimeException("No color available for MagicManaType " + this);
    }
}
