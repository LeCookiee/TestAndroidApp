package edu.sjsu.android.placeholdername;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.math.MathException;

import java.util.StringJoiner;

public class DeterminantCalculator extends InverseMatrixCalculator {
    public DeterminantCalculator() {
        //required empty constructor
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setFragmentTitleAndDesc(R.string.determinant_calc_fragment_title, R.string.determinant_calc_fragment_desc);
        return getBinding().getRoot();
    }

    @Override
    public void onCalculate() {
        getBinding().efSolutionLabel.setText(getString(R.string.solution_displayed_below));

        StringBuilder errorBuilder = new StringBuilder();
        StringJoiner matrixBuilder = new StringJoiner(",", "{", "}");
        String[] rows = getBinding().matInputBox.getText().toString().split("\n"); //get user input

        if (rows.length != matrixSize) { //user inputted wrong number of rows
            errorBuilder.append("SYNTAX ERROR: expected ").append(matrixSize).append(" rows, but input matrix contains ").append(rows.length).append(" rows\n");
        }

        for (int i = 0; i < rows.length; i++) { //parse matrix for evaluator to solve
            String row = rows[i];
            String[] rowElements = row.split(" ");

            if (rowElements.length != matrixSize) { //user inputted wrong number of columns
                errorBuilder.append("SYNTAX ERROR: expected ").append(matrixSize).append(" columns in row ").append(i + 1).append(", but row ").append(i + 1).append(" contains ").append(rowElements.length).append(" columns\n");
            } else {
                String parsedRow = "{" + String.join(",", rowElements) + "}";
                matrixBuilder.add(parsedRow);
            }
        }

        Log.d("DeterminantMatrixInput", matrixBuilder.toString());

        if (errorBuilder.toString().isEmpty()) { //no parsing errors generated, proceed to evaluate
            getBinding().errorLabel.setText(getString(R.string.errors_found_no_errors_found));

            try {
                IExpr result = MathUtil.evaluate("Det(" + matrixBuilder + ")");
                String text = result.toString();
                Log.d("InversedMatrix", text);

                getBinding().efSolutionDisplay.setLatex(MathUtil.createLatexString(text));
                getBinding().efSolutionDisplay.setVisibility(View.VISIBLE);
            } catch (MathException me) {
                getBinding().errorLabel.setText(getString(R.string.evaluation_error));
                getBinding().efSolutionDisplay.setVisibility(View.INVISIBLE);
            }
        } else {
            getBinding().errorLabel.setText(errorBuilder.toString());
        }
    }
}