package gitlet;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/** Represents a gitlet repository.
 *  this Class handles all coordination among the other classes.
 *
 *  @author dongliang
 */
public class Repository {

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = Utils.join(CWD, ".gitlet");
    /** The blobs dicrectory. */
    public static final File GITLET_BLOBS = Utils.join(GITLET_DIR, "blobs");
    /** The commits directory. */
    public static final File GITLET_COMMITS = Utils.join(GITLET_DIR, "commits");
    public static final File GITLET_REFS = Utils.join(GITLET_DIR, "refs"); // TODO: use or not?
    /** The HEAD. */
    public static final File GITLET_HEAD = Utils.join(GITLET_DIR, "HEAD");


    /**
     * Initialize the .gitlet folder to contain gitlet opreations.
     * Structure as follows:
     * .gitlet/ -- top level folder for all persistent data in your lab12 folder
     *    - stage -- a file containing all staged files' map of added/removed name and its blob.
     *    - refs/ //TODO
     *    - blobs/ -- different version of files. name in sha-1.
     *    - commits/ -- commits. name in sha-1.
     *    - log //TODO
     */
    public static void init() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already"
                    + " exists in the current directory.");
            System.exit(0);
        }
        makeDirectories();
        Commit initalCommit = new Commit(); // constructor without parameters is specific for the initial commit.
        writeCommitChangeHEAD(initalCommit);
        // TODO: master branch refs to inital commit.
    }

    /** helper function of init. creates necessary folders. */
    private static void makeDirectories() {
        GITLET_DIR.mkdir();
        GITLET_BLOBS.mkdir();
        GITLET_COMMITS.mkdir();
        GITLET_REFS.mkdir();
    }

    /** write commit to .gitlet/commits.
     * because HEAD changes iff a commit is written. so change HEAD at the same time. */
    private static void writeCommitChangeHEAD(Commit c) {
        String commitSha1 = c.sha1();
        File commitFile = Utils.join(GITLET_COMMITS, commitSha1);
        Utils.writeObject(commitFile, c);
        Refs.SaveHEAD(commitSha1);
    }

    /**
     * add FILENAME to add/remove one file at a time to the staging area.
     * Includes: turn file into a blob; update stage; save stage.
     */
    public static void add(String fileName) {
        if (!GITLET_DIR.exists()) {
            System.out.println("No .gitlet! please check.");
            System.exit(0);
        }
        List<String> allPlainFiles = Utils.plainFilenamesIn(CWD);
        // make sure that the asked file exists.
        if (allPlainFiles != null && allPlainFiles.contains(fileName)) {
            String fileContent = Utils.readContentsAsString(Utils.join(CWD, fileName));
            String blobSha1Name = Utils.sha1(fileContent);
            File blob = Utils.join(GITLET_BLOBS, blobSha1Name);
            Utils.writeContents(blob, fileContent);
            Stage e = new Stage();
            e.addStage(fileName, blobSha1Name);
        } else {
            System.out.println("File does not exist.");
            System.exit(0);
        }
    }

    /**
     * Receives commit message, make a new commit according to stage area and old commit.
     */
    public static void commit(String message) {
        if (!GITLET_DIR.exists()) {
            System.out.println("No .gitlet! please check.");
            System.exit(0);
        }
        Stage e = new Stage();
        if (!e.haveStage()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        if (message.isEmpty()) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }
        Commit c = new Commit(message, Refs.getHEAD()); // old HEAD is always where the parent is.
        writeCommitChangeHEAD(c);
        e.clear(); // clear stage area after commit.
    }

    /**
     * Checkout File that is in current HEAD, replace it in CWD.
     * Get current commit using HEAD in .gitlet.
     */
    public static void checkoutFile(String fileName) {
        String currHEAD = Utils.readContentsAsString(GITLET_HEAD);
        File currCommit = Utils.join(GITLET_COMMITS, currHEAD);
        checkoutFileHelper(currCommit, fileName);
    }

    /**
     * Checkout File that is in arbitrarly commit, replace it in CWD.
     */
    public static void checkoutCommitFile(String commitBlob, String fileName) {
        File desCommit = Utils.join(GITLET_COMMITS, commitBlob);
        checkoutFileHelper(desCommit, fileName);
    }

    /**
     * checkout file from current commit and arbitrarly commit is basically the same.
     */
    private static void checkoutFileHelper(File checkoutCommit, String fileName) {

        Commit c = Utils.readObject(checkoutCommit, Commit.class);
        Map<String, String> content = c.getCommitContent();
        if (content.containsKey(fileName)) {
            String blobName = content.get(fileName);
            File inFile = Utils.join(GITLET_BLOBS, blobName);
            File outFile = Utils.join(CWD, fileName);
            Utils.restrictedDelete(outFile); // delete the target file in CWD. (guaranteed there!)
            Utils.writeContents(outFile, Utils.readContentsAsString(inFile));
        } else {
            System.out.println("File does not exist in that commit.");
        }
    }

    /**
     * Starting at the current head commit, display information about each
     * commit backwards along the commit tree until the initial commit,
     * following the first parent commit links, ignoring any second parents
     * found in merge commits.
     */
    public static void log() {
        String currSha1 = Utils.readContentsAsString(GITLET_HEAD);
        File currCommit = Utils.join(GITLET_COMMITS, currSha1);
        Commit c = Utils.readObject(currCommit, Commit.class);
        String content;
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z");
        while (c.getParent() != null) {
            Date d = c.getTimestamp();
            content = "===\n" + "commit " + currSha1 + "\n"
                    + "Date: " + formatter.format(d) + "\n" + c.getMessage() + "\n\n";
            System.out.print(content);
            currSha1 = c.getParent();
            currCommit = Utils.join(GITLET_COMMITS, currSha1);
            c = Utils.readObject(currCommit, Commit.class);
        }
        content = "===\n" + "commit " + currSha1 + "\n"
                + "Date: " + formatter.format(c.getTimestamp()) + "\n" + c.getMessage() + "\n\n";
        System.out.print(content);
    }
}
