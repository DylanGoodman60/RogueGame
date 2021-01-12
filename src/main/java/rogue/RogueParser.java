package rogue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RogueParser {

    private ArrayList<Map<String, String>> rooms = new ArrayList<>();
    private ArrayList<Map<String, String>> items = new ArrayList<>();
    private ArrayList<Map<String, String>> itemLocations = new ArrayList<>();
    private HashMap<String, Character> symbols = new HashMap<>();

    private Iterator<Map<String, String>> roomIterator;
    private Iterator<Map<String, String>> roomIterator2;
    private Iterator<Map<String, String>> itemIterator;

    private int numOfRooms = -1;
    private int numOfItems = -1;

    /**
     * Default constructor.
     */
    public RogueParser() {

    }

    /**
     * Constructor that takes filename and sets up the object.
     * @param filename  (String) name of file that contains file location for rooms and symbols
     */
    public RogueParser(String filename) {

        parse(filename);
    }

    /**
     * Return the next room.
     * @return (Map) Information about a room
     */
    public Map nextRoom() {

        if (roomIterator.hasNext()) {
            return roomIterator.next();
        } else {
            return null;
        }
    }

    /**
     * Return the next room.
     * @return (Map) Information about a room
     */
    public Map nextRoom2() {

        if (roomIterator2.hasNext()) {
            return roomIterator2.next();
        } else {
            return null;
        }
    }

    /**
     * Returns the next item.
     * @return (Map) Information about an item
     */
    public Map nextItem() {

        if (itemIterator.hasNext()) {
            return itemIterator.next();
        } else {
            return null;
        }

    }

    /**
     * Get the character for a symbol.
     * @param symbolName (String) Symbol Name
     * @return (Character) Display character for the symbol
     */
    public Character getSymbol(String symbolName) {

        if (symbols.containsKey(symbolName)) {
            return symbols.get(symbolName);
        }

        // Does not contain the key
        return null;
    }

    /**
     * return hashmap of symbols.
     * @return return symbols
     */
    public Map getSymbols() {
        return symbols;
    }

    /**
     * Get the number of items.
     * @return (int) Number of items
     */
    public int getNumOfItems() {

        return numOfItems;
    }

    /**
     * Get the number of rooms.
     * @return (int) Number of rooms
     */
    public int getNumOfRooms() {

        return numOfRooms;
    }

    /**
     * Read the file containing the file locations.
     * @param filename (String) Name of the file
     */
    private void parse(String filename) {
        JSONParser parser = new JSONParser();
        JSONObject roomsJSON = null;
        JSONObject symbolsJSON = null;
        try {
            Object obj = parser.parse(new FileReader(filename));
            JSONObject configurationJSON = (JSONObject) obj;
            String roomsFileLocation = (String) configurationJSON.get("Rooms");
            String symbolsFileLocation = (String) configurationJSON.get("Symbols");
            Object roomsObj = parser.parse(new FileReader(roomsFileLocation));
            Object symbolsObj = parser.parse(new FileReader(symbolsFileLocation));
            extractInfo(roomsJSON, symbolsJSON, roomsObj, symbolsObj);
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find file named: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("Error parsing JSON file");
        }
    }

    /**
     * extract json objs.
     * @param roomsJSON   room obj
     * @param symbolsJSON symbol obj
     * @param roomsObj  rooms obj2
     * @param symbolsObj rooms obj2
     */
    public void extractInfo(JSONObject roomsJSON, JSONObject symbolsJSON, Object roomsObj, Object symbolsObj) {
      roomsJSON = (JSONObject) roomsObj;
      symbolsJSON = (JSONObject) symbolsObj;
      extractRoomInfo(roomsJSON);
      extractItemInfo(roomsJSON);
      extractSymbolInfo(symbolsJSON);
      roomIterator = rooms.iterator();
      roomIterator2 = rooms.iterator();
      itemIterator = items.iterator();
    }

    /**
     * Get the symbol information.
     * @param symbolsJSON  (JSONObject) Contains information about the symbols
     */
    private void extractSymbolInfo(JSONObject symbolsJSON) {


        JSONArray symbolsJSONArray = (JSONArray) symbolsJSON.get("symbols");

        // Make an array list of room information as maps
        for (int i = 0; i < symbolsJSONArray.size(); i++) {
            JSONObject symbolObj = (JSONObject) symbolsJSONArray.get(i);
            symbols.put(symbolObj.get("name").toString(), String.valueOf(symbolObj.get("symbol")).charAt(0));
        }
    }

    /**
     * Get the room information.
     * @param roomsJSON (JSONObject) Contains information about the rooms
     */
    private void extractRoomInfo(JSONObject roomsJSON) {


        JSONArray roomsJSONArray = (JSONArray) roomsJSON.get("room");

        // Make an array list of room information as maps
        // Rooms is an arraylist of HashMaps with the info in it?
        for (int i = 0; i < roomsJSONArray.size(); i++) { //FOR EACH ROOM
            rooms.add(singleRoom((JSONObject) roomsJSONArray.get(i)));
            numOfRooms += 1;
        }
    }

    /**
     * Get a room's information.
     * @param roomJSON (JSONObject) Contains information about one room
     * @return (Map<String, String>) HashMap that has ALL room information
     */
    private Map<String, String> singleRoom(JSONObject roomJSON) {
        HashMap<String, String> room = new HashMap<>();
        room.put("id", roomJSON.get("id").toString());
        room.put("start", roomJSON.get("start").toString());
        room.put("height", roomJSON.get("height").toString());
        room.put("width", roomJSON.get("width").toString());
        defaultDoors(room);
        JSONArray doorArray = (JSONArray) roomJSON.get("doors");
        for (int j = 0; j < doorArray.size(); j++) { //FOR EACH DOOR
            JSONObject doorObj = (JSONObject) doorArray.get(j);
            String dir = String.valueOf(doorObj.get("dir"));
            room.put(dir, doorObj.get("wall_pos").toString() + "," + doorObj.get("con_room").toString());
        }
        JSONArray lootArray = (JSONArray) roomJSON.get("loot");
        for (int j = 0; j < lootArray.size(); j++) {
            itemLocations.add(itemPosition((JSONObject) lootArray.get(j), roomJSON.get("id").toString()));
        }
        return room;
    }

    /**
     * fill doors.
     * @param room hashinfo
     */
    public void defaultDoors(HashMap<String, String> room) {
      room.put("E", "-1");
      room.put("N", "-1");
      room.put("S", "-1");
      room.put("W", "-1");
    }

    /**
     * Create a map for information about an item in a room.
     * @param lootJSON (JSONObject) Loot key from the rooms file
     * @param roomID (String) Room id value
     * @return (Map<String, String>) Contains information about the item, where it is and what room
     */
    private Map<String, String>  itemPosition(JSONObject lootJSON, String roomID) {

        HashMap<String, String> loot = new HashMap<>();

        loot.put("room", roomID);
        loot.put("id", lootJSON.get("id").toString());
        loot.put("x", lootJSON.get("x").toString());
        loot.put("y", lootJSON.get("y").toString());

        return loot;
    }

    /**
     * Get the Item information from the Item key.
     * @param roomsJSON (JSONObject) The entire JSON file that contains keys for room and items
     */
    private void extractItemInfo(JSONObject roomsJSON) {

        JSONArray itemsJSONArray = (JSONArray) roomsJSON.get("items");

        for (int i = 0; i < itemsJSONArray.size(); i++) {
            items.add(singleItem((JSONObject) itemsJSONArray.get(i)));
            numOfItems += 1;
        }
    }

    /**
     * Get a single item from its JSON object.
     * @param itemsJSON (JSONObject) JSON version of an item
     * @return (Map<String, String>) Contains information about a single item
     */
    private Map<String, String>  singleItem(JSONObject itemsJSON) {
        HashMap<String, String> item = new HashMap<>();
        item.put("id", itemsJSON.get("id").toString());
        item.put("name", itemsJSON.get("name").toString());
        item.put("type", itemsJSON.get("type").toString());
        item.put("description", itemsJSON.get("description").toString());
        for (Map<String, String> itemLocation : itemLocations) {
            if (itemLocation.get("id").toString().equals(item.get("id").toString())) {
                item.put("room", itemLocation.get("room"));
                item.put("x", itemLocation.get("x"));
                item.put("y", itemLocation.get("y"));
                break;
            }
        }
        return item;
    }

}
