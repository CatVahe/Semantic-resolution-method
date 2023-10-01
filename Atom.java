import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Atom {
    String atom;
    boolean negative;
    int index;

    public Atom (String atom, boolean negative, int index){
        this.atom = atom;
        this.negative = negative;
        this.index = index;
    }

    public Atom (String atom, int index){
        this.atom = atom;
        this.negative = false;
        this.index = index;
    }

    public Atom (){
        this.atom = "";
        this.negative = false;
        this.index = -1;
    }

    //es inch apush kod a

    static void startResolution (HashMap<String, LinkedList<Atom>> setClause){

        HashMap<String, LinkedList<Atom>> trueGroup = new HashMap<>();
        HashMap<String, LinkedList<Atom>> falseGroup = new HashMap<>();
        for (int j = 0; j < setClause.size(); j++) {
            for (int k = 0; k < setClause.get(Integer.toString(j)).size(); k++) {
                if (setClause.get(Integer.toString(j)).get(k).negative &&
                        !tableContainFormula(falseGroup, setClause.get(Integer.toString(j))))
                    falseGroup.put(Integer.toString(j), setClause.get(Integer.toString(j)));
            }
            if (!tableContainFormula(falseGroup,setClause.get(Integer.toString(j))) &&
                    !tableContainFormula(trueGroup, setClause.get(Integer.toString(j))))
                trueGroup.put(Integer.toString(j), setClause.get(Integer.toString(j)));
        }
        for (int i = 0; i < setClause.size(); i++) {
            System.out.println(i + ". " + "{" + setClause.get(Integer.toString(i)).toString() + "}");
        }

        if(iterationTable(setClause, trueGroup, falseGroup)) {

                if(setClause.get(Integer.toString(setClause.size()-1)).size() == 0)
                    System.out.println("system solved");
                else
                    System.out.println("it is impossible to find an empty clause");

            System.out.println("end method");
        }


    }

    static boolean iterationTable (HashMap<String,LinkedList<Atom>> setClause,
                                   HashMap<String, LinkedList<Atom>> trueGroup,
                                   HashMap<String, LinkedList<Atom>> falseGroup )
    {
        System.out.print("solution of the system by the resolution method\n");
        boolean alteration = false;

        int i = 0;
        while (i < trueGroup.size())
        {
            int j = 0;
            while (j <falseGroup.size())
            {
                ArrayList<String> keyTrue = new ArrayList<>(trueGroup.keySet());
                ArrayList<String> keyFalse = new ArrayList<>(falseGroup.keySet());
                LinkedList<Atom>  formula1 = trueGroup.get(keyTrue.get(i));
                LinkedList<Atom>  formula2 = falseGroup.get(keyFalse.get(j));

                HashMap<String, LinkedList<Atom>> newFormula = Atom.resolventTwoAtom(formula1, formula2);

                if (formula1.size() == 1 && formula2.size() == 1) {

                    if (setClause.get(Integer.toString(i)).get(0).atom.compareTo(setClause.get(Integer.toString(j)).get(0).atom) == 0 &&
                            setClause.get(Integer.toString(i)).get(0).negative != setClause.get(Integer.toString(j)).get(0).negative) {
                        LinkedList<Atom> at = new LinkedList<>();
                        at.add(new Atom("", false, -1));
                        int number = setClause.size();
                        setClause.put(Integer.toString(number), at);
                        printResolvent(setClause,keyTrue.get(i), keyFalse.get(j), Integer.toString(number));
                        return false;
                    }

                }
                int z = 0;
                while (z < newFormula.size())
                {

                    if (!tableContainFormula(setClause, newFormula.get("0")))
                    {
                        int number = setClause.size();
                        setClause.put(Integer.toString(number), newFormula.get(Integer.toString(z)));
                        printResolvent(setClause,keyTrue.get(i), keyFalse.get(j), Integer.toString(number));
                        alteration = true;
                        int k = 0;
                        while (k < newFormula.get(Integer.toString(z)).size()) {
                            for (int l = 0; l < newFormula.get("0").size(); l++) {
                                if (newFormula.get("0").get(l).negative) {
                                    falseGroup.put(Integer.toString(number), newFormula.get("0"));
                                }
                            }
                            if(!tableContainFormula(falseGroup, newFormula.get("0"))) {
                                trueGroup.put(Integer.toString(number), newFormula.get("0"));
                                i = 0;
                            }

                            k++;
                        }
                    }
                    z++;
                }
                j++;
            }
            i++;
        }
        return alteration;
    }

    static HashMap<String,LinkedList<Atom> > resolventTwoAtom(LinkedList<Atom> a, LinkedList<Atom> b)
    {
        HashMap<String, LinkedList<Atom>> setClause = new HashMap<>();
        int i = 0;
        while (i < a.size())
        {
            int j = 0;
            while (j < b.size())
            {
                if (a.get(i).atom.compareTo(b.get(j).atom) == 0 && a.get(i).negative != b.get(j).negative)
                {
                    LinkedList<Atom> newAtom;
                    if(order(a,b,i)){
                        newAtom = getMatch(a, b, i, j);
                        if (!tableContainFormula(setClause, newAtom))
                            setClause.put("0",getMatch(a, b, i, j));
                    }
                }
                j++;
            }
            i++;
        }
        return setClause;
    }
    // new matches
    static LinkedList<Atom> getMatch (LinkedList<Atom> a, LinkedList<Atom> b, int match_a, int match_b){
        LinkedList<Atom> clause = new LinkedList<>();

        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                if(!contain(clause, a.get(i)) && i != match_a)
                    clause.add(a.get(i));
                if(!contain(clause, b.get(j)) && j != match_b)
                    clause.add(b.get(j));
            }
        }
        return clause;
    }
    //atom =
    boolean compare (Atom a){
        return ((this.atom.compareTo(a.atom) == 0) && (this.negative == a.negative));
    }

    static  boolean contain (LinkedList<Atom> a, Atom b){
        for (Atom value : a) {
            if (value.compare(b))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return (negative? "~" : "") + atom;
    }

    static HashMap<String, LinkedList<Atom>> atomFromFormula(String formula, String order) {
        HashMap<String,LinkedList<Atom>> setClause = new HashMap<>();
        StringBuilder s = new StringBuilder();
        int number = 0;
        for(int i = 1; i < formula.length()-1; i++)
        {
            if (formula.charAt(i) != '}')
            {
                s.append(formula.charAt(i));
            }
            else
            {
                if (formula.charAt(i) == '}')
                {
                    s.append("}");
                    i++;
                }
                setClause.put(Integer.toString(number), Atom.atomFromString(s.toString(), order));
                number++;
                s = new StringBuilder();
            }
        }

        return setClause;
    }

    static LinkedList<Atom> atomFromString(String formulaClausal,String order){
        LinkedList<Atom> clause = new LinkedList<>();
        StringBuilder s = new StringBuilder();
        String[] orderChar = order.split(",");


        for (int i = 1; i < formulaClausal.length()-1; i++) {
            if(formulaClausal.charAt(i) != '+') {
                s.append(formulaClausal.charAt(i));
            } else {
                if(s.substring(0, 1).charAt(0) == '~')
                    clause.add(new Atom(s.substring(1), true, indexAtom(s.substring(1), orderChar)));
                else
                    clause.add(new Atom(s.toString(), false, indexAtom(s.toString(), orderChar)));
                s = new StringBuilder();
            }
        }
        if(s.substring(0,1).charAt(0) == '~')
            clause.add(new Atom(s.substring(1),true,indexAtom(s.substring(1), orderChar)));
        else
            clause.add(new Atom(s.toString(), false, indexAtom(s.toString(), orderChar)));
        return clause;
    }

    static int indexAtom (String atom, String[] index){
        int i = 0;
        StringBuilder predicate = new StringBuilder();
        for (int j = 0; j < atom.length(); j++) {
            if(atom.charAt(j) != '(' )
                predicate.append(atom.charAt(i));
            else break;
        }
        while(i < index.length) {
            if(index[i].equals(predicate.toString())){
                return i;
            }
            i++;
        }
        return -1;
    }

    static boolean tableContainFormula (HashMap<String,LinkedList<Atom>> setClause, LinkedList<Atom> clause)    {

        for (LinkedList<Atom> atoms : setClause.values()) {
            boolean containB = true;
            int containsCount = 0;
            for (Atom value : clause) {
                if (!contain(atoms, value)) {
                    containB = false;
                    break;
                } else
                    containsCount++;
            }
            if (containB && containsCount == atoms.size())
                return true;
        }
        return false;
    }

    static boolean order (LinkedList<Atom> a, LinkedList<Atom> b, int matchA){
        for (Atom value : a) {
            for (Atom item : b) {
                if (a.get(matchA).index > value.index || a.get(matchA).index > item.index)
                    return false;
            }
        }
        return true;
    }

    static void printResolvent (HashMap<String, LinkedList<Atom>> setClause, String clause1, String clause2, String key ) {
        System.out.println(key + ". " + "{" + setClause.get(key) + "}" + "     " + "resolvent " + clause1 + " and " + clause2);

    }
}
