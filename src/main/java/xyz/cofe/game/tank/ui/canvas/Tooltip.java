package xyz.cofe.game.tank.ui.canvas;

import xyz.cofe.game.tank.Drawing;
import xyz.cofe.game.tank.geom.Point;
import xyz.cofe.game.tank.geom.Size2D;
import xyz.cofe.game.tank.ui.text.TextBlock;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Tooltip extends CanvasHost<Tooltip> implements Drawing {
    public Tooltip(){}
    public Tooltip(String text){
        setText(text);
    }

    public Tooltip configure(Consumer<Tooltip> conf){
        if( conf==null )throw new IllegalArgumentException( "conf==null" );
        conf.accept(this);
        return this;
    }

    //region text : String
    private Supplier<String> text;
    public String getText() {
        return text!=null ? text.get() : null;
    }
    public void setText(String text) {
        this.text = ()->text;
    }
    public void setText( Supplier<String> text ){
        this.text = text;
    }
    public Tooltip text( Supplier<String> text ){
        setText(text);
        return this;
    }
    //endregion
    //region background : Color
    private Color background = Color.orange;
    public Color getBackground() {
        return background;
    }
    public void setBackground(Color background) {
        this.background = background;
    }
    //endregion
    //region foreground : Color
    private Color foreground = Color.black;
    public Color getForeground() {
        return foreground;
    }
    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }
    //endregion
    //region font : Font
    private Font font;
    public Font getFont() {
        if( font==null )font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        return font;
    }
    public void setFont(Font font) {
        this.font = font;
    }
    //endregion

    //region location
    private Function<Tooltip, Point> location;
    public Function<Tooltip, Point> location(){ return location; }
    public Tooltip location( Function<Tooltip, Point> location ){
        this.location = location;
        return this;
    }
    public Tooltip location( double x, double y ){
        this.location = t -> Point.of(x,y);
        return this;
    }
    public Tooltip relative(CrosshairCursor cc, double x, double y ){
        if( cc==null )throw new IllegalArgumentException( "cc==null" );
        location = t -> cc.location().map( p -> {
            if( x==0 && y==0 )return p;
            if( x>=0 && y>=0 ) {
                var p2 = p.translate(Point.of(x, y));
                return p2;
            }
            var tbOpt = textBlock();
            if( tbOpt.isPresent() ) {
                var bnd = tbOpt.get().bounds().orElse(null);
                if( bnd!=null ) {
                    if (x < 0 && y >= 0) {
                        double w = bnd.width();
                        return Point.of( p.x() - w + x, p.y() + y );
                    }
                    if( x < 0 && y < 0 ){
                        double w = bnd.width();
                        double h = bnd.height();
                        return Point.of( p.x() - w + x, p.y() + y - h );
                    }
                    if( x >= 0 && y < 0 ){
                        double w = bnd.width();
                        double h = bnd.height();
                        return Point.of( p.x() + x, p.y() + y - h );
                    }
                }
            }
            return p;
        }).orElse(Point.of(0,0));
        return this;
    }
    public Tooltip right( double rightMargin ){
        var prev = location;
        location = self -> {
            var prevLoc = prev!=null ? prev.apply(self) : Point.of(0,0);
            var y = prevLoc.y();
            var x = prevLoc.x();

            var pt = Point.of(x,y);

            var cmpt = component();
            if( cmpt!=null ){
                pt = textBlock().flatMap( tb -> {
                    return tb.bounds().map(Size2D::width).map( tbW -> cmpt.getWidth() - tbW - rightMargin );
                }).map( newX -> Point.of(newX,y)).orElse(pt);
            }

            return pt;
        };
        return this;
    }
    public Tooltip bottom( double bottomMargin ){
        var prev = location;
        location = self -> {
            var prevLoc = prev!=null ? prev.apply(self) : Point.of(0,0);
            var y = prevLoc.y();
            var x = prevLoc.x();

            var pt = Point.of(x,y);

            var cmpt = component();
            if( cmpt!=null ){
                pt = textBlock().flatMap( tb -> {
                    return tb.bounds().map(Size2D::height).map( tbH -> cmpt.getHeight() - tbH - bottomMargin );
                }).map( newY -> Point.of(x,newY)).orElse(pt);
            }

            return pt;
        };
        return this;
    }
    public Tooltip right( Tooltip from, double rightMargin ){
        if( from==null )throw new IllegalArgumentException( "from==null" );
        var prev = location;

        location = self -> {
            var prevLoc = prev!=null ? prev.apply(self) : Point.of(0,0);
            var y = prevLoc.y();
            var x = prevLoc.x();
            var pt = Point.of(x,y);

            var fromPt = from.location().apply(from);

            pt = textBlock().flatMap( tb -> {
                return tb.bounds().map(Size2D::width).map( tbW -> fromPt.x() - tbW - rightMargin );
            }).map( newX -> Point.of(newX,y)).orElse(pt);

            return pt;
        };

        return this;
    };
    public Tooltip bottom( Tooltip from, double bottomMargin ){
        if( from==null )throw new IllegalArgumentException( "from==null" );

        var prev = location;
        location = self -> {
            var prevLoc = prev!=null ? prev.apply(self) : Point.of(0,0);
            var y = prevLoc.y();
            var x = prevLoc.x();

            var fromPt = from.location().apply(from);

            var pt = Point.of(x,y);

            pt = textBlock().flatMap( tb -> {
                return tb.bounds().map(Size2D::height).map( tbH -> fromPt.y() - tbH - bottomMargin );
            }).map( newY -> Point.of(x,newY)).orElse(pt);

            return pt;
        };
        return this;
    };
    public Tooltip relative( Tooltip from, Function<Point,Point> offset ){
        if( from==null )throw new IllegalArgumentException( "from==null" );
        if( offset==null )throw new IllegalArgumentException( "offset==null" );
        location = self -> {
            var fromPt = from.location().apply(from);
            return offset.apply(fromPt);
        };
        return this;
    }
    //endregion

    private TextBlock textBlock;
    public Optional<TextBlock> textBlock(){
        return textBlock!=null
            ? Optional.of(textBlock)
            : Optional.empty();
    }

    @Override
    public void draw(Graphics2D gs) {
        if( gs==null )return;

        var str = getText();
        if( str==null )return;

        var fnt = getFont();
        if( fnt==null )return;

        Color bg = getBackground();
        Color fg = getForeground();
        if( fg==null )return;

        gs.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        gs.setFont(fnt);
        if( bg==null ){
            gs.setPaint(fg);
            var tb = TextBlock.textBlock(str,gs,fnt);
            textBlock = tb;
            if( location!=null ){
                tb.location(location.apply(this));
            }
            tb.draw(gs);
        }else {
            var tb = TextBlock.textBlock(str,gs,fnt);
            textBlock = tb;
            if( location!=null ){
                tb.location(location.apply(this));
            }

            gs.setPaint(bg);
            tb.bounds2d().ifPresent(gs::fill);

            gs.setPaint(fg);
            tb.draw(gs);
        }
    }
}
