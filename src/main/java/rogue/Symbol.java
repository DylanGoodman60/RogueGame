package rogue;
//import java.util.ArrayList;
import org.json.simple.JSONObject;
//import java.awt.Point; might be important no idea rn

public class Symbol {

    private String name;
    private Character symbol;

    /**
     * Symbol empty constructor.
     */
    public Symbol() {

    }

    /**
     * Symbol constructor with JSON.
     * @param sJSON json obj
     */
    public Symbol(JSONObject sJSON) {


        setName((sJSON.get("name")).toString());
        String strSym = (sJSON.get("symbol")).toString();
        setSymbol(strSym.charAt(0));
        //setSymbol(((JSONs.get("symbol")).charAt(0)));
    }


    /**
     * set the name.
     * @param n String name
     */
    public void setName(String n) {
        name = n;
    }

    /**
     * get the name.
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * set the symbol.
     * @param s Character symbol
     */
    public void setSymbol(Character s) {
        symbol = s;
    }

    /**
     * get the symbol.
     * @return Character symbol
     */
    public Character getSymbol() {
        return symbol;
    }


}
