package xyz.cofe.game.tank;

/**
 * Старт/стоп анимации
 * @param <SELF> проивзодный класс
 */
public interface Animated<SELF extends Animated<SELF>> {
    /**
     * В режиме анимации
     * @return true - режим анимации, присуствует смена кадров
     */
    public boolean isAnimationRunning();

    /**
     * Запуск анимации
     * @return Self ссылка
     */
    public SELF startAnimation();

    /**
     * Остановка анимации
     * @return Self ссылка
     */
    public SELF stopAnimation();
}
