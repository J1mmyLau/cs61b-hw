package gitlet;

/** General exception indicating a Gitlet error.  For fatal errors, the
 *  result of .getMessage() is the error message to be printed.
 *  @author P. N. Hilfinger
 */
class GitletException extends RuntimeException {


    /** A GitletException with no message. */
    GitletException() {
        super();
    }

    /** A GitletException MSG as its message. */
    GitletException(String msg) {
        super(msg);
    }

    static class NoArgsException extends GitletException {
        NoArgsException() {
            super("Please enter a command.");
        }
    }

    static class InvalidCommands extends GitletException {
        InvalidCommands() {
            super("No command with that name exists.");
        }
    }

    static class InvalidOperands extends GitletException {
        InvalidOperands() {
            super("Incorrect operands.");
        }
    }

    static class NoChanges extends GitletException {
        NoChanges() {
            super("No changes added to the commit.");
        }
    }

    static class NoWorkingDirectory extends GitletException {
        NoWorkingDirectory() {
            super("Not in an initialized Gitlet directory.");
        }
    }

    static class AlreadyInitialized extends GitletException {
        AlreadyInitialized() {
            super("A Gitlet version-control system already exists in the current directory.");
        }
    }

    static class FileNotExists extends GitletException {
        FileNotExists() {
            super("File does not exist.");
        }
    }

    static class NoMessage extends GitletException {
        NoMessage() {
            super("Please enter a commit message.");
        }
    }

    static class NoReasontoRemove extends GitletException {
        NoReasontoRemove() {
            super("No reason to remove the file.");
        }
    }

    static class NoBranch extends GitletException {
        NoBranch() {
            super("A branch with that name does not exist.");
        }
    }

    static class MessageNotFound extends GitletException {
        MessageNotFound() {
            super("Found no commit with that message.");
        }
    }

    static class FileNotInCommit extends GitletException {
        FileNotInCommit() {
            super("File does not exist in that commit.");
        }
    }

    static class CommitNotFound extends GitletException {
        CommitNotFound() {
            super("A branch with that name already exists.");
        }
    }

    static class NoNeedToCheckout extends GitletException {
        NoNeedToCheckout() {
            super("No need to checkout the current branch.");
        }
    }
     static class BranchNotFound extends GitletException {
         BranchNotFound() {
             super("No such branch exists.");
         }
     }
}
