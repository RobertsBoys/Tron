/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tron;

import java.awt.*;
import java.util.ArrayList;

public abstract class Bike {
    protected int id;
    protected Color color;
    private Square pos;
    private boolean alive;
    private boolean moved;
    private Dir curDir;
    
    public Bike(int x,int y,Dir setDir)
    {
        id=curID;
        curID++;
        color=getColor(id).brighter();
        pos=Square.getSquare(x,y);
        curDir=setDir;
        alive=true;
        moved=false;
        (pos).take(id);
    }
    public Bike(Square setPos,Dir setDir)
    {
        id=curID;
        curID++;
        color=getColor(id).brighter();
        pos=setPos;
        curDir=setDir;
        alive=true;
        moved=false;
        (pos).take(id);
    }
    public Bike()
    {
        this(0,0,Dir.East);
    }
    public void update()
    {
        moved=false;
        move();
        if (!moved)
        {
            move(curDir);
        }
    }
    public void setCurDir(Dir set) {
        if (!set.isOpp(curDir)) {
            curDir=set;
        }
    }
    public abstract void move();
    protected void move(Square to)
    {
        if (pos.isNeighbor(to))
        {
            if (to.isFree())
            {
                curDir=pos.getDir(to);
                pos=to;
                moved=true;
            }
            else {
                checkCollide(to);
                kill();
            }
            pos.take(id);
        }
        else 
            move(pos.getDir(to));
    }
    protected void move(Dir dir)
    {
        if (moved)
            return;
        if (dir.isOpp(curDir))
            return;
        pos=pos.transform(dir);
        if (pos.isFree())
        {
            curDir=dir;
            moved=true;
            pos.take(id);
        }
        else 
        {
            checkCollide(pos);
            kill();
        }
    }
    private void checkCollide(Square at) {
        for (int b=0;b<bikes.size();++b)
        {
            if (!bikes.get(b).equals(this))
            {
                if (bikes.get(b).getPos().equals(at))
                {
                    bikes.get(b).kill();
                }
            }
        }
    }
    public abstract String toString();
    public abstract String getWinPhrase();
    public abstract String getSymbol();
    public void onEnd(boolean win)
    {
        
    }
    public boolean equals(Bike other)
    {
        if (other==null)
            return false;
        return (id==other.getID());
    }
    public int getID() {
        return id;
    }
    public Square getPos() {
        return pos;
    }
    public Dir getCurDir() {
        return curDir;
    }
    public boolean isAlive() {
        return alive;
    }
    public Square nextPos() {
        return pos.transform(curDir);
    }
    private void kill()
    {
//        (pos).take(id);
        alive=false;
    }
    public void draw(Graphics2D g)
    {
        if (alive)
        {
            g.setColor(color);
            g.fillOval((pos).getPX(),(pos).getPY(),
                    Square.SQUARE_SIZE,Square.SQUARE_SIZE);
            g.setColor(Color.BLACK);
            g.drawOval((pos).getPX(),(pos).getPY(),
                    Square.SQUARE_SIZE,Square.SQUARE_SIZE);
            g.drawString(getSymbol(),(pos).getPX()+5,(pos).getPY()+15);
        }
    }
    public static double getNrstBikeDist(Square from,Bike ignore)
    {
        double lowestDist=99999999;
        for (int n=0;n<bikes.size();n++)
        {
            if (bikes.get(n).isAlive())
                if (!bikes.get(n).equals(ignore))
                {
                    double dist=Util.gridDistance(from,bikes.get(n).getPos());
                    if (dist<lowestDist)
                    {
                        lowestDist=dist;
                    }
                }
        }
        return lowestDist;
    }
    public static Color getColor(int num)
    {
        switch (num)
        {
            case -1:
                return new Color(255,255,255,100);
            case 0:
                return new Color(230,0,0);
            case 1:
                return new Color(0,0,230);
            case 2:
                return new Color(0,230,0);
            case 3:
                return new Color(0,230,230);
            case 4:
                return new Color(250,250,0);
            case 5:
                return new Color(250,0,250);
            default:
                return Color.BLACK;
        }
    }
    /*
    ---------------------------------------------------
    ----------Static-----------------------------------
    ---------------------------------------------------
    */
    private static ArrayList<Bike> bikes;//a list of all the bikes
    static int curID=0;//Not for you

    //--------MVP Functions------------------
    public static int amt()
    {//Amount of bikes in the list.
        return bikes.size();
    }
    public static Bike get(int n)
    {//How to access the bikes
        if (n<amt())
            return bikes.get(n);
        else return null;
    }
    
    //-----------The Rest-----------------
    
    public static void onStart()
    {//Not for you
        curID=0;
        bikes=new ArrayList<Bike>();
        createBikes();
    }
    private static void createBikes()
    {
    /*
        Edit this to change around the lineup.
        Keep the directions as they are or the bikes might immidiately crash
        Don't call this from your own code
    */
        bikes.add(new PlayerBike(Square.northWest(),Dir.East));
        bikes.add(new NextGenBike(Square.northEast(),Dir.South));
        bikes.add(new Robobike(Square.southEast(),Dir.West));
        bikes.add(new MapHugger(Square.southWest(),Dir.North));
        
    }
    public static void resetAll()
    {//Not for you
        bikes.clear();
        curID=0;
        createBikes();
    }
}
