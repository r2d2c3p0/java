package com.sat.util;

/*
 * 
 * Author r2d2c3p0
 * Generate the numbers and get the drawing days and times for major lottery games.
 * US based only.
 * 
 * MegaMillions.
 * Texas Lotto.
 * Powerball.
 * All or Nothing (Texas).
 * 11/11/2017.
 * 
 * Bon chance.
 * The limits change from time to time. Visit the web sites and update accordingly.
 *  
*/

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GenerateLotteryTicketNumbers {

	/*
	 * Web site: https://www.txlottery.org/export/sites/lottery/Games/Lotto_Texas/index.html
	 * Number frequency https://www.txlottery.org/export/sites/lottery/Games/Lotto_Texas/Number_Frequency.html
	*/
	static int LT_NUMBER_LIMIT=54;
	
	/*
	 * Web site: http://www.megamillions.com
	 * Check the number limits here http://www.megamillions.com/how-to-play
	 * RNG http://www.megamillions.com/random-number-generator
	*/
		
	static int MEGABALL_LIMIT=25;
	static int MM_NUMBER_LIMIT=70;
	
	/*
	 * Web site: http://www.powerball.com/pb_home.asp
	 * Check the number limits here http://www.powerball.com/powerball/pb_howtoplay.asp 
	*/
	static int POWERBALL_LIMIT=26;
	static int PB_NUMBER_LIMIT=69;
	
	/*
	 * 
	 * Web site: https://www.txlottery.org/export/sites/lottery/Games/All_or_Nothing/index.html
	 * 
	*/
	static int AON_NUMBER_LIMIT=24;
	
	// Main program.
	public static void main(String[] args) {
		
		@SuppressWarnings("resource")
		Scanner GameName = new Scanner(System.in);
		System.out.println("\n\t\t===========================================================");
		System.out.println("\t\t Generate Lottery Ticket Numbers, Select game below:");
		System.out.println("\t\t===========================================================");
		System.out.println("\t\t\t 1. Mega Millions.\n\t\t\t 2. Texas Lotto.\n\t\t\t 3. "
				+ "Powerball.\n\t\t\t 4. All or Nothing.");
		System.out.println("\t\t===========================================================\n\n");
		
		String Selection="1";System.out.print("\tMake your selection [1/2/3/4]: ");
		Selection = GameName.next();

		if (Selection.equals("1")) {
			MegaMillions();
		} else if (Selection.equals("2")) {
			TexasLotto();
		} else if (Selection.equals("3")) {
			Powerball();
		} else if (Selection.equals("4")) {
			AllOrNothing();
		} else {
			System.out.println("\n Game not found.\n");
			System.exit(1);
		}
        
	}

	public static void MegaMillions() {
		
		List<Integer> mm_winningnumber_arrayL = new ArrayList<Integer>();
		int mm_rollnumber;
		Random mm_winningnumber = new Random();
		
		for (int roll=0; roll<5; roll++) {			
			boolean noduplicate_number = true;			
			while (noduplicate_number) {
				mm_rollnumber = mm_winningnumber.nextInt(MM_NUMBER_LIMIT)+1;
				if (mm_winningnumber_arrayL.contains(mm_rollnumber)) {
					@SuppressWarnings("unused")
					int y=0;
				} else {
					mm_winningnumber_arrayL.add(mm_rollnumber);
					noduplicate_number = false;
				}
			}
		}
		
		int mm_rollnumber1 = mm_winningnumber_arrayL.get(0);
		int mm_rollnumber2 = mm_winningnumber_arrayL.get(1);
		int mm_rollnumber3 = mm_winningnumber_arrayL.get(2);
		int mm_rollnumber4 = mm_winningnumber_arrayL.get(3);
		int mm_rollnumber5 = mm_winningnumber_arrayL.get(4);
		int megaball = mm_winningnumber.nextInt(MEGABALL_LIMIT)+1;
		
		log(0,"");
		log(1,"MegaMillions [Every Tuesday and Friday at 10:12 PM CST]");
		log(1,"MegaMillions [MM_NUMBER_LIMIT="+MM_NUMBER_LIMIT+" & MEGABALL_LIMIT="+MEGABALL_LIMIT+"]");
		log(1,"MegaMillions ["+mm_rollnumber1+" - "+mm_rollnumber2+" - "
				+ ""+mm_rollnumber3+" - "+mm_rollnumber4+" - "
				+mm_rollnumber5+" - "+megaball+"(m)]");
		log(0,"");
		
	}
	
	public static void TexasLotto() {
		
		List<Integer> lt_winningnumber_arrayL = new ArrayList<Integer>();
		int lt_rollnumber;
		Random lt_winningnumber = new Random();
		
		for (int roll=0; roll<6; roll++) {			
			boolean noduplicate_number = true;
			while (noduplicate_number) {
				lt_rollnumber = lt_winningnumber.nextInt(LT_NUMBER_LIMIT)+1;
				if (lt_winningnumber_arrayL.contains(lt_rollnumber)) {
					@SuppressWarnings("unused")
					int y=0;
				} else {
					lt_winningnumber_arrayL.add(lt_rollnumber);
					noduplicate_number = false;
				}
			}
		}
		
		int lt_rollnumber1 = lt_winningnumber_arrayL.get(0);
		int lt_rollnumber2 = lt_winningnumber_arrayL.get(1);
		int lt_rollnumber3 = lt_winningnumber_arrayL.get(2);
		int lt_rollnumber4 = lt_winningnumber_arrayL.get(3);
		int lt_rollnumber5 = lt_winningnumber_arrayL.get(4);
		int lt_rollnumber6 = lt_winningnumber_arrayL.get(5);
		
		log(0,"");
		log(2,"Lotto [Every Wednesday and Saturday at 10:12 PM CST]");
		log(2,"Lotto [LT_NUMBER_LIMIT="+LT_NUMBER_LIMIT+"]");
		log(2,"Lotto ["+lt_rollnumber1+" - "+lt_rollnumber2+" - "
				+ ""+lt_rollnumber3+" - "+lt_rollnumber4+" - "
				+lt_rollnumber5+" - "+lt_rollnumber6+"]");
		log(0,"");
		
	}
	
	public static void Powerball() {
		
		List<Integer> pb_winningnumber_arrayL = new ArrayList<Integer>();
		int pb_rollnumber;
		Random pb_winningnumber = new Random();
		
		for (int roll=0; roll<5; roll++) {			
			boolean noduplicate_number = true;			
			while (noduplicate_number) {
				pb_rollnumber = pb_winningnumber.nextInt(PB_NUMBER_LIMIT)+1;
				if (pb_winningnumber_arrayL.contains(pb_rollnumber)) {
					@SuppressWarnings("unused")
					int y=0;
				} else {
					pb_winningnumber_arrayL.add(pb_rollnumber);
					noduplicate_number = false;
				}
			}
		}
		
		int pb_rollnumber1 = pb_winningnumber_arrayL.get(0);
		int pb_rollnumber2 = pb_winningnumber_arrayL.get(1);
		int pb_rollnumber3 = pb_winningnumber_arrayL.get(2);
		int pb_rollnumber4 = pb_winningnumber_arrayL.get(3);
		int pb_rollnumber5 = pb_winningnumber_arrayL.get(4);
		int powerball = pb_winningnumber.nextInt(POWERBALL_LIMIT)+1;
		
		log(0,"");
		log(3,"Powerball [Every Wednesday and Saturday at 10:12 PM CST]");
		log(3,"Powerball [PB_NUMBER_LIMIT="+PB_NUMBER_LIMIT+" & POWERBALL_LIMIT="+POWERBALL_LIMIT+"]");
		log(3,"Powerball ["+pb_rollnumber1+" - "+pb_rollnumber2+" - "
				+ ""+pb_rollnumber3+" - "+pb_rollnumber4+" - "
				+pb_rollnumber5+" - "+powerball+"(p)]");
		log(0,"");
		
	}
	
	public static void AllOrNothing() {
		
		List<Integer> aon_winningnumber_arrayL = new ArrayList<Integer>();
		int aon_rollnumber;
		Random aon_winningnumber = new Random();
		
		for (int roll=0; roll<12; roll++) {			
			boolean noduplicate_number = true;
			while (noduplicate_number) {
				aon_rollnumber = aon_winningnumber.nextInt(AON_NUMBER_LIMIT)+1;
				if (aon_winningnumber_arrayL.contains(aon_rollnumber)) {
					@SuppressWarnings("unused")
					int y=0;
				} else {
					aon_winningnumber_arrayL.add(aon_rollnumber);
					noduplicate_number = false;
				}
			}
		}
		
		int aon_rollnumber1 = aon_winningnumber_arrayL.get(0);
		int aon_rollnumber2 = aon_winningnumber_arrayL.get(1);
		int aon_rollnumber3 = aon_winningnumber_arrayL.get(2);
		int aon_rollnumber4 = aon_winningnumber_arrayL.get(3);
		int aon_rollnumber5 = aon_winningnumber_arrayL.get(4);
		int aon_rollnumber6 = aon_winningnumber_arrayL.get(5);
		int aon_rollnumber7 = aon_winningnumber_arrayL.get(6);
		int aon_rollnumber8 = aon_winningnumber_arrayL.get(7);
		int aon_rollnumber9 = aon_winningnumber_arrayL.get(8);
		int aon_rollnumber10 = aon_winningnumber_arrayL.get(9);
		int aon_rollnumber11 = aon_winningnumber_arrayL.get(10);
		int aon_rollnumber12 = aon_winningnumber_arrayL.get(11);
		
		log(0,"");
		log(4,"All or Nothing [Every weekday and Saturday at 10:00 AM, 12:27 PM, 6:00 PM, and 10:12 PM CST]");
		log(4,"All or Nothing [AON_NUMBER_LIMIT="+AON_NUMBER_LIMIT+"]");
		log(4,"All or Nothing ["+aon_rollnumber1+" - "+aon_rollnumber2+" - "
				+ ""+aon_rollnumber3+" - "+aon_rollnumber4+" - "
				+ ""+aon_rollnumber5+" - "+aon_rollnumber6+" - "
				+ ""+aon_rollnumber7+" - "+aon_rollnumber8+" - "
				+ ""+aon_rollnumber9+" - "+aon_rollnumber10+" - "
				+aon_rollnumber11+" - "+aon_rollnumber12+"]");
		log(0,"");
		
	}

	private static void log(int game, String Message) {
		
		SimpleDateFormat dateFormatter = new SimpleDateFormat("E, y-M-d 'at' h:m:s a z");
		Date currentdate = new Date();
		dateFormatter = new SimpleDateFormat("hh:mm:ss a zzz EEEE, MMMM d, yyyy");
		if (game==0) {
			System.out.println();
		} else {
			System.out.println("\t["+dateFormatter.format(currentdate)+"] "+Message);
		}
	    
	}

}