package xyz.cofe.game.tank;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import xyz.cofe.game.tank.geom.Rect;

public class RectCollisionTest {
    @Test
    public void test01(){
        var r1 = Rect.rect(0,0,10,10);
        var r2 = Rect.rect(5,5,15,15);
        var r3 = Rect.rect(15,15,25,25);

        var ir1_2 = r1.intersection(r2);
        System.out.println(ir1_2);

        var ir1_2b = r2.intersection(r1);
        System.out.println(ir1_2b);

        assertTrue(ir1_2.isPresent());
        assertTrue(ir1_2b.isPresent());

        var ir1_3 = r1.intersection(r3);
        System.out.println(ir1_3);
        assertTrue(ir1_3.isEmpty());
    }
}
