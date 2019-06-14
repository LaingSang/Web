package web.my.webapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/9/29.
 */

public class ErWeiActivity extends Activity {
    private ImageView img;
    private Button btnSave;
    private int w;
    private int h;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erwei);
        initView();
    }
    private void initView(){
        img=findViewById(R.id.erwei);
        btnSave=findViewById(R.id.btnsave);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        final String url=bundle.getString("weixinerwei");
     //   MyUtil.createQRImage(url,ErWeiActivity.this,img);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownPicUtil.downPic(url, new DownPicUtil.DownFinishListener(){

                    @Override
                    public void getDownPath(String s) {
                        Toast.makeText(ErWeiActivity.this,"保存成功",Toast.LENGTH_LONG).show();
                        Message msg = Message.obtain();
                        msg.obj=s;

                    }
                });
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
