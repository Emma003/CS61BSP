package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        /** TODO: what if args is empty?
         *  TODO: user inputs command that doesn't exist
         *  TODO: wrong number or format of operands
         *  TODO: a gitlet dir hasn't been initialized
         */
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                Repository.init();
                break;
            case "add":
                Repository.add(args[1]);
                break;
            case "commit":
                if (args.length == 2) {
                    Repository.commit(args[1]);
                } else {
                    System.out.println("Please enter a commit message.");
                }
                break;
            case "rm":
                if (args.length == 2) {
                    Repository.rm(args[1]);
                } else {
                    System.out.println("Please write a file to remove");
                }
                break;
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
                Repository.status();
                break;
            case "checkout":
                if (args.length == 1) {
                    Repository.checkoutBranch(args[1]);
                } else if (args.length == 2) {
                    CommitTree.checkoutFile(args[2]);
                } else if (args.length == 4) {
                    CommitTree.checkoutCommitFile(args[1],args[3]);
                } else {
                    System.out.println("Please enter the right amount of arguments.");
                }
                break;
            case "branch":
                if (args.length == 2) {
                    Repository.newBranch(args[1]);
                } else {
                    System.out.println("Please enter branch name");
                }
                break;
            case "rm-branch":
                Repository.rmBranch(args[1]);
                break;
        }
    }
}
