/**
 * 
 */
package mayi.lagou.com.data;

import java.io.Serializable;

/**
 * @author wenfeili@163.com
 * 
 * @date 2014-4-8
 */
public class PositionDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 职位名称 */
	private String positionName;
	/** 公司 */
	private String company;
	/** 公司图标 */
	private String comIconUrl;
	/** 领域 */
	private String field;
	/** 阶段 */
	private String stage;
	/** 规模 */
	private String scale;
	/** 工作地址 */
	private String address;
	/** 薪资 */
	private String salary;
	/** 城市 */
	private String city;
	/** 工作经验 */
	private String experience;
	/** 学历 */
	private String education;
	/** 工作性质 */
	private String jobCategory;
	/** 职位诱惑 */
	private String jobTempt;
	/** 发布时间 */
	private String releaseTime;
	/** 职位详情 */
	private String jobDetail;

	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}

	/**
	 * @return the comIconUrl
	 */
	public String getComIconUrl() {
		return comIconUrl;
	}

	/**
	 * @param comIconUrl the comIconUrl to set
	 */
	public void setComIconUrl(String comIconUrl) {
		this.comIconUrl = comIconUrl;
	}

	/**
	 * @param field the field to set
	 */
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * @return the stage
	 */
	public String getStage() {
		return stage;
	}

	/**
	 * @param stage the stage to set
	 */
	public void setStage(String stage) {
		this.stage = stage;
	}

	/**
	 * @return the scale
	 */
	public String getScale() {
		return scale;
	}

	/**
	 * @param scale the scale to set
	 */
	public void setScale(String scale) {
		this.scale = scale;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the positionName
	 */
	public String getPositionName() {
		return positionName;
	}

	/**
	 * @param positionName
	 *            the positionName to set
	 */
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * @param company
	 *            the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * @return the releaseTime
	 */
	public String getReleaseTime() {
		return releaseTime;
	}

	/**
	 * @param releaseTime
	 *            the releaseTime to set
	 */
	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}

	/**
	 * @return the salary
	 */
	public String getSalary() {
		return salary;
	}

	/**
	 * @param salary
	 *            the salary to set
	 */
	public void setSalary(String salary) {
		this.salary = salary;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the experience
	 */
	public String getExperience() {
		return experience;
	}

	/**
	 * @param experience
	 *            the experience to set
	 */
	public void setExperience(String experience) {
		this.experience = experience;
	}

	/**
	 * @return the education
	 */
	public String getEducation() {
		return education;
	}

	/**
	 * @param education
	 *            the education to set
	 */
	public void setEducation(String education) {
		this.education = education;
	}

	/**
	 * @return the jobCategory
	 */
	public String getJobCategory() {
		return jobCategory;
	}

	/**
	 * @param jobCategory
	 *            the jobCategory to set
	 */
	public void setJobCategory(String jobCategory) {
		this.jobCategory = jobCategory;
	}

	/**
	 * @return the jobTempt
	 */
	public String getJobTempt() {
		return jobTempt;
	}

	/**
	 * @param jobTempt
	 *            the jobTempt to set
	 */
	public void setJobTempt(String jobTempt) {
		this.jobTempt = jobTempt;
	}

	/**
	 * @return the jobDetail
	 */
	public String getJobDetail() {
		return jobDetail;
	}

	/**
	 * @param jobDetail
	 *            the jobDetail to set
	 */
	public void setJobDetail(String jobDetail) {
		this.jobDetail = jobDetail;
	}

}
