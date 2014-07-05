/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tron;

/**
 *
 * @author Isaac
 */
public class PlayerBike extends Bike
{
    public PlayerBike(Square setLoc,Dir dir) {
        super(setLoc,dir);
    }
    public PlayerBike(int x,int y,Dir dir) {
        super(x,y,dir);
    }
    public void move()
    {
        move(getCurDir());
        //its moved in the driver- hackish i know
    }
    public String getSymbol() {
        return "P1";
    }
    public String getWinPhrase() {
        return "Humans win again!";
    }
    public String toString() {
        return "Player, ID="+id;
    }
}
