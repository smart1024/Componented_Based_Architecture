package lilin.com.member;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import lilin.com.annotation.BindPath;
import lilin.com.arouter.ARouter;

@BindPath("member/member")
public class MemberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
    }

    public void jumpActivity(View view) {
        ARouter.getInstance().jumpActivity("main/main", null);
    }
}
