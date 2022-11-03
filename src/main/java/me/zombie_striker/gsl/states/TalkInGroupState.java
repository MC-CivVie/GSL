package me.zombie_striker.gsl.states;

import me.zombie_striker.gsl.namelayers.NameLayer;

public class TalkInGroupState extends PlayerState {

    private NameLayer chatGroup;

    public TalkInGroupState(NameLayer nameLayer){
        this.chatGroup = nameLayer;
    }

    public NameLayer getChatGroup() {
        return chatGroup;
    }
}
