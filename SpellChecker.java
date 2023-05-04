import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.List;
import java.util.Enumeration;
import java.util.HashSet;

public class SpellChecker implements SpellCheckerInterface{

    File inputFile;
    Scanner inputScanner;
    Hashtable<Integer, String> dictionaryHashTable = new Hashtable<Integer, String>(); 
    
    public SpellChecker(String filename){
        fillHashTableFromDictionary(filename);
    }

    public SpellChecker(){
        System.err.println("You must input a file name in the constructor.");
        throw new RuntimeException();
    }

    private void setFile(String fileNameToSet){
        inputFile = new File(fileNameToSet);

        try{
            inputScanner = new Scanner(inputFile);
        }
        catch(FileNotFoundException excp){
            System.err.printf("The file (%s) you tried to check does not exist.\n", fileNameToSet);
            throw new RuntimeException();
        }
    }

    private void fillHashTableFromDictionary(String dictionaryFile){

        File inputDictionaryFile;
        Scanner inputDictionaryScanner;
        
        try{
            inputDictionaryFile = new File(dictionaryFile);
            inputDictionaryScanner = new Scanner(inputDictionaryFile);
        }

        catch(FileNotFoundException excp){
            System.err.println("That dictionary file does not exist.");
            throw new RuntimeException();
        }

        while(inputDictionaryScanner.hasNextLine()){
            String line = inputDictionaryScanner.nextLine();
            String[] lineSplit = line.replaceAll("\\p{Punct}", "").toLowerCase().split(" ");
            for(String word : lineSplit){
                dictionaryHashTable.put(word.hashCode(), word);
            }
        }


    }
    
    public List<String> getIncorrectWords(String filename){
        
        setFile(filename);
        
        ArrayList<String> listOfIncorrect = new ArrayList<>();
        
        while(inputScanner.hasNextLine()){
            String line = inputScanner.nextLine();
            String[] lineSplit = line.replaceAll("\\p{Punct}", "").toLowerCase().split(" ");
            WordForLoop: for(String word : lineSplit){
                try{
                    if(!Character.isLetterOrDigit(word.charAt(0))){
                        continue WordForLoop;
                    }
                }
                catch(StringIndexOutOfBoundsException excp){
                    continue WordForLoop;
                }
                if(dictionaryHashTable.containsValue(word)){
                    ;
                }
                else{
                    listOfIncorrect.add(word);
                }
            }
        }
        
        return listOfIncorrect;
    }

    public Set<String> getSuggestions(String word){
        
        Set<String> suggestedWords = new HashSet<String>();

        word.toLowerCase();

        if(dictionaryHashTable.containsValue(word)){
            return suggestedWords;
        }
        
        ArrayList<String> addCharacterWords = addOneCharacter(word);
        ArrayList<String> removeCharacterWords = removeOneCharacter(word);
        ArrayList<String> swappedCharacterWords = swapAdjacentCharacters(word);

        for(String item : addCharacterWords){
            if(dictionaryHashTable.containsValue(item)){
                suggestedWords.add(item);
            }
        }

        for(String item : removeCharacterWords){
            if(dictionaryHashTable.containsValue(item)){
                suggestedWords.add(item);
            }
        }

        for(String item : swappedCharacterWords){
            if(dictionaryHashTable.containsValue(item)){
                suggestedWords.add(item);
            }
        }

        return suggestedWords;

    }

    private ArrayList<String> addOneCharacter(String word){
        
        StringBuilder modifyingString = new StringBuilder(word);
        ArrayList<String> returningAL = new ArrayList<String>();

        for(int i = 0; i <= word.length(); i++){

            for(int j = 97; j <= 122; j++){
                modifyingString.insert(i, (char) j);
                returningAL.add(modifyingString.toString());
                modifyingString.deleteCharAt(i);

            }

        }

        return returningAL;
    }

    private ArrayList<String> removeOneCharacter(String word){
        StringBuilder modifyingString = new StringBuilder(word);
        ArrayList<String> returningAL = new ArrayList<String>();

        if(word.length() <= 1){
            return returningAL;
        }
        
        for(int i = 0; i < word.length(); i++){
            Character addBack = modifyingString.charAt(i);
            modifyingString.deleteCharAt(i);
            returningAL.add(modifyingString.toString());
            modifyingString.insert(i, addBack);
        }

        return returningAL;
    }

    private ArrayList<String> swapAdjacentCharacters(String word){
        StringBuilder modifyingString = new StringBuilder(word);
        ArrayList<String> returningAL = new ArrayList<String>();
        
        if(word.length() <= 1){
            return returningAL;
        }

        for(int i = 0; i < word.length() - 1; i++){
            Character firstChar = word.charAt(i);
            Character secondChar = word.charAt(i+1);

            modifyingString.setCharAt(i, secondChar);
            modifyingString.setCharAt(i+1, firstChar);
            returningAL.add(modifyingString.toString());
            modifyingString.setCharAt(i, firstChar);
            modifyingString.setCharAt(i+1, secondChar);
        }

        return returningAL;

    }

}