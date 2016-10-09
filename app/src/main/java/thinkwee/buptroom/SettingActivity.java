package thinkwee.buptroom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by think on 20162016/10/9 00098:48
 * PACKAGE:thinkwee.buptroom
 * PROJECT:BuptRoom
 * TODO:
 */

public class SettingActivity extends AppCompatActivity {

    private ListView settinglist;
    private List<Map<String,Object>> mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        this.setTitle("主题选择");
        SettingActivity.this.setTheme(R.style.AppTheme_NoActionBar);

        //添加toolbar返回
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        toolbar.setTitle("2333");
        toolbar.setSubtitle("23333333");

        toolbar.setNavigationIcon(R.drawable.ic_menu_back);//或者在布局中 app:navigationIcon="?attr/homeAsUpIndicator"
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                finish();
            }
        });*/

        settinglist=(ListView)findViewById(R.id.listview_setting);
        mData=getData();
        MyAdapter adapter= new MyAdapter(this);
        settinglist.setAdapter(adapter);


    }

    private List<Map<String,Object>> getData(){
        List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();
        Map<String,Object> map= new HashMap<String, Object>();
        map.put("colorpreview",R.drawable.blue);
        map.put("colortext","blue");
        list.add(map);

        map= new HashMap<String, Object>();
        map.put("colorpreview",R.drawable.hotpink);
        map.put("colortext","hotpink");
        list.add(map);

        map= new HashMap<String, Object>();
        map.put("colorpreview",R.drawable.pureblack);
        map.put("colortext","pureblack");
        list.add(map);

       return list;

    }

    public final class ViewHolder{
        public ImageView img;
        public TextView text;
    }

    public class MyAdapter extends BaseAdapter{
        private LayoutInflater inflater;

        public MyAdapter(Context context){
            this.inflater=LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }
        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int pos, View view, ViewGroup viewGroup) {
            ViewHolder holder= null;
            if (view==null){
                holder= new ViewHolder();
                view =inflater.inflate(R.layout.setting_item,null);
                holder.img=(ImageView)view.findViewById(R.id.colorpreview);
                holder.text=(TextView)view.findViewById(R.id.colortext);
                view.setTag(holder);
            }else{
                holder=(ViewHolder)view.getTag();
            }

            holder.img.setBackgroundResource((Integer)mData.get(pos).get("colorpreview"));
            holder.text.setText((String)mData.get(pos).get("colortext"));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                        public void onClick(View v){
                    Toast.makeText(SettingActivity.this, "改个毛线", Toast.LENGTH_SHORT).show();

                }
            });

            return view;
        }
    }


}
