package com.ajaycodes.config;

import static org.springframework.batch.repeat.RepeatStatus.FINISHED;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;

import com.ajaycodes.service.SecondTasklet;

@Configuration
public class SampleJob {

	private static final Logger log = LoggerFactory.getLogger(SampleJob.class);

	private SecondTasklet secondTasklet;
	
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    
	SampleJob(SecondTasklet secondTasklet, JobRepository jobRepository,
			PlatformTransactionManager transactionManager) {
		super();
		this.secondTasklet = secondTasklet;
		this.jobRepository = jobRepository;
		this.transactionManager = transactionManager;
	}

	// define bean of type job
	@Bean
	Job firstJob() {
		return new JobBuilder("First Job", jobRepository)
				// for first step
				.start(firstStep())
				// for next all steps
				.next(secondStep()).build();
	}

	// define a step
	@Bean
	Step firstStep() {
		return new StepBuilder("First Step", jobRepository)
				.tasklet(firstTask(), transactionManager)
				.build();
	}

	// define a tasklet
	private Tasklet firstTask() {
		// task inside the step
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println("This is first tasklet step");
				return FINISHED;
			}
		};
	}

	// define second step
	@Bean
	Step secondStep() {
	return new StepBuilder("Second Step", jobRepository)
			.tasklet(secondTasklet, transactionManager)
			.build();
	}

}
