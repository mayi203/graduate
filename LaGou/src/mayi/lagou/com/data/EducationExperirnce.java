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
public class EducationExperirnce implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 学校 */
	private String school;
	/** 专业 */
	private String major;
	/** 教育时间 */
	private String educationTime;
	/**图标*/
	private String iconUrl;

	/**
	 * @return the school
	 */
	public String getSchool() {
		return school;
	}

	/**
	 * @param school
	 *            the school to set
	 */
	public void setSchool(String school) {
		this.school = school;
	}

	/**
	 * @return the major
	 */
	public String getMajor() {
		return major;
	}

	/**
	 * @param major
	 *            the major to set
	 */
	public void setMajor(String major) {
		this.major = major;
	}

	/**
	 * @return the educationTime
	 */
	public String getEducationTime() {
		return educationTime;
	}

	/**
	 * @param educationTime
	 *            the educationTime to set
	 */
	public void setEducationTime(String educationTime) {
		this.educationTime = educationTime;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

}
