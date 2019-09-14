package com.aill.serialportdemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aill.androidserialport.SerialPort;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
//    private ReadSerialPortMsgThread mReadThread;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private EditText editText1;
    private SerialPort serialPort;
    private StringBuffer seriapPortMsg = new StringBuffer();
    private EditText serMsg;
    private TextView log;
    private Context mContext;

    private void initSerialPort(){

        try {
            serialPort = new SerialPort(new File("/dev/ttyS1"), 9600, 0);
            mInputStream = serialPort.getInputStream();
            mOutputStream = serialPort.getOutputStream();
            //new Thread(new ReadSerialPortMsgThread()).start();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,"打开串口失败");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText1 =(EditText) findViewById (R.id.sendMsg);
        mContext = this;
        log= (TextView) findViewById(R.id.log);
        initSerialPort();
    }
/*
    //接受数据
    class ReadSerialPortMsgThread implements  Runnable{
        @Override
        public void run() {
            int size;
            byte buff[] = new byte[1024];
            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            while (true){
                try {
                    mInputStream = serialPort.getInputStream();
                    if(mInputStream==null){
                        return;
                    }
                    size = mInputStream.read(buff);
                    if(size<=0){
                        continue;
                    }
                    final String message = new String(buff,0,size);
                    Log.d(TAG,"接收到串口回调  "+message);
                    seriapPortMsg.append(message);
                    if(buff[size - 1] == '\n'){
                        log.post(new Runnable() {
                            @Override
                            public void run() {
                                //log.setText(sdf.format(new Date())+"接收到串口发送的指令  "+message);
                                log.setText(message);
                            }
                        });
                    }
                    log.setText(message);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
*/

    //发送数据
    public void sendText(View view){
            //从串口对象中获取输出流
            String msg = editText1.getText().toString()+"\r\n";
        try {
            if(msg!=null&&!msg.equals("")){
                byte [] buff = msg.getBytes();
                try {
                    //从串口对象中获取输出流
                    mOutputStream = serialPort.getOutputStream();
                    mOutputStream.write(buff,0,buff.length);
                    mOutputStream.flush();
                    Log.i(TAG ,"MSG："+msg);
                    Log.i(TAG ,"输出完成");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG,e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,"打开串口失败");
        }
    }
}
