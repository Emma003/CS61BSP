package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gitlet.Utils.join;
import static gitlet.Utils.readContentsAsString;

public class Stage implements Serializable {

    /** INDEX file that contains serialized staging area object*/
    public static final File INDEX = join(Repository.GITLET_DIR, "INDEX.txt");
    /** addition staging area*/
    public final Map<String,String> additionStage = new HashMap<>();
    /** removal staging area*/
    public final List<String> removalStage = new ArrayList<>();
    /** tracked files with unstaged modifications*/
    public final Map<String,String> modifiedTrackedFiles = new HashMap<>();
    /** all files that were ever committed*/
    public final Map<String,String> trackedFiles= new HashMap<>();
    /** all files in CWD that were never staged/committed*/
    public final List<String> untrackedFiles = new ArrayList<>();


    /**
     * TODO: TEST THIS
     * @param filename
     */
    public void add(String filename) {
        List<String> cwdFiles = Utils.plainFilenamesIn(Repository.CWD);

        // Failure case: file isn't in CWD
        if (!cwdFiles.contains(filename)) {
            System.out.println("File does not exist.");
            return;
        }

        // Return file and blob using filename
        File addedFile = Utils.join(Repository.CWD, filename);
        String contents = Utils.readContentsAsString(addedFile);
        Blob newBlob = new Blob(filename, contents);

        // Check if file is already in current commit
        String currentCommitID = CommitTree.currentCommit();
        Commit currentCommit = Commit.returnCommit(currentCommitID);
        if (currentCommit.isCommitVersion(filename, newBlob.id)) {
            if(additionStage.containsKey(filename)) {
                additionStage.remove(filename);
            }
            return;
        }

        // File is already staged
        if (additionStage.containsKey(filename)) {
            additionStage.replace(filename, newBlob.id);
            return;
        }

        // File is staged for removal
        if (removalStage.contains(filename)) {
            removalStage.remove(filename);
        }

        // Add file to addition staging area
        additionStage.put(filename,newBlob.id);

    }

    public static void saveIndex(Stage stage) {
        File indexFile = INDEX;
        Utils.writeObject(indexFile, stage);
    }

    public static Stage returnIndex() {
        File inFile = INDEX;
        return Utils.readObject(inFile,Stage.class);
    }

    /** TODO: TEST THIS*/
    public List<String> getUntrackedFiles() {
        String currentCommitID = CommitTree.currentCommit();
        Commit currentCommit = Commit.returnCommit(currentCommitID);

        List<String> filesInCWD = Utils.plainFilenamesIn(Repository.CWD);
        for (String file: filesInCWD) {
            if(!currentCommit.getFiles().containsKey(file) && !additionStage.containsKey(file)) {
                untrackedFiles.add(file);
            }
        }
        return untrackedFiles;
    }



    /** TODO: TEST THIS */
    public void rm(String filename) {
        // Check if file is tracked/stage
        String currentCommitID = CommitTree.currentCommit();
        Commit currentCommit = Commit.returnCommit(currentCommitID);
        if(!currentCommit.getFiles().containsKey(filename) && !additionStage.containsKey(filename)) {
            System.out.println("No reason to remove the file");
            return;
        }

        // File is already staged -> remove from staginf
        if (additionStage.containsKey(filename)) {
            additionStage.remove(filename);
        }

        // File is tracked
        if (currentCommit.getFiles().containsKey(filename)) {
            Utils.restrictedDelete(filename);
        }

        removalStage.add(filename);
    }

    /** TODO: Add functions for modified files and untracked files
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

        // Addition stage area
        System.out.println("\n\n=== Staged Files ===");
        for (String file: additionStage.keySet()) {
            System.out.println(file);
        }

        System.out.println("\n\n=== Removed Files ===");
        for (String file: removalStage) {
            System.out.println(file);
        }

        System.out.println("\n\n=== Modifications Not Staged For Commit ===");

        System.out.println("\n\n=== Untracked Files ===");
        List<String> untrackedFiles = this.getUntrackedFiles();
        for (String file: untrackedFiles) {
            System.out.println(file);
        }
    }

    public static void clearStagingArea(String filename) {

    }


}
