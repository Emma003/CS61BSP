package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * TODO: TEST THIS
     *
     * Directory map:
     * .gitlet
     *      InitialCommit hashcode
     */
    public static void init() {
        // Failure case
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
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

        // Update HEAD commit
        Utils.writeContents(HEAD,branchName);
    }

    public static void checkoutBranch(String branch) {

    }



}
