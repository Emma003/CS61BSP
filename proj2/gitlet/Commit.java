package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

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
        this.filesInCommit = new TreeMap<>();
        String idtext = "commit" + parent + message;
        this.id = Utils.sha1(idtext);
    }


    /** Makes a new commit object (takes arguments) */
    public Commit(String message, String parent, boolean mergeCommit) {
        // Clone HEAD commit
        Commit currentCommit = Commit.returnCommit(parent);
        this.filesInCommit = currentCommit.filesInCommit;

        // Update metadata
        this.message = message;
        this.parent = parent;
        this.timestamp = new Date(0);
        this.isMergeCommit = mergeCommit;
        String idtext = "commit" + parent + message + mergeCommit;
        this.id = Utils.sha1(idtext);
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
            if (filesInCommit.containsKey(filename)) {
                if(filesInCommit.get(filename).equals(blob)) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Iterates over addition stage files and adds/modifies/deletes from the current commit*/
    public void updateCommitFiles (Map<String,String> stagedForAddition, ArrayList<String> stagedForRemoval) {
        // Iterating over addition stage
        for (Map.Entry<String, String> entry : stagedForAddition.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if(filesInCommit.containsKey(key)) {
                filesInCommit.replace(key,value);
            } else {
                filesInCommit.put(key,value);
            }
        }

        // Iterating over removal stage
        for (String file: stagedForRemoval) {
            if(filesInCommit.containsKey(file)) {
                filesInCommit.remove(file);
            }
        }
    }

    public void putFileInCWD(String filename) {
        // Commit doesn't have the requested file [FAILURE CASE]
        if (!filesInCommit.containsKey(filename)) {
            System.out.println("File does not exist in that commit.");
            return;
        }

        // Retrieve blob of file
        String fileContent = Blob.returnBlob(filesInCommit.get(filename));
        List<String> cwdFiles = Utils.plainFilenamesIn(Repository.CWD);

        // A version of the file exists in CWD -> delete it
        if (cwdFiles.contains(filename)) {
            Utils.restrictedDelete(filename);
        }

        // Create new file in CWD and write contents
        File currentFile = Utils.join(Repository.CWD,filename);
        try {
            currentFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.writeContents(currentFile,fileContent);
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
        if (isMergeCommit) {
            return "WRITE THE CORRECT STATUS OUTPUT FOR MERGE COMMITS";
        }
        return "=== \ncommit " + this.id + "\nDate: " + this.timestamp.toString() + "\n" + this.message + "\n";
    }
}
