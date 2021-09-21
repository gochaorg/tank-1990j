package xyz.cofe.game.tank.ui;

import xyz.cofe.ecolls.Closeables;
import xyz.cofe.gui.swing.GuiUtil;
import xyz.cofe.gui.swing.SwingListener;
import xyz.cofe.text.Text;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.function.BiConsumer;

public class ActionEditor {
    //region actionSettings : Map<String,ActionSetting>
    private Map<String,ActionSetting> actionSettings;
    public Map<String, ActionSetting> getActionSettings() {
        return actionSettings;
    }

    public void setActionSettings(Map<String, ActionSetting> actionSettings) {
        this.actionSettings = actionSettings;
    }
    //endregion
    //region registry : Map<MenuBuilder.ActionItem, JMenuItem>
    private Map<MenuBuilder.ActionItem, JMenuItem> registry;
    public Map<MenuBuilder.ActionItem, JMenuItem> getRegistry() { return registry; }
    public void setRegistry(Map<MenuBuilder.ActionItem, JMenuItem> registry) {
        this.registry = registry;
    }
    //endregion
    //region menuItem : JMenuItem
    private JMenuItem menuItem;
    public JMenuItem getMenuItem() { return menuItem; }
    public void setMenuItem(JMenuItem menuItem) { this.menuItem = menuItem; }
    //endregion
    //region actionItem : MenuBuilder.ActionItem
    private MenuBuilder.ActionItem actionItem;
    public MenuBuilder.ActionItem getActionItem() { return actionItem; }
    public void setActionItem(MenuBuilder.ActionItem actionItem) { this.actionItem = actionItem; }
    //endregion

    public class Panel extends JPanel {
        public final JTextField textField;
        public Panel(){
            setFocusable(true);
            setLayout(new BorderLayout());

            textField = new JTextField();
            add(textField);

            textField.setEditable(false);

            SwingListener.onKeyReleased(textField, this::ks_keyReleased);
            SwingListener.onKeyPressed(textField, this::ks_keyPressed);
        }

        private boolean controlDown = false;
        private boolean shiftDown = false;
        private boolean altDown = false;
        private boolean metaDown = false;
        private boolean altGraphDown = false;
        private int keyCode;
        private char keyChar;
        private int keyEventType;

        public final List<BiConsumer<KeyStroke,KeyStroke>> onKeyStrokeChanged = new ArrayList<>();
        private KeyStroke keyStroke;
        public void keyStroke(KeyStroke ks){
            keyStroke(ks,true);
        }
        public void keyStroke(KeyStroke ks, boolean updateTextField){
            shiftDown = false;
            controlDown = false;
            altGraphDown = false;
            altDown = false;
            metaDown = false;
            keyCode = KeyEvent.VK_F1;

            var oldKs = this.keyStroke;
            this.keyStroke = ks;
            if( ks==null ){
                if( updateTextField )textField.setText("");
            }else{
                if( updateTextField )textField.setText(ks.toString());

                keyCode = ks.getKeyCode();
                keyChar = ks.getKeyChar();
                keyEventType = ks.getKeyEventType();

                shiftDown = (ks.getModifiers() & java.awt.event.InputEvent.SHIFT_DOWN_MASK )
                    == java.awt.event.InputEvent.SHIFT_DOWN_MASK;

                controlDown = (ks.getModifiers() & java.awt.event.InputEvent.CTRL_DOWN_MASK )
                    == java.awt.event.InputEvent.CTRL_DOWN_MASK;

                altDown = (ks.getModifiers() & java.awt.event.InputEvent.ALT_DOWN_MASK )
                    == java.awt.event.InputEvent.ALT_DOWN_MASK;

                altGraphDown = (ks.getModifiers() & java.awt.event.InputEvent.ALT_GRAPH_DOWN_MASK )
                    == java.awt.event.InputEvent.ALT_GRAPH_DOWN_MASK;

                metaDown = (ks.getModifiers() & java.awt.event.InputEvent.META_DOWN_MASK )
                    == java.awt.event.InputEvent.META_DOWN_MASK;
            }

            if( oldKs!=ks || (oldKs!=null && ks!=null && !Objects.equals(oldKs.toString(), ks.toString())) ){
                onKeyStrokeChanged.forEach( l -> l.accept(oldKs,ks));
            }
        }
        public KeyStroke keyStroke(){
            return keyStroke;
        }

        private boolean readingKeyStroke = true;
        public boolean isReadingKeyStroke() { return readingKeyStroke; }
        public void setReadingKeyStroke(boolean readingKeyStroke) { this.readingKeyStroke = readingKeyStroke; }

        private void ks_keyPressed(KeyEvent e){
            if( !isReadingKeyStroke() )return;

            if( e.getKeyCode()==KeyEvent.VK_CONTROL ){
                controlDown = true;
            }else if( e.getKeyCode()==KeyEvent.VK_ALT ){
                altDown = true;
            }else if( e.getKeyCode()==KeyEvent.VK_SHIFT ){
                shiftDown = true;
            }else if( e.getKeyCode()==KeyEvent.VK_ALT_GRAPH ){
                altGraphDown = true;
            }else if( e.getKeyCode()==KeyEvent.VK_META ){
                metaDown = true;
            }else {
                keyCode = e.getKeyCode();
            }
            rebuildKeyStroke();
        }
        private void ks_keyReleased(KeyEvent e){
            if( !isReadingKeyStroke() )return;

            if( e.getKeyCode()==KeyEvent.VK_CONTROL ){
                controlDown = false;
            }else if( e.getKeyCode()==KeyEvent.VK_ALT ){
                altDown = false;
            }else if( e.getKeyCode()==KeyEvent.VK_SHIFT ){
                shiftDown = false;
            }else if( e.getKeyCode()==KeyEvent.VK_ALT_GRAPH ){
                altGraphDown = false;
            }else if( e.getKeyCode()==KeyEvent.VK_META ){
                metaDown = false;
            }
            rebuildKeyStroke();
        }

        private void rebuildKeyStroke(){
            int mod = 0;
            if( controlDown )mod = mod | InputEvent.CTRL_DOWN_MASK;
            if( shiftDown )mod = mod | InputEvent.SHIFT_DOWN_MASK;
            if( altDown )mod = mod | InputEvent.ALT_DOWN_MASK;
            if( altGraphDown )mod = mod | InputEvent.ALT_GRAPH_DOWN_MASK;
            if( metaDown )mod = mod | InputEvent.META_DOWN_MASK;
            var ks = KeyStroke.getKeyStroke(keyCode,mod);
            keyStroke(ks);
        }
    }

    public static class FrameSettings {
        public boolean actionTextEditable = true;
    }
    public class Frame extends JFrame {
        public Frame(FrameSettings settings){
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setTitle("edit key stroke");

            //getContentPane().setLayout(new BorderLayout());
            var gbl = new GridBagLayout();
            getContentPane().setLayout(gbl);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 1;
            gbc.gridy = 3;
            gbc.weightx = 1;
            gbc.weighty = 0;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.ipadx = 120;
            getContentPane().add(editPanel, gbc);

            gbc = new GridBagConstraints();
            gbc.gridx = 10;
            gbc.gridy = 3;
            gbc.insets = new Insets(0,3,0,0);
            getContentPane().add(setButton, gbc);
            setButton.setText("set");

            gbc = new GridBagConstraints();
            gbc.gridx = 12;
            gbc.gridy = 3;
            gbc.insets = new Insets(0,3,0,3);
            getContentPane().add(resetButton, gbc);
            resetButton.setText("reset");

            gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.insets = new Insets(0,0,0,3);
            getContentPane().add(readKeyStroke, gbc);
            readKeyStroke.setText("reading");
            readKeyStroke.setToolTipText("reading key stroke");
            readKeyStroke.setSelected(true);

            if( settings!=null && settings.actionTextEditable ) {
                gbc = new GridBagConstraints();
                gbc.gridx = 1;
                gbc.gridy = 1;
                gbc.weightx = 1;
                gbc.gridwidth = 12;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(3, 0, 3, 3);
                getContentPane().add(actionText, gbc);

                gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 1;
                //gbc.weightx = 1;
                //gbc.gridwidth = 12;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(3, 3, 3, 3);
                getContentPane().add(new JLabel("text:"), gbc);
            }

            gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.insets = new Insets(2,3,5,3);
            gbc.weightx = 1;
            gbc.weighty = 1;
            gbc.gridwidth = 13;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.ipady = 50;
            getContentPane().add(new JScrollPane(message), gbc);

            Closeables txtFieldChanges = new Closeables();
            readKeyStroke.addActionListener(e -> {
                editPanel.setReadingKeyStroke(readKeyStroke.isSelected());
                txtFieldChanges.close();
                if( readKeyStroke.isSelected() ){
                    editPanel.textField.setEditable(false);
                    editPanel.textField.requestFocus();
                    editPanel.textField.setBackground(Color.white);
                    editPanel.textField.setForeground(Color.black);
                }else{
                    editPanel.textField.setEditable(true);
                    editPanel.textField.requestFocus();
                    editPanel.textField.setBackground(Color.white);
                    tryReReadKeyStroke();
                    txtFieldChanges.add(
                    SwingListener.onTextChanged(editPanel.textField, this::tryReReadKeyStroke));
                }
            });
            editPanel.textField.setBackground(Color.white);

            SwingListener.onActionPerformed(resetButton,ev->{
                editPanel.keyStroke(null);
            });

            message.setContentType("text/html");
            message.addHyperlinkListener(new HyperlinkListener() {
                @Override
                public void hyperlinkUpdate(HyperlinkEvent e) {
                    //System.out.println("hlink event "+e);
                    //System.out.println("  url "+e.getURL());
                    if( e.getEventType()== HyperlinkEvent.EventType.ACTIVATED ){
                        if( e.getURL()!=null && e.getURL().toString().startsWith("http://unlink?") ){
                            String[] actNames = e.getURL().toString().split("\\?",2);
                            if( actNames.length==2 ){
                                String actName = Text.urlDecode(actNames[1]);
                                if( actionSettings!=null ){
                                    var actSet = actionSettings.get(actName);
                                    if( actSet!=null ){
                                        actSet.setKeyStroke(null);
                                        System.out.println("unlink keystroke for "+actName);
                                    }
                                }
                            }
                        }
                    }
                }
            });

            editPanel.onKeyStrokeChanged.add((oldKs,newKs)->{
                if( newKs==null )return;
                message.setText("");
                message.setEditable(false);
                message.setContentType("text/html");
                try {
                    message.setPage("http://local");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                StringBuilder sb = new StringBuilder();
                if( actionSettings!=null ){
                    actionSettings.forEach( (actName,aset)->{
                        if(
                            aset!=null && aset.getKeyStroke()!=null && aset.getKeyStroke().equals(newKs.toString())
                            && actName!=null && !actName.equals(actionItem.name)
                        ){
                            sb.append("action <b>").append(
                                Text.htmlEncode(actName)
                            ).append("</b> matched")
                                .append(" <a href='").append("http://unlink?"+Text.urlDecode(actName)).append("'>unlink</a>")
                                .append("<br>");
                        }
                    });
                }
                message.setText(sb.toString());
            });

            pack();
        }

        public final Panel editPanel = new Panel();
        public final JButton setButton = new JButton();
        public final JButton resetButton = new JButton();
        public final JCheckBox readKeyStroke = new JCheckBox();
        public final JTextPane message = new JTextPane();
        public final JTextField actionText = new JTextField();

        private Optional<KeyStroke> readKeyStroke(String str){
            var ks = KeyStroke.getKeyStroke(str);
            return ks!=null ? Optional.of(ks) : Optional.empty();
        }
        private void tryReReadKeyStroke(){
            var ks = readKeyStroke(editPanel.textField.getText());
            if( ks.isPresent() ){
                editPanel.textField.setForeground(Color.black);
                editPanel.keyStroke(ks.get(),false);
            }else{
                editPanel.textField.setForeground(Color.red);
            }
        }
    }

    private static Optional<JMenuBar> menuBar(Component component){
        var jframe = GuiUtil.getJFrameOfComponent(component);
        if( jframe!=null ){
            var mbar = jframe.getJMenuBar();
            if( mbar!=null ){
                return Optional.of(mbar);
            }
        }
        return Optional.empty();
    }

    public static class MenuLocker {
        public MenuLocker(JMenuBar menuBar){
            if( menuBar==null )throw new IllegalArgumentException( "menuBar==null" );
            this.menuBar = menuBar;
        }

        private JMenuBar menuBar;
        public MenuLocker lock(){
            if(menuBar!=null){
                menuBar.setEnabled(false);
            }
            return this;
        }
        public MenuLocker lock(boolean lock){
            if(menuBar!=null){
                menuBar.setEnabled(!lock);
            }
            return this;
        }
        public MenuLocker unlock(){
            if(menuBar!=null){
                menuBar.setEnabled(false);
            }
            return this;
        }
        public boolean isLocked(){
            return menuBar != null && !menuBar.isEnabled();
        }
    }
    public static Optional<MenuLocker> menuLocker(Component component ){
        if( component==null )throw new IllegalArgumentException( "component==null" );
        return menuBar(component).map(MenuLocker::new);
    }

    public void edit(){
        if( actionSettings==null || registry==null || menuItem==null || actionItem==null )return;

        Optional<MenuLocker> menuLocker =
            menuLocker(menuItem);

        menuLocker.ifPresent(MenuLocker::lock);

        FrameSettings fset = new FrameSettings();
        fset.actionTextEditable = true;

        Frame frame = new Frame(fset);
        frame.setTitle("Edit key stroke of "+actionItem.name);
        frame.editPanel.keyStroke(menuItem.getAccelerator());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.toFront();
        frame.editPanel.textField.requestFocus();
        SwingListener.onActionPerformed(frame.setButton,ev -> {
            var aset = actionSettings.get(actionItem.name);
            if( aset!=null ){
                var ks = frame.editPanel.keyStroke();
                aset.setKeyStroke(
                    ks!=null ? ks.toString() : null
                );
            }
            frame.setVisible(false);
            frame.dispose();
        });

        SwingListener.onWindowClosing(frame, ev ->{
            menuLocker.ifPresent(MenuLocker::unlock);
        });

        var aset = actionSettings.get(actionItem.name);
        if( aset!=null ){
            frame.actionText.setText(aset.getText()!=null ? aset.getText() : "");
            SwingListener.onTextChanged(frame.actionText, () -> {
                aset.setText(frame.actionText.getText());
            });
        }
    }
}
