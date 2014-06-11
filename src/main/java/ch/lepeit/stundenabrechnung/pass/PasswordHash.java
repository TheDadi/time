package ch.lepeit.stundenabrechnung.pass;

import java.security.MessageDigest;

public class PasswordHash {

	String password = "";

	public PasswordHash(String password) {
		getPasswordHash(password);
	}


	public void getPasswordHash(String password) {
		try {
			// Create MessageDigest instance for MD5
			MessageDigest md = MessageDigest.getInstance("SHA");
			// Add password bytes to digest
			md.update(password.getBytes());
			// Get the hash's bytes
			byte[] bytes = md.digest();
			// This bytes[] has bytes in decimal format;
			// Convert it to hexadecimal format
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
						.substring(1));
			}
			// Get complete hashed password in hex format
			this.password = sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(password);
	}

	public String getPassword() {
		return password;
	}

	public Boolean isPasswordEqual(String password) {
		if (this.password.equals(password)) {
			return true;
		} else {
			return false;
		}

	}

}
