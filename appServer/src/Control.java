import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by crcrcry on 2016/11/21.
 */

public class Control {
    private static final double PI = 3.1415926;

    private Dimension dim; //存储屏幕尺寸
    private Robot robot;//自动化对象
    private int Sensitivity = 2;
    private int screen_width, screen_height;
    private double lastDist = 0;

    private int threeWhere = 0;
    private boolean flag = true;

    public Control(){
        dim = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("屏幕大小为：" + dim.getWidth() + " " + dim.getHeight());
        screen_width = (int)dim.getWidth();
        screen_height = (int)dim.getHeight();
        try{
            robot = new Robot();
        }catch(AWTException e){
            e.printStackTrace();
        }
    }

    private void Move(Double width,Double height){    //鼠标移动函数
        Point mousepoint = MouseInfo.getPointerInfo().getLocation();
        Double perX = Sensitivity*width;
        Double perY = Sensitivity*height;
        Double nextX = mousepoint.x+perX;
        Double nextY = mousepoint.y+perY;
        try{
            if(nextX >= screen_width){
                nextX = screen_width-1.0;
            }else if(nextX <= 0){
                nextX = 1.0;
            }

            if(nextY >= screen_height){
                nextY = screen_height-1.0;
            }else if(nextY <= 0){
                nextY = 1.0;
            }

            robot.delay(2);
            robot.mouseMove(nextX.intValue(), nextY.intValue());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void Click(int button){
        Point mousepoint = MouseInfo.getPointerInfo().getLocation();
        robot.mouseMove(mousepoint.x,mousepoint.y);
        robot.mousePress(button);
    }

    private void Release(int button){
        Point mousepoint = MouseInfo.getPointerInfo().getLocation();
        robot.mouseMove(mousepoint.x,mousepoint.y);
        robot.mouseRelease(button);
    }

    /*处理socket传递的字符串信息*/
    public void dealStr(String str){
        String[] part = str.split("&");
        switch (part[0]){
            case "1":{
                flag = true;
                threeWhere = 0;
                if(part[1].equals("0")){
                    this.Click(InputEvent.BUTTON1_DOWN_MASK);
                    this.Release(InputEvent.BUTTON1_DOWN_MASK);
                }else if(part[1].equals("1")){
                    //this.Release(InputEvent.BUTTON1_DOWN_MASK);
                }else{
                    double _x = Double.parseDouble(part[2]);
                    double _y = Double.parseDouble(part[3]);
                    this.Move(_x,_y);
                }
                break;
            }

            //case 2有问题，传过来的是相对向量，dist计算有问题
            case "2":{
                threeWhere = 0;

                if(part[1].equals("0")){

                }else if(part[1].equals("1")){

                }else{
                    double _x1 = Double.parseDouble(part[2]);
                    double _y1 = Double.parseDouble(part[3]);
                    double _x2 = Double.parseDouble(part[4]);
                    double _y2 = Double.parseDouble(part[5]);
                    double angle_cos = angle(_x1,_y1,_x2,_y2);
                    if(Math.acos(angle_cos) >= PI/2 && Math.acos(angle_cos) <= PI){
                        //放大缩小
                        double nowDist = distance(_x1,_y1,_x2,_y2);
                        if(nowDist > lastDist){
                            //放大
                        }else{
                            //缩小

                        }
                        lastDist = nowDist;
                    }else{
                        //滚动
                        double angle2 = Math.acos(angle(_x1,_y1,1,0));//和(1,0)向量的夹角
                        if(angle2 >= PI/4 && angle2 <= 3*PI/4){
                            //上下滚动
                            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                            if(_y1 < 0) robot.mouseWheel(-1);
                            else        robot.mouseWheel(1);

                            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

                        }else{
                            //左右滚动
//                            if(_x1 > 0 && flag){
//                                robot.keyPress(KeyEvent.VK_RIGHT);
//                                robot.keyRelease(KeyEvent.VK_RIGHT);
//                                flag = false;
//                            }else{
//                                if(flag){
//                                    for(int i = 0; i < 2; i++){
//
//                                    }
//
//                                    flag = false;
//                                }
//
//                            }
                        }
                    }
                }

                break;
            }

            case "3":{
                //三指的情况，不必记录中间的手指数据，因为三指是同向移动
                //1:left 2:right 3:up 4:down
                if(part[1].equals("2")){
                    double _x1 = Double.parseDouble(part[2]);
                    double _y1 = Double.parseDouble(part[3]);
                    double angle = Math.acos(angle(_x1,_y1,1,0));//和(1,0)向量的夹角
                    if(angle >= PI/4 && angle <= 3*PI/4){
                        //上下滚动
                        if(_y1 > 0){
                            if(threeWhere == 4){
                                //do nothing
                            }else{
                                if(threeWhere == 3 || threeWhere == 0){
                                    System.out.println("down???");
                                    threeWhere = 4;
                                    _3Fingers(4);
                                }

                            }

                        }else{
                            if(threeWhere == 3){

                            }else{
                                if(threeWhere == 4 || threeWhere == 0){
                                    System.out.println("up???");
                                    threeWhere = 3;
                                    _3Fingers(3);
                                }
                            }

                        }
                    }else{
                        //左右滚动
                        if(_x1 > 0){
                            if (threeWhere == 2){

                            }else{
                                if (threeWhere == 0 || threeWhere == 1){
                                    System.out.println("right???");
                                    threeWhere = 2;
                                    _3Fingers(2);
                                }
                            }
                        }else{
                            if (threeWhere == 1){

                            }else{
                                if (threeWhere == 0 || threeWhere == 2){
                                    System.out.println("left???");
                                    threeWhere = 1;
                                    _3Fingers(1);
                                }
                            }
                        }
                    }
                }else{

                }


                break;
            }
            /*
            case "4":{
                //1:四指散开

                //2:四指聚合

                break;
            }
            */
        }
    }

    private double distance(double x1, double y1, double x2, double y2){
        return Math.sqrt((y2-y1)*(y2-y1) + (x2-x1)*(x2-x1));
    }

    private double angle(double x1, double y1, double x2, double y2){
        double ab = x1 * x2 + y1 * y2;
        double a = Math.sqrt(x1*x1 + y1*y1);
        double b = Math.sqrt(x2*x2 + y2*y2);
        return ab/(a*b);
    }

    private void _3Fingers(int flag){
        robot.keyPress(KeyEvent.VK_CONTROL);
        switch (flag){
            case 1:{
                /*itune last*/
                robot.keyPress(KeyEvent.VK_META);
                robot.delay(10);
                for(int i = 0; i < 1; i++){
                    robot.keyPress(KeyEvent.VK_LEFT);
                    robot.keyRelease(KeyEvent.VK_LEFT);
                }
                robot.keyRelease(KeyEvent.VK_META);
                break;
            }
            case 2:{
                /*itune last*/
                robot.keyPress(KeyEvent.VK_META);
                robot.delay(10);
                for(int i = 0; i < 1; i++){
                    robot.keyPress(KeyEvent.VK_RIGHT);
                    robot.keyRelease(KeyEvent.VK_RIGHT);
                }
                robot.keyRelease(KeyEvent.VK_META);
                break;
            }
            case 3:{
                /*itune声音增大*/
                robot.keyPress(KeyEvent.VK_META);
                robot.delay(10);
                for(int i = 0; i < 3; i++){
                    robot.keyPress(KeyEvent.VK_UP);
                    robot.keyRelease(KeyEvent.VK_UP);
                }
                robot.keyRelease(KeyEvent.VK_META);
                break;
            }
            case 4:{
                /*itune减小*/
                robot.keyPress(KeyEvent.VK_META);
                robot.delay(100);
                for(int i = 0; i < 3; i++){
                    robot.keyPress(KeyEvent.VK_DOWN);
                    robot.keyRelease(KeyEvent.VK_DOWN);
                }
                robot.keyRelease(KeyEvent.VK_META);
                break;
            }
        }
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    private void _4fingers(int flag){
        if(flag == 1){
            robot.keyPress(KeyEvent.VK_F11);
            robot.keyRelease(KeyEvent.VK_F11);
        }else if(flag == 2){
            robot.keyPress(KeyEvent.VK_F4);
            robot.keyRelease(KeyEvent.VK_F4);
        }
    }
}

/*
传递信息解析方式：
&分割信息
第一个信息表示：几根手指
第二个信息表示：按下0、放开1、移动2
第三四、五六个信息表示：1、3手指向量/2、4手指坐标（第二个信息是2的情况下）
注意：四指的时候，八个点都要给
*/