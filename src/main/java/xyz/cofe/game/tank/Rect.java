package xyz.cofe.game.tank;

import xyz.cofe.num.BaseNumbers;

public interface Rect<N extends Number> extends Size2D<N> {
    N left();
    N top();
    N right();
    N bottom();
    public static <N extends Number> Rect<N> rect( N left, N top, N right, N bottom ){
        if( left==null )throw new IllegalArgumentException( "left==null" );
        if( top==null )throw new IllegalArgumentException( "top==null" );
        if( right==null )throw new IllegalArgumentException( "right==null" );
        if( bottom==null )throw new IllegalArgumentException( "bottom==null" );

        var hnum = BaseNumbers.commonBase(left,right);
        var vnum = BaseNumbers.commonBase(top,bottom);

        var swapLeftRight = hnum.more();
        var swapTopDown = vnum.more();

        var x0 = swapLeftRight ? right : left;
        var x1 = swapLeftRight ? left : right;
        var y0 = swapTopDown ? bottom : top;
        var y1 = swapTopDown ? top : bottom;
        //noinspection unchecked
        N w = (N)hnum.sub(x1,x0);
        //noinspection unchecked
        N h = (N)hnum.sub(y1,y0);

        return new Rect<N>() {
            @Override
            public N left(){
                return x0;
            }

            @Override
            public N top(){
                return y0;
            }

            @Override
            public N right(){
                return x1;
            }

            @Override
            public N bottom(){
                return y1;
            }

            @Override
            public N width(){
                return w;
            }

            @Override
            public N height(){
                return h;
            }
        };
    }
    public static <N extends Number> Rect<N> of( N left, N top, N width, N height ){
        if( left==null )throw new IllegalArgumentException( "left==null" );
        if( top==null )throw new IllegalArgumentException( "top==null" );
        if( width==null )throw new IllegalArgumentException( "width==null" );
        if( height==null )throw new IllegalArgumentException( "height==null" );

        var base = BaseNumbers.commonBase(left,top);
        //noinspection unchecked
        var rgt = (N)base.add(left, width);
        //noinspection unchecked
        var btm = (N)base.add(top, height);

        return rect(left,top,rgt,btm);
    }
}
