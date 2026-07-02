package ru.inversion.fx.form.controls.treetableex.column;

import ru.inversion.fx.form.controls.treetableex.JInvTreeTableColumnEx;

/** */
public class JInvTreeTableColumnEx_Boolean<P> extends JInvTreeTableColumnEx<P, Boolean> {

    private static final int FIXED_WIDTH = 25;

    public JInvTreeTableColumnEx_Boolean() {
        super();
        init();
    }

    public JInvTreeTableColumnEx_Boolean( final String name ) {
        super( name );
        init();
    }

    private void init( ) {
        setMinWidth ( FIXED_WIDTH );
        setPrefWidth( FIXED_WIDTH );
        setMaxWidth ( 40 );
    }
}
