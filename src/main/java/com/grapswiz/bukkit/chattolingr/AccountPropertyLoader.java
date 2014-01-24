package com.grapswiz.bukkit.chattolingr;

import java.io.IOException;
import java.util.Properties;

public class AccountPropertyLoader {
	private Properties conf;
	
	public AccountPropertyLoader() throws IOException {
		conf = new Properties();
		conf.load(this.getClass().getResourceAsStream("/account.properties"));
	}
	
	public String getUser() {
		return conf.getProperty("user");
	}
	
	public String getPassword() {
		return conf.getProperty("password");
	}
}

