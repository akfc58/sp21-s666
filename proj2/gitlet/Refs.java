package gitlet;

import java.io.File;
import java.io.Serializable;

/**
 * This class defines references of branches,
 * such as HEAD, master. References are represented in sha-1 strings.
 */
public class Refs implements Serializable {

    /** dir of HEAD. */
    private static final File HEAD_DIR = Utils.join(Repository.GITLET_DIR, "HEAD");
    /** the refs. */
    private static String HEAD = "";
    //TODO: branches pointer

    public static void setSaveHEAD(String head) {
        HEAD = head;
        Utils.writeContents(HEAD_DIR, HEAD);
    }

    public static String getHEAD() {
        return Utils.readContentsAsString(HEAD_DIR);
    }
}
