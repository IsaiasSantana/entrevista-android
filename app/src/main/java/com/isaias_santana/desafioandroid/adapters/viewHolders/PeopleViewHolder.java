package com.isaias_santana.desafioandroid.adapters.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.isaias_santana.desafioandroid.R;
import com.isaias_santana.desafioandroid.domain.People;

/**
 * Created by isaias on 29/05/17.
 */

public class PeopleViewHolder extends RecyclerView.ViewHolder
{
    private TextView name;
    private TextView height;
    private TextView gender;
    private TextView mass;

    public PeopleViewHolder(View view)
    {
        super(view);
        setViews(view);
    }

    private void setViews(View view)
    {
        name    = (TextView) view.findViewById(R.id.tv_nome);
        height  = (TextView) view.findViewById(R.id.tv_altura);
        gender  = (TextView) view.findViewById(R.id.tv_genero);
        mass    = (TextView) view.findViewById(R.id.tv_peso);
    }

    public void setData(People people)
    {
        name.setText(people.getName());
        height.setText(people.getHeight());
        gender.setText(people.getGender());
        mass.setText(people.getMass());
    }
}
