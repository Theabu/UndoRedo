package shadeglare.undoredo;

import java.util.ArrayList;

public class MultiAction
        extends ArrayList<IAction>
        implements IMultiAction {

    private boolean _isDelayed;
    public void setIsDelayed(boolean value) {
        _isDelayed = value;
    }
    public boolean getIsDelayed() {
        return _isDelayed;
    }

    private boolean _allowToMergeWithPrevious;
    public void setAllowToMergeWithPrevious(boolean value) {
        _allowToMergeWithPrevious = value;
    }
    public boolean getAllowToMergeWithPrevious() {
        return _allowToMergeWithPrevious;
    }

    public MultiAction() {
        setIsDelayed(true);
    }

    public boolean canExecute() {
        for (IAction action : this) {
            if (!action.canExecute()) {
                return false;
            }
        }
        return true;
    }
    public boolean canUnExecute() {
        for (IAction action : this) {
            if (!action.canUnExecute()) {
                return false;
            }
        }
        return true;
    }

    public void execute() {
        if (!getIsDelayed()) {
            setIsDelayed(true);
            return;
        }
        for (IAction action : this) {
            action.execute();
        }
    }
    public void unExecute() {
        for (int i = size() - 1; i >= 0; i--) {
            get(i).unExecute();
        }
    }

    public boolean tryToMerge(IAction followingAction) {
        return false;
    }
}
