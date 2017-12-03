package counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.url.Url;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.utils.LogUtil;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.utils.ToastUtils;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends BasicActivity {
    // 账号输入框
    private EditText account;
    // 密码输入框
    private EditText password;
    // 登陆按钮
    private Button logging;
    // 输入框字符串
    private String userString;
    // 密码框字符串
    private String passwordString;
    // 是否记住密码单选框
    private CheckBox cb_remember_password;
    //
    private SharedPreferences pref;
    // 账号密码错误提示
   // private TextView account_password_wrong_tips;
    // 是否记住密码
    private boolean isNeedRember;

    private SharedPreferences.Editor editor;
    // 单选纽 评委
    private RadioButton rb_comission;
    // 单选纽 仲裁
    private RadioButton rb_arbitrary;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = PreferenceManager.getDefaultSharedPreferences(this);


        // 获取控件
        getViews();

        // 检查数据
        checkData();

        // 加监听
        addListener();

    }

    /**
     *
     */
    private void showProgress(){
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("亲，系统正在登陆哦");
        progressDialog.setMessage("loading...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    /**
     *
     */
    private void closeProgress(){
        if(progressDialog != null){
            progressDialog.cancel();
        }
    }

    /**
     * 添加监听
     */
    private void addListener() {
        logging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 转loading条
                showProgress();

                // 获取 account 和 password
                userString = account.getText().toString().trim();
                passwordString = password.getText().toString().trim();
                isNeedRember = cb_remember_password.isChecked();

                if(userString.equals("") || passwordString.equals("")){
                    ToastUtils.show(MainActivity.this, "请检查账号密码后重新登录!", 1);
                }

                LogUtil.LogUtil("after username : ", userString);
                LogUtil.LogUtil("after password : ", passwordString);

                // 192.168.1.100:80/app/login
                sendRequestWithOkHttp(userString, passwordString, Url.URL_BASIC + Url.URL_LOGIN);
            }
        });
    }

    // 向下一个界面传递信息
    private void checkData() {
        // 检查是否存储有账号还有密码
        boolean isremember = pref.getBoolean("remember_password", false);

        if(isremember){
            account.setText(pref.getString("account", ""));
            password.setText(pref.getString("password", ""));
            cb_remember_password.setChecked(true);
            if(pref.getString("user", "").equals(MyApplication.comission)){
                rb_comission.setChecked(true);
            }else{
                rb_arbitrary.setChecked(true);
            }
        }
    }

    /**
     * 获取控件
     */
    private void getViews() {
        account = (EditText)findViewById(R.id.et_username);
        password = (EditText)findViewById(R.id.et_password);
        logging = (Button)findViewById(R.id.btn_logging);
        cb_remember_password = (CheckBox)findViewById(R.id.cb_remember_password);
        // account_password_wrong_tips = (TextView)findViewById(R.id.tv_account_password_wrong_tip);
        rb_comission = (RadioButton)findViewById(R.id.rb_comission_main_activity);
        rb_arbitrary =  (RadioButton)findViewById(R.id.rb_arbitrary_main_activity);
    }

    /**
     *
     * 请求数据
     *
     * @param account
     * @param password
     * @param url
     */
    private void sendRequestWithOkHttp(final String account, final String password, final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {

                RequestBody formBody = new FormBody.Builder().add("account", account)
                                                                   .add("password", password)
                                                                   .build();

                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)//10秒连接超时
                        .writeTimeout(30, TimeUnit.SECONDS)//10m秒写入超时
                        .readTimeout(30, TimeUnit.SECONDS)//10秒读取超时
                        .cookieJar(new CookieJar() {

                    private ConcurrentHashMap<String, List<Cookie>> cookieStore = new ConcurrentHashMap<>();
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url.host(), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                }).build();

                Request request = new Request.Builder().url(url).post(formBody).build();

                try {

                    Response response = okHttpClient.newCall(request).execute();

                    /**
                     * 登陆成功
                     */
                    if(response.isSuccessful()){
                        // 获取数据

                        String responseData = response.body().string();

                        // 仲裁
                        if(responseData.equals("2") && rb_arbitrary.isChecked()) {
                            MyApplication.currentUser = MyApplication.arbitrate;
                            MyApplication.currentId = account;
                            editor = pref.edit();
                            if (isNeedRember) {
                                editor.putBoolean("remember_password", true);
                                editor.putString("account", userString);
                                editor.putString("password", passwordString);
                                editor.putString("user", MyApplication.currentUser);
                                editor.commit();
                            } else {
                                editor.clear();
                                editor.commit();
                            }
                            editor.apply();

                            Log.i("login", responseData);
                            closeProgress();

                            Intent intent = new Intent(MainActivity.this, ItemSelectActivity.class);
                            startActivity(intent);

                        }else if(responseData.equals("3") && rb_comission.isChecked()){
                            MyApplication.currentUser = MyApplication.comission;
                            MyApplication.currentId = account;
                            editor = pref.edit();
                            if (isNeedRember) {
                                editor.putBoolean("remember_password", true);
                                editor.putString("account", userString);
                                editor.putString("password", passwordString);
                                editor.putString("user", MyApplication.currentUser);
                                editor.commit();
                            } else {
                                editor.clear();
                                editor.commit();
                            }
                            editor.apply();

                            Log.i("login", responseData);
                            closeProgress();

                            Intent intent = new Intent(MainActivity.this, ItemSelectActivity.class);
                            startActivity(intent);

                        }else{
                            //失败
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "登录失败,请检查账号密码后重新登录!", Toast.LENGTH_SHORT).show();
                                    closeProgress();
                                }
                            });
                        }

                    }else{
                        //失败

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "登录失败,请检查账号密码后重新登录!", Toast.LENGTH_SHORT).show();
                                closeProgress();
                            }
                        });

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 解析json数据
     * @param responseData
     */
    private void parseJsonWithJSONObject(String responseData) {
        try {
            JSONArray jsonArray = new JSONArray(responseData);
            for(int i = 0; i < jsonArray.length(); ++i){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // 大赛名称
                MyApplication.competitionName = jsonObject.getString("");
                // 角色编号
                MyApplication.currentId = jsonObject.getString("");
                //

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }









}
