package ec.edu.ups.controlador.exceptions;

import javax.swing.JOptionPane;

public class PreexistingEntityException extends Exception {
    public PreexistingEntityException(String message, Throwable cause) {
        super(message, cause);
        int posicion = message.indexOf('[');
        posicion++;
        int posicion2 = message.indexOf('=');
        String campo = message.substring(posicion, posicion2);
        campo=campo.toUpperCase();
        if(message.indexOf("already exist")>0){
            JOptionPane.showMessageDialog(null, campo + " YA EXISTE", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        JOptionPane.showMessageDialog(null, message, "ERROR", JOptionPane.ERROR_MESSAGE);
    }
    public PreexistingEntityException(String message) {
        super(message);
        
    }
}
