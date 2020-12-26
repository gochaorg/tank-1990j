package xyz.cofe.game.tank;

public interface Size2D<N extends Number> {
    N width();
    N height();
    public static <N extends Number> Size2D<N> of(N width, N height ){
        if( width==null )throw new IllegalArgumentException( "width==null" );
        if( height==null )throw new IllegalArgumentException( "height==null" );
        return new Size2D<N>() {
            @Override
            public N width(){
                return width;
            }

            @Override
            public N height(){
                return height;
            }
        };
    }
}
