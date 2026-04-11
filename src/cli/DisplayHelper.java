package cli;

import base.PlayerBase;

public class DisplayHelper {
    public void showWelcome() {
        System.out.println("==================================");
        System.out.println("     TURN-BASED COMBAT GAME");
        System.out.println("==================================");
    }

    public void showClassMenu() {
        System.out.println("\nChoose your class:");
        System.out.println("1. Warrior");
        System.out.println("2. Wizard");
    }

    public void showPlayerInfo(PlayerBase player) {
        System.out.println("\n===== PLAYER INFO =====");
        System.out.println("Name: " + player.getName());
        System.out.println("HP: " + player.getCurrentHp() + "/" + player.getMaxHp()); //get the "currentHp out of maxHp"
        System.out.println("ATK: " + player.getAttack());
        System.out.println("DEF: " + player.getDefense());
        System.out.println("SPD: " + player.getSpeed());
    }
}
