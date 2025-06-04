package com.naga.multi_database.services.excel;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ExcelService {
    List<Map<String, String>> readExcelFile(MultipartFile file,String sheetName) throws IOException;

    ResponseEntity<byte[]> getExcelSheetDownload(String sheetName, String entityName, String fileName);
}
