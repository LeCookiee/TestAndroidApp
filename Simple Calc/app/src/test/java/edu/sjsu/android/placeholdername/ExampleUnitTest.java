package edu.sjsu.android.placeholdername;
import org.junit.Test;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IExpr;
import static org.junit.Assert.*;




public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test() {
        ExprEvaluator eval = new ExprEvaluator(false, (short) 100);
        IExpr result = eval.eval("hiiuoohi");
        System.out.println(result.toString());
    }
}