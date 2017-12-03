package counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class BasicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
