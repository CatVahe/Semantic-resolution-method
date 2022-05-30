import java.util.ArrayList;

public class Unification {

    static final String[] functional = {"f", "g", "h", "m", "n", "o", "p"};
    static final String[] constSymbol = {"a", "b", "c", "d", "e", "i", "j"};//{'a','b','c','d','e'};
    static final String[] variable = {"x", "y", "z", "u", "v", "w", "s", "t"}; //{'x','y','z','u','v'};

    static String unification(String formula, String order) {
        ArrayList<String> unificationSet;
        String temp = formula;
        ArrayList<ArrayList<String>> unified = new ArrayList<>();
        String[] p = order.split(",");
        String predicateTemp;
        System.out.println("Unification start");
        for (String s : p) {
            predicateTemp = s;
            unificationSet = unElement(predicateTemp, temp);
            unified.add((compareFrom(unificationSet, predicateTemp)));
        }

        formula = replace(formula, unified);

        //formula = compare(unificationSet);
        return formula;
    }

    static String compare(ArrayList<String> set1, ArrayList<String> set2) {
        StringBuilder s = new StringBuilder();

        if (set1.size() == set2.size()) {
            for (int j = 0; j < set1.size(); j++) {
                String a = set1.get(j);
                String b = set2.get(j);
                if (contain(a, constSymbol) && contain(b, variable)){
                    s.append(a);
                }
                else if (contain(Character.toString(a.charAt(0)), functional) && contain(b, variable)
                        && compareFuncElements(a, b)){
                    s.append(a);
                }
                else if (contain(a, variable) && contain(b, variable)){
                    s.append(a);
                }
                else if (contain(a, variable) && contain(b, constSymbol)){
                    s.append(b);
                }
                else if (contain(a, variable) && contain(Character.toString(b.charAt(0)), functional)
                        && compareFuncElements(b, a))
                    s.append(b);
                else if (contain(a, constSymbol) && contain(b, constSymbol))
                    return "not unified";
                else if (contain(Character.toString(a.charAt(0)), functional) && contain(b, constSymbol))
                    return "not unified";
                else if (contain(a, constSymbol) && contain(Character.toString(a.charAt(0)), functional))
                    return "not unified";
                s.append(',');
            }
            s.deleteCharAt(s.lastIndexOf(","));
        }
        return s.toString();
    }

    static ArrayList<String> compareFrom (ArrayList<String> unificationSet, String pre) {
        ArrayList<ArrayList<String>> elementSet = new ArrayList<>();
        StringBuilder temp = new StringBuilder();

        for (String s : unificationSet) {
            elementSet.add(elementSplit(s));
        }
        for (int j = 0; j < elementSet.size() - 1; j++) {
            if (!compare(elementSet.get(j), elementSet.get(j + 1)).equals("not unified")) {
                temp.append(pre).append('(');
                temp.append(compare(elementSet.get(j), elementSet.get(j + 1))).append(')');
                System.out.println(pre + " " + elementSet.get(j) +" " + pre + " " + elementSet.get(j +1) + "---->" + temp);
                unificationSet.set(j, temp.toString());
                unificationSet.set(j + 1, temp.toString());
                elementSet.set(j+1, elementSplit(temp.toString()));
            }
            else
                System.out.println(pre + " " + elementSet.get(j) +" " + pre + " " + elementSet.get(j +1) + "---->not unified");
            temp = new StringBuilder();
        }
        return unificationSet;
    }

    static String replace (String formula, ArrayList<ArrayList<String>> unified) {
        StringBuilder s = new StringBuilder();
        s.append(formula);
        int i = -1, k = 0;
        while (k < unified.size()) {
            int k1 = 0;
            while (k1 < unified.get(k).size()){
                for (int j = 0; j < s.toString().length(); j++) {
                    if(unified.get(k).get(k1).charAt(0) == s.toString().charAt(j)) {
                        i++;
                        if (i == k1) {
                            int last = predicateLimit(s.toString(), j);
                            s.delete(j, last);
                            s.insert(j, unified.get(k).get(i));
                            i = -1;
                            break;
                        }
                    }
                }
                k1++;
            }
            k++;
        }
        return s.toString();
    }

    static ArrayList<String> unElement (String p, String formula){
        StringBuilder s = new StringBuilder();
        ArrayList<String> EX = new ArrayList<>();
        for (int i = 0; i < formula.length(); i++) {
           if(formula.charAt(i) == p.charAt(0)){
               int preLim = predicateLimit(formula, i);
               s.append(formula, i, preLim);
               i = preLim;
               EX.add(s.toString());
           }
           s = new StringBuilder();
        }
        return EX;

    }

    static ArrayList<String> elementSplit (String atom){
        ArrayList<String> elements = new ArrayList<>();
            for (int j = 2; j < atom.length()-1; j++) {
                if (!contain(Character.toString(atom.charAt(j)), functional) &&
                        atom.charAt(j) != ',')
                    elements.add(Character.toString(atom.charAt(j)));
                else if(contain(Character.toString(atom.charAt(j)), functional)){
                    int endFunc = functionLimit(atom.substring(j));
                    elements.add(atom.substring(j, endFunc + j+1));
                    j = endFunc + j;
                }
            }
        return elements;
        }


    static int functionLimit (String formula){
        int countOpen = 0, countClose = 0;
        for (int i = 0; i < formula.length(); i++) {
            if(formula.charAt(i) == '(')
                countOpen++;
            if(formula.charAt(i) == ')')
                countClose++;
            if(countOpen !=0 && countClose == countOpen)
                return i;
        }
        return formula.indexOf(')');
    }
    static int predicateLimit (String formula, int index){
        int countOpen = 0, countClose = 0;
        for (int i = index; i < formula.length(); i++) {
            if(formula.charAt(i) == '(')
                countOpen++;
            if(formula.charAt(i) == ')')
                countClose++;
            if(countOpen !=0 && countClose == countOpen)
                return i+1;
        }
        return formula.indexOf(')');
    }

    static boolean compareFuncElements (String elements, String el){
        for (int i = 0; i < elements.length(); i++) {
            if(elements.charAt(i) == el.charAt(0))
                return false;
        }
        return true;
    }  
    
    static boolean contain (String symbol, String[] elType){
        for (String el :
                elType) {
            if(el.equals(symbol))
                return true;
        }
        return false;

    }

}
