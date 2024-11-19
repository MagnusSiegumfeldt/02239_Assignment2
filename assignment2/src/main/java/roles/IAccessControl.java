package roles;

public interface IAccessControl {
    public boolean check(String name, String right);
}
