package com.coolweather.colorweather;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.coolweather.colorweather.Adapter.PositionAdapter;
import java.util.ArrayList;
import java.util.List;
import com.coolweather.colorweather.db.positionCity;
import org.litepal.LitePal;

public class positionAreaFragment extends Fragment {

    private List<positionCity> positionList;
    private Button backButton;
    public static PositionAdapter adapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("123", "positionActivity onCreateView");
        View view = inflater.inflate(R.layout.position_acticity_area, container, false);
        backButton = (Button) view.findViewById(R.id.back);
        recyclerView = (RecyclerView) view.findViewById(R.id.alwaysRecyclerView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("123", "onActivityCreate");

        positionList = new ArrayList<>();
        positionList = LitePal.findAll(positionCity.class);
        Log.d("123", "after" + positionList.toString());
        if (positionList.size() > 0) {
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(manager);
            adapter = new PositionAdapter(positionList, getContext(), getActivity());
            recyclerView.setAdapter(adapter);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

}
