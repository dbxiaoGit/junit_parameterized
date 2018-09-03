package cn.x;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;  
import org.junit.runner.RunWith;  
import org.junit.runners.Parameterized;  
import org.junit.runners.Parameterized.Parameters;  
import static org.junit.Assert.*;

/**
 *    a参数化测试的类必须有Parameterized测试运行器修饰
 *
 */
@RunWith(Parameterized.class)  
public class JunitParameterizedTest {
	private ArrayList headerData ;
	private ArrayList testLineData ;	

	public JunitParameterizedTest(ArrayList headerData,ArrayList testLineData) {
		this.headerData = headerData ;
		this.testLineData = testLineData ;
	}
/*	private String headerData ;
	private String testLineData ;	

	public JunitParameterizedTest(String headerData,String testLineData) {
		this.headerData = headerData ;
		this.testLineData = testLineData ;
	}*/
	
    /**
     * 0.准备数据。数据的准备需要在一个方法中进行，该方法需要满足一定的要求：
     * 1.该方法必须由Parameters注解修饰 
     * 2.该方法必须为public static的 
     * 3.该方法必须返回Collection类型 
     * 4.该方法的名字不做要求 
     * 5.该方法没有参数 
     * 
     * @return
     */
 
    @Parameters  
    @SuppressWarnings("unchecked")  
    public static Collection prepareData(){  
    	ArrayList finalTestDataList = new ArrayList<>();
/*        Object [][] object = {{Arrays.asList(1,1),Arrays.asList(1,2)},{Arrays.asList(2,1),Arrays.asList(2,2)}};  
        return Arrays.asList(object);  */
    	String excelPath = "testData.xlsx";
    	try {
            //String encoding = "GBK";
            File excel = new File(excelPath);
            if (excel.isFile() && excel.exists()) {   //判断文件是否存在

                String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
                Workbook wb = null;
                //根据文件后缀（xls/xlsx）进行判断
                if ( "xls".equals(split[1])){
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new HSSFWorkbook(fis);
                }else if ("xlsx".equals(split[1])){
                    wb = new XSSFWorkbook(excel);
                }else {
                    System.out.println("文件类型错误!");
                    
                }

                //开始解析
                Sheet sheet = wb.getSheetAt(1);     //读取sheet 

                int firstRowIndex = sheet.getFirstRowNum();   
                int lastRowIndex = sheet.getLastRowNum();
                System.out.println("firstRowIndex: "+firstRowIndex);
                System.out.println("lastRowIndex: "+lastRowIndex);

//                for(int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
//                    System.out.println("rIndex: " + rIndex);
//                    Row row = sheet.getRow(rIndex);
//                    if (row != null) {
//                        int firstCellIndex = row.getFirstCellNum();
//                        int lastCellIndex = row.getLastCellNum();
//                        for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {   //遍历列
//                            Cell cell = row.getCell(cIndex);
//                            if (cell != null) {
//                                System.out.println(cell.toString());
//                            }
//                        }
//                    }
//                }
                Row firstRow = sheet.getRow(firstRowIndex);
                //获得第一行的数据保存到headList中供后面使用
                List<String> headList = new ArrayList<String>();
                int firstRowColumns = firstRow.getLastCellNum();
                System.out.println("firstRowColumns:"+firstRowColumns);
                for (int j = 0; j < firstRowColumns; j++) {
                	System.out.println("firstRow j:"+j);
                    Cell cell = firstRow.getCell(j);
                    String headCellValue = "";
                    if (cell!=null ) {
                        try {
                            headCellValue = cell.getStringCellValue();
                        } catch (IllegalStateException e) {
                            headCellValue = cell.getNumericCellValue() + "";
                        }
                    }
                	System.out.println(headCellValue);
                    headList.add(headCellValue);
                }
                
                //遍历下面的每一行，再和headList结合起来组成测试数据                       
                for(int i = firstRowIndex +1 ;i < lastRowIndex;i++) {
                	 List everyTestList = new ArrayList<>();
                	 List everyTestDataList = new ArrayList<String>();
                	Row testDataRow = sheet.getRow(i);
                    int testDataRowColumns = testDataRow.getLastCellNum();
                    System.out.println("testDataRowColumns:"+testDataRowColumns);
	                for (int j = 0; j < testDataRowColumns; j++) {
	                	System.out.println(i+"row,"+" j:"+j);
	                    Cell cell = testDataRow.getCell(j);
	                    String testDataCellValue = "";
	                    if (cell!=null) {
	                        try {
	                        	testDataCellValue = cell.getStringCellValue();
	                        } catch (IllegalStateException e) {
	                        	testDataCellValue = cell.getNumericCellValue() + "";
	                        }
	                    }
	                	System.out.println(testDataCellValue);
	                    everyTestDataList.add(testDataCellValue);
	                }
	                everyTestList.add(headList);
	                everyTestList.add(everyTestDataList);
	                System.out.println("headList"+headList.toString());
	                System.out.println("everyTestDataList"+everyTestDataList.toString());
	                System.out.println("everyTestList"+everyTestList.toString());
	                finalTestDataList.add(everyTestList.toArray());
	                System.out.println("finalTestDataList"+finalTestDataList.toString());
                }    
                
                
 /*               Row firstRow = sheet.getRow(firstRowIndex);
                //获得第一行的数据保存到headList中供后面使用
                StringBuilder headString = new StringBuilder();
                int firstRowColumns = firstRow.getLastCellNum();
                System.out.println("firstRowColumns:"+firstRowColumns);
                for (int j = 0; j < firstRowColumns; j++) {
                	System.out.println("firstRow j:"+j);
                    Cell cell = firstRow.getCell(j);
                    String headCellValue = "";
                    if (cell!=null ) {
                        try {
                            headCellValue = cell.getStringCellValue();
                        } catch (IllegalStateException e) {
                            headCellValue = cell.getNumericCellValue() + "";
                        }
                    }
                	System.out.println(headCellValue);
                	headString.append(headCellValue);
                	if(j<firstRowColumns-1) {
                		headString.append("|");
                	}
                }
            	System.out.println("headString:"+headString.toString());

                //遍历下面的每一行，再和headList结合起来组成测试数据                       
                for(int i = firstRowIndex +1 ;i < lastRowIndex;i++) {
                	 List everyTestList = new ArrayList<>();
                	 StringBuilder everyTestDataString = new StringBuilder();
                	Row testDataRow = sheet.getRow(i);
                    int testDataRowColumns = testDataRow.getLastCellNum();
                    System.out.println("testDataRowColumns:"+testDataRowColumns);
	                for (int j = 0; j < testDataRowColumns; j++) {
	                	System.out.println(i+"row,"+" j:"+j);
	                    Cell cell = testDataRow.getCell(j);
	                    String testDataCellValue = "";
	                    if (cell!=null) {
	                        try {
	                        	testDataCellValue = cell.getStringCellValue();
	                        } catch (IllegalStateException e) {
	                        	testDataCellValue = cell.getNumericCellValue() + "";
	                        }
	                    }
	                	System.out.println(testDataCellValue);
	                	everyTestDataString.append(testDataCellValue);
	                	if(j<testDataRowColumns-1) {
	                		everyTestDataString.append("|");
	                	}
	                }
	            	System.out.println("everyTestDataString:"+everyTestDataString.toString());
	                everyTestList.add(headString.toString());
	                everyTestList.add(everyTestDataString.toString());
	                finalTestDataList.add(everyTestList.toArray());
	                System.out.println("finalTestDataList"+finalTestDataList.toString());
                }*/
                
            } else {
                System.out.println("找不到指定的文件");
             
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
/*    	//return finalTestDataList;
    	ArrayList list = new ArrayList<>();
    	ArrayList list1 = new ArrayList<>();
    	ArrayList list2 = new ArrayList<>();
    	list2.add(2);
    	list1.add(list1);
    	list1.add(list1);
    	list.add(list1);
    	list.add(list1);
    	return list;*/
        Object [][] object = new Object[finalTestDataList.size()][] ;
        for(int k=0; k<finalTestDataList.size(); k++){
        	object[k] = (Object[]) finalTestDataList.get(k);
        }
        return Arrays.asList(object); 
    } 
    
	@Test
	public void test() {
		System.out.println("headerData:"+headerData.toString());
		System.out.println("testLineData:"+testLineData.toString());
		System.out.println("=========================");
		
		/*	list转int数组
		Integer[] targetVector = new Integer[testData.size()];
		targetVector = (Integer[])testData.toArray(targetVector);
		assertEquals(expectedEvaluate+1,actualEvaluate);
		*/
	}
	
   
      
}
