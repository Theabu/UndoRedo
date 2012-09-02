package shadeglare.undoredo;

import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;

public class ActionManager {

    public ActionManager() {
        setHistory(new History());
    }

    private EventListenerList redoUndoBufferChangedListeners = new EventListenerList();

    private IActionHistory _history;
    protected void setHistory(IActionHistory value) {
        if (_history != null) {
            _history.setCollectionChangedListeners(null);
        }
        _history = value;
        if (_history != null) {
            _history.setCollectionChangedListeners(redoUndoBufferChangedListeners);
        }
    }
    public IActionHistory getHistory() {
        return _history;
    }
    
    private IAction _currentAction;
    protected void setCurrentAction(IAction value) {
        _currentAction = value;
    }
    public IAction getCurrentAction() {
        return _currentAction;
    }

    private boolean _executeImmediatelyWithoutRecording;
    public void setExecuteImmediatelyWithoutRecording(boolean value) {
        _executeImmediatelyWithoutRecording = value;
    }
    public boolean getExecuteImmediatelyWithoutRecording() {
        return _executeImmediatelyWithoutRecording;
    }

    private Stack<ITransaction> _transactionStack = new Stack<ITransaction>();
    public Stack<ITransaction> getTransactionStack() {
        return _transactionStack;
    }

    public ITransaction getRecordingTransaction() {
        if (getTransactionStack().size() > 0) {
            return getTransactionStack().peek();
        }
        return null;
    }
    public void openTransaction(ITransaction transaction) {
        getTransactionStack().push(transaction);
    }
    public void commitTransaction() {
        if (getTransactionStack().size() == 0) {
            throw new IllegalStateException("ActionManager.CommitTransaction was called"
                    + " when there is no open transaction (TransactionStack is empty)."
                    + " Please examine the stack trace of this exception to find code"
                    + " which called CommitTransaction one time too many."
                    + " Normally you don't call OpenTransaction and CommitTransaction directly,"
                    + " but use using(var t = Transaction.Create(Root)) instead.");
        }

        ITransaction committing = getTransactionStack().pop();

        if (committing.getAccumulatingAction().size() > 0) {
            recordAction(committing.getAccumulatingAction());
        }
    }
    public void rollbackTransaction() {
        if (getTransactionStack().size() != 0) {
            ITransaction topLevelTransaction = getTransactionStack().peek();
            if (topLevelTransaction != null && topLevelTransaction.getAccumulatingAction() != null) {
                topLevelTransaction.getAccumulatingAction().unExecute();
            }
            getTransactionStack().clear();
        }
    }

    public void undo() {
        if (!canUndo()) {
            return;
        }
        if (isActionExecuting()) {
            throw new IllegalStateException(String.format("ActionManager is currently busy"
                    + " executing a transaction (%s). This transaction has called Undo()"
                    + " which is not allowed until the transaction ends."
                    + " Please examine the stack trace of this exception to see"
                    + " what part of your code called Undo.", getCurrentAction().toString()));
        }
        setCurrentAction(getHistory().getCurrentState().getPreviousAction());
        getHistory().moveBack();
        setCurrentAction(null);
    }
    public void redo() {
        if (!canRedo()) {
            return;
        }
        if (isActionExecuting()) {
            throw new IllegalStateException(String.format("ActionManager is currently busy"
                    + " executing a transaction (%s). This transaction has called Redo()"
                    + " which is not allowed until the transaction ends."
                    + " Please examine the stack trace of this exception to see"
                    + " what part of your code called Redo.", getCurrentAction().toString()));
        }
        setCurrentAction(getHistory().getCurrentState().getNextAction());
        getHistory().moveForward();
        setCurrentAction(null);
    }

    public boolean canUndo() {
        return getHistory().canMoveBack();
    }
    public boolean canRedo() {
        return getHistory().canMoveForward();
    }

    public boolean isActionExecuting() {
        return getCurrentAction() != null;
    }

    public void recordAction(IAction existingAction) {
        if (existingAction == null) {
            throw new NullPointerException("ActionManager.RecordAction: the existingAction argument is null");
        }
        checkNotRunningBeforeRecording(existingAction);
        if (getExecuteImmediatelyWithoutRecording() && existingAction.canExecute()) {
            existingAction.execute();
            return;
        }
        ITransaction currentTransaction = getRecordingTransaction();
        if (currentTransaction != null) {
            currentTransaction.getAccumulatingAction().add(existingAction);
            if (!currentTransaction.getIsDelayed()) {
                existingAction.execute();
            }
        } else {
            runActionDirectly(existingAction);
        }
    }

    private void checkNotRunningBeforeRecording(IAction existingAction) {
        String existing = existingAction != null ? existingAction.toString() : "";

        if (getCurrentAction() != null) {
            throw new IllegalStateException(
                    String.format(
                            "ActionManager.RecordActionDirectly: the ActionManager is currently running "
                                    + "or undoing an action (%s), and this action (while being executed) attempted "
                                    + "to recursively record another action (%b), which is not allowed. "
                                    + "You can examine the stack trace of this exception to see what the "
                                    + "executing action did wrong and change this action not to influence the "
                                    + "Undo stack during its execution. Checking if ActionManager.ActionIsExecuting == true "
                                    + "before launching another transaction might help to avoid the problem. Thanks and sorry for the inconvenience.",
                            getCurrentAction().toString(),
                            existing));
        }
    }

    private synchronized void runActionDirectly(IAction actionToRun) {
        checkNotRunningBeforeRecording(actionToRun);
        setCurrentAction(actionToRun);
        if (getHistory().appendAction(actionToRun)) {
            getHistory().moveForward();
        }
        setCurrentAction(null);
    }

    public void addUndoRedoBufferChangedListener(ActionListener listener) {
        redoUndoBufferChangedListeners.add(ActionListener.class, listener);
    }
    public void removeUndoRedoBufferChangedListener(ActionListener listener) {
        redoUndoBufferChangedListeners.remove(ActionListener.class, listener);
    }

    public void clear() {
        getHistory().clear();
        setCurrentAction(null);
    }

    public Iterable<IAction> enumUndoableActions() {
        return getHistory().enumUndoableActions();
    }
}
