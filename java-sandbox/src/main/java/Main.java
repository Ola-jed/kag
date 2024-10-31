import core.objects.Monomial;
import core.objects.Polynomial;
import core.objects.PolynomialRing;
import core.order.CommonOrderings;
import core.operators.PolynomialOperatorsKt;
import kotlin.Pair;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        var ring = new PolynomialRing<>("x", "y");
        var x = ring.monomial(new Pair<>("x", 1));
        var yPow3 = ring.monomial(new Pair<>("y", 3));
        var poly = new Polynomial<>(List.of(new Pair<Number, Monomial<Number>>(4, x)), CommonOrderings.INSTANCE.gradedLexicographicOrder());

        System.out.println(PolynomialOperatorsKt.plus(poly, poly));
    }
}
