package io.oigres.ecomm.service.orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan( basePackageClasses = { Bootstrap.class } )
public class Bootstrap {
    
	public static void main(String[] args) {
		SpringApplication.run(Bootstrap.class, args);
	}

    // @Bean
    // public ApplicationRunner initializeData(UserJpaRepository plantRepository) throws Exception {
    //     return (ApplicationArguments args) -> {
    //         List<User> plants = Arrays.asList(
    //                 new User("subalpine fir", "abies lasiocarpa", "pinaceae"),
    //                 new User("sour cherry", "prunus cerasus", "rosaceae"),
    //                 new User("asian pear", "pyrus pyrifolia", "rosaceae"),
    //                 new User("chinese witch hazel", "hamamelis mollis", "hamamelidaceae"),
    //                 new User("silver maple", "acer saccharinum", "sapindaceae"),
    //                 new User("cucumber tree", "magnolia acuminata", "magnoliaceae"),
    //                 new User("korean rhododendron", "rhododendron mucronulatum", "ericaceae"),
    //                 new User("water lettuce", "pistia", "araceae"),
    //                 new User("sessile oak", "quercus petraea", "fagaceae"),
    //                 new User("common fig", "ficus carica", "moraceae")
    //         );
    //         plantRepository.saveAll(plants);
    //     };
    // }

    // @Bean
    // public ApplicationRunner buildIndex(Indexer indexer) throws Exception {
    //     return (ApplicationArguments args) -> {
    //         indexer.indexPersistedData(User.class);
    //     };
    // }
	
}
