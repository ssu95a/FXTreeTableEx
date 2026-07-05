package ru.inversion.fx.form.controls.treetableex;

import javafx.geometry.Pos;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableRow;
import ru.inversion.fx.form.controls.renderer.Colorizer;
import ru.inversion.fx.form.controls.renderer.IColoredCell;
import ru.inversion.utils.S;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.inversion.fx.form.controls.JInvTableColumn.COLUMN_USER_RENDERER;

/** */
public class JInvTreeTableCell<P,T> extends TreeTableCell<P,T> implements IColoredCell<P> {

    public JInvTreeTableCell( Pos alignment ) {
        setAlignment(alignment);
    }
    public JInvTreeTableCell( ) { }

    protected void superUpdateItem( T item, boolean empty )
    {
        super.updateItem( item, empty );
    }

    @Override
    protected void updateItem(T item, boolean empty) {

        super.updateItem(item, empty);

        if( empty || item == null ) {
            clearCell();
            return;
        }

        setText( getTreeTableColumn().getStringConverter().toString(item) );
        setGraphic(null);

        applyRenderer(item, false);
    }

    /** */
    protected void clearCell() {

        setText(null);
        setGraphic(null);
        setTooltip(null);

        setStyle(S.EMPTY_STRING);

        setId(null);

//        for( PseudoClass pseudoClass : new HashSet<>(getPseudoClassStates()) )
//             pseudoClassStateChanged( pseudoClass, false );

        clearColor();
    }

    /** */
    public JInvTreeTableColumnEx getTreeTableColumn() {
        return (JInvTreeTableColumnEx)this.getTableColumn();
    }

    /** */
    public P getPojo( )
    {
        final TreeTableRow<P> row = getTreeTableRow();

        if ( row == null ){
            return null;
        }
        return row.getItem();
    }

    protected BiConsumer<JInvTreeTableCell, Object> getRenderer() {
        return (BiConsumer<JInvTreeTableCell, Object>) getTableColumn().getProperties().getOrDefault(COLUMN_USER_RENDERER, null);
    }

    /** */
    protected void applyRenderer(T item, boolean empty) {
        if (empty) {
            clearCell();
            return;
        }

        if (getRenderer() != null) {
            getRenderer().accept(this, item);
        }
    }

    //region Colorizer (copy of JInvTableCell)

    private Map<Function<IColoredCell<P>, Colorizer>, Colorizer> colorMap;

    private Map<Function<IColoredCell<P>, Colorizer>, Colorizer> getColorMap() {
        if( colorMap == null )
            colorMap = new LinkedHashMap<>();
        return colorMap;
    }

    @Override
    public void addColor( final Function<IColoredCell<P>, Colorizer> styleExpr ) {
        Colorizer color = styleExpr.apply( this );
//        boolean isDebug = toString().contains( "erh" );
//        String styleBefore = "";
//        String mapBefore = "";
//        if ( isDebug ){
//            styleBefore = getStyle();
//            mapBefore = getColorMap().toString();
//        }

        Map<Function<IColoredCell<P>, Colorizer>, Colorizer> colorMap = getColorMap();
        if ( color != null ){
            colorMap.remove( styleExpr );
            colorMap.put( styleExpr, color );
//            if ( isDebug )
//            logger.info( "{}: added color {}", this, color );
        } else {

            if ( !colorMap.isEmpty() && colorMap.containsKey(styleExpr) ){
                String s = colorMap.get( styleExpr ).toString();
                removeColorStyle( s );
                colorMap.remove( styleExpr );
//                if ( isDebug )
//                logger.info( "{}: removed style {}", this, s );
            }
        }
        setStyleInternal();
//        if ( isDebug ){
//            logger.info( "Style |{}|\n" +
//                         "   -> |{}|\n" +
//                         "Map   |{}|\n" +
//                         "   -> |{}|", styleBefore, getStyle(), mapBefore, getColorMap().toString()
//            );
//        }
    }

    private void setStyleInternal(){
        setStyle( getColorStyleString() );
        pseudoClassStateChanged( COLORIZED_BACKGROUND, check( IS_CUSTOM_BACKGROUND ) );
        pseudoClassStateChanged( COLORIZED_BACKGROUND_SECONDARY, check( IS_CUSTOM_BACKGROUND_SECONDARY ) );
        pseudoClassStateChanged( COLORIZED_TEXT, check( IS_CUSTOM_TEXT ) );
        pseudoClassStateChanged( COLORIZED_TEXT_SECONDARY, check( IS_CUSTOM_TEXT_SECONDARY ) );
    }


    private boolean check(Predicate<Colorizer> predicate) {
        return getColorMap().values().stream()
                .anyMatch( predicate );
    }

    private String getColorStyleString() {
        return getColorMap().values().stream()
                .map( Colorizer::toString )
                .collect( Collectors.joining( " " ) );
    }

    @Override
    public void clearColor() {
        Map<Function<IColoredCell<P>, Colorizer>, Colorizer> colorMap = getColorMap();

        if ( !colorMap.isEmpty() ){
            removeColorStyle();
            colorMap.clear();
        }

        pseudoClassStateChanged( COLORIZED_BACKGROUND, check( IS_CUSTOM_BACKGROUND ) );
        pseudoClassStateChanged( COLORIZED_BACKGROUND_SECONDARY, check( IS_CUSTOM_BACKGROUND_SECONDARY ) );
        pseudoClassStateChanged( COLORIZED_TEXT, check( IS_CUSTOM_TEXT ) );
        pseudoClassStateChanged( COLORIZED_TEXT_SECONDARY, check( IS_CUSTOM_TEXT_SECONDARY ) );
    }

    private void removeColorStyle() {
        removeColorStyle( null );
    }
    private void removeColorStyle(String colors)
    {
        String style = getStyle();

        if( S.isNullOrEmpty(colors) )
            colors = getColorStyleString();

        if( S.isNullOrEmpty(style) || S.isNullOrEmpty(colors) )
            return;

        final int index = style.indexOf(colors);

        if( index < 0 )
            return;

        style = ( style.substring(0, index) + style.substring(index + colors.length()) ).trim();

        setStyle(style);
    }
}
