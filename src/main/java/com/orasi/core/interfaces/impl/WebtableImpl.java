package com.orasi.core.interfaces.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.orasi.core.interfaces.Webtable;
import com.orasi.utils.WebDriverSetup;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Wrapper class like Select that wraps basic checkbox functionality.
 */
public class WebtableImpl extends ElementImpl implements Webtable {
	//private java.util.Date date= new java.util.Date();
    /**
     * Wraps a WebElement with checkbox functionality.
     *
     * @param element to wrap up
     */
    public WebtableImpl(WebElement element) {
        super(element);
    }
    
    public int getRowCount(WebDriver driver)
	{
    	driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		List<WebElement> rowCollection = this.element.findElements(By.xpath("tr"));

		if (rowCollection.size() == 0) {
			rowCollection = this.element.findElements(By.xpath("tbody/tr"));
		}
    	driver.manage().timeouts().implicitlyWait(WebDriverSetup.getDefaultTestTimeout(), TimeUnit.SECONDS);
		return rowCollection.size();
	}
	
	public int getColumnCount( WebDriver driver, int row) {
    	driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		List<WebElement> rowCollection = this.element.findElements(By.xpath("tr"));

		if (rowCollection.size() == 0) {
			rowCollection = this.element.findElements(By.xpath("tbody/tr"));
		}
    	driver.manage().timeouts().implicitlyWait(WebDriverSetup.getDefaultTestTimeout(), TimeUnit.SECONDS);
		
		int currentRow = 1;
		int columnCount = 0;
		String xpath = null;
		
		for(WebElement rowElement : rowCollection){
			if(row == currentRow){	
	        	driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
	        	
				if(rowElement.findElements(By.xpath("th")).size() != 0){
	        		xpath = "th";
	        	}else if(rowElement.findElements(By.xpath("td")).size() != 0){
	        		xpath = "td";
	        	//need to throw an exception
	        	}
	        	
	        	driver.manage().timeouts().implicitlyWait(WebDriverSetup.getDefaultTestTimeout(), TimeUnit.SECONDS);
	        	
				List<WebElement> columnCollection = rowElement.findElements(By.xpath(xpath));
				columnCount =columnCollection.size();	 
				break;
			}else{
				currentRow++;
			}			
		}
		
		return columnCount;
	}


	public WebElement getCell( WebDriver driver, int row, int column){
    	driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		List<WebElement> rowCollection = this.element.findElements(By.xpath("tr"));

		if (rowCollection.size() == 0) {
			rowCollection = this.element.findElements(By.xpath("tbody/tr"));
		}
    	driver.manage().timeouts().implicitlyWait(WebDriverSetup.getDefaultTestTimeout(), TimeUnit.SECONDS);
		WebElement elementCell = null;

        int currentRow = 1,currentColumn = 1;
        String xpath = null;        
        Boolean found = false;
        
        for(WebElement rowElement : rowCollection)
        {        	
        	if(row != currentRow){currentRow++;}
        	else{
			   	driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
	        	if(rowElement.findElements(By.xpath("th")).size() != 0)
	        	{
	        		xpath = "th";
	        	}else if(rowElement.findElements(By.xpath("td")).size() != 0)
	        	{
	        		xpath = "td";
	        	//need to throw an exception
	        	}
	        	
	        	driver.manage().timeouts().implicitlyWait(WebDriverSetup.getDefaultTestTimeout(), TimeUnit.SECONDS);
	        	
	            List<WebElement> columnCollection = rowElement.findElements(By.xpath(xpath));          
	            for(WebElement cell : columnCollection)
	            {	            	
					if (column != currentColumn){currentColumn++;}
					else
	            	{
	            		//System.out.println("row #:"+currentRow+", col #:"+currentColumn+ " text="+ cell.getText());
						elementCell = cell;
	            		found = true;
	            		break;
	            	}										
	            }
	            if (found){break;}
        	}        	
        }
		return elementCell;     	
	}
	

	public void clickCell( WebDriver driver, int row, int column){
    	driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		List<WebElement> rowCollection = this.element.findElements(By.xpath("tr"));

		if (rowCollection.size() == 0) {
			rowCollection = this.element.findElements(By.xpath("tbody/tr"));
		}
    	driver.manage().timeouts().implicitlyWait(WebDriverSetup.getDefaultTestTimeout(), TimeUnit.SECONDS);

        int currentRow = 1,currentColumn = 1;
        String xpath = null;        
        Boolean found = false;
        
        for(WebElement rowElement : rowCollection)
        {        	
        	if(row != currentRow){currentRow++;}
        	else{
			   	driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
	        	if(rowElement.findElements(By.xpath("th")).size() != 0)
	        	{
	        		xpath = "th";
	        	}else if(rowElement.findElements(By.xpath("td")).size() != 0)
	        	{
	        		xpath = "td";
	        	//need to throw an exception
	        	}
	        	
	        	driver.manage().timeouts().implicitlyWait(WebDriverSetup.getDefaultTestTimeout(), TimeUnit.SECONDS);
	        	
	            List<WebElement> columnCollection = rowElement.findElements(By.xpath(xpath));          
	            for(WebElement cell : columnCollection)
	            {	            	
					if (column != currentColumn){currentColumn++;}
					else
	            	{
	            		//System.out.println("row #:"+currentRow+", col #:"+currentColumn+ " text="+ cell.getText());
	            		cell.click();
	            		found = true;
	            		break;
	            	}										
	            }
	            if (found){break;}
        	}        	
        }   	
	}

	public String getCellData( WebDriver driver, int row, int column){
    	driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		List<WebElement> rowCollection = this.element.findElements(By.xpath("tr"));

		if (rowCollection.size() == 0) {
			rowCollection = this.element.findElements(By.xpath("tbody/tr"));
		}
    	driver.manage().timeouts().implicitlyWait(WebDriverSetup.getDefaultTestTimeout(), TimeUnit.SECONDS);

        int currentRow = 1,currentColumn = 1;
        String xpath = null, cellData = "";        
        Boolean found = false;
        
        for(WebElement rowElement : rowCollection)
        {        	
        	if(row != currentRow){currentRow++;}
        	else{
			   	driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
	        	if(rowElement.findElements(By.xpath("th")).size() != 0)
	        	{
	        		xpath = "th";
	        	}else if(rowElement.findElements(By.xpath("td")).size() != 0)
	        	{
	        		xpath = "td";
	        	//need to throw an exception
	        	}
	        	
	        	driver.manage().timeouts().implicitlyWait(WebDriverSetup.getDefaultTestTimeout(), TimeUnit.SECONDS);
	        	
	            List<WebElement> columnCollection = rowElement.findElements(By.xpath(xpath));          
	            for(WebElement cell : columnCollection)
	            {	            	
					if (column != currentColumn){currentColumn++;}
					else
	            	{
	            		//System.out.println("row #:"+currentRow+", col #:"+currentColumn+ " text="+ cell.getText());
	            		cellData = cell.getText();
	            		found = true;
	            		break;
	            	}										
	            }
	            if (found){break;}
        	}        	
        }
		return cellData;     	
	}
	
	public int getRowWithCellText( WebDriver driver, String text){
    	driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		List<WebElement> rowCollection = this.element.findElements(By.xpath("tr"));

		if (rowCollection.size() == 0) {
			rowCollection = this.element.findElements(By.xpath("tbody/tr"));
		}
    	driver.manage().timeouts().implicitlyWait(WebDriverSetup.getDefaultTestTimeout(), TimeUnit.SECONDS);

        int currentRow = 1,currentColumn = 1, rowFound = 0;
        String xpath = null;
        Boolean found = false;
        
        for(WebElement rowElement : rowCollection)
        {        	
        	if(currentRow <= rowCollection.size()){
			   	driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
	        	if(rowElement.findElements(By.xpath("th")).size() != 0)
	        	{
	        		xpath = "th";
	        	}else if(rowElement.findElements(By.xpath("td")).size() != 0)
	        	{
	        		xpath = "td";
	        	//need to throw an exception
	        	}
	        	
	        	driver.manage().timeouts().implicitlyWait(WebDriverSetup.getDefaultTestTimeout(), TimeUnit.SECONDS);
	        	
	            List<WebElement> columnCollection = rowElement.findElements(By.xpath(xpath));          
	            for(WebElement cell : columnCollection)
	            {	            	
					if (currentColumn <= columnCollection.size()){
						
	            		//System.out.println("row #:"+currentRow+", col #:"+currentColumn+ " text="+ cell.getText());
	            		if(cell.getText().trim().equals(text)){
	            			rowFound = currentRow;	            	
	            			found = true;
	            			break;
	            		}
	            		currentColumn++;
	            	}										
	            }
	            if (found){break;}
	            currentRow++;
	            currentColumn=1;
        	}        	
        }
		return rowFound;     	
	}
	
	public int getRowWithCellText( WebDriver driver, String text, int columnPosition){
    	driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		List<WebElement> rowCollection = this.element.findElements(By.xpath("tr"));

		if (rowCollection.size() == 0) {
			rowCollection = this.element.findElements(By.xpath("tbody/tr"));
		}
    	driver.manage().timeouts().implicitlyWait(WebDriverSetup.getDefaultTestTimeout(), TimeUnit.SECONDS);

        int currentRow = 1,currentColumn = 1, rowFound = 0;
        String xpath = null;
        Boolean found = false;
        
        for(WebElement rowElement : rowCollection)
        {        	
        	if(currentRow <= rowCollection.size()){
			   	driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
	        	if(rowElement.findElements(By.xpath("th")).size() != 0)
	        	{
	        		xpath = "th";
	        	}else if(rowElement.findElements(By.xpath("td")).size() != 0)
	        	{
	        		xpath = "td";
	        	//need to throw an exception
	        	}
	        	
	        	driver.manage().timeouts().implicitlyWait(WebDriverSetup.getDefaultTestTimeout(), TimeUnit.SECONDS);
	        	
	            List<WebElement> columnCollection = rowElement.findElements(By.xpath(xpath));          
	            for(WebElement cell : columnCollection)
	            {	            	
	            	if (currentColumn == columnPosition){
                      		//System.out.println("row #:"+currentRow+", col #:"+currentColumn+ " text="+ cell.getText());
		            		if(cell.getText().trim().equals(text)){
		            			rowFound = currentRow;	            	
		            			found = true;
		            			break;
		            		}
						}else{
		            		currentColumn++;
		            	}									
	            }
	            if (found){break;}
	            currentRow++;
	            currentColumn=1;
        	}        	
        }
		return rowFound;     	
	}
	
	public int getRowWithCellText( WebDriver driver, String text, int columnPosition, int startRow){
    	driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		List<WebElement> rowCollection = this.element.findElements(By.xpath("tr"));

		if (rowCollection.size() == 0) {
			rowCollection = this.element.findElements(By.xpath("tbody/tr"));
		}
    	driver.manage().timeouts().implicitlyWait(WebDriverSetup.getDefaultTestTimeout(), TimeUnit.SECONDS);

        int currentRow = 1,currentColumn = 1, rowFound = 0;
        String xpath = null;
        Boolean found = false;
        
        for(WebElement rowElement : rowCollection)
        {   
        	if(startRow > currentRow) {
        		currentRow++;        	
        	}else{     	
	        	if(currentRow <= rowCollection.size()){
				   	driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		        	if(rowElement.findElements(By.xpath("th")).size() != 0)
		        	{
		        		xpath = "th";
		        	}else if(rowElement.findElements(By.xpath("td")).size() != 0)
		        	{
		        		xpath = "td";
		        	//need to throw an exception
		        	}
		        	
		        	driver.manage().timeouts().implicitlyWait(WebDriverSetup.getDefaultTestTimeout(), TimeUnit.SECONDS);
		        			    
		            List<WebElement> columnCollection = rowElement.findElements(By.xpath(xpath));          
		            for(WebElement cell : columnCollection)
		            {	            	
						if (currentColumn == columnPosition){
							//System.out.println("row #:"+currentRow+", col #:"+currentColumn+ " text="+ cell.getText());
		            		if(cell.getText().trim().equals(text)){
		            			rowFound = currentRow;	            	
		            			found = true;
		            			break;
		            		}
						}else{
		            		currentColumn++;
		            	}										
		            }
		            if (found){break;}
		            currentRow++;
		            currentColumn=1;
	        	}   
        	}
        }
		return rowFound;     	
	}
	

	public int getColumnWithCellText(WebDriver driver, String text){
    	driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		List<WebElement> rowCollection = this.element.findElements(By.xpath("tr"));

		if (rowCollection.size() == 0) {
			rowCollection = this.element.findElements(By.xpath("tbody/tr"));
		}
    	driver.manage().timeouts().implicitlyWait(WebDriverSetup.getDefaultTestTimeout(), TimeUnit.SECONDS);

        int currentRow = 1,currentColumn = 1, columnFound = 0;
        String xpath = null;
        Boolean found = false;
        
        for(WebElement rowElement : rowCollection)
        {        	
        	if(currentRow <= rowCollection.size()){
			   	driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
	        	if(rowElement.findElements(By.xpath("th")).size() != 0)
	        	{
	        		xpath = "th";
	        	}else if(rowElement.findElements(By.xpath("td")).size() != 0)
	        	{
	        		xpath = "td";
	        	//need to throw an exception
	        	}
	        	
	        	driver.manage().timeouts().implicitlyWait(WebDriverSetup.getDefaultTestTimeout(), TimeUnit.SECONDS);
	        	
	            List<WebElement> columnCollection = rowElement.findElements(By.xpath(xpath));          
	            for(WebElement cell : columnCollection)
	            {	            	
					if (currentColumn <= columnCollection.size()){
						
	            		//System.out.println("row #:"+currentRow+", col #:"+currentColumn+ " text="+ cell.getText());
	            		if(cell.getText().trim().equals(text)){
	            			columnFound = currentColumn;	            	
	            			found = true;
	            			break;
	            		}
	            		currentColumn++;
	            	}										
	            }
	            if (found){break;}
	            currentRow++;
	            currentColumn=1;
        	}        	
        }
		return columnFound;     	
	}
	
	public int getColumnWithCellText(WebDriver driver, String text, int rowPosition){
    	driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		List<WebElement> rowCollection = this.element.findElements(By.xpath("tr"));

		if (rowCollection.size() == 0) {
			rowCollection = this.element.findElements(By.xpath("tbody/tr"));
		}
    	driver.manage().timeouts().implicitlyWait(WebDriverSetup.getDefaultTestTimeout(), TimeUnit.SECONDS);

        int currentRow = 1,currentColumn = 1, columnFound = 0;
        String xpath = null;
        Boolean found = false;
        
        for(WebElement rowElement : rowCollection)
        {   
        	if(rowPosition > currentRow) {
        		currentRow++;        	
        	}else{     	     	
	        	if(currentRow <= rowCollection.size()){
				   	driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		        	if(rowElement.findElements(By.xpath("th")).size() != 0)
		        	{
		        		xpath = "th";
		        	}else if(rowElement.findElements(By.xpath("td")).size() != 0)
		        	{
		        		xpath = "td";
		        	//need to throw an exception
		        	}
		        	
		        	driver.manage().timeouts().implicitlyWait(WebDriverSetup.getDefaultTestTimeout(), TimeUnit.SECONDS);
		        	
		            List<WebElement> columnCollection = rowElement.findElements(By.xpath(xpath));          
		            for(WebElement cell : columnCollection)
		            {	            	
						if (currentColumn <= columnCollection.size()){
							
		            		//System.out.println("row #:"+currentRow+", col #:"+currentColumn+ " text="+ cell.getText());
		            		if(cell.getText().trim().equals(text)){
		            			columnFound = currentColumn;	            	
		            			found = true;
		            			break;
		            		}
		            		currentColumn++;
		            	}										
		            }
		            if (found){break;}
		            currentRow++;
		            currentColumn=1;
	        	}
	        }        	
        }
		return columnFound;     	
	}

	public int getRowThatContainsCellText( WebDriver driver, String text, int columnPosition){
    	driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		List<WebElement> rowCollection = this.element.findElements(By.xpath("tr"));

		if (rowCollection.size() == 0) {
			rowCollection = this.element.findElements(By.xpath("tbody/tr"));
		}
    	driver.manage().timeouts().implicitlyWait(WebDriverSetup.getDefaultTestTimeout(), TimeUnit.SECONDS);

        int currentRow = 1,currentColumn = 1, rowFound = 0;
        String xpath = null;
        Boolean found = false;
        
        for(WebElement rowElement : rowCollection)
        {        	
        	if(currentRow <= rowCollection.size()){
			   	driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
	        	if(rowElement.findElements(By.xpath("th")).size() != 0)
	        	{
	        		xpath = "th";
	        	}else if(rowElement.findElements(By.xpath("td")).size() != 0)
	        	{
	        		xpath = "td";
	        	//need to throw an exception
	        	}
	        	
	        	driver.manage().timeouts().implicitlyWait(WebDriverSetup.getDefaultTestTimeout(), TimeUnit.SECONDS);
	        	
	            List<WebElement> columnCollection = rowElement.findElements(By.xpath(xpath));          
	            for(WebElement cell : columnCollection)
	            {	            	
	            	if (currentColumn == columnPosition){
                      		//System.out.println("row #:"+currentRow+", col #:"+currentColumn+ " text="+ cell.getText());
		            		if(cell.getText().trim().contains(text)){
		            			rowFound = currentRow;	            	
		            			found = true;
		            			break;
		            		}
						}else{
		            		currentColumn++;
		            	}									
	            }
	            if (found){break;}
	            currentRow++;
	            currentColumn=1;
        	}        	
        }
		return rowFound;     	
	}
}