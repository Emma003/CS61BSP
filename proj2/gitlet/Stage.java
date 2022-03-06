package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gitlet.Utils.join;
import static gitlet.Utils.readContentsAsString;

public class Stage implements Serializable {

    /** INDEX file that contains serialized staging area object*/
    public static final File INDEX = join(Repository.GITLET_DIR, "INDEX.txt");
    /** addition staging area*/
    public static final Map<String,String> additionStage = new HashMap<>();
    /** removal staging area*/
    public static final Map<String,String> removalStage = new HashMap<>();
    /** tracked files with unstaged modifications*/
    public static final Map<String,String> modifiedTrackedFiles = new HashMap<>();
    /** all files that were ever committed*/
    public static final Map<String,String> trackedFiles= new HashMap<>();
    /** all files in CWD that were never staged/committed*/
    public static final Map<String,String> untrackedFiles = new HashMap<>();


    /**
     * TODO: add file to staging area
     * TODO: overwrite previous staging area entry if its the same file being staged
     * TODO: implement persistence for the staging area (save hashmap in file)
     * TODO: avoid adding to staging area if its the same version as the current commit
     * TODO: remove file from staging area if it's the same version as current commit
     * TODO: FAIL: file doesn't exist
     * @param filename
     */
    public static void add(String filename) {
        List<String> cwdFiles = Utils.plainFilenamesIn(Repository.CWD);

        // Failure case: file isn't in CWD
        if (!cwdFiles.contains(filename)) {
            System.out.println("File does not exist.");
            return;
        }

        File addedFile = new File(Repository.CWD, filename);
        String contents = Utils.readContentsAsString(addedFile);
        Blob newBlob = new Blob(filename, contents);

        /**
         * TODO: if the file is the same as the current commit,
         */

        // File is already staged
        if (additionStage.containsKey(filename)) {
            additionStage.replace(filename, newBlob.id);
        } else {
            additionStage.put(filename,newBlob.id);
        }





    }

    public static void saveStage(String filename) {

    }

    public static void returnStage(String filename) {

    }

    public static void rm(String filename) {

    }

    public static void status(String filename) {

    }

    public static void clearStagingArea(String filename) {

    }


}
