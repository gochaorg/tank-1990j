package xyz.cofe.game.tank;

public interface Size2DInt extends Size2D<Integer> {
    public static Size2DInt of( int width, int height ){
        return new Size2DInt() {
            @Override
            public Integer width(){
                return width;
            }

            @Override
            public Integer height(){
                return height;
            }
        };
    }
}
