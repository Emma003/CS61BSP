package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gitlet.Utils.join;
import static gitlet.Utils.readContentsAsString;

public class Stage implements Serializable {

    /** INDEX file that contains serialized staging area object*/
    static final File INDEX = join(Repository.GITLET_DIR, "INDEX.txt");
    /** addition staging area*/
    public Map<String,String> additionStage = new HashMap<>();
    /** removal staging area*/
    public ArrayList<String> removalStage = new ArrayList<>();
    /** tracked files with unstaged modifications*/
    public Map<String,String> modifiedTrackedFiles = new HashMap<>();
    /** all files that were ever committed*/
    public Map<String,String> trackedFiles = new HashMap<>();
    /** all files in CWD that were never staged/committed*/
    public ArrayList<String> untrackedFiles = new ArrayList<>();

    public Stage() {
    }

    /**
     * createIndex(): initializes index file, only used when init is called
     * saveIndex(): serializes index file after modifications
     * returnIndex(): deserializes index file and returns it as a Stage object
     * */
    public static void createIndex(Stage stage) {
        try {
            Stage.INDEX.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.writeObject(INDEX,stage);
    }

    public static void saveIndex(Stage stage) {
        File indexFile = INDEX;
        Utils.writeObject(indexFile, stage);
    }

    public static Stage returnIndex() {
        File inFile = INDEX;
        return Utils.readObject(inFile,Stage.class);
    }


    /**
     * TODO: TEST THIS
     * @param filename
     */
    public void add(String filename) {
        List<String> cwdFiles = Utils.plainFilenamesIn(Repository.CWD);

        // File isn't in CWD [FAILURE CASE]
        if (cwdFiles == null || !cwdFiles.contains(filename)) {
            System.out.println("File does not exist.");
            return;
        }

        // Return file and blob using filename
        File addedFile = Utils.join(Repository.CWD, filename);
        String contents = Utils.readContentsAsString(addedFile);
        Blob newBlob = new Blob(filename, contents);

        // File version is already in current commit [FAILURE CASE]
        String currentCommitID = CommitTree.currentCommit();
        Commit currentCommit = Commit.returnCommit(currentCommitID);
        if (currentCommit.isCommitVersion(filename, newBlob.id)) {
            if(additionStage != null == additionStage.containsKey(filename)) {
                additionStage.remove(filename);
            }
            return;
        }

        // File is already staged [FAILURE CASE]
        if (additionStage != null) {
            if (additionStage.containsKey(filename)) {
                additionStage.replace(filename, newBlob.id);
                return;
            }
        }

        // File is staged for removal
        if (removalStage != null) {
            if (removalStage.contains(filename)) {
                removalStage.remove(filename);
            }
        }

        // Add file to addition staging area and save blob
        newBlob.saveBlob();
        additionStage.put(filename,newBlob.id);

    }

    /** TODO: TEST THIS */
    public void rm(String filename) {
        // Deserialize current commit
        String currentCommitID = CommitTree.currentCommit();
        Commit currentCommit = Commit.returnCommit(currentCommitID);

        // Current commit isn't tracking any file [FAILURE CASE]
        if (currentCommit.getFiles() == null) {
            System.out.println("No reason to remove the file.");
            return;
        }

        // File isn't tracked or staged [FAILURE CASE]
        if (!currentCommit.getFiles().containsKey(filename) && !additionStage.containsKey(filename)) {
            System.out.println("No reason to remove the file.");
            return;
        }

        // File is already staged -> remove from staging
        if (additionStage.containsKey(filename)) {
            additionStage.remove(filename);
        }

        // File is tracked -> delete from CWD
        if (currentCommit.getFiles().containsKey(filename)) {
            Utils.restrictedDelete(filename);
        }

        removalStage.add(filename);
    }

    /** TODO: Add functions for modified files and untracked files
     *  TODO: UNTRACKED FILES HAS A BUG WHEN RM IS CALLED
     *  TODO: TEST THIS
     */
    public void printStatus() {
        // Print branch status
        System.out.println("=== Branches ===");
        List<String> branches = Utils.plainFilenamesIn(Repository.BRANCHES);
        for (String branch: branches) {
            if(Utils.readContentsAsString(Repository.HEAD).equals(branch)) {
                System.out.print("*");
            }
            System.out.println(branch);
        }

        // Addition stage
        System.out.println("\n\n=== Staged Files ===");
        if(additionStage != null) {
            for (String file: additionStage.keySet()) {
                System.out.println(file);
            }
        }

        // Removal stage
        System.out.println("\n\n=== Removed Files ===");
        if (removalStage != null) {
            for (String file : removalStage) {
                System.out.println(file);
            }
        }

        // Modified non-staged files
        System.out.println("\n\n=== Modifications Not Staged For Commit ===");

        // Untracked files
        System.out.println("\n\n=== Untracked Files ===");
        List<String> untracked = this.getUntrackedFiles();
        if(untracked != null) {
            for (String file: untracked) {
                System.out.println(file);
            }
        }
        System.out.println();
        this.untrackedFiles.clear();

    }

    /** TODO: TEST THIS
     * This method returns the list of files in CWD that haven't been staged or committed
     * [HELPER METHOD]
     * */
    public List<String> getUntrackedFiles() {
        String currentCommitID = CommitTree.currentCommit();
        Commit currentCommit = Commit.returnCommit(currentCommitID);
        List<String> filesInCWD = Utils.plainFilenamesIn(Repository.CWD);

        if (currentCommit.getFiles() == null && additionStage == null) {
            return filesInCWD;
        }
        for (String file: filesInCWD) {
            if(!currentCommit.getFiles().containsKey(file) && !additionStage.containsKey(file)) {
                untrackedFiles.add(file);
            }
        }
        return untrackedFiles;
    }


    /**
     * This method empties addition and removal collections
     * [HELPER METHOD]
     * */
    public void clearStagingArea() {
        additionStage.clear();
        removalStage.clear();
    }


}
