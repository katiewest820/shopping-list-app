package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GroceryListController {

    @Autowired
    GroceryListRepository groceryListRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MongoOperations mongoOperations;

    @RequestMapping(value = "/addGroceryList", method = RequestMethod.POST)
    public List<GroceryList> update(@RequestBody GroceryList newGroceryList){
        groceryListRepository.save(newGroceryList);
        System.out.println(newGroceryList.getName() + " successfully added");
        return groceryListRepository.findAll();
    }

    @RequestMapping(value = "/allGroceryLists", method = RequestMethod.GET)
    public List<GroceryList> getAllGroceryLists(){
        return groceryListRepository.findAll();
    }

    @RequestMapping(value = "/editGroceryList/{id}", method = RequestMethod.PUT)
    public List<GroceryList> editGroceryList(@PathVariable("id") String id, @RequestBody GroceryList editedGroceryList){
        long itemCount = mongoOperations.count(new Query(Criteria.where("groceryListId").is(editedGroceryList.id)), Item.class, "item");
        GroceryList groceryList = mongoOperations.findOne(new Query(Criteria.where("id").is(id)), GroceryList.class, "groceryList");
        groceryList.setItemCount(itemCount);
        groceryList.setActive(editedGroceryList.active);
        groceryList.setId(editedGroceryList.id);
        groceryList.setName(editedGroceryList.name);
        mongoOperations.save(groceryList);
        return groceryListRepository.findAll();
    }




}
