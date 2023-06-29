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
    private Map<String, String> toAdd = new HashMap<>();
    private Map<String, String> toRemove = new HashMap<>();
    private final static File stageDir = Utils.join(Repository.GITLET_DIR, "stage");

    /** new runtime stage instance always keep track of  what's persisted before. */
    public Stage() {
        readStage();
    }

    public void addStage(String key, String val) {
        toAdd.put(key, val);
        saveStage();
    }

    public void removeStage(String key, String val) {
        //TODO
    }

    public void clear() {
        //TODO: clear stage area after a commit.
        toAdd = new HashMap<>();
        toRemove = new HashMap<>();
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
    }


    public Map<String, String> getToAdd() {
        return toAdd;
    }
    public Map<String, String> getToRemove() {
        return toRemove;
    }

}
