package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;
import gitlet.Utils.*;

import static gitlet.Utils.*;

/** represents the staging area of gitlet.
 * includes files staged to be added and
 * files staged to be removed.
 */
public class Stage implements Serializable {

    /** Folder that contains the two sets of map of
     * staged files to be 1.added; 2.removed.
     */
    private static Set<Map<String, File>> toAdd = new HashSet<>();
    private static Set<Map<String, File>> toRemove = new HashSet<>();

    public static void addStage(String fileName, File blob) {
        Map<String, File> newAdd = new TreeMap<String, File>();
        newAdd.put(fileName, blob);
        toAdd.add(newAdd);
        for (Map e : toAdd) {
            System.out.println(e);
        }
    }

    public static void saveStage() {
        File stage = join(Repository.GITLET_DIR, "stage");
        writeObject(stage, serialize(Stage.class));
    }

    public static void clear() {
        //TODO: clear stage area after a commit.
    }
}
