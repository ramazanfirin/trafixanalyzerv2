package com.masterteknoloji.trafficanalyzer.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

import com.masterteknoloji.trafficanalyzer.servlet.AngularFileManagerServlet;

//@Configuration
public class AngularFileBrowserConfiguration implements ServletContextInitializer, EmbeddedServletContainerCustomizer {

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
      registerServlet(servletContext);
  }

  private void registerServlet(ServletContext servletContext) {
//      ServletRegistration.Dynamic serviceServlet = servletContext.addServlet("ServiceConnect", new AngularFileManagerServlet());
//
//      serviceServlet.addMapping("/bridges/php/handler.php/**");
//      serviceServlet.setAsyncSupported(true);
//      serviceServlet.setLoadOnStartup(2);
  }

@Override
public void customize(ConfigurableEmbeddedServletContainer container) {
	// TODO Auto-generated method stub
	
}
}
