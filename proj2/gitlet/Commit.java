package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;
import java.util.TreeMap;

import static gitlet.Utils.join;

/** Represents a gitlet commit object.
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
     * TODO: MAKE SURE TO READ THE TREEMAP DOCUMENTATION, THERE'S A BUNCH OF USEFUL METHOD IT IMPLEMENTS!!!!

    /** The message of this Commit. */
    private String message;
    /** The date of this Commit. */
    private Date timestamp;
    /** The parent of this Commit. */
    private String parent;
    /** The id of this Commit. */
    private String id;
    /** Is the commit a merge commit. */
    private boolean isMergeCommit;
    /** The files tracked in this Commit. */
    private TreeMap<String,String> filesInCommit = new TreeMap<>();

    /* TODO: fill in the rest of this class. */

    /** Makes initial commit (no arguments)*/
    public Commit() {
        this.message = "initial commit";
        this.parent = null;
        this.timestamp = new Date(0); // epoch date TODO: verify this w/ print statement
        this.isMergeCommit = false;
        String idtext = "commit" + parent + message;
        this.id = Utils.sha1(idtext);
    }


    /** Makes a new commit object (takes arguments) */
    public Commit(String message, String parent) {
        this.message = message;
        this.parent = parent;
        if (this.parent == null) {
            this.timestamp = null;
        }

    }

    public void saveCommit() {
        File commitFile = Utils.join(Repository.COMMITS, id);
        try {
            commitFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.writeObject(commitFile,this);
    }

    public static Commit returnCommit(String filename) {
        File commitFile = Utils.join(Repository.COMMITS, filename);
        return Utils.readObject(commitFile, Commit.class);
    }

    /**
     * Returns true if the file passed as parameter is the same version as in this commit
     * @param filename
     * @param blob
     * @return
     */
    public boolean isCommitVersion(String filename, String blob) {
        if (filesInCommit != null) {
            if (filesInCommit.keySet().contains(filename)) {
                if(filesInCommit.get(filename) == blob) {
                    return true;
                }
            }
        }

        return false;
    }

    public void addFilesToCommit(HashMap<String,String> files) {

    }

    public String getParent() {
        return parent;
    }

    public TreeMap<String, String> getFiles() {
        return filesInCommit;
    }

    public String getMessage() {
        return this.message;
    }

    public Date getDate() {
        return this.timestamp;
    }

    public String hash() {
        return this.id;
    }

    @Override
    public String toString() {
        return "=== \ncommit " + this.id + "\nDate: " + this.timestamp.toString() + "\n" + this.message + "\n";
    }
}
