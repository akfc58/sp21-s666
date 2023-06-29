package gitlet;

import java.io.File;
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
            System.out.println("A Gitlet version-control system already" +
                    " exists in the current directory.");
            System.exit(0);
        }
        makeDirectories();
        Commit initalCommit = new Commit(); // constructor without parameters specific for the initial commit.
        writeCommitChangeHEAD(initalCommit);
        // TODO: master branch  refs to inital commit.
    }

    /** helper function of init. creates necessary folders. */
    private static void makeDirectories() {
        File commits = Utils.join(GITLET_DIR, "commits");
        File blobs = Utils.join(GITLET_DIR, "blobs");
        File refs = Utils.join(GITLET_DIR, "refs");
        GITLET_DIR.mkdir();
        commits.mkdir();
        refs.mkdir();
        blobs.mkdir();
    }

    /** write commit to .gitlet/commits.
     * because HEAD changes iff a commit is written. so change HEAD at the same time. */
    private static void writeCommitChangeHEAD(Commit c) {
        String commitSha1 = c.sha1();
        File commitFile = Utils.join(GITLET_DIR, "commits", commitSha1);
        Utils.writeObject(commitFile, c);
        Refs.setSaveHEAD(commitSha1);
    }

    /**
     * add FILENAME to add/remove one file at a time to the staging area.
     * Includes: turn file into a blob; update stage; save stage.
     * @param fileName
     */
    public static void add(String fileName) {
        if (!GITLET_DIR.exists()) {
            System.out.println("No .gitlet! please check.");
            System.exit(0);
        }
        List<String> allPlainFiles = Utils.plainFilenamesIn(CWD);
        // make sure that the asked file exists.
        if (allPlainFiles!= null && allPlainFiles.contains(fileName)) {
            String fileContent = Utils.readContentsAsString(Utils.join(CWD, fileName));
            String blobSha1Name = Utils.sha1(fileContent);
            File blob = Utils.join(GITLET_DIR, "blobs", blobSha1Name);
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
     * @param message
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
}
