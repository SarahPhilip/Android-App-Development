package com.sdsu.cs646.shameetha818455307.datepickerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class ListActivity extends ActionBarActivity implements MyListFragment.OnDataPass {

    private String mTextFieldValue;
    int mIndexValue;
    int mCurrentPosition;


    public static final String EXTRA_TEXTFIELD_VALUE_RECEIVED = "com.sdsu.cs646.shameetha818455307.datepickerdemo.textfield_value_received";
    public static final String EXTRA_INDEX = "com.sdsu.cs646.shameetha818455307.datepickerdemo.index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mCurrentPosition = getIntent().getIntExtra(EXTRA_INDEX, -1);
            mTextFieldValue = getIntent().getStringExtra(EXTRA_TEXTFIELD_VALUE_RECEIVED);
        }
        Button backButton = (Button)this.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
//                myIntent.putExtra(MainActivity.EXTRA_INDEX, mCurrentPosition);
//                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(myIntent);
                finish();
            }
        });

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_aaa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        mTextFieldValue = getIntent().getStringExtra(EXTRA_TEXTFIELD_VALUE_RECEIVED);
        mCurrentPosition = getIntent().getIntExtra(EXTRA_INDEX, -1);
    }

    @Override
    public void onDataPass(int data) {
        mCurrentPosition = data;
    }

    @Override
    public int getIndex() {
        return getIntent().getIntExtra(EXTRA_INDEX, -1);
    }

}
