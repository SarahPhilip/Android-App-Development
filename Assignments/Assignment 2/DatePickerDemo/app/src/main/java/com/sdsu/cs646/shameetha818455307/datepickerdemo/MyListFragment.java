package com.sdsu.cs646.shameetha818455307.datepickerdemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListFragment;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyListFragment extends ListFragment implements AdapterView.OnItemClickListener {

    int mCurCheckPosition;
    OnDataPass dataPasser;
    private List desertList;
    private DesertListAdapter adapter;
    private View currentSelectedView;

    public static final String PREFERENCE_NAMES = "ListFragmentData";

    public MyListFragment() {
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        getListView().setItemChecked(mCurCheckPosition, true);
        getListView().setOnItemClickListener(this);
        mCurCheckPosition = dataPasser.getIndex();
        getListView().setItemChecked(0, true);

        registerForContextMenu(getListView());

    }
    @Override
    public void onStart() {
        super.onStart();
        Resources res = getResources();
        String[] myString = res.getStringArray(R.array.Deserts);
        desertList = new ArrayList();
        for (String s : myString)
            desertList.add(new DesertListItem(s));
        adapter = new DesertListAdapter(getActivity(), desertList);
        setListAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        passData(position);
        mCurCheckPosition = position;
        SharedPreferences settings = getActivity().getSharedPreferences(PREFERENCE_NAMES, 0);
        int index = settings.getInt("selected_index",-1);
        if(index != -1)
            parent.getChildAt(index).setBackgroundColor(Color.TRANSPARENT);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("selected_index", position);
        editor.commit();

        if (currentSelectedView != null && currentSelectedView != view) {
            unhighlightCurrentRow(currentSelectedView);
        }

        currentSelectedView = view;
        highlightCurrentRow(currentSelectedView);
    }
    private void unhighlightCurrentRow(View rowView) {
        rowView.setBackgroundColor(Color.TRANSPARENT);
    }

    private void highlightCurrentRow(View rowView) {
        rowView.setBackgroundColor(Color.GRAY);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dataPasser = (OnDataPass) activity;

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.desert_list_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.edit:
                Toast.makeText(getActivity(), "You selected Edit", Toast.LENGTH_SHORT)
                        .show();
                return true;
            case R.id.delete:

                Toast.makeText(getActivity(), "You selected Delete", Toast.LENGTH_SHORT)
                        .show();
                AdapterView.AdapterContextMenuInfo information = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int position = information.position;
                removeItemFromList(position);

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    protected void removeItemFromList(int deletePosition) {

        desertList.remove(deletePosition);
        adapter.notifyDataSetChanged();
    }

    public void passData(int data) {
        dataPasser.onDataPass(data);
    }

    public interface OnDataPass {
        public void onDataPass(int data);
        public int getIndex();
    }

}
