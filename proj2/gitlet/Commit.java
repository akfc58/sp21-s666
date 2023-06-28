package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.util.Date;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
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
    private Commit parent;
    // TODO: variables of tracking all files.

    public Commit(String m, Commit p) {
        this.message = m;
        this.parent = p;
        this.timestamp = new Date();
        // TODO verify this date object.
    }

    /** Create the initial commit with this no argument constructor.
     */
    public Commit() {
        this.message = "inital commit";
        this.parent = null;
        this.timestamp = new Date(0); // creates the 1970 UTC date.
        // TODO verify this date object.

    }
}
