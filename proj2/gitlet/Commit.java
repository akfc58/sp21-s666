package gitlet;

import java.io.Serializable;
import java.util.*;

/** Represents a gitlet commit object.
 *
 *  @author dongliang
 */
public class Commit implements Serializable, Dumpable{

    /** The message of this Commit. */
    private String message;
    /** The timestamp of this commit. */
    private Date timestamp;
    /** The parent of this commit. Represented in sha-1 value. */
    private String parent;
    /** The blobs Set of this commit. */
    private Map<String, String> commitContent = new TreeMap<>();

    public Commit(String m, String p) {
        this.message = m;
        this.parent = p;
        this.timestamp = new Date(); // current date.
        this.commitContent = updateFile();
    }

    /** Create the initial commit with this no argument constructor.
     */
    public Commit() {
        this.message = "initial commit";
        this.parent = null;
        this.timestamp = new Date(0); // creates the 1970 UTC date.
        this.commitContent = null;
    }

    /**
     * this function update the commitContent to files to be commited.
     * @return
     */
    private Map<String, String> updateFile() {
        Commit father = Utils.readObject(Utils.join(Repository.GITLET_COMMITS,
                this.parent), Commit.class);
        Stage e = Utils.readObject(Utils.join(Repository.GITLET_DIR,
                "stage"), Stage.class);
        Map<String, String> returnVal = new TreeMap<>();
        if (father.commitContent != null) {
            returnVal.putAll(father.commitContent);
        }
        if (e.getToAdd() != null) {
            returnVal.putAll(e.getToAdd());
        }
        if (e.getToRemove() != null) {
            for (String fileToRemove: e.getToRemove().keySet()) {
                returnVal.remove(fileToRemove);
            }
        }
        return returnVal;
    }

    public String sha1() {
        // TODO: is this the right way to do sha1? maybe it's right!
        //  this is having a universal method for this class to
        //  calculate a sha1 for each instance.
        return Utils.sha1(Utils.serialize(this));
    }

    /** getters. */
    public Map<String, String> getCommitContent() {
        return commitContent;
    }

    public String getParent() {
        return parent;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void dump() {
        System.out.println(this.sha1());
        System.out.println(this.getTimestamp().toString());
        System.out.println(this.getMessage());
        System.out.println(this.getCommitContent());
        System.out.println(this.getParent());
    }
}
