package com.example.crcrcry.touchpad;

import java.util.Date;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import java.io.*;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    //屏幕控件
    private int width, height;
    private Button start;
    private EditText ip;
    private com.example.crcrcry.touchpad.MyView pad;

    //变量
    private Socket socket;
    private PrintWriter socketOut;
    private int fingers = 0;
    private StringBuffer x;
    private int[] lastPoint = new int[8];
    private long startTime = 0, thisTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取安卓屏幕数据
        DisplayMetrics screen = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(screen);
        width = screen.widthPixels;
        height = screen.heightPixels;

        //初始化屏幕控件
        start = (Button) findViewById(R.id.start);
        pad = (com.example.crcrcry.touchpad.MyView) findViewById(R.id.pad);
        ip = (EditText) findViewById(R.id.ip);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //多线程socket，main线程无法建立socket
                new Thread(){


                    @Override
                    public void run(){
                        try{
                            //start.setClickable(false);
                            //建立socket连接
                            String ip_str = ip.getText().toString();        //10.111.230.175
                            //String ip_str = "10.180.35.148";
                            socket = new Socket(ip_str,11111);
                            socketOut = new PrintWriter(socket.getOutputStream());
                            System.out.println("Success socket");

                            //监听触摸事件
                            pad.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {

                                    thisTime = new Date().getTime();
                                    if((thisTime%300)>290){
                                        System.out.println("Forbidden time");
                                        return false;
                                    }


                                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                                        //单指按下
                                        case MotionEvent.ACTION_DOWN:{
                                            fingers = 1;

                                            lastPoint[0] = (int)event.getX();
                                            lastPoint[1] = (int)event.getY();

                                            String str = String.format("%d&%d",1,0);
                                            x = new StringBuffer(str);

                                            break;
                                        }
                                        //多指按下
                                        case MotionEvent.ACTION_POINTER_DOWN:{
                                            fingers++;
                                            if(fingers == 2){
                                                lastPoint[2] = (int) event.getX(1);
                                                lastPoint[3] = (int) event.getY(1);
                                            }else if(fingers == 3){
                                                lastPoint[4] = (int) event.getX(2);
                                                lastPoint[5] = (int) event.getY(2);
                                            }
                                            else if(fingers == 4){
                                                lastPoint[6] = (int) event.getX(3);
                                                lastPoint[7] = (int) event.getY(3);
                                            }
                                            break;
                                        }
                                        //单指放开
                                        case MotionEvent.ACTION_UP:{
                                            fingers = 0;

                                            String str = String.format("%d&%d",1,1);
                                            x = new StringBuffer(str);

                                            break;
                                        }
                                        //多指放开
                                        case MotionEvent.ACTION_POINTER_UP:{
                                            fingers--;

                                            break;
                                        }
                                        //移动
                                        case MotionEvent.ACTION_MOVE:{

                                            switch (fingers){
                                                case 1:{
                                                    String str = String.format("1&2&%d&%d",(int)(event.getX()-lastPoint[0]),(int)(event.getY()-lastPoint[1]));
                                                    x = new StringBuffer(str);
                                                    lastPoint[0] = (int) event.getX();
                                                    lastPoint[1] = (int) event.getY();
                                                    break;
                                                }
                                                case 2:{
                                                    String str = String.format("2&2&%d&%d&%d&%d",(int)(event.getX(0)-lastPoint[0]),(int)(event.getY(0)-lastPoint[1]),(int)(event.getX(1)-lastPoint[2]),(int)(event.getY(1)-lastPoint[3]));
                                                    x = new StringBuffer(str);
                                                    lastPoint[0] = (int) event.getX(0);
                                                    lastPoint[1] = (int) event.getY(0);
                                                    lastPoint[2] = (int) event.getX(1);
                                                    lastPoint[3] = (int) event.getY(1);
                                                    break;
                                                }
                                                case 3:{
                                                    String str = String.format("3&2&%d&%d",(int)(event.getX(0)-lastPoint[0]),(int)(event.getY(0)-lastPoint[1]));
                                                    x = new StringBuffer(str);
                                                    lastPoint[0] = (int) event.getX(0);
                                                    lastPoint[1] = (int) event.getY(0);
                                                    lastPoint[2] = (int) event.getX(1);
                                                    lastPoint[3] = (int) event.getY(1);
                                                    lastPoint[4] = (int) event.getX(2);
                                                    lastPoint[5] = (int) event.getY(2);
                                                    break;
                                                }
                                                case 4:{

                                                    break;
                                                }
                                            }

                                            break;
                                        }
                                    }

                                    socketOut.println(x);
                                    socketOut.flush();
                                    System.out.println("Client: " + x);


                                    return false;
                                }
                            });


                        }catch (IOException e){
                            System.out.println("Create Socket Error!");
                        }
                    }
                }.start();
            }
        });

    }
}
