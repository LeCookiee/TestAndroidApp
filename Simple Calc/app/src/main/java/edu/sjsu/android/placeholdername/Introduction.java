package edu.sjsu.android.placeholdername;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


public class Introduction extends Fragment {



    public Introduction() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_introduction, container, false);
        ImageButton buttonNavigateToMainScreen = view.findViewById(R.id.imageButton2);
        buttonNavigateToMainScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMainScreen();
            }
        });

        TextView linkTextView1 = view.findViewById(R.id.linearSystLink);
        TextView linkTextView2 = view.findViewById(R.id.echelonLink);

        TextView linkTextView3 = view.findViewById(R.id.inverseLink);
        TextView linkTextView4 = view.findViewById(R.id.determinantLink);
        TextView linkTextView5 = view.findViewById(R.id.nullSpaceLink);

        linkTextView1.setOnClickListener(v -> openUrl("https://www.mathsisfun.com/algebra/systems-linear-equations.html"));

        linkTextView2.setOnClickListener(v -> openUrl("https://www.statlect.com/matrix-algebra/row-echelon-form"));

        linkTextView3.setOnClickListener(v -> openUrl("https://www.mathsisfun.com/algebra/matrix-inverse.html"));
        linkTextView4.setOnClickListener(v -> openUrl("https://www.khanacademy.org/math/multivariable-calculus/thinking-about-multivariable-function/x786f2022:vectors-and-matrices/a/determinants-mvc"));
        linkTextView5.setOnClickListener(v -> openUrl("https://math.libretexts.org/Bookshelves/Linear_Algebra/Matrix_Analysis_(Cox)/03%3A_The_Fundamental_Subspaces/3.02%3A_Null_Space"));

        // Add more click listeners for additional links if needed

        return view;
    }
    private void navigateToMainScreen() {
        // Perform fragment transaction to navigate to MainScreenFragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, new mainScreen());
        transaction.addToBackStack(null); // Optional: Add to back stack
        transaction.commit();
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}