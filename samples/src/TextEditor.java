import java.awt.event.*;
import shadeglare.undoredo.*;

public class TextEditor implements ActionListener {

    public StringBuffer currentText = new StringBuffer();
    public ActionManager actionManager = new ActionManager();

    public TextEditor() {
        actionManager.addUndoRedoBufferChangedListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
    }
}
