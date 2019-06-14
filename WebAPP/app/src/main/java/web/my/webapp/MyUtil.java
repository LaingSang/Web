package web.my.webapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;



/**
 * Created by Administrator on 2018/9/11.
 */

public class MyUtil  {
    public static void setHideShowAnimation(View v, final int time){

        Animation animH=new AlphaAnimation(1.0f,0.0f);
        final Animation animS=new AlphaAnimation(0.0f,1.0f);
        if(v==null&&time<0){
            return;
        }else{
            animH.setDuration(time);
            animH.setFillAfter(true);
            animH.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    animS.setDuration(time);
                    animS.setFillAfter(true);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            v.startAnimation(animH);

        }
    }

   /* public static void createQRImage(String url, Activity a, ImageView imageView)
    {
        DisplayMetrics displayMetrics=new DisplayMetrics();
        a.getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width=displayMetrics.widthPixels*3;
        int height=displayMetrics.heightPixels*3;
        try
        {
            //判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1)
            {
                return;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x <width; x++)
                {
                    if (bitMatrix.get(x, y))
                    {
                        pixels[y * width + x] = 0xff000000;
                    }
                    else
                    {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            //显示到一个ImageView上面
            imageView.setImageBitmap(bitmap);
        }
        catch (WriterException e)
        {
            e.printStackTrace();
        }
    }*/

    public static void popupwindowSave(View view, final Activity a, final String url, int saveX, int saveY) {
        final View v = LayoutInflater.from(a).inflate(R.layout.popupwindow_save, null);
        TextView txtsave = v.findViewById(R.id.saveimg);
        final PopupWindow save;
        save = new PopupWindow(dip2px(120,a), dip2px(40,a));
        save.setContentView(v);
        save.setFocusable(true);
        save.setOutsideTouchable(true);
        save.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        save.showAtLocation(view, Gravity.CENTER | Gravity.LEFT, saveX, saveY + 10);

        txtsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(a);
                builder.setTitle("提示");
                builder.setMessage("保存图片到本地");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 下载图片到本地
                        DownPicUtil.downPic(url, new DownPicUtil.DownFinishListener(){

                            @Override
                            public void getDownPath(String s) {
                                Toast.makeText(a,"下载完成",Toast.LENGTH_LONG).show();
                                Message msg = Message.obtain();
                                msg.obj=s;

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
    public static int dip2px(float dipValue, Activity a){
        float scale = a.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }
}
