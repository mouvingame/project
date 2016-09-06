package ru.startandroid.apartmentrentals;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ApartmentFragment extends Fragment{

    private RecyclerView mApartmentRecyclerView;
    private List<ApartmentItem> mItems = new ArrayList<>();
    private LinearLayoutManager linManager;

    boolean loading = true;
    boolean newResults = true;
    int i = 0;
    int b = 0;

    public static Fragment newInstance(){
        //Log.i("ZHEKA", "Fragment - newInstance");
        return new ApartmentFragment();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.update, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case(R.id.update_id):
                newResults = true;
                i = 0;
                if(!isNetworkAvailableAndConnected()){
                    Toast.makeText(getActivity(), "Нет соединения с интернетом", Toast.LENGTH_LONG).show();
                }
                new FetchItemsTask().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);

        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable &&
                cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.i("ZHEKA", "Fragment - onCreate");
        setRetainInstance(true);
        setHasOptionsMenu(true);
        if(!isNetworkAvailableAndConnected()){
            Toast.makeText(getActivity(), "Нет соединения с интернетом", Toast.LENGTH_LONG).show();
        }
        new FetchItemsTask().execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Log.i("ZHEKA", "Fragment - onCreateView");
        View view = inflater.inflate(R.layout.fragment_apartment, container, false);
        mApartmentRecyclerView = (RecyclerView)view.findViewById(R.id.apartment_recycler_view);

        linManager = new LinearLayoutManager(getActivity());
        mApartmentRecyclerView.setLayoutManager(linManager);
        mApartmentRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                 if(dy > 0){
                    int visibleItemCount = linManager.getChildCount();
                    int totalItemCount = linManager.getItemCount();
                    int pastVisiblesItems = linManager.findFirstVisibleItemPosition();
                    Log.d("ZHEKA", " RecyclerView.OnScrollListener - onScrolled - dy>0" + " " + visibleItemCount
                            + " " + totalItemCount + " " + pastVisiblesItems);

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            Log.d("ZHEKA", " RecyclerView.OnScrollListener - LastItem");
                            newResults = false;
                            ++i;
                            Log.d("ZHEKA", " RecyclerView.OnScrollListener - i == " + i);
                            new FetchItemsTask().execute(i);

                        }
                    }
                }
            }
        });
        setupAdapter();
        return view;
    }

    private void setupAdapter() {
        Log.d("ZHEKA", "Fragment - setupAdapter");
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(getResources()
                .getQuantityString(R.plurals.toast_plurals, mItems.size(), String.valueOf(mItems.size())));
        mApartmentRecyclerView.setAdapter(new ApartmentAdapter(mItems));
        loading = true;
    }

    private class ApartmentHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tv_rooms;
        TextView tv_address;
        TextView tv_BYN;
        TextView tv_USD;
        TextView tv_BYR;
        ImageView imgview;

        ApartmentItem mItem;

        public ApartmentHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            tv_rooms = (TextView) itemView.findViewById(R.id.recycler_tv_rooms);
            tv_address = (TextView) itemView.findViewById(R.id.recycler_tv_address);
            tv_BYN = (TextView) itemView.findViewById(R.id.recycler_tv_BYN);
            tv_USD = (TextView) itemView.findViewById(R.id.recycler_tv_USD);
            tv_BYR = (TextView) itemView.findViewById(R.id.recycler_tv_BYR);
            imgview = (ImageView) itemView.findViewById(R.id.imgview);
        }

        public void bindApartment(ApartmentItem item){

            mItem = item;

            char number = item.getRentType().charAt(0);
            String kol = String.valueOf(number);
            tv_rooms.setText(kol + "ком");
            tv_address.setText(item.getAddress());
            tv_BYN.setText(item.getAmountBYN() + " р.");
            tv_USD.setText(String.format("%.0f", Float.parseFloat(item.getAmountUSD())) + " $");
            tv_BYR.setText(String.format("%,d", Integer.parseInt(item.getAmountBYR())) + " р.");

            Picasso.with(getActivity())
                    .load(item.getPhotoUrl())
                    .into(imgview);
        }

        @Override
        public void onClick(View v) {
            Intent i = WebActivity.newIntent(getActivity(), mItem.getWebUrl());
            startActivity(i);
        }
    }
    private class ApartmentAdapter extends RecyclerView.Adapter<ApartmentHolder>{

        private List<ApartmentItem> mItems;

        public ApartmentAdapter(List<ApartmentItem> items){
            //Log.d("ZHEKA", "ApartmentAdapter - constructor");
            mItems = items;
        }

        @Override
        public ApartmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.recycler_apartment, parent, false);
            return new ApartmentHolder(view);
        }

        @Override
        public void onBindViewHolder(ApartmentHolder holder, int position) {
            ApartmentItem item = mItems.get(position);
            holder.bindApartment(item);
        }

        @Override
        public int getItemCount() {
            //Log.d("ZHEKA", "ApartmentAdapter - getItemCount");
            //Log.d("ZHEKA", "ApartmentAdapter - " + mItems.size());
            return mItems.size();
        }
    }

    private class FetchItemsTask extends AsyncTask<Integer, Void, List<ApartmentItem>>{
        @Override
        protected List<ApartmentItem> doInBackground(Integer... params) {
            Log.i("ZHEKA", "doInBackground");
            if(newResults){
                return  new FlickrFetchr().fetchItems();
            }else{
                return  new FlickrFetchr().fetchOldItems(params[0]);
            }

        }

        @Override
        protected void onPostExecute(List<ApartmentItem> items) {
            super.onPostExecute(items);
            Log.i("ZHEKA", "onPostExecute");
            mItems.clear();
            mItems = items;
            setupAdapter();
        }
    }
}
