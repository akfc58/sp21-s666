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
        switch(firstArg) {
            // TODO: If a user inputs a command with the wrong number or format of operands,
            //  print the message Incorrect operands. and exit.
            case "init":
                if (args.length != 1) {
                    System.out.println("Incorrect operands.");
                }
                Repository.init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                break;
            case "commit":

            case "rm":

            case "log":

            case "global-log":

            case "find":

            case "status":

            case "checkout":

            case "branch":

            case "rm-branch":

            case "reset":

            case "merge":

            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
            // TODO: FILL THE REST IN
        }
    }
}
