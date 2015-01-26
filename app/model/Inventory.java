package model;

import java.util.ArrayList;
import java.util.List;

import play.data.validation.Constraints.Required;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Property;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;

import controllers.MorphiaObject;

@Entity("redmartCollection")
public class Inventory {
	@Id
	public String primaryKey;
	@Required
	@Property("id")
	public String id;
	@Required
	public String title;
	@Embedded
	public Pricing pricing;
	
    public static List<Inventory> all() {
        if (MorphiaObject.datastore != null) {
                return MorphiaObject.datastore.find(Inventory.class).asList();
        } else {
                return new ArrayList<Inventory>();
        }
    }
    
    public static List<Inventory> findProducts(String id) {
    	List<Inventory> response = null;
    	String pattern = "/.*" + id + ".*/.test(this.id)";
    	if (MorphiaObject.datastore != null) {
    		response = MorphiaObject.datastore.createQuery(Inventory.class).where(pattern).asList();
    		} else {
    			System.out.println("Something fishy in findProduct");
    		}
    	return response;
    }
    
    public static void updateDB(int id, String title, double cost) {
    	if (MorphiaObject.datastore != null) {
    		Query<Inventory> q = MorphiaObject.datastore.createQuery(Inventory.class).field("id").equal(id);
    		UpdateOperations<Inventory> updateTitle = MorphiaObject.datastore.createUpdateOperations(Inventory.class).set("title", title);
    		UpdateOperations<Inventory> updatePrice = MorphiaObject.datastore.createUpdateOperations(Inventory.class).set("pricing.price", cost);
    		MorphiaObject.datastore.update(q, updateTitle);
    		MorphiaObject.datastore.update(q, updatePrice);
    	} else {
    		System.out.println("Something fishy in updateDB");
    	}
    }
    
	public static boolean onlyOneIdExists(String id) {
		if (MorphiaObject.datastore != null) {
			int count = MorphiaObject.datastore.createQuery(Inventory.class).field("id").equal(Integer.parseInt(id)).asList().size();
			System.out.println(count);
			if(count == 1) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return id;
	}
}
