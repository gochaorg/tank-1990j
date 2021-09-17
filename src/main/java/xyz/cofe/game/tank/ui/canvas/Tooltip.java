package xyz.cofe.game.tank.ui.canvas;

import xyz.cofe.game.tank.Drawing;
import xyz.cofe.game.tank.geom.Point;
import xyz.cofe.game.tank.geom.Rect;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
    //region text block
    private static class TextLine {
        public String text;
        public Rectangle2D bounds;
        public LineMetrics lineMetrics;
        public double x = 0;
        public double y = 0;
        public double y0(){
            return y - bounds.getHeight();
        }
    }
    private static List<TextLine> textLines( String text, Graphics2D gs, Font font ){
        var frctx = gs.getFontRenderContext();
        var lines =
        Arrays.stream(text.split("\r?\n")).map(txt -> {
            var tline = new TextLine();
            tline.text = txt;
            tline.bounds = font.getStringBounds(txt,frctx);
            tline.lineMetrics = font.getLineMetrics(txt,frctx);
            return tline;
        }).collect(Collectors.toList());

        double y = 0;
        for( var line : lines ){
            line.y = line.lineMetrics.getAscent() + y;
            y += line.lineMetrics.getHeight();
            line.x = 0;
        }

        return lines;
    }
    private static class TextBlock implements Drawing {
        public List<TextLine> textLines;
        public TextBlock location( double x, double y ){
            if( textLines!=null ){
                for( var line : textLines ){
                    line.x = x;
                    line.y = y + line.lineMetrics.getAscent();
                    y += line.lineMetrics.getHeight();
                }
            }
            return this;
        }
        public TextBlock location(Point pt){
            if( pt==null )throw new IllegalArgumentException( "pt==null" );
            return location(pt.x(), pt.y());
        }

        @Override
        public void draw(Graphics2D gs) {
            if( gs==null )return;
            for( var line : textLines ){
                gs.drawString(line.text, (float) line.x, (float) line.y);
            }
        }

        @SuppressWarnings("OptionalGetWithoutIsPresent")
        public Optional<Rect> bounds(){
            double minX = Double.NaN;
            double minY = Double.NaN;
            double maxX = Double.NaN;
            double maxY = Double.NaN;

            if( textLines==null || textLines.isEmpty() )return Optional.empty();
            minX = textLines.stream().map( t -> List.of(t.x, t.x + t.bounds.getWidth()) )
                .flatMap(List::stream).mapToDouble(x->x).min().getAsDouble();
            minY = textLines.stream().map( t -> List.of(t.y0(), t.y0() + t.bounds.getHeight()) )
                .flatMap(List::stream).mapToDouble(x->x).min().getAsDouble();

            maxX = textLines.stream().map( t -> List.of(t.x, t.x + t.bounds.getWidth()) )
                .flatMap(List::stream).mapToDouble(x->x).max().getAsDouble();
            maxY = textLines.stream().map( t -> List.of(t.y0(), t.y0() + t.bounds.getHeight()) )
                .flatMap(List::stream).mapToDouble(x->x).max().getAsDouble();

            return Optional.of( Rect.rect(minX,minY, maxX, maxY) );
        }

        public Optional<Rectangle2D> bounds2d(){
            return bounds().map( b -> new Rectangle2D.Double( b.left(), b.top(), b.width(), b.height() ));
        }
    }
    private TextBlock textBlock( String text, Graphics2D gs, Font font ){
        var tb = new TextBlock();
        tb.textLines = textLines(text,gs,font);
        return tb;
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
            var tb = textBlock(str,gs,fnt);
            textBlock = tb;
            if( location!=null ){
                tb.location(location.apply(this));
            }
            tb.draw(gs);
        }else {
            var tb = textBlock(str,gs,fnt);
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
