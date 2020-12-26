package xyz.cofe.game.tank;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Данные о расположении спрайтов на картинке
 */
public class SpritesData {
    //region Информация о последовательности кадров: frames, duration, left, top, width, height, loop
    /** Кол-во кадров, кадры расположены смеженно, слева на право */
    public final int frames;

    /** Продолжительность */
    public final double duration;

    /** Левый угол первого кадра */
    public final int left;

    /** Верхний угол первого кадра */
    public final int top;

    /** Ширина спрайта */
    public final int width;

    /** Высота спрайта */
    public final int height;

    /** Цикличность */
    public final boolean loop;
    //endregion

    /**
     * Конструктор
     * @param fc Кол-во кадров, кадры расположены смеженно, слева на право
     * @param dur Продолжительность
     * @param lf Левый угол
     * @param tp Верхний угол
     * @param w Ширина спрайта
     * @param h Высота спрайта
     * @param l Цикличность кадров
     */
    public SpritesData(int fc, double dur, int lf, int tp, int w, int h, boolean l ){
        this.frames = fc;
        this.duration = dur;
        this.left = lf;
        this.top = tp;
        this.width = w;
        this.height = h;
        this.loop = l;
    }

    //region spritesData
    public static final SpritesData player_one_up_0 = new SpritesData( 2, 0.05, 0, 0, 32, 32, true );
    public static final SpritesData player_one_right_0 = new SpritesData( 2, 0.05, 0, 32, 32, 32, true );
    public static final SpritesData player_one_down_0 = new SpritesData( 2, 0.05, 0, 64, 32, 32, true );
    public static final SpritesData player_one_left_0 = new SpritesData( 2, 0.05, 0, 96, 32, 32, true );
    public static final SpritesData player_one_up_1 = new SpritesData( 2, 0.05, 64, 0, 32, 32, true );
    public static final SpritesData player_one_right_1 = new SpritesData( 2, 0.05, 64, 32, 32, 32, true );
    public static final SpritesData player_one_down_1 = new SpritesData( 2, 0.05, 64, 64, 32, 32, true );
    public static final SpritesData player_one_left_1 = new SpritesData( 2, 0.05, 64, 96, 32, 32, true );
    public static final SpritesData player_one_up_2 = new SpritesData( 2, 0.05, 128, 0, 32, 32, true );
    public static final SpritesData player_one_right_2 = new SpritesData( 2, 0.05, 128, 32, 32, 32, true );
    public static final SpritesData player_one_down_2 = new SpritesData( 2, 0.05, 128, 64, 32, 32, true );
    public static final SpritesData player_one_left_2 = new SpritesData( 2, 0.05, 128, 96, 32, 32, true );
    public static final SpritesData player_one_up_3 = new SpritesData( 2, 0.05, 192, 0, 32, 32, true );
    public static final SpritesData player_one_right_3 = new SpritesData( 2, 0.05, 192, 32, 32, 32, true );
    public static final SpritesData player_one_down_3 = new SpritesData( 2, 0.05, 192, 64, 32, 32, true );
    public static final SpritesData player_one_left_3 = new SpritesData( 2, 0.05, 192, 96, 32, 32, true );
    public static final SpritesData player_two_up_0 = new SpritesData( 2, 0.05, 0, 128, 32, 32, true );
    public static final SpritesData player_two_right_0 = new SpritesData( 2, 0.05, 0, 160, 32, 32, true );
    public static final SpritesData player_two_down_0 = new SpritesData( 2, 0.05, 0, 192, 32, 32, true );
    public static final SpritesData player_two_left_0 = new SpritesData( 2, 0.05, 0, 224, 32, 32, true );
    public static final SpritesData player_two_up_1 = new SpritesData( 2, 0.05, 64, 128, 32, 32, true );
    public static final SpritesData player_two_right_1 = new SpritesData( 2, 0.05, 64, 160, 32, 32, true );
    public static final SpritesData player_two_down_1 = new SpritesData( 2, 0.05, 64, 192, 32, 32, true );
    public static final SpritesData player_two_left_1 = new SpritesData( 2, 0.05, 64, 224, 32, 32, true );
    public static final SpritesData player_two_up_2 = new SpritesData( 2, 0.05, 128, 128, 32, 32, true );
    public static final SpritesData player_two_right_2 = new SpritesData( 2, 0.05, 128, 160, 32, 32, true );
    public static final SpritesData player_two_down_2 = new SpritesData( 2, 0.05, 128, 192, 32, 32, true );
    public static final SpritesData player_two_left_2 = new SpritesData( 2, 0.05, 128, 224, 32, 32, true );
    public static final SpritesData player_two_up_3 = new SpritesData( 2, 0.05, 192, 128, 32, 32, true );
    public static final SpritesData player_two_right_3 = new SpritesData( 2, 0.05, 192, 160, 32, 32, true );
    public static final SpritesData player_two_down_3 = new SpritesData( 2, 0.05, 192, 192, 32, 32, true );
    public static final SpritesData player_two_left_3 = new SpritesData( 2, 0.05, 192, 224, 32, 32, true );
    public static final SpritesData lvl_brick = new SpritesData( 2, 1.0, 0, 256, 16, 16, false );
    public static final SpritesData lvl_white = new SpritesData( 1, 1.0, 0, 272, 16, 16, false );
    public static final SpritesData lvl_slide = new SpritesData( 1, 1.0, 0, 288, 16, 16, false );
    public static final SpritesData lvl_bush = new SpritesData( 1, 1.0, 0, 304, 16, 16, false );
    public static final SpritesData lvl_water = new SpritesData( 1, 1.0, 0, 320, 16, 16, false );
    public static final SpritesData lvl_background = new SpritesData( 1, 1.0, 3, 346, 1, 1, false );
    public static final SpritesData lvl_eagle = new SpritesData( 2, 1.0, 0, 360, 32, 32, false );
    public static final SpritesData item_star = new SpritesData( 1, 0.15, 0, 392, 32, 32, false );
    public static final SpritesData item_life = new SpritesData( 1, 0.15, 32, 392, 32, 32, false );
    public static final SpritesData item_invincibility = new SpritesData( 1, 0.15, 64, 392, 32, 32, false );
    public static final SpritesData item_protect = new SpritesData( 1, 0.15, 96, 392, 32, 32, false );
    public static final SpritesData item_time = new SpritesData( 1, 0.15, 64, 360, 32, 32, false );
    public static final SpritesData item_granade = new SpritesData( 1, 0.15, 96, 360, 32, 32, false );
    public static final SpritesData enemy_slow_up = new SpritesData( 2, 0.15, 0, 424, 32, 32, true );
    public static final SpritesData enemy_slow_right = new SpritesData( 2, 0.15, 0, 456, 32, 32, true );
    public static final SpritesData enemy_slow_down = new SpritesData( 2, 0.15, 0, 488, 32, 32, true );
    public static final SpritesData enemy_slow_left = new SpritesData( 2, 0.15, 0, 520, 32, 32, true );
    public static final SpritesData enemy_slow_bonus_up = new SpritesData( 4, 0.1, 64, 424, 32, 32, true );
    public static final SpritesData enemy_slow_bonus_right = new SpritesData( 4, 0.1, 64, 456, 32, 32, true );
    public static final SpritesData enemy_slow_bonus_down = new SpritesData( 4, 0.15, 64, 488, 32, 32, true );
    public static final SpritesData enemy_slow_bonus_left = new SpritesData( 4, 0.15, 64, 520, 32, 32, true );
    public static final SpritesData enemy_fast_up = new SpritesData( 2, 0.15, 0, 552, 32, 32, true );
    public static final SpritesData enemy_fast_right = new SpritesData( 2, 0.15, 0, 584, 32, 32, true );
    public static final SpritesData enemy_fast_down = new SpritesData( 2, 0.15, 0, 616, 32, 32, true );
    public static final SpritesData enemy_fast_left = new SpritesData( 2, 0.15, 0, 648, 32, 32, true );
    public static final SpritesData enemy_fast_bonus_up = new SpritesData( 4, 0.15, 64, 552, 32, 32, true );
    public static final SpritesData enemy_fast_bonus_right = new SpritesData( 4, 0.15, 64, 584, 32, 32, true );
    public static final SpritesData enemy_fast_bonus_down = new SpritesData( 4, 0.15, 64, 616, 32, 32, true );
    public static final SpritesData enemy_fast_bonus_left = new SpritesData( 4, 0.15, 64, 648, 32, 32, true );
    public static final SpritesData enemy_medium_up = new SpritesData( 2, 0.15, 0, 680, 32, 32, true );
    public static final SpritesData enemy_medium_right = new SpritesData( 2, 0.15, 0, 712, 32, 32, true );
    public static final SpritesData enemy_medium_down = new SpritesData( 2, 0.15, 0, 744, 32, 32, true );
    public static final SpritesData enemy_medium_left = new SpritesData( 2, 0.15, 0, 776, 32, 32, true );
    public static final SpritesData enemy_medium_bonus_up = new SpritesData( 4, 0.15, 64, 680, 32, 32, true );
    public static final SpritesData enemy_medium_bonus_right = new SpritesData( 4, 0.15, 64, 712, 32, 32, true );
    public static final SpritesData enemy_medium_bonus_down = new SpritesData( 4, 0.15, 64, 744, 32, 32, true );
    public static final SpritesData enemy_medium_bonus_left = new SpritesData( 4, 0.15, 64, 776, 32, 32, true );
    public static final SpritesData enemy_big_up = new SpritesData( 2, 0.15, 0, 808, 32, 32, true );
    public static final SpritesData enemy_big_right = new SpritesData( 2, 0.15, 0, 840, 32, 32, true );
    public static final SpritesData enemy_big_down = new SpritesData( 2, 0.15, 0, 872, 32, 32, true );
    public static final SpritesData enemy_big_left = new SpritesData( 2, 0.15, 0, 904, 32, 32, true );
    public static final SpritesData enemy_big_bonus_up = new SpritesData( 4, 0.15, 64, 808, 32, 32, true );
    public static final SpritesData enemy_big_bonus_right = new SpritesData( 4, 0.15, 64, 840, 32, 32, true );
    public static final SpritesData enemy_big_bonus_down = new SpritesData( 4, 0.15, 64, 872, 32, 32, true );
    public static final SpritesData enemy_big_bonus_left = new SpritesData( 4, 0.15, 64, 904, 32, 32, true );
    public static final SpritesData effect_spawn = new SpritesData( 6, 0.05, 256, 32, 32, 32, true );
    public static final SpritesData effect_explode = new SpritesData( 3, 0.05, 256, 64, 32, 32, true );
    public static final SpritesData bullet = new SpritesData( 4, 1.0, 0, 352, 8, 8, false );
    public static final SpritesData editor = new SpritesData( 1, 1.0, 462, 0, 50, 223, false );
    public static final SpritesData menu_logo = new SpritesData( 1, 1.0, 135, 273, 376, 136, false );
    public static final SpritesData menu_options = new SpritesData( 1, 1.0, 324, 422, 188, 78, false );
    public static final SpritesData player_border = new SpritesData( 2, 0.05, 256, 0, 32, 32, true );
    public static final SpritesData player_stat = new SpritesData( 1, 1.0, 32, 272, 16, 16, false );
    public static final SpritesData enemy_stat = new SpritesData( 1, 1.0, 48, 272, 16, 16, false );
    public static final SpritesData flag = new SpritesData( 1, 1.0, 64, 272, 32, 32, false );
    public static final SpritesData chars = new SpritesData( 26, 1.0, 0, 992, 16, 16, false );
    public static final SpritesData signs = new SpritesData( 18, 1.0, 0, 1008, 16, 16, false );
    //endregion

    //region common image
    private static final URL spritesCommonImageUrl = SpritesData.class.getResource("/sprite.bmp");
    private static final BufferedImage spritesCommonImage;
    static {
        BufferedImage img;
        try {
            img = ImageIO.read(spritesCommonImageUrl);
        } catch ( IOException e) {
            e.printStackTrace();
            img = null;
        }
        spritesCommonImage = img;
    }
    //endregion

    //region images() : BufferedImage[] - кадры
    private volatile boolean spriteImagesReaded = false;
    private final List<BufferedImage> spriteImages = new ArrayList<>();

    /**
     * Спрайты
     * @return спрайты
     */
    public List<BufferedImage> images(){
        if( spriteImagesReaded )return spriteImages;
        synchronized( this ){
            if( spriteImagesReaded )return spriteImages;
            for( int frame=0; frame<frames; frame++ ){
                spriteImages.add(image(frame));
            }
            spriteImagesReaded = true;
            return spriteImages;
        }
    }

    private BufferedImage image( int frame ){
        var image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        var gs = image.createGraphics();
        gs.drawImage(spritesCommonImage,
            0,0, width, height,
            left + width*frame, top,
            left + width + width*frame, top + height,
            null
        );
        gs.dispose();

        var rastr = image.getRaster();
        int[] rgba = new int[4];
        int r = 0;
        int g = 1;
        int b = 2;
        int a = 3;
        for( int y = 0; y < rastr.getHeight(); y++ ){
            for( int x = 0; x < rastr.getWidth(); x++ ){
                rgba = rastr.getPixel(x, y, rgba);
                if( rgba[r] == 0 && rgba[g] == 0 && rgba[b] == 0 ){
                    rgba[a] = 0;
                    rastr.setPixel(x, y, rgba);
                }
            }
        }

        return image;
    }
    //endregion
}
