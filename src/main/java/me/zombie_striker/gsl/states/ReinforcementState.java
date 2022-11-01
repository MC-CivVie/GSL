package me.zombie_striker.gsl.states;

import me.zombie_striker.gsl.materials.MaterialType;
import me.zombie_striker.gsl.namelayers.NameLayer;

public class ReinforcementState extends PlayerState {

    private NameLayer nameLayer;
    private MaterialType reinforceGroup;

    public ReinforcementState(NameLayer nameLayer, MaterialType reinforceGroup) {
        this.nameLayer = nameLayer;
        this.reinforceGroup = reinforceGroup;
    }

    public MaterialType getReinforceGroup() {
        return reinforceGroup;
    }

    public NameLayer getNameLayer() {
        return nameLayer;
    }
}
