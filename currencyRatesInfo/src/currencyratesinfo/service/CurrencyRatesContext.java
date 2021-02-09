package currencyratesinfo.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import currencyratesinfo.exception.DataNotFoundException;
import currencyratesinfo.exception.FormatNotFoundException;
import currencyratesinfo.exception.InvalidCurrencyException;
import currencyratesinfo.exception.TimeOutException;

public class CurrencyRatesContext {
	private CurrencyRatesStrategy strategy;
	private String URL = "http://api.nbp.pl/api/exchangerates/tables/A/";
	private String Rates_URL = "http://api.nbp.pl/api/exchangerates/rates/A/"; 
	private final CloseableHttpClient closableHttpClient = HttpClients.createDefault();
	
	public void set(CurrencyRatesStrategy strategy) {
		this.strategy = strategy;
	}
	
	public Date getDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}
	
	public Date getActualDateRates(Date date) {
		Date tempDate = date;
		boolean res = response(URL + new SimpleDateFormat("yyyy-MM-dd").format(tempDate));
		while(!res) {
			tempDate = getDate(tempDate);
			res = response(URL + new SimpleDateFormat("yyyy-MM-dd").format(tempDate));
		}
		return tempDate;
	}
	
	public boolean response(String URL) {
		HttpGet httpGet = new HttpGet(URL);
		try {
		CloseableHttpResponse closableHttpResponse = closableHttpClient.execute(httpGet);
			if(closableHttpResponse.getStatusLine().getStatusCode() == 200) {
				closableHttpResponse.close();
				return true;
			} else {
				httpGet.releaseConnection();
				return false;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void getActualRatesFile() {
		
	}
	
	public String getActualRatesURL(Date date, String code, String format) {
		HttpGet httpGet = new HttpGet(Rates_URL + code + "/" + new SimpleDateFormat("yyyy-MM-dd").format(date) + "/?format=" + format);
		CloseableHttpResponse closableHttpResponse;
		try {
			closableHttpResponse = closableHttpClient.execute(httpGet);
			HttpEntity httpEntity = closableHttpResponse.getEntity();
			if(closableHttpResponse.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(httpEntity);
			} else {
				throw new DataNotFoundException("Data not found.");
			}
		} catch (InvalidCurrencyException ce) {
			throw new InvalidCurrencyException("Invalid currency.");
		} catch (ConnectException te) {
			throw new TimeOutException("Connection timeout.");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public BigDecimal getCurrencyRates(Date date, String code, BigDecimal quantity) {
		if(strategy.getFormat()[0].equals("url")) {
			date = getActualDateRates(date);
			try {
				String s = getActualRatesURL(date, code, strategy.getFormat()[1]);
				BigDecimal value = strategy.getRates(s, quantity);
				return value;
			} catch(DataNotFoundException e) {
				throw new DataNotFoundException("Data not found");
			}
		}
		throw new FormatNotFoundException("Format not found.");
	}
}
