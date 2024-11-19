package accesscontrol;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AccessControlListManager implements IAccessControl {

    HashMap<String, ArrayList<String>> accessControlList;

    public AccessControlListManager(String aclFile) {
        accessControlList = new HashMap<String, ArrayList<String>>();

        BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(aclFile));
			String line = reader.readLine();

			while (line != null) {
                String[] splitted = line.split(":");
				String name = splitted[0];
                String[] acl = splitted[1].split(",");
                ArrayList<String> aclList = new ArrayList<String>(Arrays.asList(acl));
                this.accessControlList.put(name, aclList);
                
				line = reader.readLine();
			}

			reader.close();
		} catch (IOException e) {
			System.err.println("Could not read access control list file: " + e);
		}
    }

    public boolean check(String name, String right) {
        if (!accessControlList.containsKey(name)) {
            return false;
        }
        ArrayList<String> rights = accessControlList.get(name);
        return rights.contains(right);
    }
}
