package cli;
public class GameCLI {
    
    public void start() {
        display.showWelcome();

        String name = input.readLine("Enter your name: ");

        PlayerBase player = choosePlayerClass(name);

        display.showPlayerInfo(player)
    
    }
