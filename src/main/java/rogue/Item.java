package rogue;
import java.awt.Point;
import java.io.Serializable;

/**
 * A basic Item class; basic functionality for both consumables and equipment.
 */
public class Item implements Serializable {

    private static final long serialVersionUID = -6016544674652150116L;
    private String name;
    private Character displayCharacter;
    private int id;
    private String type;
    private Point p;
    private String description;
    private String descP1 = "";
    private String descP2 = "";
    private Room currentRoom;
    private int currentRoomId;
    private boolean isWorn = false;
    //Constructors

    /**
    * test case constructor if no information is given.
    */
    public Item() {
        name = "";
        displayCharacter = '*';
        id = 0;
        type = "";
        p = new Point(0, 0);
        description = "";
        currentRoomId = 0;
    }

    // Getters and setters

    /**
     * Id getter.
     * @return Id integer
     */
    public int getId() {
        return id;

    }

    /**
     * Set item worn status.
     * @param state true & false or huh?
     */
    public void setWorn(boolean state) {
      isWorn = state;
    }

    /**
     * return string whether worn or naw.
     * @return the "worn" string
     */
    public String wornString() {
      if (isWorn) {
        return " (equipped)";
      } else {
        return "";
      }
    }

    /**
     * Id setter.
     * @param newId Id integer
     */
    public void setId(int newId) {
        id = newId;
    }

    /**
     * name getter.
     * @return name string
     */
    public String getName() {
        return name;
    }

    /**
     * Name setter.
     * @param newName name string
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * Type getter.
     * @return type string
     */
    public String getType() {
        return type;

    }

    /**
     * type setter.
     * @param newType string type
     */
    public void setType(String newType) {
        type = newType;
    }

    /**
     * displayCharacter getter.
     * @return Character displayCharacter
     */
    public Character getDisplayCharacter() {
        return displayCharacter;

    }

    /**
     * displayCharacter setter.
     * @param newDisplayCharacter Character displayCharacter
     */
    public void setDisplayCharacter(Character newDisplayCharacter) {
        displayCharacter = newDisplayCharacter;
    }

    /**
     * Description getter.
     * @return description string
     */
    public String getDescription() {
        return description;

    }

    /**
     * description setter.
     * @param newDescription description string
     */
    public void setDescription(String newDescription) {

        description = newDescription;
    }


    /**
     * Split if theres two things.
     */
    public void parseDescriptions() {

      if (description.contains(":")) {
        String[] str = description.split(":");
        descP1 = str[0].trim();
        descP2 = str[1].trim();
      }
    }

    /**
     * get DescP1.
     * @return String
     */
    public String getDescP1() {
      return descP1;
    }

    /**
     * get DescP2.
     * @return String
     */
    public String getDescP2() {
      return descP2;
    }

    /**
     * xy location getter.
     * @return Point xy location
     */
    public Point getXyLocation() {
        return p;
    }

    /**
     * return String of both this.item & obj .
     * @param  o Object o
     * @return   both strings added
     */
    public String useItem(Object o) {

        String str = (this.toString() + o.toString());
        return str;
    }

    /**
     * Xy location setter.
     * @param newXyLocation Point xy location
     */
    public void setXyLocation(Point newXyLocation) {
        p = newXyLocation;
    }

    /**
     * Current room id getter.
     * @return cur room Id
     */
    public int getCurrentRoomId() {
        //This is of type room! Super smart
        return currentRoomId;

    }

    /**
     * Return the current room.
     * @return Room item is in
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * current room setter.
     * @param newCurrentRoom Room currentroom
     */
    public void setCurrentRoom(Room newCurrentRoom) {
        currentRoom = newCurrentRoom;
    }

    /**
     * Set current room id.
     * @param newCurrentRoomId cur room ID to be set
     */
    public void setCurrentRoomId(int newCurrentRoomId) {
        currentRoomId = newCurrentRoomId;
    }
}
