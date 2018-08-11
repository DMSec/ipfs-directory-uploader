package br.com.dmsec.idup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import br.com.dmsec.idup.property.FileStorageProperties;
import br.com.dmsec.idup.property.IpfsProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class, IpfsProperties.class
})
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
