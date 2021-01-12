package rogue;

public class Clothing extends Item implements Wearable {

/**
 * return wear description.
 * @return [description]
 */
  public String wear() {
    return getDescription();
  }

}
