package edu.sjsu.android.placeholdername;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.math.MathException;

import java.util.StringJoiner;

public class NullSpaceCalculator extends EchelonFormCalculator {
    public NullSpaceCalculator() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setFragmentTitleAndDesc(R.string.nullspace_calc_fragment_title, R.string.nullspace_calc_fragment_desc);
        return getBinding().getRoot();
    }


    @Override
    public void onCalculate() {
        StringBuilder errorBuilder = new StringBuilder();
        StringJoiner matrixBuilder = new StringJoiner(",", "{", "}");
        String[] rows = getBinding().matInputBox.getText().toString().split("\n"); //get user input

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

        if (errorBuilder.toString().equals("")) { //no parsing errors generated, proceed to evaluate
            getBinding().errorLabel.setText(R.string.errors_found_message);

            try {
                IExpr result = MathUtil.evaluate("NullSpace(" + matrixBuilder + ")");
                String text = result.toString();
                Log.d("NullSpaceOutputSet", text);

                if (!text.contains("NullSpace")) { //eval did not return an error, convert returned result into latex form
                    if (text.equals("{}"))
                        getBinding().efSolutionDisplay.setLatex("$\\emptyset$");
                    else {
                        String[] vectors = text.substring(1, text.length() - 1).split("\n");
                        for (int i = 0; i < vectors.length; i++) {
                            String oldText = vectors[i].trim();

                            if (oldText.charAt(oldText.length()-1) == ',') //check if there's a trailing comma and remove it along with braces
                                oldText = oldText.substring(1, oldText.length() - 2);
                            else
                                oldText = oldText.substring(1, oldText.length() - 1);
                            
                            vectors[i] = MathUtil.createLatexVector(oldText.split(","));
                        }
                        getBinding().efSolutionDisplay.setLatex(MathUtil.createLatexSet(vectors));
                    }
                    getBinding().efSolutionDisplay.setVisibility(View.VISIBLE);
                } else {
                    getBinding().errorLabel.setText(R.string.evaluation_error_message);
                    getBinding().efSolutionDisplay.setVisibility(View.INVISIBLE);
                }
            } catch (MathException me) {
                getBinding().errorLabel.setText(R.string.evaluation_error_message);
                getBinding().efSolutionDisplay.setVisibility(View.INVISIBLE);
            }
        } else {
            getBinding().errorLabel.setText(errorBuilder.toString());
        }
    }
}