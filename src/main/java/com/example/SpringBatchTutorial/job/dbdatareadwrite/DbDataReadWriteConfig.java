package com.example.SpringBatchTutorial.job.dbdatareadwrite;


import com.example.SpringBatchTutorial.core.domain.SendCoupon;
import com.example.SpringBatchTutorial.core.domain.User;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

@Configuration
@RequiredArgsConstructor
public class DbDataReadWriteConfig {


    @Autowired
    EntityManagerFactory em;


    @Bean
    public Job trMigrationJob(JobRepository jobRepository){

        return new JobBuilder("dbDataJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(dbDataStep(jobRepository, new PlatformTransactionManager() {
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
                        }
                ))
                .build();
    }

    @JobScope
    @Bean
    public Step dbDataStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager

    ) {
        return new StepBuilder("dbDataStep", jobRepository)
                .<User, SendCoupon>chunk(5, transactionManager)// 5개의 단위로
                .reader(dbDataReader())
                .processor(dbDataProcessor())
                .writer(dbDataWriter())
                .build();
    }


    @StepScope
    @Bean
    public JpaItemWriter<SendCoupon> dbDataWriter(){
        return new JpaItemWriterBuilder<SendCoupon>()
                .entityManagerFactory(em)
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<User, SendCoupon> dbDataProcessor(){
        return new ItemProcessor<User, SendCoupon>() {
            @Override
            public SendCoupon process(User user) throws Exception {
                return new SendCoupon(user);
            }
        };
    }

    @StepScope
    @Bean
    public JpaPagingItemReader<User> dbDataReader(){
        return new JpaPagingItemReaderBuilder<User>()
                .name("dbReader")
                .entityManagerFactory(em)
                .queryString("select o from User o")
                .pageSize(10)
                .build();
    }

}
