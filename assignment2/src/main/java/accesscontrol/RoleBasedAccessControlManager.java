package accesscontrol;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
                
                IUser user;
                switch (role) {
                    case "manager":
                        user = new Manager(); break;
                    case "technician":
                        user = new Technician(); break;
                    case "basicuser":
                        user = new BasicUser(); break;
                    case "poweruser":
                        user = new PowerUser(); break;
                }
                userList.add(user);
                
                
                
				line = reader.readLine();
			}

			reader.close();
		} catch (IOException e) {
			System.err.println("Could not read access control list file: " + e);
		}



    }
    public check(String name, String right) {
        
    }


}
