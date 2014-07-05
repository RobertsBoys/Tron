package tron;


import java.util.*;
import java.awt.*;

public class Util 
{
    private static int updates;//Keeps track of number of updates since game began
                    //Doesn't change while paused
                    //Can be used to keep track of game-time
    
    public static void onStart()
    {//Not for you
        updates=0;
    }
    public static Square getIntersection(Square p1,Dir d1,Square p2,Dir d2)
        /*
            If there are two bikes, Bike1 and Bike2,
                and Bike1 is at location p1 moving in direction d1,
                and Bike2 is at p2 moving in d2,
                this function returns where they will collide.
                It seems silly but its a lot of typing.
            If they won't collide it returns Square.error,
                which can be tested for with _.isError()
        */
    {
        if (d1.x()>0 && p1.x() > p2.x())
            return Square.error;
        else if (d1.x()<0 && p1.x()<p2.x())
            return Square.error;
        if (d2.x()>0 && p2.x() > p1.x())
            return Square.error;
        else if (d2.x()<0 && p2.x() < p1.x())
            return Square.error;
        if (d1.y()>0 && p1.y() > p2.y())
            return Square.error;
        else if (d1.y()<0 && p1.y()<p2.y())
            return Square.error;
        if (d2.y()>0 && p2.y() > p1.y())
            return Square.error;
        else if (d2.y()<0 && p2.y() < p1.y())
            return Square.error;
        
        if (Dir.sameDimension(d1, d2))
        {
            if (d1.x()!=0)
            {
                if (p1.x()==p2.x())
                {
                    return Square.getSquare((p1.x()+p2.x())/2,p1.y());
                }
                else return Square.error;
            }
            else
            {//then the directions are the same on the y
                if (p1.y()==p2.y())
                {
                    return Square.getSquare(p1.x(),(p1.y()+p2.y())/2);
                }
                else return Square.error;
            }
        }
        else
        {
            if (d1.x()!=0)
            {//then d1 is y
                return Square.getSquare(p2.x(),p1.y());
            }
            else
            {//else d1 is y and d2 is x
                return Square.getSquare(p1.x(),p2.y());
            }
        }
    }
    public static int gridDistance(Square p1,Square p2) {
        /*
            GridDistance means the amount of time a bike will take
                to move from p1 to p2
        */
        return Math.abs(p1.x()-p2.x())+ Math.abs(p1.y()-p2.y());
    }
    public static int gridDistance(int x,int y,int eks,int wi)
    {
        return Math.abs(x-eks)+ Math.abs(y-wi);
    }
    public static double sqrdDistance(Square p1,Square p2)
        /*
            Returns the hypotenuse distance between p1 & p2, except squared.
            Its quicker than regular distance and its still valid
            for simple greater than comparisons
        */
    {
        return sqrdDistance(p1.x(),p1.y(),p2.x(),p2.y());
    }
    public static double sqrdDistance(int x,int y,int eks,int wi)
    {
        return Math.pow(x-eks, 2)+ Math.pow(y-wi,2);
    }
    public static double distance(Square p1,Square p2)
        /*
            Returns hypotenuse distance between p1 and p2
        */
    {
        return distance(p1.x(),p1.y(),p2.x(),p2.y());
    }
    public static double distance(int x,int y,int eks,int wi)
    {
        return Math.sqrt(sqrdDistance(x,y,eks,wi));
    }
    public static int highest(int a,int b) {
        if (a>b)
            return a;
        else return b;
    }
    public static int lowest(int a,int b) {
        if (a<b)
            return a;
        else return b;
    }
    public static int getTime() {
        return updates;
    }
    public static int updatesPassed() {
        return updates;
    }
    public boolean everyNth(int frequency)
    /*
        Returns true every Nth updates(game ticks).
        Can be used to run a routine every 5 updates without keeping track of it yourself
        Add a second parameter to shift the phase. 
            (ex. everyNth(5,2) returns true on Update 2,7,12)
    */
    {
        return updates%frequency == 0;
    }
    public boolean everyNth(int freq,int offset)
    {
        /*  See previous function.
            Do not use negative offsets, as modulo gets funky with negative numbers
        */
        return (updates+offset)%freq ==0;
    }
    public static void onFrame() {
        //Not for you
        updates++;
    }
    public static void reset() {
        //Not for you
        updates=0;
    }
}
