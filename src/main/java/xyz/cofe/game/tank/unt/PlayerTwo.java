package xyz.cofe.game.tank.unt;

import xyz.cofe.game.tank.sprite.SpriteLine;
import xyz.cofe.game.tank.sprite.SpritesData;

import java.util.Map;

/**
 * Второй игрок
 */
public class PlayerTwo extends Player<PlayerTwo> {
    /**
     * Конструктор
     */
    public PlayerTwo(){}

    /**
     * Конструктор копирования
     * @param sample образец
     */
    public PlayerTwo(Figure<?> sample){
        super(sample);
    }

    /**
     * Конструктор копирования
     * @param sample образец
     */
    public PlayerTwo(PlayerTwo sample){
        super(sample);
    }

    /**
     * Клонирование объекта
     * @return клон
     */
    public PlayerTwo clone(){ return new PlayerTwo(this); }

    private static final Map<PlayerState,Map<Direction,SpriteLine>> sprites =
        Map.of(
            PlayerState.Level0,
            Map.of(
                Direction.LEFT, SpritesData.player_two_left_0.toSpriteLine(),
                Direction.RIGHT, SpritesData.player_two_right_0.toSpriteLine(),
                Direction.UP, SpritesData.player_two_up_0.toSpriteLine(),
                Direction.DOWN, SpritesData.player_two_down_0.toSpriteLine()
            ),
            PlayerState.Level1,
            Map.of(
                Direction.LEFT, SpritesData.player_two_left_1.toSpriteLine(),
                Direction.RIGHT, SpritesData.player_two_right_1.toSpriteLine(),
                Direction.UP, SpritesData.player_two_up_1.toSpriteLine(),
                Direction.DOWN, SpritesData.player_two_down_1.toSpriteLine()
            ),
            PlayerState.Level2,
            Map.of(
                Direction.LEFT, SpritesData.player_two_left_2.toSpriteLine(),
                Direction.RIGHT, SpritesData.player_two_right_2.toSpriteLine(),
                Direction.UP, SpritesData.player_two_up_2.toSpriteLine(),
                Direction.DOWN, SpritesData.player_two_down_2.toSpriteLine()
            ),
            PlayerState.Level3,
            Map.of(
                Direction.LEFT, SpritesData.player_two_left_3.toSpriteLine(),
                Direction.RIGHT, SpritesData.player_two_right_3.toSpriteLine(),
                Direction.UP, SpritesData.player_two_up_3.toSpriteLine(),
                Direction.DOWN, SpritesData.player_two_down_3.toSpriteLine()
            )
        );

    @Override
    protected Map<PlayerState,Map<Direction,SpriteLine>> sprites(){ return sprites; }
}
