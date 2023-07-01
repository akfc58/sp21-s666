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

    /** save a given commit's sha1 value which HEAD points at to HEAD_DIR. */
    public static void SaveHEAD(String head) {
        HEAD = head;
        Utils.writeContents(HEAD_DIR, HEAD);
    }

    /** get HEAD from HEAD_DIR. */
    public static String getHEAD() {
        return Utils.readContentsAsString(HEAD_DIR);
    }

    /** set active branch in ACTIVE_DIR in plain text.
     * if KEY is the same as old active branch, program halts.
     * delete old active branch and save new KEY, VAL as new branch. */
    public static void moveActiveBranch(String val) {
        List<String> l = Utils.plainFilenamesIn(Repository.ACTIVE_DIR);
        if (l == null || l.isEmpty()) {
            // There's no active commit, create master.
            Utils.writeContents(Utils.join(Repository.ACTIVE_DIR, "master"), val);
        } else {
            // Move active branch simply means to change the commit id it points to.
            Utils.writeContents(Utils.join(Repository.ACTIVE_DIR, l.get(0)), val);
        }
    }

    public static String getActiveBranchName() {
        List<String> l = Utils.plainFilenamesIn(Repository.ACTIVE_DIR);
        if (l.isEmpty()) {
            System.out.println("there is no active branch. please check.");
        }
        return l.get(0);
    }

    /** Set new branch, do nothing else. */
    public static void setBranch(String key, String val) {
        //TODO 1. read otherbranch object. 2.read active branch(HEAD) 3. create a new branch points to HEAD 4.save to file.
        List<String> l = Utils.plainFilenamesIn(Repository.GITLET_REFS);
        if (l.contains(key) || getActiveBranchName() == key) {
            // if new branch exists
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        Utils.writeContents(Utils.join(Repository.GITLET_REFS, key), val);
    }

    public static String getBranch(String key) {
        return Utils.readContentsAsString(Utils.join(Repository.GITLET_REFS, key));
    }

    /** If a branch with the given name does not exist, aborts.
    Print the error message A branch with that name does not exist.
     If you try to remove the branch you’re currently on, aborts,
     printing the error message Cannot remove the current branch. */
    public static void removeBranch(String key) {
        List<String> l = Utils.plainFilenamesIn(Repository.GITLET_REFS);
        if (getActiveBranchName().equals(key)) {
            // if try to remove active branch, aborts.
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        if (!l.contains(key)) {
            // if remove a non-exist branch, aborts.
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        File f =Utils.join(Repository.GITLET_REFS, key);
        f.delete(); //TODO careful!
    }

    public static void changeActiveBranch(String newActive) {
        File oldActiveFile = Utils.join(Repository.GITLET_REFS, "active", Refs.getActiveBranchName());
        oldActiveFile.renameTo(Utils.join(Repository.GITLET_REFS, Refs.getActiveBranchName()));
        File newActiveFile = Utils.join(Repository.GITLET_REFS, newActive);
        newActiveFile.renameTo(Utils.join(Repository.GITLET_REFS, "active", newActive));

    }
    //TODO set other branch. getter of branch.
}
