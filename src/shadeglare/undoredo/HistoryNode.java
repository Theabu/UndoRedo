package shadeglare.undoredo;

class HistoryNode {

    private IAction _previousAction;

    public void setPreviousAction(IAction value) {
        _previousAction = value;
    }
    public IAction getPreviousAction() {
        return  _previousAction;
    }

    private IAction _nextAction;

    public void setNextAction(IAction value) {
        _nextAction = value;
    }
    public IAction getNextAction() {
        return _nextAction;
    }

    private HistoryNode _previousNode;

    public void setPreviousNode(HistoryNode value) {
        _previousNode = value;
    }
    public HistoryNode getPreviousNode() {
        return _previousNode;
    }

    private HistoryNode _nextNode;

    public void setNextNode(HistoryNode value) {
        _nextNode = value;
    }
    public HistoryNode getNextNode() {
        return _nextNode;
    }

    public HistoryNode(IAction lastExistingAction, HistoryNode lastExistingState) {
        setPreviousAction(lastExistingAction);
        setPreviousNode(lastExistingState);
    }
    public HistoryNode() {

    }
}
