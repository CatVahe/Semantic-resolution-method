import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class ForPredicats {
    public static void main(String[] args) {

        String formula;
        String order;

        Scanner scanner = new Scanner(System.in);
        order = scanner.nextLine();
        formula = scanner.nextLine();
        formula = Unification.unification(formula, order);
        System.out.println("unification result");
        System.out.println(formula);
        System.out.println();
        System.out.println("Start resolution");
        System.out.println();
        HashMap<String,LinkedList<Atom>> setClause = Atom.atomFromFormula(formula,order);
        Atom.startResolution(setClause);

    }
}