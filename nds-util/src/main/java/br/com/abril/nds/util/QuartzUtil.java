package br.com.abril.nds.util;

import java.util.List;
import java.util.Set;

import org.quartz.JobKey;
import org.quartz.Scheduler;
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
			
			Set<JobKey> jobKeys = findJobKeysFromGroup(jobGroupName);
			
			for (JobKey jobKey : jobKeys) {
				
				scheduler.deleteJob(jobKey);
			}
			
		} catch (Exception e) {

			throw new RuntimeException(e);
		}		
	}
	
	/**
	 * Obtém as chaves dos jobs de um grupo.
	 * 
	 * @param jobGroupName - nome do grupo
	 * 
	 * @return {@link List} de {@link JobKey}
	 */
	public static Set<JobKey> findJobKeysFromGroup(String jobGroupName) {
		
		try {
			
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			
			GroupMatcher<JobKey> groupMatcher = GroupMatcher.groupContains(jobGroupName);
			
			return scheduler.getJobKeys(groupMatcher);

		} catch (Exception e) {

			throw new RuntimeException(e);
		}	
	}

}
