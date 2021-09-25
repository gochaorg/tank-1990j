package xyz.cofe.game.tank.ui.cmd;

import xyz.cofe.game.tank.sprite.SpritesData;
import xyz.cofe.game.tank.unt.Note;
import xyz.cofe.game.tank.unt.Water;

public class ConvertToNoteAction extends ConvertToAction<Note> {
    public ConvertToNoteAction(){
        super(Note::new);
    }
}
