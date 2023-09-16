package com.example.SpringBatchTutorial.filedatareadwrite;


import com.example.SpringBatchTutorial.core.dto.Player;
import com.example.SpringBatchTutorial.core.dto.PlayerYears;
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
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

@Configuration
@RequiredArgsConstructor
public class FileDataReadWriteConfig {

    @Bean
    public Job fileReadWriteJob(JobRepository jobRepository) throws Exception {

        return new JobBuilder("fileReadWriteJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(fileReadStep(jobRepository, new PlatformTransactionManager() {
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
    public Step fileReadStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager

    ) throws Exception {
        return new StepBuilder("fileReadWriteStep", jobRepository)
                .<Player, PlayerYears>chunk(5, transactionManager)
                .reader(playerFlatFileItemReader())
/*                .writer(new ItemWriter<Player>() {
                    @Override
                    public void write(Chunk<? extends Player> chunk) throws Exception {
                        chunk.forEach(i -> System.out.println(i));
                    }
                })*/
                .processor(playerYearsItemProcessor())
                .writer(playerFlatFileItemWriter())
                .build();
    }

    @StepScope
    @Bean
    public FlatFileItemWriter<PlayerYears> playerFlatFileItemWriter() {
        BeanWrapperFieldExtractor<PlayerYears> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"ID", "lastName", "position", "yearsExperience"});
        fieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<PlayerYears> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);

        FileSystemResource outputResource = new FileSystemResource("src/main/resources/players_output.txt");

        return new FlatFileItemWriterBuilder<PlayerYears>()
                .name("playerItemWriter")
                .resource(outputResource)
                .lineAggregator(lineAggregator)
                .build();
    }


    @StepScope
    @Bean
    public ItemProcessor<Player, PlayerYears> playerYearsItemProcessor() {
        return new ItemProcessor<Player, PlayerYears>() {
            @Override
            public PlayerYears process(Player item) throws Exception {
                return new PlayerYears(item);
            }
        };
    }

    @StepScope
    @Bean
    public FlatFileItemReader<Player> playerFlatFileItemReader() throws Exception {

        return new FlatFileItemReaderBuilder<Player>()
                .name("playerItemReader")
                .resource(new FileSystemResource("src/main/resources/players.csv"))
                .lineTokenizer(new DelimitedLineTokenizer())
                .linesToSkip(1)
                .fieldSetMapper(new PlayerFieldSetMapper())
                .build();
    }



}
