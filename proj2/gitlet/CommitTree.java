package gitlet;

import java.io.File;

public class CommitTree {


    /**
     * @return string id of current HEAD commit
     */
    public static String currentCommit() {
        String currentBranch = Utils.readContentsAsString(Repository.HEAD);
        File branchFile = Utils.join(Repository.BRANCHES, currentBranch);
        return Utils.readContentsAsString(branchFile);
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



    public void findSplit() {
        /**
         * MERGE HELPER
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
