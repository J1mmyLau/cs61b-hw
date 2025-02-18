package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Utils.*;

public class Commit implements Serializable {
    /**
     * commitID: the unique identifier of the commit
     * parentID: the unique identifier of the parent commit
     * message: the message of the commit
     * date: the date of the commit
     * files: the files that are staged for the commit
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /**
     * The message of this Commit.
     */
    static final File CWD = new File(System.getProperty("user.dir"));
    static final File GITLET_DIR = join(CWD, ".gitlet");
    private final String commitID;
    private final ArrayList<String> parentsID;
    private ArrayList<String> SonID;
    private final Date date;
    private final String message;
    private final Set<String> files;
    private final boolean isMerged;
    private final String branch;
    private final List<String> blobs;
    private final Map<String, String> fileToBlob;
    private int depth;

    public Commit(String message, ArrayList<String> parentID, Set<String> files, boolean isMerged, String branch) {
        this.files = files;
        this.message = message;
        this.parentsID = parentID;
        this.date = new Date();
        this.commitID = Utils.sha1(flattenFile(), parentID.get(0), message, getTimestamp());
        this.isMerged = isMerged;
        this.branch = branch;
        this.blobs = new ArrayList<String>();
        this.fileToBlob = new HashMap<String, String>();
    }

    public void addParent(String parentID) {
        parentsID.add(parentID);
    }

    public void addSon(String sonID) {
        SonID.add(sonID);
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void writeBlob() {
        for (String fileName : this.getFiles()) {
            String sha1 = sha1(readContentsAsString(join(CWD, fileName)));
            File file = join(CWD, fileName);
            blobs.add(sha1);
            fileToBlob.put(fileName, sha1);
            if (!join(GITLET_DIR, "blobs", sha1).exists()) {
                writeObject(join(GITLET_DIR, "blobs", sha1), readContentsAsString(file));
            }
        }
    }

    public Boolean containsBlob(String sha1) {
        return blobs.contains(sha1);
    }
    public String getBlob(String fileName) {
        if (!fileToBlob.containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
            return null;
        }
        return fileToBlob.get(fileName);
    }

    public String getBranch() {
        return branch;
    }

    public boolean isMerged() {
        return isMerged;
    }


    public ArrayList<String> getParentsID() {
        return parentsID;
    }

    public String getCommitID() {
        return commitID;
    }

    public String flattenFile() {
        String result = "";
        for (String file : files) {
            result += file;
        }
        return result;
    }

    public Date getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public Set<String> getFiles() {
        return files;
    }

    public String getTimestamp() {
        return new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z").format(date);
    }


}
