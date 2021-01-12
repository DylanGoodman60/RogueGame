package rogue;

public class Ring extends Magic implements Wearable {
  /**
   * ring string getter.
   * @return LOTR
   */
  public String wear() {
    return getDescription();
  }

}
