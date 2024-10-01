package edu.sjsu.android.placeholdername;

import android.util.Log;

import org.matheclipse.core.eval.EvalEngine;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.parser.ExprParser;
import org.matheclipse.core.parser.ExprParserFactory;

import java.util.StringJoiner;

public class MathUtil {

    public static final float LatexFontSize = 28f;
    private static final ExprEvaluator MathEvaluator = new ExprEvaluator(false, (short) 100);
    private static final ExprParser MathParser = new ExprParser(EvalEngine.get(), ExprParserFactory.RELAXED_STYLE_FACTORY, true);


    public static IExpr evaluate(String function) {
        return MathEvaluator.eval(function);
    }

    public static IExpr parse(String expression) {return MathParser.parse(expression);}

    public static String toLatexFraction(String numerator, String denominator) {
        return String.format("\\frac{%s}{%s}", numerator, denominator);
    }

    public static String createLatexMatrix(String[][] mat) {
        StringJoiner rowJoiner = new StringJoiner("\\\\\n", "\\begin{bmatrix}", "\\end{bmatrix}");
        for (String[] row : mat) {
            StringJoiner columnJoiner = new StringJoiner(" & ");
            for (String ele : row) {
                String parsed = ele.replace("*", "");

                String[] fractionComponents = parsed.split("/");
                if (fractionComponents.length == 2) {
                    columnJoiner.add(toLatexFraction(fractionComponents[0], fractionComponents[1]));
                } else {
                    columnJoiner.add(ele);
                }
            }
            rowJoiner.add(columnJoiner.toString());
        }
        return rowJoiner.toString();
    }

    public static String createLatexSet(String[] set) {
        StringJoiner joiner = new StringJoiner(",", "\\left\\{", "\\right\\}");
        for (String s: set)
            joiner.add(s);
        return joiner.toString();
    }

    public static String createLatexVector(String[] vectorElements) {
        StringJoiner vecJoiner = new StringJoiner("\\\\\n", "\\begin{bmatrix}", "\\end{bmatrix}");
        for (String element : vectorElements) {
            String[] fractionComponents = element.replace("*", "").split("/");
            if (fractionComponents.length == 2) {
                vecJoiner.add(toLatexFraction(fractionComponents[0], fractionComponents[1]));
            } else {
                vecJoiner.add(element);
            }
        }
        Log.d("VectorOutput", vecJoiner.toString());
        return vecJoiner.toString();
    }

    public static String createLatexString(String text)  {
        return text.replace("*", "");
    }
}
