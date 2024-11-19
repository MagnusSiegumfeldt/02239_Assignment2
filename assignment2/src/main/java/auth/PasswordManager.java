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
	private static String FILE_NAME = "./pwrds.csv";

	public PasswordManager() throws IOException {}

	public boolean checkLogin(String username, String password) throws FileNotFoundException, IOException {
		try {
			Reader in = new FileReader(FILE_NAME);
			CSVFormat csvFormat = CSVFormat.EXCEL.builder().setHeader().setSkipHeaderRecord(false).build();

			CSVParser csvParser = new CSVParser(in, csvFormat);
			Iterator<CSVRecord> csvIterator = csvParser.iterator();

			while (csvIterator.hasNext()) {
				CSVRecord record = csvIterator.next();

				String rUser = record.get("username");
				String rPass = record.get("password");
				String rSalt = record.get("salt");
				String hPass = BCrypt.hashpw(password, rSalt);
				
				if (rUser.equals(username) && hPass.equals(rPass)) {
					System.out.println("Verified password");
					csvParser.close();
					return true;
				}
			}

			System.out.println("Didn't verify password");
			csvParser.close();
			return false;
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
	}

	public void createLogin(String username, String password) {
		String salt = BCrypt.gensalt();
		String hPass = BCrypt.hashpw(password, salt);

		try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_NAME), StandardOpenOption.APPEND);
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.EXCEL); 

            csvPrinter.printRecord(Arrays.asList(username, hPass, salt));

            csvPrinter.flush();   
			csvPrinter.close();         
        } catch (Exception e) {}
	}
}
