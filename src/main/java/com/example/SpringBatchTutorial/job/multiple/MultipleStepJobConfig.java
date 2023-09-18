package com.example.SpringBatchTutorial.job.multiple;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class MultipleStepJobConfig {


    @Bean
    public Job multipleStepJob(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new JobBuilder("multipleStepJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(multipleStep1(jobRepository, transactionManager))
                .next(multipleStep2(jobRepository, transactionManager))
                .next(multipleStep3(jobRepository, transactionManager))
                .build();
    }

    @JobScope
    @Bean
    public Step multipleStep1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("multipleStep1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step1");
                    return RepeatStatus.FINISHED;
                }, transactionManager).build();
    }

    @JobScope
    @Bean
    public Step multipleStep2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("multipleStep2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step2");

                    ExecutionContext context = chunkContext
                            .getStepContext()
                            .getStepExecution()
                            .getJobExecution()
                            .getExecutionContext();

                    context.put("test", "hello");

                    return RepeatStatus.FINISHED;
                }, transactionManager).build();
    }

    @JobScope
    @Bean
    public Step multipleStep3(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("multipleStep3", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("test");

                    ExecutionContext context = chunkContext
                            .getStepContext()
                            .getStepExecution()
                            .getJobExecution()
                            .getExecutionContext();

                    System.out.println(context.get("someKey"));

                    return RepeatStatus.FINISHED;
                }, transactionManager).build();
    }






}
