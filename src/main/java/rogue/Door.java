package rogue;
import java.util.ArrayList;
import java.io.Serializable;

public class Door implements Serializable {

    private static final long serialVersionUID = -3335100909640512698L;
    private String direction;
    private int wallPos;
    private String connectedRoomStr;

    private ArrayList<Room> connectedRooms = new ArrayList<>();
    /**
     * Might need to create a default door.
     */
    public Door() {
        direction = "";
        wallPos = 0;
        connectedRoomStr = "";
    }
    /**
     * Set direction.
     * @param dir direction
     */
    public void setDirection(String dir) {
        direction = dir;
    }

    /**
     * wall pos setter.
     * @param newWallPos Wallpos
     */
    public void setWallPos(int newWallPos) {
        wallPos = newWallPos;
    }

    /**
     * set con room str.
     * @param newStr connected room in a string
     */
    public void setConnectedRoomStr(String newStr) {
        connectedRoomStr = newStr;
    }

    /**
     * direction getter.
     * @return direction string
     */
    public String getDirection() {
        return direction;
    }
    /**
     * Id getter.
     * @return Id integer
     */
    public int getWallPos() {
        return wallPos;
    }

    /**
     * Get the string that contains the connected room.
     * Has to be here cuz of weird parsing.
     * @return connected room str
     */
    public String getConnectedRoomStr() {
        return connectedRoomStr;
    }

    /**
     * Add a connected room.
     * @param r The connected room of type Room
     */
    public void connectRoom(Room r) {
        connectedRooms.add(r);
    }

    /**
     * Connected Rooms getter.
     * @return Connected rooms arraylist
     */
    public ArrayList<Room> getConnectedRooms() {
        return connectedRooms;
    }

    /**
     * Get the room connected by the door but NOT passed in.
     * @param  currentRoom Room that you DONT want returned
     * @return             room that does not match
     */
    public Room getOtherRoom(Room currentRoom) {
        if (connectedRooms.size() == 2) {
            if (currentRoom.equals(connectedRooms.get(0))) {
                return connectedRooms.get(1);
            }
        }
        return connectedRooms.get(0);
    }
}
