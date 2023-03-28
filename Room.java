import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "The World Beside Us" application. 
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 *  base code written by Michael Kölling and David J. Barnes 2016.02.29
 *  
 *  @author Artemis MacDuffie
 *  @version 2023.03.27
 */

public class Room 
{
    private String description;
    private HashMap<String, Room> exits;    // stores exits of this room.
    
    // stores keys for moving between rooms as <direction, lock state>
    // lock state: false is unlocked, true is locked
    private HashMap<String, Boolean> keys;      
    
    private boolean finalExit;  // whether or not this is the last room
                                                
    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * 
     * It is assumed that a given room is NOT the final room.
     * 
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<String, Room>();
        keys = new HashMap<String, Boolean>();
        finalExit = false;
    }
    
    /**
     * Alternate constructor. Assigning true to finalExit
     * allows this room to be marked as the final room.
     */
    public Room(String description, boolean exit) 
    {
        this.description = description;
        exits = new HashMap<String, Room>();
        keys = new HashMap<String, Boolean>();
        finalExit = exit;
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }

    /**
     * Define a direction as needing a key.
     * @param direction The direction of the exit.
     * @param key       The key for that exit.
     */
    public void setKey(String direction){
        keys.put(direction, true);
    }
    
    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        return "You are " + description + ".\n" + getExitString();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> localExits = exits.keySet();
        for(String exit : localExits) {
            returnString += " " + exit;
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
    
    /**
     * Return whether or not this room is flagged as the final room.
     * @return True if it's flagged, otherwise return false.
     */
    public boolean isFinalRoom() {
        if (finalExit) {
            return true;
        }
        return false;
    }
    
    /**
     * 
     */
    public boolean isLocked(String direction) {
        if (keys.get(direction) == null) {
            return false;
        }
        return keys.get(direction);
    }
}

