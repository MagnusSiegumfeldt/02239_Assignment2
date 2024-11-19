package accesscontrol;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import accesscontrol.roles.BasicUser;
import accesscontrol.roles.IUser;
import accesscontrol.roles.Manager;
import accesscontrol.roles.PowerUser;
import accesscontrol.roles.Technician;

/*
manager <- technician, powerUser, addLogin
technician <- starter,configManager
powerUser <- basicUser, topQueue, restart
basicUser <- print, queue
		
starter <- start, stop, restart
configManager <- status, setConfig, readConfig
*/
public class RoleBasedAccessControlManager implements IAccessControl {
  HashMap<String, IUser> rolemap;

  public RoleBasedAccessControlManager(String accessControlFile) {
    rolemap = new HashMap<String, IUser>();

    BufferedReader reader;
    try {
      reader = new BufferedReader(new FileReader(accessControlFile));
      String line = reader.readLine();

      while (line != null) {
        String[] splitted = line.split(":");
        String name = splitted[0];
        String role = splitted[1];

        IUser user = null; // Initialize with a default value
        switch (role) {
          case "manager":
            user = new Manager();
            break;
          case "technician":
            user = new Technician();
            break;
          case "poweruser":
            user = new PowerUser();
            break;
          case "basicuser":
            user = new BasicUser();
            break;
          default:
            throw new IllegalArgumentException("Unknown role: " + role); // Handle unexpected roles
        }

        rolemap.put(name, user);

        line = reader.readLine();
      }

      reader.close();
    } catch (IOException e) {
      System.err.println("Could not read access control list file: " + e);
    }

  }

  @Override
  public boolean check(String name, String right) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'check'");
  }

}
