
## Class Door

| method sig | responsibility | instance vars used | other class methods called | objects used with method calls | lines of code |
|:----------:|:--------------:|:------------------:|:--------------------------:|:------------------------------:|:-------------:|
| setDirection() | width getter | width | none | none | 3 |
| setWallPos() | width setter | width | none | none | 3 |
| setConnectedRoomStr() | set room str |  String connectedRoomStr | none | none | 3 |
| getDirection() | get Direction | String direction | none | none | 3 |
| getWallPos() | get wall pos | int wallPos | int wallPos | none | 3 |
| getConnectedRoomStr() | get connected room str | String connectedRoomStr | none | none | 3
| connectRoom() | connect room to other room | ArrayList<Room> connectedRooms | none | none | 3 |
| getConnectedRooms() | get all rooms connected and itself | ArrayList<Room> connectedRooms | none | none | 3 |
| getOtherRoom() | get room connected to | ArrayList<Room> connectedRooms, Room currentRoom | none | none | 9 |
