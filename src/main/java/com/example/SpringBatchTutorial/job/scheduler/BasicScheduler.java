package com.example.SpringBatchTutorial.job.scheduler;


import com.example.SpringBatchTutorial.job.basicjob.BasicJobConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class BasicScheduler {

    @Autowired
    private JobLauncher jobLauncher;

    private Job basicJobConfig;

    // sec - minute - hour - day of month - month - day of the week

    @Scheduled(cron = "10 * * * * *")
    public void BasicJobRun() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        JobParameters jobParameters = new JobParameters(

                Collections.singletonMap("requestTime", new JobParameter<>(System.currentTimeMillis(), Long.class)));

        jobLauncher.run(new Job() {
            @Override
            public String getName() {
                return "basicJobRun";
            }

            @Override
            public void execute(JobExecution execution) {
            }
        }, jobParameters);
    }

}
