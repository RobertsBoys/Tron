/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tron;

import java.awt.*;
import java.util.*;
import java.util.ArrayList;
import static tron.Square.getSquare;

/**
 *      Strategy is the map outside the 5x5
 *      Tactical is the map inside the 5x5
 *      Decide the strategy which is where to exit the 5x5
 *      Decide the tactical inside the 5x5
 *      Select some basic moves and create functions
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
       int free = 0;
       int firstMove;
       int heading=2; // 2=up, 1=left, 3=right, 0=down
       ArrayList <Path> possible = new ArrayList();
       boolean moveForward = false;
       boolean moveRight = false;
       boolean moveLeft = false;
     //  read a 5x5 square in front (current square 3,0)
       Reader[][] tactical= new Reader[5][5];
      // =======================================
 //Determine direction so you can get 5 ahead and 2 to each side
        Dir cdir = getCurDir();
        Square loc = getPos();
   /** I need a way to number the squares relative to the direction.
   * I want square #1 to be to the right and then number in a consistent manner.
   * North(0,-1,0),
   * East(1,0,1),
   * South(0,1,2),
   * West(-1,0,3),
   * 
   *  a,b  4,0 4,1 4,2 4,3 4,4
   *       3,0 3,1 3,2 3,3 3,4
   *       2,0 2,1 2,2 2,3 2,4
   *       1,0 1,1 1,2 1,3 1,4
   *       0,0 0,1 0,2 0,3 0,4 
   * fill the above grid with the current location in 0,2.
   * The grid is always pointing forward.
     */
        int x;
        int y;
        Square point;
        for (int a=0; a<5; a++)
        {
            for (int b=0; b<5; b++)
            {
            x=loc.getX()+a*cdir.x()+(2-b)*cdir.y();
            y=loc.getY()+a*cdir.y()+(b-2)*cdir.x();                
  //        System.out.println("so far so good tactical"+ x + " " + y);       
              if (Square.inBounds(x,y))   {
                       System.out.println("In Bounds="+a + " " + b);       
                       point = getSquare(x,y);
                       int options=0;
                       //if not taken, then how many free neighbors?
                       if (!point.notFree()) {
                            ArrayList<Square> sides = point.getNeighbors();
                            for (int p=0; p<sides.size(); p++) {
                                options=options+(sides.get(p).isFree() ? 1:0);
                            }} else {
                           options = -1; } //free sides not relevant
                       tactical[a][b]=new Reader(x, y, point.takenBy(), point.isFree(),options);
                    } else {
                   System.out.println("Out of bounds="+a + " " +b);       
                   tactical[a][b]=new Reader(-1,-1,10,false,-1); //Out of bounds
      
               }
                     System.out.println("Tactical: " + tactical[a][b].free);          
            }}
         //the first move has special logic and flow....
        if(tactical[0][1].free) {
           System.out.println("first turn 0,1");
           firstMove = -1;
            heading=1;
            possible.add(new Path(firstMove, heading, 1, 0));
        } 
        if(tactical[1][2].free)  {
           System.out.println("first turn 1,2");
            firstMove = 0;
            possible.add(new Path(firstMove, heading, 2, 1));
            System.out.println("Now Back: " + possible.get(0).getFirstMove());          
      
        } 
        if(tactical[0][3].free)  {
           System.out.println("first turn 0,3");
            firstMove = 1;
            heading=3;
            possible.add(new Path(firstMove, heading, 3, 0));
        }
        //now it gets more difficult...abstract moves based on path data
        System.out.println("first turn paths="+possible.size());
        for (int k=0; k< possible.size(); k++) { //this should go until all checked
            System.out.println("k="+k);          
            if(!possible.get(k).getComplete()) {
    //            LFR(possible.get(k), tactical, possible);
    //      private void LFR(Path thisone, Reader[][] grid, ArrayList <Path> theList) {
               System.out.println("testing=");
               boolean tryLeft = true;
               boolean tryForward = true;
               boolean tryRight = true;
               boolean finishedL = false;
               boolean finishedF = false;
               boolean finishedR = false;
               int h, tx=0, ty=0;
               //keep it in bounds
               if (possible.get(k).heading == 1 || possible.get(k).x == 0) {
                  tryLeft = false;
               }
               if (possible.get(k).heading == 3 || possible.get(k).x == 4) {
                  tryRight = false;
               }
               if ((possible.get(k).heading == 2 && possible.get(k).y == 4) || 
                   (possible.get(k).heading == 1 && possible.get(k).x == 0) ||
                   (possible.get(k).heading == 3 && possible.get(k).x == 4)) {
                   tryForward = false;
               } //now try each allowed direction and add valid paths
               if (tryLeft) {
                  h =possible.get(k).heading - 1;
                  if (h==1) {
                     tx = possible.get(k).x-1;
                     ty = possible.get(k).y;
                  } else if (h ==2) {
                     tx = possible.get(k).x;
                     ty = possible.get(k).y+1;
                  }
                  if (ty==4 || possible.get(k).turn == 6) 
                     finishedL = true;
                  if(tactical[ty][tx].free) {
                    possible.add(new Path(possible.get(k).moves, (possible.get(k).turn+1), -1, h, tx, ty, finishedL));
                  }
               }
               if (tryForward) {
                  h = possible.get(k).heading;
                  if (h ==1) {
                     tx = possible.get(k).x-1;
                     ty = possible.get(k).y;
                  } else if (h ==2) {
                     tx = possible.get(k).x;
                     ty = possible.get(k).y+1;
                  } else if (h ==3) {
                     tx = possible.get(k).x+1;
                     ty = possible.get(k).y;
                  }
                  if (ty==4 || possible.get(k).turn == 6)
                     finishedF = true;
                  if (tactical[ty][tx].free) {
                     possible.add(new Path(possible.get(k).moves, (possible.get(k).turn+1), 0, h, tx, ty, finishedF));
                  }
               }
               if (tryRight) {
                  h = possible.get(k).heading+1;
                  if (h ==2) {
                     tx = possible.get(k).x;
                     ty = possible.get(k).y+1;
                  } else if (h ==3) {
                     tx = possible.get(k).x+1;
                     ty = possible.get(k).y;
                  }
                  if (ty==4 || possible.get(k).turn == 6)
                     finishedR = true;
                  if (tactical[ty][tx].free) {
                     possible.add(new Path(possible.get(k).moves, (possible.get(k).turn+1), 1, h, tx, ty, finishedR));
                  }
               }           
               } else {
                k=possible.size();
            }
        } 
            System.out.println("total paths="+possible.size());
            // score them somehow, and stay left
              // now get max turn and check scores and look for firstmove = L    
        for (int l=possible.size()-1; l>0; l--) { 
            System.out.println("L= "+l + possible.get(l).getTurn());
            System.out.println("Comp "+ possible.get(l).getComplete() ); //"+ possible.get(l).getFirstMove()"
            if (possible.get(l).getComplete() && possible.get(l).getFirstMove() == -1) {
              System.out.println("Left path="+l);
              l = 0;
               moveLeft = true;
           }
            if (possible.get(l).getComplete()) {
                if (possible.get(l).getFirstMove() == 0) {
                    moveForward = true;
                    System.out.println("Forward path="+l);
                } else {
                    moveRight = true;
                }
            }
        }
             // if complete & fm=L then good....
       if (moveLeft) {
            setCurDir(getCurDir().rotate(false));                
        } else if (moveForward) {
            setCurDir(getCurDir());
        } else if (moveRight) {
            setCurDir(getCurDir().rotate(true));
        } else { //in case there are no complete paths, take the longest one
            firstMove = possible.get(possible.size()-1).getFirstMove();
            if (firstMove == -1) {
            setCurDir(getCurDir().rotate(false));                
        } else if (firstMove == 0) {
            setCurDir(getCurDir());
        } else if (firstMove == 1) {
            setCurDir(getCurDir().rotate(true));
        }       
            }
     System.out.println("You Win=");
    
   }
    public class Reader {
        public int x,y, taken, freeSides;
        public boolean free;
        public Reader(int ix, int iy, int itaken, boolean ifree, int isides) {
            x = ix; //square number
            y = iy;
            taken = itaken; //10 is out of bounds, -1 free, 0-3 playerID
            freeSides = isides;
            free = ifree; 
    //          System.out.println("Initiate Reader"); 
        }
    }
    
    public class Path {
        // Paths are hypothetical sets of moves to get out of the 5x5.  One of them is best...
        ArrayList<Integer> moves = new ArrayList(); //holds a series of moves (-1=L, 0=F, 1=R)
        int firstMove, score, turn, heading, x, y; //first move directs action, score compares
        boolean used, complete; //used means Path has been superceded, complete means at border
        public Path(ArrayList m, int t, int dir, int h, int ix, int iy, boolean comp) {
            //Initialize on subesquent turns
            moves.addAll(m);
            moves.add(dir);
            firstMove = moves.get(0);
            turn = t;
            heading = h;
            x = ix;
            y = iy;
            complete = comp;
            System.out.println("Path: addition " + x + " "+y+ " "+ complete);          
        }
        public Path(int fm, int h, int fx, int fy) {
            //Initialize on the first move
            firstMove = fm;
            heading = h;
            moves.add(fm);
            turn = 1;
            x = fx;
            y = fy;        
            complete = false;
            System.out.println("Path: first timers");          
            }
        public void setScore(int s) {
            score = s;
            }
        public void setComplete(boolean c) {
            complete = c;
        }
        public int getScore() {
            return score;
        }
        public boolean getComplete() {
            return complete;
        }
        public int getFirstMove() {
            return firstMove;
        }
        public int getTurn() {
            return turn;
        }
    }
      //  4,0	4,1	4,2	4,3	4,4
      //  3,0	3,1	3,2	3,3	3,4
      //  2,0	2,1	2,2	2,3	2,4
      //  1,0	1,1	1,2	1,3	1,4
      //  0,0	0,1	0,2	0,3	0,4
     }
  //  public ArrayList<Reader> getTacticalFixed()
  //  {
  //      ArrayList<Reader> tacticalF=new ArrayList<Reader>();
//Determine direction so you can get 5 ahead and 2 to each side
  //      Dir cdir = getCurDir();
  //      Square loc = getPos();
   /** I need a way to number the squares relative to the direction.
   * I want square #1 to be to the right and then number in a consistent manner.
   * North(0,-1,0),
   * East(1,0,1),
   * South(0,1,2),
   * West(-1,0,3),
   * 
   *        1  2  3  4  5      num = 1 + py*(-1)*cdir.y + 5*py*(math.abs(cdir.y) -1)
   *        6  7  8  9  10             + px*(-1)*cdir.x + 5*px*(math.abs(cdir.x) =1)
   *        11 12 13 14 15     
   *        16 17 18 19 20
   *        21 22 23 24 25
  * 
     *
   *     int px = 0;
   *     int py;
   *   //  by using the current direction matrix, I get sideways +/-2 and 4 forward 0 back.
   *     for (int x=loc.getX()-2+2*cdir.x();x<=loc.getX()+2+2*cdir.x();x++)
   *     {
   *         px++;
   *         py=0;
   *         for (int y=loc.getY()-2+2*cdir.y();y<=loc.getY()+2+2*cdir.y();y++)
   *         {
   *             py++;
   *             if (Square.xInBounds(x) && Square.yInBounds(y))
   *                 {
   *                     tacticalF.add(new Reader(x, y, takenBy()));
   *                     
   *                 } 
   *             }         
   *     }
   *     return tacticalF;
   *   }
  */
