package xyz.cofe.game.tank.ui;

import xyz.cofe.gui.swing.SwingListener;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Optional;
import java.util.TreeSet;

import static xyz.cofe.game.tank.ui.GridBagLayoutBuilder.Anchor.*;
import static xyz.cofe.game.tank.ui.GridBagLayoutBuilder.Fill.*;

public class FontChooser extends JDialog {
    public FontChooser(){
        initUi();
    }

    public FontChooser( JFrame parent ){
        super(parent);
        initUi();
    }

    private void initUi(){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] allFonts = ge.getAllFonts();
        TreeSet<String> fontNameSet = new TreeSet<>();
        for( var fnt : allFonts ){
            fontNameSet.add(fnt.getFamily());
        }

        fontName = new JComboBox<>(fontNameSet.toArray(new String[]{}));
        fontName.setMinimumSize(new Dimension(120,23));
        fontName.setPreferredSize(new Dimension(120,23));

        setTitle("Select font");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JButton okBut = new JButton("Ok");
        JButton cancelBut = new JButton("Cancel");

        new GridBagLayoutBuilder(getContentPane())
            .row( row -> {
                row.label("Font name:");
                row.cell(fontName).fill(Horizontal).padRight(3).minWidth(120);
                row.cell(boldStyle);
                row.cell(italicStyle);
                row.label("size:").padLeft(3);
                row.cell(fontSize).fill(Horizontal,2.0, 0.0).minWidth(75);
            })
            .row( row -> {
                row.label("Preview:").colSpan(6).anchor(Center);
            })
            .row( row -> {
                row.cell(preview).colSpan(6).fill(Both,1.0, 1.0);
            })
            .row( row -> {
                row.cell(cancelBut);
                row.cell(okBut).colSpan(5).anchor(Right);
            })
            .contentPadding( p -> p.left(3).right(3).top(3).bottom(5) )
            .build();

        setSize(500,300);

        SwingListener.onActionPerformed( fontName, ev -> {
            if( editCall )return;
            preview();
        });

        SwingListener.onActionPerformed(boldStyle, ev -> {
            if( editCall )return;
            preview();
        });

        SwingListener.onActionPerformed(italicStyle, ev -> {
            if( editCall )return;
            preview();
        });

        SwingListener.onTextChanged(fontSize, () -> {
            if( editCall )return;
            preview();
        });

        SwingListener.onActionPerformed(okBut, ev -> {
            okClicked = true;
            setVisible(false);
            dispose();
        });
    }

    private JComboBox<String> fontName;

    //private final JTextField fontName = new JTextField();
    private final JCheckBox boldStyle = new JCheckBox("Bold");
    {
        boldStyle.setFont(new Font(Font.SANS_SERIF,Font.BOLD,12));
    }

    private final JCheckBox italicStyle = new JCheckBox("Italic");
    {
        italicStyle.setFont(new Font(Font.SERIF,Font.ITALIC,12));
    }

    private final JTextField fontSize = new JTextField();
    private final JTextField preview = new JTextField("Preview");

    private boolean editCall = false;

    private void edit( Font font ){
        try {
            editCall = true;
            fontName.setSelectedItem(font.getFamily());
            fontSize.setText(""+font.getSize());
            boldStyle.setSelected(false);
            italicStyle.setSelected(false);
            if( font.isBold() ){
                boldStyle.setSelected(true);
            }
            if( font.isItalic() ){
                italicStyle.setSelected(true);
            }
            preview();
        } finally {
            editCall = false;
        }
    }

    private void preview(){
        var fname = fontName.getSelectedItem();
        if( fname==null )return;

        var fsize = fontSize.getText();
        int size;
        try {
            size = Integer.parseInt(fsize);
        }catch (NumberFormatException e){
            return;
        }

        Font font = new Font(
            fname.toString(),
            (boldStyle.isSelected() ? Font.BOLD : Font.PLAIN)
            | (italicStyle.isSelected() ? Font.ITALIC : Font.PLAIN)
            ,
            size
            );

        preview.setFont(font);
    }

    public Font currentFont(){
        return preview.getFont();
    }

    protected boolean okClicked;
    public boolean isOk(){ return okClicked; }

    public static Optional<Font> choose(Font font, JFrame parent ){
        FontChooser fc = new FontChooser(parent);
        if( font!=null )fc.edit(font);
        fc.setModal(true);
        fc.setVisible(true);
        return fc.isOk() ? Optional.of(fc.currentFont()) : Optional.empty();
    }

    public static Optional<Font> choose(Font font){
        FontChooser fc = new FontChooser();
        if( font!=null )fc.edit(font);
        fc.setModal(true);
        fc.setVisible(true);
        return fc.isOk() ? Optional.of(fc.currentFont()) : Optional.empty();
    }

    public static Optional<Font> choose(){
        FontChooser fc = new FontChooser();
        fc.setModal(true);
        fc.setVisible(true);
        return fc.isOk() ? Optional.of(fc.currentFont()) : Optional.empty();
    }
}
