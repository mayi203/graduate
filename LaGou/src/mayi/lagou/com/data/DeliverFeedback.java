/**
 * 
 */
package mayi.lagou.com.data;

import java.io.Serializable;

/**
 * @author wenfeili@163.com
 * 
 * @date 2014-4-13
 */
public class DeliverFeedback implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 职位 */
	private String position;
	/** 薪水 */
	private String salary;
	/** 公司 */
	private String company;
	/** 投递时间 */
	private String deliverTime;
	/** 简历 */
	private String resume;
	/** 进度 */
	private String progress;

	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
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
	 * @return the deliverTime
	 */
	public String getDeliverTime() {
		return deliverTime;
	}

	/**
	 * @param deliverTime
	 *            the deliverTime to set
	 */
	public void setDeliverTime(String deliverTime) {
		this.deliverTime = deliverTime;
	}

	/**
	 * @return the resume
	 */
	public String getResume() {
		return resume;
	}

	/**
	 * @param resume
	 *            the resume to set
	 */
	public void setResume(String resume) {
		this.resume = resume;
	}

	/**
	 * @return the progress
	 */
	public String getProgress() {
		return progress;
	}

	/**
	 * @param progress
	 *            the progress to set
	 */
	public void setProgress(String progress) {
		this.progress = progress;
	}

}
