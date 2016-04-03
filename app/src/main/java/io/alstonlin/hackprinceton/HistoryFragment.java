package io.alstonlin.hackprinceton;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * The Fragment for the image feed in AppActivity
 */
public class HistoryFragment extends Fragment {

    private static final String ARG_ACTIVITY = "activity";
    private MainActivity activity;
    private HistoryAdapter adapter;

    /**
     * Use this Factory method to create the Fragment instead of the constructor.
     * @param activity The Activity this Fragment will be attached to
     * @return The new Fragment instance
     */
    public static HistoryFragment newInstance(MainActivity activity) {
        final HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ACTIVITY, activity);
        fragment.setArguments(args);
        fragment.activity = activity;
        return fragment;
    }

    private void setupAdapter(View v){
        ListView list = (ListView) v.findViewById(R.id.list);
        HistoryAdapter adapter = new HistoryAdapter(activity);
        list.setAdapter(adapter);
        this.adapter = adapter;
        DAO.getInstance().getFood(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_data, container, false);
        setupAdapter(v);
        return v;
    }
}