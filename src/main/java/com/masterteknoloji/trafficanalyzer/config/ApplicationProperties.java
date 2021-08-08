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
	
	String aiScriptPath;
	
	String aiScriptName;
	
	Boolean getScriptOutput;
	
	Long longProcessKillTreshold;

	public String getFtpDirectory() {
		return ftpDirectory;
	}

	public void setFtpDirectory(String ftpDirectory) {
		this.ftpDirectory = ftpDirectory;
	}

	public String getAiScriptPath() {
		return aiScriptPath;
	}

	public void setAiScriptPath(String aiScriptPath) {
		this.aiScriptPath = aiScriptPath;
	}

	public String getAiScriptName() {
		return aiScriptName;
	}

	public void setAiScriptName(String aiScriptName) {
		this.aiScriptName = aiScriptName;
	}

	public Boolean getGetScriptOutput() {
		return getScriptOutput;
	}

	public void setGetScriptOutput(Boolean getScriptOutput) {
		this.getScriptOutput = getScriptOutput;
	}

	public Long getLongProcessKillTreshold() {
		return longProcessKillTreshold;
	}

	public void setLongProcessKillTreshold(Long longProcessKillTreshold) {
		this.longProcessKillTreshold = longProcessKillTreshold;
	}
	
	

}
