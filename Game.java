/**
 *  This class is the main class of the "The World Beside Us" application. 
 *  The World Beside US" is a text based adventure game.  Users can walk
 *  around and interact with surreal scenery.
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 *  base code written by Michael Kölling and David J. Barnes 2016.02.29
 *  
 *  @author Artemis MacDuffie
 *  @version 2023.03.27
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room busStop, alley, marketWest, marketSquare, marketEast;
        Room inn, stall, maskShop, trinket, dirtPath;
        Room swamp, village, bridge, gates, circle;
        Room basement;
      
        // create the rooms
        busStop = new Room("standing at a bus stop");
        alley = new Room("wandering through an unusually dark alleyway");
        marketWest = new Room("in a strange marketplace. It seems to be dominated" +
            " by food services");
        marketSquare = new Room("in a strange marketplace. It seems to be dominated" +
            " by artisans");
        marketEast = new Room("in a strange marketplace. This corner of it is more" +
            " for some reason");
        inn = new Room("in a lively inn");
        stall = new Room("at a food stall serving something you don't recognize");
        maskShop = new Room("in a store that seems to specialize in quality" +
            " masquerade masks");
        dirtPath = new Room("on a dirt path winding through the city outskirts");
        trinket = new Room("in a small store selling knick-knacks");
        swamp = new Room("in a wooded wetland");
        village = new Room("in a humble village populated by large, gangly creatures");
        bridge = new Room("at one end of a long and foreboding bridge");
        gates = new Room("at the mighty gates of a towering castle");
        circle = new Room("in a forest clearing filled with mushrooms, some of" +
            " which form a ring.");
        basement = new Room("in a musty basement with a strange chalk" +
            " circle on the floor");
        
        // initialise room exits
        busStop.setExit("east", alley);
        alley.setExit("east", marketWest);
        marketWest.setExit("west", alley);
        marketWest.setExit("north", inn);
        marketWest.setExit("east", marketSquare);
        marketWest.setExit("south", stall);
        marketSquare.setExit("west", marketWest);
        marketSquare.setExit("south", maskShop);
        marketSquare.setExit("east", marketEast);
        marketEast.setExit("west", marketSquare);
        marketEast.setExit("north", trinket);
        marketEast.setExit("east", bridge);
        marketEast.setExit("south", dirtPath);
        inn.setExit("south", marketWest);
        inn.setExit("down", basement);
        stall.setExit("north", marketWest);
        maskShop.setExit("north", marketSquare);
        trinket.setExit("south", marketEast);
        dirtPath.setExit("north", marketEast);
        dirtPath.setExit("south", swamp);
        swamp.setExit("north", dirtPath);
        swamp.setExit("east", circle);
        swamp.setExit("west", village);
        village.setExit("east", swamp);
        bridge.setExit("west", marketEast);
        bridge.setExit("east", gates);
        gates.setExit("west", bridge);
        circle.setExit("west", swamp);
        circle.setExit("ring", basement);
        basement.setExit("circle", circle);
        basement.setExit("up", inn);
        
        currentRoom = busStop;  // start game at bus stop
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("    ~~ Welcome to <The World Beside Us!> ~~");
        System.out.println("Be curious, be cautious, be wise, and be brave.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;
                
            case LOOK:
                look();
                break;
                
            case EAT:
                eat(command);
                break;

            case QUIT:
                wantToQuit = quit(command);
                break;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("There's this dark alleyway you pass by on a");
        System.out.println("regular basis. For some reason you've always");
        System.out.println("felt drawn to it. Perhaps today is the day you");
        System.out.println("finally explore it. Perhaps it will change you.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     * @param Command The command to go to another room.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is nothing that way.");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }
    
    /**
     * Prints the longer version of the room description.
     */
    private void look() {
        System.out.println(currentRoom.getLongDescription());
    }
    
    /**
     * Try to eat something. If there is food present, consume it,
     * otherwise print an error message.
     */
    private void eat(Command command) {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to eat...
            System.out.println("Eat what?");
            return;
        }
        
        String food = command.getSecondWord();
        
        /* Once food is implemented it'll look something like this
        
        Item foodItem = inventory.getItem(food);
        
        if (foodItem == null) {
            System.out.println("That isn't here.");
        }
        else {
            System.out.println("You eat the " + food + ".");
        }
        
        */
        
        System.out.println("You eat the " + food + ".");
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    /**
     * The main method; it creates an instance of a game and initiates it.
     */
    public static void main(String[] args) {
        Game newGame = new Game();
        newGame.play();
    }
}
