package com.inocen.gojepang.activity.lesson;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.inocen.gojepang.R;
import com.inocen.gojepang.databinding.LessonHomeModel;

import java.util.List;

public class LessonIntroAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<LessonHomeModel> lessonHomeModeList;
    private String[] values;
    private Typeface tf;

    public LessonIntroAdapter(Context context, String[] values) {
        super(context, R.layout.list_intro, values);
        this.context = context;
        this.values = values;
//        this.tf = Typeface.createFromAsset(context.getAssets(), "font/droidsansjapanese.ttf"); //initialize typeface here.
    }


    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_intro, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.list_text_view);
//        textView.setTypeface(tf); // set typeface here
        textView.setText(values[position]);
        return rowView;
    }
}
