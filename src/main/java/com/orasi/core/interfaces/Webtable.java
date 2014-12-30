package com.orasi.core.interfaces;

import javax.naming.directory.NoSuchAttributeException;

import com.orasi.core.interfaces.impl.WebtableImpl;
import com.orasi.core.interfaces.impl.internal.ImplementedBy;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Interface that wraps a WebElement in CheckBox functionality.
 */
@ImplementedBy(WebtableImpl.class)
public interface Webtable extends Element {

    /**
     * @summary - Get the row count of the Webtable
     */
    int getRowCount(WebDriver driver);

    /**
     * @summary - Get the column count for the Webtable on a specified Row
     */
    int getColumnCount(WebDriver driver, int row) throws NoSuchAttributeException;

    /**
     * @summary - Get cell data of the specified row and Column in a Webtable
     */
    String getCellData( WebDriver driver, int row, int column) throws NoSuchAttributeException;
    

    /**
     * @summary - Return the Cell of the specified row and Column in a Webtable
     */
    WebElement getCell( WebDriver driver, int row, int column) throws NoSuchAttributeException;
    

    /**
     * @summary - Click cell in the specified row and Column in a Webtable
     */
    void clickCell( WebDriver driver, int row, int column) throws NoSuchAttributeException;
    
    
    /**
     * @summary - Get Row number where text is found
     */
    int getRowWithCellText(WebDriver driver, String text);

    /**
     * @summary - Get Row number where text is found in a specific column
     */    
    int getRowWithCellText( WebDriver driver, String text, int columnPosition);

    /**
     * @summary - Get Row number where text is found in a specific column and starting row
     */    
    int getRowWithCellText( WebDriver driver, String text, int columnPosition, int startRow);
    
    /**
     * @summary - Get Column number where text is found
     */  
    int getColumnWithCellText(WebDriver driver, String text);
    
    /**
     * @summary - Get Column number where text is found in a specific row
     */  
    int getColumnWithCellText(WebDriver driver, String text, int rowPosition);
    
    /**
     * @summary - Get Row number where text is found within a specific column - using 'contains'
     */    
    int getRowThatContainsCellText( WebDriver driver, String text, int columnPosition);
}