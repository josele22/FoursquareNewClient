package com.joseantonio.foursquarenewclient.app.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.joseantonio.foursquarenewclient.app.rest.Profiles.Profiles;
import com.joseantonio.foursquarenewclient.app.rest.Profiles.Result;

import java.util.List;

/**
 * Created by josetorres on 13/05/14.
 */
public class AdapterProfileFragment extends ArrayAdapter<Result> {

    public AdapterProfileFragment(Context context, int resource, List<Result> objects) {
        super(context, resource, objects);
    }
}
