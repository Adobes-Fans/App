import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class test2
{
    //定义该图形中所需的组件的引用
    private Frame f;
    private Button bt;
    private TextField tf;

    //方法
    test2()//构造方法
    {
        madeFrame();
    }

    public void madeFrame()
    {
        f = new Frame("My Frame");

        //对Frame进行基本设置。
        f.setBounds(300,100,600,500);//对框架的位置和大小进行设置
        f.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));//设计布局

        bt = new Button("My Button");
        tf = new TextField(20);

        //将组件添加到Frame中
        f.add(tf);
        f.add(bt);

        //加载一下窗体上的事件
        myEvent();

        //显示窗体
        f.setVisible(true);
    }

    private void myEvent()
    {
        f.addWindowListener(new WindowAdapter()//窗口监听
        {
            public void windowClosing(WindowEvent e)
            {
                System.out.println("窗体执行关闭！");
                System.exit(0);
            }
        });
        bt.addKeyListener(new KeyAdapter()//键盘监听按钮
        {
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
                    System.exit(0);
                    //组合键
                else if(e.isControlDown()&&e.getKeyCode()==KeyEvent.VK_ENTER)
                    System.exit(0);
                else System.out.println(e.getKeyChar()+"..."+KeyEvent.getKeyText(e.getKeyCode()));
            }

        });
        tf.addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent e)
            {
                int code = e.getKeyCode();
                if(!(code>=KeyEvent.VK_0&&code<=KeyEvent.VK_9))
                {
                    System.out.println(code+"..."+"是非法的");
                    e.consume();
                }
            }
        });
    }

    public static void main(String[] agrs)
    {
        new test2();
    }
}