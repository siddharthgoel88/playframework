package controllers;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import model.Inventory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;
import play.mvc.BodyParser.Json;
import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("RedMart Product Search"));
    }
    
    public static Result productSearch() {
    	ObjectNode response = play.libs.Json.newObject();
    	DynamicForm searchForm = Form.form().bindFromRequest();
    	String productId = searchForm.get("productid");
    	System.out.println("id =" + productId);
    	List<Inventory> matchingProducts = Inventory.findProducts(productId);
    	response.put("matching-ids", play.libs.Json.toJson(matchingProducts));
    	return ok(response);
    }
    
    @BodyParser.Of(Json.class)
    public static Result update() {
    	ObjectNode response = play.libs.Json.newObject();
    	JsonNode json = request().body().asJson();
    	if (json != null) {
    		String id = json.findPath("id").asText();
    		String title = json.findPath("title").asText();
    		String price = json.findPath("price").asText();
    		if(!verifyId(id)) {
    			response.put("success", false);
    			response.put("message", "Invalid id");
    			return badRequest(response);
    		} else if (!verifyTitle(title)) {
    			response.put("success", false);
    			response.put("message", "Invalid title");
    			return badRequest(response);
    		} else if (!verifyPrice(price)) {
    			response.put("success", false);
    			response.put("message", "Invalid price");
    			return badRequest(response);
    		}
    		
    		Inventory.updateDB(Integer.parseInt(id), title, Double.parseDouble(price));
    		response.put("success", true);
    		response.put("message", "DB updated successfully");
    		return ok(response);
    		
    	} else {
    		System.out.println("Empty json");
    	}
    	return ok(response);
    }

	private static boolean verifyId(String id) {
		if(Inventory.onlyOneIdExists(id)) {
			return true;
		}
		return false;
	}

	private static boolean verifyTitle(String title) {
		if (title != null && StringUtils.isAlphanumericSpace(title)) {
			return true;
		}
		return false;
	}
	
	private static boolean verifyPrice(String price) {
		if (price != null) {
			   try {
		            Double.parseDouble(price);
		            return true;
		        } catch (NumberFormatException e) {
		            return false;
		        }
		}
		return false;
	}

}
