package hello;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface groceryListRepository extends MongoRepository<GroceryList, String> {

}
