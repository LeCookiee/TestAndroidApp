package edu.sjsu.android.placeholdername;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.sjsu.android.placeholdername.databinding.FragmentMainScreenBinding;

public class mainScreen extends Fragment {

    private FragmentMainScreenBinding binding;
    public mainScreen() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainScreenBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.toBasic.setOnClickListener(new CalcBtnOnClickListener());
        binding.toCalcListBtn.setOnClickListener(new CalcBtnOnClickListener());
        binding.intro.setOnClickListener(new CalcBtnOnClickListener());
        binding.deleteApp.setOnClickListener(v -> uninstallApp());
        return view;
    }

    private class CalcBtnOnClickListener implements View.OnClickListener {
        /** @noinspection DataFlowIssue*/
        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            int fragmentContainerId = ((ViewGroup)getView().getParent()).getId();

            if (v == binding.toCalcListBtn)
                transaction.replace(fragmentContainerId, CalcNavBtnFragment.class, null)
                        .addToBackStack(null);
            else if (v == binding.toBasic)
                transaction.replace(fragmentContainerId, Calc.class, null)
                        .addToBackStack(null);
            else if (v == binding.intro)
                transaction.replace(fragmentContainerId, Introduction.class, null)
                        .addToBackStack(null);


            transaction.commit();
        }
    }

    /** @noinspection DataFlowIssue*/
    private void uninstallApp() {
        Uri packageURI = Uri.parse("package:" + getActivity().getPackageName());
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        startActivity(uninstallIntent);
    }
}