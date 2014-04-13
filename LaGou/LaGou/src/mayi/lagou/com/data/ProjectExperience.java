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
public class ProjectExperience implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String projectName;
	private String projectTime;
	private String projectDetail;

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName
	 *            the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @return the projectTime
	 */
	public String getProjectTime() {
		return projectTime;
	}

	/**
	 * @param projectTime
	 *            the projectTime to set
	 */
	public void setProjectTime(String projectTime) {
		this.projectTime = projectTime;
	}

	/**
	 * @return the projectDetail
	 */
	public String getProjectDetail() {
		return projectDetail;
	}

	/**
	 * @param projectDetail
	 *            the projectDetail to set
	 */
	public void setProjectDetail(String projectDetail) {
		this.projectDetail = projectDetail;
	}

}
