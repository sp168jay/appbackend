package com.example.abair.apptestbackend;

import android.os.AsyncTask;
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

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
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
//        HttpSend httas = new HttpSend();//產生一個 AsyncTask 的物件實體
//        httas.execute(send_txt.getText().toString());//執行這個非同步的任務。參數是我們要傳的文字。

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

//        String url = "http://www.google.com";
        String url = "http://192.168.43.90:8000/" + dataInput;//把編碼過的加到網路主機的網址後面

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

//    這個 activity 用到的類別，所以在類別中宣告即可
//    處理網路事件，會強制使用其他執行緒來處理，要不程式會當。這裹使用 AsyncTask
    private class HttpSend extends AsyncTask<String, Void, String>//這種 < > 寫法是 java template 的寫法，很進階的用法。請查一下～
    {
        @Override
        protected String doInBackground(String... str){//一定要實現的函式 execute 會呼叫這個函式
            //宣告幾個在整個函式中會用到的變數
            InputStream inputStream = null;
            HttpURLConnection urlConnection = null;

            try{
//                String get_url = "http://tw.yahoo.com/";//如果是用這個會得到 yahoo 網頁的內容

                str[0] = URLEncoder.encode(str[0], "utf-8");//傳給 HttpURLConnection 的網址內容，如果有中文，需要先編碼
                String get_url = "http://192.168.43.90:8000/" + str[0];//str是放我們要傳的資料
//
//                HttpClient Client = new DefaultHttpClient();//可以用來做類似瀏覽器的事情
//                HttpGet httpget;//會拿來放網址。
//                ResponseHandler<String> responseHandler = new BasicResponseHandler();//用來處理回傳的內容
//                httpget = new HttpGet(get_url);//放 http get 的指令
//                String content = Client.execute(httpget, responseHandler);//執行類似輸入網址然後按 enter 的動作，之後會得到 content
//
//                return content;

//                使用 HttpURLConnection 取代 HttpClient
//                URL url = new URL("http://www.google.com/");
                URL url = new URL(get_url);//連到之前我們的網址
                urlConnection = (HttpURLConnection) url.openConnection();//建立物件實體
                urlConnection.setRequestMethod("GET");//設定連線方式
                int statusCode = urlConnection.getResponseCode();//實際連線，並回傳狀態

                if( statusCode == 200 )
                {
                    //將資料流轉成字串
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());//處理回傳的資料流
                    BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( inputStream ) );//封裝成 reader
                    String line = "";
                    String result = "";

                    while((line = bufferedReader.readLine()) != null)//從 reader 中，一行一行讀取出資料，放到字串中
                    {
                        result += line;
                    }

                    if(inputStream != null)
                    {
                        inputStream.close();//釋放資料流的資源
                    }
                    return result;
                }
                else if(statusCode == 404)//處理找不到網頁的情況
                {
                    return "Page Not Found...";
                }
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
            return "Cannot Connect";
        }

        protected  void onPostExecute(String result)//這個會處理 response 回來的資料
        {
            TextView tv = (TextView) findViewById(R.id.show_text);//找到我們要拿來顯示資料的 UI 元件
            tv.setText(result);//設定這個元件的內容
        }

    }
}
