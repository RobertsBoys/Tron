/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tron;

import java.awt.Color;
import java.util.ArrayList;

public class TronPaul extends Bike
{
    ArrayList<Square> path;
    public TronPaul(int x,int y,Dir setDir)
    {
        super(x,y,setDir);
        path=new ArrayList<Square>();
    }
    public TronPaul(Square setLoc,Dir setDir)
    {
        super(setLoc,setDir);
        path=new ArrayList<Square>();
    }
    public void reset()
    {
        
    }
    public void move()
    {
        if (path.size()<=1)
            path=getLongestPath(getPos());
        else
            for (int n=path.size()-2;n>=0 && n>path.size()-10 ;n--)
            {// path.size()-1 is always taken: the bike is there
                if (path.get(n).notFree())
                {
                    path=getLongestPath(getPos());
                    break;
                }
            }
        path.remove(path.size()-1);
        for (int n=1;n<path.size();n++)
        {
            Draw.draw(new Draw(Draw.Type.Line,
                    path.get(n).px()+10,path.get(n).py()+10,
                    path.get(n-1).px()+10,path.get(n-1).py()+10, Color.YELLOW));
        }
        if (path.size()>0)
        {
            move((path.get(path.size()-1)));
        }
        else
        {
            Draw.draw(new Draw("Shit.",getPos().px()-15,getPos().py()-5, 10, Color.BLACK, 1));
        }
    }
    public ArrayList[][] makeGrid() {
        ArrayList[][] grid=new ArrayList[Square.xAmt()][Square.yAmt()];
        /*
        for (int b=0;b<Bike.amt();b++)
        {
            if (!Bike.get(b).equals(this))
            {
                Square where=Bike.get(b).getPos();
//                System.out.println("checking bike "+Bike.get(b).toString());
                for (int r=0;r<3;r++)
                {
//                    System.out.println("invalidatin square "+where.toString());
                    grid[where.x()][where.y()]=new ArrayList<Square>();
                    where=where.transform(Bike.get(b).getCurDir());
                    if (where.notFree())
                        r=4;
                }
            }
        }
        */
        return grid;
    }
    public ArrayList getPath(Square what,ArrayList[][] grid)
    {
        return grid[what.x()][what.y()];
    }
    public int pathLength(Square what,ArrayList[][] grid) {
        if (grid[what.x()][what.y()]==null)
            return 0;
        else return grid[what.x()][what.y()].size();
    }
    public ArrayList<Square> getLongestPath(Square where)
    {
        ArrayList[][] grid=makeGrid();
        
        grid[where.x()][where.y()]=new ArrayList<Square>();
        grid[where.x()][where.y()].add(where);
        Queue<Square> active=new Queue<Square>();
        active.push_back(where);
        int longestPath=0;
        Square furthest=where;
        while (active.notEmpty())
        {
            Square current=active.pop_front();
            ArrayList<Square> curPath=(ArrayList<Square>)getPath(where, grid);
            int curLeng=getPath(where, grid).size();
            if (curLeng > longestPath) 
            {
                System.out.println("len= "+curLeng);
                longestPath=curLeng;
                furthest=current;
            }
            for (int d=0;d<4;d++)
            {
                Square nayb=current.transform(Dir.getDir(d));
                if (nayb.notError() && nayb.isFree())
                {
                    if (pathLength(nayb,grid)<=curLeng)
                    {
                        boolean alreadyVisited=false;
                        if (getPath(where,grid)!=null) 
                        {//if the naybors curPath is null, it's certainly not already in the curPath
                            alreadyVisited= (curPath.contains(nayb));
                        }
                        if (!alreadyVisited) {
//                            active.purge(nayb);
                            active.push_back(nayb);
                            grid[nayb.x()][nayb.y()]=(ArrayList)curPath.clone();
                            grid[nayb.x()][nayb.y()].add(nayb);
                        }
                    }
                }
            }
        }
        return getPath(furthest,grid);
        /*
        if (gridLookup(where,squaresChecked)!=null)
            return gridLookup(where,squaresChecked);
        else
        {
            squaresChecked[where.x()][where.y()]=new ArrayList<Square>();
            gridLookup(where,squaresChecked).add(where);
        }
//        System.out.println("checking "+where.toString());
        ArrayList<Square> longest=new ArrayList<Square>();
        Dir direc=dir.getOpp();
        for (int r=0;r<3;r++)
        {
            direc=direc.rotate(true);
            Square to=where.transform(direc);
            if (to.isFree())
            {
                ArrayList<Square> thisPath=getLongestPath(to,direc,squaresChecked);
                if (thisPath.size()>longest.size())
                    longest=thisPath;
            }
        }
        longest=(ArrayList<Square>)longest.clone();
        longest.add(where);
        squaresChecked[where.x()][where.y()]=longest;
        return longest;
                
                */
    }
    public String getSymbol() {
        return "TP";
    }
    public String toString()
    {
        return "Tron Paul "+id;
    }
    public String getWinPhrase()
    {
        return "Tron Paul 2016!";
    }
}
