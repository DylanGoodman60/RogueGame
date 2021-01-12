package rogue;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;
import java.awt.Point;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;

/**
* A room within the dungeon - contains monsters, treasure,
* doors out, etc.
*/
public class Room implements Serializable {

    private static final long serialVersionUID = -4323696733070050459L;
    private int id;
    private boolean start;
    private int height;
    private int width;
    private boolean isPlayer;

    private static final int TEMPNUM = 5;
    private static final int TEMPNUM2 = 6;
    private static final int TEMPNUM3 = 3;

    private HashMap<String, Door> doors = new HashMap<>();
    private ArrayList<Item> items = new ArrayList<Item>();
    private Map<String, Character> symbolInfo;

    private Player thePlayer;

    /**
     * Room blank constructor.
     */
    public Room() {
        id = 0;
        start = false;
        height = 0;
        width = 0;
        isPlayer = false;
    }


    // Required getter and setters below
    /**
     * width getter.
     * @return width int
     */
    public int getWidth() {
        return width;
    }

    /**
     * width setter.
     * @param newWidth int width of room?
     */
    public void setWidth(int newWidth) {
        width = newWidth;
    }

    /**
     * height getter.
     * @return height of room int
     */
    public int getHeight() {
        return height;
    }

    /**
     * height setter.
     * @param newHeight set room height
     */
    public void setHeight(int newHeight) {
        height = newHeight;
    }

    /**
     * Id getter.
     * @return int id of room
     */
    public int getId() {

        return id;
    }

    /**
     * Id setter.
     * @param newId Set id of room
     */
    public void setId(int newId) {

        id = newId;
    }

    /**
     * Add a door to hash map.
     * @param newDoor Door to be "put"
     */
    public void addDoor(Door newDoor) {
        if (newDoor != null) {
            doors.put(newDoor.getDirection(), newDoor);
        }
    }

    /**
     * Get array of room items.
     * @return Item arraylist
     */
    public ArrayList<Item> getRoomItems() {
        return items;
    }

    /**
     * Set room items arraylist.
     * @param newRoomItems Item arraylist of items
     */
    public void setRoomItems(ArrayList<Item> newRoomItems) {
        items = newRoomItems;
    }

    /**
     * Player getter.
     * @return Player type player
     */
    public Player getPlayer() {
        return thePlayer;

    }

    /**
     * Player setter.
     * @param newPlayer Player type player
     */
    public void setPlayer(Player newPlayer) {
        thePlayer = newPlayer;
    }
    /**
     * Door getter.
     * @param  direction dir of door
     * @return           [description]
     */
    public Door getDoor(String direction) {
        if (doors.containsKey(direction)) {
            return doors.get(direction);
        }
        return null;
    }

    /**
     * get the number of doors.
     * @return number of doors
     */
    public int numDoors() {
        return doors.size();
    }

    // /**
    //  * door setter.
    //  * @param direction door direction
    //  * @param location  door location
    //  */
    // public void setDoor(String direction, int location) {
    //     doors.add(new Door(direction, location));
    //     //System.out.println(direction + location);
    // }

    /**
     * Start setter.
     * @param newStart doesPlayerStartHere?
     */
    public void setStart(boolean newStart) {
        start = newStart;
    }

    /**
     * Start getter.
     * @return returns if player starts in room
     */
    public boolean getStart() {
        return start;
    }
    /**
     * bool set player in room.
     */
    public void setPlayerInRoom() {
        isPlayer = true;
    }

    /**
     * bool remove player from room.
     */
    public void removePlayerFromRoom() {
        isPlayer = false;
    }

    /**
     * Bool is player in room?
     * @return Bool
     */
    public boolean isPlayerInRoom() {
        return isPlayer;
    }

    /**
     * Remove an item.
     * @param theItem Item to remove
     */
    public void removeItem(Item theItem) {
            if (items.contains(theItem)) {
                items.remove(items.indexOf(theItem));
            }
    }
    /**
     * put Width into 2d char array.
     * @param arr char array
     */
    void putWidth(char[][] arr) {

        Character c = symbolInfo.get("NS_WALL");
        for (int i = 0; i < width; i++) {
            arr[0][i] = c;
            arr[height - 1][i] = c;

        }
    }

    /**
     * put walls into 2d char array.
     * @param arr room 2d array
     */
    void putWalls(char[][] arr) {

        Character c = symbolInfo.get("EW_WALL");
        for (int i = 0; i < height; i++) {
            arr[i][0] = c;
            arr[i][width - 1] = c;
        }
    }

    /**
     * put doors into 2d char array.
     * @param arr room 2d array
     */
     void putDoors1(char[][] arr) {
         int x = 0;
         int y = 0;
         if (doors.containsKey("N")) {
             x = 0;
             y = doors.get("N").getWallPos();
             arr[x][y] = symbolInfo.get("DOOR");
         }
         if (doors.containsKey("S")) {
             x = height - 1;
             y = doors.get("S").getWallPos();
             arr[x][y] = symbolInfo.get("DOOR");
         }
     }

     void putDoors2(char[][] arr) {
       int x = 0;
       int y = 0;
       if (doors.containsKey("E")) {
           x = doors.get("E").getWallPos();
           y = width - 1;
           arr[x][y] = symbolInfo.get("DOOR");
       }
       if (doors.containsKey("W")) {
           x = doors.get("W").getWallPos();
           y = 0;
           arr[x][y] = symbolInfo.get("DOOR");
       }
     }

    /**
     * put player icon in 2dArr.
     * @param arr 2d char array
     */
    void putPlayer(char[][] arr) {
        if (isPlayerInRoom()) {
            // arr[1][1] = symbols.get(TEMPNUM3).getSymbol(); //X and Y might be flipped
            Point p = thePlayer.getXyLocation();
            arr[(int) p.getY()][(int) p.getX()] = symbolInfo.get("PLAYER");
        //}
        }
    }

    /**
     * put items into 2d Array.
     * @param arr 2d char array
     */
    void putItems(char[][] arr) {
        for (Item item: items) {
            Character c = symbolInfo.get(item.getType().toUpperCase());
            arr[(int) item.getXyLocation().getY()][(int) item.getXyLocation().getX()] = c;
        }
    }

    /**
    * Produces a string that can be printed to produce an ascii rendering of the room and all of its contents.
    * @return (String) String representation of how the room looks
    */
    public String displayRoom() {

        char[][] displayArray = new char[getHeight()][getWidth()];
        putEntities(displayArray);
        //fillRemaining();
        String roomString = "";
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                if (displayArray[i][j] == '\0') { //if they got nothing
                    //displayArray[i][j] = symbols.get(2).getSymbol();
                    displayArray[i][j] = symbolInfo.get("FLOOR");
                }
                roomString += displayArray[i][j];
            }
            roomString += '\n';
        }
        return roomString;
    }

    /**
     * put entities.
     * @param arr displayArr
     */
    public void putEntities(char[][] arr) {
        putWalls(arr);
        putWidth(arr);
        putDoors1(arr);
        putDoors2(arr);
        putPlayer(arr);
        putItems(arr);
    }
    /**
     * Symbol hashmap setter.
     * @param symbols Hashmap of symbol information
     */
    public void setSymbols(Map symbols) {
        symbolInfo = symbols;
    }

    /**
     * Check the whole room for errors.
     * @return                         True if good, false if no bueno
     * @throws NotEnoughDoorsException throws e to fix doors if there are none
     */
    public boolean verifyRoom() throws NotEnoughDoorsException {
        Point p = null;
        int pX = 0;
        int pY = 0;
        if (!checkPlayer(p, pX, pY)) {
          return false;
        }
        for (Map.Entry<String, Door> door : doors.entrySet()) {
            if (door.getValue().getConnectedRooms().size() != 2) {
                throw new NotEnoughDoorsException();
            }
        }
        return true;
    }

    /**
     * check player and do itemss.
     * @param p  point p
     * @param pX  x coord
     * @param pY y coord
     * @return boolean sf
     */
    public boolean checkPlayer(Point p, int pX, int pY) {
      if (isPlayerInRoom()) {
          p = getPlayer().getXyLocation();
          pX = (int) p.getX();
          pY = (int) p.getY();
          if (pX < 1 || pX > (width - 2) || pY < 1 || pY > (height - 2)) {
              return false;
          }
      }
      for (Item item : items) {
          p = item.getXyLocation();
          pX = (int) p.getX();
          pY = (int) p.getY();

          if (pX < 1 || pX > (width - 2) || pY < 1 || pY > (height - 2)) {
              return false;
          }
      }
      return true;
    }

    /**
     * Add item to items array in rooms, check to make sure it is gucci.
     * @param  toAdd                       Item object being added
     * @throws ImpossiblePositionException Item in an invalid spot
     * @throws NoSuchItemException         Id does not match that of room?
     */
    public void addItem(Item toAdd) throws ImpossiblePositionException, NoSuchItemException {

        Point p;
        p = toAdd.getXyLocation();
        int pX = (int) p.getX();
        int pY = (int) p.getY();
        if (pX < 1 || pX > (width - 2) || pY < 1 || pY > (height - 2)) {
            throw new ImpossiblePositionException();
        } else if (!Rogue.itemExists(toAdd)) {
            System.out.println("Threw with " + toAdd.toString());
            throw new NoSuchItemException();
        } else {
            items.add(toAdd);
        }
    }
}
