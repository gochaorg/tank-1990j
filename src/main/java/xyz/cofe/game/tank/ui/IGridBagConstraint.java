package xyz.cofe.game.tank.ui;

import java.awt.GridBagConstraints;

public interface IGridBagConstraint {
    IGridBagConstraint clone();
    IGridBagConstraint assign( GridBagConstraints gbc );
    IGridBagConstraint assign( IGridBagConstraint gbc );
    GridBagConstraints toGBC();

    //region x : Integer
    public Integer getX();
    public void setX(Integer x);
    //endregion
    //region y : Integer
    public Integer getY();
    public void setY(Integer y);
    //endregion
    //region fill : Integer
    public Integer getFill();
    public void setFill(Integer fill);
    public static class Fill {
        public final IGridBagConstraint igbc;
        public Fill(IGridBagConstraint igbc) {
            if( igbc==null )throw new IllegalArgumentException( "igbc==null" );
            this.igbc = igbc;
        }
        public IGridBagConstraint both(){
            igbc.setFill(GridBagConstraints.BOTH);
            return igbc;
        }
        public IGridBagConstraint none(){
            igbc.setFill(GridBagConstraints.NONE);
            return igbc;
        }
        public IGridBagConstraint horizontal(){
            igbc.setFill(GridBagConstraints.HORIZONTAL);
            return igbc;
        }
        public IGridBagConstraint vertical(){
            igbc.setFill(GridBagConstraints.VERTICAL);
            return igbc;
        }
    }
    public default Fill fill(){return new Fill(this); }
    //endregion
    //region weightX : Double
    public Double getWeightX();

    public void setWeightX(Double weightX);
    //endregion
    //region weightY : Double
    public Double getWeightY();

    public void setWeightY(Double weightY);
    //endregion
    //region ipadx : Integer
    public Integer getIpadx();

    public void setIpadx(Integer ipadx);
    //endregion
    //region ipady : Integer
    public Integer getIpady();

    public void setIpady(Integer ipady);
    //endregion
    //region paddingLeft : Integer
    public Integer getPaddingLeft();

    public void setPaddingLeft(Integer paddingLeft);
    //endregion
    //region paddingRight : Integer
    public Integer getPaddingRight();

    public void setPaddingRight(Integer paddingRight);
    //endregion
    //region paddingTop : Integer
    public Integer getPaddingTop();

    public void setPaddingTop(Integer paddingTop);
    //endregion
    //region paddingBottom : Integer
    public Integer getPaddingBottom();

    public void setPaddingBottom(Integer paddingBottom);
    //endregion
    //region anchor : Integer
    public Integer getAnchor();
    public void setAnchor(Integer anchor);
    public static class Anchor {
        public final IGridBagConstraint igbc;
        public Anchor(IGridBagConstraint igbc) {
            if( igbc==null )throw new IllegalArgumentException( "igbc==null" );
            this.igbc = igbc;
        }
        public IGridBagConstraint top(){
            igbc.setAnchor(GridBagConstraints.NORTH);
            return igbc;
        }
        public IGridBagConstraint bottom(){
            igbc.setAnchor(GridBagConstraints.SOUTH);
            return igbc;
        }
        public IGridBagConstraint center(){
            igbc.setAnchor(GridBagConstraints.CENTER);
            return igbc;
        }
        public IGridBagConstraint left(){
            igbc.setAnchor(GridBagConstraints.WEST);
            return igbc;
        }
        public IGridBagConstraint right(){
            igbc.setAnchor(GridBagConstraints.EAST);
            return igbc;
        }
        public IGridBagConstraint leftTop(){
            igbc.setAnchor(GridBagConstraints.NORTHWEST);
            return igbc;
        }
        public IGridBagConstraint rightTop(){
            igbc.setAnchor(GridBagConstraints.NORTHEAST);
            return igbc;
        }
        public IGridBagConstraint leftBottom(){
            igbc.setAnchor(GridBagConstraints.SOUTHWEST);
            return igbc;
        }
        public IGridBagConstraint rightBottom(){
            igbc.setAnchor(GridBagConstraints.SOUTHEAST);
            return igbc;
        }
        public IGridBagConstraint pageStart(){
            igbc.setAnchor(GridBagConstraints.PAGE_START);
            return igbc;
        }
        public IGridBagConstraint pageEnd(){
            igbc.setAnchor(GridBagConstraints.PAGE_END);
            return igbc;
        }
        public IGridBagConstraint lineStart(){
            igbc.setAnchor(GridBagConstraints.LINE_START);
            return igbc;
        }
        public IGridBagConstraint lineEnd(){
            igbc.setAnchor(GridBagConstraints.LINE_END);
            return igbc;
        }
        public IGridBagConstraint firstLineStart(){
            igbc.setAnchor(GridBagConstraints.FIRST_LINE_START);
            return igbc;
        }
        public IGridBagConstraint firstLineEnd(){
            igbc.setAnchor(GridBagConstraints.FIRST_LINE_END);
            return igbc;
        }
        public IGridBagConstraint lastLineStart(){
            igbc.setAnchor(GridBagConstraints.LAST_LINE_START);
            return igbc;
        }
        public IGridBagConstraint lasttLineEnd(){
            igbc.setAnchor(GridBagConstraints.LAST_LINE_END);
            return igbc;
        }
        public IGridBagConstraint baseLine(){
            igbc.setAnchor(GridBagConstraints.BASELINE);
            return igbc;
        }
        public IGridBagConstraint baseLineLeading(){
            igbc.setAnchor(GridBagConstraints.BASELINE_LEADING);
            return igbc;
        }
        public IGridBagConstraint baseLineTrailing(){
            igbc.setAnchor(GridBagConstraints.BASELINE_TRAILING);
            return igbc;
        }
        public IGridBagConstraint aboveBaseLine(){
            igbc.setAnchor(GridBagConstraints.ABOVE_BASELINE);
            return igbc;
        }
        public IGridBagConstraint aboveBaseLineLeading(){
            igbc.setAnchor(GridBagConstraints.ABOVE_BASELINE_LEADING);
            return igbc;
        }
        public IGridBagConstraint aboveBaseLineTrailing(){
            igbc.setAnchor(GridBagConstraints.ABOVE_BASELINE_TRAILING);
            return igbc;
        }
        public IGridBagConstraint belowBaseLine(){
            igbc.setAnchor(GridBagConstraints.BELOW_BASELINE);
            return igbc;
        }
        public IGridBagConstraint belowBaseLineLeading(){
            igbc.setAnchor(GridBagConstraints.BELOW_BASELINE_LEADING);
            return igbc;
        }
        public IGridBagConstraint belowBaseLineTrailing(){
            igbc.setAnchor(GridBagConstraints.BELOW_BASELINE_TRAILING);
            return igbc;
        }
    }
    public default Anchor anchor(){return new Anchor(this); }
    //endregion
    //region gridWidth : Integer
    public Integer getGridWidth();

    public void setGridWidth(Integer gridWidth);
    //endregion
    //region gridHeight : Integer
    public Integer getGridHeight();
    public void setGridHeight(Integer gridHeight);
    //endregion
}
