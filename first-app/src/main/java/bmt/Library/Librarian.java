package bmt.Library;

public class Librarian {
    public String librarianId;
    public String name;
    public String password;

    public Librarian(String librarianId, String name, String password) {
        this.librarianId = librarianId;
        this.name = name;
        this.password = password;
    }

    public boolean authenticate(String inputPassword) {
        return password.equals(inputPassword);
    }
}
