package xyz.cofe.game.tank.ui;

import xyz.cofe.fn.Tuple;
import xyz.cofe.fn.Tuple2;

import javax.swing.JComponent;
import javax.swing.JLabel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GridBagLayoutBuilder {
    protected Container tableHolder;
    public GridBagLayoutBuilder(Container tableHolder){
        if( tableHolder==null )throw new IllegalArgumentException( "tableHolder==null" );
        this.tableHolder = tableHolder;
    }

    public class ContentPadding {
        public ContentPadding left( int indent ){
            for( var row : rows ){
                if( !row.cells.isEmpty() ){
                    row.cells.get(0).getGridBagConstraint().setPaddingLeft(indent);
                }
            }
            return this;
        }
        public ContentPadding right( int indent ){
            for( var row : rows ){
                if( !row.cells.isEmpty() ){
                    row.cells.get(row.cells.size()-1).getGridBagConstraint().setPaddingRight(indent);
                }
            }
            return this;
        }
        public ContentPadding top( int indent ){
            if( !rows.isEmpty() ){
                var r = rows.get(0);
                for( var c : r.cells ){
                    c.getGridBagConstraint().setPaddingTop(indent);
                }
            }
            return this;
        }
        public ContentPadding bottom( int indent ){
            if( !rows.isEmpty() ){
                var r = rows.get(rows.size()-1);
                for( var c : r.cells ){
                    c.getGridBagConstraint().setPaddingBottom(indent);
                }
            }
            return this;
        }
    }
    public GridBagLayoutBuilder contentPadding( Consumer<ContentPadding> paddingConsumer ){
        if( paddingConsumer==null )throw new IllegalArgumentException( "paddingConsumer==null" );
        paddingConsumer.accept(new ContentPadding());
        return this;
    }

    public void build(){
        if( !(tableHolder.getLayout() instanceof GridBagLayout) ){
            tableHolder.setLayout(new GridBagLayout());
        }
        for( var row : rows ){
            row.build();
        }
    }

    //region gridBagConstraint : IGridBagConstraint
    protected IGridBagConstraint gridBagConstraint = new IGridBagConstraintImpl();
    public IGridBagConstraint getGridBagConstraint(){ return gridBagConstraint; }
    public void setGridBagConstraint(IGridBagConstraint c){
        if( c==null )throw new IllegalArgumentException( "c==null" );
        gridBagConstraint = c;
    }
    public GridBagLayoutBuilder gridBagConstraint(IGridBagConstraint c){
        if( c==null )throw new IllegalArgumentException( "c==null" );
        gridBagConstraint = c;
        return this;
    }
    //endregion

    public static enum Fill {
        Both(GridBagConstraints.BOTH),
        None(GridBagConstraints.NONE),
        Horizontal(GridBagConstraints.HORIZONTAL),
        Vertical(GridBagConstraints.VERTICAL);
        Fill(int fill){
            value = fill;
        }
        public final int value;
    }
    public static enum Anchor {
        Left(GridBagConstraints.WEST),
        Right(GridBagConstraints.EAST),
        Top(GridBagConstraints.NORTH),
        Bottom(GridBagConstraints.SOUTH),
        Center(GridBagConstraints.CENTER),
        LeftTop(GridBagConstraints.NORTHWEST),
        RightTop(GridBagConstraints.NORTHEAST),
        RightBottom(GridBagConstraints.SOUTHEAST),
        LeftBottom(GridBagConstraints.SOUTHWEST),
        ;
        Anchor(int value){
            this.value = value;
        }
        public final int value;
    }

    protected final List<RowBuilder> rows = new ArrayList<>();
    public class RowBuilder {
        public final List<CellBuilder> cells = new ArrayList<>();

        //region gridBagConstraint : IGridBagConstraint
        protected IGridBagConstraint gridBagConstraint = new IGridBagConstraintImpl();
        public IGridBagConstraint getGridBagConstraint(){ return gridBagConstraint; }
        public void setGridBagConstraint(IGridBagConstraint c){
            if( c==null )throw new IllegalArgumentException( "c==null" );
            gridBagConstraint = c;
        }
        public RowBuilder gridBagConstraint(IGridBagConstraint c){
            if( c==null )throw new IllegalArgumentException( "c==null" );
            gridBagConstraint = c;
            return this;
        }
        //endregion

        public RowBuilder padLeft(int indent){
            getGridBagConstraint().setPaddingLeft(indent);
            return this;
        }
        public RowBuilder padRight(int indent){
            getGridBagConstraint().setPaddingRight(indent);
            return this;
        }
        public RowBuilder padTop(int indent){
            getGridBagConstraint().setPaddingTop(indent);
            return this;
        }
        public RowBuilder padBottom(int indent){
            getGridBagConstraint().setPaddingBottom(indent);
            return this;
        }

        public RowBuilder cell( Consumer<CellBuilder> cellBuilder ){
            if( cellBuilder==null )throw new IllegalArgumentException( "cellBuilder==null" );
            CellBuilder cellb = new CellBuilder(this);
            inherit(cellb);
            cells.add(cellb);
            cellBuilder.accept(cellb);
            return this;
        }
        public CellBuilder cell( JComponent cell ){
            if( cell==null )throw new IllegalArgumentException( "cell==null" );
            CellBuilder cellb = new CellBuilder(this);
            inherit(cellb);
            cells.add(cellb);
            cellb.components.add(cell);
            return cellb;
        }
        public CellBuilder label( String label ){
            if( label==null )throw new IllegalArgumentException( "label==null" );

            var cell = new JLabel(label);
            cell.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,12));

            CellBuilder cellb = new CellBuilder(this);
            inherit(cellb);
            cells.add(cellb);
            cellb.components.add(cell);
            return cellb;
        }

        protected void inherit(CellBuilder cellb){
            cellb.getGridBagConstraint().assign(getGridBagConstraint());
        }

        public void build(){
            for( var cell : cells ){
                cell.build();
            }
        }
    }
    public GridBagLayoutBuilder row(Consumer<RowBuilder> rowBuilder){
        if( rowBuilder==null )throw new IllegalArgumentException( "rowBuilder==null" );
        RowBuilder rowb = new RowBuilder();
        rowb.getGridBagConstraint().assign(getGridBagConstraint());
        rows.add(rowb);
        rowBuilder.accept(rowb);
        return this;
    }

    public class CellBuilder {
        public final RowBuilder row;
        public final List<JComponent> components = new ArrayList<>();

        //region gridBagConstraint : IGridBagConstraint
        protected IGridBagConstraint gridBagConstraint = new IGridBagConstraintImpl();
        public IGridBagConstraint getGridBagConstraint(){ return gridBagConstraint; }
        public void setGridBagConstraint(IGridBagConstraint c){
            if( c==null )throw new IllegalArgumentException( "c==null" );
            gridBagConstraint = c;
        }
        public CellBuilder gridBagConstraint(IGridBagConstraint c){
            if( c==null )throw new IllegalArgumentException( "c==null" );
            gridBagConstraint = c;
            return this;
        }
        //endregion

        public CellBuilder(RowBuilder rowb){
            if( rowb==null )throw new IllegalArgumentException( "rowb==null" );
            this.row = rowb;
        }
        public CellBuilder component( JComponent component ){
            if( component==null )throw new IllegalArgumentException( "component==null" );
            components.add(component);
            return this;
        }
        public CellBuilder fill(Fill f){
            if( f==null )throw new IllegalArgumentException( "f==null" );
            getGridBagConstraint().setFill(f.value);
            switch (f){
                case Both:
                    if( getGridBagConstraint().getWeightX()==null )getGridBagConstraint().setWeightX(1.0);
                    if( getGridBagConstraint().getWeightY()==null )getGridBagConstraint().setWeightY(1.0);
                    break;
                case Horizontal:
                    if( getGridBagConstraint().getWeightX()==null )getGridBagConstraint().setWeightX(1.0);
                    break;
                case Vertical:
                    if( getGridBagConstraint().getWeightY()==null )getGridBagConstraint().setWeightY(1.0);
                    break;
                case None:
                    getGridBagConstraint().setWeightX(null);
                    getGridBagConstraint().setWeightY(null);
                    break;
            }
            return this;
        }

        public CellBuilder fill(Fill f, Double weightX, Double weightY){
            if( f==null )throw new IllegalArgumentException( "f==null" );
            getGridBagConstraint().setFill(f.value);
            if( weightX!=null )getGridBagConstraint().setWeightX(weightX);
            if( weightY!=null )getGridBagConstraint().setWeightY(weightY);
            return this;
        }

        public CellBuilder padLeft(int indent){
            getGridBagConstraint().setPaddingLeft(indent);
            return this;
        }
        public CellBuilder padRight(int indent){
            getGridBagConstraint().setPaddingRight(indent);
            return this;
        }
        public CellBuilder padTop(int indent){
            getGridBagConstraint().setPaddingTop(indent);
            return this;
        }
        public CellBuilder padBottom(int indent){
            getGridBagConstraint().setPaddingBottom(indent);
            return this;
        }
        public CellBuilder minWidth(int width){
            getGridBagConstraint().setIpadx(width);
            return this;
        }
        public CellBuilder minHeight(int height){
            getGridBagConstraint().setIpady(height);
            return this;
        }
        public CellBuilder colSpan(int colSpan){
            getGridBagConstraint().setGridWidth(colSpan);
            return this;
        }
        public CellBuilder rowSpan(int rowSpan){
            getGridBagConstraint().setGridHeight(rowSpan);
            return this;
        }
        public CellBuilder anchor( Anchor anchor ){
            getGridBagConstraint().setAnchor(anchor.value);
            return this;
        }

        protected Tuple2<Integer,Integer> gridXY(){
            int cellIdx = row.cells.indexOf(this);
            int rowIdx = rows.indexOf(row);
            return Tuple.of(cellIdx,rowIdx);
        }
        private GridBagConstraints gridBagConstraints(){
            GridBagConstraints gbc = getGridBagConstraint().toGBC();
            var gridXy = gridXY();
            gbc.gridx = gridXy.a();
            gbc.gridy = gridXy.b();
            return gbc;
        }

        public void build(){
            if( components.isEmpty() )return;
            var gbc = gridBagConstraints();
            for( var cmpt : components ){
                if( cmpt!=null ){
                    tableHolder.add(cmpt,gbc);
                }
            }
        }
    }
}
