package shadeglare.undoredo;

import java.util.*;

public interface IMultiAction extends IAction, List<IAction> {
    void setIsDelayed(boolean value);
    boolean getIsDelayed();
}
