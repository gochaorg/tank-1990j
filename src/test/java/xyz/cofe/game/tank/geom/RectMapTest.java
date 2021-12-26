package xyz.cofe.game.tank.geom;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import xyz.cofe.T;
import xyz.cofe.game.tank.stat.Series;
import xyz.cofe.game.tank.stat.TCounter;
import xyz.cofe.text.Align;
import xyz.cofe.text.Text;

import java.util.*;

public class RectMapTest {
    public List<Rect> generate( int cnt, Size2D minRect, Size2D maxRect, Rect bounds ){
        if( cnt<2 )throw new IllegalArgumentException( "cnt<2" );
        if( minRect==null )throw new IllegalArgumentException( "minRect==null" );
        if( maxRect==null )throw new IllegalArgumentException( "maxRect==null" );
        if( bounds==null )throw new IllegalArgumentException( "bounds==null" );
        Random rnd = new Random();
        ArrayList<Rect> rects = new ArrayList<>();
        for( int i=0; i<cnt; i++ ){
            double w = minRect.width() + Math.abs(rnd.nextDouble())*(maxRect.width()-minRect.width());
            double h = minRect.height() + Math.abs(rnd.nextDouble())*(maxRect.height()-minRect.height());
            double x = Math.abs(rnd.nextDouble())*(bounds.width()-w) + bounds.left();
            double y = Math.abs(rnd.nextDouble())*(bounds.height()-h) + bounds.top();
            rects.add(Rect.of(x,y,w,h));
        }
        return rects;
    }

    public List<Rect> computeCollisions( List<Rect> rects, Rect rect ){
        List<Rect> res = new ArrayList<>();
        for( var r : rects ){
            rect.intersection(r).ifPresent(i -> res.add(r));
        }
        return res;
    }

    public final Rect bounds = Rect.of(0,0,512,512);

    @Tag(T.Perf)
    @Test
    public void std_collision_perf(){
        double bounds_size = 512;
        bounds_size = bounds_size * 8;
        Rect bounds = Rect.of(0,0,bounds_size,bounds_size);

        Size2D minGenSize = Size2D.of(8,8);
        Size2D maxGenSize = Size2D.of(64,64);

        Size2D minSize = Size2D.of(16,16);
        Size2D maxSize = Size2D.of(64,64);
        for( int i=50; i<=500; i+=50 ){
            System.out.println("generate i="+i+" i*i="+(i*i));
            var rects = generate(i*i,minGenSize, maxGenSize, bounds);

            TCounter cnt = new TCounter(100);
            Random rnd = new Random();

            for( int j=0; j<500; j++ ){
                double w = minSize.width() + Math.abs(rnd.nextDouble())*(maxSize.width()-minSize.width());
                double h = minSize.height() + Math.abs(rnd.nextDouble())*(maxSize.height()-minSize.height());
                double x = Math.abs(rnd.nextDouble())*(bounds.width()-w) + bounds.left();
                double y = Math.abs(rnd.nextDouble())*(bounds.height()-h) + bounds.top();
                Rect rect = Rect.of(x,y,w,h);

                cnt.start();
                computeCollisions(rects, rect);
                cnt.stop();
            }

            System.out.println("stat");
            cnt.new Echo().print("");
        }
    }

    @Tag(T.Simple) @Test
    public void cellOf(){
        var rm = new RectMap(bounds, Size2D.of(32,32));

        var p0 = rm.cellOf(Point.of(30,30));
        System.out.println(p0);
        Assertions.assertTrue(p0.isPresent());
        Assertions.assertTrue(p0.get().x==0 && p0.get().y==0);

        var p1 = rm.cellOf(Point.of(60,60));
        System.out.println(p1);
        Assertions.assertTrue(p1.isPresent());
        Assertions.assertTrue(p1.get().x==1 && p1.get().y==1);
    }

    @Tag(T.Simple) @Test
    public void cellOf_02(){
        var rm = new RectMap(bounds, Size2D.of(32,32));

        var irect = Rect.rect(40,40, 70,70);
        var r0 = rm.cellsOf(irect);
        System.out.println(r0);
        Assertions.assertTrue(r0.isPresent());
        Assertions.assertTrue(r0.get().size()>0);

        for( var i=0; i<r0.get().size(); i++ ){
            var c = r0.get().get(i);
            System.out.println("["+i+"] "+c+" -> "+c.toRect());
            Assertions.assertTrue(irect.intersection(c.toRect()).isPresent());
        }
    }

    @Tag(T.Simple) @Test
    public void put_01(){
        var rm = new RectMap(bounds, Size2D.of(32,32));

        Size2D minGenSize = Size2D.of(8,8);
        Size2D maxGenSize = Size2D.of(64,64);

        for( var r : generate(100,minGenSize,maxGenSize,bounds) ){
            rm.cellsOf(r).ifPresent( cells -> {
                cells.put(r);
            });
        }

        System.out.println("items, count "+rm.items().size() );
        rm.items().forEach( (r,cells)->{
            System.out.println(" rect "+r+" cells "+cells);
        });
    }

    public List<Rect> generate_linear( int w, int h, double cw, double ch ){
        var l = new ArrayList<Rect>();
        for( int y=0; y<h; y++ ){
            for( int x=0; x<w; x++ ){
                l.add(Rect.of(x*cw, y*ch, cw, ch));
            }
        }
        return l;
    }

    @Tag(T.Simple) @Test
    public void intersection(){
        var bounds = Rect.of(0,0,100,100);
        var cw = 10;
        var ch = 10;
        var rm = new RectMap(bounds, Size2D.of(cw,ch));

        //Size2D minGenSize = Size2D.of(8,8);
        //Size2D maxGenSize = Size2D.of(64,64);
        var rects //= generate(100,minGenSize,maxGenSize,bounds);
            = generate_linear(10,10, cw, ch);
        for( var r : rects ){
            rm.cellsOf(r).ifPresent( cells -> {
                cells.put(r);
            });
        }

        var rectCmp = new Comparator<Rect>(){
            @Override
            public int compare( Rect a, Rect b ){
                int c0 = Double.compare(a.left(), b.left());
                int c1 = Double.compare(a.top(), b.top());
                int c2 = Double.compare(a.right(), b.right());
                int c3 = Double.compare(a.bottom(), b.bottom());
                return c0!=0 ? c0 : c1!=0 ? c1 : c2!=0 ? c2 : c3;
            }
        };

        var isec1 = new TreeSet<Rect>(rectCmp);

        Rect rect = Rect.of(bounds.width()/3, bounds.height()/3, bounds.width()/3, bounds.height()/3 );
        System.out.println("rect "+rect);

        rm.cellsOf(rect).ifPresent( q -> {
            var clz = q.collisions(rect);
            System.out.println("collisions count "+clz.size());
            clz.forEach(x->isec1.add(x.with));
        });

        var isec2 = new TreeSet<Rect>(rectCmp);
        isec2.addAll(computeCollisions(rects, rect));
        System.out.println("computeCollisions c "+isec2.size());

        System.out.println("isec1 . isec2 "+isec1.containsAll(isec2));
        System.out.println("isec2 . isec1 "+isec2.containsAll(isec1));

        System.out.println("\nisec1:");
        for( var r : isec1 ) System.out.println(r);

        System.out.println("\nisec2:");
        for( var r : isec2 ) System.out.println(r);

        Assertions.assertTrue(isec1.containsAll(isec2));
        Assertions.assertTrue(isec2.containsAll(isec1));
    }

    public static class RectMapPerf extends RectMap {
        public final Map<String,TCounter> counters = new TreeMap<>();

        public RectMapPerf( Rect bounds, Size2D cell ){
            this(bounds,cell,10000);
        }
        public RectMapPerf( Rect bounds, Size2D cell, int cntLen ){
            super(bounds, cell);

            cellOfCnt = new TCounter(cntLen);
            counters.put("cellOf",cellOfCnt);

            cellsOfCnt = new TCounter(cntLen);
            counters.put("cellsOf",cellsOfCnt);

            itemsCnt = new TCounter(cntLen);
            counters.put("items",itemsCnt);

            collisionsCnt = new TCounter(cntLen);
            counters.put("collisions",collisionsCnt);

            putCnt = new TCounter(cntLen);
            counters.put("put",putCnt);
        }

        public final TCounter cellOfCnt;

        @Override
        public Optional<Cell> cellOf( Point pt, boolean fullInc ){
            return cellOfCnt.track( ()->super.cellOf(pt, fullInc) );
        }

        public final TCounter cellsOfCnt;

        @Override
        public Optional<Cells> cellsOf( Rect rect ){
            return cellsOfCnt.track( ()->super.cellsOf(rect) );
        }

        public final TCounter itemsCnt;

        @Override
        public Map<Rect, Set<Cell>> items(){
            return itemsCnt.track(super::items);
        }

        public final TCounter collisionsCnt;

        @Override
        public List<Collision> collisions( Rect rect ){
            return collisionsCnt.track( ()->super.collisions(rect) );
        }

        public final TCounter putCnt;

        @Override
        public void put( Iterable<Rect> rects ){
            putCnt.track( ()->super.put(rects) );
        }

        public final TCounter cells_collisions = new TCounter(1000);
        public final TCounter cells_items = new TCounter(1000);
        public final TCounter cells_collision = new TCounter(1000);
        {
            counters.put("cells_collisions",cells_collisions);
            counters.put("cells_items",cells_items);
            counters.put("cells_collision",cells_collision);
        }

        public class CellsPerf extends Cells {
            public CellsPerf( List<Cell> cells ){
                super(cells);
            }

            @Override
            public List<Collision> collisions( Rect rect ){
                return cells_collisions.track( ()->super.collisions(rect) );
            }

            @Override
            protected void collision( List<Collision> lst, Rect rect, Rect itm ){
                cells_collision.track( ()->super.collision(lst, rect, itm) );
            }

            @Override
            public Set<Rect> items(){
                return cells_items.track(super::items);
            }
        }

        @Override
        protected Cells Cells( List<Cell> cells ){
            return new CellsPerf(cells);
        }
    }

    @Tag(T.Perf)  @Test
    public void intersection_perf(){
        double bounds_size = 512;
        bounds_size = bounds_size * 8;
        Rect bounds = Rect.of(0,0,bounds_size,bounds_size);

        Size2D minGenSize = Size2D.of(8,8);
        Size2D maxGenSize = Size2D.of(64,64);

        Size2D minSize = Size2D.of(16,16);
        Size2D maxSize = Size2D.of(64,64);
        for( int i=50; i<=500; i+=50 ){
            System.out.println("generate i="+i+" i*i="+(i*i));
            var rects = generate(i*i,minGenSize, maxGenSize, bounds);

            int rect_cell_size = 64;
            var rm = new RectMapPerf(bounds, Size2D.of(rect_cell_size,rect_cell_size));
            rm.put(rects);

            TCounter cnt = new TCounter(100);
            Random rnd = new Random();

            for( int j=0; j<500; j++ ){
                double w = minSize.width() + Math.abs(rnd.nextDouble())*(maxSize.width()-minSize.width());
                double h = minSize.height() + Math.abs(rnd.nextDouble())*(maxSize.height()-minSize.height());
                double x = Math.abs(rnd.nextDouble())*(bounds.width()-w) + bounds.left();
                double y = Math.abs(rnd.nextDouble())*(bounds.height()-h) + bounds.top();
                Rect rect = Rect.of(x,y,w,h);

                cnt.start();
                //computeCollisions(rects, rect);
                rm.collisions(rect);
                cnt.stop();
            }

            System.out.println("stat");

            int a_len = rm.counters.keySet().stream().mapToInt(String::length).max().getAsInt()+2;

            rm.counters.forEach( (name,tcnt)->{
                System.out.println(
                    Text.align(name, Align.End, " ", a_len)+
                    " "+tcnt.echo().toString());
            });

            var ser = new Series();
            rm.cells( cell -> {
                ser.add(cell.count());
            });

            System.out.println(
                Text.align("rects per cell",Align.End," ",a_len)+
                    " avg="+ser.avg().orElse(null)
            );
        }
    }
}
