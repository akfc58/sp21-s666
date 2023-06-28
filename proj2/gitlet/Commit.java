package gitlet;


import java.io.Serializable;
import java.util.*;
import static gitlet.Utils.*;

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
    private Set<Map<String, String>> commitContent = new HashSet<>();
    /** The Branches of commit. */
    private static List<Map<String, String>> branches = new ArrayList<>(); // same for all commits.
    private static String HEAD = ""; // same for all commits.

    public Commit(String m, String p) {
        this.message = m;
        this.parent = p;
        this.timestamp = new Date(); // current date.
        this.commitContent = updateFile();

    //TODO: 1. of parent. 2. of stage toAdd.
    }

    /** Create the initial commit with this no argument constructor.
     */
    public Commit() {
        this.message = "inital commit";
        this.parent = null;
        this.timestamp = new Date(0);// creates the 1970 UTC date.
        this.commitContent = null;
        HEAD = this.sha1();
        Map<String, String> newMap = new HashMap<>();
        newMap.put("master", HEAD);
        branches.add(newMap);
    }

    /**
     * this function update the commit entry of files to be commited.
     * @return
     */
    private Set<Map<String, String>> updateFile() {
        Commit father = Utils.readObject(join(Repository.GITLET_DIR,
                "commits",this.parent), Commit.class);
        Stage e = Utils.readObject(join(Repository.GITLET_DIR,
                "stage"), Stage.class);
        Set<Map<String, String>> returnVal = new HashSet<>();
        returnVal.addAll(father.commitContent);
        returnVal.addAll(e.getToAdd());
        System.out.println(returnVal);
        return returnVal;
    }

    public String sha1() {
        // TODO: is this the right way to do sha1? maybe it's right!
        //  this is having a universal method for this class to
        //  calculate a sha1 for each instance.
        return Utils.sha1(Utils.serialize(this));
    }

    /**
     * get universal HEAD.
     * @return
     */
    public static String getHEAD() {
        return HEAD;
    }
}
