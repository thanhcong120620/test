package doVE;

import java.util.ArrayList;
import java.util.List;

public class test {

	public static void main(String[] args) {
		
			test3();

		
		
	}
	
	public static void test3() {
		LotteryBoardEntity entity = new LotteryBoardEntity();
		entity.setDate("15-1-2023");
		entity.setG0("287473");
		entity.setG1("75354");
		entity.setG2("56873");
		entity.setG31("54754");
		entity.setG32("01252");
		entity.setG41("57625");
		entity.setG42("96725");
		entity.setG43("23573");
		entity.setG44("87473");
		entity.setG45("38323");
		entity.setG46("87473");
		entity.setG47("84523");
		entity.setG5("5348");
		entity.setG61("8683");
		entity.setG62("2748");
		entity.setG63("1723");
		entity.setG7("283");
		entity.setG8("83");
		LotteryBoardEntity entity2 = new LotteryBoardEntity();
		entity2.setDate("11-1-2023");
		entity2.setG0("287642");
		entity2.setG1("89423");
		entity2.setG2("12348");
		entity2.setG31("67283");
		entity2.setG32("72523");
		entity2.setG41("10348");
		entity2.setG42("19683");
		entity2.setG43("58348");
		entity2.setG44("81823");
		entity2.setG45("20783");
		entity2.setG46("83148");
		entity2.setG47("19048");
		entity2.setG5("8323");
		entity2.setG61("9548");
		entity2.setG62("9825");
		entity2.setG63("5034");
		entity2.setG7("833");
		entity2.setG8("13");
		
		List<LotteryBoardEntity> lbList = new ArrayList<>();
		lbList.add(entity2);
		lbList.add(entity);

		/*------------------------Get frequency lottery by date-----------------------*/
		SetUpDate setUpDate = new SetUpDate();
		String currentDay = setUpDate.setCurrentDay();
		String day30Ago = setUpDate.set30DayAgo();
//		String currentDay = "15-1-2023";
//		String day30Ago = "15-12-2022";
//		System.out.println("currentDay: "+currentDay);
//		System.out.println("day30Ago: "+day30Ago);
		Static st = new Static();
		List<Num> numList = new ArrayList<>();
		numList = st.numFrequency(lbList,day30Ago,currentDay);
		
		//sort:
		System.out.println("sort by num: ");
		numList = st.sortByNum(numList);
		for(Num num : numList) {
			System.out.println(num.toString());
		}
		
//		System.out.println("sort by frequent: ");
//		numList = st.sortByFrequency(numList);
//		for(Num num : numList) {
//			System.out.println(num.toString());
//		}
		
		/*------------------------------------------------------------------------------*/

		
		
		
		
		
		
		
	}
	
	public static void test2() {
		Static st = new Static();
		List<Num> numList = new ArrayList<>();
		int[] arr = {21,12,12,22,77,21,77};

		for(int i=0; i<arr.length;i++) {
			st.setFrequency(numList, ""+arr[i]);
		}
		for(Num num : numList) {
			System.out.println(num.toString());
		}
	}
	
	public static void test1() {
		LotteryBoardEntity entity = new LotteryBoardEntity();
		entity.setG0("287473");
		entity.setG1("75341");
		entity.setG2("56831");
		entity.setG31("54754");
		entity.setG32("01252");
		entity.setG41("57658");
		entity.setG42("96725");
		entity.setG43("23586");
		entity.setG44("87435");
		entity.setG45("38323");
		entity.setG46("87473");
		entity.setG47("84523");
		entity.setG5("5348");
		entity.setG61("8653");
		entity.setG62("2735");
		entity.setG63("1724");
		entity.setG7("283");
		entity.setG8("38");
		
		drawLottery dL = new drawLottery();
		
		System.out.println(dL.drawWithLottery("381283", entity));
	}
	
}
