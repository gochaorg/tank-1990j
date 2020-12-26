package xyz.cofe.game.tank;

public interface RectInt extends Rect<Integer> {
    public static RectInt rect(int left, int top, int right, int bottom ){
        int x0 = Math.min(left,right);
        int x1 = Math.max(left,right);
        int y0 = Math.min(top,bottom);
        int y1 = Math.max(top,bottom);
        int w = Math.abs(left-right);
        int h = Math.abs(top-bottom);
        return new RectInt() {
            @Override public Integer left(){ return x0; }
            @Override public Integer top(){ return y0; }
            @Override public Integer right(){ return x1; }
            @Override public Integer bottom(){ return y1; }
            @Override public Integer width(){ return w; }
            @Override public Integer height(){ return h; }
        };
    }
    public static RectInt of( int left, int top, int width, int height ){
        return rect(left, top, left+width, top+height);
    }
}
