package gitlet;

import java.io.File;
import java.io.IOException;

import static gitlet.Utils.join;

public class Blob {
    public String filename;
    public String id;
    public String contents;

    public Blob(String filename, String contents) {
        this.filename = filename;
        this.contents = "blob" + filename + contents;
        this.id = Utils.sha1(contents);
    }

    public void saveBlob(){
        File blobFile = join(Repository.OBJECTS, id);
        try {
            blobFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.writeObject(blobFile,contents);
    }
}
