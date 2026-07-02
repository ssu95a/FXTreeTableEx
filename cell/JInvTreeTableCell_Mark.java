package ru.inversion.fx.form.controls.treetableex.cell;

import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import ru.inversion.fx.form.controls.treetableex.JInvTreeTableCell;
import ru.inversion.fx.form.controls.treetableex.TSFXAdapter;

/**
 * Ячейка для пометки записей.
 */
public final class JInvTreeTableCell_Mark<P> extends JInvTreeTableCell<P, Boolean> {

    private static final PseudoClass markPseudoClass = PseudoClass.getPseudoClass("mark");

    private final CheckBox checkBox;
    private final TSFXAdapter<P> adapter;
    private final boolean leafOnly;

    public JInvTreeTableCell_Mark( TSFXAdapter<P> adapter, boolean leafOnly )
    {
        super();

        this.leafOnly = leafOnly;
        this.adapter  = adapter;

        this.checkBox = new CheckBox();
        this.checkBox.setFocusTraversable(false);

        checkBox.setOnAction((ActionEvent event) -> switchMark());

        checkBox.addEventFilter(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {

            if( event.isControlDown() && event.getButton().equals(MouseButton.PRIMARY) ) {
                checkBox.selectedProperty().set(!checkBox.isSelected());
                switchMark();
            }

            if (event.isShiftDown() && event.getButton().equals(MouseButton.PRIMARY)) {

                TreeTableView<P> table = getTreeTableView();

                if (table != null && getTreeTableRow() != null) {
                    checkBox.selectedProperty().set(true);
                    table.getSelectionModel().select(getTreeTableRow().getIndex());
                }
            }
        });

        this.getStyleClass().add("check-box-table-cell");

        setGraphic(null);
        setText(null);
    }

    /** */
    private void switchMark() {
        commitEdit(checkBox.isSelected());
    }

    /** */
    private boolean isLeaf() {

        if( getTreeTableRow() == null )
            return false;

        final TreeItem<P> item = getTreeTableRow().getTreeItem();
        return item != null && item.isLeaf();
    }

    /** */
    @Override
    public void updateItem(Boolean item, boolean empty) {

        superUpdateItem(item, empty);

        if (empty || (leafOnly && !isLeaf())) {
            setText(null);
            setGraphic(null);
            checkBox.setSelected(false);
            switchMarkColorOnRow(false);
            applyRenderer(item, true);
            return;
        }

        setText(null);
        setGraphic(checkBox);

        boolean selected = Boolean.TRUE.equals(item);

        checkBox.setSelected(selected);
        switchMarkColorOnRow(selected);

        applyRenderer(item, false);
    }

    /** */
    @Override
    public void commitEdit(Boolean newValue) {

        super.commitEdit(newValue);

        if( getTreeTableRow() == null || getTreeTableRow().isEmpty() )
            return;

        final TreeItem<P> item = getTreeTableRow().getTreeItem();

        if (item == null) {
            return;
        }

        if( Boolean.TRUE.equals(newValue) )
        {
            adapter.markItem(item);
            switchMarkColorOnRow(true);
        }
        else
        {
            adapter.unMarkItem(item);
            switchMarkColorOnRow(false);
        }
    }

    /** */
    private void switchMarkColorOnRow(boolean val) {

        if (getTreeTableRow() != null) {
            getTreeTableRow().pseudoClassStateChanged(markPseudoClass, val);
        }
    }
}