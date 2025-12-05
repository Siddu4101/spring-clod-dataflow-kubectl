package com.springbatch.demo.batch;

import com.springbatch.demo.dto.Person;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;

@Configuration
@EnableBatchProcessing
public class BatchConfig {


    @Bean
    public FlatFileItemReader<String> reader() {
        return new FlatFileItemReaderBuilder<String>()
                .name("fileReader")
                .resource(new ClassPathResource("input.txt")) // file in src/main/resources
                .lineMapper((line, lineNumber) -> line) // identity mapping
                .build();
    }

    @Bean
    public ItemProcessor<String, String> processor() {
        return p -> p.concat("Sid");
    }


    @Bean
    public FlatFileItemWriter<String> writer() {
        return new FlatFileItemWriterBuilder<String>()
                .name("textWriter")
                .resource(new FileSystemResource("output.txt"))
                .lineAggregator(s -> s)
                .build();
    }

    @Bean
    public Step step(JobRepository repo, PlatformTransactionManager tx) {
        return new StepBuilder("step", repo)
                .<String, String>chunk(5, tx)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job job(JobRepository repo, Step step) {
        return new JobBuilder("job", repo)
                .start(step)
                .build();
    }


}
