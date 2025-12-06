package com.springbatch.demo.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
public class BatchConfig {

    @Bean
    public ItemReader<String> reader() {
        return new  ListItemReader<>(List.of("apple", "banana", "orange"));
    }

    @Bean
    public ItemProcessor<String, String> processor() {
        return item -> item.toUpperCase();
    }

    @Bean
    public ItemWriter<String> writer() {
        return items -> items.forEach(System.out::println);
    }

    @Bean
    public Step simpleStep(JobRepository jobRepository,
                           PlatformTransactionManager transactionManager) {

        return new StepBuilder("simpleStep", jobRepository)
                .<String, String>chunk(2, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job simpleJob(JobRepository jobRepository, Step simpleStep) {
        return new JobBuilder("simpleJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(simpleStep)
                .build();
    }
}
