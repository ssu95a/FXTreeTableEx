/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.inversion.fx.form.controls.treetableex.column;

import javafx.geometry.Pos;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;
import ru.inversion.dataset.fx.ICellValueChangeListener;
import ru.inversion.fx.app.Tags;
import ru.inversion.fx.form.controls.JInvTableColumnDate;
import ru.inversion.fx.form.controls.treetableex.JInvTreeTableColumnEx;
import ru.inversion.fx.form.controls.treetableex.TableObservableValue;
import ru.inversion.fx.form.controls.treetableex.cell.JInvTreeTableCell_Date;
import ru.inversion.meta.IEntityProperty;

import java.time.temporal.Temporal;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author antonovdi
 */
public class JInvTreeTableColumnEx_Date<P, T> extends JInvTreeTableColumnEx<P, T> {

    public JInvTableColumnDate.DateContentType dateFormat = JInvTableColumnDate.DateContentType.DATE_TIME;

    public JInvTreeTableColumnEx_Date()
    {
        super();
        setAlignment( Pos.CENTER );
    }

    public JInvTableColumnDate.DateContentType getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat( JInvTableColumnDate.DateContentType dateFormat) {
        this.dateFormat = dateFormat;
    }

    /** */
    public void bind( IEntityProperty< P, T > ep, ICellValueChangeListener<P> cellValueChangeListener )
    {
        if( this.entityProperty != null )
            throw new IllegalStateException(Tags.PRODUCT_LABEL + "Column '" + getFieldName() + "' already bind" );

        this.entityProperty = Objects.requireNonNull( ep, "'entityProperty' is null" );

        final Class<?> propertyType = ep.getType();

        if( !Temporal.class.isAssignableFrom(propertyType) && !Date.class.isAssignableFrom(propertyType) )
            throw new IllegalStateException( Tags.PRODUCT_LABEL + "Column '" + getFieldName() + "' is not date/time type" );

        this.converter = dateFormat.getStringConverter( ep.getType() );

        Callback< TreeTableColumn<P,T>, TreeTableCell<P,T> > cellFactory = param -> new JInvTreeTableCell_Date();

        final TableObservableValue bval = new TableObservableValue(ep);
        setCellFactory     ( cellFactory );
        setCellValueFactory( param -> bval.pojoInstance( param.getValue() == null ? null : param.getValue().getValue()) );
    }
}
