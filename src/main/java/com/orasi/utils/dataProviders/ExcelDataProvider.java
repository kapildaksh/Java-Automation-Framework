package com.orasi.utils.dataProviders;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelDataProvider {
	
	private static XSSFSheet ExcelWSheet;
	private static XSSFWorkbook ExcelWBook;
	private static XSSFCell Cell;
	private static String[] listScenarios;
	
	//Constructor
	public ExcelDataProvider(){
		
	}
	
	/**
	 * This gets the test data from excel workbook by the sheet specified.  It returns all the data 
	 * as a 2d array
	 * 
	 * @param	sheetName	the excel sheet
	 * @version	10/16/2014
	 * @author 	Justin Phlegar
	 * @return 	2d array of test data
	 */
	public static Object[][] getTestScenarioData(String filePath, String SheetName) {

		String[][] tabArray = null;
		
		// Get the file location from the project main/resources folder
		filePath =  ExcelDataProvider.class.getResource(filePath).getPath();

		// in case file path has a %20 for a whitespace, replace with actual
		// whitespace
		filePath = filePath.replace("%20", " ");
		
		try {

			FileInputStream ExcelFile = new FileInputStream(filePath);

			// Access the required test data sheet

			ExcelWBook = new XSSFWorkbook(ExcelFile);

			ExcelWSheet = ExcelWBook.getSheet(SheetName);

			int startRow = 1;

			int startCol = 0;

			int ci, cj;

			int totalRows = ExcelWSheet.getLastRowNum();

			// you can write a function as well to get Column count

			int totalCols = ExcelWSheet.getRow(startRow).getLastCellNum();

			tabArray = new String[totalRows][totalCols];

			ci = 0;

			for (int i = startRow; i <= totalRows; i++, ci++) {
				cj = 0;
				
				for (int j = startCol; j < totalCols; j++, cj++) {
					tabArray[ci][cj] = getCellData(i, j);
					System.out.println(getCellData(0,j) + ": " + tabArray[ci][cj]);
				}
				System.out.println("");
			}
		} catch (FileNotFoundException e) {
			System.out.println("Could not read the Excel sheet");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Could not read the Excel sheet");
			e.printStackTrace();
		}
		return (tabArray);
	}
	

	public static Object[][] getTestScenarioData(String filePath, String SheetName, int rowToRun) {

		String[][] tabArray = null;

		try {

			FileInputStream ExcelFile = new FileInputStream(filePath);

			// Access the required test data sheet

			ExcelWBook = new XSSFWorkbook(ExcelFile);

			ExcelWSheet = ExcelWBook.getSheet(SheetName);

			int startRow = 1;

			int startCol = 0;

			int ci, cj;

			

			// you can write a function as well to get Column count

			int totalCols = ExcelWSheet.getRow(startRow).getLastCellNum();

			tabArray = new String[1][totalCols];

			ci = 0;

			
			cj = 0;
			
			for (int j = startCol; j < totalCols; j++, cj++) {
				tabArray[ci][cj] = getCellData(rowToRun, j);
				System.out.println(tabArray[ci][cj]);
			}
			System.out.println("");
		}
		
		catch (FileNotFoundException e) {
			System.out.println("Could not read the Excel sheet");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Could not read the Excel sheet");
			e.printStackTrace();
		}
		return (tabArray);
	}

	// This method is to read the test data from the Excel cell, in this we are
	// passing parameters as Row num and Col num
	public static String getCellData(int RowNum, int ColNum) {
		try {
			Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
			String CellData = Cell.getStringCellValue();
			return CellData;
		} catch (Exception e) {
			return "";
		}
	}
	
	

}
