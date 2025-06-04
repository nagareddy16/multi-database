package com.naga.multi_database.services.excel.impl;

import com.naga.multi_database.mongodb.model.Product;
import com.naga.multi_database.services.employee.impl.EmployeeServiceImpl;
import com.naga.multi_database.services.excel.ExcelService;
import com.naga.multi_database.services.product.impl.ProductServiceImpl;
import com.naga.multi_database.sqldb.dto.EmployeeDTO;
import com.naga.multi_database.sqldb.entity.Employee;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.IntStream;


@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private EmployeeServiceImpl employeeService;

    @Autowired
    private ProductServiceImpl productService;

    private static final Logger log = LoggerFactory.getLogger(ExcelServiceImpl.class);

    /**
     * @param file
     * @return
     */
    @Override
    public List<Map<String, String>> readExcelFile(MultipartFile file, String sheetName) throws IOException {
        if (file.isEmpty()) {
            log.error("File can't be empty : {}", file.getName());
            throw new RuntimeException("File can't be empty.");
        } else if (file.getOriginalFilename().endsWith(".xlx") || file.getOriginalFilename().endsWith(".xlsx")) {
            InputStream stream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(stream);
            List<String> sheets = new ArrayList<>();
            for (int num = 0; num < workbook.getNumberOfSheets(); num++) {
                sheets.add(workbook.getSheetAt(num).getSheetName());
            }

            if (!sheets.contains(sheetName))
                throw new RuntimeException("SheetName doesn't found");

            Sheet sheet = workbook.getSheet(sheetName);
            Iterator<Row> rows = sheet.iterator();
            List<Map<String, String>> mapList = new ArrayList<>();
            int rowNum = 0;
            Map<Integer, String> headerData = new HashMap<>();
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (rowNum == 0) {
                    Iterator<Cell> cellIterator = currentRow.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        headerData.put(cell.getColumnIndex(), cell.getStringCellValue());
                    }
                    rowNum++;
                    continue;
                }
                Iterator<Cell> cellIteratorVal = currentRow.cellIterator();
                Map<String, String> rowData = new HashMap<>();

                while (cellIteratorVal.hasNext()) {
                    Cell cell = cellIteratorVal.next();
                    String headerName = headerData.get(cell.getColumnIndex());
                    String cellName = getValueAsString(cell);
                    rowData.put(headerName, cellName);
                }
                mapList.add(rowData);
                rowNum++;
            }
            log.info("mapList : {}", mapList);
            for(Map<String, String> map : mapList){
                EmployeeDTO employeeDTO = new EmployeeDTO(null, map.get("firstName"), map.get("lastName"), map.get("email"));
                employeeService.saveEmployee(employeeDTO);
            }
            return mapList;
        } else {
            log.error("Un supported file type : {}", file.getOriginalFilename());
            return Collections.emptyList();
        }
    }

    private String getValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    /**
     * @param sheetName
     * @param entityName
     * @param fileName
     * @return
     */
    @Override
    public ResponseEntity<byte[]> getExcelSheetDownload(String sheetName, String entityName, String fileName) {

        if(sheetName == null || entityName == null || fileName == null){
            log.error("SheetName can't be empty : {} entityName can't be empty : {} fileName can't be empty : {}",
                    sheetName,entityName,fileName);
            throw new RuntimeException("Exception has occurred while processing the request.");
        }
        if(!fileName.endsWith(".xlsx") || !fileName.endsWith(".xlx")){
            fileName = fileName + ".xlsx";
        }
        try {
            Class<?> entityClass = Class.forName(entityName);
            List<?> entities = fetchEntities(entityClass);
            XSSFWorkbook workbook = generateExcelSheet(sheetName, entities, fileName);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            workbook.write(stream);
            byte[] excelBytes = stream.toByteArray();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .header(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .body(excelBytes);
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private XSSFWorkbook generateExcelSheet(String sheetName, List<?> entities, String fileName) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);
        Row headerRow = sheet.createRow(0);
        if(!entities.isEmpty()){
            Object entity = entities.get(0);
            Field[] fields = entity.getClass().getDeclaredFields();
            int cellIndex = 0;
            for(Field field : fields){
                field.setAccessible(true);
                Cell cell = headerRow.createCell(cellIndex++);
                cell.setCellValue(field.getName());
                sheet.autoSizeColumn(cellIndex - 1);
            }
        }
        int rowIndex = 1;
        for(Object entity : entities){
            Row row = sheet.createRow(rowIndex++);
            int cellIndex = 0;
            for(Field field : entity.getClass().getDeclaredFields()){
                field.setAccessible(true);
                try {
                    Object fieldValue = field.get(entity);
                    Cell cell = row.createCell(cellIndex++);

                    if(fieldValue instanceof String){
                        cell.setCellValue((String) fieldValue);
                    } else if (fieldValue instanceof Integer) {
                        cell.setCellValue((Integer)fieldValue);
                    } else if (fieldValue instanceof Double) {
                        cell.setCellValue((Double) fieldValue);
                    } else if (fieldValue instanceof Boolean) {
                        cell.setCellValue((Boolean) fieldValue);
                    } else if (fieldValue != null) {
                        cell.setCellValue(fieldValue.toString());
                    } else{
                        cell.setCellValue("");
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        IntStream.range(0, headerRow.getPhysicalNumberOfCells()).forEach(i -> sheet.autoSizeColumn(i));

        try(FileOutputStream stream = new FileOutputStream(fileName)){
            workbook.write(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return workbook;
    }

    private List<?> fetchEntities(Class<?> entityClass) {
        if(entityClass == Employee.class){
            return employeeService.getAllEmployees();
        } else if (entityClass == Product.class) {
            return productService.getAllProduct();
        } else {
            return List.of("Entity not found.");
        }
    }


}
