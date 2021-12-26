package xyz.cofe.game.tank.ui;

import java.awt.event.KeyEvent;

/**
 * Код клавиши клавиатуры
 */
public enum KeyCode {
    __Undefined(-1),

    _0(KeyEvent.VK_0),_1(KeyEvent.VK_1),_2(KeyEvent.VK_2),_3(KeyEvent.VK_3),_4(KeyEvent.VK_4),
    _5(KeyEvent.VK_5),_6(KeyEvent.VK_6),_7(KeyEvent.VK_7),_8(KeyEvent.VK_8),_9(KeyEvent.VK_9),

    A(KeyEvent.VK_A),B(KeyEvent.VK_B),C(KeyEvent.VK_C),D(KeyEvent.VK_D),E(KeyEvent.VK_E),
    F(KeyEvent.VK_F),G(KeyEvent.VK_G),H(KeyEvent.VK_H),I(KeyEvent.VK_I),J(KeyEvent.VK_J),
    K(KeyEvent.VK_K),L(KeyEvent.VK_L),M(KeyEvent.VK_M),N(KeyEvent.VK_N),O(KeyEvent.VK_O),
    P(KeyEvent.VK_P),Q(KeyEvent.VK_Q),R(KeyEvent.VK_R),S(KeyEvent.VK_S),T(KeyEvent.VK_T),
    U(KeyEvent.VK_U),V(KeyEvent.VK_V),W(KeyEvent.VK_W),X(KeyEvent.VK_X),Y(KeyEvent.VK_Y),
    Z(KeyEvent.VK_Z),

    ENTER(KeyEvent.VK_ENTER),
    BACK_SPACE(KeyEvent.VK_BACK_SPACE),
    TAB(KeyEvent.VK_TAB),
    CANCEL(KeyEvent.VK_CANCEL),
    CLEAR(KeyEvent.VK_CLEAR),
    SHIFT(KeyEvent.VK_SHIFT),
    CONTROL(KeyEvent.VK_CONTROL),
    ALT(KeyEvent.VK_ALT),
    PAUSE(KeyEvent.VK_PAUSE),
    CAPS_LOCK(KeyEvent.VK_CAPS_LOCK),
    ESCAPE(KeyEvent.VK_ESCAPE),
    SPACE(KeyEvent.VK_SPACE),
    PAGE_UP(KeyEvent.VK_PAGE_UP),
    PAGE_DOWN(KeyEvent.VK_PAGE_DOWN),
    END(KeyEvent.VK_END),
    HOME(KeyEvent.VK_HOME),
    LEFT(KeyEvent.VK_LEFT),
    UP(KeyEvent.VK_UP),
    RIGHT(KeyEvent.VK_RIGHT),
    DOWN(KeyEvent.VK_DOWN),
    COMMA(KeyEvent.VK_COMMA),
    MINUS(KeyEvent.VK_MINUS),
    PERIOD(KeyEvent.VK_PERIOD),
    SLASH(KeyEvent.VK_SLASH),
    SEMICOLON(KeyEvent.VK_SEMICOLON),
    EQUALS(KeyEvent.VK_EQUALS),
    OPEN_BRACKET(KeyEvent.VK_OPEN_BRACKET),
    BACK_SLASH(KeyEvent.VK_BACK_SLASH),
    CLOSE_BRACKET(KeyEvent.VK_CLOSE_BRACKET),
    NUMPAD0(KeyEvent.VK_NUMPAD0),
    NUMPAD1(KeyEvent.VK_NUMPAD1),
    NUMPAD2(KeyEvent.VK_NUMPAD2),
    NUMPAD3(KeyEvent.VK_NUMPAD3),
    NUMPAD4(KeyEvent.VK_NUMPAD4),
    NUMPAD5(KeyEvent.VK_NUMPAD5),
    NUMPAD6(KeyEvent.VK_NUMPAD6),
    NUMPAD7(KeyEvent.VK_NUMPAD7),
    NUMPAD8(KeyEvent.VK_NUMPAD8),
    NUMPAD9(KeyEvent.VK_NUMPAD9),
    MULTIPLY(KeyEvent.VK_MULTIPLY),
    ADD(KeyEvent.VK_ADD),
    SEPARATER(KeyEvent.VK_SEPARATER),
    SEPARATOR(KeyEvent.VK_SEPARATOR),
    SUBTRACT(KeyEvent.VK_SUBTRACT),
    DECIMAL(KeyEvent.VK_DECIMAL),
    DIVIDE(KeyEvent.VK_DIVIDE),
    NUM_LOCK(KeyEvent.VK_NUM_LOCK),
    SCROLL_LOCK(KeyEvent.VK_SCROLL_LOCK),
    F1(KeyEvent.VK_F1),
    F2(KeyEvent.VK_F2),
    F3(KeyEvent.VK_F3),
    F4(KeyEvent.VK_F4),
    F5(KeyEvent.VK_F5),
    F6(KeyEvent.VK_F6),
    F7(KeyEvent.VK_F7),
    F8(KeyEvent.VK_F8),
    F9(KeyEvent.VK_F9),
    F10(KeyEvent.VK_F10),
    F11(KeyEvent.VK_F11),
    F12(KeyEvent.VK_F12),
    F13(KeyEvent.VK_F13),
    F14(KeyEvent.VK_F14),
    F15(KeyEvent.VK_F15),
    F16(KeyEvent.VK_F16),
    F17(KeyEvent.VK_F17),
    F18(KeyEvent.VK_F18),
    F19(KeyEvent.VK_F19),
    F20(KeyEvent.VK_F20),
    F21(KeyEvent.VK_F21),
    F22(KeyEvent.VK_F22),
    F23(KeyEvent.VK_F23),
    F24(KeyEvent.VK_F24),
    PRINTSCREEN(KeyEvent.VK_PRINTSCREEN),
    INSERT(KeyEvent.VK_INSERT),
    HELP(KeyEvent.VK_HELP),
    META(KeyEvent.VK_META),
    BACK_QUOTE(KeyEvent.VK_BACK_QUOTE),
    QUOTE(KeyEvent.VK_QUOTE),
    KP_UP(KeyEvent.VK_KP_UP),
    KP_DOWN(KeyEvent.VK_KP_DOWN),
    KP_LEFT(KeyEvent.VK_KP_LEFT),
    KP_RIGHT(KeyEvent.VK_KP_RIGHT),
    DEAD_GRAVE(KeyEvent.VK_DEAD_GRAVE),
    DEAD_ACUTE(KeyEvent.VK_DEAD_ACUTE),
    DEAD_CIRCUMFLEX(KeyEvent.VK_DEAD_CIRCUMFLEX),
    DEAD_TILDE(KeyEvent.VK_DEAD_TILDE),
    DEAD_MACRON(KeyEvent.VK_DEAD_MACRON),
    DEAD_BREVE(KeyEvent.VK_DEAD_BREVE),
    DEAD_ABOVEDOT(KeyEvent.VK_DEAD_ABOVEDOT),
    DEAD_DIAERESIS(KeyEvent.VK_DEAD_DIAERESIS),
    DEAD_ABOVERING(KeyEvent.VK_DEAD_ABOVERING),
    DEAD_DOUBLEACUTE(KeyEvent.VK_DEAD_DOUBLEACUTE),
    DEAD_CARON(KeyEvent.VK_DEAD_CARON),
    DEAD_CEDILLA(KeyEvent.VK_DEAD_CEDILLA),
    DEAD_OGONEK(KeyEvent.VK_DEAD_OGONEK),
    DEAD_IOTA(KeyEvent.VK_DEAD_IOTA),
    DEAD_VOICED_SOUND(KeyEvent.VK_DEAD_VOICED_SOUND),
    DEAD_SEMIVOICED_SOUND(KeyEvent.VK_DEAD_SEMIVOICED_SOUND),
    AMPERSAND(KeyEvent.VK_AMPERSAND),
    ASTERISK(KeyEvent.VK_ASTERISK),
    QUOTEDBL(KeyEvent.VK_QUOTEDBL),
    LESS(KeyEvent.VK_LESS),
    GREATER(KeyEvent.VK_GREATER),
    BRACELEFT(KeyEvent.VK_BRACELEFT),
    BRACERIGHT(KeyEvent.VK_BRACERIGHT),
    AT(KeyEvent.VK_AT),
    COLON(KeyEvent.VK_COLON),
    CIRCUMFLEX(KeyEvent.VK_CIRCUMFLEX),
    DOLLAR(KeyEvent.VK_DOLLAR),
    EURO_SIGN(KeyEvent.VK_EURO_SIGN),
    EXCLAMATION_MARK(KeyEvent.VK_EXCLAMATION_MARK),
    INVERTED_EXCLAMATION_MARK(KeyEvent.VK_INVERTED_EXCLAMATION_MARK),
    LEFT_PARENTHESIS(KeyEvent.VK_LEFT_PARENTHESIS),
    NUMBER_SIGN(KeyEvent.VK_NUMBER_SIGN),
    PLUS(KeyEvent.VK_PLUS),
    RIGHT_PARENTHESIS(KeyEvent.VK_RIGHT_PARENTHESIS),
    UNDERSCORE(KeyEvent.VK_UNDERSCORE),
    WINDOWS(KeyEvent.VK_WINDOWS),
    CONTEXT_MENU(KeyEvent.VK_CONTEXT_MENU),
    FINAL(KeyEvent.VK_FINAL),
    CONVERT(KeyEvent.VK_CONVERT),
    NONCONVERT(KeyEvent.VK_NONCONVERT),
    ACCEPT(KeyEvent.VK_ACCEPT),
    MODECHANGE(KeyEvent.VK_MODECHANGE),
    KANA(KeyEvent.VK_KANA),
    KANJI(KeyEvent.VK_KANJI),
    ALPHANUMERIC(KeyEvent.VK_ALPHANUMERIC),
    KATAKANA(KeyEvent.VK_KATAKANA),
    HIRAGANA(KeyEvent.VK_HIRAGANA),
    FULL_WIDTH(KeyEvent.VK_FULL_WIDTH),
    HALF_WIDTH(KeyEvent.VK_HALF_WIDTH),
    ROMAN_CHARACTERS(KeyEvent.VK_ROMAN_CHARACTERS),
    ALL_CANDIDATES(KeyEvent.VK_ALL_CANDIDATES),
    PREVIOUS_CANDIDATE(KeyEvent.VK_PREVIOUS_CANDIDATE),
    CODE_INPUT(KeyEvent.VK_CODE_INPUT),
    JAPANESE_KATAKANA(KeyEvent.VK_JAPANESE_KATAKANA),
    JAPANESE_HIRAGANA(KeyEvent.VK_JAPANESE_HIRAGANA),
    JAPANESE_ROMAN(KeyEvent.VK_JAPANESE_ROMAN),
    KANA_LOCK(KeyEvent.VK_KANA_LOCK),
    INPUT_METHOD_ON_OFF(KeyEvent.VK_INPUT_METHOD_ON_OFF),
    CUT(KeyEvent.VK_CUT),
    COPY(KeyEvent.VK_COPY),
    PASTE(KeyEvent.VK_PASTE),
    UNDO(KeyEvent.VK_UNDO),
    AGAIN(KeyEvent.VK_AGAIN),
    FIND(KeyEvent.VK_FIND),
    PROPS(KeyEvent.VK_PROPS),
    STOP(KeyEvent.VK_STOP),
    COMPOSE(KeyEvent.VK_COMPOSE),
    ALT_GRAPH(KeyEvent.VK_ALT_GRAPH),
    BEGIN(KeyEvent.VK_BEGIN),
    UNDEFINED(KeyEvent.VK_UNDEFINED),

    ;
    public final int code;
    KeyCode(int code){
        this.code = code;
    }
}
