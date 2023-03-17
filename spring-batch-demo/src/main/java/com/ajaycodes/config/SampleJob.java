package com.ajaycodes.config;

import static org.springframework.batch.repeat.RepeatStatus.FINISHED;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SampleJob {
	
	
	private static final Logger log = LoggerFactory.getLogger(SampleJob.class);

	
	//JobBuilderFactory to create Job
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	//StepBuilerFactory to create Step
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

    // define bean of type job
    @Bean
    Job firstJob() {
		return jobBuilderFactory.get("First Job")
		.start(firstStep())
		.build();
	}

	// define a step
	private Step firstStep() {
		return stepBuilderFactory.get("First Step")
		.tasklet(firstTask())
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
}
