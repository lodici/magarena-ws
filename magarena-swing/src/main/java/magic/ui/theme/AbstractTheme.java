package magic.ui.theme;

import magic.ui.IconImages;
import magic.ui.widget.FontsAndBorders;

import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import magic.data.MagicIcon;

public abstract class AbstractTheme implements Theme {

    // Prevents horizontal scroll-bar appearing in the user action panel
    // for custom themes with a VALUE_SPACING less than 5.
    private static final int MIN_VALUE_SPACING = 5;

    private final String name;
    private final Map<String,Object> themeMap;

    AbstractTheme(final String name) {

        this.name=name;
        themeMap=new HashMap<>();

        addToTheme(ICON_LIFE,IconImages.getIcon(MagicIcon.LIFE));
        addToTheme(ICON_PREVENT,IconImages.getIcon(MagicIcon.PREVENT2));
        addToTheme(ICON_POISON,IconImages.getIcon(MagicIcon.POISON));
        addToTheme(ICON_LAND,IconImages.getIcon(MagicIcon.LAND2));
        addToTheme(ICON_HAND,IconImages.getIcon(MagicIcon.HAND2));
        addToTheme(ICON_LIBRARY,IconImages.getIcon(MagicIcon.LIBRARY2));
        addToTheme(ICON_GRAVEYARD,IconImages.getIcon(MagicIcon.GRAVEYARD2));
        addToTheme(ICON_MESSAGE,IconImages.getIcon(MagicIcon.LOG));
        addToTheme(ICON_SMALL_BATTLEFIELD,IconImages.getIcon(MagicIcon.ALL));
        addToTheme(ICON_SMALL_COMBAT,IconImages.getIcon(MagicIcon.COMBAT));
        addToTheme(ICON_SMALL_STACK,IconImages.getIcon(MagicIcon.SPELL));
        addToTheme(ICON_SMALL_HAND,IconImages.getIcon(MagicIcon.HAND));
        addToTheme(ICON_SMALL_GRAVEYARD,IconImages.getIcon(MagicIcon.GRAVEYARD));
        addToTheme(ICON_SMALL_EXILE,IconImages.getIcon(MagicIcon.EXILE));

        addToTheme(COLOR_TITLE_FOREGROUND,Color.WHITE);
        addToTheme(COLOR_TITLE_BACKGROUND,new Color(0x23,0x6B,0x8E));
        addToTheme(COLOR_ICON_BACKGROUND,FontsAndBorders.GRAY3);
        addToTheme(COLOR_CHOICE_FOREGROUND,Color.BLUE);
        addToTheme(COLOR_COMMON_FOREGROUND,Color.BLACK);
        addToTheme(COLOR_UNCOMMON_FOREGROUND,new Color(0x8C,0x78,0x53));
        addToTheme(COLOR_RARE_FOREGROUND,new Color(0xCD,0x7F,0x32));
        addToTheme(OPTION_USE_OVERLAY,false);
        addToTheme(COLOR_CHOICE,new Color(0,250,0,70));
        addToTheme(COLOR_COMBAT_CHOICE,new Color(250,100,0,90));
        addToTheme(COLOR_CHOICE_BORDER,new Color(0,250,0,70));
        addToTheme(COLOR_COMBAT_CHOICE_BORDER,new Color(250,0,0,125));
        addToTheme(COLOR_SELECTED_PLAYER,Color.RED);
        addToTheme(COLOR_GAME_BORDER,Color.BLACK);
        addToTheme(COLOR_VIEWER_BACKGROUND,Color.WHITE);
        addToTheme(COLOR_SEPARATOR_BACKGROUND,Color.LIGHT_GRAY);

        addToTheme(VALUE_SPACING, MIN_VALUE_SPACING);
        addToTheme(VALUE_BACKGROUND_STRETCH,0);
        addToTheme(VALUE_GAME_LAYOUT,1);
        addToTheme(VALUE_GAME_STRETCH,0);
        addToTheme(VALUE_GAME_OFFSET,0);
        addToTheme(VALUE_GAME_BORDER,0);
    }

    final void addToTheme(final String aName, final Object value) {
        if (VALUE_SPACING.equals(aName) && (int) value < MIN_VALUE_SPACING) {
            themeMap.put(aName, MIN_VALUE_SPACING);
        } else {
            themeMap.put(aName,value);
        }
    }

    @Override
    public void load() {

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public BufferedImage getTexture(final String aName) {
        final Object value=themeMap.get(aName);
        return value==null?IconImages.MISSING:(BufferedImage)value;
    }

    @Override
    public Boolean getOptionUseOverlay() {
        final Object value = themeMap.get(OPTION_USE_OVERLAY);
        return (Boolean)value;
    }

    @Override
    public ImageIcon getIcon(final String aName) {
        final Object value=themeMap.get(aName);
        return value==null?IconImages.getIcon(MagicIcon.MISSING_ICON):(ImageIcon)value;
    }

    @Override
    public Color getColor(final String aName) {
        final Object value=themeMap.get(aName);
        return value==null?Color.BLACK:(Color)value;
    }

    @Override
    public Color getTextColor() {
        return getColor(COLOR_TEXT_FOREGROUND);
    }

    @Override
    public Color getNameColor() {
        return getColor(COLOR_NAME_FOREGROUND);
    }

    @Override
    public Color getChoiceColor() {
        return getColor(COLOR_CHOICE);
    }

    @Override
    public int getValue(final String aName) {
        final Object value=themeMap.get(aName);
        return value==null?0:(Integer)value;
    }

    @Override
    public ImageIcon getAvatarIcon(final int index,final int size) {
        return AvatarImages.getInstance().getAvatarIcon(index,size);
    }
    
    @Override
    public ImageIcon getAbilityIcon(AbilityIcon ability) {
        final String key = "icon_" + ability.name().toLowerCase();
        final Object value = themeMap.get(key);
        return value == null ? null : (ImageIcon)value;
    }

}
