/**
 * Representations for all the valid command words for the game
 * along with a string in a particular language.
 * 
 *  base code written by Michael Kölling and David J. Barnes 2016.02.29
 *  
 *  @author Artemis MacDuffie
 *  @version 2023.03.27
 */
public enum CommandWord
{
    // A value for each command word along with its
    // corresponding user interface string.
    GO("go"), QUIT("quit"), HELP("help"), UNKNOWN("?"), LOOK("look"), EAT("eat");
    
    // The command string.
    private String commandString;
    
    /**
     * Initialise with the corresponding command string.
     * @param commandString The command string.
     */
    CommandWord(String commandString)
    {
        this.commandString = commandString;
    }
    
    /**
     * @return The command word as a string.
     */
    public String toString()
    {
        return commandString;
    }
}
