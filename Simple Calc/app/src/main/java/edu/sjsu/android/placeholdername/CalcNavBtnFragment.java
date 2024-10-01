package edu.sjsu.android.placeholdername;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.HashMap;

/**
 * A fragment representing a list of Items.
 */
public class CalcNavBtnFragment extends Fragment {

    public static final HashMap<String, Class<? extends Fragment>> calcFragmentTitles = new HashMap<String, Class<? extends Fragment>>() {{
        put("System of Linear Equations Solver", LinearSystemSolver.class);
        put("Echelon Form Calculator", EchelonFormCalculator.class);
        put("Inverse Matrix Calculator", InverseMatrixCalculator.class);
        put("Determinant Calculator", DeterminantCalculator.class);
        put("Null Space Calculator", NullSpaceCalculator.class);
    }};


    public CalcNavBtnFragment() {
        //required empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calc_nav_list, container, false);
        ImageButton buttonNavigateToMainScreen = view.findViewById(R.id.imageButton2);
        buttonNavigateToMainScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMainScreen();
            }
        });

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.calcNavList);
        recyclerView.setAdapter(new CalcNavAdapter(calcFragmentTitles, context));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        return view;
    }
    private void navigateToMainScreen() {
        // Perform fragment transaction to navigate to MainScreenFragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, new mainScreen());
        transaction.addToBackStack(null); // Optional: Add to back stack
        transaction.commit();
    }
}