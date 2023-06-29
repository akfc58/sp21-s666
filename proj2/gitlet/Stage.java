package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

/** represents the staging area of gitlet.
 * includes files staged to be added and
 * files staged to be removed.
 */
public class Stage implements Serializable {

    /** Folder that contains the two sets of map of
     * staged files to be 1.added; 2.removed.
     */
    private static Set<Map<String, String>> toAdd = new HashSet<>();
    private static Set<Map<String, String>> toRemove = new HashSet<>();
    private final static File stage = Utils.join(Repository.GITLET_DIR, "stage");

    public static void addStage(String fileName, String blobName) {
        Map<String, String> newAdd = new TreeMap<String, String>();
        newAdd.put(fileName, blobName);
        toAdd.add(newAdd);
    }

    public static void saveStage() {
        Utils.writeObject(stage, Stage.class);
        for (Map m : toAdd) {
            System.out.println(m);
        }
    }

    public static void updateStage(String key, String val) {
        Map<String, String> newAdd = new TreeMap<String, String>();
        newAdd.put(key, val);
        toAdd.add(newAdd);
    }

    public static void clear() {
        //TODO: clear stage area after a commit.
        toAdd = new HashSet<>();
        toRemove = new HashSet<>();
        Utils.writeObject(stage, Stage.class);
    }

    public static Set<Map<String, String>> getToAdd() {
        return toAdd;
    }
}
