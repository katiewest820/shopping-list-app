package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

//import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GroceryItemHistoryController {

    //private final AtomicLong counter = new AtomicLong();

    @Autowired
    ItemHistoryRepository itemHistoryRepository;

    @Autowired
    MongoOperations mongoOperations;



    @RequestMapping(value = "/addItemHistory", method = POST)
    public ResponseEntity<ItemHistory> update(@RequestBody ItemHistory newItem){
        System.out.println(newItem.item);
        ItemHistory newItemHistory = mongoOperations.findOne(new Query(Criteria.where("item").is(newItem.item)), ItemHistory.class, "itemHistory");

        System.out.println(newItemHistory);
        if(newItemHistory == null){
            itemHistoryRepository.save(newItem);
            return ResponseEntity.ok(newItem);
        }else{

            System.out.println(newItemHistory.addedDate);
            newItemHistory.setAddedDate(newItem.addedDate);
            System.out.println(newItemHistory.addedDate);
            mongoOperations.save(newItemHistory);
            return ResponseEntity.ok(newItemHistory);
        }


    }

    @RequestMapping(value = "/allItemsFromHistory", method = RequestMethod.GET)
    public List<ItemHistory> getAllHistoricalItems(){

        List<ItemHistory> itemHistory = itemHistoryRepository.findAll();
        System.out.println(itemHistory);
        System.out.println(itemHistory.size());
        LocalDate cutoff = LocalDate.now().minusMonths(3);
        System.out.println(cutoff);
        for(int i = 0; i < itemHistory.size(); i++){

            ItemHistory newItem = itemHistory.get(i);
            System.out.println(newItem.addedDate);
            System.out.println(itemHistory.get(i));
            if(newItem.addedDate.isBefore(cutoff)){
                mongoOperations.findAndRemove(new Query(Criteria.where("id").is(newItem.id)), ItemHistory.class,"itemHistory");
            }
        }
        return itemHistoryRepository.findAll();
    }





}
