package br.com.abril.nds.util;

import java.util.List;
import java.util.Set;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.impl.matchers.GroupMatcher;

/**
 * Classe utilitária para complexidades relacionadas ao Quartz.
 * 
 * @author Discover Technology
 *
 */
public class QuartzUtil {
	
	private static QuartzUtil INSTANCE = new QuartzUtil();
	
	private Scheduler scheduler;
	
	private QuartzUtil() {
		
	}
	
	public static QuartzUtil doAgendador(Scheduler scheduler) {
		
		INSTANCE.scheduler = scheduler;
		
		return INSTANCE;
	}
	
	/**
	 * Remove os jobs atrelados ao grupo passado como parâmetro.
	 * 
	 * @param jobGroupName - nome do grupo
	 */
	public void removeJobsFromGroup(String jobGroupName) {
		
		try {
			
			Set<JobKey> jobKeys = findJobKeysFromGroup(jobGroupName);
			
			for (JobKey jobKey : jobKeys) {
				
				this.scheduler.deleteJob(jobKey);
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
	public Set<JobKey> findJobKeysFromGroup(String jobGroupName) {
		
		try {
			
			GroupMatcher<JobKey> groupMatcher = GroupMatcher.groupContains(jobGroupName);
			
			return this.scheduler.getJobKeys(groupMatcher);

		} catch (Exception e) {

			throw new RuntimeException(e);
		}	
	}

}
