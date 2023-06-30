package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.*;

/** represents the staging area of gitlet.
 * includes files staged to be added and
 * files staged to be removed.
 */
public class Stage implements Serializable {

    private static final File STAGE_DIR = Utils.join(Repository.GITLET_DIR, "stage");
    private Map<String, String> toAdd = new HashMap<>();
    private Map<String, String> toRemove = new HashMap<>();

    /** new runtime stage instance always keep track of  what's persisted before. */
    public Stage() {
        readStage();
    }

    public void addStage(String key, String val) {

            // if the new KEY is already there and sha1 is the same, remove it from
            // toAdd as there's no need to stage it.
        toAdd.put(key, val);
        saveStage();
    }

    public void removeStage(String key, String val) {
        //TODO
    }

    public void clear() {
        toAdd = new HashMap<>();
        toRemove = new HashMap<>();
        Utils.writeObject(STAGE_DIR, this);
    }

    public void deleteItem(String key) {
        //TODO delete item in toAdd.
        readStage();
        if (toAdd.containsKey(key)) {
            toAdd.remove(key);
        }
        saveStage();
    }

    public  boolean haveStage() {
        if (toAdd.isEmpty() && toRemove.isEmpty()) {
            return false;
        }
        return true;
    }

    private void readStage() {
        if (STAGE_DIR.exists()) {
            Stage oldStage = Utils.readObject(STAGE_DIR, Stage.class);
            this.toAdd = oldStage.toAdd;
            this.toRemove = oldStage.toRemove;
        }
    }



    private  void saveStage() {
        Utils.writeObject(STAGE_DIR, this);
    }


    public Map<String, String> getToAdd() {
        return toAdd;
    }
    public Map<String, String> getToRemove() {
        return toRemove;
    }

}
