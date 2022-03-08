package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** Subdirectory for commits */
    public static final File COMMITS = join(GITLET_DIR, "commits");
    /** Subdirectory for blobs */
    public static final File BLOBS = join(GITLET_DIR, "blobs");
    /** Subdirectory for branches */
    public static final File BRANCHES = join(GITLET_DIR, "branches");
    /** HEAD file */
    public static final File HEAD = join(GITLET_DIR, "HEAD.txt");
    /** master file */
    public static final File MASTER = join(BRANCHES, "master");


    /**
     *
     * Directory map:
     * .gitlet
     *      InitialCommit hashcode
     */
    public static void init() {
        // Failure case
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
        }

        // Making initial directories and files
        GITLET_DIR.mkdir(); COMMITS.mkdir(); BLOBS.mkdir(); BRANCHES.mkdir();

        // Initial commit file
        Commit initialCommit = new Commit();
        initialCommit.saveCommit();

        //HEAD file with path to current branch
        try {
            HEAD.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.writeContents(HEAD, "master");

        //MASTER file with initial commit id
        try {
            MASTER.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.writeContents(MASTER, initialCommit.hash());

        Stage setStage = new Stage();
        Stage.createIndex(setStage);
    }

    public static void switchBranch(String newBranch) {
        Utils.writeContents(HEAD, newBranch);
    }

    public static void add(String filename) {
        Stage index = Stage.returnIndex();
        index.add(filename);
        Stage.saveIndex(index);
    }

    public static void commit(String message) {
        Stage index = Stage.returnIndex();
        CommitTree.commit(index, message);
        Stage.saveIndex(index);
    }

    public static void rm(String filename) {
        Stage index = Stage.returnIndex();
        index.rm(filename);
        Stage.saveIndex(index);
    }

    public static void status() {
        Stage index = Stage.returnIndex();
        index.printStatus();
        Stage.saveIndex(index);
    }

    /** TODO: TEST THIS */
    public static void log() {
        String currentCommitID = CommitTree.currentCommit();
        CommitTree.log(currentCommitID);
    }

    /** TODO: TEST THIS */
    public static void globalLog() {
        List<String> cwdFiles = Utils.plainFilenamesIn(COMMITS);
        for (String commitID: cwdFiles) {
            Commit commit = Commit.returnCommit(commitID);
            System.out.println(commit.toString());
        }

    }

    /** TODO: TEST THIS */
    public static void find(String message) {
        // Adding all commit filenames into a list
        List<String> cwdFiles = Utils.plainFilenamesIn(COMMITS);
        boolean found = false;

        // Iterating over commits folder
        for (String commitID: cwdFiles) {
            Commit commit = Commit.returnCommit(commitID);
            if (commit.getMessage().equals(message)) {
                System.out.println(commit.hash());
                found = true;
            }
        }

        if (!found) {
            System.out.println("Found no commit with that message.");
        }
    }

    public static void newBranch(String branchName) {
        // Branch with name already exists [FAILURE CASE]
        List<String> branches = Utils.plainFilenamesIn(BRANCHES);
        if (branches.contains(branchName)) {
            System.out.println("A branch with that name already exists.");
            return;
        }

        //new BRANCH file
        File BRANCH = Utils.join(BRANCHES, branchName);
        try {
            BRANCH.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write in pointer to current commit
        String currentCommitID = CommitTree.currentCommit();
        Utils.writeContents(BRANCH, currentCommitID);
    }

    public static void rmBranch(String branchName) {
        // Removing non-existent branch [FAILURE CASE]
        List<String> branches = Utils.plainFilenamesIn(BRANCHES);
        if (!branches.contains(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }

        // Removing current branch [FAILURE CASE]
        String currentBranch = Utils.readContentsAsString(HEAD);
        if(currentBranch.equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
            return;
        }

        // Delete branch file from branch folder
        File removedBranch = Utils.join(BRANCHES, branchName);
        removedBranch.delete();

    }

    public static void checkoutBranch(String branch, Stage index) {
        // Checking out non-existent branch [FAILURE CASE]
        List<String> branches = Utils.plainFilenamesIn(BRANCHES);
        if (!branches.contains(branch)) {
            System.out.println("No such branch exists.");
            return;
        }

        // Checking out current branch [FAILURE CASE]
        String currentBranch = CommitTree.currentBranch();
        if(currentBranch.equals(branch)) {
            System.out.println("No need to checkout the current branch.");
            return;
        }

        // A CWD file is untracked and is about to be overwritten by checkout [FAILURE CASE]
        List<String> cwdFiles = Utils.plainFilenamesIn(CWD);
        List<String> untrackedFiles = index.getUntrackedFiles();
        for (String file: cwdFiles) {
            if (untrackedFiles.contains(file)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                return;
            }
        }

        // Get commit at the head of the given branch
        File branchFile = Utils.join(Repository.BRANCHES, branch);
        String headCommitID = Utils.readContentsAsString(branchFile);
        Commit headCommitAtBranch = Commit.returnCommit(headCommitID);

        // Put every tracked file in CWD
        Set<String> trackedFiles = headCommitAtBranch.getFiles().keySet();
        for(String file: trackedFiles) {
            headCommitAtBranch.putFileInCWD(file);
        }

        // Change HEAD pointer to checked out branch
        Utils.writeContents(Repository.HEAD,branch);

        // Delete previously tracked files that aren't tracked in the checked out branch
        List<String> previouslyTrackedFiles = index.getUntrackedFiles();
        for (String file: previouslyTrackedFiles) {
            File fileToDelete = Utils.join(CWD, file);
            fileToDelete.delete();
        }

        // Clear staging area
        index.clearStagingArea();
        index.untrackedFiles.clear();

    }



    /**
     * TODO: Include abbreviated commit IDs
     * */
    public static void reset(String commitID, Stage index) {
        // Non-existent commit [FAILURE CASE]
        List<String> commits = Utils.plainFilenamesIn(Repository.COMMITS);
        if (!commits.contains(commitID)) {
            System.out.println("No commit with that id exists.");
            return;
        }

        // A CWD file is untracked and is about to be overwritten by checkout [FAILURE CASE]
        List<String> cwdFiles = Utils.plainFilenamesIn(Repository.CWD);
        List<String> untrackedFiles = index.getUntrackedFiles();
        for (String file: cwdFiles) {
            if (untrackedFiles.contains(file)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                return;
            }
        }

        Commit commitAtGivenID = Commit.returnCommit(commitID);
        // Put every tracked file in CWD
        Set<String> trackedFiles = commitAtGivenID.getFiles().keySet();
        for(String file: trackedFiles) {
            commitAtGivenID.putFileInCWD(file);
        }

        // Change HEAD to given ID
        CommitTree.updateCurrentHead(commitID);

        // Delete previously tracked files that aren't tracked in the checked out branch
        List<String> previouslyTrackedFiles = index.getUntrackedFiles();
        for (String file: previouslyTrackedFiles) {
            File fileToDelete = Utils.join(CWD, file);
            fileToDelete.delete();
        }

        // Clear staging area
        index.clearStagingArea();
        index.untrackedFiles.clear();

    }

    /** Try and have this method do the bulk of checkout(branch) and reset */
    public static void rearrangeCWD(String commitID, Stage index) {

    }





}
