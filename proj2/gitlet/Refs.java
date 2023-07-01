package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

/**
 * This class defines references of branches,
 * such as HEAD, master. References are represented in sha-1 strings.
 */
public class Refs implements Serializable {

    /** dir of HEAD. */
    private static final File HEAD_DIR = Utils.join(Repository.GITLET_DIR, "HEAD");
    /** the refs: HEAD, activeBranch, otherBranch. */
    private static String HEAD;
    //TODO: branches pointer
    private static Map<String, String> activeBranch = new TreeMap<>();
    private static Map<String, String> otherBranch = new TreeMap<>();

    /** save a given commit's sha1 value which HEAD points at to HEAD_DIR. */
    public static void SaveHEAD(String head) {
        HEAD = head;
        Utils.writeContents(HEAD_DIR, HEAD);
    }

    /** get HEAD from HEAD_DIR. */
    public static String getHEAD() {
        return Utils.readContentsAsString(HEAD_DIR);
    }

    public static void setActiveBranch(String key, String val) {
        if (activeBranch.containsKey(key) || otherBranch.containsKey(key)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        activeBranch.put(key, val);
        otherBranch.remove(key, val);
        int counter = 0;
        for (String branch: activeBranch.keySet()) {
            counter += 1;
            if (counter >= 2) {
                System.out.println("more than one active branch!");
                System.exit(0);
            }
            Utils.writeContents(Utils.join(Repository.ACTIVE_DIR, branch), activeBranch.get(branch));
        }
    }

    public static String getActiveBranchName() {
        List<String> l = Utils.plainFilenamesIn(Repository.ACTIVE_DIR);
        return l.get(0);
    }

    //TODO set other branch. getter of branch.
}
