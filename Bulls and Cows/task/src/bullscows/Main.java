package bullscows;

import java.util.*;

import static java.lang.Character.getNumericValue;

public class Main {
    static Map<Integer,Integer> secretMap = new LinkedHashMap<>() , answerMap = new LinkedHashMap<>();
    static Map<Integer ,String> numericValueOfAlphCharacters = new LinkedHashMap<>();
    static String secretString, answerString, bullAndCowsString;
    static Scanner sc = new Scanner(System.in);
    static int possibleSymbolsTill,secretSize, turn = 1;
    static boolean end = false;

    public static void main(String[] args) {
        initializeGame();
    }

    public static void initializeGame(){
        valueOfAlphInIntegers();
        inputLengthSecretCode();
        defineNumberOfPossibleSymbols();
        generateSecret();
        while (!end){
            System.out.println("Turn " + turn + ":");
            answerInput();
            compareSecretAndAnswer();
        }
    }

    public static void valueOfAlphInIntegers(){
        if(numericValueOfAlphCharacters.isEmpty()) {
            String alph = "abcdefghijklmnopqrstuvwxyz";
            for (int j = 0; j < alph.length(); j++) {
                Integer numericValueOf = getNumericValue(alph.charAt(j));
                numericValueOfAlphCharacters.put(numericValueOf, String.valueOf(alph.charAt(j)));
            }
        }
    }

    public static void inputLengthSecretCode(){
        System.out.println("Input the length of the secret code:");
        do{
            String secretString = sc.nextLine();
            try {
                secretSize = Integer.parseInt(secretString);
                if (secretSize > 36 || secretSize < 1)
                    throw new NumberFormatException();
            } catch (NumberFormatException e){
                if(secretSize > 36 || secretSize < 1){
                    System.out.println("error : can't generate a secret number with a length of " + secretSize
                            + " because there aren't enough unique digits.");
                } else
                    System.out.println("Error: \"" + secretString + "\" isn't a valid number.");
                exitGame();
            }
        } while (secretSize > 36 || secretSize < 1);
    }

    public static void defineNumberOfPossibleSymbols(){
        System.out.println("Input the number of possible symbols in the code:");
        do{
            String possibleSymbolsFillString = sc.nextLine();
            try{
                possibleSymbolsTill = Integer.parseInt(possibleSymbolsFillString);
                if (possibleSymbolsTill > 36)
                    throw new NumberFormatException();
                else if (possibleSymbolsTill < secretSize)
                    throw new NumberFormatException();
            } catch (NumberFormatException e){
                if(possibleSymbolsTill < secretSize) {
                    System.out.println("error: it's not possible to generate a code with a length of " + secretSize
                            + " with " + possibleSymbolsTill + " unique symbols.");
                } else if (possibleSymbolsTill > 36)
                        System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
                else
                    System.out.println("Error: \"" + possibleSymbolsFillString + "\" isn't a valid number.");
                exitGame();
            }
        } while (possibleSymbolsTill > 36);
    }

    public static void generateSecret(){
        int i = 0, min = 0, max = possibleSymbolsTill;
        do{
            int secretInt = (int) (min + (Math.random() * ((max - min) + 1)));
            if (!secretMap.containsValue(secretInt)) {
                secretMap.put(i, secretInt);
                i++;
            }
        } while(secretMap.size() < secretSize);
        validateSecret();
        System.out.println("The secret is prepared: " + "*".repeat(secretSize) + " (0-9, a-"+ numericValueOfAlphCharacters.get(possibleSymbolsTill - 1) +").");
        System.out.println("Okay, let's start a game!");
    }

    public static void validateSecret(){
        if (secretMap.get(0) == 0) {
            int secretInt = (int) (1 + (Math.random() * ((9 - 1) + 1)));
            secretMap.put(0, secretInt);
        }
    }

    public static void dealWithAnswer(String answer){
        int answerInt;
        for (int i = 0; i < answer.length(); i++) {
            try{
                answerInt = Integer.parseInt(String.valueOf(answer.charAt(i)));
            } catch (NumberFormatException e){
                answerInt = getNumericValue(answer.charAt(i));
            }
            answerMap.put(i, answerInt);
        }
    }

    public static void answerInput(){
        String answer;
        do {
            answer = sc.nextLine();
            if (answer.length() == secretSize)
                dealWithAnswer(answer);
        } while(answer.length() != secretSize);
        turn++;
    }

    public static void compareSecretAndAnswer(){
        secretString = generateStringOfMap(secretMap);
        answerString = generateStringOfMap(answerMap);
        if(answerString.equals(secretString)){
            end = !end;
            System.out.println("Grade: " + secretSize + " bulls.");
            System.out.println("Congratulations! You guessed the secret code.");
        } else {
            bullsAndCows();
        }
    }

    public static String getPositionChar(int i){
        return String.valueOf(numericValueOfAlphCharacters.get(i));
    }

    public static String generateStringOfMap(Map<Integer,Integer> map){
        String str = "";
        String character;
        for (int i = 0; i < map.size(); i++) {
            if(map.get(i) > 9)
                character = getPositionChar(map.get(i));
            else
                character = String.valueOf(map.get(i));
            str = str.concat(character);
        }
        return str;
    }

    public static void bullsAndCows() {
        int bulls = 0;
        int cows = 0;
        for (int i = 0; i < secretString.length(); i++) {
            if (answerMap.get(i).equals(secretMap.get(i)))
                bulls++;
            else if (answerMap.containsValue(secretMap.get(i)) && !answerMap.get(i).equals(secretMap.get(i)))
                cows++;
        }

        if(bulls == 0 && cows == 0)
            bullAndCowsString = "None";
        else if(bulls != 0 && cows != 0)
            bullAndCowsString = "Grade: " + bulls + " bull(s) and " + cows + " cow(s).";
        else if (bulls != 0 && cows == 0)
            bullAndCowsString = "Grade: " + bulls + " bull(s).";
        else if (bulls == 0 && cows != 0)
            bullAndCowsString = "Grade: " + cows + " cow(s).";

        System.out.println(bullAndCowsString);
    }

    public static void exitGame(){
        System.exit(0);
    }
}
