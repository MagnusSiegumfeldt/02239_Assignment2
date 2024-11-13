package auth;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;

public class PasswordManager {
  // username, salt, password
  private String FILE_NAME;

  public PasswordManager() throws IOException {
    FILE_NAME = "passwords.csv";
    this.CreateFileIfNotExists();
  }

  public PasswordManager(String filename) throws IOException {
    FILE_NAME = filename;
    this.CreateFileIfNotExists();
  }

  public void CreateFileIfNotExists() throws IOException {
    File file = new File(FILE_NAME);
    if (!file.exists()) {
      file.createNewFile();
    }
  }

}
