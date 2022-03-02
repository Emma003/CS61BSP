package gitlet;

import java.io.File;
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

    /* TODO: fill in the rest of this class. */

    /**
     * TODO: create gitlet repo
     * TODO: make initial commit
     * TODO: create the rest of the things needed in .gitlet
     * TODO: FAIL if gitlet repo already exists
     *
     * .gitlet
     *      InitialCommit
     */
    public void init() {
        //Get CWD
        if (!GITLET_DIR.exists()) {
            GITLET_DIR.mkdir();
        }

        Commit initialCommit = new Commit();
        File initialCommitFile = Utils.join(GITLET_DIR,initialCommit);
        Utils.writeObject(initialCommitFile,initialCommit);


    }


    public void commit() {
        // Read HEAD commit object and staging area

        // Clone HEAD commit
        // Modify its message and timestamp
        // Use the staging area to modify the files tracked by the new commit

        // Convert pointers

        // Write back any new or modified object made into a new file
    }
}
