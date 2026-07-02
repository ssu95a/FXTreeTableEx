package ru.inversion.fx.form.controls.treetableex.column;

import javafx.geometry.Pos;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;
import ru.inversion.dataset.fx.ICellValueChangeListener;
import ru.inversion.fx.form.controls.JInvMoneyField;
import ru.inversion.fx.form.controls.treetableex.JInvTreeTableCell;
import ru.inversion.fx.form.controls.treetableex.JInvTreeTableColumnEx;
import ru.inversion.fx.form.controls.treetableex.TableObservableValue;
import ru.inversion.meta.IEntityProperty;
import ru.inversion.utils.Tags;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Столбец TreeTable для отображения денежных данных
 * <p>
 * @author Sulimoff
 */
public class JInvTreeTableColumnEx_Money<P> extends JInvTreeTableColumnEx<P,BigDecimal> {
    public JInvTreeTableColumnEx_Money() {
        super();
        this.converter = JInvMoneyField.stringConverter;
    }

    /** */
    public void bind( IEntityProperty< P, BigDecimal > ep, ICellValueChangeListener<P> cellValueChangeListener )
    {
        if( this.entityProperty != null )
            throw new IllegalStateException(Tags.PRODUCT_LABEL + "Column '" + getFieldName() + "' already bind" );

        this.entityProperty = Objects.requireNonNull( ep, "'entityProperty' is null" );

        if( ep.getType() != BigDecimal.class )
            throw new IllegalStateException(ru.inversion.fx.app.Tags.PRODUCT_LABEL + "Column '" + getFieldName() + "' is not BigDecimal type" );

        Callback<TreeTableColumn<P,BigDecimal>, TreeTableCell<P,BigDecimal>> cellFactory = param -> new JInvTreeTableCell<>(Pos.CENTER_RIGHT);
        final TableObservableValue bval = new TableObservableValue(ep);
        setCellFactory     ( cellFactory );
        setCellValueFactory( param -> bval.pojoInstance( param.getValue() == null ? null : param.getValue().getValue()) );
    }
}
