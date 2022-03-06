package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** Subdirectory for objects (commits and blobs) */
    public static final File OBJECTS = join(GITLET_DIR, "objects");
    /** Subdirectory for branches */
    public static final File BRANCHES = join(GITLET_DIR, "branches");
    /** HEAD file */
    public static final File HEAD = join(GITLET_DIR, "HEAD.txt");
    /** master file */
    public static final File MASTER = join(BRANCHES, "master.txt");


    /**
     * INIT COMMAND
     * TODO: create gitlet repo
     * TODO: make initial commit (save it with its hash code) with commit message "initial commit"
     * TODO: create master and HEAD pointer and have it point to initial commit
     * TODO: create the rest of the things needed in .gitlet
     * TODO: FAIL if gitlet repo already exists
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
        GITLET_DIR.mkdir(); OBJECTS.mkdir(); BRANCHES.mkdir();

        // Initial commit file
        Commit initialCommit = new Commit();
        initialCommit.saveCommit();

        //HEAD file with path to current branch
        try {
            HEAD.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.writeContents(HEAD, "branches/master");

        //MASTER file with initial commit id
        try {
            MASTER.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.writeContents(MASTER, initialCommit.hash());

    }

    public static void switchBranch(String newBranch) {
        Utils.writeContents(HEAD, "branches/" + newBranch);
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
    public void commit() {
        // Read HEAD commit object and staging area

        // Clone HEAD commit
        // Modify its message and timestamp
        // Use the staging area to modify the files tracked by the new commit

        // Convert pointers

        // Write back any new or modified object made into a new file
    }
}
