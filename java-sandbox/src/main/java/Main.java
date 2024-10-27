import core.objects.PolynomialRing;
import core.order.CommonOrderings;
import kotlin.Pair;

public class Main {
    public static void main(String[] args) {
        var ring = new PolynomialRing<>("x", "y");
        var x = ring.monomial(new Pair<>("x", 1));
        var yPow3 = ring.monomial(new Pair<>("y", 3));

        System.out.println(CommonOrderings.INSTANCE.lexicographicOrder().compare(x, yPow3));
        System.out.println(CommonOrderings.INSTANCE.gradedLexicographicOrder().compare(x, yPow3));
    }
}
