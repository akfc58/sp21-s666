package gitlet;

import java.io.File;
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
        // TODO: master branch and HEAD branch refs to inital commit.
        // TODO: create all other things in .gitlet.
        Commit initalCommit = new Commit();
        File intialCommitFile = join(GITLET_DIR, "initalCommit");
        Utils.writeObject(intialCommitFile, initalCommit);
    }
}
