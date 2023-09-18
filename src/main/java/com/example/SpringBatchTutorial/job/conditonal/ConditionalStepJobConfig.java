package com.example.SpringBatchTutorial.job.conditonal;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobScope;
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

import static org.springframework.batch.repeat.RepeatStatus.FINISHED;

@Configuration
@RequiredArgsConstructor
public class ConditionalStepJobConfig {

    @Bean
    public Job ConditionalStepJob(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new JobBuilder("conditionalStepJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(conditionalStartStep(jobRepository, transactionManager))
                .on("FAILED").to(conditionalStartStep(jobRepository, transactionManager))
                .from(conditionalStartStep(jobRepository, transactionManager))
                .on("COMPLETED").to(conditionalCompletedStep(jobRepository, transactionManager))
                .from(conditionalStartStep(jobRepository, transactionManager))
                .on("*").to(conditionalAllStep(jobRepository, transactionManager))
                .end()
                .build();
    }


    @JobScope
    @Bean
    public Step conditionalAllStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("conditionalAllStep", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("conditional All step");
                        return FINISHED;
                    }
                }, transactionManager)
                .build();
    }


    @JobScope
    @Bean
    public Step conditionalCompletedStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("conditionalCompletedStep", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("conditional Completed Step");
                        return FINISHED;
                    }
                }, transactionManager)
                .build();

    }

    @JobScope
    @Bean
    public Step conditionalStartStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {

        return new StepBuilder("conditionalStartStep", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("conditional Start step");
                        return FINISHED;
                        // throw new Exception("! ! ! Exception ! ! !");
                    }
                }, transactionManager)
                .build();

    }

    @JobScope
    @Bean Step conditionalFailStep(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("conditionalFailStep", jobRepository)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        return FINISHED;
                    }
                }, transactionManager)
                .build();
    }

}
