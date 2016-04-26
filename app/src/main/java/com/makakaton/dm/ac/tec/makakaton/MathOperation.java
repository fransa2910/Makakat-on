package com.makakaton.dm.ac.tec.makakaton;

import android.util.Log;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import org.apache.commons.math3.analysis.integration.TrapezoidIntegrator;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by samyoo on 3/9/16.
 */
public class MathOperation {
    private double subLevel = 1;
    private int limitExp = 0; //how many times can be expanded (operation)
    private int rounds = 1; //
    private int maxRounds; //maximum difficulty to reach.
    private String level;
    private int round;
    private int derAndInt = 3;
    private int easy = 2;
    private int medium = 3;
    private int hard = 4;
    private int derORInt0;
    private int derORInt1;
    private String  typeFunction;

    public int getDerORInt1() {
        return derORInt1;
    }

    public int getDerORInt0() {
        return derORInt0;
    }

    public String getTypeFunction() {
        return typeFunction;
    }

    public double getSubLevel() {
        return subLevel;
    }

    public int getMaxRounds() {
        return maxRounds;
    }

    public void setSubLevel(double subLevel) {
        this.subLevel = subLevel;
    }

    public void resetRounds() {
        this.rounds = 1;
    }

    public void setMaxRound(int maxRounds) {
        this.maxRounds = maxRounds;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    public void resetLimitExp() {
        this.limitExp = 0;
    }

    public void chooseRandomlyMaxRounds() {
        Random rand = new Random();
        this.maxRounds = rand.nextInt(10) + 15;
    }

/*    public void chooseRandomlyMaxRoundsMedium(){
        Random rand = new Random();
        this.maxRounds = rand.nextInt(10)+25;
    }*/

    public void increaseRounds() {
        this.rounds++;
    }

    public void increaseLimitExp() {
        this.limitExp++;
    }

    public int getLimitExp() {
        return limitExp;
    }

    public String getLevel() {
        return level;
    }


    public void setLevel(String level) {
        this.level = level;
    }

    public String mathLevel(String whichLevel) {
        if (whichLevel.equals("LEVEL 1")) {
            return createOperationL1();
        } else if (whichLevel.equals("LEVEL 2")) {
            return createOperationL2();
        } else {
            return createOperationL3();
        }


    }

    /*public String mathLevel2(){
        Random random = new Random();
        int limitExp = random.nextInt(whichLevel);
        String opearation = "0";

       // int randExpression1 = random.nextInt(limitExp);
        //int randExpression2 = random.nextInt(limitExp);


        if (0<=limitExp && limitExp<=subLevel1){
            String operation = createOperation(limitExp);

            return operation;
        }



        return operation;
    }
*/
    /*private  String createOperation1(int randExpression1, int randOperation, int randExpression2, int limitExp){
        Random random = new Random();
        int number = random.nextInt(12);
        return expression(randExpression1, limitExp) + operation(randOperation) + expression(randExpression2, limitExp);
    }
*/
    public String createOperationL1() {
        return expression() + operation() + expression();/*watch out limitExp in operation*/
    }


    public String createOperationL2() {
        Log.e("create operation L2??", "yes, im there");
        return "(" + expression2() + operation() + expression2() + ")";/*watch out limitExp in operation*/
    }

    public String createOperationL3() {
        derORInt0= Integer.valueOf(number());
        derORInt1 = Integer.valueOf(number());
        typeFunction = integrateOrDerivative();
        if (subLevel == 1) {

            return typeFunction+ number() + "x^" + 2 + operation3() + expression3(1);/*watch out limitExp in operation*/
        } else if (subLevel == 2) {
            return  typeFunction + number() + "x^" + 3 + operation3() + expression3(2);/*watch out limitExp in operation*/
        } else {
            if (derAndInt < 4) {
                derAndInt++;

            }
            return typeFunction + number() + "x^" + String.valueOf(derAndInt) + operation3() + expression3(derAndInt - 1);/*watch out limitExp in operation*/
        }

    }

    private String integrateOrDerivative() {
        double matrand = Math.random();
        if (0 <= matrand && matrand < 0.5) {
            return "dx/dy";
        }

        return "∫";

    }


    private String operation() {
        double rand = Math.random();
        if (0 <= rand && rand < 0.70) {
            return "+";
        } else if (0.70 <= rand && rand < 0.86) {
            return "-";
        } else {
            return "*";
        }
    }

    /*    private  String expression(int randExpression, int limitOp){
            Random random = new Random();
            int probOperation = random.nextInt(100);
            int probExpression1= random.nextInt(limitOp);
            int probExpression2= random.nextInt(limitOp);
            if (0<=randExpression && randExpression < 3){
                return expression(probExpression1,limitOp) +operation(probOperation) + expression(probExpression2,limitOp);
            }else{
                return number();
            }

        }*/
    private String expression() {
        double whatToDo = Math.random();
        Log.e("what to do", String.valueOf(whatToDo));
        if (subLevel == 1) {
            if (limitExp < easy) {
                limitExp++;
                if (0 <= whatToDo && whatToDo < 0.5) {

                    return expression() + operation() + expression();
                } else {
                    limitExp = 0;
                    return number();
                }

            }
            /*meter una condicion aqui*/


        } else if (subLevel == 2) {
            if (limitExp < medium) {
                limitExp++;
                if (0 <= whatToDo && whatToDo < 0.6) {
                    return expression() + operation() + expression();
                } else {

                    return number();
                }
            }
        } else {
            if (limitExp < hard) {
                limitExp++;
                if (0 <= whatToDo && whatToDo < 0.7) {
                    return expression() + operation() + expression();
                } else {

                    return number();
                }
            }
        }

        return number();

    }

    private String expression2() {
        double whatToDo = Math.random();
        Log.e("what to do", String.valueOf(whatToDo));
        if (subLevel == 1) {
            if (limitExp < easy) {
                limitExp++;
                if (0 <= whatToDo && whatToDo < 0.5) {

                    return "(" + expression2() + operation() + expression2() + ")";
                } else {
                    limitExp = 0;
                    return number();
                }

            }
            /*meter una condicion aqui*/


        } else if (subLevel == 2) {
            if (limitExp < medium) {
                limitExp++;
                if (0 <= whatToDo && whatToDo < 0.6) {
                    return "(" + expression2() + operation() + expression2() + ")";
                } else {

                    return number();
                }
            }
        } else {
            if (limitExp < hard) {
                limitExp++;
                if (0 <= whatToDo && whatToDo < 0.7) {
                    return "(" + expression2() + operation() + expression2() + ")";
                } else {

                    return number();
                }
            }
        }

        return number();

    }

    private String operation3() {
        double whatToDo = Math.random();
        if (0 <= whatToDo && whatToDo < 0.7) {
            return "+";
        }
        return "-";

    }

    private String expression3(int parameters) {
        double whatToDo = Math.random();
        //Log.e("what to do", String.valueOf(whatToDo));

        if ((0 <= whatToDo && whatToDo < 0.25) && 0 < parameters) {

            return number() + "x^" + parameters + operation3() + expression3(parameters - 1);
        } else {
            return number() + "x^" + parameters;
        }


            /*meter una condicion aqui*/


    }

    public double evalIntegralAndDerivative(String function) {
        SimpsonIntegrator simpson = new SimpsonIntegrator();
        TrapezoidIntegrator trapezoid = new TrapezoidIntegrator();
        int maxDegree;
        int maxDegreeFinal;
        System.out.println("function.indexOf(0) "+(Character.toString(function.charAt(0))));
        if ((Character.toString(function.charAt(0))).equals("∫")) {
            ArrayList<Double> lista= new ArrayList<>();
            System.out.println("entre!");
           maxDegree = Integer.parseInt(Character.toString(function.charAt(4)));
        System.out.println("maxDegree-> "+maxDegree);

            int maxDegree1 = maxDegree;
            for (int i = 1; i < function.length(); i = i + 5) {
                System.out.println("maxDegree1"+maxDegree1);
                System.out.println("function.indexOf(i + 3)"+Integer.parseInt(Character.toString(function.charAt(i + 3))));
                if (maxDegree1 == Integer.parseInt(Character.toString(function.charAt(i + 3)))) {
                    if (Character.toString(function.charAt(i-1)).equals("-")){
                        lista.add(-1*Double.valueOf(String.valueOf(Character.toString(function.charAt(i)))));
                    }else{
                        lista.add(Double.valueOf(String.valueOf(Character.toString(function.charAt(i)))));
                    }

                } else {
                    lista.add(0.0);
                }
                maxDegree1--;
            }
            if (lista.size()<maxDegree){
                for (int i = 0;i < maxDegree1;i++){
                    lista.add(0.0);
                }
            }
            double[] vector = new double[maxDegree+1];
            for (int i = 0; i < maxDegree; i++) {
                vector[maxDegree-i]= lista.get(i);
            }

            for (int i = 0; i < maxDegree; i++) {
                System.out.println(vector[i]);
            }


            PolynomialFunction f = new PolynomialFunction(vector);
            UnivariateFunction uf = (UnivariateFunction) new PolynomialFunction(vector);
            System.out.println(uf);
            System.out.println("To String " + uf.toString());
            System.out.println("Degree: " + f.degree());

            double i = simpson.integrate(10, uf, 0, 1);
            //double j = trapezoid.integrate(2, uf, 0, 1);
            //System.out.println("Trapezoid integral : " + j);
            System.out.println("Simpson integral : " + i);
            return i;
        }
        ArrayList<Double> listaDer= new ArrayList<>();
        maxDegree = Integer.parseInt(Character.toString(function.charAt(8)));
        System.out.println("maxDegree-> "+maxDegree);

        int maxDegree1 = maxDegree;
        for (int i = 5; i < function.length(); i = i + 5) {
            System.out.println("maxDegree1"+maxDegree1);
            System.out.println("function.indexOf(i + 3)"+Integer.parseInt(Character.toString(function.charAt(i + 3))));
            if (maxDegree1 == Integer.parseInt(Character.toString(function.charAt(i + 3)))) {
                if (Character.toString(function.charAt(i-1)).equals("-")){
                    listaDer.add(-1*Double.valueOf(String.valueOf(Character.toString(function.charAt(i)))));
                }else{
                    listaDer.add(Double.valueOf(String.valueOf(Character.toString(function.charAt(i)))));
                }

            } else {
                listaDer.add(0.0);
            }
            maxDegree1--;
        }

        if (listaDer.size()<maxDegree){
            for (int i = 0;i < maxDegree1;i++){
                listaDer.add(0.0);
            }
        }
        DerivativeStructure x = new DerivativeStructure(1, maxDegree, 0, derORInt1);
        DerivativeStructure[] vector = new DerivativeStructure[maxDegree];//maxdegree = 4 , vector max = 3
        vector[0] = x;
        for (int i = 1; i < maxDegree; i++) {
            vector[i] = x.pow(i+1);
        }
        if (subLevel==1){
            DerivativeStructure y = new DerivativeStructure(listaDer.get(0),vector[1], listaDer.get(1),x);
            return y.getPartialDerivative(1);
        }else if (subLevel==2){
            DerivativeStructure y = new DerivativeStructure(listaDer.get(0),vector[2], listaDer.get(1), vector[1],listaDer.get(2),x);
            return y.getPartialDerivative(1);
        }else{
            DerivativeStructure y = new DerivativeStructure(listaDer.get(0),vector[3], listaDer.get(1), vector[2],listaDer.get(2),vector[1],listaDer.get(3),x);
            return y.getPartialDerivative(1);
        }






    }


    private String number() {
        Random random = new Random();
        int number = random.nextInt(9) + 1;
        String numb = String.valueOf(number);
        return numb;
    }

    public double eval(final String str) {
        class Parser {
            int pos = -1, c;

            void eatChar() {
                c = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            void eatSpace() {
                while (Character.isWhitespace(c)) eatChar();
            }

            double parse() {
                eatChar();
                double v = parseExpression();
                if (c != -1) throw new RuntimeException("Unexpected: " + (char) c);
                return v;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor | term brackets
            // factor = brackets | number | factor `^` factor
            // brackets = `(` expression `)`

            double parseExpression() {
                double v = parseTerm();
                for (; ; ) {
                    eatSpace();
                    if (c == '+') { // addition
                        eatChar();
                        v += parseTerm();
                    } else if (c == '-') { // subtraction
                        eatChar();
                        v -= parseTerm();
                    } else {
                        return v;
                    }
                }
            }

            double parseTerm() {
                double v = parseFactor();
                for (; ; ) {
                    eatSpace();
                    if (c == '/') { // division
                        eatChar();
                        v /= parseFactor();
                    } else if (c == '*' || c == '(') { // multiplication
                        if (c == '*') eatChar();
                        v *= parseFactor();
                    } else {
                        return v;
                    }
                }
            }

            double parseFactor() {
                double v;
                boolean negate = false;
                eatSpace();
                if (c == '+' || c == '-') { // unary plus & minus
                    negate = c == '-';
                    eatChar();
                    eatSpace();
                }
                if (c == '(') { // brackets
                    eatChar();
                    v = parseExpression();
                    if (c == ')') eatChar();
                } else { // numbers
                    int startIndex = this.pos;
                    while ((c >= '0' && c <= '9') || c == '.') eatChar();
                    if (pos == startIndex) throw new RuntimeException("Unexpected: " + (char) c);
                    v = Double.parseDouble(str.substring(startIndex, pos));
                }

                eatSpace();
                if (c == '^') { // exponentiation
                    eatChar();
                    v = java.lang.Math.pow(v, parseFactor());
                }
                if (negate) v = -v; // unary minus is applied after exponentiation; e.g. -3^2=-9
                return v;
            }
        }
        return new Parser().parse();
    }
}
