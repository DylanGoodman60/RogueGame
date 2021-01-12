package rogue;

public class Potion extends Magic implements Edible, Tossable {
/**
 * return eat string.
 * @return [YUM!]
 */
  public String eat() {
    if (!getDescP1().equals("")) {
    return getDescP1();
  } else if (!getDescription().equals("")) {
    return getDescription();
  }
    return "You drink the potion.";
  }
  /**
   * return toss string.
   * @return thrown!
   */
  public String toss() {

    if (!getDescP2().equals("")) {
      return getDescP2();
    }
    return "You toss the potion.";
  }
}
