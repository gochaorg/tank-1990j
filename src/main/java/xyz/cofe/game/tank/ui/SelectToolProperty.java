package xyz.cofe.game.tank.ui;

import xyz.cofe.game.tank.ui.tool.SelectTool;

/**
 * Свойство инструмента - ссылка на инструмент выбора
 */
public interface SelectToolProperty {
    /**
     * Возвращает инструмент выбора
     * @return инструмент выбора
     */
    public SelectTool getSelectTool();

    /**
     * Указывает инструмент выбора
     * @param selectTool инструмент выбора
     */
    public void setSelectTool(SelectTool selectTool);
}
