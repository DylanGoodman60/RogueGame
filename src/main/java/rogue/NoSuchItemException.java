package rogue;
public class NoSuchItemException extends Exception {

    /**
     * InvalidMoveException description.
     */
    public NoSuchItemException() {
        super();
    }

    /**
     * InvalidMoveException description.
     * @param message error message string
     */
    public NoSuchItemException(String message) {
        super(message);
    }

}
