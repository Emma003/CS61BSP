package gitlet;

import java.io.File;
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
    /** The staging area for addition (filename, blob ID)*/
    public static Map<String,String> additionStagingArea = new HashMap<>();
    /** The staging area for removal (filename, blob ID)*/
    public static Map<String,String> removalStagingArea = new HashMap<>();

    /* TODO: fill in the rest of this class. */

    /**
     * INIT COMMAND
     * TODO: create gitlet repo
     * TODO: make initial commit (save it with its hash code)
     * TODO: create master and HEAD pointer and have it point to initial commit
     * TODO: create the rest of the things needed in .gitlet
     * TODO: FAIL if gitlet repo already exists
     *
     * Directory map:
     * .gitlet
     *      InitialCommit hashcode
     */
    public static void init() {
        //Get CWD
        if (!GITLET_DIR.exists()) {
            GITLET_DIR.mkdir();
        } else {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        }

        Commit initialCommit = new Commit();
        String initialHashcode = initialCommit.hash();
        File initialCommitFile = join(GITLET_DIR, initialHashcode);
        Utils.writeObject(initialCommitFile,initialCommit);

    }

    /**
     * TODO: add file to staging area
     * TODO: overwrite previous staging area entry if its the same file being staged
     * TODO: implement persistence for the staging area (save hashmap in file)
     * TODO: avoid adding to staging area if its the same version as the current commit
     * TODO: remove file from staging area if it's the same version as current commit
     * TODO: FAIL: file doesn't exist
     * @param filename
     */
    public static void add (String filename) {

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
