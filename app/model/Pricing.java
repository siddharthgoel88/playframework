package model;

import play.data.validation.Constraints.Required;

import com.google.code.morphia.annotations.Embedded;

@Embedded
public class Pricing {
	@Required
	public double price;
	
}
