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
public class ProjectShow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 作品链接 */
	private String projectUrl;
	/** 作品描述 */
	private String projectDetail;

	/**
	 * @return the projectUrl
	 */
	public String getProjectUrl() {
		return projectUrl;
	}

	/**
	 * @param projectUrl
	 *            the projectUrl to set
	 */
	public void setProjectUrl(String projectUrl) {
		this.projectUrl = projectUrl;
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
