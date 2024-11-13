package auth;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.security.MessageDigest;
import java.util.Iterator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.mindrot.jbcrypt.BCrypt;


public class PasswordManager {
  // username, salt, password
  private String FILE_NAME;

  private static final int WORKLOAD = 12;

  public PasswordManager() throws IOException {
    FILE_NAME = "./pwrds.csv";
  }

  public PasswordManager(String filename) throws IOException {
    FILE_NAME = filename;
  }


  public boolean checkLogin(String username, String password) throws FileNotFoundException, IOException {
    try {
      
      
      Reader in = new FileReader(FILE_NAME);
      CSVFormat csvFormat = CSVFormat.EXCEL.builder().setHeader().setSkipHeaderRecord(false).build();

      CSVParser csvParser = new CSVParser(in, csvFormat);
      Iterator<CSVRecord> csvIterator = csvParser.iterator();

      while (csvIterator.hasNext()) {
        CSVRecord record = csvIterator.next();
      
        String recordUsername = record.get("username");
        String recordPassword = record.get("password");
        String recordSalt = record.get("salt");

        BCrypt.gensalt(2);

        String hashedPassword = hashPassword(password, recordHash);


        System.out.println(username +":"+ password);
        System.out.println(recordUsername +":"+ recordPassword);

        //if (recordUsername == username && verifyPassword(password, recordPassword)) {
        if (recordUsername.equals(username) && hashedPassword.equals(recordPassword)) {  
          System.out.println("Verified password");
          return true;
        }
      }
      System.out.println("Didn't verify password");
      return false;
    } catch (Exception e) {
      System.err.println(e);
      return false;
    } 
  }

  public static String hashPassword(String password, String salt) {
    // BCrypt automatically generates and includes a random salt
    return BCrypt.hashpw(password, salt);
  }
    
  public static boolean verifyPassword(String password, String storedSalt) {
    // BCrypt.checkpw automatically handles all the salt extraction and verification
    return BCrypt.checkpw(password, storedSalt);
  }
}
