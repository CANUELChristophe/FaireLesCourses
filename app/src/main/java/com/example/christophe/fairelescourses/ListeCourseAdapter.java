package com.example.christophe.fairelescourses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by christophe on 04/11/2017.
 */

public class ListeCourseAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ListeCourse> listeCourse;

    public ListeCourseAdapter(Context context, ArrayList<ListeCourse> listeCourse) {
        this.context = context;
        this.listeCourse = listeCourse;
    }

    @Override
    public int getCount() {
        return listeCourse.size();
    }

    @Override
    public Object getItem(int position) {
        return listeCourse.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout linearLayout = null;
        if (convertView != null) {
            linearLayout = (LinearLayout) convertView;
        }
        else
        {
            linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_course, parent,false);
        }

        final LinearLayout linearLayoutSaisie = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.faire_les_courses, parent,false);

// get current item to be displayed
        final ListeCourse currentListeCourse = (ListeCourse) getItem(position);
// On récup les deux textes views de notre template (layout_course.xml)
        TextView textViewProduitCourse = (TextView) linearLayout.findViewById(R.id.textViewProduitCourse);
        TextView textViewQuantiteCourse = (TextView) linearLayout.findViewById(R.id.textViewQuantiteCourse);
        TextView textViewUniteCourse = (TextView) linearLayout.findViewById(R.id.textViewUniteCourse);
        CheckBox checkBoxCourse = (CheckBox) linearLayout.findViewById(R.id.checkBoxCourse);

        final boolean[] checkBoxClickBoolean = {false};
// si click sur checkBox
        checkBoxCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentListeCourse.switchEtat();
                checkBoxClickBoolean[0] = true;
            }
        });

//si click sur la ligne mais pas sur le checkBox

        if (!checkBoxClickBoolean[0]){
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText editTextProduitSaisie = (EditText) linearLayoutSaisie.findViewById(R.id.editTextProduitSaisie);
                    editTextProduitSaisie.setText(currentListeCourse.getProduit());
                }
            });
        }

        textViewProduitCourse.setText(currentListeCourse.getProduit());
        textViewQuantiteCourse.setText(currentListeCourse.getQuantiteString());
        textViewUniteCourse.setText((currentListeCourse.getUnite()));
        checkBoxCourse.setChecked(currentListeCourse.isEtat());
// retourne la ligne
        return linearLayout;
    }
}