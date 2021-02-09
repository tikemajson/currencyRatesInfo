package currencyratesinfo.service;

import java.math.BigDecimal;

public interface CurrencyRatesStrategy {
	public BigDecimal getRates(String s, BigDecimal quantity);
	public String[] getFormat();
}
