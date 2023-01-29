package com.qiuyu.demo.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * EasyExcel导出工具类
 *
 * <dependency>
 *     <groupId>com.alibaba</groupId>
 *     <artifactId>easyexcel</artifactId>
 *     <version>3.0.5</version>
 * </dependency>
 *
 * @author qy
 * @since 2022/7/12 16:26
 */
@Slf4j
public class EasyExcelUtil {

    private static final int MAXROWS = 1000000;

    /**
     * 获取默认表头内容的样式
     */
    private static HorizontalCellStyleStrategy getDefaultHorizontalCellStyleStrategy() {
        /* 表头样式 **/
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景色（浅灰色）
        // 可以参考：https://www.cnblogs.com/vofill/p/11230387.html
        headWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        // 字体大小
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 10);
        headWriteCellStyle.setWriteFont(headWriteFont);
        //设置表头居中对齐
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        /* 内容样式 **/
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 内容字体样式（名称、大小）
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontName("宋体");
        contentWriteFont.setFontHeightInPoints((short) 10);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        //设置内容垂直居中对齐
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置内容水平居中对齐
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        //设置边框样式
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        // 头样式与内容样式合并
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }

    /**
     * 导出
     */
    public static <T> void writeExcel(HttpServletResponse response, List<T> data, String fileName, String sheetName, Class<T> clazz) throws Exception {
        long exportStartTime = System.currentTimeMillis();
        log.info("报表导出Size: " + data.size() + "条。");
        EasyExcel.write(getOutputStream(fileName, response), clazz).excelType(ExcelTypeEnum.XLSX).sheet(sheetName).registerWriteHandler(getDefaultHorizontalCellStyleStrategy()).doWrite(data);
        log.info("报表导出结束时间:" + new Date() + ";写入耗时: " + (System.currentTimeMillis() - exportStartTime) + "ms");
    }


    private static OutputStream getOutputStream(String fileName, HttpServletResponse response) throws Exception {
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); // .xlsx
        response.setCharacterEncoding("utf8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        return response.getOutputStream();
    }


}