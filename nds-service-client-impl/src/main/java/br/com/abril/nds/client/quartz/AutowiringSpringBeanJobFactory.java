package br.com.abril.nds.client.quartz;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

public final class AutowiringSpringBeanJobFactory 
	extends SpringBeanJobFactory implements ApplicationContextAware {

	private transient AutowireCapableBeanFactory beanFactory;
	
    private boolean enableQuartzTasks;

	public void setEnableQuartzTasks(boolean enableQuartzTasks) {
		this.enableQuartzTasks = enableQuartzTasks;
	}

	public void setApplicationContext(final ApplicationContext context) {
		
		this.beanFactory = context.getAutowireCapableBeanFactory();
	}

	@Override
	protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
		
		if(!enableQuartzTasks) {
			return new Object();
		}
		
		final Object job = super.createJobInstance(bundle);
		
		this.beanFactory.autowireBean(job);
		
		return job;
	}
	
}
