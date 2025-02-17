package gitlet;

import java.io.File;

public class Main {

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    static final File CWD = new File(System.getProperty("user.dir"));
    static final File GITLET_DIR = Utils.join(CWD, ".gitlet");
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        Repository repo = new Repository();
        String firstArg = args[0];
        if(!firstArg.equals("init") && !GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        switch (firstArg) {
            case "init": {
                repo.init();
                break;
            }
            case "add": {
                repo.add(args[1]);
                break;
            }
            case "commit": {
                repo.commit(args[1], false);
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
            case "debug": {
                repo.debugCommit(args[1]);
                break;
            }
            case "checkout": {
                if (args.length == 2) {
                    repo.checkoutBranch(args[1]);
                } else if (args.length == 3) {
                    if(!args[1].equals("--")) {
                        System.out.println("Incorrect operands.");
                    }
                    repo.checkoutFile(args[2]);
                } else if (args.length == 4) {
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
                System.out.println("No command with that name exists.");
            }
        }
    }

}
