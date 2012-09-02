package shadeglare.undoredo;

public abstract class AbstractAction implements IAction {

    private int _executeCount;
    protected int getExecuteCount() {
        return  _executeCount;
    }
    protected void setExecuteCount(int value) {
        _executeCount = value;
    }

    private boolean _allowToMergeWithPrevious = true;
    public void setAllowToMergeWithPrevious(boolean value) {
        _allowToMergeWithPrevious = value;
    }
    public boolean getAllowToMergeWithPrevious() {
        return _allowToMergeWithPrevious;
    }

    public boolean tryToMerge(IAction followingAction) {
        return false;
    }

    protected abstract void executeCore();
    protected abstract void unExecuteCore();

    public void execute() {
        if (!canExecute()) {
            return;
        }
        executeCore();
        setExecuteCount(getExecuteCount() + 1);
    }
    public void unExecute() {
        if (!canUnExecute()) {
            return;
        }
        unExecuteCore();
        setExecuteCount(getExecuteCount() - 1);
    }

    public boolean canExecute() {
        return getExecuteCount() == 0;
    }
    public boolean canUnExecute() {
        return !canExecute();
    }
}
