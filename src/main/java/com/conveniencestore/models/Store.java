package com.conveniencestore.models;

import com.conveniencestore.enums.ProductCategory;
import lombok.Data;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Data
public class Store {
    private String name;
    private List<Applicant> applicants;
    private List<Staff> staffs;
    private List<Transaction> transactions;
    private Product[] listOfProductsInStore;
    private final String excelFilePath;
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();
    private Set<Customer> multithreadedList;

    {
        excelFilePath = "./src/main/resources/Contries.xlsx";
    }

    public Store(String name){
        this.name = name;
        this.applicants = new ArrayList<>();
        this.staffs = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.multithreadedList = new HashSet<>();
        this.listOfProductsInStore = loadProductsFromSheet();
    }

    public Product [] getListOfProductsInStore(){
        try {
            readLock.lock();
            return this.listOfProductsInStore;
        } finally {
            readLock.unlock();
        }
    }

    private Product[] loadProductsFromSheet() {
        try (FileInputStream fileInputStream = new FileInputStream(excelFilePath)) {

            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int numberOfRows = sheet.getPhysicalNumberOfRows();
            this.listOfProductsInStore = new Product[numberOfRows - 1];
            int index = 0;

            for (int row = 1; row < numberOfRows; row++) {
                XSSFRow rows = sheet.getRow(row);
                Product product = new Product(
                        rows.getCell(0).getStringCellValue(),
                        rows.getCell(1).getNumericCellValue(),
                        (int) rows.getCell(3).getNumericCellValue(),
                        ProductCategory.valueOf(rows.getCell(2).getStringCellValue().toUpperCase())
                );
                product.checkAndSetAvailability();
                this.listOfProductsInStore[index++] = product;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this.listOfProductsInStore;
    }
}
