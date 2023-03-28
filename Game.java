import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

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
    private ArrayList<GameItem> inventory;
    private HashMap<Room, GameItem> itemLocations;
    private Stack<Room> priorRooms;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        parser = new Parser();
        inventory = new ArrayList<GameItem>();
        itemLocations = new HashMap<Room, GameItem>();
        priorRooms = new Stack<Room>();
        createRooms();
    }

    /**
     * Create all the rooms and link their exits together.
     * Also set "keys" (exits that can be blocked) and items.
     */
    private void createRooms()
    {
        Room busStop, alley, marketWest, marketSquare, marketEast;
        Room inn, stall, maskShop, trinket, dirtPath;
        Room swamp, village, bridge, gates, circle, basement;
      
        // create the rooms
        busStop = new Room("standing at a bus stop");
        alley = new Room("wandering through an unusually dark alleyway");
        marketWest = new Room("in a strange marketplace.\nIt seems to be dominated" +
            " by food services");
        marketSquare = new Room("in a strange marketplace.\nIt seems to be dominated" +
            " by artisans");
        marketEast = new Room("in a strange marketplace.\nThis corner of it is more" +
            " unoccupied for some reason");
        inn = new Room("in a lively inn");
        stall = new Room("at a food stall serving something you don't recognize");
        maskShop = new Room("in a store that seems to specialize\nin quality" +
            " masquerade masks");
        dirtPath = new Room("on a dirt path winding through the city outskirts");
        trinket = new Room("in a small store selling knick-knacks");
        swamp = new Room("in a wooded wetland");
        village = new Room("in a humble village populated by large, gangly creatures");
        bridge = new Room("at one end of a long and foreboding bridge");
        gates = new Room("at the mighty gates of a towering castle,\n" +
            "where armored guards with spears eye you with suspicion", true);
        circle = new Room("in a forest clearing filled with mushrooms,\nsome of" +
            " which form a ring.");
        basement = new Room("in a musty basement with a strange\nchalk" +
            " circle on the floor");
        
        // initialise room exits
        busStop.setExit("east", alley);
        alley.setExit("west", busStop);
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
        
        // set keys
        inn.setKey("down"); // you cannot go down to the basement
        //swamp.setKey("west"); // you cannot enter the village
        // The intent was that you needed to be wearing the mask to
        // enter the village, but I'm unsure how to implement that.
        
        // place items
        GameItem skewer = new GameItem("skewer", "a savory looking veggie " +
            "skewer,\nthough you cannot identify what plants it's made from.",
            "It's delicious, but for some reason\n" +
            "you immediately feel a sense of dread.", false);
        itemLocations.put(stall, skewer);
        GameItem mask = new GameItem("mask", "a beautiful mask, appropriate\n" +
            "for a masquerade ball.", "You put it on. It makes you feel regal.", false);
        itemLocations.put(maskShop, mask);
        GameItem jewel = new GameItem("jewel", "a splendid blue faceted strone.",
            "You show off the sparkling gem.", false);
        itemLocations.put(village, jewel);
        GameItem necklace = new GameItem("necklace", "a simply but charming " +
            "wooden bead necklace.", "You put it around your neck. It looks nice.", false);
        itemLocations.put(trinket, necklace);
        GameItem armoire = new GameItem("armoire", "an expertly made walnut armoire.",
            "You step into it and chill before getting bored.\nIt's really heavy so " +
            "you decide to leave it behind.", true);
        itemLocations.put(inn, armoire);
        
        // start game at bus stop
        currentRoom = busStop;
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
                System.out.println("Sorry, that's not a command.");
                System.out.println("Your command words are:");
                parser.showCommands();
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
                
            case TAKE:
                take(command);
                break;
            
            case EXAMINE:
                examine(command);
                break;
                
            case USE:
                use(command);
                break;
                
            case BACK:
                back();
                break;
                
            case DROP:
                drop(command);
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
     * @param command The command to go to another room.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord().toLowerCase();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is nothing that way.");
        }
        else {
            if (currentRoom.isLocked(direction)){
                System.out.println("Your way is blocked.");
            }
            else{
                priorRooms.add(currentRoom);
                currentRoom = nextRoom;
                System.out.println(currentRoom.getLongDescription());
                roomItemDescription();
            }
        }
    }
    
    /**
     * Prints the longer version of the room description.
     */
    private void look() {
        System.out.println(currentRoom.getLongDescription());
        roomItemDescription();
    }
    
    /**
     * Prints the description of the item in a room.
     */
    private void roomItemDescription() {
        if (itemLocations.get(currentRoom) != null) {
            System.out.print("There's something here: ");
            System.out.println(itemLocations.get(currentRoom).getDescription());
        }
    }
    
    /**
     * Take something in the room. If there is something, it will
     * be added to the inventory, otherwise print an error message.
     * 
     * @param command The command to take something.
     */
    private void take(Command command) {
        if(!command.hasSecondWord()) {
            System.out.println("Take what?");
            return;
        }
        
        String itemName = command.getSecondWord().toLowerCase();
        
        if (itemLocations.get(currentRoom) != null) {
            GameItem placeItem = itemLocations.get(currentRoom);
            
            if (placeItem.getName().equals(itemName)) {
                inventory.add(placeItem);
                itemLocations.remove(currentRoom);
                System.out.println("You took the " + itemName + ".");
            }
            else {
                System.out.println("That item isn't here.");
            }
        }
        else {
            System.out.println("There is nothing here to take.");
        }
    }
    
    /**
     * Look closely at the inventory or something in it,
     * otherwise print an error message.
     * 
     * @param command The command to examine something.
     */
    private void examine(Command command) {
        if(!command.hasSecondWord()) {
            System.out.println("Examine what?");
            return;
        }
        
        String itemName = command.getSecondWord().toLowerCase();
        
        if (itemName.equals("inventory")) {
            if (inventory.size() > 0) {
                System.out.println("You currently have:");
                for (GameItem inventoryItem : inventory) {
                    System.out.println("   > " + inventoryItem.getName());
                }
                return;
            }
            else {
                System.out.println("Your inventory is empty.");
                return;
            }
        }
        
        for (GameItem inventoryItem : inventory) {
            if (inventoryItem.getName().equals(itemName)) {
                System.out.println("It is " + inventoryItem.getDescription());
                return;
            }
        }
        
        System.out.println("That doesn't seem to be here.");
    }
    
    /**
     * Remove and discard an inventory item.
     * 
     * @param command The command to drop the item.
     */
    private void drop(Command command) {
        if(!command.hasSecondWord()) {
            System.out.println("Drop what?");
            return;
        }
        
        String itemName = command.getSecondWord().toLowerCase();
        
        for (GameItem inventoryItem : inventory) {
            if (inventoryItem.getName().equals(itemName)) {
                inventory.remove(inventoryItem);
                System.out.println("You drop the " + itemName + ".");
                return;
            }
        }
        
        System.out.println("You don't have any " + itemName + ".");
    }
    
    /**
     * Use an inventory item.
     * 
     * @param command The command to use the item.
     */
    private void use(Command command) {
        if(!command.hasSecondWord()) {
            System.out.println("Use what?");
            return;
        }
        
        String itemName = command.getSecondWord().toLowerCase();
        
        for (GameItem inventoryItem : inventory) {
            if (inventoryItem.getName().equals(itemName)) {
                /*
                *  I won't lie, I'm not sure how to implement this past this point.
                *  I know that as it is it's not good because it operates off the
                *  method's knowledge of the item name, and the entire premise of
                *  isFinalRoom feels very flawed.
                *  
                *  Because the rooms are not in a collection, I can't iterate
                *  through them. If I could, I could probably change the lock
                *  states that way, which is the point of the other two items.
                
                *  I could rewrite the rooms as a set so they can be iterated, and
                *  then make a hash map linking items to the places where they can
                *  be used, or places where they produce an effect.
                *  
                *  That's a lot of reworking though, and sadly, I'm out of
                *  time for this assignment, so I think this is it for now.
                */
                System.out.println(inventoryItem.getEffect());
                if (itemName.equals("jewel")) {
                    if (currentRoom.isFinalRoom()) {
                        System.out.println("The guards are in awe of the clarity " +
                            "of the stone.\nThey tell you that the lord of the castle" +
                            " will reward you\nhandsomely for it. They can tell " +
                            "you're not from here,\nand they say it's the only way " +
                            "you can return home\nat this point. After a moment to " +
                            "think on it,\nyou decide that it is indeed time to leave.");
                        
                        System.out.println("Thank you for playing.  Good bye.");
                        System.exit(0);
                    }
                    return;
                }
                inventory.remove(inventoryItem);
                return;
            }
        }
        System.out.println("You don't have any " + itemName + ".");
    }
    
    /**
     * Go to the previous room.
     */
    private void back() {
        if (!priorRooms.isEmpty()){
            currentRoom = priorRooms.pop();
            System.out.println(currentRoom.getLongDescription());
            roomItemDescription();
        }
        else {
            System.out.println("You are where you started.");
        }
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
