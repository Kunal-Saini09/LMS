package bmt.Library;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Librarian {
    private StringProperty librarianId;
    private StringProperty name;
    private StringProperty password;

    public Librarian(String librarianId, String name, String password) {
        this.librarianId = new SimpleStringProperty(librarianId);
        this.name = new SimpleStringProperty(name);
        this.password = new SimpleStringProperty(password);
    }

    // Property getters for JavaFX
    public StringProperty librarianIdProperty() {
        return librarianId;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    // Simple getters
    public String getLibrarianId() {
        return librarianId.get();
    }

    public String getName() {
        return name.get();
    }

    public String getPassword() {
        return password.get();
    }

    public boolean authenticate(String inputPassword) {
        return getPassword().equals(inputPassword);
    }
}
