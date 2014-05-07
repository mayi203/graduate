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

	public static UserInfo parserUserInfo(String html) {
		Document doc = Jsoup.parse(html);
		UserInfo userInfo = new UserInfo();
		userInfo.setBasicInfo(getBasicInfo(doc));
		userInfo.setUserIcon(getUserIcon(doc));
		userInfo.setJobExpect(getJobExpect(doc));
		userInfo.setJobExperience(obtainJobExperience(doc));
		userInfo.setProjectExperience(obatinProjectExperience(doc));
		userInfo.setResumePreviewUrl(getResumePreviewUrl(doc));
		userInfo.setEducationExperience(obtainEducationExperience(doc));
		userInfo.setSelfDescription(getSelfDescription(doc));
		userInfo.setProjectShow(obtainProjectShow(doc));
		return userInfo;
	}

	/**
	 * @param doc
	 * @return
	 */
	private static List<EducationExperirnce> obtainEducationExperience(
			Document doc) {
		try {
			List<EducationExperirnce> educationExperience = new ArrayList<EducationExperirnce>();
			EducationExperirnce education = null;
			Elements educations = doc.select("ul.elist").select("div");
			for (int i = 0; i < educations.size(); i++) {
				education = new EducationExperirnce();
				education.setSchool(educations.get(i).select("h3").text()
						.trim());
				education
						.setMajor(educations.get(i).select("h4").text().trim());
				education.setEducationTime(doc.select("ul.elist")
						.select("span").get(i).text().trim());
				educationExperience.add(education);
			}
			return educationExperience;
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * @param doc
	 * @return
	 */
	private static List<ProjectShow> obtainProjectShow(Document doc) {

		List<ProjectShow> projectShows = new ArrayList<ProjectShow>();
		ProjectShow project = null;
		try {
			Elements projects = doc.select("div.workShow").select("div.f16");
			for (int i = 0; i < projects.size(); i++) {
				project = new ProjectShow();
				project.setProjectUrl(projects.get(i).select("a").text().trim());
				project.setProjectDetail(doc.select("div.workShow").select("p")
						.get(i).text().trim());
				projectShows.add(project);
			}
		} catch (IndexOutOfBoundsException e) {
		}
		return projectShows;
	}

	/**
	 * @param doc
	 * @return
	 */
	private static String getSelfDescription(Document doc) {
		try {
			return doc.select("div.descriptionShow").text().trim();
		} catch (NullPointerException e) {
			return "";
		}
	}

	/**
	 * @param doc
	 * @return
	 */
	private static List<JobExperience> obtainJobExperience(Document doc) {
		try {
			List<JobExperience> jobExperiences = new ArrayList<JobExperience>();
			JobExperience job = null;
			Elements jobs = doc.select("ul.wlist").select("div");
			for (int i = 0; i < jobs.size(); i++) {
				job = new JobExperience();
				job.setPositionName(jobs.get(i).select("h3").text().trim());
				job.setCompanyName(jobs.get(i).select("h4").text().trim());
				job.setIconUrl(jobs.get(i).select("img").attr("src"));
				job.setJobTime(doc.select("ul.wlist").select("span").get(i)
						.text().trim());
				jobExperiences.add(job);
			}
			return jobExperiences;
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * @param doc
	 * @return
	 */
	private static List<ProjectExperience> obatinProjectExperience(Document doc) {
		try {
			List<ProjectExperience> projects = new ArrayList<ProjectExperience>();
			ProjectExperience project = null;
			Elements projectList = doc.select("div.projectList");
			for (int i = 0; i < projectList.size(); i++) {
				project = new ProjectExperience();
				String proName = projectList.get(i).select("div.f16").text()
						.trim();
				proName.substring(0, proName.indexOf(" ")).trim();
				project.setProjectName(proName.substring(0,
						proName.indexOf(" ")).trim());
				project.setProjectTime(projectList.get(i).select("span").text()
						.trim());
				try {
					project.setProjectDetail(projectList.get(i)
							.select("div.dl1").text().trim());
				} catch (NullPointerException e) {
					project.setProjectDetail("");
				}
				projects.add(project);
			}
			return projects;
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * @param doc
	 * @return
	 */
	private static String getJobExpect(Document doc) {
		try {
			return doc.select("div.expectShow").select("span").text().trim();
		} catch (NullPointerException e) {
			return "";
		}
	}

	/**
	 * @param doc
	 * @return
	 */
	private static String getResumePreviewUrl(Document doc) {
		return doc.select("div.nameShow").select("a").attr("href");
	}

	/**
	 * @param doc
	 * @return
	 */
	private static String getUserIcon(Document doc) {
		return doc.select("div.basicShow").select("img").attr("src");
	}

	/**
	 * @param doc
	 * @return
	 */
	private static String getBasicInfo(Document doc) {
		try {
			String info = doc.select("div.basicShow").select("span").toString()
					.replace("<br />", BROKEN).trim();
			Document dom = Jsoup.parse(info);
			return dom.text().replace(BROKEN, "\n").trim();
		} catch (NullPointerException e) {
			return "";
		}
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
				delivers.add(deliver);
			}
			return delivers;
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
}
