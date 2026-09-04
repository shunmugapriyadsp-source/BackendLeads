package com.example.employee.api.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.LineMapper;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.employee.api.entity.Lead;
import com.example.employee.api.repository.LeadRepository;

@Configuration
public class BatchConfig {

	@Bean
	public Job employeeJob(JobRepository jobRepository, Step employeeStep) {

		return new JobBuilder("LeadJOB", jobRepository).start(employeeStep).build();
	}

	@Bean
	public Step employeeStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
			ItemReader<Lead> reader, ItemProcessor<Lead, Lead> processor, ItemWriter<Lead> writer) {

		return new StepBuilder("Lead", jobRepository).<Lead, Lead>chunk(100, transactionManager).reader(reader)
				.processor(processor).writer(writer).build();
	}

	@Bean
	public FlatFileItemReader<Lead> reader() {

		return new FlatFileItemReaderBuilder<Lead>().name("leadReader")
				.resource(new ClassPathResource("DecryptedLeadList.csv")).linesToSkip(1).maxItemCount(500)
				.lineMapper(lineMapper()).build();
	}

	@Bean
	public LineMapper<Lead> lineMapper() {
		return new LineMapper<Lead>() {

			@Override
			public Lead mapLine(String line, int lineNumber) {

				try {
					String[] data = line.split(",", -1);

					if (data.length < 14) {
						throw new IllegalArgumentException(
								"Line " + lineNumber + " has only " + data.length + " columns");
					}

					Lead lead = new Lead();

					lead.setId(data[0].trim());
					lead.setBranchCode(data[1].trim());
					lead.setChanelType(data[2].trim());
					lead.setCityCode(data[3].trim());
					lead.setConversationId(data[4].trim());

					String createdAt = data[5].trim();

					if (!createdAt.isEmpty() && !createdAt.equalsIgnoreCase("NULL")) {

						lead.setCreatedAt(LocalDateTime.parse(createdAt, DateTimeFormatter.ofPattern("M/d/yyyy H:mm")));
					}

					lead.setCustomeId(data[6].trim());
					lead.setEmail(data[7].trim());
					lead.setFirstName(data[8].trim());
					lead.setLastName(data[9].trim());
					lead.setMobileNumber(data[10].trim());
					lead.setPincode(data[11].trim());
					lead.setProductCode(data[12].trim());
					lead.setProductName(data[13].trim());

					return lead;

				} catch (Exception e) {

					System.out.println("CSV ERROR at line " + lineNumber + " : " + line);

					throw new RuntimeException("Failed to parse CSV line " + lineNumber, e);
				}
			}
		};
	}

	@Bean
	public ItemProcessor<Lead, Lead> processor() {

		return lead -> {

			// Skip record if ID is empty
			if (lead.getId() == null || lead.getId().isBlank()) {
				return null;
			}

			// Remove unwanted spaces
			if (lead.getEmail() != null) {
				lead.setEmail(lead.getEmail().trim());
			}

			if (lead.getFirstName() != null) {
				lead.setFirstName(lead.getFirstName().trim());
			}

			if (lead.getLastName() != null) {
				lead.setLastName(lead.getLastName().trim());
			}

			// Return the processed record
			return lead;
		};
	}

	@Bean
	public ItemWriter<Lead> writer(LeadRepository leadRepository) {

		return items -> {

			for (Lead lead : items) {
				leadRepository.save(lead);
			}

			System.out.println("Inserted " + items.size() + " records");
		};
	}
}