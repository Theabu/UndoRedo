package shadeglare.undoredo;

import javax.swing.event.*;

interface IActionHistory extends Iterable<IAction> {

    boolean appendAction(IAction newAction);
    void clear();
    
    void moveBack();
    void moveForward();
    
    boolean canMoveBack();
    boolean canMoveForward();
    int length();

    HistoryNode getCurrentState();

    Iterable<IAction> enumUndoableActions();

    EventListenerList getCollectionChangedListeners();
    void setCollectionChangedListeners(EventListenerList value);
}
