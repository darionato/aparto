package com.badlydone.aparto.core;

import java.util.ArrayList;

import com.badlydone.aparto.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class aPartoProfileAdapter extends ArrayAdapter<aPartoProfile> {
			
    private ArrayList<aPartoProfile> items;

    public aPartoProfileAdapter(Context context, int textViewResourceId, ArrayList<aPartoProfile> items) {
        super(context, textViewResourceId, items);
        this.items = items;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
    	
            View v = convertView;
            
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)
                	getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.row_profile, null);
            }
            
            aPartoProfile o = items.get(position);
            if (o != null) 
            {
            	
            	// set the descriptiorn
                TextView tt = (TextView) v.findViewById(R.id.txtProfileName);
                if (tt != null)
                	tt.setText(o.getProfileName());
                
            	// set the phone number
                tt = (TextView) v.findViewById(R.id.txtPhoneNumber);
                if (tt != null)
                	tt.setText(o.getPhoneNumber());
                
            }
            return v;
    }
}

