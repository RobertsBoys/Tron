/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tron;

/**
 *
 * @author Isaac
 */
public class Tourist extends Bike {
    public Tourist(int setx,int sety,Dir setDir) {
        super(setx,sety,setDir);
    }
    public Tourist(Square setLoc,Dir setDir) {
        super(setLoc,setDir);
    }
    public void move()
    {
        Dir dir=Dir.getRandDir();
        for (int n=0;n<4;n++)
        {
            if (getPos().transform(dir).isFree())
            {
                move(dir);
                return;
            }
            dir=dir.rotate(true);
            
        }
    }
    public String getSymbol()
    {
        return "t";
    }
    public String getWinPhrase() {
        return "yay i won";
    }
    public String toString() {
        return "Tourist "+id;
    }
}
