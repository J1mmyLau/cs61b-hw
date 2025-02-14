package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if(args.length == 0) {
            throw new GitletException.NoArgsException();
        }
        Repository repo = new Repository();
        String firstArg = args[0];
        switch(firstArg) {
            case "init": {
                // TODO: handle the `init` command
                repo.init();
                break;
            }
            case "add": {
                // TODO: handle the `add [filename]` command
                repo.add(args[1]);
                break;
            }
            case "commit": {
                repo.commit(args[1]);
                break;
            }
            case "rm": {
                repo.rm(args[1]);
                break;
            }
            case "log": {
                repo.log();
                break;
            }
            case "global-log": {
                repo.globalLog();
                break;
            }
            case "find": {
                repo.find(args[1]);
                break;
            }
            case "status": {
                repo.status();
                break;
            }
            case "checkout": {
                if(args.length == 2) {
                    repo.checkoutBranch(args[1]);
                } else if(args.length == 3) {
                    repo.checkoutFile(args[2]);
                } else if(args.length == 4) {
                    repo.checkoutCommit(args[1], args[3]);
                }
                break;
            }
            case "branch": {
                repo.branch(args[1]);
                break;
            }
            case "rm-branch": {
                repo.removeBranch(args[1]);
                break;
            }
            case "reset": {
                repo.reset(args[1]);
                break;
            }
            case "merge": {
                repo.merge(args[1]);
                break;
            }

            default: {
                throw new GitletException.InvalidCommands();
            }
            // TODO: FILL THE REST IN
        }
    }

}
