package com.example.balakrishnan.mybrowser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    ImageView backgroundIV,sendIV;
    Typeface regular,bold;
    FontChanger regularFontChanger,boldFontChanger;
    public static EditText urlET;
    TextView welcomeTV;
    TextView clockTV;
    MaterialRippleLayout settingsMRL,downloadMRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        SearchSuggestion s= new SearchSuggestion();
        init();
        loadBackgroundImage();
        boldFontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));
        sendIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(urlET.getText().toString().length()!=0)
                    startWebActivity();
                else
                    Toast.makeText(getApplicationContext(),"Please Enter URL",Toast.LENGTH_SHORT).show();
            }
        });

        urlET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if(urlET.getText().toString().length()!=0)
                startWebActivity();
                else
                Toast.makeText(getApplicationContext(),"Please Enter URL",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        SearchSuggestionInitiate();
        urlET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(urlET.length()==0)
                {
                    welcomeTV.setVisibility(View.VISIBLE);
                    clockTV.setVisibility(View.VISIBLE);
                    sList1.clear();
                    sAdapter1.notifyDataSetChanged();
                }

                return true;
            }
        });
        urlET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                System.out.println(urlET.getText().toString());
                String q=urlET.getText().toString();
                if(!q.startsWith("http://")&&!q.startsWith("https://"))
                    s1.updateSuggestion(q);
                welcomeTV.setVisibility(View.INVISIBLE);
                clockTV.setVisibility(View.INVISIBLE);


            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(urlET.length()==0)
                {
                    welcomeTV.setVisibility(View.VISIBLE);
                    clockTV.setVisibility(View.VISIBLE);
                    sList1.clear();
                    sAdapter1.notifyDataSetChanged();
                }

            }
        });

        settingsMRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,AboutActivity.class));
            }
        });

        downloadMRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
    SearchSuggestion s1=new SearchSuggestion();
    RecyclerView recyclerView;

    //LinearLayoutManager layoutManager;
    FlexboxLayoutManager layoutManager;
    public static SuggestionAdapter sAdapter1;
    public static List<Suggestion> sList1 = new ArrayList<>();
    public void SearchSuggestionInitiate()
    {


        layoutManager = new FlexboxLayoutManager(getApplicationContext());
        layoutManager.setFlexDirection(FlexDirection.ROW_REVERSE);
        layoutManager.setJustifyContent(JustifyContent.FLEX_END);

        recyclerView = findViewById(R.id.recycler_view_home);
        sAdapter1 = new SuggestionAdapter(sList1,getApplicationContext(),this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(sAdapter1);
        s1=new SearchSuggestion();
    }
    public void startWebActivity(){
        Intent intent = new Intent(HomeActivity.this,WebActivity.class);
        intent.putExtra("url",urlET.getText().toString().trim());
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(HomeActivity.this, urlET,urlET.getTransitionName());
        startActivity(intent,optionsCompat.toBundle());

    }
    public void init(){
        backgroundIV = findViewById(R.id.backgroundIV);
        backgroundIV.setDrawingCacheEnabled(true);
        backgroundIV.animate().alpha(0).start();
        sendIV = findViewById(R.id.sendIV);
        regular = Typeface.createFromAsset(getAssets(), "fonts/product_san_regular.ttf");
        bold = Typeface.createFromAsset(getAssets(),"fonts/product_sans_bold.ttf");
        regularFontChanger = new FontChanger(regular);
        boldFontChanger = new FontChanger(bold);
        //Changing the font throughout the activity
        welcomeTV =findViewById(R.id.welcomeTV);

        urlET = findViewById(R.id.urlET);
        clockTV = findViewById(R.id.textClock);
        settingsMRL = findViewById(R.id.settingsMRL);
        downloadMRL = findViewById(R.id.downloadMRL);

    }

    public void loadBackgroundImage(){


        final Handler handler = new Handler();
        HomeActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Picasso.with(getApplicationContext())
                        .load("https://source.unsplash.com/collection/319663")
                        .skipMemoryCache()
                        .into(backgroundIV, new Callback() {
                            @Override
                            public void onSuccess() {
                                if(backgroundIV.getDrawingCache()!=null){
                                    //Changing the color of send icon
                                    sendIV.setColorFilter(getDominantColor(backgroundIV.getDrawingCache()));
                                }

                                Animation zoomin= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoomin);
                                zoomin.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {
                                        backgroundIV.animate().alpha(1).setDuration(2000).start();
                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        backgroundIV.animate().alpha(0).setDuration(2000).start();
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                                backgroundIV.setAnimation(zoomin);
                                backgroundIV.startAnimation(zoomin);

                            }


                            @Override
                            public void onError() {
                                Toast.makeText(getApplicationContext(),"No internet!",Toast.LENGTH_LONG).show();
                            }
                        });

                handler.postDelayed(this,20000);
            }
        });

    }

    //Getting dominant color from wallpaper
    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }

    //To show keyboard
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    //To  hide keyboard
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

    }
}
