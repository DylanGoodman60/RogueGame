package rogue;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.TerminalPosition;

// import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Container;
import javax.swing.JPanel;
import javax.swing.JFileChooser;
// import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.BoxLayout;
import java.io.File;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.io.IOException;
// import java.lang.ClassNotFoundException;
import java.util.ArrayList;


import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;

public class WindowUI extends JFrame {


  private SwingTerminal terminal;
  private TerminalScreen screen;
  public static final int WIDTH = 700;
  public static final int HEIGHT = 800;
  // Screen buffer dimensions are different than terminal dimensions
  public static final int COLS = 80;
  public static final int ROWS = 24;
  private final char startCol = 0;
  private final char msgRow = 1;
  private final char roomRow = 3;

  private JPanel invPanel;
  private Container invPane;
  private JLabel playerName;
  private JLabel constantText;

  private boolean canToss = false;
  private boolean canWear = false;
  private boolean canEat = false;

  private Rogue rogueGameToSave;
  private Rogue theGame;

  /**
  Constructor.
  **/

  public WindowUI() {
    super();
    setWindowDefaults(getContentPane());
    setUpPanels(getContentPane());
    setTerminal(getContentPane());
    makeMenuBar();
    pack();
    start();
    //  setUpInputArea(getContentPane());

  }
  private void setUpPanels(Container contentPane) {
    JPanel labelPanel = new JPanel();
    JPanel namePanel = new JPanel();
    //setUpTextPanel(textPanel, contentPane);
    setUpInvPanel(labelPanel, contentPane);
    setUpNamePanel(namePanel, contentPane);
  }
  private void setUpInvPanel(JPanel thePanel, Container contentPane) {
    invPanel = thePanel;
    invPane = contentPane;

    BoxLayout boxlayout = new BoxLayout(invPanel, BoxLayout.Y_AXIS);
    displayInventoryHeader();


    contentPane.add(invPanel, BorderLayout.EAST);
  }
  private void setUpNamePanel(JPanel thePanel, Container contentPane) {
    playerName = new JLabel("Judi's Rogue game");
    thePanel.add(playerName);
    contentPane.add(thePanel, BorderLayout.NORTH);
  }
  private void makeMenuBar() {
    JMenuBar menubar = new JMenuBar();
    setJMenuBar(menubar);
    Border fileLine = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
    menubar.setBorder(fileLine);
    JMenu fileMenu = new JMenu("File");
    menubar.add(fileMenu);

  }

  /**
   * Fill menu bar with dropdown.
   * @param fileMenu the JMenu obj
   */
  public void fillMenuBar(JMenu fileMenu) {
    JMenuItem save = new JMenuItem("Save");
    save.addActionListener(sev -> saveGame());
    fileMenu.add(save);
    JMenuItem name = new JMenuItem("Change name");
    name.addActionListener(ev -> changePlayerName());
    fileMenu.add(name);
    JMenuItem openSave = new JMenuItem("Load Save");
    openSave.addActionListener(sev -> loadGame());
    fileMenu.add(openSave);
    JMenuItem openItem = new JMenuItem("Load JSON");
    openItem.addActionListener(jev -> newJSON());
    fileMenu.add(openItem);
    JMenuItem quitItem = new JMenuItem("Quit");
    quitItem.addActionListener(qev -> quit());
    fileMenu.add(quitItem);
  }
  private void changePlayerName() {

    String name = JOptionPane.showInputDialog("What is the new name?");
    if (name.equals("")) {
      name = "Guy who thinks he\'s funny";
    }
    playerName.setText(name + "\'s Rogue Game");
  }
  // private void setUpInputArea(Container contentPane) {
  //   JPanel labelPanel = new JPanel();
  //   //JPanel textPanel = new JPanel();
  //   JLabel exampleLabel = new JLabel("Today");
  //   labelPanel.add(exampleLabel);
  //   //textPanel.add(new JLabel("Input area"));
  //   contentPane.add(labelPanel);
  //   //contentPane.add(textPanel);
  // }

  private void setWindowDefaults(Container contentPane) {
    setTitle("Rogue!");
    setSize(WIDTH, HEIGHT);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    contentPane.setLayout(new BorderLayout());

  }

  private void setTerminal(Container contentPane) {
    terminal = new SwingTerminal();
    Border fileLine = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
    terminal.setBorder(fileLine);
    contentPane.add(terminal, BorderLayout.CENTER);
  }

  private void start() {
    try {
      screen = new TerminalScreen(terminal);
      //screen = new VirtualScreen(baseScreen);
      screen.setCursorPosition(TerminalPosition.TOP_LEFT_CORNER);
      screen.startScreen();
      screen.refresh();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }



  /**
  Prints a string to the screen starting at the indicated column and row.
  @param toDisplay the string to be printed
  @param column the column in which to start the display
  @param row the row in which to start the display
  **/
  public void putString(String toDisplay, int column, int row) {

    Terminal t = screen.getTerminal();
    try {
      t.setCursorPosition(column, row);
      for (char ch: toDisplay.toCharArray()) {
        t.putCharacter(ch);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
  Changes the message at the top of the screen for the user.
  @param msg the message to be displayed
  **/
  public void setMessage(String msg) {
    putString("                                                ", 1, 1);
    putString(msg, startCol, msgRow);
  }

  /**
  Redraws the whole screen including the room and the message.
  @param message the message to be displayed at the top of the room
  @param room the room map to be drawn
  **/
  public void draw(String message, String room) {

    try {
      screen.getTerminal().clearScreen();
      setMessage(message);
      putString(room, startCol, roomRow);
      screen.refresh();
    } catch (IOException e) {

    }

  }

  /**
  Obtains input from the user and returns it as a char.  Converts arrow
  keys to the equivalent movement keys in rogue.
  @return the ascii value of the key pressed by the user
  **/
  public char getInput() {
    KeyStroke keyStroke = null;
    char returnChar;
    keyStroke = getKeyStroke();
    if (keyStroke.getKeyType() == KeyType.ArrowDown) {
      returnChar = Rogue.DOWN;  //constant defined in rogue
    } else if (keyStroke.getKeyType() == KeyType.ArrowUp) {
      returnChar = Rogue.UP;
    } else if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
      returnChar = Rogue.LEFT;
    } else if (keyStroke.getKeyType() == KeyType.ArrowRight) {
      returnChar = Rogue.RIGHT;
    } else {
      returnChar = keyStroke.getCharacter();
    }
    return returnChar;
  }

  /**
   * get a keystroke.
   * @return valid keystroke
   */
  public KeyStroke getKeyStroke() {
    KeyStroke keyStroke = null;
    while (keyStroke == null) {
      try {
        keyStroke = screen.pollInput();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return keyStroke;
  }

  /**
  * Update the inventory to the right side bar.
  * @param game instance of rogue game
  * @param items Arraylist of items the player has
  */
  public void updateInv(Rogue game, ArrayList<Item> items) {
    invPanel.removeAll();
    displayInventoryHeader();

    for (Item it: items) {
      JButton item1 = new JButton(it.getName() + it.wornString());
      item1.addActionListener(event -> interactItem(game, it));
      invPanel.add(item1);
    }
    invPane.add(invPanel, BorderLayout.EAST);
    validate();
  }

  /**
  * add header "Inventory".
  */
  public void displayInventoryHeader() {
    BoxLayout boxlayout = new BoxLayout(invPanel, BoxLayout.Y_AXIS);
    invPanel.setLayout(boxlayout);
    Border line = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
    invPanel.setBorder(line);
    JLabel invLabel = new JLabel("Inventory");
    invPanel.add(invLabel);
  }

  /**
  * Drop item, remove from player inventory and place back.
  * remove from the invPanel.
  * @param game the game
  * @param theItem item to drop
  */
  public void interactItem(Rogue game, Item theItem) {
    if (canToss) {
      if (!tossSmallFood(game, theItem)) {
        if (!tossPotion(game, theItem)) {
          JOptionPane.showMessageDialog(null, "Cannot toss this item");
        }
      }
    } else if (canWear) {
      if (!wearClothing(game, theItem)) {
        if (!wearRing(game, theItem)) {
          JOptionPane.showMessageDialog(null, "Cannot wear this item");
        }
      }
    } else if (canEat) {
      if (!tossableAttempt(game, theItem)) {
          JOptionPane.showMessageDialog(null, "Cannot eat this item");
        }
      }
    }
  /**
   * attempt to toss.
   * @param game    [rogue instance]
   * @param theItem [item to toss]
   * @return whether it was tossed
   */
  public boolean tossableAttempt(Rogue game, Item theItem) {
    if (eatFood(game, theItem)) {
      if (eatPotion(game, theItem)) {
        return true;
      }
    }
    return false;
  }

  /**
  * Eat food.
  * @param  game    game instance
  * @param  theItem item to eat
  * @return         Whether the item was eaten or not
  */
  public boolean eatPotion(Rogue game, Item theItem) {
    if (theItem.getType().equals("Potion") && canEat) {
      game.eatItem(theItem);
      Potion pot = (Potion) theItem;
      JOptionPane.showMessageDialog(null, pot.eat());
      canToss = false;
      canWear = false;
      canEat = false;
      return true;
    }
    return false;
  }

  /**
  * Eat food.
  * @param  game    game instance
  * @param  theItem item to eat
  * @return         Whether the item was eaten or not
  */
  public boolean eatFood(Rogue game, Item theItem) {
    if (theItem.getType().equals("Food") && canEat) {
      game.eatItem(theItem);
      Food food = (Food) theItem;
      JOptionPane.showMessageDialog(null, food.eat());
      canToss = false;
      canWear = false;
      canEat = false;
      return true;
    }
    return false;
  }

  /**
  * Eat small food.
  * @param  game    game instance
  * @param  theItem item to eat
  * @return         Whether the item was eaten or not
  */
  public boolean eatSmallFood(Rogue game, Item theItem) {
    if (theItem.getType().equals("SmallFood") && canEat) {
      game.eatItem(theItem);
      SmallFood food = (SmallFood) theItem;
      JOptionPane.showMessageDialog(null, food.eat());
      canToss = false;
      canWear = false;
      canEat = false;
      return true;
    }
    return false;
  }

  /**
  * Toss/eat small food.
  * @param  game    Game instance
  * @param  theItem the SmallFood item
  * @return         Whether it was possible or not
  */
  public boolean tossSmallFood(Rogue game, Item theItem) {
    if (theItem.getType().equals("SmallFood") && canToss) {
      game.dropItem(theItem);
      SmallFood food = (SmallFood) theItem;
      JOptionPane.showMessageDialog(null, food.toss());
      canToss = false;
      canWear = false;
      canEat = false;
      return true;
    }
    return false;
  }

  /**
  * drink potion.
  * @param  game    Game instance
  * @param  theItem the SmallFood item
  * @return         Whether it was possible or not
  */
  public boolean tossPotion(Rogue game, Item theItem) {
    if (theItem.getType().equals("Potion") && canToss) {
      game.dropItem(theItem);
      Potion pot = (Potion) theItem;
      JOptionPane.showMessageDialog(null, pot.toss());
      canToss = false;
      canWear = false;
      canEat = false;
      return true;
    }
    return false;
  }

  /**
  * Wear the clothing attempt.
  * @param  game    game instance
  * @param  theItem clothing to be worn
  * @return         whether it was succesfully worn or not
  */
  public boolean wearClothing(Rogue game, Item theItem) {
    if (theItem.getType().equals("Clothing") && canWear) {
      game.wearItem(theItem);
      Clothing cloth = (Clothing) theItem;
      JOptionPane.showMessageDialog(null, cloth.wear());
      canToss = false;
      canWear = false;
      canEat = false;
      return true;
    }
    return false;
  }

  /**
  * Wear the ring attempt.
  * @param  game    game instance
  * @param  theItem ring to be worn
  * @return         whether it was succesfully worn or not
  */
  public boolean wearRing(Rogue game, Item theItem) {
    if (theItem.getType().equals("Ring") && canWear) {
      game.wearItem(theItem);
      Ring ring = (Ring) theItem;
      JOptionPane.showMessageDialog(null, ring.wear());
      canToss = false;
      canWear = false;
      canEat = false;
      return true;
    }
    return false;
  }

  /**
  * if user hits T, toss an item.
  * @param input the input from the user
  * @return boolean whether toss was made or not
  */
  public boolean attemptToss(Character input) {
    if (input == 'T' || input == 't') {
      JOptionPane.showMessageDialog(null, "Select an item to toss");
      canToss = true;
      canWear = false;
      canEat = false;
      return true;
    }
    return false;
  }

  /**
  * If user hits w, wear an item.
  * @param  input user input
  * @return       whether it worked or nah
  */
  public boolean attemptWear(Character input) {
    if (input == 'W' || input == 'w') {
      JOptionPane.showMessageDialog(null, "Select an item to wear");
      canWear = true;
      canToss = false;
      canEat = false;
      return true;
    }
    return false;
  }

  /**
  * If user hits 'e', eat state init.
  * @param  input user Input
  * @return whether item was eaten or bruh
  */
  public boolean attemptEat(Character input) {
    if (input == 'E' || input == 'e') {
      JOptionPane.showMessageDialog(null, "Select an item to eat");
      canEat = true;
      canWear = false;
      canToss = false;
      return true;
    }
    return false;
  }

  /**
  * Quit the program.
  */
  public void quit() {
    System.out.println("Thanks for playing!");
    System.exit(1);
  }

  /**
  * deserialization of Game.
  */
  public void loadGame() {
    String filename = getFileNameBrowser();
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename)); ) {

      theGame = (Rogue) in.readObject();

    } catch (IOException e) {
      System.out.println("IOException caught: " + e);
    } catch (ClassNotFoundException ex) {
      System.out.println("ClassNotFoundException  caught: " + ex);
    }
  }

  /**
  * serialization of Game.
  */
  public void saveGame() {

    String filename = getFileNameBrowser();
    try {
      FileOutputStream outPutStream = new FileOutputStream(filename);
      ObjectOutputStream outPutDest = new ObjectOutputStream(outPutStream);

      outPutDest.writeObject(rogueGameToSave);
      outPutDest.close();
      outPutStream.close();
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  /**
  * Load new JSON file.
  */
  public void newJSON() {

    String configurationFileLocation = getFileNameBrowser();
    //Parse the json files
    RogueParser parser = new RogueParser(configurationFileLocation);
    // allocate memory for the game and set it up
    theGame = new Rogue(parser);
    rogueGameToSave = theGame;
  }

  /**
  * get a valid filename.
  * @return the valid filename once gotten
  */
  public String getFileName() {
    boolean validName = false;
    String name = "";
    while (!validName) {
      name = JOptionPane.showInputDialog("What is the name of the file?");
      if (name.length() != 0) {
        if (name.contains(".") && name.length() > (2 + 1)) {
          validName = true;
        } else {
          name = JOptionPane.showInputDialog("Please enter a VALID file name");
        }
      }
    }
    return name;
  }

  /**
  * get a valid filename.
  * @return the valid filename once gotten
  */
  public String getFileNameBrowser() {
    boolean foundFile = false;
    while (!foundFile) {
      JFileChooser fileBrowser = new JFileChooser();
      int res = fileBrowser.showOpenDialog(invPane);
      if (res == JFileChooser.APPROVE_OPTION) {
        File chosenFile = fileBrowser.getSelectedFile();
        return chosenFile.getName();
      } else {
        System.out.println("Please pick valid file");
      }
    }
    return "";
  }
  /**
  The controller method for making the game logic work.
  @param args command line parameters
  **/
  public static void main(String[] args) {

    char userInput = 'h';
    String message;
    String configurationFileLocation = "fileLocations.json";
    RogueParser parser = new RogueParser(configurationFileLocation);
    WindowUI theGameUI = new WindowUI();
    theGameUI.theGame = new Rogue(parser);
    theGameUI.rogueGameToSave = theGameUI.theGame;
    Player thePlayer = new Player("Judi");
    theGameUI.theGame.setPlayer(thePlayer);
    message = "Welcome to my Rogue game";
    theGameUI.draw(message, theGameUI.theGame.getNextDisplay());
    theGameUI.setVisible(true);

    theGameUI.runGame(userInput, message, theGameUI);
  }

  /**
   * run active part of game.
   * @param userInput input of user
   * @param message   c message
   * @param theGameUI Window UI
   */
  public void runGame(char userInput, String message, WindowUI theGameUI) {
    while (userInput != 'q') {
      userInput = theGameUI.getInput();
      try {
        if (theGameUI.attemptToss(userInput) || theGameUI.attemptWear(userInput) || theGameUI.attemptEat(userInput)) {
          theGameUI.updateInv(theGameUI.theGame, theGameUI.theGame.getPlayerItems());
          theGameUI.draw(message, theGameUI.theGame.getNextDisplay());
        } else {
          message = theGameUI.theGame.makeMove(userInput);
          theGameUI.updateInv(theGameUI.theGame, theGameUI.theGame.getPlayerItems());
          theGameUI.draw(message, theGameUI.theGame.getNextDisplay());
        }
      } catch (InvalidMoveException badMove) {
        message = "I didn't understand what you meant, please enter a command";
        theGameUI.setMessage(message);
      }
    }
  }
}
