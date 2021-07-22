package com.masterteknoloji.trafficanalyzer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Trafficanalzyzerv 2.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
	
	String ftpDirectory;

	public String getFtpDirectory() {
		return ftpDirectory;
	}

	public void setFtpDirectory(String ftpDirectory) {
		this.ftpDirectory = ftpDirectory;
	}
	
	

}
