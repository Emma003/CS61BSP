package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class

/** Represents a gitlet commit object.
 *
 * Combinations of log messages, other metadata (commit date, author, etc.), a reference
 * to a tree, and references to parent commits. The repository also maintains a mapping
 * from branch heads to references to commits, so that certain important commits have
 * symbolic names.
 *
 *
 *
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author procrastin
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    /** The date of this Commit. */
    private Date timestamp;
    /** The parent of this Commit. */
    private String parent;
    /** The files tracked in this Commit. */
    /** TODO: add a data structure that contains/tracks the files */

    /* TODO: fill in the rest of this class. */

    /** Makes initial commit (no arguments)*/
    public Commit() {
        this.message = "initial commit";
        this.parent = null;
        this.timestamp = new Date(0); // epoch date TODO: verify this w/ print statement
    }


    /** Makes a new commit object (takes arguments) */
    public Commit(String message, String parent) {
        this.message = message;
        this.parent = parent;
        if (this.parent == null) {
            this.timestamp = null;
        }

    }

    public String getParent() {
        return parent;
    }

    public String getFiles() {
        return null;
    }

    public String getMessage() {
        return this.message;
    }

    public Date getDate() {
        return this.timestamp;
    }
}
