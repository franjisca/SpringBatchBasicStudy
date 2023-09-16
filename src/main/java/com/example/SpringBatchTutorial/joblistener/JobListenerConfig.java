package com.example.SpringBatchTutorial.joblistener;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

@Configuration
@RequiredArgsConstructor
public class JobListenerConfig {


    @Bean
    public Job jobListenerJob(JobRepository jobRepository){
        return new JobBuilder("jobListenerJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(new JobLoggerListener())
                .start(jobListenerStep(jobRepository, new PlatformTransactionManager() {
                    @Override
                    public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
                        return null;
                    }

                    @Override
                    public void commit(TransactionStatus status) throws TransactionException {

                    }

                    @Override
                    public void rollback(TransactionStatus status) throws TransactionException {

                    }
                }))
                .build();
    }

    @JobScope
    @Bean
    public Step jobListenerStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("jobListenerStep", jobRepository)
                .tasklet(JobListenerTasklet(), transactionManager)
                .build();
    }

    @StepScope
    @Bean
    public Tasklet JobListenerTasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
               System.out.println("Job Listener Job called");
               return RepeatStatus.FINISHED;
            }
        };
    }
}
