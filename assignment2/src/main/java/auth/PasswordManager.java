package auth;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordManager {
	String passwordFile;

	public PasswordManager() {
		this("passwords.csv");
	}
	public PasswordManager(String filename) {
		this.passwordFile = filename;
	}

	public boolean checkLogin(String username, String password) {
		Reader in;
		CSVFormat csvFormat;
		CSVParser csvParser;
		
		boolean success = false;

		try {
			in = new FileReader(this.passwordFile);

		} catch (FileNotFoundException e) {
			System.err.println("Could not find password file: " + e);
			return false;
		}
		
		csvFormat = CSVFormat.EXCEL.builder().setHeader().setSkipHeaderRecord(false).build();
		try {
			csvParser = new CSVParser(in, csvFormat);
		} catch (IOException e) {
			System.err.println("Could not parse password file: " + e);
			return false;			
		}

		Iterator<CSVRecord> csvIterator = csvParser.iterator();
		while (csvIterator.hasNext()) {
			CSVRecord record = csvIterator.next();

			String rUser = record.get("username");
			String rPass = record.get("password");
			String rSalt = record.get("salt");
			String hPass = BCrypt.hashpw(password, rSalt);
			
			if (rUser.equals(username) && hPass.equals(rPass)) {
				System.out.println("Verified password");
				success = true;
			}
		}

		try {
			csvParser.close();
		} catch (IOException e) {
			System.err.println("Could not close parser: " + e);
			return false;
		}

		return success;
	}

	public boolean createLogin(String username, String password) {
		String salt = BCrypt.gensalt();
		String hPass = BCrypt.hashpw(password, salt);

		try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(this.passwordFile), StandardOpenOption.APPEND);
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.EXCEL); 

            csvPrinter.printRecord(Arrays.asList(username, hPass, salt));

            csvPrinter.flush();   
			csvPrinter.close();
			return true;         
        } catch (IOException e) {
			System.err.println("Could not add login: " + e);
			return false;
		}
	}

	public void clear() {
		try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(this.passwordFile));
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.EXCEL); 

            csvPrinter.printRecord(Arrays.asList("username", "password", "salt"));

            csvPrinter.flush();   
			csvPrinter.close();
        } catch (IOException e) {
			System.err.println("Could not add login: " + e);
		}
	}
}
