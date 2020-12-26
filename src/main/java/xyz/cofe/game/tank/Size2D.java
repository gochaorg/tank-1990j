package xyz.cofe.game.tank;

public interface Size2D {
    double width();
    double height();
    public static Size2D of(double width, double height ){
        return new Size2D() {
            @Override
            public double width(){
                return width;
            }

            @Override
            public double height(){
                return height;
            }
        };
    }
}
