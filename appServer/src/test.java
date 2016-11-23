import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by crcrcry on 2016/11/22.
 */
public class test {
    public static void main(String[] args){
        try {
            Robot ttt = new Robot();

/*
            ttt.keyPress(KeyEvent.VK_CONTROL);
            ttt.keyPress(KeyEvent.VK_SPACE);
            ttt.keyRelease(KeyEvent.VK_SPACE);
            ttt.delay(1000);
            ttt.keyPress(KeyEvent.VK_SPACE);
            ttt.keyRelease(KeyEvent.VK_SPACE);
            ttt.delay(1000);
            ttt.keyPress(KeyEvent.VK_SPACE);
            ttt.keyRelease(KeyEvent.VK_SPACE);
            ttt.delay(1000);
            ttt.keyPress(KeyEvent.VK_SPACE);
            ttt.keyRelease(KeyEvent.VK_SPACE);
            ttt.delay(1000);ttt.keyPress(KeyEvent.VK_SPACE);
            ttt.keyRelease(KeyEvent.VK_SPACE);
            ttt.delay(1000);


*/
            ttt.keyPress(KeyEvent.VK_META);
            ttt.delay(500);
            System.out.println("按下command了");
            ttt.keyPress(KeyEvent.VK_TAB);
            ttt.keyPress(KeyEvent.VK_M);
            ttt.delay(500);
            System.out.println("按下m了");
            //ttt.keyRelease(KeyEvent.VK_M);
            //ttt.keyRelease(KeyEvent.VK_CIRCUMFLEX);


        }catch (AWTException e){
            System.out.println(e);
        }


    }
}
