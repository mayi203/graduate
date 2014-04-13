/**
 * 
 */
package mayi.lagou.com.data;

import java.io.Serializable;

/**
 * @author wenfeili@163.com
 * 
 * @date 2014-4-11
 */
public class JobExperience implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 工作时间 */
	private String jobTime;
	/** 职位 */
	private String positionName;
	/** 公司 */
	private String companyName;
	/** 公司图标 */
	private String iconUrl;

	/**
	 * @return the jobTime
	 */
	public String getJobTime() {
		return jobTime;
	}

	/**
	 * @param jobTime
	 *            the jobTime to set
	 */
	public void setJobTime(String jobTime) {
		this.jobTime = jobTime;
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
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName
	 *            the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the iconUrl
	 */
	public String getIconUrl() {
		return iconUrl;
	}

	/**
	 * @param iconUrl
	 *            the iconUrl to set
	 */
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

}
