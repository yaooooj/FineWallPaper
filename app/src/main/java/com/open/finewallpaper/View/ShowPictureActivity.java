package com.open.finewallpaper.View;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.open.finewallpaper.Adapter.ShowAdapter;
import com.open.finewallpaper.Bean.SetBean;
import com.open.finewallpaper.R;
import com.open.finewallpaper.Util.GlideApp;

import com.open.finewallpaper.Util.RvScrollListener;

import java.util.ArrayList;
import java.util.List;


public class ShowPictureActivity extends AppCompatActivity {
    private final static String TAG = "ShowPictureActivity";
    private List<String> data;
    private RecyclerView recyclerView;
    private ShowAdapter mShowAdapter;
    private Toolbar toolbar;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_picture);

        final Bundle bundle = getIntent().getBundleExtra("bundle");
        type = bundle.getString("type");
        init();
        initToolbar();
        recyclerView = (RecyclerView) findViewById(R.id.show_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        mShowAdapter = new ShowAdapter(ShowPictureActivity.this,type);
        recyclerView.setAdapter(mShowAdapter);
        mShowAdapter.setOnItemListener(new ShowAdapter.OnItemListener() {
            @Override
            public void itemClick(ArrayList<SetBean> data, int position) {

                Bundle bundle1 = new Bundle();
                bundle1.putParcelableArrayList("url",data);
                bundle1.putInt("position",position);
                Intent intent = new Intent(ShowPictureActivity.this,NextActivity.class);
                intent.putExtra("urls",bundle1);
                startActivity(intent);

            }
        });
        recyclerView.addOnScrollListener(new RvScrollListener() {
            @Override
            public void onLoadMore(View view) {
                GlideApp.with(ShowPictureActivity.this).resumeRequests();
            }

            @Override
            public void onDragLoadMore() {
                GlideApp.with(ShowPictureActivity.this).pauseRequests();
            }
        });
    }

    public void init(){
        data = new ArrayList<>();

    }

    public void initToolbar(){

        toolbar = (Toolbar) findViewById(R.id.show_tb);
        toolbar.getBackground().setAlpha(255);
        TextView textView = (TextView) findViewById(R.id.show_tb_tv);
        textView.setText(type);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /*
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.show_apb);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                toolbar.setBackgroundColor(changeAlpha(Color.GRAY,
                        Math.abs(verticalOffset*1.0f)/appBarLayout.getTotalScrollRange()));
            }
        });

        */
    }

    public int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction *0.7);
        return Color.argb(alpha, red, green, blue);
    }



}
