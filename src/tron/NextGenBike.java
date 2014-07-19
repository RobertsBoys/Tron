/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tron;

/**
 *
 * @author Erling
 */
public class NextGenBike extends Bike {
    Dir temp; 
    int counter;
    int plan;
    public NextGenBike(int x, int y,Dir setDir)
    {
        super(x,y,setDir);
        construct();
    }
    public NextGenBike(Square setloc, Dir setDir)
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
        return "NextGenBike "+id;
    }
    public String getWinPhrase()
    {
        return "Next Rocks!";
    }
    public String getSymbol()
    {
        return "NG";
    }
    public void move()
    {
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

