package xyz.cofe.game.tank.ui.text;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Линия текста
 */
public class TextLine {
    /**
     * Текст
     */
    public String text;

    /**
     * Размеры рамки рендера текста
     */
    public Rectangle2D bounds;

    /**
     * Информация о рендере линии
     */
    public LineMetrics lineMetrics;

    /**
     * Расположение текста
     */
    public double x = 0;

    /**
     * Расположение текста
     */
    public double y = 0;

    /**
     * Верхний край рендера
     * @return Верхний край рендера
     */
    public double y0() {
        return y - bounds.getHeight();
    }

    /**
     * Создание списка линицй текста
     * @param text текст
     * @param gs контекст рендера
     * @param font шрифт
     * @return список линий
     */
    public static List<TextLine> textLines(String text, Graphics2D gs, Font font) {
        var frctx = gs.getFontRenderContext();
        var lines =
            Arrays.stream(text.split("\r?\n")).map(txt -> {
                var tline = new TextLine();
                tline.text = txt;
                tline.bounds = font.getStringBounds(txt, frctx);
                tline.lineMetrics = font.getLineMetrics(txt, frctx);
                return tline;
            }).collect(Collectors.toList());

        double y = 0;
        for (var line : lines) {
            line.y = line.lineMetrics.getAscent() + y;
            y += line.lineMetrics.getHeight();
            line.x = 0;
        }

        return lines;
    }
}
