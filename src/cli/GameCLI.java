package cli;

import base.PlayerBase;
import playerclass.Warrior;
import playerclass.Wizard;

public class GameCLI {
    private InputHandler input;
    private DisplayHelper display;

    public GameCLI() {
        input = new InputHandler();
        display = new DisplayHelper();
    }

    public void start() {
        display.showWelcome();//show welcome screen

        String name = input.readLine("Enter your name: ");//ask for name

        PlayerBase player = choosePlayerClass(name);//choose wizard or warrior

        display.showPlayerInfo(player);//show the player stats
    }

    private PlayerBase choosePlayerClass(String name) {
        display.showClassMenu(); //show the menu
        int choice = input.readInt("Enter choice: ", 1, 2);
        //create the corresponding player object based on the player's choice: wizard or warrior
        if (choice == 1) {
            return new Warrior(name);
        } else {
            return new Wizard(name);
        }
    }
}
