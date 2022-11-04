package me.zombie_striker.gsl.megabuilds;

import me.zombie_striker.gsl.materials.MaterialType;
import me.zombie_striker.gsl.recipes.FactoryRecipe;
import me.zombie_striker.gsl.utils.InventoryUtil;
import me.zombie_striker.gsl.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.Furnace;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MegaBuild {

    private List<Location> blocks = new LinkedList<>();
    private Location center;
    private MegaBuildType type;

    private FactoryRecipe recipe;

    private int power = 0;
    private boolean active = false;

    public MegaBuild(MegaBuildType type, Location center, Location... blocks) {
        this.center = center;
        this.type = type;
        for (Location l : blocks) {
            this.blocks.add(l);
        }
    }

    public FactoryRecipe getRecipe() {
        return recipe;
    }

    public void setRecipe(FactoryRecipe recipe) {
        this.recipe = recipe;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public Location getCenter() {
        return center;
    }

    public MegaBuildType getType() {
        return type;
    }

    public List<Location> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<Location> blocks) {
        this.blocks = blocks;
    }

    public void tick() {
        if (recipe == null) {
            setActive(false);
            return;
        }
        boolean foundFuel = false;
        loop:
        for (int x = 0; x < type.getLabels().length; x++) {
            for (int y = 0; y < type.getLabels()[x].length; y++) {
                for (int z = 0; z < type.getLabels()[x][y].length; z++) {
                    String label = type.getLabels()[x][y][z];
                    if (label != null && label.equals("engine")) {
                        Block furance = center.clone().subtract(type.getOffsetX(), type.getOffsetY(), type.getOffsetZ()).add(x, y, z).getBlock();
                        if (furance.getType() == Material.FURNACE) {
                            Furnace furnacestate = (Furnace) furance.getState();
                            if (InventoryUtil.hasAtleast(MaterialType.getMaterialType("CHARCOAL"), 1, furnacestate.getInventory())) {
                                furnacestate.setBurnTime((short) 10);
                                furnacestate.update();
                                InventoryUtil.removeAmount(MaterialType.getMaterialType("CHARCOAL"), 1, furnacestate.getInventory());
                                foundFuel = true;
                                power++;
                                break loop;
                            } else {
                                furnacestate.setBurnTime((short) 0);
                                furnacestate.update();
                                setActive(false);
                                break loop;
                            }
                        }
                    }
                }
            }
        }

        if (foundFuel) {
            if (power >= recipe.getPower()) {
                power -= recipe.getPower();

                for (int x = 0; x < type.getLabels().length; x++) {
                    for (int y = 0; y < type.getLabels()[x].length; y++) {
                        for (int z = 0; z < type.getLabels()[x][y].length; z++) {
                            String label = type.getLabels()[x][y][z];
                            if (label != null && label.equals("container")) {
                                Block container = center.clone().subtract(type.getOffsetX(), type.getOffsetY(), type.getOffsetZ()).add(x, y, z).getBlock();
                                if (container.getState() instanceof Container) {
                                    Container containerstate = (Container) container.getState();
                                    boolean hasAll = true;
                                    for (Map.Entry<MaterialType, Integer> e : recipe.getIngredients().entrySet()) {
                                        if(!InventoryUtil.hasAtleast(e.getKey(),e.getValue(),containerstate.getInventory())){
                                            hasAll=false;
                                            break;
                                        }
                                    }
                                    if(hasAll){
                                        for (Map.Entry<MaterialType, Integer> e : recipe.getIngredients().entrySet()) {
                                            InventoryUtil.removeAmount(e.getKey(),e.getValue(),containerstate.getInventory());
                                        }
                                        for(Map.Entry<MaterialType, Integer> e : recipe.getResults().entrySet()){
                                            containerstate.getInventory().addItem(ItemUtil.setAmount(e.getKey().toItemStack(),e.getValue()));
                                        }
                                    }else{
                                        setActive(false);
                                    }
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
