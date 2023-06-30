package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author dongliang
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "init":
                validateNumArgs(args, 1);
                Repository.init();
                break;
            case "add":
                validateNumArgs(args, 2);
                Repository.add(args[1]);
                break;
            case "commit":
                validateNumArgs(args, 2);
                Repository.commit(args[1]);
                break;
            case "rm":

            case "log":
                Repository.log();
                break;
            case "global-log":
                Repository.globalLog();
                break;
            case "find":
                Repository.find(args[1]);
                break;
            case "status":

            case "checkout":
                if (args.length == 3) { // use equals, not == !!!
                    Repository.checkoutFile(args[2]);
                }
                if (args.length == 4) {
                    Repository.checkoutCommitFile(args[1], args[3]); //TODO: do not move HEAD. See specs.
                }
                if (args.length == 2) {
                    // TODO: add arg[1] in branch list.
                }
                break;
            case "branch":

            case "rm-branch":

            case "reset":

            case "merge":

            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }

    /** validate the command arguments. */
    public static void validateNumArgs(String[] args, int n) {
        if (args.length != n) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }
}

