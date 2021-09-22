package xyz.cofe.game.tank.ui.cmd;

public class RunGCCommand implements Runnable {
    @Override
    public void run() {
        System.gc();
    }
}
