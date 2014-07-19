/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tron;

/**
 *
 * @author Erling
 */
public class MapHugger extends Bike {
      int counter;
    int plan;
    public MapHugger(int x, int y,Dir setDir)
    {
        super(x,y,setDir);
        construct();
    }
    public MapHugger(Square setloc, Dir setDir)
    {
        super(setloc,setDir);
        construct();
    }
    private void construct()
    {
        counter = 0;
        plan = 0;
    }
    public String toString()
    {
        return "MapHugger "+id;
    }
    public String getWinPhrase()
    {
        return "How about a hug!";
    }
    public String getSymbol()
    {
        return "MH";
    }
    public void move()
    {
        //  read a 5x5 square in front (current square 3,0)
        //  find opponents
        //  if path to opponents is straight ahead, make a corridor
        //  look for corridor to avoid
        //  hug the right wall
            switch (plan) {
                case 0:
   //                 System.out.println("case is 0");
                    if (nextPos().isTaken()) {
                      setCurDir(getCurDir().rotate(true));
                      }
   //      Make a bump to trip up the other player.
                    if(counter++==10) {
                      counter = 0;
                      setCurDir(getCurDir().rotate(true));
                      plan=1;
                      }
                    break;
   //     case 1 - check left and turn left
                case 1:
   //                 System.out.println("case is 1");
                    if(getPos().transform(getCurDir().rotate(false)).isFree()) {
                        setCurDir(getCurDir().rotate(false));
                        plan=2;
                    } else if(nextPos().isTaken()) {
                        setCurDir(getCurDir().rotate(true));
                        plan = 0;
                    } else {
                        plan = 0;
                    } break;
   //     case 2 - check left again and turn left
            case 2:
   //                System.out.println("case is 2");
                    if(getPos().transform(getCurDir().rotate(false)).isFree()) {
                        setCurDir(getCurDir().rotate(false));
                        plan=3;
                    } else if(nextPos().isTaken()) {
                        setCurDir(getCurDir().rotate(true));
                        plan = 0;
                    } else {
                        plan = 0;
                    } break;
   //     case 3 - check right and turn right 
            case 3:
   //                System.out.println("case is 3");
                    if(getPos().transform(getCurDir().rotate(true)).isFree()) {
                        setCurDir(getCurDir().rotate(true));
                        plan=0;
                    } else if(nextPos().isTaken()) {
                        setCurDir(getCurDir().rotate(true));
                        plan = 0;
                    } else {
                        plan = 0;
                    } break;
            }
   }
}
