// with a hat-tip to http://www.bayrisch-lernen.de/popups/zahlen.html

package timepiece;

import java.util.LinkedList;
import java.util.List;

public class TimeNamesBavarian {
	
	public static String[] getHours() {
		String[] hours = {
				"oans",
				"zwoa",
				"drei",
				"fiar",
				"fümf",
				"sechs",
				"simnn",
				"achd",
				"nain",
				"zehn",
				"älf",
				"zweif"
		};
		return hours;
	}
	
	public static String[] getMinutes() {
		String[] minutes = {
				"null",
				"fümfe",
		 		"zehne",
		 		"fuchzehn",
		 		"zwanzge",
		 		"fümfa+zwanzge",
		 		"draissge",
		 		"fümfa+draissge",
		 		"fiarzge",
		 		"fümfa+fiarzge",
		 		"fuchzge",
		 		"fümfa+fuchzge"
		};
		return minutes;
	}
	

	@SuppressWarnings("unchecked")
	public static List<String>[][] getTimeStrings() {
		List<String>[][] times = new List[12][];
		for (int i = 0; i < times.length; i++) {
			times[i] = new List[12];
			for (int j = 0; j < times[i].length; j++) {
				times[i][j] = new LinkedList<String>();
			}
		}
		
		String[] hours = getHours();
		String[] minutes = getMinutes();
		
		//simple format    X uhr Y, Y = n * 5
		String simpleFormat = "%s uhr %s";
		for (int hour = 0; hour < hours.length; hour++) {
			for (int minute = 0; minute < minutes.length; minute++) {
				String h = hours[hour];
				String m =  minutes[minute];
				if(hour == 0) h = "oan";
				if(minute == 0) m = "";
				times[hour][minute].add(String.format(simpleFormat, h, m).trim());
			}
		}
		
		/*
		 * punkt X
		 * Y nach X, Y = fünf, zehn, zwanzig
		 * viertel nach X
		 * Y vor halb X+1 , Y = zehn, fünf
		 * halb X+1
		 * Y nach halb X+1, Y = zehn, fünf
		 * dreiviertel X+1
		 * Y vor X+1, Y = zwanzig, zehn, fünf
		 */
		
		String[] fzz = {
				minutes[1],
				minutes[2],
				null,
				minutes[4]
		};
		
		String[] fz = {
				minutes[1],
				minutes[2]
		};
		
		for (int hour = 0; hour < hours.length; hour++) {
			String nextHour = hour == hours.length - 1?hours[0]:hours[hour+1];
			
			times[hour][0].add(String.format("punkt %s", hours[hour]).trim());
			times[hour][3].add(String.format("fiadl noch %s", hours[hour]).trim());
			times[hour][6].add(String.format("hoiba %s", nextHour).trim());
			times[hour][9].add(String.format("dreifiadl %s", nextHour).trim());
			
			for (int i = 0; i < fzz.length; i++) {
				if(fzz[i] != null ) {
					times[hour][i + 1].add(String.format("%s noch %s", fzz[i], hours[hour]).trim());
					times[hour][11 -i ].add(String.format("%s vor %s", fzz[i], nextHour).trim());
				}
			}
			
			for (int i = 0; i < fz.length; i++) {
				times[hour][5 - i].add(String.format("%s vor hoiba %s", fz[i], nextHour).trim());
				times[hour][7 +i ].add(String.format("%s noch hoiba %s", fz[i], nextHour).trim());
			}
		}
		
		
		return times;
	}
}
