package com.example;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableFeignClients
@SpringBootApplication
public class AddressBookClientApplication {

	@Bean
	public Sampler defaultSampler() {
		return new AlwaysSampler();
	}

	public static void main(String[] args) {
		SpringApplication.run(AddressBookClientApplication.class, args);
	}


}

@Data
class Contact{
	Long id;
	String name;
	String contact;
	String address;
}

@FeignClient(name = "address-book-service", fallback = AddressBookFallback.class)
interface AddressBookRemoteService {
	@RequestMapping("/contacts")
	Resources<Contact> getAllContacts();
}

@Service
class AddressBookFallback implements AddressBookRemoteService{
	public Resources<Contact> getAllContacts(){
		Contact c = new Contact();
		c.setName("default");
		c.setAddress("default");
		c.setContact("0000000");
		List<Contact> l = new ArrayList<>();
		l.add(c);
		Resources<Contact> rs = new Resources<>(l);
		return rs;
	}
}

@RestController
class ContactController{
	@Autowired
	AddressBookRemoteService addressBookRemoteService;

	@Autowired
	AddressBookFallback addressBookFallback;

	@RequestMapping("/names")
	public List<String> getAllNames(){
		return addressBookRemoteService
				.getAllContacts()
				.getContent()
				.stream()
				.map(c -> c.getName())
				.collect(Collectors.toList());
	}
}