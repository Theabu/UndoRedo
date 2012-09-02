import shadeglare.undoredo.*;

import java.util.ArrayList;

public class RemoveWhitespacesAction extends AbstractAction {

    public TextEditor textEditor;
    public ArrayList<Integer> positions = new ArrayList<Integer>();

    public RemoveWhitespacesAction(TextEditor textEditor) {
        this.textEditor = textEditor;
    }

    public void executeCore() {
        StringBuffer text = textEditor.currentText;
        while (true) {
            boolean  canBreak = true;
            int lastSearchIndex = 0;
            for (int i = lastSearchIndex; i < text.length(); i++) {
                if (text.charAt(i) == ' ') {
                    canBreak = false;
                    lastSearchIndex = i;
                    positions.add(i);
                    text.deleteCharAt(i);
                    break;
                }
            }
            if (canBreak) {
                break;
            }
        }
    }

    public void unExecuteCore() {
        StringBuffer text = textEditor.currentText;
        for (int i = 0; i < positions.size(); i++) {
            text.insert(positions.get(i) + i, ' ');
        }
    }

}
