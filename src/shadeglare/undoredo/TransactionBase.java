package shadeglare.undoredo;


public class TransactionBase implements ITransaction {

    private ActionManager _actionManager;
    protected void setActionManager(ActionManager value) {
        _actionManager = value;
    }
    public ActionManager getActionManager() {
        return _actionManager;
    }

    private Boolean _isDelayed;
    public void setIsDelayed(boolean value) {
        _isDelayed = value;
    }
    public boolean getIsDelayed() {
        return _isDelayed;
    }

    private IMultiAction _accumulatingAction;
    protected void setAccumulatingAction(IMultiAction value) {
        _accumulatingAction = value;
    }
    public IMultiAction getAccumulatingAction() {
        return _accumulatingAction;
    }

    private boolean _aborted;
    protected void setAborted(boolean value) {
        _aborted = value;
    }
    public boolean getAborted() {
        return _aborted;
    }

    public TransactionBase(ActionManager actionManager, boolean isDelayed)  {
        this(actionManager);
        setIsDelayed(isDelayed);
    }

    public TransactionBase(ActionManager actionManager) {
        this();
        setActionManager(actionManager);
        if (actionManager != null) {
            actionManager.openTransaction(this);
        }
    }

    public TransactionBase() {
        setIsDelayed(true);
    }

    public void commit() {
        if (getActionManager() != null) {
            getActionManager().commitTransaction();
        }
    }

    public void rollback() {
        if (getActionManager() != null) {
            getActionManager().rollbackTransaction();
            setAborted(true);
        }
    }

    public void dispose() {
        if (!getAborted()) {
            commit();
        }
    }
}
