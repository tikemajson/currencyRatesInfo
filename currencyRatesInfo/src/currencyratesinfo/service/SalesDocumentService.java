package currencyratesinfo.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SalesDocumentService {
	public static void insert() {
		String dateString = "2020-12-27";
		Date date;
		BigDecimal value;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		CurrencyRatesContext currencyRatesContext = new CurrencyRatesContext();
		currencyRatesContext.set(new CurrencyDataFromJson());
		value = currencyRatesContext.getCurrencyRates(date, "USD", new BigDecimal("1"));
		System.out.println(value);
	}
	
	public static void main(String[] args) {
		insert();
	}
}
