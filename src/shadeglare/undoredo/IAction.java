package shadeglare.undoredo;

public interface IAction {
    void execute();
    void unExecute();

    boolean canExecute();
    boolean canUnExecute();

    boolean tryToMerge(IAction followingAction);

    boolean getAllowToMergeWithPrevious();
    void setAllowToMergeWithPrevious(boolean value);
}
