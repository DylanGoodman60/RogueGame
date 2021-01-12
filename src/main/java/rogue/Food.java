package rogue;

public class Food extends Item implements Edible {

  /**
   * eat string getter.
   * @return eat string
   */
  public String eat() {
    if (!getDescP1().equals("")) {
      return getDescP1();
    } else if (!getDescription().equals("")) {
      return getDescription();
    }
    return "You eat the food";
  }

}
