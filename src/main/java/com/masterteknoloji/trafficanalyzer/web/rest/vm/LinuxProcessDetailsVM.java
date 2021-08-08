package com.masterteknoloji.trafficanalyzer.web.rest.vm;

public class LinuxProcessDetailsVM {

		Long pid;
		Long duration;
		String sessionId;
		public Long getPid() {
			return pid;
		}
		public void setPid(Long pid) {
			this.pid = pid;
		}
		public Long getDuration() {
			return duration;
		}
		public void setDuration(Long duration) {
			this.duration = duration;
		}
		public String getSessionId() {
			return sessionId;
		}
		public void setSessionId(String sessionId) {
			this.sessionId = sessionId;
		}
}
