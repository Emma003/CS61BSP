package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author procrastin
 */
public class Commit implements Serializable {

    /** The message of this Commit. */
    private String message;
    /** The date of this Commit. */
    private String timestamp;
    /** The parent of this Commit. */
    private String parent;
    /** The 2nd parent of this merge Commit. */
    private String secondParent;
    /** The id of this Commit. */
    private String id;
    /** Is the commit a merge commit. */
    private boolean isMergeCommit;
    /** The files tracked in this Commit. In the form <filename,blobID> */
    public TreeMap<String,String> filesInCommit = new TreeMap<>();

    /* TODO: fill in the rest of this class. */

    /** Makes initial commit (no arguments)*/
    public Commit() {
        this.message = "initial commit";
        this.parent = null;
        this.timestamp = getCurrentDate();
        this.isMergeCommit = false;
        this.filesInCommit = new TreeMap<>();
        String idtext = "commit" + parent + message;
        this.id = Utils.sha1(idtext);
    }


    /** Makes a new commit object */
    public Commit(String message, String parent, boolean isMergeCommit) {
        // Clone HEAD commit
        Commit currentCommit = Commit.returnCommit(parent);
        this.filesInCommit = currentCommit.filesInCommit;

        // Update metadata
        this.message = message;
        this.parent = parent;
        this.timestamp = getCurrentDate();
        this.isMergeCommit = isMergeCommit;
        String idtext = "commit" + parent + message + isMergeCommit;
        this.id = Utils.sha1(idtext);
    }

    /** Makes a new merge commit object
     * TODO: finish writing this
     * */
    public Commit(String message, String parent1, String parent2, boolean isMergeCommit) {
        // Clone HEAD commit
        Commit currentCommit = Commit.returnCommit(parent1);
        this.filesInCommit = currentCommit.filesInCommit;

        // Update metadata
        this.message = message;
        this.parent = parent1;
        this.timestamp = getCurrentDate();
        this.isMergeCommit = isMergeCommit;
        this.secondParent = parent2;
        String idtext = "commit" + parent1 + message + isMergeCommit;
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
        String fileContent = Blob.returnBlobContent(filesInCommit.get(filename));
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

    public static String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.ENGLISH);
        String strDate = formatter.format(date);
        return strDate;
    }

    public String hash() {
        return this.id;
    }

    /** This method returns the full ID given a shortened version */
    public static String getFullID(String abbreviation) {
        List<String> allCommits = Utils.plainFilenamesIn(Repository.COMMITS);
        for (String commit : allCommits) {
            if (commit.contains(abbreviation)) {
                return commit;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        if (isMergeCommit) {
            return "=== \ncommit " + this.id + "\nMerge: " + this.parent.substring(0,7) + " " + this.secondParent.substring(0,7) + "\nDate: " + this.timestamp + "\n" + this.message + "\n";
        }
        return "=== \ncommit " + this.id + "\nDate: " + this.timestamp + "\n" + this.message + "\n";
    }
}
