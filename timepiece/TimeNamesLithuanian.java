package timepiece;

import java.util.LinkedList;
import java.util.List;

public class TimeNamesLithuanian {
	
	public static String[] getHoursNomS() {//nominative singular
		String[] hours = { 
				"pirma",
				"antra",
				"trečia",
				"ketvirta",
				"penkta",
				"šešta",
				"septinta",
				"aštunta",
				"devinta",
				"dešimta",
				"vienuolikta",
				"dvylikta"
		};
		return hours;
	}
	
	public static String[] getHoursNomP() {//nominative plural
		String[] hours = { 
				"pirma",
				"dvi",
				"trys",
				"keturios",
				"penkios",
				"šešios",
				"septynios",
				"aštuonios",
				"devynios",
				"dešimt",
				"vienuolika",
				"dvylika"
		};
		return hours;
	}
	
	public static String[] getHoursGenS() {//genitive singular
		String[] hours = { 
				"pirmos",
				"antros",
				"trečios",
				"ketvirtos",
				"penktos",
				"šeštos",
				"septintos",
				"aštuntos",
				"devintos",
				"dešimtos",
				"vienuoliktos",
				"dvyliktos"
		};
		return hours;
	}	
	
	public static String[] getHoursGenP() {//genitive plural
		String[] hours = { 
				"NULL",
				"dviejų",
				"trijų",
				"keturių",
				"penkių",
				"šešių",
				"septynių",
				"aštuonių",
				"devynių",
				"dešimt",
				"vienuolikos",
				"dvylikos"
		};
		return hours;
	}	
	
	public static String[] getMinutesN() { //nominative
		String[] minutes = {
				"NULL", //not used - "exactly [hour]"
				"penkios",
		 		"dešimt",
		 		"penkiolika",
		 		"dvidešimt",
		 		"dvidešimt penkios",
		 		"trisdešimt",
		 		"trisdešimt penkios",
		 		"keturiasdešimt",
		 		"keturiasdešimt penkios",
		 		"penkiadešimt",
		 		"penkiasdešimt penkios"
		};
		return minutes;
	}
	
	public static String[] getMinutesG() { //genitive
		String[] minutes = {
				"NULL", //not used - "exactly [hour]"
				"penkių",
		 		"dešimt",
		 		"penkiolikos",
		 		"dvidešimt",
		 		"dvidešimt penkių",
		 		"trisdešimt",
		 		"trisdešimt penkių",
		 		"keturiasdešimt",
		 		"keturiasdešimt penkių",
		 		"penkiadešimt",
		 		"penkiasdešimt penkių"
		};
		return minutes;
	}
	
	
	public static List<String>[][] getTimeStrings() {
		List<String>[][] times = new List[12][];
		for (int i = 0; i < times.length; i++) {
			times[i] = new List[12];
			for (int j = 0; j < times[i].length; j++) {
				times[i][j] = new LinkedList<String>();
			}
		}
		
		String[] hoursnoms = getHoursNomS();
		String[] hoursnomp = getHoursNomP();
		String[] hoursgens = getHoursGenS();
		String[] hoursgenp = getHoursGenP();
		String[] minutesn = getMinutesN();
		String[] minutesg = getMinutesG();
		
		//simple format    X uhr Y, Y = n * 5
		String simpleFormat = "%s %s";
		for (int hour = 0; hour < hoursnoms.length; hour++) {
			for (int minute = 0; minute < minutesn.length; minute++) {
				String hns = hoursnoms[hour];
				String hnp = hoursnomp[hour];
				String hgs = hoursgens[hour];
				String hgp = hoursgenp[hour];
				String mn =  minutesn[minute];
				String mg =  minutesg[minute];
				//"lygiai [valanda]" (exactly [hour])
				if(minute == 0) {
					times[hour][minute].add(String.format("lygiai %s", hns).trim());
					if (hour > 0) times[hour][minute].add(String.format("lygiai %s", hnp).trim());
				}
				
				if(minute != 0) {
					times[hour][minute].add(String.format("po %s %s", hgs, mn).trim()); //pridėk "dvylika dvidešimt penkios"?
					if (hour > 0) times[hour][minute].add(String.format("po %s %s", hgp, mn).trim());
				}
				
				//if(minute == 1) m = "o five";
				//times[hour][minute].add(String.format(simpleFormat, h, m).trim());
				
			}
			int nexthour = 0;
			if (hour != hoursnoms.length - 1) nexthour = hour + 1;

			//h:20
			times[hour][4].add(String.format("be dešimt pusė %s", hoursgens[nexthour]).trim());
			//h:25
			times[hour][5].add(String.format("be penkių pusė %s", hoursgens[nexthour]).trim());
			//half hour
			times[hour][6].add(String.format("pusė %s", hoursgens[nexthour]).trim());
			if (nexthour > 0) times[hour][6].add(String.format("pusė %s", hoursgenp[nexthour]).trim());
			//h:35
			times[hour][7].add(String.format("penkios po pusės %s", hoursgens[nexthour]).trim());
			if (nexthour > 0) times[hour][7].add(String.format("penkios po pusės %s", hoursgenp[nexthour]).trim());
			//h:50
			times[hour][8].add(String.format("be dešimt %s", hoursnomp[nexthour]).trim());
			//h:55
			times[hour][9].add(String.format("be penkių %s", hoursnomp[nexthour]).trim());

			//this should be rewritten above, maybe. quarters are not used.		
			//		
			//times[hour][9].add(String.format("quarter to %s",nextHour).trim());
		}
		
		/*
		 * for (int hour = 0; hour < hoursnoms.length; hour++) { int nexthour = 0; if
		 * (hour != hoursnoms.length - 1) nexthour = hour + 1;
		 * 
		 * //h:20 times[hour][4].add(String.format("be dešimt pusė %s",
		 * hoursgens[nexthour]).trim()); //h:25
		 * times[hour][5].add(String.format("be penkių pusė %s",
		 * hoursgens[nexthour]).trim()); //half hour
		 * times[hour][6].add(String.format("pusė %s", hoursgens[nexthour]).trim()); if
		 * (hour > 0) times[hour][6].add(String.format("pusė %s",
		 * hoursgenp[nexthour]).trim()); //h:35
		 * times[hour][7].add(String.format("penkios po pusės %s",
		 * hoursgens[nexthour]).trim()); //h:50
		 * times[hour][8].add(String.format("be dešimt %s",
		 * hoursgens[nexthour]).trim()); //h:55
		 * times[hour][9].add(String.format("be penkių %s",
		 * hoursgens[nexthour]).trim());
		 * 
		 * //this should be rewritten above, maybe. quarters are not used. //
		 * //times[hour][9].add(String.format("quarter to %s",nextHour).trim()); }
		 */
		
		return times;
	}
	
	public static void main(String[] args) {
		List<String>[][] ts = getTimeStrings();
		for (List<String>[] lists : ts) {
			for (List<String> list : lists) {
				for (String string : list) {
					System.out.println(string);
				}
			}
		}
	}
}
