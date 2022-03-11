package gitlet;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class CommitTree {
    /**
     * @return string id of current HEAD commit
     */
    public static String currentCommit() {
        File branchFile = Utils.join(Repository.BRANCHES, currentBranch());
        return Utils.readContentsAsString(branchFile);
    }

    /** Returns the name of the current branch */
    public static String currentBranch() {
        return Utils.readContentsAsString(Repository.HEAD);
    }

    /** Updates the current branch file with the id of the current commit */
    public static void updateCurrentHead(String commitID) {
        String currentBranch = Utils.readContentsAsString(Repository.HEAD);
        File branchFile = Utils.join(Repository.BRANCHES, currentBranch);
        Utils.writeContents(branchFile, commitID);
    }

    /** Returns the ID of the commit at the head of the other branch */
    public static String otherCommit (String branchName) {
         File otherBranchHeadID = Utils.join(Repository.BRANCHES, branchName);
         return Utils.readContentsAsString(otherBranchHeadID);
    }

    public static void log(String commitID) {
        if (commitID == null) {
            return;
        }
        Commit currentCommit = Commit.returnCommit(commitID);
        System.out.println(currentCommit.toString());
        log(currentCommit.getParent());
    }

    public static void commit(Stage index, String message, boolean isMergeCommit, String otherBranch) {
        // No staged files [FAILURE CASE]
        if (index.additionStage.isEmpty() && index.removalStage.isEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        }

        Commit newCommit = null;
        // Cloning, updating and saving new commit
        String currentCommitID = CommitTree.currentCommit();
        if (isMergeCommit) {
            String otherBranchHeadID = CommitTree.otherCommit(otherBranch);
            newCommit = new Commit(message, currentCommitID, otherBranchHeadID, isMergeCommit);
        } else {
            newCommit = new Commit(message, currentCommitID, isMergeCommit);
        }

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
        // ID is shorter than 40 chars -> get full version
        if (commitID.length() < 40) {
            commitID = Commit.getFullID(commitID);
        }
        // Commit exists
        List<String> commits = Utils.plainFilenamesIn(Repository.COMMITS);
        if (commits.contains(commitID)) {
            Commit checkoutCommit = Commit.returnCommit(commitID);
            checkoutCommit.putFileInCWD(filename);
        } else {
            System.out.println("No commit with that id exists.");
        }
    }



    public static void merge (String branch, Stage index) {
        // Uncommitted additions/removals [FAILURE CASE]
        if (!(index.additionStage.isEmpty()) || !(index.removalStage.isEmpty())) {
            System.out.println("You have uncommitted changes.");
            return;
        }

        // Branch doesn't exist [FAILURE CASE]
        List<String> branches = Utils.plainFilenamesIn(Repository.BRANCHES);
        if (!(branches.contains(branch))) {
            System.out.println("A branch with that name does not exist.");
            return;
        }

        // Given branch is current branch [FAILURE CASE]
        if (branch.equals(currentBranch())) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }

        // Getting the files for the split, current branch, and given branch head commits
        Commit currentHeadCommit = Commit.returnCommit(currentCommit());
        Map <String,String> currentFiles = currentHeadCommit.getFiles();

        Commit otherHeadCommit = Commit.returnCommit(otherCommit(branch));
        Map <String,String> otherFiles = otherHeadCommit.getFiles();

        Commit splitPoint = Commit.returnCommit(findSplit(currentHeadCommit, otherHeadCommit));
        Map <String,String> splitPointFiles = splitPoint.getFiles();

        // Untracked file in the current commit [FAILURE CASE]
        List <String> currentUntrackedFiles = index.getUntrackedFiles();
        for (String file: currentUntrackedFiles) {
            // && !(otherFiles.containsKey(file))
            if (!(currentUntrackedFiles.isEmpty())) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                index.untrackedFiles.clear();
                return;
            }
        }


        // Split point is the same commit as given branch [FAILURE CASE]
        if (splitPoint.equals(otherHeadCommit)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        }

        // Split point is the same commit as current branch [FAILURE CASE]
        if (splitPoint.equals(currentHeadCommit)) {
            Repository.checkoutBranch(branch, index);
            System.out.println("Current branch fast-forwarded.");
            return;
        }

        /** MERGE CASES ~~~~~~ CURRENT -> master, GIVEN -> other
         *
         *  --[1] master: same | given: modified | present @ split
         *      -> check out + stage
         *
         *  -[2] master: modified | given: same | present @ split
         *      -> file stays as is
         *
         *  -[3] master: modified in same way | given: modified in same way [same content or both deleted] | present @ split
         *  ** READ THE SPEC FOR THIS ONE AGAIN, KINDA CONFUSING
         *      -> file stays as is
         *
         *  -[4] master: created | given: absent | absent @ split
         *      -> file stays as is
         *
         *  --[5] master: absent | given: created | absent @ split
         *      -> check out + stage
         *
         *  --[6] master: same | given: absent | present @ split
         *      -> file is removed and untracked
         *
         *  -[7] master: absent | given: same | present @ split
         *      -> remain absent
         *
         *  [8] master & given: modified in != ways | present/absent @ split
         *      -> CONFLICT: create new merged file and stage
         *
         *  AFTER ALL NECESSARY CHANGES HAVE BEEN MADE, DO A MERGE COMMIT
         *      -> print "Merged [] into []."
         *      -> if there was a merge conflict, print "Encountered.."
         *
         */

        boolean conflictHappened = false;

        // Iterating over current branch files
        for (Map.Entry<String, String> entry : currentFiles.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            boolean mergeCaseHappened = false;


            /**
             * Conflict situations:
             *
             *              I       II      III      IV       V
             *
             * SPLIT    abs A |  abs B |  C    |   D     |   E
             *
             * MASTER   A     |  !B    |  !C   |   !D    |   abs E
             *
             * BRANCH   !A    |  B     |  !!C  |   abs D |  !E
             *
             *
             */

            if(splitPointFiles.containsKey(key)) {
                if (splitPointFiles.containsValue(value)) {

                    //IMPLEMENT CASE 1 AND 6
                    if (otherFiles.containsKey(key)) {
                        if (!(otherFiles.containsValue(value))) {
                            noConflictmergeCase(branch, index, key, 1);
                            mergeCaseHappened = true;
                            //System.out.println("merge case 1");
                        }
                    } else {
                        noConflictmergeCase(branch, index, key, 6);
                        mergeCaseHappened = true;
                        //System.out.println("merge case 6");
                    }
                } else {
                    if (otherFiles.containsKey(key)) {
                        if (!(otherFiles.containsValue(value)) && !(otherFiles.get(key).equals(splitPointFiles.get(key)))) {
                            createConflictFile(index, branch, key, 3); // CONFLICT case III
                            mergeCaseHappened = true;
                            conflictHappened = true;
                            //System.out.println("merge case 8 [CONFLICT 3]");
                        }
                    } else {
                        createConflictFile(index, branch, key, 4); // CONFLICT case IV
                        mergeCaseHappened = true;
                        conflictHappened = true;
                        //System.out.println("merge case 8 [CONFLICT 4]");
                    }
                }
            } else {
                if (otherFiles.containsKey(key)) {
                    if (!(otherFiles.containsValue(value))) {
                        createConflictFile(index, branch, key, 1); // CONFLICT case I and II
                        mergeCaseHappened = true;
                        conflictHappened = true;
                        //System.out.println("merge case 8 [CONFLICT 1]");
                    }
                }
            }

        }

        // Iterating over other branch files
        for (Map.Entry<String, String> entry : otherFiles.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            boolean  mergeCaseHappened = false;

            /**
             *
             * [5] master: absent | given: created | absent @ split
             *      -> check out + stage
             *
             */

            if (splitPointFiles.containsKey(key)) {
                if (!splitPointFiles.containsValue(value)) {
                    if (!currentFiles.containsKey(key)) {
                        createConflictFile(index, branch, key,5); // CONFLICT case 5
                        mergeCaseHappened = true;
                        conflictHappened = true;
                        //System.out.println("merge case 8 [CONFLICT 5]");
                    }
                }
            } else {
                if (!currentFiles.containsKey(key)) {
                    noConflictmergeCase(branch, index, key,5);
                    mergeCaseHappened = true;
                    //System.out.println("merge case 5");
                }
            }

        }

        if (conflictHappened) {
            System.out.println("Encountered a merge conflict.");
        }

        commit(index, "Merged " + branch + " into " + currentBranch() + ".", true, branch);
    }

    public static void noConflictmergeCase (String other, Stage index, String filename, int mergeCase) {
        switch (mergeCase) {
            case 1,5:
                checkoutCommitFile(otherCommit(other), filename);
                index.add(filename);
                return;
            case 6:
                index.rm(filename);
                return;
        }
    }

    public static void createConflictFile(Stage index, String other, String filename, int conflictCase) {
        // Create new conflict file
        File CONFLICT_FILE = Utils.join(Repository.CWD, filename);
        try {
            CONFLICT_FILE.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get master and other branch files
        Commit currentHeadCommit = Commit.returnCommit(currentCommit());
        Map <String,String> currentFiles = currentHeadCommit.getFiles();

        Commit otherHeadCommit = Commit.returnCommit(otherCommit(other));
        Map <String,String> otherFiles = otherHeadCommit.getFiles();

        // Initialize the two different file contents that will be written into the new merge file
        String currentBlobContent = "";
        String otherBlobContent = "";
        String newFileContents = "";

        switch(conflictCase) {
            case 1,2,3:
                currentBlobContent = Blob.returnBlobContent(currentFiles.get(filename));
                otherBlobContent = Blob.returnBlobContent(otherFiles.get(filename));
                newFileContents = "<<<<<<< HEAD\n" + currentBlobContent + "\n=======\n" + otherBlobContent + ">>>>>>>" ;
                break;
            case 4:
                currentBlobContent = Blob.returnBlobContent(currentFiles.get(filename));
                otherBlobContent = "";
                newFileContents = "<<<<<<< HEAD\n" + currentBlobContent + "\n=======\n>>>>>>>" ;
                break;
            case 5:
                currentBlobContent = "";
                otherBlobContent = Blob.returnBlobContent(otherFiles.get(filename));
                newFileContents = "<<<<<<< HEAD\n=======\n" + otherBlobContent + "\n>>>>>>>" ;
                break;
        }

        Utils.writeContents(CONFLICT_FILE, newFileContents);
        index.add(filename);
    }

    /**
     * [HELPER METHOD]
     * Returns ID of the split point.
     *
     * TODO: think of a way to deal with finding a split point when there was a merge commit in the commit history (occurs when a commit node
     * has 2 parents)
     */
    public static String findSplit(Commit currentBranchHead, Commit otherBranchHead) {
        Commit currentPointer = currentBranchHead;
        List<String> commitHistory = new ArrayList<>();
        // Add latest commit
        commitHistory.add(currentPointer.hash());

        // Add the current branch's history into a commit
        while(currentPointer.getParent() != null) {
            commitHistory.add(currentPointer.getParent());
            currentPointer = Commit.returnCommit(currentPointer.getParent());
        }

        // Add very first commit
        commitHistory.add(currentPointer.hash());

        Commit splitCommit = closestCommonAncestor(commitHistory, otherBranchHead);
        return splitCommit.hash();
    }

    /**
     * [HELPER METHOD]
     * Recursive helper function that goes through branch's commit parent to find the latest common ancestor */
    public static Commit closestCommonAncestor (List<String> history, Commit current) {
        if (history.contains(current.hash())) {
            return current;
        }
        return Commit.returnCommit(current.getParent());
    }


}
