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
public class UserInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String basicInfo;
	private String userIcon;

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

}
