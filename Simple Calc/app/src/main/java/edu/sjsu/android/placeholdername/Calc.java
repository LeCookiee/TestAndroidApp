package edu.sjsu.android.placeholdername;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.mariuszgromada.math.mxparser.*;

public class Calc extends Fragment implements View.OnClickListener {
    private EditText display;

    public Calc() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calc, container, false);
        display = (EditText) view.findViewById(R.id.inputeq);
        display.setShowSoftInputOnFocus(false); // default keyboard won't pop up
        display.setOnClickListener(v -> {
            if (getString(R.string.input_values).equals(display.getText().toString())) {
                display.setText(""); // clears text in EditText
            }
        });
        ImageButton buttonNavigateToMainScreen = view.findViewById(R.id.imageButton2);
        buttonNavigateToMainScreen.setOnClickListener(new View.OnClickListener() {//to mainscreen
            @Override
            public void onClick(View v) {
                navigateToMainScreen();
            }
        });

        // Set click listeners for buttons
        view.findViewById(R.id.clearbtn).setOnClickListener(this);
        view.findViewById(R.id.exponentbtn).setOnClickListener(this);
        view.findViewById(R.id.parenthesisbtn).setOnClickListener(this);
        view.findViewById(R.id.multiplybtn).setOnClickListener(this);
        view.findViewById(R.id.dividebtn).setOnClickListener(this);
        view.findViewById(R.id.addbtn).setOnClickListener(this);
        view.findViewById(R.id.subtractbtn).setOnClickListener(this);
        view.findViewById(R.id.ninebtn).setOnClickListener(this);
        view.findViewById(R.id.eightbtn).setOnClickListener(this);
        view.findViewById(R.id.sevenbtn).setOnClickListener(this);
        view.findViewById(R.id.sixbtn).setOnClickListener(this);
        view.findViewById(R.id.fivebtn).setOnClickListener(this);
        view.findViewById(R.id.fourbtn).setOnClickListener(this);
        view.findViewById(R.id.threebtn).setOnClickListener(this);
        view.findViewById(R.id.twobtn).setOnClickListener(this);
        view.findViewById(R.id.onebtn).setOnClickListener(this);
        view.findViewById(R.id.zerobtn).setOnClickListener(this);
        view.findViewById(R.id.pointbtn).setOnClickListener(this);
        view.findViewById(R.id.equalsbtn).setOnClickListener(this);
        view.findViewById(R.id.backbtn).setOnClickListener(this);


        return view;
    }
    private void navigateToMainScreen() {
        // Perform fragment transaction to navigate to MainScreenFragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, new mainScreen());
        transaction.addToBackStack(null); // Optional: Add to back stack
        transaction.commit();
    }
    private void updateText(String toAdd) {
        String old = display.getText().toString();
        int cursorPos = display.getSelectionStart();
        String left = old.substring(0, cursorPos);
        String right = old.substring(cursorPos);
        if (getString(R.string.input_values).equals(display.getText().toString())) {
            display.setText(toAdd);
            display.setSelection(cursorPos + 1);
        } else {
            display.setText(String.format("%s%s%s", left, toAdd, right));
            display.setSelection(cursorPos + 1);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        // Handle button clicks
        switch (v.getId()) {
            case R.id.clearbtn:
                display.setText("");
                break;
            case R.id.exponentbtn:
                updateText("^");
                break;
            case R.id.parenthesisbtn:
                int pos = display.getSelectionStart();
                int openPar = 0;
                int closePar = 0;
                int textLen = display.getText().length();

                for (int i = 0; i < pos; i++)
                {
                    if (display.getText().toString().charAt(i) == '(')
                    {
                        openPar += 1;
                    }
                    if (display.getText().toString().charAt(i) == ')')
                    {
                        closePar += 1;
                    }
                }
                if (openPar == closePar || display.getText().toString().charAt(textLen - 1) == '(')
                {
                    updateText("(");

                }
                else if (closePar < openPar && display.getText().toString().charAt(textLen - 1) != '(')
                {
                    updateText(")");
                }
                display.setSelection(pos + 1);
                break;

            case R.id.multiplybtn:
                updateText("*");
                break;
            case R.id.dividebtn:
                updateText("/");
                break;
            case R.id.addbtn:
                updateText("+");
                break;
            case R.id.subtractbtn:
                updateText("-");
                break;
            case R.id.ninebtn:
                updateText("9");
                break;
            case R.id.eightbtn:
                updateText("8");
                break;
            case R.id.sevenbtn:
                updateText("7");
                break;
            case R.id.sixbtn:
                updateText("6");
                break;
            case R.id.fivebtn:
                updateText("5");
                break;
            case R.id.fourbtn:
                updateText("4");
                break;
            case R.id.threebtn:
                updateText("3");
                break;
            case R.id.twobtn:
                updateText("2");
                break;
            case R.id.onebtn:
                updateText("1");
                break;

            case R.id.zerobtn:
                updateText("0");
                break;
            case R.id.pointbtn:
                updateText(".");
                break;
            case R.id.equalsbtn:
                String useExp = display.getText().toString();

                Expression exp = new Expression(useExp);

                String result = String.valueOf(exp.calculate());

                display.setText(result);

                display.setSelection(result.length());
                break;
            case R.id.backbtn:
                int position = display.getSelectionStart();
                int tLength = display.getText().length();

                if (position != 0 && tLength != 0)
                {
                    SpannableStringBuilder select = (SpannableStringBuilder) display.getText();
                    select.replace(position-1, position, "");
                    display.setText(select);
                    display.setSelection(position - 1);
                }
                break;


        }
    }


}
