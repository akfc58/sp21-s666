package gitlet;

import java.io.File;
import java.util.*;

import static gitlet.Utils.*;


/** Represents a gitlet repository.
 *  this Class handles all coordination among the other classes.
 *
 *  @author dongliang
 */
public class Repository {

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    /**
     * Initialize the .gitlet folder to contain gitlet opreations.
     * Structure as follows:
     * .gitlet/ -- top level folder for all persistent data in your lab12 folder
     *    - staged/ -- folder containing all stage files
     *       - added/ -- folder containing all stage files that marked added.
     *       - removed/ -- folder containing all stage files that marked removed.
     *    - refs/
     *    - object/ -- all persisted object.
     *    - log
     *    - HEAD
     */
    public static void init() {
        // not overwrite current .gitlet and exit.
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already" +
                    " exists in the current directory.");
            System.exit(0);
        }
        GITLET_DIR.mkdir();
        File commits = join(GITLET_DIR, "commits");
        File blobs = join(GITLET_DIR, "blobs");
        File refs = join(GITLET_DIR, "refs");
        commits.mkdir();
        refs.mkdir();
        blobs.mkdir();
        // TODO: master branch and HEAD branch refs to inital commit.
        // TODO: create all other things in .gitlet. 1. staged 2. objects. 3.refs(pointers as string) 4.logs?
        Commit initalCommit = new Commit();
        File intialCommitFile = join(commits, initalCommit.sha1());
        Utils.writeObject(intialCommitFile, initalCommit);
    }

    public static void add(String fileName) {
        List<String> allPlainFiles = plainFilenamesIn(CWD);
        if (allPlainFiles!= null && allPlainFiles.contains(fileName)) {
            String fileContent = readContentsAsString(join(CWD, fileName));
            String blobSha1Name = sha1(fileContent);
            File blob = join(GITLET_DIR, "blobs", blobSha1Name);
            writeContents(blob, fileContent); // create blob that contains file content.
            //TODO: add name -> blob set of map to stage area.
            Stage.addStage(fileName, blob);
            Stage.saveStage();
        } else {
            System.out.println("File does not exist.");
            System.exit(0);
        }
    }

}
