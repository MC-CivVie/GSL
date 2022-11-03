package me.zombie_striker.gsl.entities;

import org.bukkit.Material;

public class TradingBooth {
  private Material base;
  private Material itemPriceMat;
  private int itemPriceEach;
  private int itemCount;
  private boolean isActive;

  public TradingBooth(Material base, Material itemPriceMat, int itemPriceEach) {
    this.base = base;
    this.itemPriceMat = itemPriceMat;
    this.itemPriceEach = itemPriceEach;
    this.itemCount = 0;
    this.isActive = false;
  }

  public Material getBase() {
    return this.base;
  }

  public Material getPriceMat() {
    return this.itemPriceMat;
  }

  public int getPriceEach() {
    return this.itemPriceEach;
  }

  public int getItemCount() {
    return this.itemCount;
  }

  public boolean getActive() {
    return this.isActive;
  }
}
