package mayi.lagou.com.utils;

import java.util.ArrayList;
import java.util.List;

import mayi.lagou.com.data.LaGouPosition;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParserUtil {

	public static List<LaGouPosition> parserPosition(String html) {
		Document doc = Jsoup.parse(html);
		List<LaGouPosition> positionList = new ArrayList<LaGouPosition>();
		LaGouPosition position = null;
		Elements positionNodes = doc.select("div.hot_pos_l");
		Elements companyNodes = doc.select("div.hot_pos_r");
		for (int i = 0; i < positionNodes.size(); i++) {
			position = new LaGouPosition();
			position.setPositionName(getPositionName(positionNodes.get(i)));
			position.setPositionUrl(getPositionUrl(positionNodes.get(i)));
			position.setCity(getCity(positionNodes.get(i)));
			position.setMoney(getMoney(positionNodes.get(i)));
			position.setExperience(getPositionSpan(positionNodes.get(i), 2));
			position.setEducation(getPositionSpan(positionNodes.get(i), 3));
			position.setPositionTempt(getPositionSpan(positionNodes.get(i), 4));
			position.setTime(getTimeOrScale(positionNodes.get(i)));
			position.setCompany(getCompany(companyNodes.get(i)));
			position.setCompanyUrl(getCompanyUrl(companyNodes.get(i)));
			position.setField(getCompanySpan(companyNodes.get(i), 0));
			position.setStage(getCompanySpan(companyNodes.get(i), 2));
			position.setScale(getTimeOrScale(companyNodes.get(i)));
			position.setWeal(getWeal(companyNodes.get(i)));
			positionList.add(position);
		}
		return positionList;
	}

	private static List<String> getWeal(Element element) {
		List<String> wealList = new ArrayList<String>();
		int size = element.select("li").size();
		for (int i = 0; i < size; i++) {
			String weal = element.select("li").get(i).text();
			wealList.add(weal);
		}
		return wealList;
	}

	private static String getPositionSpan(Element element, int index) {
		return element.select("span").get(index).text();
	}

	private static String getCompanySpan(Element element, int index) {
		return element.select("span").get(index).text();
	}

	private static String getTimeOrScale(Element element) {
		int size = element.select("span").size();
		return element.select("span").get(size - 1).text();
	}

	private static String getCompanyUrl(Element element) {
		return element.select("a").attr("href");
	}

	private static String getCompany(Element element) {
		if (element.select("div.mb10") != null)
			return element.select("div.mb10").select("a").text();
		else
			return element.select("a").text();
	}

	private static String getMoney(Element element) {
		return element.select("span").get(1).text();
	}

	private static String getCity(Element element) {
		return element.select("span.c9").text();
	}

	private static String getPositionName(Element element) {
		return element.select("div.mb10").select("a").text();
	}

	private static String getPositionUrl(Element element) {
		return element.select("div.mb10").select("a").attr("href");
	}
}
