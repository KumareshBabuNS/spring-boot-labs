package com.example;

import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@EnableDiscoveryClient
@SpringBootApplication
public class AddressBookServiceApplication {

	@Bean
	public Sampler defaultSampler() {
		return new AlwaysSampler();
	}

	public static void main(String[] args) {
		SpringApplication.run(AddressBookServiceApplication.class, args);
	}
}

@Data @Entity
class Contact{
	@Id @GeneratedValue
	Long id;
	String name;
	String contact;
	String address;
}

@RestResource
interface ContactRepository extends JpaRepository<Contact, Long>{

}