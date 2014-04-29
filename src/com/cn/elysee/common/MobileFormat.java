package com.cn.elysee.common;

/**
 * @author hzx 2014��4��21��
 * @version V1.0
 */
public class MobileFormat
{
	/**
	 * �й��ƶ�ӵ�к����Ϊ:139,138,137,136,135,134,159,158,157(3G),151,150,188(3G),187(3G
	 * );13���Ŷ� �й���ͨӵ�к����Ϊ:130,131,132,156(3G),186(3G),185(3G);6���Ŷ�
	 * �й�����ӵ�к����Ϊ:133,153,189(3G),180(3G);4�������
	 */
	private static String regMobileStr = "^1(([3][456789])|([5][01789])|([8][78]))[0-9]{8}$";
	private static String regMobile3GStr = "^((157)|(18[78]))[0-9]{8}$";
	private static String regUnicomStr = "^1(([3][012])|([5][6])|([8][56]))[0-9]{8}$";
	private static String regUnicom3GStr = "^((156)|(18[56]))[0-9]{8}$";
	private static String regTelecomStr = "^1(([3][3])|([5][3])|([8][09]))[0-9]{8}$";
	private static String regTelocom3GStr = "^(18[09])[0-9]{8}$";
	private static String regPhoneString = "^(?:13\\d|15\\d)\\d{5}(\\d{3}|\\*{3})$";

	private String mobile = "";
	private int facilitatorType = 0;
	private boolean isLawful = false;
	private boolean is3G = false;

	public MobileFormat(String mobile)
	{
		this.setMobile(mobile);
	}

	public void setMobile(String mobile)
	{
		if (mobile == null) { return; }
		/**
		 * ��һ���ж��й��ƶ�
		 */
		if (mobile.matches(MobileFormat.regMobileStr))
		{
			this.mobile = mobile;
			this.setFacilitatorType(0);
			this.setLawful(true);
			if (mobile.matches(MobileFormat.regMobile3GStr))
			{
				this.setIs3G(true);
			}
		}
		/**
		 * �ڶ����ж��й���ͨ
		 */
		else if (mobile.matches(MobileFormat.regUnicomStr))
		{
			this.mobile = mobile;
			this.setFacilitatorType(1);
			this.setLawful(true);
			if (mobile.matches(MobileFormat.regUnicom3GStr))
			{
				this.setIs3G(true);
			}
		}
		/**
		 * �������ж��й�����
		 */
		else if (mobile.matches(MobileFormat.regTelecomStr))
		{
			this.mobile = mobile;
			this.setFacilitatorType(2);
			this.setLawful(true);
			if (mobile.matches(MobileFormat.regTelocom3GStr))
			{
				this.setIs3G(true);
			}
		}
		/**
		 * ���Ĳ��ж�����
		 */
		if (mobile.matches(MobileFormat.regPhoneString))
		{
			this.mobile = mobile;
			this.setFacilitatorType(0);
			this.setLawful(true);
			if (mobile.matches(MobileFormat.regMobile3GStr))
			{
				this.setIs3G(true);
			}
		}
	}

	public String getMobile()
	{
		return mobile;
	}

	public int getFacilitatorType()
	{
		return facilitatorType;
	}

	public boolean isLawful()
	{
		return isLawful;
	}

	public boolean isIs3G()
	{
		return is3G;
	}

	private void setFacilitatorType(int facilitatorType)
	{
		this.facilitatorType = facilitatorType;
	}

	private void setLawful(boolean isLawful)
	{
		this.isLawful = isLawful;
	}

	private void setIs3G(boolean is3G)
	{
		this.is3G = is3G;
	}
}