package gitlet;


import java.io.Serializable;
import java.util.Date;

/** Represents a gitlet commit object.
 *
 *  @author dongliang
 */
public class Commit implements Serializable {
    /**
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    /** The timestamp of this commit. */
    private Date timestamp;
    /** The parent of this commit. */
    private String parent;
    // TODO: variables of tracking all files.
    // TODO: sha1 naming?
    // TODO: branches and HEAD, which is just a refs using sha1.

    public Commit(String m, String p) {
        this.message = m;
        this.parent = p;
        this.timestamp = new Date(); // current date.
        // TODO verify this date object.
    }

    /** Create the initial commit with this no argument constructor.
     */
    public Commit() {
        this.message = "inital commit";
        this.parent = null;
        this.timestamp = new Date(0); // creates the 1970 UTC date.
    }

    public String sha1() {
        // TODO: is this the right way to do sha1? maybe it's right!
        //  this is having a universal method for this class to
        //  calculate a sha1 for each instance.
        return Utils.sha1(Utils.serialize(this));
    }
}
