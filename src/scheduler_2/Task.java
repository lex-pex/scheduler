package scheduler_2;

public class Task {
    private int caretPosition;
    private String text;
    
    public Task(int caret, String text) {
        this.caretPosition = caret; this.text = text;
    }
    
    public void setCaretPosition(int caretPosition) {
        this.caretPosition = caretPosition;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCaretPosition() {
        return caretPosition;
    }

    public String getText() {
        return text;
    }
    
}
