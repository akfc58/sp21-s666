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
    private Set<Map<String, String>> toAdd = new HashSet<>();
    private Set<Map<String, String>> toRemove = new HashSet<>();
    private final static File stageDir = Utils.join(Repository.GITLET_DIR, "stage");

    /** new runtime stage instance always keep track of  what's persisted before. */
    public Stage() {
        readStage();
    }

    public void addStage(String key, String val) {
        Map<String, String> newAdd = new TreeMap<String, String>();
        newAdd.put(key, val);
        toAdd.add(newAdd);
        saveStage();
    }

    public void removeStage(String key, String val) {
        //TODO
    }

    public void clear() {
        //TODO: clear stage area after a commit.
        toAdd = new HashSet<>();
        toRemove = new HashSet<>();
        Utils.writeObject(stageDir, this);
    }

    public  boolean haveStage() {
        if (toAdd.isEmpty() && toRemove.isEmpty()) {
            return false;
        }
        return true;
    }

    private void readStage() {
        if (stageDir.exists()) {
            Stage oldStage = Utils.readObject(stageDir, Stage.class);
            this.toAdd = oldStage.toAdd;
            this.toRemove = oldStage.toRemove;
        }
    }



    private  void saveStage() {
        Utils.writeObject(stageDir, this);
        for (Map m : toAdd) {
            System.out.println(m);
        }
    }


    public Set<Map<String, String>> getToAdd() {
        return toAdd;
    }
    public Set<Map<String, String>> getToRemove() {
        return toRemove;
    }

}
