
/**
 * Write a description of class Inventory here.
 *
 * @author Artemis MacDuffie
 * @version 2023.03.27
 */
public class GameItem
{
    // instance variables - replace the example below with your own
    private String name;
    private String location;
    private String description;

    /**
     * Constructor for objects of class Inventory
     */
    public GameItem(String name, String location, String description)
    {
        this.name = name;
        this.location = location;
        this.description = description;
    }

    /**
     * Name getter
     *
     * @return The name field
     */
    public String getName() {
        return name;
    }
    
    /**
     * Location getter
     * 
     * @return The location field
     */
    public String getLocation() {
        return location;
    }
    
    /**
     * Description getter
     * 
     * @return The location field
     */
    public String getDescription() {
        return description;
    }
}
