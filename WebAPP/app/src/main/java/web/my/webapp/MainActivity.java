package web.my.webapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.FileNotFoundException;

import jiankong.jk.makeupanimation.Animators;

public class MainActivity extends Activity {
    private WebView wbv;
    /* private TextView txtTitle;http://ssctest.lot-center.com
     private TextView imgBack;
     private ImageView imgmore;*/
    private ImageView imgLOGO;
    private String path = "baidu.com";
    private long exitTime = 0;
    private boolean sws = true;

    private String imgurl = "";
    private int saveX;
    private int saveY;

    private TextView txtWangluo;
    private Handler handlers=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1001){
                imgLOGO.setVisibility(View.GONE);
            }
        }
    };
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String picFile = (String) msg.obj;
            String[] split = picFile.split("/");
            String fileName = split[split.length-1];
            try {
                MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), picFile, fileName, null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
            getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + picFile)));
            Toast.makeText(MainActivity.this,"图片保存图库成功",Toast.LENGTH_LONG).show();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        wbv = findViewById(R.id.web);
        imgLOGO=findViewById(R.id.logo);
        txtWangluo=findViewById(R.id.wangluo);
/*        imgmore=(ImageView)findViewById(R.id.more);
        imgmore.setOnClickListener(listener);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        imgBack = (TextView) findViewById(R.id.back);
        imgBack.setOnClickListener(listener);*/
        Urls(path, wbv);

        handlers.sendEmptyMessageDelayed(1001,3000L);
       /* //接口地址
        String path="http://gl.sljlnn.com/home/index2";
        //数据集合
        Map<String,String> testdata=new HashMap<>();
        //数据
        String ss="sdasdasd";
        //添加数据 ps:""里面的值需要跟后台给的值一样
        testdata.put("sdasd",ss);
        //使用工具调用post进行访问接口 获取返回值
        UrlHttpUtil.post(path, testdata,new CallBackUtil.CallBackString() {
            @Override
            public void onFailure(int code, String errorMessage) {

            }
            //返回值
            @Override
            public void onResponse(String response) {
                //显示返回数据
                Log.e("shuju",response);

            }
        });*/

    }
    private void initWebView(WebView wbv){
        // TODO Auto-generated method stub
        wbv.setInitialScale(5);
        WebSettings webSettings = wbv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(true);
        webSettings.setDisplayZoomControls(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setLoadsImagesAutomatically(true);
        wbv.clearCache(true);

    }
    @SuppressLint("SetJavaScriptEnabled")
    private void Urls(String path, final WebView wbv){
        sws = true;
        if (sws) {
            path = "http://" + path;
        }
        initWebView(wbv);
        wbv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final WebView.HitTestResult hitTestResult = wbv.getHitTestResult();
                if(hitTestResult.getType()== WebView.HitTestResult.IMAGE_TYPE||hitTestResult.getType()== WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE){
                    popupwindowSave(view);
                }
                return false;
            }
        });
        wbv.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                WebView newWebView =new WebView(MainActivity.this);
                view.addView(newWebView);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();
                newWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        //去掉注释使用系统浏览器打开
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                        browserIntent.setData(Uri.parse(url));
                        startActivity(browserIntent);
                        //  view.loadUrl(url);
                        return true;
                    }
                });
                return true;
            }
        });
        wbv.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView webView, String s) {
                //  wbv.loadUrl("javascript: var allLinks = document.getElementsByTagName('a'); if (allLinks) {var i;for (i=0; i<allLinks.length; i++) {var link = allLinks[i];var target = link.getAttribute('target'); if (target && target == '_blank') {link.href = 'newtab:'+link.href;link.setAttribute('target','_self');}}}");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                Log.e("协议",url);
                if (url.contains("alipayqr://")){
                    boolean isAli=isAlipayQR();
                    if (isAli){
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                }else if (url.contains("alipays://")){
                    boolean isAli=isAlipay();
                    if (isAli){
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                }else if (url.startsWith("newtab:")){
                    url=url.replace("newtab:","");
                    view.loadUrl(url);
                }else{
                    view.loadUrl(url);
                }
                return true;
            }
        });
        wbv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                saveX=(int)motionEvent.getX();
                saveY=(int)motionEvent.getY();
                return false;
            }
        });

        wbv.loadUrl(path);
        wbv.getSettings().setBlockNetworkImage(false);
        sws = false;
    }
    /*
     *
     * */
    public class MyWebChromeClient extends WebChromeClient{
        @Override
        public void onCloseWindow(WebView webView) {
            super.onCloseWindow(webView);
        }

        @Override
        public boolean onCreateWindow(WebView webView, boolean b, boolean b1, Message message) {
            WebView chidView=new WebView(MainActivity.this);
            chidView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                    webView.loadUrl(s);
                    return true;
                }
            });
            final WebSettings settings=chidView.getSettings();
            settings.setJavaScriptEnabled(true);
            chidView.setWebChromeClient(this);
            WebView.WebViewTransport transport= (WebView.WebViewTransport) message.obj;
            transport.setWebView(chidView);
            message.sendToTarget();
            return true;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wbv.canGoBack()) {
            wbv.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private boolean isAlipay(){
        Uri uri=Uri.parse("alipays://platformapi/startApp");
        Intent intent=new Intent(Intent.ACTION_VIEW,uri);
        ComponentName cname=intent.resolveActivity(getPackageManager());
        return  cname!=null;

    }
    private boolean isAlipayQR(){
        Uri uri=Uri.parse("alipayqr://platformapi/startApp");
        Intent intent=new Intent(Intent.ACTION_VIEW,uri);
        ComponentName cname=intent.resolveActivity(getPackageManager());
        return  cname!=null;

    }
    /*  private void popupwindowMore(View view) {
          View v=LayoutInflater.from(MainActivity.this).inflate(R.layout.popupwindow_more,null);
          TextView txtmore=(TextView)v.findViewById(R.id.more);
          PopupWindow more;
          more=new PopupWindow(dip2px(80),dip2px(30));
          more.setContentView(v);
          more.setFocusable(true);

      }*/
    private void popupwindowSave(View view) {
        final View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.popupwindow_save, null);
        TextView txtsave = (TextView) v.findViewById(R.id.saveimg);
        final WebView.HitTestResult hitTestResult = wbv.getHitTestResult();
        final PopupWindow save;
        save = new PopupWindow(dip2px(120), dip2px(40));
        save.setContentView(v);
        save.setFocusable(true);
        save.setOutsideTouchable(true);
        save.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        save.showAtLocation(view, Gravity.TOP | Gravity.LEFT, saveX, saveY + 10);

        txtsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("提示");
                builder.setMessage("保存图片到本地");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String url = hitTestResult.getExtra();
                        // 下载图片到本地
                        DownPicUtil.downPic(url, new DownPicUtil.DownFinishListener(){

                            @Override
                            public void getDownPath(String s) {
                                Toast.makeText(MainActivity.this,"下载完成",Toast.LENGTH_LONG).show();
                                Message msg = Message.obtain();
                                msg.obj=s;
                                handler.sendMessage(msg);
                            }
                        });

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    // 自动dismiss
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                save.dismiss();
            }
        });
    }
    public int dip2px( float dipValue){
        float scale = MainActivity.this.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }
  /*  private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.back:
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    finish();
                    break;
                case R.id.more:

                    break;
            }
        }
    };*/

}


