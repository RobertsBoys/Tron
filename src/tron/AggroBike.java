/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tron;

import java.awt.Color;
import java.util.ArrayList;
/**
 *
 * @author Isaac
 */
public class AggroBike extends Bike
{
    protected class Space{
        int paths;
        int dist;
        int parentX,parentY;
        double value;
        public Space() {
            paths=0;
            parentX=-1;
            parentY=-1;
            dist=Integer.MAX_VALUE;
            value=0;
        }
    }
    protected Space map[][];
    public AggroBike(int x, int y,Dir setDir)
    {
        super(x,y,setDir);
        construct();
    }
    public AggroBike(Square setloc, Dir setDir)
    {
        super(setloc,setDir);
        construct();
    }
    private void construct()
    {
        map=new Space[Square.xAmt()][Square.yAmt()];
        for (int x=0;x<Square.xAmt();++x)
        {
            for (int y=0;y<Square.yAmt();++y)
            {
                map[x][y]=new Space();
            }
        }
    }
    public String toString()
    {
        return "AggroBike "+id;
    }
    public String getWinPhrase()
    {
        return "GRR";
    }
    public String getSymbol()
    {
        return "Ag";
    }
    public void move()
    {
//        if (Util.update()%4==1)
            calcMap();
    }
    protected void calcMap()
    {
        int pointAmt=20;
        for (int x=0;x<Square.xAmt();++x)
        {
            for (int y=0;y<Square.yAmt();++y)
            {
                map[x][y].paths=0;
            }
        }
        for (int n=0;n<pointAmt;n++)
        {
//            System.out.println("Calcing point "+n);
            djikstra(getRandPoint());
        }
        double highest=0;
        Square best=nextPos();
        for (int x=0;x<Square.xAmt();++x)
        {
            for (int y=0;y<Square.yAmt();++y)
            {
                if (map[x][y].paths!=0)
                {
                    map[x][y].value=map[x][y].paths;
                    for (int b=0;b<Bike.amt();++b)
                    {
                        double dist=Util.gridDistance(Bike.get(b).nextPos(),Square.get(x,y));
                        double val=pointAmt/(1+dist);
//                        if (Bike.get(b).equals(this))
//                            map[x][y].value+=val;
//                        else
                            map[x][y].value-=val;
                    }
                    Square sqr=Square.get(x, y);
                    if (map[x][y].value>highest)
                    {
                        highest=map[x][y].value;
                        best=sqr;
                    }
//                    Draw.draw(new Draw(""+(int)map[x][y].value,sqr.px(),sqr.py(),10,Color.YELLOW,1));
                }
            }
        }
        move(getPos().getDir(best));
    }
    protected Square getRandPoint()
    {
        Square point;
        do {
            point=Square.getRandSquare();
        } while (point.notFree());
        return point;
    }
    protected void djikstra(Square point)
    {
//        System.out.println("djikking");
        for (int x=0;x<Square.xAmt();++x)
        {
            for (int y=0;y<Square.yAmt();++y)
            {
//                if (map[x][y]!=null)
                {
                    map[x][y].dist=Integer.MAX_VALUE;
                    map[x][y].parentX=-1;
                    map[x][y].parentY=-1;
                }
            }
        }
        getSpace(point).dist=0;
        
        Queue<Square> active=new Queue<Square>();
        active.push_back(point);
        
        while (active.notEmpty())
        {
            Square cur=active.pop_front();
            Space current=getSpace(cur);
            for (int dir=0;dir<4;dir++)
            {
                Square naybor= cur.transform(Dir.getDir(dir));
                if (!naybor.isError())
                {
                    Space nayb=getSpace(naybor);
                    if (current.dist+1 < nayb.dist)
                    {
                        nayb.dist=current.dist+1;
                        nayb.parentX=cur.x();
                        nayb.parentY=cur.y();
                        if (naybor.isFree())//check here so the bikes will be marked
                        {
                            active.push_back(naybor);
                        }
                    }
                }
            }
        }
        for (int b=0;b<Bike.amt();++b)
        {
            Space pos=getSpace(Bike.get(b).nextPos());
            boolean atPoint=false;
            while (!atPoint) {
                if (pos.parentX!=-1)
                {
                    pos.paths++;
                    pos=map[pos.parentX][pos.parentY];
                }
                else atPoint=true;
            }
        }
    }
    protected Space getSpace(Square loc)
    {
        if (loc.isError()) {
            System.out.println("Loc is error AB");
            loc=null;
            System.out.println(loc.toString());
        }
        return map[loc.x()][loc.y()];
    }
}
