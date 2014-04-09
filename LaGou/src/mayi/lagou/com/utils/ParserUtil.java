package mayi.lagou.com.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mayi.lagou.com.data.LaGouPosition;
import mayi.lagou.com.data.PositionDetail;

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

	public static PositionDetail parserPositionDetail(String html) {
		PositionDetail positionDetail = new PositionDetail();
		Document doc = Jsoup.parse(html);
		Elements spans = doc.select("dd.job_request").select("span");
		positionDetail.setCompany(obtainDetailByRegular(
				doc.select("dl.job_company").select("h2").get(0),
				"<h2[^>]*>([^<img>]*)", 0));
		positionDetail.setComIconUrl(getComIconUrl(doc));
		positionDetail.setField(obtainDetailByRegular(
				doc.select("dl.job_company").select("ul.c_feature").get(0),
				"</span>([^</li>]*)", 0));
		positionDetail.setScale(obtainDetailByRegular(
				doc.select("dl.job_company").select("ul.c_feature").get(0),
				"</span>([^</li>]*)", 1));
		positionDetail.setStage(obtainDetailByRegular(
				doc.select("dl.job_company").select("ul.c_feature").get(1),
				"</span>([^</li>]*)", 0));
		positionDetail.setAddress(getDetailAddress(doc));
		positionDetail.setPositionName(getDetailPositionName(doc));
		positionDetail.setSalary(getDetailSalary(doc));
		positionDetail.setCity(getDetailSpan(spans, 1));
		positionDetail.setExperience(getDetailSpan(spans, 2));
		positionDetail.setEducation(getDetailSpan(spans, 3));
		positionDetail.setJobCategory(getDetailSpan(spans, 4));
		positionDetail.setJobTempt(obtainDetailByRegular(
				doc.select("dd.job_request").get(0), "<br />([^<div>]*)", 0));
		positionDetail.setReleaseTime(getDetailReleaseTime(doc));
		positionDetail.setJobDetail(obtainJobDetail(doc));
		return positionDetail;
	}

	/**
	 * @param doc
	 * @return
	 */
	private static String getComIconUrl(Document doc) {
		return doc.select("dl.job_company").select("h2").select("img")
				.attr("src").toString();
	}

	/**
	 * @param doc
	 * @return
	 */
	private static String getDetailAddress(Document doc) {
		Elements divs = doc.select("dl.job_company").select("dd").select("div");
		return divs.get(0).text();
	}

	/**
	 * @param doc
	 * @return
	 */
	private static List<String> obtainJobDetail(Document doc) {
		List<String> jobDetails = new ArrayList<String>();
		Elements ps = doc.select("dd.job_bt").select("p");
		for (int i = 0; i < ps.size(); i++) {
			jobDetails.add(ps.get(i).text());
		}
		return jobDetails;
	}

	/**
	 * @param doc
	 * @return
	 */
	private static String getDetailReleaseTime(Document doc) {
		return doc.select("dd.job_request").select("div").text();
	}

	/**
	 * @param doc
	 * @return
	 */
	private static String obtainDetailByRegular(Element source, String tag,
			int index) {
		List<String> info = new ArrayList<String>();
		Pattern pattern = Pattern.compile(tag);
		Matcher matcher = pattern.matcher(source.toString());
		while (matcher.find()) {
			info.add(matcher.group(1));
		}
		return info.get(index);
	}

	/**
	 * @param doc
	 * @return
	 */
	private static String getDetailSpan(Elements spans, int index) {
		return spans.get(index).text();
	}

	/**
	 * @param doc
	 * @return
	 */
	private static String getDetailSalary(Document doc) {
		return doc.select("dd.job_request").select("span.red").text();
	}

	/**
	 * @param doc
	 * @return
	 */
	private static String getDetailPositionName(Document doc) {
		return doc.select("h1").attr("title").toString();
	}
}
