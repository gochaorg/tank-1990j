package xyz.cofe.game.tank.geom;

import xyz.cofe.fn.Consumer1;
import xyz.cofe.fn.Tuple2;

import java.util.*;

public class RectMap {
    private Set<Rect>[][] map2item;
    private double cellWidth;
    private double cellHeight;
    private Map<Rect, Set<Cell>> item2map;
    private Rect bounds;

    public RectMap(Rect bounds, Size2D cell){
        if( bounds==null )throw new IllegalArgumentException( "bounds==null" );
        if( cell==null )throw new IllegalArgumentException( "cell==null" );
        this.bounds = bounds;
        this.cellWidth = cell.width();
        this.cellHeight = cell.height();
    }

    protected Cells Cells(List<Cell> cells){
        return new Cells(cells);
    }
    protected Cell Cell(int x,int y){
        return new Cell(x, y);
    }
    protected Collision Collision( Rect what, Rect with, Rect intersection ){
        return new Collision(what, with, intersection);
    }

    public class Cell {
        public final int x;
        public final int y;

        public Cell(int x,int y){
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals( Object o ){
            if( this == o ) return true;
            if( o == null || getClass() != o.getClass() ) return false;
            Cell cell = (Cell) o;
            return x == cell.x && y == cell.y;
        }

        @Override
        public int hashCode(){
            return Objects.hash(x, y);
        }

        @Override
        public String toString(){
            return "Cell("+x+","+y+")";
        }

        public Rect toRect(){
            var b = bounds;
            if( b==null )throw new IllegalStateException("bounds==null");
            return Rect.of( x * cellWidth + b.left(), y*cellHeight + b.top(), cellWidth, cellHeight );
        }

        public int count(){
            if( y<0 )throw new IllegalStateException("y<0");
            if( x<0 )throw new IllegalStateException("x<0");

            if( map2item==null )return 0;
            if( y>=map2item.length )return 0;

            var xarr = map2item[y];
            if( xarr==null )return 0;
            if( x>=xarr.length )return 0;

            var set = xarr[x];
            if( set==null )return 0;
            return set.size();
        }
        public Set<Rect> items(){
            if( y<0 )throw new IllegalStateException("y<0");
            if( x<0 )throw new IllegalStateException("x<0");

            if( map2item==null )map2item = new Set[0][0];
            if( y>=map2item.length ){
                map2item = Arrays.copyOf(map2item, y+1);
            }

            var xarr = map2item[y];
            if( xarr==null ){
                xarr = new Set[0];
                map2item[y] = xarr;
            }

            if( x>=xarr.length ){
                xarr = Arrays.copyOf(xarr,x+1);
                map2item[y] = xarr;
            }

            var set = xarr[x];
            if( set==null ){
                set = new HashSet<>();
                xarr[x] = set;
            }

            return set;
        }

        public void put(Rect rect){
            if( rect==null )throw new IllegalArgumentException( "rect==null" );
            items().add(rect);

            if( item2map==null ){
                item2map = new HashMap<>();
            }

            item2map.computeIfAbsent(rect,x -> new HashSet<>()).add(this);
        }
        public void remove(Rect rect){
            if( rect==null )throw new IllegalArgumentException( "rect==null" );

            if( y<0 )throw new IllegalStateException("y<0");
            if( x<0 )throw new IllegalStateException("x<0");

            if( map2item==null )return;
            if( y>=map2item.length ){
                return;
            }

            var xarr = map2item[y];
            if( xarr==null ){
                return;
            }

            if( x>=xarr.length ){
                return;
            }

            var set = xarr[x];
            if( set==null ){
                return;
            }

            set.remove(rect);

            if( item2map!=null ){
                item2map.remove(rect);
            }
        }
    }
    public static class Collision {
        public final Rect what;
        public final Rect with;
        public final Rect intersection;

        public Collision( Rect what, Rect with, Rect intersection ){
            this.what = what;
            this.with = with;
            this.intersection = intersection;
        }
    }
    public class Cells implements Iterable<Cell> {
        public final List<Cell> cells;
        public Cells(List<Cell> cells){
            if( cells==null )throw new IllegalArgumentException( "cells==null" );
            this.cells = cells;
        }

        public int size(){ return cells.size(); }
        public Cell get(int idx){ return cells.get(idx); }

        @Override
        public Iterator<Cell> iterator(){ return cells.iterator(); }

        public void put( Rect rect ){
            if( rect==null )throw new IllegalArgumentException( "rect==null" );
            for( var c : cells ){
                c.put(rect);
            }
        }

        public Set<Rect> items(){
            HashSet<Rect> items = new HashSet<>(100);
            for( var c : cells ){
                items.addAll(c.items());
            }
            return items;
        }

        public List<Collision> collisions( Rect rect ){
            if( rect==null )throw new IllegalArgumentException( "rect==null" );
            ArrayList<Collision> lst = new ArrayList<>();
            for( var itm : items() ){
                collision(lst,rect,itm);
            }
            return lst;
        }

        protected void collision( List<Collision> lst, Rect rect, Rect itm ){
            itm.intersection(rect).ifPresent( isec -> {
                lst.add(Collision(rect,itm,isec));
            });
        }
    }

    public Optional<Cell> cellOf( Point pt ){
        return cellOf(pt, false);
    }
    public Optional<Cell> cellOf( Point pt, boolean fullInc ){
        if( pt==null )throw new IllegalArgumentException( "pt==null" );
        if( bounds==null )return Optional.empty();
        if( cellHeight<=0 )return Optional.empty();
        if( cellWidth<=0 )return Optional.empty();
        if( !bounds.contains(pt,true,fullInc,true,fullInc) )return Optional.empty();

        int cx = (int)((pt.x() - bounds.left()) / cellWidth);
        int cy = (int)((pt.y() - bounds.top()) / cellHeight);

        return Optional.of(Cell(cx,cy));
    }
    public Optional<Cells> cellsOf( Rect rect ){
        if( rect==null )throw new IllegalArgumentException( "rect==null" );
        if( bounds==null )return Optional.empty();
        if( cellHeight<=0 )return Optional.empty();
        if( cellWidth<=0 )return Optional.empty();

        var isect = bounds.intersection(rect);
        if( isect.isEmpty() )return Optional.empty();

        rect = isect.get();
        var ltCell = cellOf(rect.leftTopPoint());
        var rbCell = cellOf(rect.rightBottomPoint(),true);
        if( ltCell.isEmpty() ){
            throw new IllegalStateException("bug!");
        }
        if( rbCell.isEmpty() ){
            throw new IllegalStateException("bug!");
        }

        var lt = ltCell.get();
        var rb = rbCell.get();

        var lst = new ArrayList<Cell>();

        for( int y=lt.y; y<=rb.y; y++ ){
            for( int x=lt.x; x<=rb.x; x++ ){
                lst.add(Cell(x,y));
            }
        }

        return Optional.of(Cells(lst));
    }
    public Map<Rect,Set<Cell>> items(){
        if( item2map==null )item2map = new HashMap<>();
        return item2map;
    }

    public List<Collision> collisions( Rect rect ){
        if( rect==null )throw new IllegalArgumentException( "rect==null" );
        return cellsOf(rect).map( cells -> cells.collisions(rect) ).orElse(List.of());
    }
    public void put( Iterable<Rect> rects ){
        if( rects==null )throw new IllegalArgumentException( "rects==null" );
        for( var rect : rects ){
            if( rect==null )continue;
            cellsOf(rect).ifPresent( cells -> cells.put(rect) );
        }
    }
    public void cells( Consumer1<Cell> cell ){
        if( cell==null )throw new IllegalArgumentException( "cell==null" );
        var m2itm = map2item;
        if( m2itm==null )return;

        for( var y=0; y<m2itm.length; y++ ){
            var xcells = m2itm[y];
            if( xcells==null )continue;
            for( var x=0; x<xcells.length; x++ ){
                var rects = xcells[x];
                if( rects==null )continue;
                cell.accept( Cell(x,y) );
            }
        }
    }
}
