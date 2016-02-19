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

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

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
        HttpSend httas = new HttpSend();//產生一個 AsyncTask 的物件實體
        httas.execute(send_txt.getText().toString());//執行這個非同步的任務。參數是我們要傳的文字。
    }

//    這個 activity 用到的類別，所以在類別中宣告即可
//    處理網路事件，會強制使用其他執行緒來處理，要不程式會當。這裹使用 AsyncTask
    private class HttpSend extends AsyncTask<String, Void, String>//這種 < > 寫法是 java template 的寫法，很進階的用法。請查一下～
    {
        @Override
        protected String doInBackground(String... str){//一定要實現的函式 execute 會呼叫這個函式
            try{
//                String get_url = "http://tw.yahoo.com/";//如果是用這個會得到 yahoo 網頁的內容
                String get_url = "http://192.168.32.136:8000/" + str[0];//str是放我們要傳的資料

                HttpClient Client = new DefaultHttpClient();//可以用來做類似瀏覽器的事情
                HttpGet httpget;//會拿來放網址。
                ResponseHandler<String> responseHandler = new BasicResponseHandler();//用來處理回傳的內容
                httpget = new HttpGet(get_url);//放 http get 的指令
                String content = Client.execute(httpget, responseHandler);//執行類似輸入網址然後按 enter 的動作，之後會得到 content

                return content;
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
