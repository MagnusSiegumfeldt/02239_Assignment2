package accesscontrol.accesses;

public interface IConfigAccess {
    default void readConfig(String parameter) {
        
    }
    default void setConfig(String parameter, String value) {
        
    }
}
