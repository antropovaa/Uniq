import java.io.FileNotFoundException;

/**
 * Additional class, that parses arguments and flags from command line.
 */

class UniqParser {
    private String inputName = "";
    private String outputName = "";
    private boolean caseInsensitive = false;
    private int ignoreCharsUntil = 0;
    private boolean uniqOnly = false;
    private boolean countCopies = false;
    private boolean toFile = false;
    private boolean fromFile = false;

    UniqParser(String[] args) {
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

    String getOutputName() throws FileNotFoundException {
        if (!outputName.matches("^*.txt$") && !toFile)
            throw new FileNotFoundException("Incorrect file format. Enter *.txt file.");
        return outputName;
    }

    boolean isFromFile() {
        return fromFile;
    }

    String getInputName() throws FileNotFoundException {
        if (!inputName.matches("^*.txt$") && !fromFile)
            throw new FileNotFoundException("Incorrect file format. Enter *.txt file.");
        return inputName;
    }
}