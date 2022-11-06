package me.zombie_striker.gsl.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;

import java.awt.*;

public class ComponentBuilder {

    public static final Color RED = new Color(200,24,20);
    public static final Color BLUE = new Color(02,20,200);
    public static final Color WHITE = new Color(200,200,200);
    public static final Color GRAY = new Color(100,100,100);
    public static final Color BLACK = new Color(20,20,20);
    public static final Color GREEN = new Color (40,200,40);
    public static final Color LIGHT_BLUE = new Color(50,110,200);

    private Component core;

    public ComponentBuilder clone(){
        return new ComponentBuilder(core.asComponent());
    }

    public ComponentBuilder(Component core){
        this.core = core;
    }
    public ComponentBuilder(String message, Color color){
        this.core = Component.text(message).color(TextColor.color(color.getRGB()));
    }
    public ComponentBuilder append(String message, Color color){
        this.core=core.append(Component.text(message).color(TextColor.color(color.getRGB())));
        return this;
    }
    public ComponentBuilder appendClickableURL(String message, Color color){
        this.core=core.append(Component.text(message).color(TextColor.color(color.getRGB())).clickEvent(ClickEvent.openUrl(message)));
        return this;
    }

    public Component build() {
        return core;
    }
}
