package br.com.grupocaravela.render;

import java.text.NumberFormat;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class MoedaRender extends DefaultTableCellRenderer {
    
	private static final long serialVersionUID = 1L;
	NumberFormat formatter;
    public MoedaRender(NumberFormat nf) { 
        super();
        formatter = nf;
    }

    public void setValue(Object value) {
        if (formatter==null) {
            formatter = NumberFormat.getInstance();
        }
        setHorizontalAlignment(SwingConstants.RIGHT);
        setText((value == null) ? "" : "R$ " + formatter.format(value));
    }
}
