import java.io.*;
import java.util.*;

/**
 * Utility for command line, that filters out adjacent, matching lines from input file,
 * writing the filtered data to output file.
 */
public class Uniq {
    public static void main(String[] args) throws IOException {
        UniqParser pars = new UniqParser(args);
        List<String> result = inputAndProcess(pars);
        output(pars, result);
    }

    /**
     * Analyzes and prepares input data from file or console and perform actions on strings.
     *
     * @param pars The result of parsing arguments.
     * @return Array of edited strings.
     * @throws FileNotFoundException if input was aborted.
     */
    public static List<String> inputAndProcess(UniqParser pars) throws FileNotFoundException {
        Scanner input;
        if (pars.isFromFile())
            input = new Scanner(new FileReader(new File(pars.getInputName())));
        else
            input = new Scanner(new InputStreamReader(System.in));
        List<Pair<Integer, String>> result = comparison(input, pars);
        return makeFinalList(pars, result);
    }

    /**
     * Compare strings depending on flags.
     *
     * @param input Scanner of input data.
     * @param pars The result of parsing arguments.
     * @return List of pairs of strings and number of it's duplicates.
     */
    public static List<Pair<Integer, String>> comparison(Scanner input, UniqParser pars) {
        List<Pair<Integer, String>> result = new ArrayList<>();
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
        return result;
    }

    /**
     * Combines all pairs into a list of strings based on flags.
     *
     * @param pars   The result of parsing arguments.
     * @param result List of pairs with strings and number of their matches.
     * @return Final list of strings whose content depends on flags.
     */
    public static List<String> makeFinalList(UniqParser pars, List<Pair<Integer, String>> result) {
        List<String> finalList = new ArrayList<>();

        for (Pair<Integer, String> pair : result)
            if (pars.isUniqOnly()) {
                if (pair.getMatches() == 1)
                    finalList.add(pair.getString());
            } else {
                if (pars.isCountCopies()) {
                    if (pair.getMatches() != 1)
                        finalList.add(pair.toString());
                    else
                        finalList.add(pair.getString());
                } else
                    finalList.add(pair.getString());
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
    public static void output(UniqParser pars, List<String> finalList) throws IOException {
        PrintWriter output;
        if (pars.isToFile())
            output = new PrintWriter(pars.getOutputName());
        else
            output = new PrintWriter(System.out);
        for (String string : finalList)
            output.println(string);
        output.flush();
        output.close();
    }
}