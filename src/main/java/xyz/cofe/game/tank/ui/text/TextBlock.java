package xyz.cofe.game.tank.ui.text;

import xyz.cofe.game.tank.Drawing;
import xyz.cofe.game.tank.geom.Point;
import xyz.cofe.game.tank.geom.Rect;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Optional;

/**
 * Текстовый блок
 */
public class TextBlock implements Drawing {
    /**
     * Список текстовых линий
     */
    public List<TextLine> textLines;

    /**
     * Перемещение текстового блока
     * @param x координаты левого верхнего угла
     * @param y координаты левого верхнего угла
     * @return SELF ссылка
     */
    public TextBlock location(double x, double y) {
        if (textLines != null) {
            for (var line : textLines) {
                line.x = x;
                line.y = y + line.lineMetrics.getAscent();
                y += line.lineMetrics.getHeight();
            }
        }
        return this;
    }

    /**
     * Перемещение текстового блока
     * @param pt координаты левого верхнего угла
     * @return SELF ссылка
     */
    public TextBlock location(Point pt) {
        if (pt == null) throw new IllegalArgumentException("pt==null");
        return location(pt.x(), pt.y());
    }

    @Override
    public void draw(Graphics2D gs) {
        if (gs == null) return;
        for (var line : textLines) {
            if( line.font!=null )gs.setFont(line.font);
            gs.drawString(line.text, (float) line.x, (float) line.y);
        }
    }

    /**
     * Вычисление рамки блока
     * @return рамка
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Optional<Rect> bounds() {
        double minX = Double.NaN;
        double minY = Double.NaN;
        double maxX = Double.NaN;
        double maxY = Double.NaN;

        if (textLines == null || textLines.isEmpty()) return Optional.empty();
        minX = textLines.stream().map(t -> List.of(t.x, t.x + t.bounds.getWidth()))
            .flatMap(List::stream).mapToDouble(x -> x).min().getAsDouble();
        minY = textLines.stream().map(t -> List.of(t.y0(), t.y0() + t.bounds.getHeight()))
            .flatMap(List::stream).mapToDouble(x -> x).min().getAsDouble();

        maxX = textLines.stream().map(t -> List.of(t.x, t.x + t.bounds.getWidth()))
            .flatMap(List::stream).mapToDouble(x -> x).max().getAsDouble();
        maxY = textLines.stream().map(t -> List.of(t.y0(), t.y0() + t.bounds.getHeight()))
            .flatMap(List::stream).mapToDouble(x -> x).max().getAsDouble();

        return Optional.of(Rect.rect(minX, minY, maxX, maxY));
    }

    /**
     * Вычисление рамки блока
     * @return рамка
     */
    public Optional<Rectangle2D> bounds2d() {
        return bounds().map(b -> new Rectangle2D.Double(b.left(), b.top(), b.width(), b.height()));
    }

    /**
     * Создание текстового блока
     * @param text текст
     * @param gs контекст
     * @param font шрифт
     * @return текстовый блок
     */
    public static TextBlock textBlock(String text, Graphics2D gs, Font font) {
        if( text==null )throw new IllegalArgumentException( "text==null" );
        if( gs==null )throw new IllegalArgumentException( "gs==null" );
        if( font==null )throw new IllegalArgumentException( "font==null" );
        var tb = new TextBlock();
        tb.textLines = TextLine.textLines(text, gs, font);
        return tb;
    }
}
