package ec.edu.ups.controlador.exceptions;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class IllegalOrphanException extends Exception {
    private List<String> messages;
    public IllegalOrphanException(List<String> messages) {
        super((messages != null && messages.size() > 0 ? messages.get(0) : null));
        if (messages == null) {
            this.messages = new ArrayList<String>();
        }
        else {
            this.messages = messages;
        }
        JOptionPane.showMessageDialog(null, messages, "ERROR", JOptionPane.ERROR_MESSAGE);
    }
    public List<String> getMessages() {
        return messages;
    }
}
