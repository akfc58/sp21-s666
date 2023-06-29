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
    public static String HEAD = "";
    public static List<String> branches = new ArrayList<>();

    /**
     * Initialize the .gitlet folder to contain gitlet opreations.
     * Structure as follows:
     * .gitlet/ -- top level folder for all persistent data in your lab12 folder
     *    - stage -- containing all staged files' map of added/removed name and its blob.
     *    - refs/ //TODO
     *    - blobs/ -- different version of files.
     *    - commits/
     *    - log
     */
    public static void init() {
        // not overwrite current .gitlet and exit.
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already" +
                    " exists in the current directory.");
            System.exit(0);
        }
        makeDirectories();
        Commit initalCommit = new Commit();
        writeCommitChangeHEAD(initalCommit);

        // TODO: master branch and HEAD branch refs to inital commit.
        // TODO: create all other things in .gitlet. 1. staged 2. objects.
        //  3.refs(pointers as string) 4.logs?

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

    /** write commit to .gitlet/commits. change HEAD. */
    private static void writeCommitChangeHEAD(Commit c) {
        String commitSha1 = c.sha1();
        File commitFile = Utils.join(GITLET_DIR, "commits", commitSha1);
        Utils.writeObject(commitFile, c);
        Refs.setSaveHEAD(commitSha1);
    }

    /**
     * add FILENAME to dd/remove one file at a time to the staging area.
     * Includes: turn file into a blob; update stage; save stage.
     * @param fileName
     */
    public static void add(String fileName) {
        if (!GITLET_DIR.exists()) {
            System.out.println("No .gitlet! please check.");
            System.exit(0);
        }
        List<String> allPlainFiles = Utils.plainFilenamesIn(CWD);
        if (allPlainFiles!= null && allPlainFiles.contains(fileName)) {
            String fileContent = Utils.readContentsAsString(Utils.join(CWD, fileName));
            String blobSha1Name = Utils.sha1(fileContent);
            File blob = Utils.join(GITLET_DIR, "blobs", blobSha1Name);
            Utils.writeContents(blob, fileContent); //TODO: should deal with binary files?
            changeStage(fileName, blobSha1Name); // change stage according to newly added blob.
        } else {
            System.out.println("File does not exist.");
            System.exit(0);
        }
    }

    private static void changeStage(String fileName, String blobName) {
            Stage.updateStage(fileName, blobName);
    }

    /**
     * Receives commit message, make a new commit according to stage area and old commit.
     * @param message
     */
    public static void commit(String message) {
        Commit c = new Commit(message, Refs.getHEAD());
        writeCommitChangeHEAD(c);
    }
}
