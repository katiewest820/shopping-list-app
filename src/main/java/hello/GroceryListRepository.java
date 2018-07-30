package hello;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface GroceryListRepository extends MongoRepository<GroceryList, String> {

}
