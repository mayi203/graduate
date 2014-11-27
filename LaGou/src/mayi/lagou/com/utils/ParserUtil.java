package mayi.lagou.com.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mayi.lagou.com.data.DeliverFeedback;
import mayi.lagou.com.data.EducationExperirnce;
import mayi.lagou.com.data.JobExperience;
import mayi.lagou.com.data.Position;
import mayi.lagou.com.data.PositionDetail;
import mayi.lagou.com.data.ProjectExperience;
import mayi.lagou.com.data.ProjectShow;
import mayi.lagou.com.data.UserInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParserUtil {

	private static final String BROKEN = "BROKEN";

	public static List<Position> parserPosition(String html) {
		try {
			Document doc = Jsoup.parse(html);
			List<Position> positionList = new ArrayList<Position>();
			Position position = null;
			Elements positionNodes = doc.select("div.hot_pos_l");
			Elements companyNodes = doc.select("div.hot_pos_r");
			for (int i = 0; i < positionNodes.size(); i++) {
				position = new Position();
				position.setPositionName(getPositionName(positionNodes.get(i)));
				position.setPositionUrl(getPositionUrl(positionNodes.get(i)));
				position.setCity(getCity(positionNodes.get(i)));
				position.setMoney(getMoney(positionNodes.get(i)));
				position.setExperience(getPositionSpan(positionNodes.get(i), 2));
				position.setEducation(getPositionSpan(positionNodes.get(i), 3));
				position.setPositionTempt(getPositionSpan(positionNodes.get(i),
						4));
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
		} catch (NullPointerException e) {
			return null;
		}
	}

	private static List<String> getWeal(Element element) {
		List<String> wealList = new ArrayList<String>();
		try {
			int size = element.select("li").size();
			for (int i = 0; i < size; i++) {
				String weal = element.select("li").get(i).text()
						.replace(" ", "").trim();
				wealList.add(weal);
			}
			return wealList;
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	private static String getPositionSpan(Element element, int index) {
		try {
			return element.select("span").get(index).text().replace(" ", "")
					.trim();
		} catch (NullPointerException e) {
			return "";
		} catch (IndexOutOfBoundsException e) {
			return "";
		}
	}

	private static String getCompanySpan(Element element, int index) {
		try {
			return element.select("span").get(index).text().replace(" ", "")
					.trim();
		} catch (NullPointerException e) {
			return "";
		} catch (IndexOutOfBoundsException e) {
			return "";
		}
	}

	private static String getTimeOrScale(Element element) {
		try {
			int size = element.select("span").size();
			return element.select("span").get(size - 1).text().replace(" ", "")
					.trim();
		} catch (NullPointerException e) {
			return "";
		} catch (IndexOutOfBoundsException e) {
			return "";
		}
	}

	private static String getCompanyUrl(Element element) {
		try {
			return element.select("a").attr("href");
		} catch (NullPointerException e) {
			return "";
		}
	}

	private static String getCompany(Element element) {
		try {
			if (element.select("div.mb10") != null)
				return element.select("div.mb10").select("a").text()
						.replace(" ", "").trim();
			else
				return element.select("a").text().replace(" ", "").trim();
		} catch (NullPointerException e) {
			return "";
		}
	}

	private static String getMoney(Element element) {
		try {
			return element.select("span").get(1).text().replace(" ", "").trim();
		} catch (NullPointerException e) {
			return "";
		}
	}

	private static String getCity(Element element) {
		try {
			return element.select("span.c9").text().replace(" ", "").trim();
		} catch (NullPointerException e) {
			return "";
		}
	}

	private static String getPositionName(Element element) {
		try {
			return element.select("div.mb10").select("a").text()
					.replace(" ", "").trim();
		} catch (NullPointerException e) {
			return "";
		}
	}

	private static String getPositionUrl(Element element) {
		try {
			return element.select("div.mb10").select("a").attr("href");
		} catch (NullPointerException e) {
			return "";
		}
	}

	public static PositionDetail parserPositionDetail(String html) {
		PositionDetail positionDetail = new PositionDetail();
		Document doc = Jsoup.parse(html);
		try {
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
			positionDetail
					.setJobTempt(obtainDetailByRegular(
							doc.select("dd.job_request").get(0),
							"<br />([^<div>]*)", 0));
			positionDetail.setReleaseTime(getDetailReleaseTime(doc));
			positionDetail.setJobDetail(obtainJobDetail(doc));
			positionDetail.setSubmitValue(getSubmitValue(doc));
			positionDetail.setDeliverState(getDeliverState(doc));
			return positionDetail;
		} catch (NullPointerException e) {
			return null;
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * @param doc
	 * @return
	 */
	private static String getDeliverState(Document doc) {
		try {
			return doc.select("dl.job_detail").select("a.btn").text();
		} catch (NullPointerException e) {
			return "";
		}
	}

	/**
	 * @param doc
	 * @return
	 */
	private static String getSubmitValue(Document doc) {
		try {
			return doc.getElementById("resubmitToken").attr("value");
		} catch (NullPointerException e) {
			return "";
		}
	}

	/**
	 * @param doc
	 * @return
	 */
	private static String obtainJobDetail(Document doc) {
		try {
			String detail = doc.select("dd.job_bt").toString()
					.replace("<br />", "<br /> " + BROKEN)
					.replace("</h3>", "</h3> " + BROKEN)
					.replace("</p>", "</p> " + BROKEN)
					.replace("</li>", "</li> " + BROKEN).replace("&nbsp;", "");
			Document dom = Jsoup.parse(detail);
			return dom.text().replace(BROKEN, "\n").substring(4).trim();
		} catch (NullPointerException e) {
			return "";
		}
	}

	/**
	 * @param doc
	 * @return
	 */
	private static String getComIconUrl(Document doc) {
		try {
			return doc.select("dl.job_company").select("img.b2").attr("src")
					.toString().trim();
		} catch (NullPointerException e) {
			return "";
		}
	}

	/**
	 * @param doc
	 * @return
	 */
	private static String getDetailAddress(Document doc) {
		try {
			Elements divs = doc.select("dl.job_company").select("dd")
					.select("div");
			return divs.get(0).text().trim();
		} catch (IndexOutOfBoundsException e) {
			return "";
		}
	}

	/**
	 * @param doc
	 * @return
	 */
	private static String getDetailReleaseTime(Document doc) {
		try {
			return doc.select("dd.job_request").select("div").text().trim();
		} catch (NullPointerException e) {
			return "";
		}
	}

	/**
	 * @param doc
	 * @return
	 */
	private static String obtainDetailByRegular(Element source, String tag,
			int index) {
		List<String> info = new ArrayList<String>();
		try {
			Pattern pattern = Pattern.compile(tag);
			Matcher matcher = pattern.matcher(source.toString());
			while (matcher.find()) {
				info.add(matcher.group(1));
			}
			return info.get(index).trim();
		} catch (IndexOutOfBoundsException e) {
			return "";
		}
	}

	/**
	 * @param doc
	 * @return
	 */
	private static String getDetailSpan(Elements spans, int index) {
		try {
			return spans.get(index).text().trim();
		} catch (NullPointerException e) {
			return "";
		}
	}

	/**
	 * @param doc
	 * @return
	 */
	private static String getDetailSalary(Document doc) {
		try {
			return doc.select("dd.job_request").select("span.red").text()
					.trim();
		} catch (NullPointerException e) {
			return "";
		}
	}

	/**
	 * @param doc
	 * @return
	 */
	private static String getDetailPositionName(Document doc) {
		try {
			return doc.select("h1").attr("title").toString();
		} catch (NullPointerException e) {
			return "";
		}
	}

	private static Element getResumeHeaderElement(Document doc) {
		try {
			return doc.getElementById("mr_mr_head");
		} catch (Exception e) {
			return null;
		}
	}

	private static Element getResumeContentElement(Document doc) {
		try {
			return doc.select("div.mr_content").first();
		} catch (Exception e) {
			return null;
		}
	}

	private static String getSimpleDescription(Element element) {
		try {
			return element.select("div.mr_baseinfo").first()
					.select("div.mr_p_introduce").first().select("span").get(1)
					.text();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private static String getUserName(Element element) {
		try {
			return element.select("div.mr_p_name").first().select("span")
					.get(1).text();
		} catch (Exception e) {
			return "";
		}
	}

	private static String getBasicInfo(Element element) {
		try {
			return element.select("div.mr_p_info").first().select("div.info_t")
					.first().select("span").get(1).text();
		} catch (NullPointerException e) {
			return "";
		}
	}

	private static String getSchoolInfo(Element element) {
		try {
			return element.select("div.mr_p_info").first().select("div.info_t")
					.first().select("span").get(0).text();
		} catch (NullPointerException e) {
			return "";
		}
	}

	private static String getUserIcon(Element element) {
		try {
			return element.getElementById("userpic").attr("src");
		} catch (Exception e) {
			return "";
		}
	}

	private static String getSelfDescription(Element element) {
		try {
			return element.getElementById("selfDescription")
					.select("div.mr_moudle_content").first()
					.select("div.self_des_list").first()
					.select("div.mr_self_r").text();
		} catch (Exception e) {
			return "";
		}
	}

	private static String getJobException(Element element) {
		try {
			return element.getElementById("expectJob")
					.select("div.mr_moudle_content").first()
					.select("div.expectjob_list").first()
					.select("div.mr_job_info").first().select("ul.clearfixs")
					.text();
		} catch (Exception e) {
			return "";
		}
	}

	private static List<ProjectShow> getProjectShow(Element element) {
		try {
			Elements elements = element.getElementById("worksShow")
					.select("div.mr_moudle_content").first()
					.select("div.mr_work_online").first()
					.select("div.mr_wo_show");
			List<ProjectShow> list = new ArrayList<ProjectShow>();
			for (Element e : elements) {
				ProjectShow show = new ProjectShow();
				String url = e.select("div.mr_self_site").first().select("a")
						.first().attr("href");
				show.setProjectUrl(url);
				String detail = e.select("div.mr_wo_preview").first().text();
				show.setProjectDetail(detail);
				list.add(show);
			}
			return list;
		} catch (Exception e) {
			return null;
		}
	}

	private static List<ProjectExperience> getProjectExperience(Element element) {
		try {
			Elements elements = element.getElementById("projectExperience")
					.select("div.mr_moudle_content").first()
					.select("div.list_show").first().select("div.mr_jobe_list");
			List<ProjectExperience> list = new ArrayList<ProjectExperience>();
			for (Element e : elements) {
				ProjectExperience ex = new ProjectExperience();
				String projectName = e.select("div.clearfixs").first()
						.select("div.mr_content_l").first().select("div.l2")
						.select("a").first().text();
				ex.setProjectName(projectName);
				String projectTime = e.select("div.clearfixs").first()
						.select("div.mr_content_r").first().select("span")
						.first().text();
				ex.setProjectTime(projectTime);
				String projectDetail = e.select("div.mr_content_m").first()
						.text();
				ex.setProjectDetail(projectDetail);
				list.add(ex);
			}
			return list;
		} catch (Exception e) {
			return null;
		}
	}

	private static List<JobExperience> getJobExperience(Element element) {
		try {
			Elements elements = element.getElementById("workExperience")
					.select("div.mr_moudle_content").first()
					.select("div.list_show").first().select("div.mr_jobe_list");
			List<JobExperience> list = new ArrayList<JobExperience>();
			for (Element e : elements) {
				JobExperience job = new JobExperience();
				String jobTime = e.select("div.clearfixs").first()
						.select("div.mr_content_r").first().select("span")
						.text();
				job.setJobTime(jobTime);
				String positionName = e.select("div.clearfixs").first()
						.select("div.mr_content_l").first().select("div.l2").first()
						.select("span").text();
				job.setPositionName(positionName);
				String companyName = e.select("div.clearfixs").first()
						.select("div.mr_content_l").first().select("div.l2").first()
						.select("h4").text();
				job.setCompanyName(companyName);
				String iconUrl = e.select("div.clearfixs").first()
						.select("div.mr_content_l").first().select("div.l1").first()
						.select("img").attr("src");
				job.setIconUrl(iconUrl);
				list.add(job);
			}
			return list;
		} catch (Exception e) {
			return null;
		}
	}

	private static List<EducationExperirnce> getEducationExperirnce(Element element){
		try {
			Elements elements = element.getElementById("educationalBackground")
					.select("div.mr_moudle_content").first()
					.select("div.list_show").first().select("div.clearfixs");
			List<EducationExperirnce> list = new ArrayList<EducationExperirnce>();
			for (Element e : elements) {
				EducationExperirnce edu = new EducationExperirnce();
				String school = e.select("div.mr_content_l").first()
						.select("div.l2").first().select("h4")
						.text();
				edu.setSchool(school);
				String major = e.select("div.mr_content_l").first()
						.select("div.l2").first().select("span")
						.text();
				edu.setMajor(major);
				String educationTime = e.select("div.mr_content_r").first()
						.select("span").text();
				edu.setEducationTime(educationTime);
				String iconUrl=e.select("div.mr_content_l").first()
						.select("div.l1").first().select("img")
						.attr("src");
				edu.setIconUrl(iconUrl);
				list.add(edu);
			}
			return list;
		} catch (Exception e) {
			return null;
		}
	}
	
	private static String getResumeMobile(Element element){
		try {
			return element.select("div.mr_p_info").first().select("div.info_b")
					.first().select("span").get(0).text();
		} catch (NullPointerException e) {
			return "";
		}
	}
	
	private static String getResumeEmail(Element element){
		try {
			return element.select("div.mr_p_info").first().select("div.info_b")
					.first().select("span").get(1).text();
		} catch (NullPointerException e) {
			return "";
		}
	}
	
	public static UserInfo parserUserInfo(String html) {
		Document doc = Jsoup.parse(html);
		Element headerElement = getResumeHeaderElement(doc);
		Element headerBaseElement = headerElement.select("div.mr_baseinfo")
				.first();
		Element containElement = getResumeContentElement(doc);
		UserInfo userInfo = new UserInfo();
		userInfo.setSimpleDescription(getSimpleDescription(headerElement));
		userInfo.setUserName(getUserName(headerBaseElement));
		userInfo.setBasicInfo(getBasicInfo(headerBaseElement));
		userInfo.setUserSchool(getSchoolInfo(headerBaseElement));
		userInfo.setUserIcon(getUserIcon(headerElement));
		userInfo.setSelfDescription(getSelfDescription(containElement));
		userInfo.setJobExpect(getJobException(containElement));
		userInfo.setProjectShow(getProjectShow(containElement));
		userInfo.setProjectExperience(getProjectExperience(containElement));
		userInfo.setJobExperience(getJobExperience(containElement));
		userInfo.setEducationExperience(getEducationExperirnce(containElement));
		userInfo.setMobile(getResumeMobile(headerBaseElement));
		userInfo.setEmail(getResumeEmail(headerBaseElement));
		return userInfo;
	}

	public static String parseResumeDialogText(String html) {
		Document doc = Jsoup.parse(html);
		return doc.text().replace(" ", "").trim();
	}

	public static List<DeliverFeedback> parseDeliverFeedback(String html) {
		List<DeliverFeedback> delivers = new ArrayList<DeliverFeedback>();
		DeliverFeedback deliver = null;
		Document doc = Jsoup.parse(html);
		try {
			Elements positions = doc.select("div.d_item");
			for (int i = 0; i < positions.size(); i++) {
				deliver = new DeliverFeedback();
				deliver.setPosition(positions.get(i).select("h2").select("em")
						.text().trim());
				deliver.setSalary(positions.get(i).select("h2").select("span")
						.text().trim());
				deliver.setCompany(positions.get(i).select("a.d_jobname")
						.text().trim());
				deliver.setDeliverTime(positions.get(i).select("span.d_time")
						.text().trim());
				deliver.setResume(positions.get(i).select("div.d_resume")
						.text().trim());
				deliver.setProgress(positions.get(i)
						.select("a.btn_showprogress").text().trim());
				deliver.setPositionUrl(positions.get(i).select("h2")
						.select("a").attr("href").trim());
				delivers.add(deliver);
			}
			return delivers;
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
}
