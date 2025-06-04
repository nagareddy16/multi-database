package com.naga.multi_database.controller.excel;


import com.naga.multi_database.services.excel.impl.ExcelServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v3")
public class ExcelController {

    @Autowired
    private ExcelServiceImpl excelService;

    @PostMapping(value = "/excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<Map<String,String>> uploadExcel(@RequestPart("file") MultipartFile file,  String sheetName) throws IOException {
        return excelService.readExcelFile(file,sheetName);
    }

    @GetMapping("/downloadExcel")
    public ResponseEntity<byte[]> getExcelSheetDownload(@RequestParam String sheetName,
                                                        @RequestParam String entityName, @RequestParam String fileName){
        return excelService.getExcelSheetDownload(sheetName, entityName, fileName);
    }
}
