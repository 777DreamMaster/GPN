package club.gpn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GpnApplication {

    public static void main(String[] args) {
        SpringApplication.run(GpnApplication.class, args);
    }

}
