package shadeglare.undoredo;

public class Transaction extends TransactionBase {

    protected Transaction(ActionManager actionManager, boolean  delayed)
    {
        super(actionManager, delayed);
        setAccumulatingAction(new MultiAction());
    }
    
    public static Transaction Create(ActionManager actionManager, boolean delayed) {
        return new Transaction(actionManager, delayed);
    }
    
    public static Transaction Create(ActionManager actionManager) {
        return Create(actionManager, true);
    }

    @Override
    public void commit() {
        getAccumulatingAction().setIsDelayed(getIsDelayed());
        super.commit();
    }
}
