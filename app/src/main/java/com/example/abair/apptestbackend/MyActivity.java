package com.example.abair.apptestbackend;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void on_send_click(View v)//按鈕對應對的函式
    {
//        System.out.println("你按了");//請查一下這個會印在哪裹
        EditText send_txt = (EditText) findViewById(R.id.senddata_txt);//findViewById 回傳的是 view，要告訴程式把回傳的看做 EditText。

        final TextView tv = (TextView) findViewById(R.id.show_text);//找到我們要拿來顯示資料的 UI 元件

        //使用 volley 來做網路資料的傳輸
        // 1) 先宣告一個隊列，用來放置要執行的請求
        RequestQueue queue = Volley.newRequestQueue(this);

        String dataInput = send_txt.getText().toString();//取得我們要額外加到網址的資料，即後端API
        try{
            dataInput = URLEncoder.encode(dataInput, "utf-8");//Volley 也需要將中文的網址做編碼，這個動作需要 try catch
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();//編碼若有錯誤，在這裹印出 exception
        }

        String API_TOKEN = "test";//根據主機上 user 的 token 來測試是否可以正常運作

//        String url = "http://www.google.com";
        //丟給 laravel 的 token 的使用方式：網址的最後加上 ?api_token=[使用者的 token 內容]
        String url = "http://192.168.43.90:8000/" + dataInput + "?api_token=" + API_TOKEN;//把編碼過的 dataInput 加到網路主機的網址後面

        //2) 定義要放到隊列中執行用的 StringRequest
        StringRequest stringRequest = new StringRequest(//需要 4 個參數
                Request.Method.GET,//定義請求的方式
                url,//執行請求用的網址
                new Response.Listener<String>(){//處理回應的字串用的匿名函式
                    @Override
                    public void onResponse(String response){//改寫處理的函式
                        tv.setText(response);//因為會用到外部的參數 tv，所以外部的參數 tv 要宣告成 final
                    }
                },
                new Response.ErrorListener(){//處理錯誤回應用的匿名函式
                    @Override
                    public void onErrorResponse(VolleyError error){//改寫處理的函式
                        tv.setText("回傳錯誤");
                    }
                }
        );

        //3) 把要執行的 StringRequest 加到隊列中執行
        queue.add(stringRequest);
    }
}
