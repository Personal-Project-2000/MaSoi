package com.personal_game.masoi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.personal_game.masoi.object.SpinnerObject;
import com.personal_game.masoi.R;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<SpinnerObject> {
    public SpinnerAdapter(@NonNull Context context, ArrayList<SpinnerObject> spinners) {
        super(context, 0, spinners);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_spinner, parent, false);
        }
        SpinnerObject item = getItem(position);
        ImageView icon = convertView.findViewById(R.id.iconDrop);
        TextView txt = convertView.findViewById(R.id.txtDrop);
        if(item != null){
            icon.setImageResource(item.getSpinnerItemImg());
            txt.setText(item.getSpinnerItemName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_spinner, parent, false);
        }
        SpinnerObject item = getItem(position);
        ImageView icon = convertView.findViewById(R.id.iconItem);
        TextView txt = convertView.findViewById(R.id.txtItem);
        if(item != null){
            icon.setImageResource(item.getSpinnerItemImg());
            txt.setText(item.getSpinnerItemName());
        }
        return convertView;
    }
}
