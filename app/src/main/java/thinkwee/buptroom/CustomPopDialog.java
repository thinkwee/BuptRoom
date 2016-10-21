package thinkwee.buptroom;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;

import thinkwee.buptroom.R;

/**
 * 该自定义Dialog应用在：弹出框居中显示图片，点击其他区域自动关闭Dialog
 *
 * @author SHANHY(365384722@QQ.COM)
 * @date   2015年12月4日
 */
public class CustomPopDialog extends Dialog {

    public CustomPopDialog(Context context) {
        super(context);
    }

    public CustomPopDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private Bitmap image;

        public Builder(Context context) {
            this.context = context;
        }

        public Bitmap getImage() {
            return image;
        }

        public void setImage(Bitmap image) {
            this.image = image;
        }

        public CustomPopDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CustomPopDialog dialog = new CustomPopDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.qrshare, null);
            dialog.addContentView(layout, new LayoutParams(
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                    , android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
            dialog.setContentView(layout);
            ImageView img = (ImageView)layout.findViewById(R.id.img_qrcode);
            img.setImageBitmap(getImage());
            Button sharebt=(Button)layout.findViewById(R.id.share_bt);
            sharebt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String sharecontent="  Stay Hungry Stay Foolish\n" +
                            "Stay Studying With BuptRoom\n" +
                            "————————————————\n" +
                            "   http://fir.im/buptroom\n" +
                            "————————————————";
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,sharecontent);
                    sendIntent.setType("text/plain");
                    context.startActivity(Intent.createChooser(sendIntent, "分享到"));
                }
            });
            return dialog;
        }
    }
}