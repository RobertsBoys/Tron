/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tron;


import java.awt.Color;
import java.util.ArrayList;
/**
 *
 * @author Erling
 */
public class ObliviousBike extends Bike 
{
    Dir temp; 
    public ObliviousBike(int x, int y,Dir setDir)
    {
        super(x,y,setDir);
        construct();
    }
    public ObliviousBike(Square setloc, Dir setDir)
    {
        super(setloc,setDir);
        construct();
    }
    private void construct()
    {
    }
    public String toString()
    {
        return "ObliviousBike "+id;
    }
    public String getWinPhrase()
    {
        return "Huh";
    }
    public String getSymbol()
    {
        return "OB";
    }
    public void move()
    {
//        if (Util.update()%4==1)
//            calcMap();
        if (nextPos().isTaken()) {
            temp = getCurDir();
            setCurDir(temp.rotate(true));
        }
    }
}
