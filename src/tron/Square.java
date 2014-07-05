/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tron;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Square 
{
    static int SQUARE_SIZE=20;
    
    private int px,py;
    private int gx,gy;
    int taken;//Signifies whether and who has left a wall in this square
    
    //----------MVP Functions-----------------------------
    
    public boolean isTaken()
    {
        return taken!=-1;
    }
    public boolean isFree()
    {
        if (!isError())
            return taken==-1;
        else return false;
    }
    public boolean isError() {
        return gx==-1 && gy==-1;
    }
    public boolean notError() {
        return gx!=-1 || gy!=-1;
    }
    public Square transform(Dir dir) {
        return getSquare(gx+dir.x(),gy+dir.y());
    }
    //-------------The rest----------------------------
    
    public Square(int x,int y)
    {
        gx=x;
        gy=y;
        px=gx*SQUARE_SIZE+20;
        py=gy*SQUARE_SIZE+60;
        taken=-1;
    }
    public void draw(Graphics2D g)
    {
        if (isFree())
            g.setColor(new Color(20,20,20));
        else g.setColor(Bike.getColor(taken));
        g.fillRect(px,py,SQUARE_SIZE,SQUARE_SIZE);
        g.setColor(new Color(70,100,180));
        g.drawRect(px,py,SQUARE_SIZE,SQUARE_SIZE);
    }
    public boolean isNeighbor(Square other)
    {
        int xDif=Math.abs(other.x()-gx);
        if (xDif>1)
            return false;
        int yDif=Math.abs(other.y()-gy);
        if (yDif>1)
            return false;
        return ((xDif==1)^(yDif==1));
    }
    public ArrayList<Square> getNeighbors()
    {
        ArrayList<Square> nabors=new ArrayList<Square>();
        for (int x=gx-1;x<=gx+1;x++)
        {
            for (int y=gy-1;y<=gy+1;y++)
            {
                if ( (x==gx) ^ (y==gy))//removes diagonals and center
                {
                    if (Square.xInBounds(x) && Square.yInBounds(y))
                    {
                        nabors.add(getSquare(x,y));
                    }
                }
            }
        }
        return nabors;
    }
    public Square getRandomNeighbor() {
        return getRandNeighbor();
    }
    public Square getRandNeighbor()
    /*
        Guaranteed to not return null
        Will only return Square.error if Square.error is passed in
    */
    {
        if (isError())
            return this;
        Square neighb;
        do {
            neighb=this.transform(Dir.getRandDir());
        } while (neighb.isError());
        return neighb;
    }
    public int getX() {
        return gx;
    }
    public int getY() {
        return gy;
    }
    public int x() {
        return gx;
    }
    public int y() {
        return gy;
    }
    public int getPX(){
        return px;
    }
    public int getPY(){
        return py;
    }
    public int px()//Pixel x location
    {
        return px;
    }
    public int py()//Pixel y location
    {
        return py;
    }
    public void take(int byWho)
    {//Not for you
        taken=byWho;
    }
    public Dir getDir(Square to)
    {
    /*
        Gives the general direction from this square to another
    */
        if (Math.abs(gx-to.gx)>Math.abs(gy-to.gy))
        {
            if (gx>to.gx)
                return Dir.East;
            else return Dir.West;
        }
        else
        {
            if (gy>to.gy)
                return Dir.South;
            else 
                return Dir.North;
        }
    }
    public boolean hasPath(Square to)
    {
        if (equals(to))
            return true;
        ArrayList<Square> nabors=getNeighbors();
        for (int n=0;n<nabors.size();n++)
        {
            if (!isInChecked(nabors.get(n)))
                if (nabors.get(n).hasPath(to))
                    return true;
        }
        return false;
    }
    public boolean equals(Square other)
    {
        if (other==null)
            return false;
        if (gx!=other.gx) return false;
        if (gy!=other.gy) return false;
        return true;
    }
    public int takenBy() {
        return taken;
    }
    public boolean notFree() 
    {//because if(loc.notFree()) is more readable than if(!loc.isFree())
        if (isError())
            return true;
        else if (taken==-1)
            return false;
        else return true;
    }
    public String toString()
    {
        return gx+", "+gy;
    }
    
    /*
    ----------------------------------------------------------
    -------------Static---------------------------------------
    ----------------------------------------------------------
    */
    private static Square[][] grid;//Holds all squares
    
    final static Square error=new Square(-1,-1);//Used when a function
                            //that needs to return a square has an error.
                            //Used instead of null to prevent crashes
                            //Also used when you go off the map
    
    private static ArrayList<Square> prechecked;//Not for you
            
    
    //---------MVP Static Functions-----------------------
    
    public static int xAmt() {//Amount of squares going across
        return grid.length;
    }
    public static int yAmt() {//Amount of squares going down
        return grid[0].length;
    }
    public static Square get(int x,int y)
    {//Gets a square.
        return getSquare(x,y);
    }
    
    //-----------The Rest-------------------------
    
    public static void onStart()
    {//Not for you
        prechecked=new ArrayList<Square>();
        grid=new Square[1160/SQUARE_SIZE][900/SQUARE_SIZE];
        System.out.println("Grid size = "+grid.length+" X "+grid[0].length);
        for (int x=0;x<grid.length;x++)
        {
            for (int y=0;y<grid[x].length;y++)
            {
                grid[x][y]=new Square(x,y);
            }
        }
    }
    public static ArrayList<Square> getPath(Square from, Square to)
    /*
    Uses djikstras algorithm to find the shortest path
    Biased towards directions in NESW order
    Includes beginning and end point in path
    Both from and to can be taken.
    On failure returns an empty list
    */
    {
        if (from.isError() || to.isError())
        {
            System.out.println("From and To cannot be error square");
            return null;
        }
        class Point
        {
            boolean checked;
            Square parent;
            Point() {
                checked=false;
                parent=null;
            }
        }
        Point[][] map=new Point[xAmt()][yAmt()];
        
        for (int x=0;x<Square.xAmt();++x)
        {
            for (int y=0;y<Square.yAmt();++y)
            {
                map[x][y]=new Point();
            }
        }
        Point start=map[from.x()][from.y()];
        start.checked=true;
        
        Queue<Square> active=new Queue<Square>();
        active.push_back(from);
        boolean success=false;
        while (active.notEmpty())
        {
            Square cur=active.pop_front();
            Point current=map[cur.x()][cur.y()];
            for (int dir=0;dir<4;dir++)
            {
                Square naybor= cur.transform(Dir.getDir(dir));
                if (!naybor.isError())
                {
                    Point nayb=map[naybor.x()][naybor.y()];
                    if (!nayb.checked)
                    {
                        nayb.checked=true;
                        nayb.parent=cur;
                        if (naybor.equals(to)) {
                            success=true;
                            dir=5;
                            active.clear();
                            //breaks both loops
                        }
                        else if (naybor.isFree())//check here so the bikes will be marked
                        {
                            active.push_back(naybor);
                        }
                    }
                }
            }
        }
        if (success)
        {
            ArrayList<Square> path=new ArrayList<Square>();
            Square cur=to;
            while (cur!=from)
            {
                if (cur==null) {
                    System.out.println("cur is null");
                    break;
                }
                else if (cur.isError()) {
                    System.out.println("cur is an error");
                    break;
                }
                path.add(0, cur);
                cur=map[cur.x()][cur.y()].parent;
            }
            path.add(0,from);
            return path;
        }
        else {
            return new ArrayList<Square>();
        }
    }
    public static ArrayList<Square> getPath(Square from, Square to,double[][] weight)
    {
    /*
        Uses djikstras algorithm to find the shortest path
        Biased towards directions in NESW order
        Includes beginning and end point in path
        Both from and to can be taken.
        On failure returns an empty list
        The weight of each square is the cost to enter it
        Weights cannot be negative
    */
        if (from.isError() || to.isError())
        {
            System.out.println("From and To cannot be error square");
            return null;
        }
        if (weight.length!=grid.length
            || weight[0].length!=grid.length)
        {
            System.out.println("Weight array must be Square.xAmt by Square.yAmt");
            if (weight.length>0)
                System.out.println("  - it is "+weight.length+" by "+weight[0].length );
            else System.out.println(" - it is empty");
            return null;
        }
        class Point
        {
            double dist;
            Square parent;
            Point() {
                dist=Double.POSITIVE_INFINITY;
                parent=null;
            }
        }
        Point[][] map=new Point[xAmt()][yAmt()];
        
        for (int x=0;x<Square.xAmt();++x)
        {
            for (int y=0;y<Square.yAmt();++y)
            {
                map[x][y]=new Point();
            }
        }
        Point start=map[from.x()][from.y()];
        start.dist=0;
        
        Queue<Square> active=new Queue<Square>();
        active.push_back(from);
        boolean success=false;
        while (active.notEmpty())
        {
            Square cur=active.pop_front();
            Point current=map[cur.x()][cur.y()];
            for (int dir=0;dir<4;dir++)
            {
                Square naybor= cur.transform(Dir.getDir(dir));
                if (!naybor.isError())
                {
                    Point nayb=map[naybor.x()][naybor.y()];
                    double cost=weight[naybor.x()][naybor.y()];
                    if (cost<0) {
                        System.out.println("Costs can not be negative (yet).");
                        return null;
                    }
                    if (current.dist+cost < nayb.dist)
                    {
                        nayb.dist=current.dist+cost;
                        nayb.parent=cur;
                        if (naybor.equals(to)) {
                            success=true;
                            dir=5;
                            active.clear();
                            //breaks both loops
                        }
                        else if (naybor.isFree())//check here so the bikes will be marked
                        {
                            active.push_back(naybor);
                        }
                    }
                }
            }
        }
        if (success)
        {
            ArrayList<Square> path=new ArrayList<Square>();
            Square cur=to;
            while (cur!=from)
            {
                if (cur==null) {
                    System.out.println("cur is null");
                    break;
                }
                else if (cur.isError()) {
                    System.out.println("cur is an error");
                    break;
                }
                path.add(0, cur);
                cur=map[cur.x()][cur.y()].parent;
            }
            path.add(0,from);
            return path;
        }
        else {
            return new ArrayList<Square>();
        }
    }
    public static void resetAll()
    {//Not for you
        for (int x=0;x<grid.length;x++)
        {
            for (int y=0;y<grid[x].length;y++)
            {
                grid[x][y].take(-1);
            }
        }
    }
    public static void drawAll(Graphics2D g)
    {//Not for you
        for (int x=0;x<grid.length;x++)
        {
            for (int y=0;y<grid[x].length;y++)
            {
                grid[x][y].draw(g);
            }
        }
    }
    
    public static boolean moveable(int x,int y) {
        //Returns true if location is both on the map and free
        if (inBounds(x,y))
        {
            if (getSquare(x,y).isFree())
                return true;
            else return false;
        }
        else return false;
    }
    public static boolean inBounds(int x,int y)
    {//Returns true if x,y is on the map
        return (x>=0 && x<grid.length && y>=0 && y<grid[0].length);
    }
    public static boolean xInBounds(int x) {
        return (x>=0 && x<grid.length);
    }
    public static boolean yInBounds(int y) {
        return (y>=0 && y<grid[0].length);
    }
    
    public static Square getSquare(int x,int y)
    {
        if (x>=0 && x<grid.length && y>=0 && y<grid[x].length)
            return grid[x][y];
        else return error;
    }
    public static boolean isInChecked(Square in)
    {//Not for you
        for (int n=0;n<prechecked.size();n++)
        {
            if (in.equals(prechecked.get(n)))
                return true;
        }
        return false;
    }
    //These 4 functions get the corners of the map
    public static Square northWest() {
        return Square.get(0,0);
    }
    public static Square southWest() {
        return Square.get(0,Square.yAmt()-1);
    }
    public static Square northEast() {
        return Square.get(Square.xAmt()-1,0);
    }
    public static Square southEast() {
        return Square.get(Square.xAmt()-1,Square.yAmt()-1);
    }
    public static Square getRandSquare() {
        //Gets any random square off the map.
        //Guarenteed not to return null or Square.error
        Random rand=new Random();
        return grid[rand.nextInt(xAmt())][rand.nextInt(yAmt())];
    }

}
