import java.io.*;
import java.util.*;

/**
 * Utility for command line, that filters out adjacent, matching lines from input file INPUT,
 * writing the filtered data to output file OUTPUT.
 */
public class Uniq {
    public static void main(String[] args) throws IOException {
        Parser pars = new Parser(args);
        ArrayList<String> result = inputAndProcess(pars);
        output(pars, result);
    }

    /**
     * Additional class, that parse arguments and flags from command line.
     */
    private static class Parser {
        private String inputName = "";
        private String outputName = "";
        private boolean caseInsensitive = false;
        private int ignoreCharsUntil = 0;
        private boolean uniqOnly = false;
        private boolean countCopies = false;
        private boolean toFile = false;
        private boolean fromFile = false;

        Parser(String[] args) {
            for (int i = 0; i < args.length; i++)
                switch (args[i]) {
                    case "-i":
                        caseInsensitive = true;
                        break;
                    case "-s":
                        ignoreCharsUntil = Integer.valueOf(args[++i]);
                        break;
                    case "-u":
                        uniqOnly = true;
                        break;
                    case "-c":
                        countCopies = true;
                        break;
                    case "-o":
                        outputName = args[++i];
                        toFile = true;
                        break;
                    default:
                        inputName = args[i];
                        if (!inputName.equals(""))
                            fromFile = true;
                }
        }

        boolean isCaseInsensitive() {
            return caseInsensitive;
        }

        int numberOfIgnoringChars() {
            return ignoreCharsUntil;
        }

        boolean isUniqOnly() {
            return uniqOnly;
        }

        boolean isCountCopies() {
            return countCopies;
        }

        boolean isToFile() {
            return toFile;
        }

        String getOutputName() {
            return outputName;
        }

        boolean isFromFile() {
            return fromFile;
        }

        String getInputName() {
            return inputName;
        }
    }

    /**
     * Analyzes and prepares input data from file or console and perform actions on strings.
     *
     * @param pars The result of parsing arguments.
     * @return Array of edited strings.
     * @throws FileNotFoundException if input was aborted.
     */
    private static ArrayList<String> inputAndProcess(Parser pars) throws IOException {
        Scanner input;

        if (pars.isFromFile()) {
            input = new Scanner(new FileReader(new File(pars.getInputName())));
        } else {
            input = new Scanner(new InputStreamReader(System.in));
        }

        ArrayList<Pair<Integer, String>> result = new ArrayList<>();
        while (input.hasNext()) {
            String thisString = input.nextLine();
            if (result.isEmpty())
                result.add(new Pair<>(1, thisString));
            else {
                Pair<Integer, String> pair = result.get(result.size() - 1);
                String string1 = pair.getString();
                String string2 = thisString;

                if (pars.isCaseInsensitive() && string1.length() == string2.length()) {
                    string1 = string1.toLowerCase();
                    string2 = string2.toLowerCase();
                }

                if (pars.numberOfIgnoringChars() != 0 && string1.length() == string2.length()) {
                    int num = pars.numberOfIgnoringChars();
                    string1 = string1.substring(num);
                    string2 = string2.substring(num);
                }

                if (string1.equals(string2))
                    pair.setMatches(pair.getMatches() + 1);
                else
                    result.add(new Pair<>(1, thisString));
            }
        }
        return makeFinalList(pars, result);
    }


    /**
     * Combines all pairs into a list of strings based on flags.
     *
     * @param pars The result of parsing arguments.
     * @param result List of pairs with strings and number of their matches.
     * @return Final list of strings whose content depends on flags.
     */
    private static ArrayList<String> makeFinalList(Parser pars, ArrayList<Pair<Integer, String>> result) {
        ArrayList<String> finalList = new ArrayList<>();

        for (Pair<Integer, String> pair : result) {
            if (pars.isUniqOnly()) {
                if (pair.getMatches() == 1)
                    finalList.add(pair.getString());
            } else {
                if (pars.isCountCopies()) {
                    if (pair.getMatches() != 1)
                        finalList.add(pair.toString());
                    else
                        finalList.add(pair.getString());
                }
                else {
                    finalList.add(pair.getString());
                }
            }
        }
        return finalList;
    }

    /**
     * Collects together and displays to file or console output data.
     *
     * @param pars The result of parsing arguments.
     * @param finalList Array of edited strings.
     * @throws IOException In case output was aborted.
     */
    private static void output(Parser pars, ArrayList<String> finalList) throws IOException {
        if (pars.isToFile()) {
            File outputFile = new File(pars.getOutputName());
            FileWriter output = new FileWriter(outputFile);
            for (String string : finalList) {
                output.write(string);
                output.write("\n");
            }
            output.flush();
            output.close();
        } else
            for (String string : finalList)
                System.out.println(string);
    }
}