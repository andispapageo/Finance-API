package com.example.user.bdswiss_test.feature;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

public class RecylerAdapter extends RecyclerView.Adapter<RecylerAdapter.ViewHolder> {
    public List<MainActivity.ObjectData> mDataset;
    int s;
        public static class ViewHolder extends RecyclerView.ViewHolder {

            public TextView mTextViewA;
            public TextView mTextViewB;
            public TextView mTextViewC;
            public TextView mTextViewD;
            public ViewHolder(LinearLayout v) {
                super(v);
                mTextViewA = v.findViewById(R.id.txt1);
                mTextViewB = v.findViewById(R.id.txt2);
                mTextViewC = v.findViewById(R.id.txt3);
                mTextViewD = v.findViewById(R.id.txt4);
            }
        }
        private Activity context;
        MainActivity.ObjectData item;
        double mGPrice;
        RecylerAdapter mRecylerAdapter;
        public RecylerAdapter(List<MainActivity.ObjectData>myDataset, Activity _context){
        this.context = _context;
        this.mDataset = myDataset;
        mRecylerAdapter = this;
    }
        public void updatePriceColor(MainActivity.ObjectData data, double mPrice, int set)
        {
            if(data == null ) return;
            if(mDataset == null) return;
            if(data != null)
            {
                for (MainActivity.ObjectData items : mDataset) {
                    if (items.symbol == data.symbol) data = items;
                }
                if(returnPos(data) != -1);
                {
                    int pos = returnPos(data);
                    mDataset.get(pos).price = data.price;
                    mDataset.get(pos).color = set;
                    Upate(pos);
                }
            }
        }
        int returnPos(MainActivity.ObjectData data)
        {
            for (int i = 0; i < mDataset.size(); i++)
                if (data.symbol.equals(mDataset.get(i).symbol))
                    return i;

            return  -1;
        }
        void Upate(int i)
        {
            s=i;
            context.runOnUiThread(new Runnable() {
                public void run() {
                    notifyItemChanged(s);
                }

            });
        }
    ViewHolder holder;
    int lastPosition = -1;
    private void setAnimation(View viewToAnimate, int position)
    {
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            animation.setDuration(750);
            lastPosition = position;
        }
    }

        @Override
        public RecylerAdapter.ViewHolder onCreateViewHolder (ViewGroup parent,
        int viewType){
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
        @Override
        public void onBindViewHolder (ViewHolder holder,int position){
            MainActivity.ObjectData set = mDataset.get(position);
            holder.mTextViewA.setText( set.symbol);
            holder.mTextViewB.setText(String.valueOf( set.bid));
            holder.mTextViewC.setText(String.valueOf( set.ask));
            holder.mTextViewD.setText(String.valueOf( set.price));
            holder.mTextViewD.setTextColor(set.color);
            setAnimation(holder.mTextViewD, position);
        }
        @Override
        public int getItemCount () {
        return mDataset.size();
    } }