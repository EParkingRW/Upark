package com.example.upark;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.upark.adapters.HistoryAdapter;
import com.example.upark.adapters.VerticalSpaceItemDecoration;
import com.example.upark.helpers.B;
import com.example.upark.models.Garage;
import com.example.upark.models.History;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {
    private final HistoryAdapter historyAdapter;
    private final String TAG = HistoryFragment.class.getSimpleName();
    private final ArrayList<History> histories;
    public HistoryFragment() {
        histories = new ArrayList<>();
        historyAdapter = new HistoryAdapter(histories);
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        try {
            Toolbar toolbar = view.findViewById(R.id.appBar);
            TextView toolBarTitle = toolbar.findViewById(R.id.title);
            toolbar.setNavigationOnClickListener(v -> requireActivity().finish());

            toolBarTitle.setText(R.string.notification);
            RecyclerView historyRecycler = view.findViewById(R.id.HistoryListRecycler);

            historyRecycler.setLayoutManager(new LinearLayoutManager(this.requireContext()));
            historyRecycler.setVerticalScrollBarEnabled(true);
            historyRecycler.setAdapter(historyAdapter);
            historyRecycler.addItemDecoration(new VerticalSpaceItemDecoration(48));


            addListenerOnGarages();

        }catch (Exception e){
            Log.e(TAG, "Error: "+ e.getMessage());
        }


        return view;
    }

    private void addListenerOnGarages() {
        Consumer <List< Garage >> onInitialGarages = (garages) -> {
            try {
                garages.forEach(each -> each.addAvailableSpaceConsumer(this::createAvailableChangeHistory));
            }catch (Exception e){
                Log.e(TAG, "Error : " + e.getMessage());
            }

        };
        B.getInstance().onInitialGarage(onInitialGarages);
    }
    private void createAvailableChangeHistory(Garage garage, int oldValue, int newValue){
        String message = "Garage "+garage.getName()+" available slots become "+ newValue;
        History history = new History(History.HistoryType.AVAILABLE_SPACE_CHANGE, message);
        if(histories.contains(history)){
           return ;
        }
        addHistory(history);
    }
    private void addHistory(History history){
        histories.add(0,history);
        historyAdapter.notifyItemInserted(0);
    }
}