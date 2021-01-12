
## Class Room

| method sig | responsibility | instance vars used | other class methods called | objects used with method calls | lines of code |
|:----------:|:--------------:|:------------------:|:--------------------------:|:------------------------------:|:-------------:|
| getWidth() | width getter | width | none | none | 3 |
| setWidth() | width setter | width | none | none | 3 |
| setHeight() | height setter | height | none | none | 3 |
| getHeight() | height getter | height | none | none | 3 |
| setId() | Id setter | Id | none || none | 3 |
| getId() | Id getter | Id | none | none | 3 |
| addDoor() | add door to room | Door doors | none | Door newDoor, Door newDoor | 5 |
| getRoomItems() | room items getter | ArrayList<Item> items | none | none | 3 |
| setRoomItems() | room items setter | ArrayList<Item> items |  none | none | 3 |
| getPlayer() | player getter | Player thePlayer | none | none | 3 |
| setPlayer() | player setter | Player thePlayer | none | none | 3 |
| getDoor() | door getter | HashMap<String, Door> doors | none | none | 6 |  none | none | 3 |
| numDoors() | return number of doors | HashMap<String, Door> doors | Door.size() | none | 3 |
| setStart() | set starting room | boolean start | none | none | 3 |
| getStart() | get starting room | boolean start | none | none | 3 |
| setPlayerInRoom() | Set the player in the room | boolean isPlayer | none | none | 3 |
| removePlayerFromRoom() | remove player from the room | boolean isPlayer | none | none | 3
| isPlayerInRoom() | boolean is the player in the room | boolean isPlayer | none | none | 6 |
| removeItem() | remove item from room | ArrayList<Item> items | none | none | 8 |
| putWalls() | put walls in char arr | char[][] arr | none | none | 8 |
| putDoors1() | put doors1 in char arr | char[][] arr | Doors.getWallPos() | none | 14 |
| putDoors2() | put doors2 in char arr | char[][] arr | Doors.getWallPos() | none | 14 |
| putPlayer() | put player in char arr | char[][] arr | isPlayerInRoom(), Player.getXyLocation
| putItems() | put items in char arr | char[][] arr | item.getType(), item.getXyLocation() | none | 6 |
| displayRoom() | display char arr | Map<String, Character> symbolInfo | getHeight(), getWidth(), putEntities() | char[][] displayArray | 18 |
| putEntities() | put entities | char[][] arr | putWalls(), putWidth(), putDoors1(), putDoors2(), putPlayer(), putItems() | char[][] arr | 8 |
| setSymbols() | set the symbols | Map<String, Character> symbolInfo | none | none | 3 |
| verifyRoom() | verify things in room | HashMap<String, Door> doors | checkPlayer(), entrySet(), getValue(), getConnectedRooms(), size() | Point p, int pX, int pY | 14 |
| checkPlayer() | check player location | int width, int height | getXyLocation(), getX(), getY(), isPlayerInRoom(), getPlayer() | none | 20 |
| addItem() | add item to room | int width, int height, ArrayList<Item> items | getXyLocation(), getX(), getY(), itemExists(), add() | Item toAdd | 16 |
