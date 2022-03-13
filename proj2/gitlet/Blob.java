package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import static gitlet.Utils.join;

public class Blob implements Serializable {
    private String filename;
    private String id;
    private String contents;

    public Blob(String filename, String contents) {
        this.filename = filename;
        this.contents = contents;
        String idtext = "blob" + filename + contents;
        this.id = Utils.sha1(idtext);
    }

    public void saveBlob() {
        File blobFile = join(Repository.BLOBS, id);
        try {
            blobFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.writeObject(blobFile, contents);
    }

    public static Blob returnBlob(String filename) {
        File addedFile = Utils.join(Repository.CWD, filename);
        String contents = Utils.readContentsAsString(addedFile);
        return new Blob(filename, contents);
    }

    /**
     * Returns blob content at a string given blob id
     * */
    public static String returnBlobContent(String blobID) {
        File inFile = join(Repository.BLOBS, blobID);
        String blobContents = Utils.readObject(inFile, String.class);
        return blobContents;
    }

    /**
     * Returns the hashcode of a blob
     */
    public String hash() {
        return this.id;
    }
}
