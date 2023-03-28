
/**
 * Write a description of class GameItem here.
 *
 * @author Artemis MacDuffie
 * @version 2023.03.27
 */
public class GameItem
{
    private String name;
    private String description; //How the item's described when examined (full sentences)
    private String effect;      //How the item's described when used
    private boolean weight;     // false for light, true for heavy
    
    /**
     * Constructor for objects of class GameItem
     */
    public GameItem(String name, String description, String effect, boolean weight)
    {
        this.name = name;
        this.description = description;
        this.effect = effect;
        this.weight = weight;
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
     * Description getter
     * 
     * @return The location field
     */
    public String getDescription() {
        return (description + " It looks " + getWeight() + ".");
    }
    
    /**
     * Effect getter
     * 
     * @return The effect field
     */
    public String getEffect() {
        return effect;
    }
    
    /**
     * Returns a string describing the weight of the item.
     * 
     * @return "heavy" or "light"
     */
    private String getWeight() {
        if (weight) {
            return "heavy";
        }
        else {
            return "light";
        }
    }
}
