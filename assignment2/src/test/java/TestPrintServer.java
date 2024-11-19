import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.Arrays;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

import auth.roles.MissingRequiredAccessException;
import printserver.PrintServant;

public class TestPrintServer {
  private PrintServant printServant;

  @Before
  public void setUp() throws RemoteException {
    String passwordFile = "./src/test/res/password_role_tests.csv";
    String aclFile = "./src/test/res/acl_tests.txt";

    this.printServant = new PrintServant(passwordFile, aclFile);

    String username = "alice";
    String password = "pass";

    String salt = BCrypt.gensalt();
    String hPass = BCrypt.hashpw(password, salt);

    try {
      BufferedWriter writer = Files.newBufferedWriter(Paths.get(passwordFile));
      CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.EXCEL);
      csvPrinter.printRecord(Arrays.asList("username", "password", "salt"));
      csvPrinter.printRecord(Arrays.asList(username, hPass, salt));

      csvPrinter.flush();
      csvPrinter.close();
    } catch (Exception e) {
      assertFalse(true);
    }
  }

  @Test
  public void testPrintServer() throws RemoteException {
    assertTrue(this.printServant.login("alice", "pass"));
    assertTrue(this.printServant.addLogin("bob", "pass"));
    this.printServant.logout("alice");
    assertTrue(this.printServant.login("bob", "pass"));
  }

  @Test(expected = MissingRequiredAccessException.class)
  public void testInvalidLogin() throws RemoteException {
    this.printServant.login("bob", "pass");
    this.printServant.addLogin("erica", "pass");
  }

}
