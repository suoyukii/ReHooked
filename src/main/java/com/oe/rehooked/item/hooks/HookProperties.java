package com.oe.rehooked.item.hooks;

import net.minecraft.world.item.Item;

public interface HookProperties {
    // The name to display on the item
    String Name();
    
    // The item properties of the hook
    Item.Properties ItemProperties();
    
    // The number of simultaneous hooks allowed
    int Count();
    
    // The maximum range the hook will travel before being deleted
    double Range();
    
    // The speed at which the hook will travel before hitting a surface
    double Speed();
    
    // The speed at which the player will be pulled into the surface hit by the hook
    double PullSpeed();
    
    // The distance from the impact point to where the chain should attach
    double Length();
    
    // The cooldown between firing hooks in ms
    int Cooldown();
}
