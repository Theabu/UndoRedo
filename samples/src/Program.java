import shadeglare.undoredo.*;

public class Program {

    public static void main(String[] args) {
        TextEditor textEditor = new TextEditor();
        textEditor.currentText = new StringBuffer(" next to same  lame go");

        System.out.println("Initial text:");
        System.out.println(textEditor.currentText.toString());
        System.out.println();

        RemoveWhitespacesAction removeWhiteSpaces = new RemoveWhitespacesAction(textEditor);
        textEditor.actionManager.recordAction(removeWhiteSpaces);
        System.out.println("Text after performing a remove whitespaces action");
        System.out.println(textEditor.currentText.toString());
        System.out.println();


        textEditor.actionManager.undo();
        System.out.println("Text after undo the previous action");
        System.out.println(textEditor.currentText.toString());
        System.out.println();
    }
}
