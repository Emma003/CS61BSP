package gitlet;

import java.io.File;
import java.util.List;
import java.util.TreeMap;

public class CommitTree {


    /**
     * @return string id of current HEAD commit
     */
    public static String currentCommit() {
        String currentBranch = Utils.readContentsAsString(Repository.HEAD);
        File branchFile = Utils.join(Repository.BRANCHES, currentBranch);
        return Utils.readContentsAsString(branchFile);
    }

    public static String currentBranch() {
        return Utils.readContentsAsString(Repository.HEAD);
    }

    public static void updateCurrentHead(String commitID) {
        String currentBranch = Utils.readContentsAsString(Repository.HEAD);
        File branchFile = Utils.join(Repository.BRANCHES, currentBranch);
        Utils.writeContents(branchFile, commitID);
    }

    /** TODO: TEST THIS (ADD MERGE CASE LATER)*/
    public static void log(String commitID) {
        if (commitID == null) {
            return;
        }
        Commit currentCommit = Commit.returnCommit(commitID);
        System.out.println(currentCommit.toString());
        log(currentCommit.getParent());
    }

    /**
     * TODO:Read HEAD commit object and staging area
     *
     * TODO:Clone HEAD commit
     * TODO:Modify its message and timestamp
     * TODO:Use the staging area to modify the files tracked by the new commit
     * TODO:Untrack files that have been staged for removal by the rm command
     *
     * TODO:Write back any new or modified object made into a new file
     * TODO:Clear staging area
     * TODO:Move HEAD pointer to point to current commit
     * TODO: FAIL: no staged files, no commit message
     */
    public static void commit(Stage index, String message) {
        // No staged files [FAILURE CASE]
        if (index.additionStage.isEmpty() && index.removalStage.isEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        }

        // Cloning, updating and saving new commit
        String currentCommitID = CommitTree.currentCommit();
        Commit newCommit = new Commit(message, currentCommitID, false);
        newCommit.updateCommitFiles(index.additionStage, index.removalStage); // Updating tracked files for new commit
        newCommit.saveCommit();

        // Clear staging area
        index.clearStagingArea();

        // Update current pointer
        String currentBranch = Utils.readContentsAsString(Repository.HEAD);
        File branchFile = Utils.join(Repository.BRANCHES, currentBranch);
        Utils.writeContents(branchFile,newCommit.hash());

    }



    public static void checkoutFile(String filename) {
        // Deserialize current commit
        String currentCommitID = CommitTree.currentCommit();
        Commit currentCommit = Commit.returnCommit(currentCommitID);
        currentCommit.putFileInCWD(filename);
    }

    /** TODO: Include abbreviated commit IDs */
    public static void checkoutCommitFile(String commitID, String filename) {
        List<String> commits = Utils.plainFilenamesIn(Repository.COMMITS);
        if (commits.contains(commitID)) {
            Commit checkoutCommit = Commit.returnCommit(commitID);
            checkoutCommit.putFileInCWD(filename);
        } else {
            System.out.println("No commit with that id exists.");
        }
    }


    public void findSplit() {
        /**
         * [HELPER METHOD]
         * Returns ID of the split point.
         *
         * TODO: starting from the head, go down the history of commits for one branch (add it to an ordered list or something) then for the
         * other branch, go down until you find a commit that was in the list of commits of the other branch
         *
         * TODO: think of a way to deal with finding a split point when there was a merge commit in the commit history (occurs when a commit node
         * has 2 parents)
         *
         * TODO: watch graph traversal videos / look up LCA algorithms in java
         */
    }


}
