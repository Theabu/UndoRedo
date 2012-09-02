package shadeglare.undoredo;

public interface ITransaction extends IDisposable {
    IMultiAction getAccumulatingAction();
    void setIsDelayed(boolean value);
    boolean getIsDelayed();
}
