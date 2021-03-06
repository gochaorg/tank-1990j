package xyz.cofe.game.tank.unt;

import java.util.Map;
import xyz.cofe.game.tank.sprite.SpriteLine;
import xyz.cofe.game.tank.sprite.SpritesData;

/**
 * Первый игрок
 */
public class PlayerOne extends Player<PlayerOne> {
    /**
     * Конструктор
     */
    public PlayerOne(){}

    /**
     * Конструктор копирования
     * @param figura образец
     */
    public PlayerOne(Figure<?> figura){
        super(figura);
    }

    /**
     * Конструктор копирования
     * @param sample образец
     */
    public PlayerOne( PlayerOne sample ){
        super(sample);
    }

    /**
     * Клонирование объекта
     * @return клон
     */
    public PlayerOne clone(){ return new PlayerOne(this); }

    private static final Map<PlayerState,Map<Direction, SpriteLine>> sprites =
        Map.of(
            PlayerState.Level0,
            Map.of(
                Direction.LEFT, SpritesData.player_one_left_0.toSpriteLine(),
                Direction.RIGHT, SpritesData.player_one_right_0.toSpriteLine(),
                Direction.UP, SpritesData.player_one_up_0.toSpriteLine(),
                Direction.DOWN, SpritesData.player_one_down_0.toSpriteLine()
            ),
            PlayerState.Level1,
            Map.of(
                Direction.LEFT, SpritesData.player_one_left_1.toSpriteLine(),
                Direction.RIGHT, SpritesData.player_one_right_1.toSpriteLine(),
                Direction.UP, SpritesData.player_one_up_1.toSpriteLine(),
                Direction.DOWN, SpritesData.player_one_down_1.toSpriteLine()
            ),
            PlayerState.Level2,
            Map.of(
                Direction.LEFT, SpritesData.player_one_left_2.toSpriteLine(),
                Direction.RIGHT, SpritesData.player_one_right_2.toSpriteLine(),
                Direction.UP, SpritesData.player_one_up_2.toSpriteLine(),
                Direction.DOWN, SpritesData.player_one_down_2.toSpriteLine()
            ),
            PlayerState.Level3,
            Map.of(
                Direction.LEFT, SpritesData.player_one_left_3.toSpriteLine(),
                Direction.RIGHT, SpritesData.player_one_right_3.toSpriteLine(),
                Direction.UP, SpritesData.player_one_up_3.toSpriteLine(),
                Direction.DOWN, SpritesData.player_one_down_3.toSpriteLine()
            )
        );

    @Override
    protected Map<PlayerState,Map<Direction,SpriteLine>> sprites(){ return sprites; }
}
