package rogue;

import java.io.Serializable;
import java.util.ArrayList;
import java.awt.Point;
/**
* The player character.
*/
public class Player implements Serializable {

    private static final long serialVersionUID = -5327031942035695797L;
    private String name;
    private Point p; //always at 0,0 for now.
    private Room currentRoom;
    private ArrayList<Item> items = new ArrayList<>();
    /**
     * empty constructor.
     */
    public Player() {
        name = "";
        p = new Point(0, 0);
    }

    /**
     * Player constructor with name.
     * @param newName String name of player
     */
    public Player(String newName) {
        setName(newName);
    }

    /**
     * name getter.
     * @return String name
     */
    public String getName() {

        return name;
    }

    /**
     * name setters.
     * @param newName String name
     */
    public void setName(String newName) {
        name = newName;
    }

    /**
     * Inventory getter.
     * @return Arraylist of items held
     */
    public ArrayList<Item> getItems() {
      return items;
    }

    /**
     * Add item to inventory.
     * @param toPickup Item to add
     */
    public void pickupItem(Item toPickup) {
      items.add(toPickup);
    }

    /**
     * remove item from inventory.
     * @param toRemove item to remove
     */
    public void removeInvItem(Item toRemove) {
      items.remove(items.indexOf(toRemove));
    }

    /**
     * xylocation getter.
     * @return point xylocation
     */
    public Point getXyLocation() {
        return p;
    }

    /**
     * xy location setter.
     * @param newXyLocation point xylocation
     */
    public void setXyLocation(Point newXyLocation) {
        p = newXyLocation;
    }

    /**
     * current room getter.
     * @return Room currentroom
     */
    public Room getCurrentRoom() {
        return currentRoom;

    }

    /**
     * current room setter.
     * @param newRoom Room new room to set
     */
    public void setCurrentRoom(Room newRoom) {
        currentRoom = newRoom;
    }
}
