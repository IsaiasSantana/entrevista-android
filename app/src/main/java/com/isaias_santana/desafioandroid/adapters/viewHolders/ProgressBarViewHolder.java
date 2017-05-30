package com.isaias_santana.desafioandroid.adapters.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.isaias_santana.desafioandroid.R;

/**
 * @author Isaías Santana on 29/05/17.
 * email: isds.santana@gmail.com
 */

public class ProgressBarViewHolder extends RecyclerView.ViewHolder
{
    private ProgressBar progressBar;

    public ProgressBarViewHolder(View view)
    {
        super(view);
        progressBar = (ProgressBar) view.findViewById(R.id.my_progress_bar);
    }

    public ProgressBar getProgressBar()
    {
        return this.progressBar;
    }
}
