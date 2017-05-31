package com.isaias_santana.desafioandroid.adapters;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.isaias_santana.desafioandroid.R;
import com.isaias_santana.desafioandroid.adapters.viewHolders.PeopleViewHolder;
import com.isaias_santana.desafioandroid.adapters.viewHolders.ProgressBarViewHolder;
import com.isaias_santana.desafioandroid.domain.People;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  Isaías Santana on 29/05/17.
 * email: isds.santana@gmail.com
 */

public class PeopleAdapter extends RecyclerView.Adapter
{

    private List<People> mData;
    private final int VIEW_ITEM = 0;
    private final int VIEW_PROGRESS_BAR = 1;
    private Handler handler;

    public PeopleAdapter()
    {
        this.handler = new Handler();
        this.mData = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        switch (viewType)
        {
            case VIEW_ITEM:
                return new PeopleViewHolder(getView(R.layout.content_recycler_list,parent));

            case VIEW_PROGRESS_BAR:
                return new ProgressBarViewHolder(getView(R.layout.progressbar_item,parent));
        }
        return new ProgressBarViewHolder(getView(R.layout.progressbar_item,parent));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if(holder instanceof PeopleViewHolder)
        {
            PeopleViewHolder people = (PeopleViewHolder) holder;

            people.setData(mData.get(position));
        }
        else if(holder instanceof ProgressBarViewHolder)
        {
            ((ProgressBarViewHolder) holder).getProgressBar().setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount()
    {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public int getItemViewType(int position)
    {
        return mData.get(position) != null ? VIEW_ITEM : VIEW_PROGRESS_BAR;
    }

    public void add(final People p)
    {
        handler.post(new Runnable() {
            @Override
            public void run()
            {
                PeopleAdapter.this.mData.add(p);
                notifyItemInserted(mData.size()-1);
            }
        });

    }

    public People getPeople(int position)
    {
        return mData.get(position);
    }

    public void setFilter(final List<People> peoples)
    {
        handler.post(new Runnable() {
            @Override
            public void run()
            {
               PeopleAdapter.this.mData = new ArrayList<>();
                PeopleAdapter.this.mData.addAll(peoples);
                notifyDataSetChanged();
            }
        });
    }

    public void removeItem(final int position)
    {
        handler.post(new Runnable() {
            @Override
            public void run()
            {
                if(position >= 0 && position <= mData.size()-1)
                {
                    PeopleAdapter.this.mData.remove(position);
                    notifyItemRemoved(position);
                }
            }
        });

    }

    private View getView(int id, ViewGroup parent)
    {
       return LayoutInflater
                .from(parent.getContext())
                .inflate(id,parent,false);
    }
}