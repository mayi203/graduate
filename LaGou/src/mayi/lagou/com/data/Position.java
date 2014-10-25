package mayi.lagou.com.data;

import java.io.Serializable;
import java.util.List;

public class Position implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 职位名称 */
	private String positionName;
	/** 对应url */
	private String positionUrl;
	/** 城市 */
	private String city;
	/** 薪资 */
	private String salary;
	/** 工作经验 */
	private String experience;
	/** 学历 */
	private String education;
	/** 职位诱惑 */
	private String positionTempt;
	/** 发布时间 */
	private String time;
	/** 公司 */
	private String company;
	/** 公司连接 */
	private String companyUrl;
	/** 领域 */
	private String field;
	/** 阶段 */
	private String stage;
	/** 规模 */
	private String scale;
	/** 福利 */
	private List<String> weal;

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getPositionTempt() {
		return positionTempt;
	}

	public void setPositionTempt(String positionTempt) {
		this.positionTempt = positionTempt;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public List<String> getWeal() {
		return weal;
	}

	public void setWeal(List<String> weal) {
		this.weal = weal;
	}

	public String getCompanyUrl() {
		return companyUrl;
	}

	public void setCompanyUrl(String companyUrl) {
		this.companyUrl = companyUrl;
	}

	public String getPositionName() {
		return positionName;
	}

	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getMoney() {
		return salary;
	}

	public void setMoney(String money) {
		this.salary = money;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getPositionUrl() {
		return positionUrl;
	}

	public void setPositionUrl(String positionUrl) {
		this.positionUrl = positionUrl;
	}

}
