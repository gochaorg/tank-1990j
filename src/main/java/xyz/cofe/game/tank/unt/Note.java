package xyz.cofe.game.tank.unt;

import xyz.cofe.fn.Tuple2;
import xyz.cofe.game.tank.Observers;
import xyz.cofe.game.tank.geom.Size2D;
import xyz.cofe.game.tank.ui.text.TextBlock;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.List;

/**
 * Текстовая заметка
 */
public class Note extends Figure<Note> {
    public final Observers<Size2D> onSizeChanged = new Observers<>();

    //region constructors
    /**
     * Конструктор
     */
    public Note(){}

    /**
     * Конструктор копирования
     * @param sample1 образец
     */
    public Note(Figure<?> sample1){
        super(sample1);
        if( sample1 instanceof Note ){
            Note sample = (Note) sample1;

            width = sample.width;
            height = sample.height;
            s1 = sample.s1;
            outlineColor = sample.outlineColor;
            fillColor = sample.fillColor;
            outlineShapeCached = sample.outlineShapeCached;
            fillShapeCached = sample.fillShapeCached;
            outlineStroke = sample.outlineStroke;

            title = sample.title;
            titleColor = sample.titleColor;
            content = sample.content;
            fontTitle = sample.fontTitle;
        }
    }

    /**
     * Конструктор копирования
     * @param sample образец
     */
    public Note(Note sample){
        super(sample);
        width = sample.width;
        height = sample.height;
        s1 = sample.s1;
        outlineColor = sample.outlineColor;
        fillColor = sample.fillColor;
        outlineShapeCached = sample.outlineShapeCached;
        fillShapeCached = sample.fillShapeCached;
        outlineStroke = sample.outlineStroke;

        title = sample.title;
        titleColor = sample.titleColor;
        content = sample.content;
        fontTitle = sample.fontTitle;

        contentTextBlock = sample.contentTextBlock;
        contentTopMargin = sample.contentTopMargin;
    }

    /**
     * Клонирование объекта
     * @return клон
     */
    @Override
    public Note clone() {
        return new Note(this);
    }
    //endregion

    //region width : double
    private double width = 32;
    @Override
    public double width() {
        return width;
    }
    public Note width(double v){
        this.width = v;
        onSizeChanged.fire(Size2D.of(width, height));
        return this;
    }
    public double getWidth(){ return width; }
    public void setWidth(double v){
        width(v);
    }
    //endregion
    //region height : double
    private double height = 32;

    @Override
    public double height() {
        return height;
    }
    public Note height(double v){
        height = v;
        onSizeChanged.fire(Size2D.of(width, height));
        return this;
    }
    public double getHeight(){ return height; }
    public void setHeight(double v){ height(v); }
    //endregion

    //region shapes
    private double s1 = 0.3;
    private BasicStroke outlineStroke = new BasicStroke(2f);
    private Shape outlineShape(){
        Area area = new Area();

        double minl = Math.min(width,height);
        double s1 = minl * this.s1;

        GeneralPath gp = new GeneralPath();
        gp.moveTo(0,0);
        gp.lineTo(width,0);
        gp.lineTo(width,height-s1);
        gp.lineTo(width-s1,height-s1);
        gp.lineTo(width-s1,height);
        gp.lineTo(0,height);
        gp.closePath();

        area.add(new Area(outlineStroke.createStrokedShape(gp)));

        gp = new GeneralPath();
        gp.moveTo(width,height-s1);
        gp.lineTo(width-s1,height-s1);
        gp.lineTo(width-s1,height);
        gp.closePath();

        area.add(new Area(outlineStroke.createStrokedShape(gp)));

        return area;
    }
    private Shape fillShape(){
        double minl = Math.min(width,height);
        double s1 = minl * this.s1;

        GeneralPath gp = new GeneralPath();
        gp.moveTo(0,0);
        gp.lineTo(width,0);
        gp.lineTo(width,height-s1);
        gp.lineTo(width-s1,height);
        gp.lineTo(0,height);
        gp.closePath();

        return gp;
    }

    private Shape outlineShapeCached;
    private Shape outlineShapeCached(){
        if( outlineShapeCached!=null )return outlineShapeCached;
        outlineShapeCached = outlineShape();
        return outlineShapeCached;
    }

    private Shape fillShapeCached;
    private Shape fillShapeCached(){
        if( fillShapeCached!=null )return fillShapeCached;
        fillShapeCached = fillShape();
        return fillShapeCached;
    }

    {
        onSizeChanged.listen(ev -> {
            fillShapeCached = null;
            outlineShapeCached = null;
        });
    }
    //endregion

    //region outlineColor : Color
    private Color outlineColor = Color.black;
    public Color getOutlineColor(){
        if( outlineColor==null ){
            return Color.black;
        }
        return outlineColor;
    }
    public void setOutlineColor(Color color){
        this.outlineColor = color;
    }
    //endregion
    //region fillColor : Color
    private Color fillColor = Color.orange;
    public Color getFillColor(){
        if( fillColor==null )return Color.orange;
        return fillColor;
    }
    public void setFillColor(Color color){
        fillColor = color;
    }
    //endregion

    //region title : String
    private String title;
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    //endregion
    //region titleColor : Color
    private Color titleColor = Color.black;
    public Color getTitleColor(){ return titleColor; }
    public void setTitleColor(Color color){
        titleColor = color;
    }
    //endregion
    //region fontTitle : Font
    public final Observers<Tuple2<Font,Font>> onFontTitleChanged = new Observers<>();
    private Font fontTitle = new Font(Font.SANS_SERIF, Font.PLAIN,12);
    public Font getFontTitle(){
        if( fontTitle ==null ){
            fontTitle = new Font(Font.SANS_SERIF, Font.PLAIN,12);
        }
        return fontTitle;
    }
    public void setFontTitle(Font fontTitle){
        if( fontTitle ==null )throw new IllegalArgumentException( "font==null" );
        var old = this.fontTitle;
        this.fontTitle = fontTitle;
        onFontTitleChanged.fire(Tuple2.of(old, fontTitle));
    }
    //endregion

    //region content : String
    public final Observers<Tuple2<String,String>> onContentChanged = new Observers<>();
    private String content;
    public String getContent(){ return content; }
    public void setContent(String txt){
        var prev = content;
        content = txt;
        if( !Objects.equals(txt,prev) ){
            onContentChanged.fire(Tuple2.of(prev,content));
        }
    }
    //endregion
    //region fontTitle : Font
    public final Observers<Tuple2<Font,Font>> onFontContentChanged = new Observers<>();
    private Font fontContent = new Font(Font.SANS_SERIF, Font.PLAIN,12);
    public Font getFontContent(){
        if( fontContent ==null ){
            fontContent = new Font(Font.SANS_SERIF, Font.PLAIN,12);
        }
        return fontContent;
    }
    public void setFontContent(Font font){
        if( font ==null )throw new IllegalArgumentException( "font==null" );
        var old = this.fontContent;
        this.fontContent = font;
        onFontContentChanged.fire(Tuple2.of(old, font));
    }
    //endregion
    //region contentColor : Color
    private Color contentColor = Color.black;
    public Color getContentColor(){ return contentColor; }
    public void setContentColor(Color color){
        contentColor = color;
    }
    //endregion

    //region contentVisible : boolean
    private boolean contentVisible = false;
    public boolean isContentVisible(){ return contentVisible; }
    public void setContentVisible(boolean visible){
        contentVisible = visible;
    }
    //endregion
    //region contentTopMargin : double
    protected double contentTopMargin=0;

    public double getContentTopMargin() {
        return contentTopMargin;
    }

    public void setContentTopMargin(double contentTopMargin) {
        this.contentTopMargin = contentTopMargin;
    }
    //endregion

    //region draw()
    private transient TextBlock contentTextBlock;
    {
        onFontContentChanged.listen(e -> contentTextBlock = null );
        onContentChanged.listen( e -> contentTextBlock = null );
    }

    private TextBlock contentTextBlock(Graphics2D gs, Font font){
        if( contentTextBlock!=null )return contentTextBlock;

        String content = getContent();
        if( content==null || content.trim().length()<1 || gs==null || font==null )return null;

        contentTextBlock = TextBlock.textBlock(content,gs,font);
        return contentTextBlock;
    }

    @Override
    public void draw(Graphics2D gs) {
        if( gs==null )return;

        AffineTransform atSaved = (AffineTransform) gs.getTransform().clone();

        var loc = getLocation();
        gs.translate(loc.x(), loc.y());

        var fillShape = fillShapeCached();
        var fillColor = this.getFillColor();
        if( fillColor!=null && fillShape!=null ){
            gs.setPaint(fillColor);
            gs.fill(fillShape);
        }

        var outlineShape = outlineShapeCached();
        var outlineColor = getOutlineColor();
        if( outlineColor!=null && outlineShape!=null ){
            gs.setPaint(outlineColor);
            gs.fill(outlineShape);
        }

        gs.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        var ftitle = getFontTitle();
        var title = getTitle();
        var ctitle = getTitleColor();
        var textY = 0;
        var textX = 2;
        if( ctitle!=null && title!=null && title.length()>0 && ftitle!=null ){
            var fm = gs.getFontMetrics(ftitle);
            var lm = fm.getLineMetrics(title,gs);
            gs.setPaint(ctitle);
            gs.setFont(ftitle);
            gs.drawString( title, textX, textY + lm.getAscent() );
            textY += lm.getHeight();
        }

        var fcontent = getFontContent();
        var ccontent = getContentColor();
        if( contentVisible && content!=null && content.length()>0 && fcontent!=null && ccontent!=null ){
            //gs.setFont(fcontent);
            gs.setPaint(ccontent);
            var tb = contentTextBlock(gs,fcontent);
            if( tb!=null ){
                tb.location(textX, textY+contentTopMargin);
                tb.draw(gs);
            }
        }

        gs.setTransform(atSaved);
    }
    //endregion

    public static List<Note> filter( Scene scene, Predicate<Note> filter ){
        if( scene==null )throw new IllegalArgumentException( "scene==null" );
        if( filter==null )throw new IllegalArgumentException( "filter==null" );
        var res = new ArrayList<Note>();
        for( var fig : scene.getFigures() ){
            if( fig instanceof Note ){
                var n = (Note)fig;
                if( filter.test(n) ){
                    res.add(n);
                }
            }
        }
        return res;
    }

    public static Optional<Note> findByTitle( Scene scene, Predicate<String> filter ){
        if( scene==null )throw new IllegalArgumentException( "scene==null" );
        if( filter==null )throw new IllegalArgumentException( "filter==null" );
        for( var fig : scene.getFigures() ){
            if( fig instanceof Note ){
                var n = (Note)fig;
                if( filter.test(n.getTitle()) ){
                    return Optional.of(n);
                }
            }
        }
        return Optional.empty();
    }
    public static Optional<Note> findByTitle( Scene scene, String filter ){
        return findByTitle(scene, title -> Objects.equals(title,filter));
    }
}
