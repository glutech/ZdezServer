package cn.com.zdez.util;

public class DateConvert {

	/**
	 * 把当前时间转为当日0时的时间，用于日用户量的统计
	 * 
	 * @param date
	 * @return
	 */
	public String DayDateConvert(String date) {
		String dayDate = "";
		String year = date.substring(0, 4);
		String month = date.substring(5, 7);
		String day = date.substring(8, 10);
		String hour = date.substring(11, 13);
		String minute = date.substring(14, 16);
		String second = date.substring(17, 19);

		// day date
		hour = minute = second = "00";
		dayDate = year + "-" + month + "-" + day + " " + hour + ":" + minute
				+ ":" + second;

		return dayDate;
	}

	/**
	 * 根据当前时间，计算出本周的起始时间，用于周用户统计
	 * 
	 * @param date
	 * @return
	 */
	public String WeekDateConvert(String date) {
		String weekDate = "";
		String year = date.substring(0, 4);
		String month = date.substring(5, 7);
		String day = date.substring(8, 10);
		String hour = date.substring(11, 13);
		String minute = date.substring(14, 16);
		String second = date.substring(17, 19);

		int yearInt = Integer.parseInt(year);
		int monthInt = Integer.parseInt(month);
		int dayInt = Integer.parseInt(day);

		// week date
		dayInt = dayInt - 7;
		if (dayInt <= 0) {
			monthInt = monthInt - 1;
			if (monthInt == 1 || monthInt == 3 || monthInt == 5
					|| monthInt == 7 || monthInt == 8 || monthInt == 10) {
				dayInt = 31 + dayInt;
			} else if (monthInt == 4 || monthInt == 6 || monthInt == 9
					|| monthInt == 11) {
				dayInt = 30 + dayInt;
			} else if (monthInt == 0) {
				yearInt = yearInt - 1;
				monthInt = 12;
				dayInt = 31 + dayInt;
			} else {
				if (yearInt % 4 == 0) {
					dayInt = 29 + dayInt;
				} else {
					dayInt = 28 + dayInt;
				}
			}

		}

		if (monthInt < 10) {
			month = "0" + monthInt;
		} else {
			month = Integer.toString(monthInt);
		}
		if (dayInt < 10) {
			day = "0" + dayInt;
		} else {
			day = Integer.toString(dayInt);
		}

		weekDate = yearInt + "-" + month + "-" + day + " " + hour + ":"
				+ minute + ":" + second;

		return weekDate;
	}

	/**
	 * 根据当前时间，计算本月起始时间，用于月用户数统计
	 * 
	 * @param date
	 * @return
	 */
	public String MonthDateConvert(String date) {
		String monthDate = "";
		String year = date.substring(0, 4);
		String month = date.substring(5, 7);
		String day = date.substring(8, 10);
		String hour = date.substring(11, 13);
		String minute = date.substring(14, 16);
		String second = date.substring(17, 19);

		int yearInt = Integer.parseInt(year);
		int monthInt = Integer.parseInt(month);
		int dayInt = Integer.parseInt(day);

		// month date
		// 如果当前月份是1月
		monthInt = monthInt - 1;
		if (monthInt == 0) {
			yearInt = yearInt - 1;
			monthInt = 12;
		} else {
			// 如果当前月份是5、7、10、12
			if (monthInt == 4 || monthInt == 6 || monthInt == 9
					|| monthInt == 11) {
				if (dayInt == 31) {
					dayInt = 30;
				}
			} else if (monthInt == 2) { // 如果当前月份是3月
				if (dayInt > 29) {
					if (yearInt % 4 == 0) {
						dayInt = 29;
					} else {
						dayInt = 28;
					}
				}
			}
		}

		if (monthInt < 10) {
			month = "0" + monthInt;
		} else {
			month = Integer.toString(monthInt);
		}
		if (dayInt < 10) {
			day = "0" + dayInt;
		} else {
			day = Integer.toString(dayInt);
		}

		monthDate = yearInt + "-" + month + "-" + day + " " + hour + ":"
				+ minute + ":" + second;

		return monthDate;
	}

}
