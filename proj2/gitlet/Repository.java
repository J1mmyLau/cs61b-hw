package gitlet;


import org.junit.Test;

import java.io.File;
import java.io.Serializable;
import java.sql.Array;
import java.util.*;

import static gitlet.Utils.*;


public class Repository {

    /**
     * TODO: add instance variables here.
     *
     * CWD: the current working directory
     * GITLET_DIR: the .gitlet directory
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    private List<String> removedFiles;
    private List<String> stagedFiles;
    private String currentBranch;

    public Repository() {
        new File(GITLET_DIR, "removing").mkdir();
        new File(GITLET_DIR, "staging").mkdir();
        if(join(GITLET_DIR, "branch").exists()){
            currentBranch = readContentsAsString(join(GITLET_DIR, "branch"));
        }
        else{
            currentBranch = "master";
        }
        removedFiles = new ArrayList<String>();
        if(plainFilenamesIn(join(GITLET_DIR, "removing")) != null){
            for(String file : plainFilenamesIn(join(GITLET_DIR, "removing"))){
                removedFiles.add(file);
            }
        }
        if(plainFilenamesIn(join(GITLET_DIR, "staging")) == null){
            return;
        }
        stagedFiles = new ArrayList<String>();
        for(String file : plainFilenamesIn(join(GITLET_DIR, "staging"))){
            stagedFiles.add(file);
        }
    }

    public void init(){
        if(GITLET_DIR.exists()){
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        }
        GITLET_DIR.mkdir();
        new File(GITLET_DIR, "commits").mkdir();
        new File(GITLET_DIR, "removing").mkdir();
        new File(GITLET_DIR, "staging").mkdir();
        new File(GITLET_DIR, "staging").mkdir();
        new File(GITLET_DIR, "blobs").mkdir();
        new File(GITLET_DIR, "refs").mkdir();
        new File(GITLET_DIR, "refs/heads").mkdir();
        new File(GITLET_DIR, "refs/tags").mkdir();
        ArrayList<String> parents = new ArrayList<>();
        parents.add("");
        Commit initialCommit = new Commit("initial commit", parents, new HashSet<String>(), false, "master");
        writeObject(join(GITLET_DIR, "commits", initialCommit.getCommitID()), (Serializable) initialCommit);
        writeObject(join(GITLET_DIR, "refs/heads/master"), initialCommit.getCommitID());
        writeContents(join(GITLET_DIR,"branch"), "master");
    }
    /* TODO: fill in the rest of this class. */
    public void add(String fileName) {
        File file = join(CWD, fileName);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            return;
        }
        stagedFiles.add(fileName);
        Commit currentCommit = readObject(join(GITLET_DIR, "commits", readObject(join(GITLET_DIR, "refs/heads/master"), String.class)), Commit.class);
        if (currentCommit.getFiles().contains(fileName)) {
            if (currentCommit.getBlob(fileName).equals(sha1(readContentsAsString(file)))) {
                return;
            }
        }
        File stagingArea = join(GITLET_DIR, "staging", fileName);
        writeContents(stagingArea, readContentsAsString(file));

    }

    public void commit(String message, Boolean isMerged) {
        if(message.equals("")){
            System.out.println("Please enter a commit message.");
            return;
        }
        File stagingArea = join(GITLET_DIR, "staging");
        File removingArea = join(GITLET_DIR, "removing");
        if (plainFilenamesIn(stagingArea).isEmpty() && plainFilenamesIn(removingArea).isEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        }
        String parentID = readObject(join(GITLET_DIR, "refs/heads/" + currentBranch), String.class);
        Commit parent = readObject(join(GITLET_DIR, "commits", parentID), Commit.class);
        Set<String> files = new HashSet<String>();
        for(String file : parent.getFiles()){
            files.add(file);
        }
        for(String file : plainFilenamesIn(stagingArea)){
            files.add(file);
        }
        for(String file : removedFiles){
            files.remove(file);
            for(String fileName : plainFilenamesIn(join(GITLET_DIR, "removing"))){
                join(GITLET_DIR, "removing", fileName).delete();
            }
        }
        ArrayList<String> parentsID = new ArrayList<>();
        parentsID.add(parentID);
        Commit newCommit = new Commit(message, parentsID, files, isMerged, parent.getBranch());
        newCommit.setDepth(parent.getDepth() + 1);
        newCommit.writeBlob();
        writeObject(join(GITLET_DIR, "commits", newCommit.getCommitID()), (Serializable) newCommit);
        writeObject(join(GITLET_DIR, "refs/heads/master"), newCommit.getCommitID());
        for (String fileName : plainFilenamesIn(stagingArea)) {
            join(GITLET_DIR, "staging", fileName).delete();
        }
    }

    public void rm(String fileName) {
        Commit currentCommit = readObject(join(GITLET_DIR, "commits", readObject(join(GITLET_DIR, "refs/heads/master"), String.class)), Commit.class);
        if (!currentCommit.getFiles().contains(fileName)) {
            System.out.println("No reason to remove the file.");
            return;
        }
        removedFiles.add(fileName);
        writeContents(join(GITLET_DIR, "removing", fileName), readContentsAsString(join(CWD, fileName)));
        if (plainFilenamesIn(join(GITLET_DIR, "staging")).contains(fileName)) {
            stagedFiles.remove(fileName);
            join(GITLET_DIR, "staging", fileName).delete();
        }
        File file=join(CWD, fileName);
        file.delete();
    }

    private void print_log(Commit commit){
        if(commit.isMerged()) {
            String commitID = commit.getCommitID();
            System.out.println("===");
            System.out.println("commit " + commitID);
            System.out.println("Merge: " + commit.getParentsID().get(0).substring(0, 7) + " " + commit.getParentsID().get(1).substring(0, 7));
            Commit parent1 = readObject(join(GITLET_DIR, "commits", commit.getParentsID().get(0)), Commit.class);
            Commit parent2 = readObject(join(GITLET_DIR, "commits", commit.getParentsID().get(1)), Commit.class);
            System.out.println("Date: " + commit.getTimestamp());
            System.out.println("Merged " + parent1.getBranch() + " into " + parent2.getBranch() + ".");
            System.out.println();
            print_log(parent1);
            print_log(parent2);
            return;
        }
        System.out.println("===");
        System.out.println("commit " + commit.getCommitID());
        System.out.println("Date: " + commit.getDate());
        System.out.println(commit.getMessage());
        System.out.println();
        if (!commit.getParentsID().get(0).equals("")) {
            Commit parent = readObject(join(GITLET_DIR, "commits", commit.getParentsID().get(0)), Commit.class);
            print_log(parent);
        }
    }

    public void log(){
        String commitID = readObject(join(GITLET_DIR, "refs/heads/master"),String.class);
        Commit commit = readObject(join(GITLET_DIR, "commits", commitID), Commit.class);
        print_log(commit);
    }

    public void globalLog(){
        for (String commitID : plainFilenamesIn(join(GITLET_DIR, "commits"))) {
            Commit commit = readObject(join(GITLET_DIR, "commits", commitID), Commit.class);
            if(commit.isMerged()){
                System.out.println("===");
                System.out.println("commit " + commitID);
                System.out.println("Merge: " + commit.getParentsID().get(0).substring(0,7) + " " + commit.getParentsID().get(1).substring(0,7));
                Commit parent1 = readObject(join(GITLET_DIR, "commits", commit.getParentsID().get(0)), Commit.class);
                Commit parent2 = readObject(join(GITLET_DIR, "commits", commit.getParentsID().get(1)), Commit.class);
                System.out.println("Date: " + commit.getTimestamp());
                System.out.println("Merged " + parent1.getBranch() + " into " + parent2.getBranch() + ".");
                System.out.println();
                continue;
            }
            System.out.println("===");
            System.out.println("commit " + commitID);
            System.out.println("Date: " + commit.getTimestamp());
            System.out.println(commit.getMessage());
            System.out.println();
        }
    }

    public void find(String message){
        boolean found = false;
        for (String commitID : plainFilenamesIn(join(GITLET_DIR, "commits"))) {
            Commit commit = readObject(join(GITLET_DIR, "commits", commitID), Commit.class);
            if (commit.getMessage().equals(message)) {
                System.out.println(commitID);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Found no commit with that message.");
            return;
        }
    }

    public void status(){
        System.out.println("=== Branches ===");
        for (String branch : plainFilenamesIn(join(GITLET_DIR, "refs/heads"))) {
            Commit curentCommit = readObject(join(GITLET_DIR, "commits", readObject(join(GITLET_DIR, "refs/heads/" + branch), String.class)), Commit.class);
            System.out.println("*"+curentCommit.getBranch());
            for(String branchName : plainFilenamesIn(join(GITLET_DIR, "refs/heads"))){
                if(!branchName.equals(curentCommit.getBranch())){
                    System.out.println(branchName);
                }
            }
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        for (String fileName : plainFilenamesIn(join(GITLET_DIR, "staging"))) {
            System.out.println(fileName);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        for (String fileName : removedFiles) {
            System.out.println(fileName);
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        senseModification();
        System.out.println();
        System.out.println("=== Untracked Files ===");
        for(String workingTreeFile : plainFilenamesIn(CWD)){
            if(!readObject(join(GITLET_DIR, "commits", readObject(join(GITLET_DIR, "refs/heads/master"), String.class)), Commit.class).getFiles().contains(workingTreeFile)){
                if(!removedFiles.contains(workingTreeFile)){
                    if(!stagedFiles.contains(workingTreeFile)) {
                        System.out.println(workingTreeFile);
                    }
                }
            }
        }
        System.out.println();
    }

    private void senseModification() {
        Commit currentCommit = readObject(join(GITLET_DIR, "commits", readObject(join(GITLET_DIR, "refs/heads/master"), String.class)), Commit.class);
        Set<String> files = currentCommit.getFiles();
        for (String workingTreeFile : plainFilenamesIn(CWD)) {
            if (!files.contains(workingTreeFile)) {
                if (!removedFiles.contains(workingTreeFile)) {
                    //System.out.println("There is an untracked file in the way; delete it or add it first.");
                    //System.exit(0);
                    int a = 0;  // do nothing
                }
                else {
                    System.out.println(workingTreeFile + "(deleted)");
                }
            }
            else {
                File file = join(CWD, workingTreeFile);
                String sha1 = sha1(readContentsAsString(file));
                if(currentCommit.getBlob(workingTreeFile) == null){
                    System.out.println(workingTreeFile + "(created)");
                }
                else if (!sha1.equals(currentCommit.getBlob(workingTreeFile))) {
                    System.out.println(workingTreeFile + "(modified)");
                }
            }
        }
    }

    public void checkout() {}

    public void checkoutBranch(String branch) {
        if (!plainFilenamesIn(join(GITLET_DIR, "refs/heads")).contains(branch)) {
            System.out.println("No such branch exists.");
            return;
        }
        if (branch.equals(readContentsAsString(join(GITLET_DIR, "branch"))) ) {
            System.out.println("No need to checkout the current branch.");
            return;
        }
        Commit currentCommit = readObject(join(GITLET_DIR, "commits", readObject(join(GITLET_DIR, "refs/heads/" + branch), String.class)), Commit.class);
        for(String fileName : plainFilenamesIn(CWD)){
            File file = join(CWD, fileName);
            file.delete();
        }
        for (String fileName : currentCommit.getFiles()) {
            checkoutFile(fileName);
        }
        writeContents(join(GITLET_DIR, "branch"), branch);
    }

    public void checkoutFile(String fileName) {
        String branch = readContentsAsString(join(GITLET_DIR, "branch"));
        Commit currentCommit = readObject(join(GITLET_DIR, "commits", readObject(join(GITLET_DIR, "refs/heads/" + branch), String.class)), Commit.class);
        if (!currentCommit.getFiles().contains(fileName)) {
            System.out.println("File does not exist in that commit.");
            return;
        }
        String blobID = currentCommit.getBlob(fileName);
        writeContents(join(CWD, fileName), readObject(join(GITLET_DIR, "blobs", blobID), String.class));
    }

    public void checkoutCommit(String commitID, String fileName) {
        commitID = commitID.substring(0, 4);
        boolean found = false;
        for (String commit : plainFilenamesIn(join(GITLET_DIR, "commits"))) {
            if (commit.contains(commitID)) {
                Commit currentCommit = readObject(join(GITLET_DIR, "commits", commit), Commit.class);
                if (!currentCommit.getFiles().contains(fileName)) {
                    System.out.println("File does not exist in that commit.");
                    return;
                }
                String blobID = currentCommit.getBlob(fileName);
                writeContents(join(CWD, fileName), readObject(join(GITLET_DIR, "blobs", blobID), String.class));
                found = true;
            }
        }
        if (!found) {
            System.out.println("No commit with that id exists.");
            return;
        }
    }

    public void branch(String arg) {
        if(plainFilenamesIn(join(GITLET_DIR, "refs/heads")).contains(arg)){
            System.out.println("A branch with that name already exists.");
            return;
        }
        writeObject(join(GITLET_DIR, "refs/heads/" + arg), readObject(join(GITLET_DIR, "refs/heads/" + currentBranch), String.class));
    }

    public void removeBranch(String arg) {
        if (!plainFilenamesIn(join(GITLET_DIR, "refs/heads")).contains(arg)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (arg.equals(readContentsAsString(join(GITLET_DIR, "branch"))) ) {
            System.out.println("Cannot remove the current branch.");
        }
        join(GITLET_DIR, "refs/heads", arg).delete();
    }

    public void reset(String commitID) {
        commitID = commitID.substring(0, 4);
        boolean found = false;
        for (String commit : plainFilenamesIn(join(GITLET_DIR, "commits"))) {
            if (commit.contains(commitID)) {
                Commit currentCommit = readObject(join(GITLET_DIR, "commits", commit), Commit.class);
                for(String fileName : plainFilenamesIn(CWD)){
                    File file = join(CWD, fileName);
                    file.delete();
                }
                for (String fileName : currentCommit.getFiles()) {
                    checkoutFile(fileName);
                }
                writeObject(join(GITLET_DIR, "refs/heads/" + currentBranch), commit);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No commit with that id exists.");
            return;
        }
    }

    private void findAncestors(String commitID, Set<String> ancestors) {
        Commit commit = readObject(join(GITLET_DIR, "commits", commitID), Commit.class);
        ancestors.add(commit.getCommitID());
        if (!commit.getParentsID().isEmpty()) {
            for(String parentID : commit.getParentsID()){
                findAncestors(parentID, ancestors);
            }
        }
    }

    private String containsAncestors(String commitID,Set<String> ancestors){
        Commit commit = readObject(join(GITLET_DIR, "commits", commitID), Commit.class);
        if(ancestors.contains(commit.getCommitID())){
            return commit.getCommitID();
        }
        if (!commit.getParentsID().isEmpty()) {
            for(String parentID : commit.getParentsID()){
                return containsAncestors(parentID, ancestors);
            }
        }
        return null;

    }

    private String findSplitPoint(String branch1, String branch2) {
        // find the split point of two branches
        // capable of merged commit with complex multiple branches
        Commit commit1 = readObject(join(GITLET_DIR, "commits", readObject(join(GITLET_DIR, "refs/heads/" + branch1), String.class)), Commit.class);
        Commit commit2 = readObject(join(GITLET_DIR, "commits", readObject(join(GITLET_DIR, "refs/heads/" + branch2), String.class)), Commit.class);
        Set<String> commit1Set = new HashSet<>();
        findAncestors(commit1.getCommitID(), commit1Set);
        return containsAncestors(commit2.getCommitID(), commit1Set);
    }

    public void merge(String givenBranch) {
        if (!plainFilenamesIn(join(GITLET_DIR, "refs/heads")).contains(givenBranch)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (givenBranch.equals(readContentsAsString(join(GITLET_DIR, "branch"))) ) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }
        Commit currentCommit = readObject(join(GITLET_DIR, "commits", readObject(join(GITLET_DIR, "refs/heads/" + currentBranch), String.class)), Commit.class);
        Commit givenCommit = readObject(join(GITLET_DIR, "commits", readObject(join(GITLET_DIR, "refs/heads/" + givenBranch), String.class)), Commit.class);
        String splitPoint = findSplitPoint(currentBranch, givenBranch);
        if (splitPoint.equals(givenCommit.getCommitID())) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        }
        if (splitPoint.equals(currentCommit.getCommitID())) {
            writeObject(join(GITLET_DIR, "refs/heads/" + currentBranch), givenCommit.getCommitID());
            System.out.println("Current branch fast-forwarded.");
            return;
        }
        Commit splitCommit = readObject(join(GITLET_DIR, "commits", splitPoint), Commit.class);
        for (String fileName : splitCommit.getFiles()) {
            if (!currentCommit.getFiles().contains(fileName) && givenCommit.getFiles().contains(fileName)) {
                checkoutCommit(givenCommit.getCommitID(), fileName);
                add(fileName);
            }
        }
        for (String fileName : givenCommit.getFiles()) {
            if (!splitCommit.getFiles().contains(fileName) && !currentCommit.getFiles().contains(fileName)) {
                checkoutCommit(givenCommit.getCommitID(), fileName);
                add(fileName);
            }
        }
        if (!plainFilenamesIn(join(GITLET_DIR, "staging")).isEmpty()) {
            System.out.println("Encountered a merge conflict.");
            return;
        }
        commit("Merged " + givenBranch + " into " + currentBranch + ".", true);
    }
}