package me.zombie_striker.gsl.recipes;

import me.zombie_striker.gsl.materials.MaterialType;

import java.util.HashMap;

public class FactoryRecipe {

    private HashMap<MaterialType, Integer> ingredients = new HashMap<>();
    private HashMap<MaterialType, Integer> results = new HashMap<>();
    private String name;
    private String displayname;

    public FactoryRecipe(String name, String displayname){
        this.name = name;
        this.displayname = displayname;
    }

    public String getDisplayname() {
        return displayname;
    }

    public String getName() {
        return name;
    }

    public HashMap<MaterialType, Integer> getIngredients() {
        return ingredients;
    }

    public HashMap<MaterialType, Integer> getResults() {
        return results;
    }
}
