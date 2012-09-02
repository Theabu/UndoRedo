package shadeglare.undoredo;

import java.util.*;
import javax.swing.event.*;
import java.awt.event.*;

class History implements IActionHistory {

    private EventListenerList _collectionChangedListeners = new EventListenerList();
    public EventListenerList getCollectionChangedListeners() {
        return _collectionChangedListeners;
    }
    public void setCollectionChangedListeners(EventListenerList value) {
        _collectionChangedListeners = value;
    }

    private int _length;
    public int length() {
        return _length;
    }

    private HistoryNode _currentState = new HistoryNode();
    public HistoryNode getCurrentState() {
        return  _currentState;
    }
    public void setCurrentState(HistoryNode value) {
        if (value != null) {
            _currentState = value;
        } else {
            throw new NullPointerException("CurrentState");
        }
    }

    private HistoryNode _head;
    public void setHead(HistoryNode value) {
        _head = value;
    }
    public HistoryNode getHead() {
        return  _head;
    }

    private IAction _lastAction;
    public void setLastAction(IAction value) {
        _lastAction = value;
    }
    public IAction getLastAction() {
        return _lastAction;
    }

    public History() {
        init();
    }

    private void init() {
        setCurrentState(new HistoryNode());
        setHead(getCurrentState());
    }
    
    public boolean appendAction(IAction newAction) {
        if (getCurrentState().getPreviousAction() == null) {
            getCurrentState().setNextAction(newAction);
            getCurrentState().setNextNode(new HistoryNode(newAction, getCurrentState()));
        } else {
            if (getCurrentState().getPreviousAction().tryToMerge(newAction)) {
                raiseCollectionChanged();
                return false;
            } else {
                getCurrentState().setNextAction(newAction);
                getCurrentState().setNextNode(new HistoryNode(newAction, getCurrentState()));
            }
        }
        return  true;
    }

    public void clear() {
        init();
        raiseCollectionChanged();
    }

    public Iterable<IAction> enumUndoableActions() {
        HistoryNode current = getHead();
        ArrayList<IAction> undoableActions = new ArrayList<IAction>();
        while (current != null && current != getCurrentState() && current.getNextAction() != null) {
            undoableActions.add(current.getNextAction());
            current = current.getNextNode();
        }
        return undoableActions;
    }

    public void moveForward() {
        if (!canMoveForward()) {
            throw new IllegalStateException("History.MoveForward() cannot execute because"
                    + " CanMoveForward returned false (the current state"
                    + " is the last state in the undo buffer.");
        }
        getCurrentState().getNextAction().execute();
        setCurrentState(getCurrentState().getNextNode());
        _length += 1;
        raiseCollectionChanged();
    }
    public void moveBack() {
        if (!canMoveBack()) {
            throw new IllegalStateException("History.MoveBack() cannot execute because"
                    + " CanMoveBack returned false (the current state"
                    + " is the last state in the undo buffer.");
        }
        getCurrentState().getPreviousAction().unExecute();
        setCurrentState(getCurrentState().getPreviousNode());
        _length -= 1;
        raiseCollectionChanged();
    }

    public boolean canMoveForward() {
        return getCurrentState().getNextAction() != null && getCurrentState().getNextNode() != null;
    }
    public boolean canMoveBack() {
        return getCurrentState().getPreviousAction() != null && getCurrentState().getPreviousNode() != null;
    }

    public Iterator<IAction> iterator() {
        return enumUndoableActions().iterator();
    }

    protected void raiseCollectionChanged() {
        ActionEvent actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, Integer.toString(length()));
        EventListener listeners[] = getCollectionChangedListeners().getListeners(ActionListener.class);
        for (EventListener listener : listeners) {
            ((ActionListener)listener).actionPerformed(actionEvent);
        }
    }

//    public void addCollectionChangedListener(ActionListener listener) {
//        getCollectionChangedListeners().add(ActionListener.class, listener);
//    }
//    public void removeCollectionChangedListener(ActionListener listener) {
//        getCollectionChangedListeners().remove(ActionListener.class, listener);
//    }
}
