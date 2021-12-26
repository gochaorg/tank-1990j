package xyz.cofe.game.tank.ui;

/**
 * Свойство инструмента - привязка к сетке
 */
public interface SnapToGridProperty {
    /**
     * Привязывать к сетке
     * @return true - есть привязка
     */
    boolean isSnapToGrid();

    /**
     * Привязывать к сетке
     * @param snapToGrid true - есть привязка
     */
    void setSnapToGrid(boolean snapToGrid);
}
