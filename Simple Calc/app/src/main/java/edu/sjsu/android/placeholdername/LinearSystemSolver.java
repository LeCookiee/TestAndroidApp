package edu.sjsu.android.placeholdername;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.math.MathException;
import java.util.ArrayList;
import java.util.StringJoiner;
import edu.sjsu.android.placeholdername.databinding.FragmentLinearSystemSolverBinding;

public class LinearSystemSolver extends Fragment {

    private FragmentLinearSystemSolverBinding binding;
    private final ArrayList<Character> variables = new ArrayList<>();
    public LinearSystemSolver() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLinearSystemSolverBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ImageButton buttonNavigateToMainScreen = view.findViewById(R.id.imageButton2);
        buttonNavigateToMainScreen.setOnClickListener(v -> navigateToMainScreen());

        binding.solutionView.setFontSize(MathUtil.LatexFontSize);
        binding.equationsInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                char[] chars = text.toCharArray();
                variables.clear();
                //scan for variables
                for (Character c: chars) {
                    if (Character.isAlphabetic(c) && !variables.contains(c)) {
                        variables.add(c);
                    }
                }

                //update detected variables text for the user
                if (variables.isEmpty()) {
                    binding.varDetectedLabel.setText(getString(R.string.variables_detected_none));
                } else {
                    StringBuilder builder = new StringBuilder();
                    for (Character c: variables) {
                        builder.append(c);
                        builder.append(", ");
                    }
                    String variablesDetectedText = getString(R.string.variables_detected, builder.toString());

                    binding.varDetectedLabel.setText(variablesDetectedText);
                }

            }
        });

        binding.solveBtn.setOnClickListener(v -> {
            binding.solutionLabel.setVisibility(View.INVISIBLE);
            binding.solutionView.setVisibility(View.INVISIBLE);

            String[] equations = binding.equationsInput.getText().toString().split("\n");
            StringBuilder errorBuilder = new StringBuilder();

            if (equations.length < 2) { //not a system of linear equations
                errorBuilder.append("MATH ERROR: Input is not a system of linear equations\n");
            } else {
                StringJoiner leftMatrix = new StringJoiner(", ", "{", "}");
                StringJoiner rightMatrix = new StringJoiner(", ", "{", "}");

                for (int i = 0; i < equations.length; i++) { //parse equations to get matrix elements
                    String equation = equations[i];
                    int equalSignIdx = equation.indexOf("=");

                    if (equalSignIdx == -1) {
                        errorBuilder.append("SYNTAX ERROR: Equation ").append(i + 1).append(" does not have an equal sign\n");
                        continue;
                    }

                    String leftHalf = equation.substring(0, equalSignIdx);
                    String rightHalf = equation.substring(equalSignIdx + 1);

                    IExpr parsedLeftHalf = MathUtil.parse(leftHalf); //process the left half of the equation
                    StringJoiner matrixRow = new StringJoiner(", ", "{", "}");
                    for (Character c : variables) { //get coefficients of each variable in equation, insert into matrix
                        IExpr coefficient = MathUtil.evaluate("Coefficient(" + parsedLeftHalf + ", " + c + ")");
                        if (coefficient.toString().equals("0") && leftHalf.contains(c.toString())) { //equation is not linear
                            errorBuilder.append("MATH ERROR: Equation ").append(i + 1).append(" is not a linear equation\n");
                        } else {
                            matrixRow.add(coefficient.toString());
                        }
                    }
                    leftMatrix.add(matrixRow.toString());

                    try {
                        Double.parseDouble(rightHalf.replace("/","")); //checks if right side only contains constant, also accounts for fractions
                        rightMatrix.add(rightHalf);
                    } catch (NumberFormatException e) {
                        errorBuilder.append("MATH ERROR: Right side of Equation ").append(i + 1).append(" is not a constant\n");
                    }
                }

                if (errorBuilder.toString().isEmpty()) { //calculate if no errors found
                    binding.errorLabel.setText(getString(R.string.errors_found_no_errors_found));

                    Log.d("LeftMatrix", leftMatrix.toString());
                    Log.d("RightMatrix", rightMatrix.toString());

                    try {
                        String solution = MathUtil.evaluate("LinearSolve(" + leftMatrix + ", " + rightMatrix + ")").toString();
                        Log.d("Solution", solution);
                        binding.solutionLabel.setVisibility(View.VISIBLE);
                        if (solution.contains("LinearSolve")) { //Symja failed to calculate or there was no solution
                            binding.solutionLabel.setText(getString(R.string.system_no_solution));
                            binding.solutionView.setVisibility(View.INVISIBLE);
                        } else {
                            String[] solutions = solution.substring(1, solution.length() - 1).split(","); //remove brackets, split string to get numbers
                            StringJoiner solutionText = new StringJoiner(", ");
                            for (int i = 0; i < solutions.length; i++) {
                                String[] fractionComponents = solutions[i].split("/"); //get numerator/denominator of fraction
                                if (fractionComponents.length == 1) { //array size of 1 indicates that number is not a fraction
                                    solutionText.add(variables.get(i) + " = " + fractionComponents[0]);
                                } else {
                                    solutionText.add(variables.get(i) + " = " + MathUtil.toLatexFraction(fractionComponents[0], fractionComponents[1]));
                                }
                            }
                            binding.solutionLabel.setText(getString(R.string.system_solution_found));
                            binding.solutionView.setLatex(solutionText.toString());
                            binding.solutionView.setVisibility(View.VISIBLE);
                        }
                    } catch (MathException me) {
                        binding.errorLabel.setText(getString(R.string.evaluation_error));
                        binding.solutionView.setVisibility(View.INVISIBLE);
                    }
                } else {
                    binding.errorLabel.setText(errorBuilder.toString());
                }
            }
        });

        return binding.getRoot();
    }
    private void navigateToMainScreen() {
        // Perform fragment transaction to navigate to MainScreenFragment
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainerView, new mainScreen());
        transaction.addToBackStack(null); // Optional: Add to back stack
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}