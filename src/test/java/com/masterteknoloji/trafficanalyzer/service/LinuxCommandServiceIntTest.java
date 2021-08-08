package com.masterteknoloji.trafficanalyzer.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.spring4.SpringTemplateEngine;

import com.masterteknoloji.trafficanalyzer.Trafficanalzyzerv2App;
import com.masterteknoloji.trafficanalyzer.domain.AnalyzeOrder;
import com.masterteknoloji.trafficanalyzer.domain.AnalyzeOrderDetails;
import com.masterteknoloji.trafficanalyzer.domain.enumeration.AnalyzeState;
import com.masterteknoloji.trafficanalyzer.repository.AnalyzeOrderDetailsRepository;
import com.masterteknoloji.trafficanalyzer.repository.AnalyzeOrderRepository;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.LinuxProcessDetailsVM;

import io.github.jhipster.config.JHipsterProperties;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Trafficanalzyzerv2App.class)
public class LinuxCommandServiceIntTest {

	@Autowired
	private JHipsterProperties jHipsterProperties;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Spy
	private JavaMailSenderImpl javaMailSender;

	@Captor
	private ArgumentCaptor messageCaptor;

	@Autowired
	private LinuxCommandService linuxCommandService;

	@Autowired
	private AnalyzeOrderRepository analyzeOrderRepository;

	@Autowired
	private AnalyzeOrderDetailsRepository analyzeOrderDetailsRepository;
	
	Random rand = new Random();
	
	@Before
	public void setup() {

	}

	@Test
	public void callAIScript() {
		String getScriptName = "scripts/pipelinetest.py";

		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(getScriptName).getFile());
		String absolutePath = file.getAbsolutePath();

		Long sessionId = rand.nextLong();
		List<String> result = linuxCommandService.callScript(absolutePath, sessionId.toString(),true);
	
		assertThat(result.get(1)).isEqualTo("--sessionId");
		assertThat(result.get(2)).isEqualTo(sessionId.toString());
		
	}
	
	@Test
	public void callAIScriptWithWait() {
		String getScriptName = "scripts/pipelinewithtime.py";

		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(getScriptName).getFile());
		String absolutePath = file.getAbsolutePath();

		Integer sessionId = rand.nextInt(100);
		List<String> result = linuxCommandService.callScript(absolutePath, sessionId.toString(),false);
	
		List<LinuxProcessDetailsVM> runningProcessList = linuxCommandService.callRunningProcess(getScriptName);
		for (java.util.Iterator iterator = runningProcessList.iterator(); iterator.hasNext();) {
			LinuxProcessDetailsVM linuxProcessDetailsVM = (LinuxProcessDetailsVM) iterator.next();
			System.out.println(linuxProcessDetailsVM.getSessionId());
			assertThat(linuxProcessDetailsVM.getSessionId()).isEqualTo(sessionId.toString());
		}
	
		System.out.println("bitti");
		
	}
	
	@Test
	public void callAIScriptWithWaitMoteThanOnce() {
		String getScriptName = "scripts/pipelinewithtime.py";

		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(getScriptName).getFile());
		String absolutePath = file.getAbsolutePath();

		Integer sessionId = rand.nextInt(100);
		List<String> result = linuxCommandService.callScript(absolutePath, sessionId.toString(),false);
		
		sessionId = rand.nextInt(100);
		List<String> result2 = linuxCommandService.callScript(absolutePath, sessionId.toString(),false);
		
		sessionId = rand.nextInt(100);
		List<String> result3 = linuxCommandService.callScript(absolutePath, sessionId.toString(),false);
		
		List<LinuxProcessDetailsVM> runningProcessList = linuxCommandService.callRunningProcess(getScriptName);
		assertThat(runningProcessList.size()).isEqualTo(3);
		
//		for (java.util.Iterator iterator = runningProcessList.iterator(); iterator.hasNext();) {
//			LinuxProcessDetailsVM linuxProcessDetailsVM = (LinuxProcessDetailsVM) iterator.next();
//			System.out.println(linuxProcessDetailsVM.getSessionId());
//			assertThat(linuxProcessDetailsVM.getSessionId()).isEqualTo(sessionId.toString());
//		}
	
		System.out.println("bitti");
		
	}
	
	@Test
	public void callKillProcess() {
		String getScriptName = "scripts/pipelinewithtime.py";

		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(getScriptName).getFile());
		String absolutePath = file.getAbsolutePath();

		Integer sessionId = rand.nextInt(100);
		List<String> result = linuxCommandService.callScript(absolutePath, sessionId.toString(),false);
		
		sessionId = rand.nextInt(100);
		List<String> result2 = linuxCommandService.callScript(absolutePath, sessionId.toString(),false);
		
		sessionId = rand.nextInt(100);
		List<String> result3 = linuxCommandService.callScript(absolutePath, sessionId.toString(),false);
		
		List<LinuxProcessDetailsVM> runningProcessList = linuxCommandService.callRunningProcess(getScriptName);
		assertThat(runningProcessList.size()).isEqualTo(3);
		
		linuxCommandService.killProcess(runningProcessList.get(0).getPid().toString());
		
		runningProcessList = linuxCommandService.callRunningProcess(getScriptName);
		assertThat(runningProcessList.size()).isEqualTo(2);
		
//		for (java.util.Iterator iterator = runningProcessList.iterator(); iterator.hasNext();) {
//			LinuxProcessDetailsVM linuxProcessDetailsVM = (LinuxProcessDetailsVM) iterator.next();
//			System.out.println(linuxProcessDetailsVM.getSessionId());
//			assertThat(linuxProcessDetailsVM.getSessionId()).isEqualTo(sessionId.toString());
//		}
	
		System.out.println("bitti");
		
	}
	
	@Test
	public void killLongTimeProcess() throws InterruptedException {
		
		AnalyzeOrder analyzeOrder = new AnalyzeOrder();;
		
		AnalyzeOrderDetails analyzeOrderDetails = new AnalyzeOrderDetails();
		analyzeOrderDetails.setState(AnalyzeState.STARTED);
		Integer sessionId1 = rand.nextInt(100);
		analyzeOrderDetails.setSessionId(sessionId1.toString());
		analyzeOrderDetailsRepository.save(analyzeOrderDetails);
		
		analyzeOrder.setOrderDetails(analyzeOrderDetails);
		analyzeOrderRepository.save(analyzeOrder);
		
		
		AnalyzeOrder analyzeOrder2 = new AnalyzeOrder();;
		
		AnalyzeOrderDetails analyzeOrderDetails2 = new AnalyzeOrderDetails();
		analyzeOrderDetails2.setState(AnalyzeState.STARTED);
		Integer sessionId2 = rand.nextInt(100);
		analyzeOrderDetails.setSessionId(sessionId2.toString());
		
		analyzeOrderDetailsRepository.save(analyzeOrderDetails2);
		
		analyzeOrder.setOrderDetails(analyzeOrderDetails2);
		analyzeOrderRepository.save(analyzeOrder2);
		
		
		String getScriptName = "scripts/pipelinewithtime.py";

		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(getScriptName).getFile());
		String absolutePath = file.getAbsolutePath();

		
		List<String> result = linuxCommandService.callScript(absolutePath, sessionId1.toString(),false);
		
		Thread.sleep(10000);
		
		List<String> result2 = linuxCommandService.callScript(absolutePath, sessionId1.toString(),false);
		
		List<LinuxProcessDetailsVM> runningProcessList = linuxCommandService.callRunningProcess(getScriptName);
		assertThat(runningProcessList.size()).isEqualTo(2);
		
		linuxCommandService.killLongTimeProcess(getScriptName);
		
		runningProcessList = linuxCommandService.callRunningProcess(getScriptName);
		assertThat(runningProcessList.size()).isEqualTo(1);
		
		
		AnalyzeOrderDetails resultValue = analyzeOrderDetailsRepository.findOne(analyzeOrderDetails.getId());
				assertThat(resultValue.getState().toString()).isEqualTo(AnalyzeState.TERMINATED.toString());
				
		System.out.println("bitti");
		
	}

}
