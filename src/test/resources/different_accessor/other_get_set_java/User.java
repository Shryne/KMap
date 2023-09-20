package different_accessor.other_get_set_java;

public class User {
    private int age;
    private String name;

    public int theAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
