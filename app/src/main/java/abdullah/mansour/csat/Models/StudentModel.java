package abdullah.mansour.csat.Models;

public class StudentModel
{
    private String fullname,email,id;

    public StudentModel() {
    }

    public StudentModel(String fullname, String email, String id) {
        this.fullname = fullname;
        this.email = email;
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }
}
