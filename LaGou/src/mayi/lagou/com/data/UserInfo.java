/**
 * 
 */
package mayi.lagou.com.data;

import java.io.Serializable;
import java.util.List;

/**
 * @author wenfeili@163.com
 * 
 * @date 2014-4-11
 */
public class UserInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 基本信息 */
	private String basicInfo;
	/**姓名*/
	private String userName;
	/**学校*/
	private String userSchool;
	/** 头像 */
	private String userIcon;
	/** 期望工作 */
	private String jobExpect;
	/** 简历预览 */
	private String resumePreviewUrl;
	/** 简历上传 */
	private String uploadResumeUrl;
	/** 项目经验 */
	private List<ProjectExperience> projectExperience;
	/** 工作经历 */
	private List<JobExperience> jobExperience;
	/**一句话描述自己*/
	private String simpleDescription;
	/** 自我描述 */
	private String selfDescription;
	/** 作品展示 */
	private List<ProjectShow> projectShow;
	/** 教育经历 */
	private List<EducationExperirnce> educationExperience;

	/**
	 * @return the educationExperience
	 */
	public List<EducationExperirnce> getEducationExperience() {
		return educationExperience;
	}

	/**
	 * @param educationExperience
	 *            the educationExperience to set
	 */
	public void setEducationExperience(
			List<EducationExperirnce> educationExperience) {
		this.educationExperience = educationExperience;
	}

	/**
	 * @return the projectShow
	 */
	public List<ProjectShow> getProjectShow() {
		return projectShow;
	}

	/**
	 * @param projectShow
	 *            the projectShow to set
	 */
	public void setProjectShow(List<ProjectShow> projectShow) {
		this.projectShow = projectShow;
	}

	/**
	 * @return the selfDescription
	 */
	public String getSelfDescription() {
		return selfDescription;
	}

	/**
	 * @param selfDescription
	 *            the selfDescription to set
	 */
	public void setSelfDescription(String selfDescription) {
		this.selfDescription = selfDescription;
	}

	/**
	 * @return the jobPerience
	 */
	public List<JobExperience> getJobExperience() {
		return jobExperience;
	}

	/**
	 * @param jobPerience
	 *            the jobPerience to set
	 */
	public void setJobExperience(List<JobExperience> jobExperience) {
		this.jobExperience = jobExperience;
	}

	/**
	 * @return the projectExperience
	 */
	public List<ProjectExperience> getProjectExperience() {
		return projectExperience;
	}

	/**
	 * @param projectExperience
	 *            the projectExperience to set
	 */
	public void setProjectExperience(List<ProjectExperience> projectExperience) {
		this.projectExperience = projectExperience;
	}

	/**
	 * @return the expectJob
	 */
	public String getJobExpect() {
		return jobExpect;
	}

	/**
	 * @param expectJob
	 *            the expectJob to set
	 */
	public void setJobExpect(String jobExpect) {
		this.jobExpect = jobExpect;
	}

	/**
	 * @return the resumePreviewUrl
	 */
	public String getResumePreviewUrl() {
		return resumePreviewUrl;
	}

	/**
	 * @param resumePreviewUrl
	 *            the resumePreviewUrl to set
	 */
	public void setResumePreviewUrl(String resumePreviewUrl) {
		this.resumePreviewUrl = resumePreviewUrl;
	}

	/**
	 * @return the uploadResumeUrl
	 */
	public String getUploadResumeUrl() {
		return uploadResumeUrl;
	}

	/**
	 * @param uploadResumeUrl
	 *            the uploadResumeUrl to set
	 */
	public void setUploadResumeUrl(String uploadResumeUrl) {
		this.uploadResumeUrl = uploadResumeUrl;
	}

	/**
	 * @return the basicInfo
	 */
	public String getBasicInfo() {
		return basicInfo;
	}

	/**
	 * @param basicInfo
	 *            the basicInfo to set
	 */
	public void setBasicInfo(String basicInfo) {
		this.basicInfo = basicInfo;
	}

	/**
	 * @return the userIcon
	 */
	public String getUserIcon() {
		return userIcon;
	}

	/**
	 * @param userIcon
	 *            the userIcon to set
	 */
	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	public String getSimpleDescription() {
		return simpleDescription;
	}

	public void setSimpleDescription(String simpleDescription) {
		this.simpleDescription = simpleDescription;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserSchool() {
		return userSchool;
	}

	public void setUserSchool(String userSchool) {
		this.userSchool = userSchool;
	}

}
