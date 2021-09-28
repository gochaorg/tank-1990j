package xyz.cofe.game.tank.ui;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class IGridBagConstraintImpl implements IGridBagConstraint {
    public IGridBagConstraintImpl(){
    }

    public IGridBagConstraintImpl(IGridBagConstraint sample){
        if( sample==null )throw new IllegalArgumentException( "sample==null" );
        x = sample.getX();
        y = sample.getY();
        fill = sample.getFill();
        weightX = sample.getWeightX();
        weightY = sample.getWeightY();
        ipady = sample.getIpady();
        ipadx = sample.getIpadx();
        paddingLeft = sample.getPaddingLeft();
        paddingRight = sample.getPaddingRight();
        paddingTop = sample.getPaddingTop();
        paddingBottom = sample.getPaddingBottom();
        anchor = sample.getAnchor();
        gridWidth = sample.getGridWidth();
        gridHeight = sample.getGridHeight();
    }

    public IGridBagConstraintImpl clone(){
        return new IGridBagConstraintImpl(this);
    }

    //region x : Integer
    public Integer x;
    public Integer getX() {
        return x;
    }
    public void setX(Integer x) {
        this.x = x;
    }
    //endregion
    //region y : Integer
    public Integer y;
    public Integer getY() {
        return y;
    }
    public void setY(Integer y) {
        this.y = y;
    }
    //endregion
    //region fill : Integer
    public Integer fill;
    public Integer getFill() {
        return fill;
    }
    public void setFill(Integer fill) {
        this.fill = fill;
    }
    //endregion
    //region weightX : Double
    public Double weightX;
    public Double getWeightX() {
        return weightX;
    }
    public void setWeightX(Double weightX) {
        this.weightX = weightX;
    }
    //endregion
    //region weightY : Double
    public Double weightY;
    public Double getWeightY() {
        return weightY;
    }
    public void setWeightY(Double weightY) {
        this.weightY = weightY;
    }
    //endregion
    //region ipadx : Integer
    public Integer ipadx;
    public Integer getIpadx() {
        return ipadx;
    }
    public void setIpadx(Integer ipadx) {
        this.ipadx = ipadx;
    }
    //endregion
    //region ipady : Integer
    public Integer ipady;
    public Integer getIpady() {
        return ipady;
    }
    public void setIpady(Integer ipady) {
        this.ipady = ipady;
    }
    //endregion
    //region paddingLeft : Integer
    public Integer paddingLeft;
    public Integer getPaddingLeft() {
        return paddingLeft;
    }
    public void setPaddingLeft(Integer paddingLeft) {
        this.paddingLeft = paddingLeft;
    }
    //endregion
    //region paddingRight : Integer
    public Integer paddingRight;
    public Integer getPaddingRight() {
        return paddingRight;
    }
    public void setPaddingRight(Integer paddingRight) {
        this.paddingRight = paddingRight;
    }
    //endregion
    //region paddingTop : Integer
    public Integer paddingTop;
    public Integer getPaddingTop() {
        return paddingTop;
    }
    public void setPaddingTop(Integer paddingTop) {
        this.paddingTop = paddingTop;
    }
    //endregion
    //region paddingBottom : Integer
    public Integer paddingBottom;
    public Integer getPaddingBottom() {
        return paddingBottom;
    }
    public void setPaddingBottom(Integer paddingBottom) {
        this.paddingBottom = paddingBottom;
    }
    //endregion
    //region anchor : Integer
    public Integer anchor;
    public Integer getAnchor() {
        return anchor;
    }
    public void setAnchor(Integer anchor) {
        this.anchor = anchor;
    }
    //endregion
    //region gridWidth : Integer
    public Integer gridWidth;
    public Integer getGridWidth() {
        return gridWidth;
    }

    public void setGridWidth(Integer gridWidth) {
        this.gridWidth = gridWidth;
    }
    //endregion
    //region gridHeight : Integer
    public Integer gridHeight;

    public Integer getGridHeight() {
        return gridHeight;
    }

    public void setGridHeight(Integer gridHeight) {
        this.gridHeight = gridHeight;
    }
    //endregion

    public GridBagConstraints toGBC(){
        GridBagConstraints gbc = new GridBagConstraints();
        if( x!=null )gbc.gridx = x;
        if( y!=null )gbc.gridy = y;
        if( fill!=null )gbc.fill = fill;
        if( weightX!=null )gbc.weightx = weightX;
        if( weightY!=null )gbc.weighty = weightY;
        if( ipadx!=null )gbc.ipadx = ipadx;
        if( ipady!=null )gbc.ipady = ipady;
        if( paddingLeft!=null || paddingRight!=null || paddingTop!=null || paddingBottom!=null ){
            gbc.insets = new Insets(
                paddingTop!=null ? paddingTop : 0,
                paddingLeft!=null ? paddingLeft : 0,
                paddingBottom!=null ? paddingBottom : 0,
                paddingRight!=null ? paddingRight : 0
            );
        }
        if( anchor!=null )gbc.anchor = anchor;
        if( gridWidth!=null )gbc.gridwidth = gridWidth;
        if( gridHeight!=null )gbc.gridheight = gridHeight;
        return gbc;
    }

    public static IGridBagConstraintImpl of( GridBagConstraints gbc ){
        if( gbc==null )throw new IllegalArgumentException( "gbc==null" );
        IGridBagConstraintImpl impl = new IGridBagConstraintImpl();
        return impl.assign(gbc);
    }

    public IGridBagConstraintImpl assign( GridBagConstraints gbc ){
        if( gbc==null )throw new IllegalArgumentException( "gbc==null" );
        x = gbc.gridx;
        x = gbc.gridy;
        fill = gbc.fill;
        weightX = gbc.weightx;
        weightY = gbc.weighty;
        ipadx = gbc.ipadx;
        ipady = gbc.ipady;
        var ins = gbc.insets;
        if( ins!=null ){
            paddingLeft = ins.left;
            paddingBottom = ins.bottom;
            paddingRight = ins.right;
            paddingTop = ins.top;
        }else{
            paddingLeft = 0; paddingRight = 0; paddingTop = 0; paddingBottom = 0;
        }
        anchor = gbc.anchor;
        gridWidth = gbc.gridwidth;
        gridHeight = gbc.gridheight;
        return this;
    }

    public IGridBagConstraintImpl assign( IGridBagConstraint sample ){
        if( sample==null )throw new IllegalArgumentException( "sample==null" );
        x = sample.getX();
        y = sample.getY();
        fill = sample.getFill();
        weightX = sample.getWeightX();
        weightY = sample.getWeightY();
        ipady = sample.getIpady();
        ipadx = sample.getIpadx();
        paddingLeft = sample.getPaddingLeft();
        paddingRight = sample.getPaddingRight();
        paddingTop = sample.getPaddingTop();
        paddingBottom = sample.getPaddingBottom();
        anchor = sample.getAnchor();
        gridWidth = sample.getGridWidth();
        gridHeight = sample.getGridHeight();
        return this;
    }
}
