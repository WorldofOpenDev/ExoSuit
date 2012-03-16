/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.celestiusmc.exosuit;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

/**
 * ExoSuit listener.
 */
public class ExoSuitListener implements Listener {
    private Set<Player> jumping = new HashSet<Player>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (event.getTo().getY() <= event.getFrom().getY()) {
            jumping.remove(player);
        }

        if ((event.getTo().getY() > event.getFrom().getY())
                && player.hasPermission("exosuit.helmet")
                && event.getPlayer().getInventory().getBoots().getType().equals(Material.DIAMOND_BOOTS)) {
            double factor = 8.0;

            if (!jumping.contains(player) && (factor != 0.0D)) {
                Vector newDirection = player.getVelocity();
                newDirection.multiply(-factor);
                player.setVelocity(newDirection);

                jumping.add(player);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        jumping.remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        jumping.remove(event.getPlayer());
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Player)) {
            return;
        }

        Player player = (Player) entity;

        //Helmet check
        if (player.hasPermission("exosuit.helmet")
                && event.getCause().equals(EntityDamageEvent.DamageCause.DROWNING)
                && player.getInventory().getHelmet().getType().equals(Material.DIAMOND_HELMET)) {
            event.setDamage(0);
        }

        //Chestplate check
        if (player.hasPermission("exosuit.chestplate")
                && (event.getCause().equals(EntityDamageEvent.DamageCause.FIRE)
                || event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK))
                && player.getInventory().getChestplate().getType().equals(Material.DIAMOND_CHESTPLATE)) {
            event.setDamage(0);
        }

        //Leggings check
        if (player.hasPermission("exosuit.leggings")
                && event.getCause().equals(EntityDamageEvent.DamageCause.FALL)
                && player.getInventory().getLeggings().getType().equals(Material.DIAMOND_LEGGINGS)) {
            event.setDamage(0);
        }

    }

}
