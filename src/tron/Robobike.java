/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tron;

import java.util.*;
import java.awt.Color;
public class Robobike extends Bike
{
    public static class InfluenceMap {
        int[][] value;
        boolean tempMap=false;
        public InfluenceMap(boolean isTemp)
        {
            value=new int[Square.xAmt()][Square.yAmt()];
            for (int x=0;x<xAmt();x++)
            {
                value[x]=new int[Square.yAmt()];
                for (int y=0;y<yAmt();y++)
                {
                    value[x][y]=0;
                }
            }
            tempMap=isTemp;
        }
        public int xAmt() {
            return value.length;
        }
        public int yAmt() {
            return value[0].length;
        }
        public int get(Square get)
        {
            return value[get.x()][get.y()];
        }
        public void diffuseAll()
        {
            InfluenceMap orig=clone();
            for (int x=0;x<xAmt();x++)
            {
                for (int y=0;y<yAmt();y++)
                {
//                    System.out.println("diffusing "+x+", "+y);
                    diffuse(orig.value[x][y],x,y);
                }
            }
        }
        public InfluenceMap clone() {
            InfluenceMap clone=new InfluenceMap(tempMap);
            for (int x=0;x<xAmt();x++)
            {
                for (int y=0;y<yAmt();y++)
                {
                    clone.value[x][y]=value[x][y];
                }
            }
            return clone;
        }
        public void diffuse(int val, int cx,int cy)
        {//                         centerX,centerY
            //Diffusal slope is always one
            if (val==0) return;
            boolean negative=(val<0);
            if (negative)
                val=-val;
            int endX=Util.lowest(cx+val,Square.xAmt());
            int endY=Util.lowest(cy+val,Square.yAmt());
            int startY=Util.highest(cy-val+1, 0);
            for (int x=Util.highest(cx-val+1,0);
                    x<endX;   ++x)
            {
                int curVal=val-Util.gridDistance(cx, cy, x, startY);
                for (int y=startY;y<endY;++y)
                {
                    if (curVal>0) {
                        if (negative)
                            value[x][y]-=curVal;
                        else
                            value[x][y]+=curVal;
                    }
                    if (y<cy)
                        curVal--;
                    else
                        curVal++;
                }
            }
        }
        public void addIn(InfluenceMap add)
        {
//            System.out.println("addin");
            for (int x=0;x<add.xAmt();x++)
            {
                for (int y=0;y<add.yAmt();y++)
                {
                    value[x][y]+=add.value[x][y];
                }
            }
        }
    }
    static InfluenceMap map=new InfluenceMap(false);
    public Robobike(int x,int y,Dir setDir)
    {
        super(x,y,setDir);
    }
    public Robobike(Square setLoc,Dir setDir)
    {
        super(setLoc,setDir);
    }
    public String getSymbol() {
        return "R";
    }
    public String toString() {
        return "Robobike "+id;
    }
    public String getWinPhrase() {
        return "Viva la robolucion!";
    }
    public static void updateMap()
    {
        for (int x=0;x<map.xAmt();x++)
        {
            for (int y=0;y<map.yAmt();y++)
            {
                if (Square.getSquare(x,y).isFree())
                {
                    map.value[x][y]=20;
                }
                else
                    map.value[x][y]=0;
            }
        }
        for (int b=0;b<Bike.amt();b++)
        {
            if (Bike.get(b).getPos().notError())
                map.value[Bike.get(b).getPos().x()][Bike.get(b).getPos().y()]-=400;
        }
        map.diffuseAll();
//        drawMap();
    }
    public static void drawMap()
    {
        for (int x=0;x<Square.xAmt();++x)
        {
            for (int y=0;y<Square.yAmt();++y)
            {
                if (Square.get(x,y).isFree())
                {
                    int r=120-map.value[x][y]/10;
                    int b=map.value[x][y]/25;
                    if (r>120) r=120;
                    if (r<0) r=0;
                    if (b>255) b=255;
                    if (b<0) b=0;
                    Draw.draw(new Draw(Square.get(x,y),new Color(0,r,b),1));
                }
            }
        }
    }
    public void move()
    {
        ArrayList<Square> nabors=getPos().getNeighbors();
        int highest=-1;
        int bestIx=-1;
//        System.out.println(nabors.size()+" neighbors got");
        for (int n=0;n<nabors.size();n++)
        {
            if (nabors.get(n).isFree())
            {
    //            System.out.println("neighval="+map.get(nabors.get(n)));
                if (map.get(nabors.get(n))>highest)
                {
                    bestIx=n;
                    highest=map.get(nabors.get(n));

                }
            }
        }
        if (bestIx!=-1)
            move(nabors.get(bestIx));
    }
}
