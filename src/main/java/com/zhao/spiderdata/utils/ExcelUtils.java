package com.zhao.spiderdata.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

public class ExcelUtils {

    public static <T> void generateExcelFile(List<T> list, String path) throws IOException {
        Workbook[] wbs = new HSSFWorkbook[] { new HSSFWorkbook() };
        for (int i = 0; i < wbs.length; i++) {
            Workbook workbook = wbs[i];
            // 得到一个POI的工具类
            CreationHelper createHelper = workbook.getCreationHelper();

            // 在Excel工作簿中建一工作表,其名为缺省值, 也可以指定Sheet名称
            Sheet sheet = workbook.createSheet();

            // 用于格式化单元格的数据
            DataFormat format = workbook.createDataFormat();

            // 设置单元格类型
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setWrapText(true);

            for (int rowNum = 0; rowNum < list.size(); rowNum++) {
                // 创建行
                Row row = sheet.createRow(rowNum);

                // 创建单元格
                Object obj = list.get(rowNum);
                Class<T> c = (Class<T>) obj.getClass();
                Field[] fields = c.getDeclaredFields();

                for (int j=0; j<fields.length; j++) {
                    Field field = fields[j];
                    StringBuffer stringBuilder =  new StringBuffer("get");
                    stringBuilder.append(field.getName().substring(0,1).toUpperCase());
                    stringBuilder.append(field.getName().substring(1));
                    Method getMethod = null;
                    String filedValue = "";
                    try {
                        getMethod = c.getMethod(stringBuilder.toString());
                        filedValue = (String) getMethod.invoke(obj);

                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                    Cell cell = row.createCell(j);

                    // 设置单元格内容
                    cell.setCellValue(createHelper.createRichTextString(filedValue));

                    // 设置单元格样式
                    cell.setCellStyle(cellStyle);

                    // 指定单元格格式：数值、公式或字符串
                    cell.setCellType(CellType.STRING);
                }

            }

            //保存
            StringBuffer filename = new StringBuffer(path);
            filename.append(UUID.randomUUID());
            filename.append(".xls");
            File file = new File(filename.toString());
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);

            workbook.write(out);
            out.close();

        }
    }

}
