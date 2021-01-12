package rogue;

public class SmallFood extends Food implements Tossable {
  /**
   * toss string.
   * @return thrown!
   */
  public String toss() {
    if (!getDescP2().equals("")) {
      return getDescP2();
    }
    return "You toss the small piece of food.";
  }
}
