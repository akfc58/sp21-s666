package gitlet;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Represents a gitlet repository.
 * this Class handles all coordination among the other classes.
 *
 * @author dongliang
 */
public class Repository {

    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /**
     * The .gitlet directory.
     */
    public static final File GITLET_DIR = Utils.join(CWD, ".gitlet");
    /**
     * The blobs dicrectory.
     */
    public static final File GITLET_BLOBS = Utils.join(GITLET_DIR, "blobs");
    /**
     * The commits directory.
     */
    public static final File GITLET_COMMITS = Utils.join(GITLET_DIR, "commits");
    public static final File GITLET_REFS = Utils.join(GITLET_DIR, "refs");
    public static final File ACTIVE_DIR = Utils.join(Repository.GITLET_REFS, "active");
    /**
     * The HEAD.
     */
    public static final File GITLET_HEAD = Utils.join(GITLET_DIR, "HEAD");


    private static SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z");


    /**
     * Initialize the .gitlet folder to contain gitlet opreations.
     * Structure as follows:
     * .gitlet/ -- top level folder for all persistent data in your lab12 folder
     * - stage -- a file containing all staged files' map of added/removed name and its blob.
     * - blobs/ -- different version of files. name in sha-1.
     * - commits/ -- commits. name in sha-1.
     * - refs/
     * - active/
     * - otherbranches
     */
    public static void init() {
        if (GITLET_DIR.exists()) {
            System.out.println("A Gitlet version-control system already"
                    + " exists in the current directory.");
            System.exit(0);
        }
        makeDirectories();
        Commit initalCommit = new Commit();
        // constructor without parameters is specific for the initial commit.
        writeCommit(initalCommit);
    }

    /**
     * helper function of init. creates necessary folders.
     */
    private static void makeDirectories() {
        GITLET_DIR.mkdir();
        GITLET_BLOBS.mkdir();
        GITLET_COMMITS.mkdir();
        GITLET_REFS.mkdir();
        ACTIVE_DIR.mkdir();
    }

    /**
     * write commit to .gitlet/commits.
     * because HEAD and active branch(i.e. master)changes iff a commit is written.
     * so change HEAD and active branch at the same time.
     */
    private static void writeCommit(Commit c) {
        String commitSha1 = c.sha1();
        File commitFile = Utils.join(GITLET_COMMITS, commitSha1);
        Utils.writeObject(commitFile, c);
        Refs.saveHEAD(commitSha1);
        Refs.moveActiveBranch(commitSha1);
    }

    /**
     * add FILENAME to add/remove one file at a time to the staging area.
     * Includes: turn file into a blob; update stage; save stage.
     */
    public static void add(String fileName) {
        if (!GITLET_DIR.exists()) {
            System.out.println("No .gitlet! please check.");
            System.exit(0);
        }
        List<String> allPlainFiles = Utils.plainFilenamesIn(CWD);
        // make sure that the asked file exists.
        if (allPlainFiles != null && allPlainFiles.contains(fileName)) {
            String fileContent = Utils.readContentsAsString(Utils.join(CWD, fileName));
            String fileBlob = Utils.sha1(fileContent);
            Stage e = new Stage();
            // if a file is commited in HEAD, changed and added again,
            // it should not be staged since nothing changed.
            if (jugdeSameFileInHEAD(fileName, fileBlob)) {
                // add is invaild, delete corresponding item in stage toAdd and toRemove.
                e.deleteItemInToAdd(fileName);
                e.deleteItemInToRemove(fileName);
            } else {
                File blob = Utils.join(GITLET_BLOBS, fileBlob);
                Utils.writeContents(blob, fileContent);
                e.addStage(fileName, fileBlob);
            }
        } else {
            System.out.println("File does not exist.");
            System.exit(0);
        }
    }

    /**
     * Helper of add. check if FILENAME is in current HEAD commit.
     * iff true, delete FILENAME in stage area, avoiding duplicated commit of same file
     * Returns true if:1. a file is committed, changed, added to stage area
     * and changed back, added again.
     * 2. a file is committed, removed (added to toRemove in stage), added back again.
     * these two situations have one thing in common, which is the current HEAD commit
     * must have the FILENAME in it with same sha1 value as the newly added FILENAME.
     * Thus, this function returns true when add is invaild.
     */
    private static boolean jugdeSameFileInHEAD(String fileName, String fileBlob) {
        String currHEAD = Utils.readContentsAsString(GITLET_HEAD);
        File currCommit = Utils.join(GITLET_COMMITS, currHEAD);
        Commit currC = Utils.readObject(currCommit, Commit.class);
        Map<String, String> m = currC.getCommitContent();
        return m != null && m.containsKey(fileName) && m.get(fileName).equals(fileBlob);
    }

    public static void rm(String fileName) {
        Stage e = new Stage();
        Commit currCommit = Utils.readObject(Utils.join(GITLET_COMMITS,
                Refs.getHEAD()), Commit.class);
        Map<String, String> currCommitContent = currCommit.getCommitContent();
        File f = Utils.join(CWD, fileName);
        if (e.getToAdd().containsKey(fileName)) {
            e.deleteItemInToAdd(fileName);
            // file in stage area is not tracked, so do not delete it.
        } else if (currCommitContent != null && currCommitContent.containsKey(fileName)) {
            String fileContent = "";
            if (f.exists()) {
                fileContent = Utils.readContentsAsString(f);
            }
            String fileBlob = Utils.sha1(fileContent);
            e.removeStage(fileName, fileBlob);
            Utils.restrictedDelete(Utils.join(CWD, fileName));
            // add fileName to toRemove. Deal with actual removal in Commit constructor.
        } else {
            // Failure case stick to spec.
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
    }

    /**
     * Receives commit message, make a new commit according to stage area and old commit.
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
        writeCommit(c);
        // every commit changes HEAD, so deal with write commit in Repo, NOT in commit class.
        e.clear(); // clear stage area after commit.
    }

    /**
     * Checkout File that is in current HEAD, replace it in CWD.
     * Get current commit using HEAD in .gitlet.
     */
    public static void checkoutFile(String fileName) {
        String currHEAD = Refs.getHEAD();
        checkoutFileHelper(currHEAD, fileName);
    }

    /**
     * Checkout File that is in arbitrarly commit, replace it in CWD.
     */
    public static void checkoutCommitFile(String commitBlob, String fileName) {
        if (commitBlob.length() < Utils.UID_LENGTH) {
            commitBlob = shortCommitIDHelper(commitBlob);
        }
        checkoutFileHelper(commitBlob, fileName);
    }

    public static String shortCommitIDHelper(String shortID) {
        List<String> commits = Utils.plainFilenamesIn(GITLET_COMMITS);
        if (commits != null) {
            for (String each : commits) {
                if (each.contains(shortID)) {
                    return each;
                }
            }
        }
        return "NoSuchCommit";
    }

    /**
     * checkout file from current commit and arbitrarly commit are basically the same.
     */
    private static void checkoutFileHelper(String checkoutCommitSha1, String fileName) {
        List<String> l = Utils.plainFilenamesIn(GITLET_COMMITS);
        File checkoutCommit = Utils.join(GITLET_COMMITS, checkoutCommitSha1);
        if (l != null && !l.contains(checkoutCommitSha1)) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        Commit c = Utils.readObject(checkoutCommit, Commit.class);
        Map<String, String> content = c.getCommitContent();
        if (content.containsKey(fileName)) {
            String blobName = content.get(fileName);
            File inFile = Utils.join(GITLET_BLOBS, blobName);
            File outFile = Utils.join(CWD, fileName);
            Utils.restrictedDelete(outFile);
            // delete the target file in CWD. if it is not there, won't error.
            Utils.writeContents(outFile, Utils.readContentsAsString(inFile));
        } else {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
    }

    /**
     * Checkout branch iff:
     * 1. it is not the active branch
     * 2. every file in cwd is tracked in current commit
     * 3. the checked-out branch actually exists.
     * then, check out all files in BRANCH commit while delete
     * everything tracked in current commit.
     * change HEAD, active branch, and other branch.
     * WARNING: 1. files that's not tracked won't be affected.
     * 2. files that's just staged to add will be set to untracked
     * since the stage area is cleared.
     */
    public static void checkoutBranch(String branch) {
        if (branch.equals(Refs.getActiveBranchName())) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        String currCommitID = Refs.getHEAD();
        if (ifOverWrite(currCommitID)) {
            System.out.println("There is an untracked file in the way;"
                    + " delete it, or add and commit it first.");
            System.exit(0);
        }
        List<String> branchList = Utils.plainFilenamesIn(GITLET_REFS);
        if (branchList == null || !branchList.contains(branch)) {
            System.out.println("No such branch exists.");
            System.exit(0);
            // if there's no other branches, then checkout branch does not exist.
        }
        String checkedOutCommitID = Refs.getBranch(branch);
        manipulateCWD(currCommitID, checkedOutCommitID);
        Refs.changeActiveBranch(branch);
        Stage e = new Stage();
        e.clear();
    }

    /**
     * iff there's a file in CWD that's either:
     * not in commit COMMITID or staged to add or
     * staged to remove but changed without gitlet's knowledge.
     * return list of untracked files.
     */
    private static List<String> untrackedFiles(String commitID) {
        File commitFile = Utils.join(GITLET_COMMITS, commitID);
        Commit c = Utils.readObject(commitFile, Commit.class);
        Map<String, String> content = c.getCommitContent();
        List<String> cwdFiles = Utils.plainFilenamesIn(CWD);
        List<String> untrackedFiles = new ArrayList<>();

        Stage e = new Stage();
        Map<String, String> toAdd = e.getToAdd();
        Map<String, String> toRemove = e.getToRemove();

        if (cwdFiles == null) {
            return untrackedFiles;
        }
        for (String each : cwdFiles) {
            // A file is tracked when:
            // 1. it's name is in current commit OR
            // 2. in staged toAdd.
            // 3. in staged toRemove, and sha1 equals.
            // important: a file tracked but have different content
            // is considered MODIFIED!
            boolean tracked = false;
            if (content != null && content.containsKey(each)) {
                tracked = true;
            }
            if (toAdd != null && toAdd.containsKey(each)) {
                tracked = true;
            }
            File eachDir = Utils.join(CWD, each);
            if (toRemove != null && toRemove.containsKey(each)
                    && toRemove.get(each).equals(
                    Utils.sha1(Utils.readContentsAsString(eachDir)))) {
                tracked = true;
            }
            if (!tracked) {
                untrackedFiles.add(each);
            }
        }
        return untrackedFiles;
    }

    /**
     * A branch checkout or commit reset is considered overwrite if:
     * 1. the checkout commit is empty, but there's something untracked
     * in the CWD.
     * 2. there is something untracked would have been overwritten by
     * checked-out commit.
     */
    private static boolean ifOverWrite(String commitID) {
        Commit c = Utils.readObject(Utils.join(GITLET_COMMITS, commitID), Commit.class);
        Map<String, String> content = c.getCommitContent();
        List<String> untracked = untrackedFiles(Refs.getHEAD());
        if (untracked.isEmpty()) {
            return false;
        }
        for (String eachUntracked : untracked) {
            if (content != null) {
                if (content.containsKey(eachUntracked)) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper for status listing what's modified. this is for the kind
     * of "deleted." which are:
     * Staged for addition, but deleted in the working directory;
     * Not staged for removal, but tracked in the current commit and
     * deleted from the working directory.
     */
    public static List<String> deleted() {
        List<String> res = new ArrayList<>();
        List<String> currFiles = Utils.plainFilenamesIn(CWD);
        String currCommitID = Refs.getHEAD();
        Commit currCommit = Utils.readObject(Utils.join(
                GITLET_COMMITS, currCommitID), Commit.class);
        Map<String, String> currContent = currCommit.getCommitContent();
        Stage e = new Stage();
        Map<String, String> toAdd = e.getToAdd();
        Map<String, String> toRemove = e.getToRemove();
        if (currContent != null) {
            for (String trackedFile : currContent.keySet()) {
                if ((currFiles != null && !currFiles.contains(trackedFile))
                        && (toRemove != null
                        && !toRemove.containsKey(trackedFile))) {
                    res.add(trackedFile);
                }
            }
        }
        if (toAdd != null) {
            for (String stagedFile : toAdd.keySet()) {
                if (currFiles != null && !currFiles.contains(stagedFile)) {
                    res.add(stagedFile);
                }
            }
        }
        return res;
    }


    /**
     * Helper for status listing what's modified. this is for the kind
     * of "modified" which are:
     * Tracked in the current commit, changed in the working directory, but not staged; or
     * Staged for addition, but with different contents than in the working directory; or
     */
    public static List<String> modified() {
        List<String> res = new ArrayList<>();
        List<String> currFiles = Utils.plainFilenamesIn(CWD);
        String currCommitID = Refs.getHEAD();
        Commit currCommit = Utils.readObject(Utils.join(
                GITLET_COMMITS, currCommitID), Commit.class);
        Map<String, String> currContent = currCommit.getCommitContent();
        Stage e = new Stage();
        Map<String, String> toAdd = e.getToAdd();
        if (currContent != null) {
            for (String trackedFile : currContent.keySet()) {
                // a file is considered modified
                // 1. in currentcommit
                // 2. in CWD
                // 3. changed(sha1 don't match)
                // 4. not staged for addtion
                if ((currFiles != null
                        && currFiles.contains(trackedFile))
                        && (!currContent.get(trackedFile).equals(
                                Utils.sha1(Utils.readContentsAsString(
                                        Utils.join(CWD, trackedFile)))))
                        && (toAdd != null
                        && !toAdd.containsKey(trackedFile))) {
                    res.add(trackedFile);
                }
            }
        }
        if (toAdd != null) {
            for (String stagedFile : toAdd.keySet()) {
                // a file is considered modified
                // 1. staged for add
                // 2. but changed(sha1 don't match
                if (currFiles != null
                        && currFiles.contains(stagedFile)
                        && (!toAdd.get(
                                stagedFile).equals(
                                        Utils.sha1(
                                                Utils.readContentsAsString(
                                                        Utils.join(
                                                                CWD, stagedFile)))))) {
                    res.add(stagedFile);
                }
            }
        }
        return res;
    }

    /**
     * delete every thing in CWD that is in current commit, and check out
     * everything in checked-out commit.
     */
    private static void manipulateCWD(String currID, String checkedOutID) {
        File currCommitFile = Utils.join(GITLET_COMMITS, currID);
        Commit currCommit = Utils.readObject(currCommitFile, Commit.class);
        Map<String, String> currContent = currCommit.getCommitContent();
        // delete files that's in current commit in CWD.
        if (currContent != null) {
            for (String eachCurrCommitFileName : currContent.keySet()) {
                Utils.restrictedDelete(Utils.join(CWD, eachCurrCommitFileName));
            }
        }
        File checkedOutFile = Utils.join(GITLET_COMMITS, checkedOutID);
        Commit checkedOutCommit = Utils.readObject(checkedOutFile, Commit.class);
        Map<String, String> checkedOutContent = checkedOutCommit.getCommitContent();
        // restore files in checked-out commit.
        if (checkedOutContent != null) {
            for (String eachCheckedOutFile : checkedOutContent.keySet()) {
                // get blob for target file.
                String blobName = checkedOutContent.get(eachCheckedOutFile);
                // get file content(stored in blob as string).
                String blobFileContent = Utils.readContentsAsString(
                        Utils.join(GITLET_BLOBS, blobName));
                // write content to CWD with name stored in checked-out commit.
                Utils.writeContents(Utils.join(CWD, eachCheckedOutFile),
                        blobFileContent);
            }
        }
    }


    public static void reset(String commitID) {
        if (commitID.length() < Utils.UID_LENGTH) {
            commitID = shortCommitIDHelper(commitID);
        }
        String currCommitID = Refs.getHEAD();
        List<String> commitList = Utils.plainFilenamesIn(GITLET_COMMITS);
        if (commitList == null || !commitList.contains(commitID)) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        if (ifOverWrite(commitID)) {
            System.out.println("There is an untracked file in the way;"
                    + " delete it, or add and commit it first.");
            System.exit(0);
        }
        manipulateCWD(currCommitID, commitID);
        Refs.saveHEAD(commitID);
        Refs.moveActiveBranch(commitID);
        Stage e = new Stage();
        e.clear();
    }

    /**
     * Starting at the current head commit, display information about each
     * commit backwards along the commit tree until the initial commit,
     * following the first parent commit links, ignoring any second parents
     * found in merge commits.
     */
    public static void log() {
        String currSha1 = Utils.readContentsAsString(GITLET_HEAD);
        File currCommit = Utils.join(GITLET_COMMITS, currSha1);
        Commit c = Utils.readObject(currCommit, Commit.class);
        String content;
        while (c.getParent() != null) {
            Date d = c.getTimestamp();
            content = "===\n" + "commit " + currSha1 + "\n"
                    + "Date: " + formatter.format(d) + "\n" + c.getMessage() + "\n\n";
            System.out.print(content);
            currSha1 = c.getParent();
            currCommit = Utils.join(GITLET_COMMITS, currSha1);
            c = Utils.readObject(currCommit, Commit.class);
        }
        content = "===\n" + "commit " + currSha1 + "\n"
                + "Date: " + formatter.format(c.getTimestamp()) + "\n" + c.getMessage() + "\n\n";
        System.out.print(content);
    }

    /**
     * Display all commits ever made. Ignore order.
     */
    public static void globalLog() {
        List<String> allCommit = Utils.plainFilenamesIn(GITLET_COMMITS);
        if (allCommit != null) {
            for (String commitSha1 : allCommit) {
                File f = Utils.join(GITLET_COMMITS, commitSha1);
                Commit c = Utils.readObject(f, Commit.class);
                Date d = c.getTimestamp();
                String content = "===\n" + "commit " + commitSha1 + "\n"
                        + "Date: " + formatter.format(d) + "\n" + c.getMessage() + "\n\n";
                System.out.print(content);
            }
        }
    }

    /**
     * Find and print out commit IDs that contains targetMessage.
     */
    public static void find(String targetMessage) {
        List<String> allCommit = Utils.plainFilenamesIn(GITLET_COMMITS);
        boolean haveResult = false;
        if (allCommit != null) {
            for (String commitSha1 : allCommit) {
                File f = Utils.join(GITLET_COMMITS, commitSha1);
                Commit c = Utils.readObject(f, Commit.class);
                String commitMessage = c.getMessage();
                if (targetMessage.equals(commitMessage)) {
                    System.out.println(commitSha1);
                    haveResult = true;
                }
            }
        }
        if (!haveResult) {
            System.out.println("Found no commit with that message.");
        }
    }

    public static void branch(String name) {
        String currCommit = Refs.getHEAD();
        // new branch must be at the HEAD commit when it is created.
        Refs.setBranch(name, currCommit);
    }

    public static void rmBranch(String name) {
        Refs.removeBranch(name);
    }

    /**
     * Displays all gitlet information in given format.
     */
    public static void status() {
        List<String> active = Utils.plainFilenamesIn(Utils.join(GITLET_REFS, "active"));
        if (active == null) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        List<String> otherBranch = Utils.plainFilenamesIn(GITLET_REFS);
        if (otherBranch != null) {
            Collections.sort(otherBranch);
        }
        Stage e = new Stage();
        List<String> toAdd = new ArrayList<>(e.getToAdd().keySet());
        java.util.Collections.sort(toAdd);
        List<String> toRemove = new ArrayList<>(e.getToRemove().keySet());
        java.util.Collections.sort(toRemove);
        System.out.println("=== Branches ===");
        System.out.println("*" + active.get(0));
        // can never be null.
        if (otherBranch != null) {
            for (String branch : otherBranch) {
                System.out.println(branch);
            }
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        for (String stagedFile : toAdd) {
            System.out.println(stagedFile);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        for (String removedFile : toRemove) {
            System.out.println(removedFile);
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        List<String> deletedFiles = deleted();
        List<String> modifiedFiles = modified();
        for (String file: deletedFiles) {
            System.out.println(file + " (deleted)");
        }
        for (String file: modifiedFiles) {
            System.out.println(file + " (modified)");
        }
        System.out.println();
        System.out.println("=== Untracked Files ===");
        for (String untracked : untrackedFiles(Refs.getHEAD())) {
            System.out.println(untracked);
        }
        System.out.println();
    }

    /**
     * merge givenBranch into currentBranch(HEAD).
     */
    public static void merge(String givenBranch) {

        // check If an untracked file in the current commit would be
        // overwritten or deleted by the merge.
        String currCommitID = Refs.getHEAD();
        if (!untrackedFiles(currCommitID).isEmpty()) {
            System.out.println("There is an untracked file in the way;"
                    + " delete it, or add and commit it first.");
            System.exit(0);
        }
        // check If there are staged additions or removals present.
        Stage e = new Stage();
        if (!e.getToAdd().isEmpty() || !e.getToRemove().isEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        // check If a branch with the given name does not exist.
        List<String> branchList = Utils.plainFilenamesIn(GITLET_REFS);
        String activeBranch = Refs.getActiveBranchName();
        if (branchList == null || !branchList.contains(givenBranch)
                && !activeBranch.equals(givenBranch)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        // check If attempting to merge a branch with itself.
        String currHEAD = Refs.getHEAD();
        if (currHEAD.equals(Refs.getBranch(givenBranch))) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }





        // 3 items of type commit: ancestorID, currentHEAD, givenBranchID.
        String ancestorID = getLastAnsector(givenBranch);
        String givenBranchID = Refs.getBranch(givenBranch);

        Map<String, String> ancestorMap = getBlobMapFromCommit(ancestorID);
        Map<String, String> currentHEADMap = getBlobMapFromCommit(currHEAD);
        Map<String, String> givenBranchMap = getBlobMapFromCommit(givenBranchID);

        for (String key : currentHEADMap.keySet()) {
            e.addStage(key, currentHEADMap.get(key));
        }

        for (String key : givenBranchMap.keySet()) {
            String blobName = givenBranchMap.get(key);
            File inFile = Utils.join(GITLET_BLOBS, blobName);
            File outFile = Utils.join(CWD, key);
            Utils.restrictedDelete(outFile);
            // delete the target file in CWD. if it is not there, won't error.
            Utils.writeContents(outFile, Utils.readContentsAsString(inFile));
            e.addStage(key, givenBranchMap.get(key));

        }
        for (String key : ancestorMap.keySet()) {
            e.deleteItemInToAdd(key);
            e.removeStage(key, ancestorMap.get(key));
            Utils.restrictedDelete(Utils.join(CWD, key));
        }
        for (String key : givenBranchMap.keySet()) {
            if (currentHEADMap.containsKey(key)) {
                System.out.println("Encountered a merge conflict");
                File currentFile = Utils.join(GITLET_BLOBS, currentHEADMap.get(key));
                String currentContent = Utils.readContentsAsString(currentFile);
                File givenFile = Utils.join(GITLET_BLOBS, givenBranchMap.get(key));
                String givenContent = Utils.readContentsAsString(givenFile);
                File outfile = Utils.join(CWD, key);
                String conflictContent = null;
                conflictContent = "<<<<<<< HEAD\n" + currentContent
                        + "=======\n" + givenContent + ">>>>>>>\n";
                Utils.writeContents(outfile, conflictContent);
                e.addStage(key, Utils.sha1(conflictContent));
                e.deleteItemInToRemove(key);
            }
        }
        commit("Merged " + givenBranch + " into " + Refs.getActiveBranchName() + ".");
    }








    /**
     * find the split spot. Might be the inital commit!
     * MUST use tree map to keep the order.
     */
    private static String getLastAnsector(String givenBranch) {
        Map<Integer, String> ansectorsOfActiveBranch = getAllAnsectors(Refs.getActiveBranchName());
        Map<Integer, String> ansectorsOfGivenBranch = getAllAnsectors(givenBranch);
        for (String id1 : ansectorsOfActiveBranch.values()) {
            for (String id2 : ansectorsOfGivenBranch.values()) {
                if (id1.equals(id2)) {
                    return id1;
                }
            }
        }
        return "no ansector.";
    }

    /**
     * get All ancestors of a branch b in the form of a Map form depth
     * to ancestor's commitID.
     */
    private static Map<Integer, String> getAllAnsectors(String branch) {
        Map<Integer, String> ancestors = new TreeMap<>();

        String targetCommitID;
        targetCommitID = Refs.getBranch(branch);
        int depth = 0;

        ancestors.put(depth, targetCommitID);
        Commit targetCommit;
        File commitFile;
        commitFile = Utils.join(GITLET_COMMITS, targetCommitID);
        targetCommit = Utils.readObject(commitFile, Commit.class);
        while (!targetCommit.getMessage().equals("initial commit")) {
            targetCommitID = targetCommit.getParent();
            commitFile = Utils.join(GITLET_COMMITS, targetCommitID);
            targetCommit = Utils.readObject(commitFile, Commit.class);
            depth += 1;
            ancestors.put(depth, targetCommitID);
        }
        return ancestors;
    }

    /**
     * get all file blobs from a given commit.
     */
    private static Map<String, String> getBlobMapFromCommit(String commitID) {
        // get the map of blobs
        Commit c = Utils.readObject(Utils.join(GITLET_COMMITS, commitID), Commit.class);
        return (Map<String, String>) c.getCommitContent();
    }
}
