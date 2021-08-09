package com.masterteknoloji.trafficanalyzer.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.masterteknoloji.trafficanalyzer.config.ApplicationProperties;
import com.masterteknoloji.trafficanalyzer.domain.AnalyzeOrderDetails;
import com.masterteknoloji.trafficanalyzer.domain.enumeration.AnalyzeState;
import com.masterteknoloji.trafficanalyzer.repository.AnalyzeOrderDetailsRepository;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.LinuxProcessDetailsVM;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;

/**
 * Service for sending emails.
 * <p>
 * We use the @Async annotation to send emails asynchronously.
 */
@Service
public class LinuxCommandService {

    private final Logger log = LoggerFactory.getLogger(LinuxCommandService.class);
    		
    private final ApplicationProperties applicationProperties;;

    private final AnalyzeOrderDetailsRepository analyzeOrderDetailsRepository;
    
    public LinuxCommandService(ApplicationProperties applicationProperties,AnalyzeOrderDetailsRepository analyzeOrderDetailsRepository) {
		super();
		this.applicationProperties = applicationProperties;
		this.analyzeOrderDetailsRepository = analyzeOrderDetailsRepository;
	}



	public void startAIScript(String sessionId,Boolean waitForOutput)  {
    	String sciptPath = applicationProperties.getAiScriptPath()+"/"+applicationProperties.getAiScriptName();
    	callScript(sciptPath,sessionId,false);
    }
	
	public List<String> callScript(String scriptPath,String sessionId,Boolean waitForOutput) {
		log.info(scriptPath+ " script will be call");
    	
		List<String> result = new ArrayList<String>();
		
    	String[] cmd = { "python", 
    			scriptPath, 
				"--sessionId",
				sessionId, };
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			if(waitForOutput)
				result = getOutput(p);
			log.info(scriptPath+ " script called");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(scriptPath+ " script called but there is errror",e);
		}
		
		return result;
		
	}
	
	public List<LinuxProcessDetailsVM> callRunningProcess(String scriptName) {
		log.info("callRunningProcess"+ " started");
    	
		List<LinuxProcessDetailsVM> result = new ArrayList<LinuxProcessDetailsVM>();
		
		String[] cmd = { "/bin/sh", "-c", "ps -eo etimes=,user,pid,cmd | grep pipeline" };
    			
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			List<String> output = getOutput(p);
			result = parseScriptOutput(output,scriptName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("callRunningProcess"+ "  errror",e);
		}
		
		log.info("callRunningProcess"+ " ended.line size:"+result.size());
		return result;
		
	}
	
	public void killProcess(String pid) {
		
		log.info("callRunningProcess"+ " started. pid="+pid);
		
		//String[] cmd = { "kill -9 "+ pid };
		String[] cmd = { "/bin/sh", "-c", "kill -9 " +pid };
		
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			List<String> output = getOutput(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("callRunningProcess"+ "  errror",e);
		}
		

		log.info("callRunningProcess"+ " finished. pid="+pid);
	}
	
	private List<LinuxProcessDetailsVM> parseScriptOutput(List<String> output,String scriptName){
		List<LinuxProcessDetailsVM> result = new ArrayList<LinuxProcessDetailsVM>();
		
		for (Iterator iterator = output.iterator(); iterator.hasNext();) {
			String temp = (String) iterator.next();
			if(temp.contains("python") && temp.contains(scriptName)) {
				String resultStr = temp.replaceAll("[ ]+", " ");
				System.out.println(resultStr);
				String[] fields = resultStr.split(" ");
				
				
				LinuxProcessDetailsVM detailsVM = new LinuxProcessDetailsVM();
				detailsVM.setPid(new Long(fields[3]));
				detailsVM.setSessionId(fields[fields.length-1]);
				detailsVM.setDuration(new Long(fields[1]));
				result.add(detailsVM);
			}
			
		}
		
		return result;
	}
	
	private List<String> getOutput(Process p) throws IOException {
		
		 List<String> result = new ArrayList<String>();
		
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

		BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

		String s = null;
		// read the output from the command
		System.out.println("Here is the standard output of the command:\n");
		while ((s = stdInput.readLine()) != null) {
			log.info(s);
			result.add(s);
		}

		// read any errors from the attempted command
		System.out.println("Here is the standard error of the command (if any):\n");
		while ((s = stdError.readLine()) != null) {
			System.out.println(s);
		}
		
		return result;
	}
     
	//@Scheduled(fixedRate = 60000)//${fixedRate.in.milliseconds}
	public void killLongTimeProcess(String getScriptName) {
		log.info("killLongTimeProcess"+ " started");
		
		List<LinuxProcessDetailsVM> runningProcessList = callRunningProcess(getScriptName);
		log.info("runningProcessSize "+ runningProcessList.size() );
		for (Iterator iterator = runningProcessList.iterator(); iterator.hasNext();) {
			LinuxProcessDetailsVM linuxProcessDetailsVM = (LinuxProcessDetailsVM) iterator.next();
			if(linuxProcessDetailsVM.getDuration()>applicationProperties.getLongProcessKillTreshold())
				killProcess(linuxProcessDetailsVM.getPid().toString());
			    List<AnalyzeOrderDetails> analyzeOrderDetails = analyzeOrderDetailsRepository.findBySessionId(linuxProcessDetailsVM.getSessionId());
				for (Iterator iterator2 = analyzeOrderDetails.iterator(); iterator2.hasNext();) {
					AnalyzeOrderDetails analyzeOrderDetails2 = (AnalyzeOrderDetails) iterator2.next();
					analyzeOrderDetails2.setState(AnalyzeState.TERMINATED);
					analyzeOrderDetailsRepository.save(analyzeOrderDetails2);
					log.info("killLongTimeProcess-processKilled. pid:"+ linuxProcessDetailsVM.getPid());
				}
		}
	
		log.info("killLongTimeProcess"+ " ended");
	}
	
	@Scheduled(fixedRateString = "${application.longProcessFixedRate}000")
	public void killLongTimeProcess() {
		log.info("killLongTimeProcess"+ " started");
		
		List<LinuxProcessDetailsVM> runningProcessList = callRunningProcess(applicationProperties.getAiScriptName());
		log.info("runningProcessSize "+ runningProcessList.size() );
		for (Iterator iterator = runningProcessList.iterator(); iterator.hasNext();) {
			LinuxProcessDetailsVM linuxProcessDetailsVM = (LinuxProcessDetailsVM) iterator.next();
			if(linuxProcessDetailsVM.getDuration()>applicationProperties.getLongProcessKillTreshold()*60)
				killProcess(linuxProcessDetailsVM.getPid().toString());
			    List<AnalyzeOrderDetails> analyzeOrderDetails = analyzeOrderDetailsRepository.findBySessionId(linuxProcessDetailsVM.getSessionId());
				for (Iterator iterator2 = analyzeOrderDetails.iterator(); iterator2.hasNext();) {
					AnalyzeOrderDetails analyzeOrderDetails2 = (AnalyzeOrderDetails) iterator2.next();
					analyzeOrderDetails2.setState(AnalyzeState.TERMINATED);
					analyzeOrderDetailsRepository.save(analyzeOrderDetails2);
					log.info("killLongTimeProcess-processKilled. pid:"+ linuxProcessDetailsVM.getPid());
				}
		}
	
		log.info("killLongTimeProcess"+ " ended");
	}
}
