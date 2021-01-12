package rogue;

import java.util.ArrayList;
// import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.io.Serializable;
// import java.io.FileNotFoundException;
// import java.io.FileReader;
// import java.io.IOException;

import java.awt.Point;

// import org.json.simple.JSONArray;
// import org.json.simple.JSONObject;
// import org.json.simple.parser.JSONParser;
// import org.json.simple.parser.ParseException;


public class Rogue implements Serializable {

  public static final char UP = 'i';
  public static final char DOWN = 'k';
  public static final char LEFT = 'j';
  public static final char RIGHT = 'l';
  public static final int YOFFSET = 3;
  private static final long serialVersionUID = -2675462794025570083L;
  private String nextDisplay;
  private transient RogueParser parser;
  private transient RogueParser parserCopy;
  private Player player;


  private ArrayList<Room> myRooms = new ArrayList<>();
  private static ArrayList<Item> items = new ArrayList<Item>();
  /**
  * Default constructor.
  */
  public Rogue() {
    nextDisplay = "";

  }
  /**
  * New game instance constructor.
  * @param theDungeonInfo RogueParser instance
  */
  public Rogue(RogueParser theDungeonInfo) {
    parser = theDungeonInfo;
    parserCopy = theDungeonInfo;
    Map roomInfo = parser.nextRoom();
    roomInfoParsing(roomInfo);
    try { //used to catch errors laterna hides
      Map itemInfo = parser.nextItem();
      while (itemInfo != null) {
        addItem(itemInfo);
        itemInfo = parser.nextItem();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    roomInfoParsing2();
  }

  /**
   * parse p1 of room info.
   * @param roomInfo   [room info hash]
   */
  public void roomInfoParsing(Map roomInfo) {
    while (roomInfo != null) {
      addRoom(roomInfo);
      roomInfo = parser.nextRoom();
    }
    Map roomInfo2 = parserCopy.nextRoom2();
    while (roomInfo2 != null) {
      try {
        addDoors(roomInfo2);
      } catch (Exception e) {
        e.printStackTrace();
      }
      roomInfo2 = parserCopy.nextRoom2();
    }
  }

  /**
   * supp function.
   */
  public void roomInfoParsing2() {
    for (Room r : myRooms) {
      r.setSymbols(parser.getSymbols());
      if (r.getStart()) {
        nextDisplay = r.displayRoom();
      }
      try {
        r.verifyRoom();
      } catch (NotEnoughDoorsException bruh) {
        if (!addRandomDoor(r)) {
          System.out.print("Dungeon file is not usable");
          System.exit(1);
        }
      }
    }
  }

  /**
  * add doors and connect them.
  * @param toAdd map which has door info
  */
  public void addDoors(Map<String, String> toAdd) {
    String[] cardinals = new String[] {"N", "E", "S", "W"};
    for (int i = 0; i < cardinals.length; i++) {
      if (toAdd.containsKey(cardinals[i])) {
        if (!(toAdd.get(cardinals[i]).equals("-1"))) {
          Door aNewDoor = new Door();
          aNewDoor.setDirection(cardinals[i]);
          String[] doorInfo = toAdd.get(cardinals[i]).split(",");
          aNewDoor.setWallPos(Integer.decode(doorInfo[0]));
          aNewDoor.connectRoom(myRooms.get(Integer.decode(doorInfo[1]) - 1));
          aNewDoor.connectRoom(myRooms.get(Integer.decode(toAdd.get("id")) - 1));
          myRooms.get(Integer.decode(toAdd.get("id")) - 1).addDoor(aNewDoor);
          //STILL NEED TO CONNECTED ROOMS at the end somehow
        }
      }
    }
  }

  /**
  * Adds a random door to a room that needs one.
  * @param theRoom [description]
  * @return whether the addition was succesful
  */
  public boolean addRandomDoor(Room theRoom) {
    String[] cardinals = new String[] {"N", "E", "S", "W"};
    Random rand = new Random();
    String dir = cardinals[rand.nextInt(cardinals.length)];
    Door aNewDoor = new Door();
    aNewDoor.setDirection(dir);
    if (dir == "N" || dir == "S") {
      aNewDoor.setWallPos(rand.nextInt(theRoom.getWidth()));
    } else {
      aNewDoor.setWallPos(rand.nextInt(theRoom.getHeight()));
    }
    aNewDoor.connectRoom(theRoom);
    for (Room room : myRooms) {
      if (room.numDoors() < cardinals.length) {
        placeRandomDoor(aNewDoor, room, theRoom);
        return true;
      }
    }
    return false;
  }

  /**
   * place Random door.
   * @param aNewDoor door to place
   * @param room     room to place door in
   * @param theRoom  coming from this room
   */
  public void placeRandomDoor(Door aNewDoor, Room room, Room theRoom) {
    aNewDoor.connectRoom(room);
    theRoom.addDoor(aNewDoor);
  }

  /**
  * Player setter.
  * @param thePlayer Player type player
  */
  public void setPlayer(Player thePlayer) {
    //thePlayer.setPosition(3,4);
    player = thePlayer;
    Point p = new Point(1, 1);
    player.setXyLocation(p);
    Room cRoom = new Room();
    for (Room r : myRooms) {
      if (r.getStart()) {
        cRoom = r;
      }
    }
    player.setCurrentRoom(cRoom);
    cRoom.setPlayer(thePlayer);
    cRoom.setPlayerInRoom();
  }

  /**
  * Playername setter.
  * @param str player name
  */
  public void setPlayerName(String str) {
    player.setName(str);
  }

  /**
  * Rooms arraylist getter.
  * @return Arraylist rooms
  */
  public ArrayList<Room> getRooms() {
    return myRooms;

  }

  /**
  * Items arraylist getter.
  * @return Items array list
  */
  public ArrayList<Item> getItems() {
    return items;

  }

  /**
  * Player inv getter.
  * @return Player inventory
  */
  public ArrayList<Item> getPlayerItems() {
    return player.getItems();
  }

  /**
  * Player getter.
  * @return Get Player type player
  */
  public Player getPlayer() {
    return player;

  }

  /**
  * see if move is valid, if so proceed (info below).
  * @param  input                Keyboard character input
  * @return                      nextDisplay?
  * @throws InvalidMoveException if move invalid throw this
  */
  public String makeMove(char input) throws InvalidMoveException {
    /* this method assesses a move to ensure it is valid.
    If the move is valid, then the display resulting from the move
    is calculated and set as the 'nextDisplay' (probably a private member variable)
    If the move is not valid, an InvalidMoveException is thrown
    and the nextDisplay is unchanged
    */
    if (!doorInteract(input)) {
      if (validPlayerMove(input)) {
        movePlayer(input);
      } else {
        // throw new InvalidMoveException("Invalid move! Can't walk into a wall :(");
        throw new InvalidMoveException("BONK - You walked into a wall, try again");
      }
    }
    nextDisplay = player.getCurrentRoom().displayRoom();
    return "That's a lovely move: " +  Character.toString(input);
  }

  /**
  * Checks if user is trying to use a door, sets player in other room.
  * @param  input user input
  * @return       true if user is changing rooms
  */
  public boolean doorInteract(char input) {
    Point playerLocation;
    playerLocation = player.getXyLocation();
    int pX = (int) playerLocation.getX();
    int pY = (int) playerLocation.getY();
    Room cRoom = player.getCurrentRoom();
    int height = cRoom.getHeight();
    int width = cRoom.getWidth();
    Point newLocation = null;
    if (interactUpperDoors(input, width, cRoom, playerLocation)) {
      return true;
    }
    if (interactLowerDoors(input, height, cRoom, playerLocation)) {
      return true;
    }
    return false;
  }

  /**
   * helper for interacting with uppers.
   * @param  input          key
   * @param  width         height room
   * @param  cRoom          current room
   * @param  playerLocation that
   * @return                whether they did
   */
  public boolean interactUpperDoors(char input, int width, Room cRoom, Point playerLocation) {
    int pX = (int) playerLocation.getX();
    int pY = (int) playerLocation.getY();
    Point newLocation = null;
    if (input == RIGHT && (pX == (width - 2))) {
      if (doorInteractEast(cRoom, pX, pY, newLocation)) {
        return true;
      }
    } else if (input == LEFT && (pX == (1))) {
      if (doorInteractWest(cRoom, pX, pY, newLocation)) {
        return true;
      }
    }
    return false;
  }

  /**
   * helper for interacting with lowrs.
   * @param  input          key
   * @param  height         height room
   * @param  cRoom          current room
   * @param  playerLocation that
   * @return                whether they did
   */
  public boolean interactLowerDoors(char input, int height, Room cRoom, Point playerLocation) {
    int pX = (int) playerLocation.getX();
    int pY = (int) playerLocation.getY();
    Point newLocation = null;
    if (input == DOWN && (pY == (height - 2))) {
      if (doorInteractSouth(cRoom, pX, pY, newLocation)) {
        return true;
      }
    } else
    if (input == UP && (pY == 1)) {
      if (doorInteractNorth(cRoom, pX, pY, newLocation)) {
        return true;
      }
    }
    return false;
  }
  /**
   * move into east door.
   * @param  cRoom [current room]
   * @param pX x
   * @param pY y
   * @param newLocation new location
   * @return       [whether it worked]
   */
  public boolean doorInteractEast(Room cRoom, int pX, int pY, Point newLocation) {
    if (cRoom.getDoor("E") != null) {
      if (cRoom.getDoor("E").getWallPos() == pY) {
        cRoom = cRoom.getDoor("E").getOtherRoom(cRoom);
        movePlayerToRoom(cRoom);
        newLocation = new Point(1, cRoom.getDoor("W").getWallPos());
        player.setXyLocation(newLocation);
        return true;
      }
    }
    return false;
  }

  /**
   * move into west door.
   * @param  cRoom [current room]
   * @param pX x
   * @param pY y
   * @param newLocation new location
   * @return       [whether it worked]
   */
  public boolean doorInteractWest(Room cRoom, int pX, int pY, Point newLocation) {
    if (cRoom.getDoor("W") != null) {
      if (cRoom.getDoor("W").getWallPos() == pY) {
        cRoom = cRoom.getDoor("W").getOtherRoom(cRoom);
        movePlayerToRoom(cRoom);
        newLocation = new Point(cRoom.getWidth() - 2, cRoom.getDoor("E").getWallPos());
        player.setXyLocation(newLocation);
        return true;
      }
    }
    return false;
  }
  /**
   * move into south door.
   * @param  cRoom [current room]
   * @param pX x
   * @param pY y
   * @param newLocation new location
   * @return       [whether it worked]
   */
  public boolean doorInteractSouth(Room cRoom, int pX, int pY, Point newLocation) {
    if (cRoom.getDoor("S") != null) {
      if (cRoom.getDoor("S").getWallPos() == pX) {
        cRoom = cRoom.getDoor("S").getOtherRoom(cRoom);
        movePlayerToRoom(cRoom);
        newLocation = new Point(cRoom.getDoor("N").getWallPos(), 1);
        player.setXyLocation(newLocation);
        return true;
      }
    }
    return false;
  }

/**
 * move into north door.
 * @param  cRoom [current room]
 * @param pX x
 * @param pY y
 * @param newLocation new location
 * @return       [whether it worked]
 */
public boolean doorInteractNorth(Room cRoom, int pX, int pY, Point newLocation) {
  if (cRoom.getDoor("N") != null) {
    if (cRoom.getDoor("N").getWallPos() == pX) {
      cRoom = cRoom.getDoor("N").getOtherRoom(cRoom);
      movePlayerToRoom(cRoom);
      newLocation = new Point(cRoom.getDoor("S").getWallPos(), cRoom.getHeight() - 2);
      player.setXyLocation(newLocation);
      return true;
    }
  }
  return false;
}
  /**
  * Move the player to a new room.
  * @param room room to move to
  */
  public void movePlayerToRoom(Room room) {
    player.setCurrentRoom(room);
    room.removePlayerFromRoom();
    player.getCurrentRoom().setPlayerInRoom();
    room.setPlayer(player);
    // Point p = new Point(1, 1); //currently lands in 1, 1
    // player.setXyLocation(p);
  }
  /**
  * actually move the player.
  * @param input where to move
  */
  public void movePlayer(char input) {
    Point plyr = player.getXyLocation();
    movePlayerDirection(plyr, input);
    try {
      checkItemCollision(plyr);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Move player in a direction.
   * @param p [point p]
   * @param input user input
   */
  public void movePlayerDirection(Point p, char input) {
    if (input == RIGHT) {
      p.setLocation(p.getX() + 1, p.getY());
      player.setXyLocation(p);
    }
    if (input == LEFT) {
      p.setLocation(p.getX() - 1, p.getY());
      player.setXyLocation(p);
    }
    if (input == UP) {
      p.setLocation(p.getX(), p.getY() - 1);
      player.setXyLocation(p);
    }
    if (input == DOWN) {
      p.setLocation(p.getX(), p.getY() + 1);
      player.setXyLocation(p);
    }
  }
  /**
  * Check if the player is on an item, if so, remove it.
  *
  * @param p position of the player
  */
  void checkItemCollision(Point p) {
    ArrayList<Item> toRemove = new ArrayList<>();
    for (Item item : items) {
      if (item.getCurrentRoom().equals(player.getCurrentRoom())) {
        if (item.getXyLocation().equals(p)) {
          player.pickupItem(item);
          item.getCurrentRoom().removeItem(item);
          toRemove.add(item);
          //Only does it within the room, not in this scopes items arraylist
        }
      }
    }
    for (Item item: toRemove) {
      items.remove(items.indexOf(item));
    }
  }

  /**
  * remove item.
  * @param anItem item to remove
  */
  public void dropItem(Item anItem) {
    player.removeInvItem(anItem);
    fixItem(anItem);
    try {
      anItem.setCurrentRoomId(player.getCurrentRoom().getId());
      anItem.setCurrentRoom(player.getCurrentRoom());
      addItemToRoom(anItem.getCurrentRoom().getId(), anItem);
    } catch (Exception e) {
      System.out.print("Cannot drop item");
    }
    anItem.setWorn(false);
  }

  /**
  * Change state of item worn.
  * @param theItem item being worn
  */
  public void wearItem(Item theItem) {
    theItem.setWorn(true);
  }

  /**
   * Eat an item.
   * @param theItem Item to eat
   */
  public void eatItem(Item theItem) {
    player.removeInvItem(theItem);
  }
  /**
  * Check if the player move is valid.
  * @param  input Move player is trying to make
  * @return       true if the move is valid
  */
  public boolean validPlayerMove(char input) {
    Point playerLocation;
    playerLocation = player.getXyLocation();
    int pX = (int) playerLocation.getX();
    int pY = (int) playerLocation.getY();
    Room cRoom = player.getCurrentRoom();
    int height = cRoom.getHeight();
    int width = cRoom.getWidth();
    if (input == RIGHT && (pX == (cRoom.getWidth() - 2))) {
      return false;
    } else if (input == LEFT && (pX == 1)) {
      return false;
    } else if (input == DOWN && (pY == (cRoom.getHeight() - 2))) {
      return false;
    } else if (input == UP && (pY == (1))) {
      return false;
    }
    return true;
  }
  /**
  * get next display.
  * @return String display
  */
  public String getNextDisplay() {


    return nextDisplay;
  }

  /**
  * Create new room.
  * @param toAdd HashMap with Room information
  */
  public void addRoom(Map<String, String> toAdd) {
    Room theRoom = new Room();
    int theId = Integer.decode((toAdd.get("id")));
    theRoom.setId(theId);
    String theStart = toAdd.get("start");
    if (theStart.equals("true")) {
      theRoom.setStart(true);
    } else {
      theRoom.setStart(false);
    }
    int theWidth = Integer.decode((toAdd.get("width")));
    theRoom.setWidth(theWidth);
    int theHeight = Integer.decode((toAdd.get("height")));
    theRoom.setHeight(theHeight);
    myRooms.add(theRoom);
  }

  /**
  * Add item.
  * @param toAdd HashMap with item information
  */
  public void addItem(Map<String, String> toAdd) {

    Item theItem = fillItem(toAdd);

    Point p = new Point();
    p.setLocation(Integer.decode(toAdd.get("x")), Integer.decode(toAdd.get("y")));
    theItem.setXyLocation(p);

    addItemToRoom(theItem.getCurrentRoomId(), theItem);
  }

  /**
  * Populate the item with its given information.
  * @param  toAdd The map with item information
  * @return       the item object once filled with info
  */
  public Item fillItem(Map<String, String> toAdd) {

    String theType = toAdd.get("type").toString();
    Item theItem = declareItems(theType);
    String theName = toAdd.get("name").toString();
    theItem.setName(theName);
    theItem.setType(theType);
    String theDescription = toAdd.get("description").toString();
    theItem.setDescription(theDescription);
    int theCurrentRoom = Integer.decode(toAdd.get("room"));
    theItem.setCurrentRoomId(theCurrentRoom);
    theItem.setCurrentRoom(myRooms.get(theCurrentRoom - 1));
    int theId = Integer.decode(toAdd.get("id"));
    theItem.setId(theId);
    theItem.parseDescriptions();

    return theItem;
  }
  /**
  * Add item to room, if possible.
  * @param theRoom Room to add item to
  * @param anItem  Item being added
  */
  public void addItemToRoom(int theRoom, Item anItem) {
    boolean itemPlaced = false;
    boolean noItem = false;
    while (!itemPlaced) {
      try {
        if (!noItem) {
          items.add(anItem); //this contains ALL the items for ALL the rooms
          myRooms.get(theRoom - 1).addItem(anItem);
        }
        itemPlaced = true;
      } catch (ImpossiblePositionException e) {
        if (!fixItem(anItem)) {
          System.out.println("There is no room for item: " + anItem.toString());
        }
      } catch (NoSuchItemException e) {
        myRooms.get(theRoom - 1).removeItem(anItem);
        noItem = true;
      }
    }
  }
  /**
  * places an incorrect item in an available position.
  * @param  theItem Item to be fixed
  * @return boolean if the item has been 'fixed'
  */
  public boolean fixItem(Item theItem) {

    Point checkPoint = new Point();
    for (int i = 1; i < theItem.getCurrentRoom().getWidth() - 1; i++) {
      for (int j = 1; j < theItem.getCurrentRoom().getHeight() - 1; j++) {
        checkPoint.setLocation(i, j);
        if (isPointEmpty(checkPoint)) {
          theItem.setXyLocation(checkPoint);
          return true;
        }
      }
    }
    return false;
  }

  /**
  * Give proper Item type.
  * @param  type String with Type
  * @return      Returns item type
  */
  public Item declareItems(String type) {
    Item anItem;
    switch (type) {
      case "Food": anItem = new Food();
      break;
      case "SmallFood": anItem = new SmallFood();
      break;
      case "Potion": anItem = new Potion();
      break;
      case "Clothing": anItem = new Clothing();
      break;
      case "Magic": anItem = new Magic();
      break;
      case "Ring": anItem = new Ring();
      break;
      default: anItem = new Item();
      break;
    }
    return anItem;
  }

  /**
  * Check if the position has either a player or item already in it.
  * @param  point to be checked
  * @return       true if point is empty
  */
  public boolean isPointEmpty(Point point) {


    for (Item item : items) {
      if (item.getXyLocation().equals(point)) {
        return false;
      }
    }
    if (player.getXyLocation().equals(point)) {
      return false;
    }
    return true;
  }

  /**
  * checks static arr list of items to see if it contains given item.
  * @param  it given item to check if exists in static arr list
  * @return    true on item exists
  */
  static boolean itemExists(Item it) {
    if (items.contains(it)) {
      return true;
    }
    return false;
  }
}
