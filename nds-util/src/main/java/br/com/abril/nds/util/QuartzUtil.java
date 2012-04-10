package br.com.abril.nds.util;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

/**
 * Classe utilitária para complexidades relacionadas ao Quartz.
 * 
 * @author Discover Technology
 *
 */
public class QuartzUtil {
	
	/**
	 * Remove os jobs atrelados ao grupo passado como parâmetro.
	 * 
	 * @param jobGroupName - nome do grupo
	 */
	public static void removeJobsFromGroup(String jobGroupName) {
		
		try {
			
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			
			GroupMatcher<JobKey> groupMatcher = GroupMatcher.groupContains(jobGroupName);
			
			for (JobKey jobKey : scheduler.getJobKeys(groupMatcher)) {
				
				scheduler.deleteJob(jobKey);
			}
			
		} catch (SchedulerException e) {

			throw new RuntimeException(e);
		}		
	}

}
