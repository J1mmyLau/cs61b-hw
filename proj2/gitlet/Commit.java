package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     * commitID: the unique identifier of the commit
     * parentID: the unique identifier of the parent commit
     * message: the message of the commit
     * date: the date of the commit
     * files: the files that are staged for the commit
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    static final File CWD = new File(System.getProperty("user.dir"));
    static final File GITLET_DIR = join(CWD, ".gitlet");
    private String commitID;
    private String parentID;
    private Date date;
    private String message;
    private Set<String> files;
    private boolean isMerged;
    private String[] parentsID;
    private String branch;
    private List<String> blobs;
    private Map<String, String> fileToBlob;


    /* TODO: fill in the rest of this class. */
    public void writeBlob() {
        for (String fileName : this.getFiles()) {
            String sha1 = sha1(readContentsAsString(join(CWD, fileName)));
            File file = join(CWD, fileName);
            blobs.add(sha1);
            fileToBlob.put(fileName, sha1);
            if(!join(GITLET_DIR, "blobs", sha1).exists()){
                writeObject(join(GITLET_DIR, "blobs", sha1), readContentsAsString(file));
            }
        }
    }

    public String getBlob(String fileName) {
        if(!fileToBlob.containsKey(fileName)) {
            return null;
        }
        return fileToBlob.get(fileName);
    }

    public Commit(String message, String parentID, Set<String> files, boolean isMerged, String branch) {
        this.files = files;
        this.message = message;
        this.parentID = parentID;
        this.date = new Date();
        this.commitID = Utils.sha1(flattenFile(), parentID, message, getTimestamp());
        this.isMerged = isMerged;
        this.branch = branch;
        this.blobs = new ArrayList<String>();
        this.fileToBlob = new HashMap<String, String>();
    }

    public String getBranch() {
        return branch;
    }

    public boolean isMerged() {
        return isMerged;
    }

    public void setParentsID(String[] parentsID) {
        this.parentsID = parentsID;
    }

    public String[] getParentsID() {
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

    public String getParentID() {
        return parentID;
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
