package gitlet;

import java.util.List;

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

        // User input is blank
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }

        // Gitlet initialization
        if (args[0].equals("init")) {
            Repository.init();
            System.exit(0);
        }

        // Gitlet hasn't been initialized -> abort
        if (!Repository.GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }

        // Unload index file that represents the staging area
        Stage index = Stage.returnIndex();

        // Function calls
        String firstArg = args[0];
        switch(firstArg) {
            case "add":
                validateNumArgs(args,2);
                index.add(args[1]);
                break;
            case "commit":
                validateNumArgs(args,2);
                if (args[1].equals("")) {
                    System.out.println("Please enter a commit message.");
                }
                CommitTree.commit(index, args[1], false, null);
                break;
            case "rm":
                validateNumArgs(args,2);
                index.rm(args[1]);
                break;
            case "log":
                validateNumArgs(args,1);
                String currentCommitID = CommitTree.currentCommit();
                CommitTree.log(currentCommitID);
                break;
            case "global-log":
                validateNumArgs(args,1);
                Repository.globalLog();
                break;
            case "find":
                validateNumArgs(args,2);
                Repository.find(args[1]);
                break;
            case "status":
                validateNumArgs(args,1);
                index.printStatus();
                break;
            case "checkout":
                if (args.length == 2) {
                    Repository.checkoutBranch(args[1], index);
                } else if (args.length == 3 && args[1].equals("--")) {
                    CommitTree.checkoutFile(args[2]);
                } else if (args.length == 4 && args[2].equals("--")) {
                    CommitTree.checkoutCommitFile(args[1],args[3]);
                }
                else {
                    System.out.println("Incorrect operands.");
                }
                break;
            case "branch":
                validateNumArgs(args,2);
                Repository.newBranch(args[1]);
                break;
            case "rm-branch":
                validateNumArgs(args,2);
                Repository.rmBranch(args[1]);
                break;
            case "reset":
                validateNumArgs(args,2);
                Repository.reset(args[1], index);
                break;
            case "merge":
                validateNumArgs(args, 2);
                CommitTree.merge(args[1], index);
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);

        }

        // Serialize the modified staging area
        Stage.saveIndex(index);
    }


    public static void validateNumArgs(String[] args, int n) {
        if (args.length != n) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }


}
