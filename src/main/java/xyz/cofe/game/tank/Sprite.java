package xyz.cofe.game.tank;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * Спрайт / кадр
 */
public class Sprite implements PositionalDrawing {
    /**
     * Конструктор
     * @param image кадр
     */
    public Sprite(BufferedImage image){
        if( image==null )throw new IllegalArgumentException( "image==null" );
        this.image = image;
    }

    /**
     * кадр
     */
    protected final BufferedImage image;

    /**
     * Кадр
     * @return кадр
     */
    public BufferedImage image(){ return image; }

    //region size : Size2D - размеры кадра
    private Size2D size;

    /**
     * Размеры кадра
     * @return размеры кадра
     */
    public Size2D size(){
        if( size!=null )return size;
        size = Size2D.of( image.getWidth(), image.getHeight() );
        return size;
    }
    //endregion
    //region bounds() : Rect - границы объекта, границы не пустых пикселей
    private Rect bounds;

    /**
     * Возвращает границы объекта, границы не пустых пикселей
     * @return границы объекта
     */
    public Rect bounds(){
        if( bounds!=null )return bounds;

        WritableRaster raster = image.getRaster();
        List<Integer> linesVert_xAxis = nonTransparentVert(raster);
        List<Integer> linesHorz_yAxis = nonTransparentHoriz(raster);

        int x0 = 0;
        int y0 = 0;
        int x1 = image.getWidth();
        int y1 = image.getHeight();

        if( linesVert_xAxis.size()>0 ){
            x0 = linesVert_xAxis.get(0);
            x1 = linesVert_xAxis.get(linesHorz_yAxis.size()-1);
        }

        if( linesHorz_yAxis.size()>0 ){
            y0 = linesHorz_yAxis.get(0);
            y1 = linesHorz_yAxis.get(linesHorz_yAxis.size()-1);
        }

        bounds = Rect.rect(x0,y0,x1,y1);
        return bounds;
    }

    /**
     * Возвращает индексы вертикальных линий (ось X) чьи значение alpha канала отличны от 0
     * @param raster изображение
     * @return индексы верт. линий (ось X)
     */
    private List<Integer> nonTransparentVert(WritableRaster raster){
        List<Integer> lines = new ArrayList<>();
        int a = 3;

        int w = raster.getWidth();

        AtomicLong alphaSum = new AtomicLong();
        for( int x=0; x<w; x++ ){
            alphaSum.set(0);
            vertPixels(raster, x, rgba -> alphaSum.addAndGet(rgba[a]));
            if( alphaSum.get()>0 ){
                lines.add(x);
            }
        }

        return lines;
    }

    /**
     * Возвращает индексы горизонтальных линий (ось Y) чьи значение alpha канала отличны от 0
     * @param raster изображение
     * @return индексы горизонтальных линий (ось Y)
     */
    private List<Integer> nonTransparentHoriz(WritableRaster raster){
        List<Integer> lines = new ArrayList<>();
        int a = 3;

        int h = raster.getHeight();

        AtomicLong alphaSum = new AtomicLong();
        for( int y=0; y<h; y++ ){
            alphaSum.set(0);
            horizPixels(raster, y, rgba -> alphaSum.addAndGet(rgba[a]));
            if( alphaSum.get()>0 ){
                lines.add(y);
            }
        }

        return lines;
    }
    private void horizPixels(WritableRaster raster, int y, Consumer<int[]> pixel){
        int w = raster.getWidth();
        int[] pixelData = new int[4];
        for( int x=0; x<w; x++ ){
            pixelData = raster.getPixel(x,y,pixelData);
            pixel.accept(pixelData);
        }
    }
    private void vertPixels(WritableRaster raster, int x, Consumer<int[]> pixel){
        int h = raster.getHeight();
        int[] pixelData = new int[4];
        for( int y=0; y<h; y++ ){
            pixelData = raster.getPixel(x,y,pixelData);
            pixel.accept(pixelData);
        }
    }
    //endregion

    /**
     * отрисовка спрайта
     * @param gs интерфейс
     * @param x координаты отображения
     * @param y координаты отображения
     */
    public void draw( Graphics2D gs, double x, double y ){
        if( gs==null )throw new IllegalArgumentException( "gs==null" );
        gs.drawImage(image, (int)x, (int)y, null);
    }
}
