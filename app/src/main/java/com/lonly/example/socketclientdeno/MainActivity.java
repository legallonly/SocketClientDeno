package com.lonly.example.socketclientdeno;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText mEditText = null;
    private TextView mTextView = null;
    private Button mButton = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button)findViewById(R.id.button);
        mEditText = (EditText)findViewById(R.id.editText);
        mTextView = (TextView)findViewById(R.id.textView);

        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setTitle("测试Socket连接");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // 应用Server的IP和端口建立Socket对象
                            Socket socket = new Socket("192.168.199.120", 4567);
                            // 判断客户端和服务器是否连接成功
                            System.out.println(socket.isConnected()?"客户端和服务器连接成功！":"客户端和服务器连接失败！.");

                            // 将信息通过这个对象来发送给Server
                            PrintWriter out = new PrintWriter(new BufferedWriter(
                                    new OutputStreamWriter(socket.getOutputStream())),
                                    true);
                            // 把用户输入的内容发送给server
                            String message = mEditText.getText().toString();
                            if(TextUtils.isEmpty(message)){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "消息不能为空。", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return;
                            }
                            Log.e(TAG, "发送给服务端消息------》'" + message + "'");
                            out.println(message);
                            out.flush();


                            // 接收服务器信息
                            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(socket.getInputStream()));
                            // 得到服务器信息
                            final String msg = in.readLine();
                            Log.e(TAG, "接收到服务端消息《------'" + msg + "'");
                            // 在页面上进行显示
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mTextView.setText(msg);
                                }
                            });

                        } catch(UnknownHostException e) {
                            Log.e(TAG, "连接服务端失败!");
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}
