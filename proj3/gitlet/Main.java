package gitlet;

import java.io.File;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Tasman Kuang
 */
public class Main {

    /** Current working directory. */
    static final File CWD = new File(".");
    /** Repo directory. */
    static final File REPO = Utils.join(CWD, ".gitlet");
    /** Object directory. */
    static final File OBJECT = Utils.join(REPO, "object");
    /** Branch directory. */
    static final File BRANCH = Utils.join(REPO, "branch");
    /** Staging area. */
    static final File STAGE = Utils.join(REPO, "stage");
    /** Commit history. */
    static final File HISTORY = Utils.join(REPO, "history");
    /** Remote directory. */
    static final File REMOTE = Utils.join(REPO, "remote");

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> ....
     *  java gitlet.Main add hello.txt*/
    public static void main(String... args) {
        if (args.length == 0) {
            System.out.println("Please enter a command");
            return;
        }
        if (args[0].equals("init")) {
            init();
        }
        if (args[0].equals("commit")) {
            commit();
        }
        System.out.println("I don't understand this command");
    }
    /** Initializes a git repo. */
    public static void init() {
        if (REPO.exists()) {
            System.out.println("A Gitlet version-control system "
                    + "already exists in the current directory.");
            return;
        }
        REPO.mkdir();
        OBJECT.mkdir();
        BRANCH.mkdir();
        STAGE.mkdir();
        HISTORY.mkdir();
        REMOTE.mkdir();
        File cwd = new File(System.getProperty("user.dir"));
    }
    /** Creates a new commit. */
    public static void commit() {
    }

}
