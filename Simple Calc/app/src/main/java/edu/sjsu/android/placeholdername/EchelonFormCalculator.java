package edu.sjsu.android.placeholdername;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.math.MathException;

import java.util.StringJoiner;

import edu.sjsu.android.placeholdername.databinding.FragmentEchelonFormCalculatorBinding;

public class EchelonFormCalculator extends Fragment {

    public static final int MAX_MATRIX_SIZE = 10;
    protected int rowCount = 1;
    protected int columnCount = 1;

    private String functionName = "RowReduce";
    private FragmentEchelonFormCalculatorBinding binding;
    public EchelonFormCalculator() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEchelonFormCalculatorBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ImageButton buttonNavigateToMainScreen = view.findViewById(R.id.imageButton2);//to mainscreen
        buttonNavigateToMainScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMainScreen();
            }
        });

        binding.efSolutionDisplay.setFontSize(MathUtil.LatexFontSize);
        binding.rowEditNum.setOnFocusChangeListener(new RowColumnFocusListener());
        binding.columnEditNum.setOnFocusChangeListener(new RowColumnFocusListener());

        binding.calculateBtn.setOnClickListener(v -> onCalculate());

        return view;
    }
    private void navigateToMainScreen() {
        // Perform fragment transaction to navigate to MainScreenFragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, new mainScreen());
        transaction.addToBackStack(null); // Optional: Add to back stack
        transaction.commit();
    }
    public void setFragmentTitleAndDesc(int titleResId, int descResId) {
        binding.fragmentTitle.setText(titleResId);
        binding.fragmentDesc.setText(descResId);
    }
    public FragmentEchelonFormCalculatorBinding getBinding() {return binding;}


    public void onCalculate() {
        StringBuilder errorBuilder = new StringBuilder();
        StringJoiner matrixBuilder = new StringJoiner(",", "{", "}");
        String[] rows = binding.matInputBox.getText().toString().split("\n"); //get user input

        if (rows.length != rowCount) { //user inputted wrong number of rows
            errorBuilder.append("SYNTAX ERROR: expected ").append(rowCount).append(" rows, but input matrix contains ").append(rows.length).append(" rows\n");
        }

        for (int i = 0; i < rows.length; i++) { //parse matrix for evaluator to solve
            String row = rows[i];
            String[] rowElements = row.split(" ");

            if (rowElements.length != columnCount) { //user inputted wrong number of columns
                errorBuilder.append("SYNTAX ERROR: expected ").append(columnCount).append(" columns in row ").append(i + 1).append(", but row ").append(i + 1).append(" contains ").append(rowElements.length).append(" columns\n");
            } else {
                String parsedRow = "{" + String.join(",", rowElements) + "}";
                matrixBuilder.add(parsedRow);
            }
        }

        Log.d("InputMatrix", matrixBuilder.toString());

        if (errorBuilder.toString().isEmpty()) { //no parsing errors generated, proceed to evaluate
            binding.errorLabel.setText(R.string.errors_found_message);

            try {
                IExpr result = MathUtil.evaluate(functionName + "(" + matrixBuilder + ")");
                String text = result.toString();
                Log.d("OutputMatrix", text);

                if (!text.contains(functionName)) { //eval did not return an error, convert returned result into latex form
                    String[][] solutionMatrix = new String[rowCount][columnCount];
                    String[] solutionRows = text.substring(1, text.length() - 1).split("\n");
                    for (int i = 0; i < solutionRows.length; i++) {
                        String trimmed = solutionRows[i].trim();

                        if (trimmed.charAt(trimmed.length()-1) == ',') //check if there's a trailing comma and remove it along with braces
                            trimmed = trimmed.substring(1, trimmed.length() - 2);
                        else
                            trimmed = trimmed.substring(1, trimmed.length() - 1);

                        Log.d("trimmed2", trimmed);
                        solutionMatrix[i] = trimmed.split(",");
                    }

                    Log.d("LatexMatrix", MathUtil.createLatexMatrix(solutionMatrix));
                    binding.efSolutionDisplay.setLatex(MathUtil.createLatexMatrix(solutionMatrix));
                    binding.efSolutionDisplay.setVisibility(View.VISIBLE);
                } else {
                    binding.errorLabel.setText(R.string.evaluation_error_message);
                    binding.efSolutionDisplay.setVisibility(View.INVISIBLE);
                }
            } catch (MathException me) {
                binding.errorLabel.setText(R.string.evaluation_error_message);
                binding.efSolutionDisplay.setVisibility(View.INVISIBLE);
            }
        } else {
            binding.errorLabel.setText(errorBuilder.toString());
        }
    }

    public class RowColumnFocusListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            String text = ((EditText) v).getText().toString();

            if (text.isEmpty())
                return;

            if (!hasFocus) { //user has likely finished inputting text
                binding.errorLabel.setText(R.string.errors_found_message);
                int count = Integer.parseInt(text);

                if ((count == rowCount && v == binding.rowEditNum) || (count == columnCount && v == binding.columnEditNum))
                    return;

                if (count == 0) {
                    binding.errorLabel.setText(R.string.syntax_error_message);
                    return;
                } else if (count > MAX_MATRIX_SIZE) { //matrix size too large
                    binding.errorLabel.setText(R.string.matrix_size_error_message);
                }

                if (v == binding.rowEditNum)
                    rowCount = count;
                else if (v == binding.columnEditNum)
                    columnCount = count;
            }
        }
    }
}