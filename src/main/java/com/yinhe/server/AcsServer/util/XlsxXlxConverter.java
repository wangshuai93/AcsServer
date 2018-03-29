package com.yinhe.server.AcsServer.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.yinhe.server.AcsServer.backbean.NodeModelDetailBean;

public class XlsxXlxConverter {
	List<NodeModelDetailBean> nodelist = new ArrayList<NodeModelDetailBean>();
	
	public void parseXlsxXls(String fileName) {
		boolean isXlsx = false;
		boolean isXls = false;
		if (fileName.endsWith(".xlsx")) {
			isXlsx = true;
		} else if (fileName.endsWith(".xls")) {
			isXls = true;
		}
		try {
			InputStream is = new FileInputStream(fileName);
			// 读xlsx
			XSSFWorkbook xssfWorkbook = null;
			// 读取工作簿
			XSSFSheet xssfSheet = null;
			// 第一行
			XSSFRow xlsxRow1 = null;
			int xlsxRows = 0;
			int xlsxCols = 0;

			// 读xls
			HSSFWorkbook hssfWorkbook = null;
			HSSFSheet hssfSheet = null;
			HSSFRow xlsRow1 = null;

			int xlsRows = 0;
			int xlsCols = 0;

			if (isXlsx) {
				xssfWorkbook = new XSSFWorkbook(is);
				xssfSheet = xssfWorkbook.getSheetAt(0);
				xlsxRows = xssfSheet.getLastRowNum();
				if (xlsxRows != 0) {
					xlsxCols = xssfSheet.getRow(0).getPhysicalNumberOfCells();
					xlsxRow1 = xssfSheet.getRow(0);
				}

				doRead(nodelist, xlsxRows, xssfSheet, xlsxCols, xlsxRow1,
						xlsRows, xlsCols, hssfSheet, xlsRow1);

			} else if (isXls) {
				hssfWorkbook = new HSSFWorkbook(is);
				hssfSheet = hssfWorkbook.getSheetAt(0);
				xlsRows = hssfSheet.getLastRowNum();
				if (xlsRows != 0) {
					xlsCols = hssfSheet.getRow(0).getPhysicalNumberOfCells();
					xlsRow1 = hssfSheet.getRow(0);
					doRead(nodelist, xlsxRows, xssfSheet, xlsxCols, xlsxRow1,
							xlsRows, xlsCols, hssfSheet, xlsRow1);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void doRead(List<NodeModelDetailBean> nodelist, int xlsxRows,
			XSSFSheet xssfSheet, int xlsxCols, XSSFRow xlsxRow1, int xlsRows,
			int xlsCols, HSSFSheet hssfSheet, HSSFRow xlsRow1) {
		int rows = 0;
		int cols = 0;
		XSSFRow xssfRow = null;
		HSSFRow hssfRow = null;

		if (xlsxRows != 0) {
			rows = xlsxRows;
		} else if (xlsRows != 0) {
			rows = xlsRows;
		}

		if (xlsxCols != 0) {
			cols = xlsxCols;
		} else if (xlsCols != 0) {
			cols = xlsCols;
		}

		for (int row = 1; row <= rows; row++) {
			NodeModelDetailBean nm = new NodeModelDetailBean();
			if (null != xssfSheet) {
				xssfRow = xssfSheet.getRow(row);
			} else if (null != hssfSheet) {
				hssfRow = hssfSheet.getRow(row);
			}

			if (null != xssfRow) {
				for (int col = 0; col < cols; col++) {
					// 获取第一行列名字
					XSSFCell cell1 = xlsxRow1.getCell(col);
					if (null != xssfRow.getCell(col)) {
						switch (getValue(cell1, null)) {
						case "name":
							nm.setAbbr_name(getValue(xssfRow.getCell(col), null));
							break;
						case "path":
							nm.setNode_path(getValue(xssfRow.getCell(col), null));
							break;
						case "type":
							nm.setType(getValue(xssfRow.getCell(col), null));
							break;
						case "rw":
							nm.setRw(getValue(xssfRow.getCell(col), null));
							break;
						case "getc":
							nm.setGetc(getValue(xssfRow.getCell(col), null));
							break;
						case "noc":
							nm.setNoc(getValue(xssfRow.getCell(col), null));
							break;
						case "nocc":
							nm.setNocc(getValue(xssfRow.getCell(col), null));
							break;
						case "acl":
							nm.setAcl(getValue(xssfRow.getCell(col), null));
							break;
						case "nin":
							nm.setNin(getValue(xssfRow.getCell(col), null));
							break;
						case "il":
							nm.setIl(getValue(xssfRow.getCell(col), null));
							break;
						case "default_value":
							nm.setDefault_value(getValue(xssfRow.getCell(col),
									null));
							break;
						case "min_value":
							nm.setMin_value(getValue(xssfRow.getCell(col), null));
							break;
						case "max_value":
							nm.setMax_value(getValue(xssfRow.getCell(col), null));
							break;
						case "other_value":
							nm.setOther_value(getValue(xssfRow.getCell(col),
									null));
							break;
						case "value_type":
							nm.setValue_type(getValue(xssfRow.getCell(col),
									null));
							break;
						case "unit":
							nm.setUnit(getValue(xssfRow.getCell(col), null));
							break;
						case "nameZh":
							nm.setNameZh(getValue(xssfRow.getCell(col), null));
							break;
						default:
							break;
						}
					}
				}
			} else if (null != hssfRow) {
				for (int col = 0; col < cols; col++) {
					// 获取第一行列名字
					HSSFCell cell1 = xlsRow1.getCell(col);
					if (null != hssfRow.getCell(col)) {
						switch (getValue(null, cell1)) {
						case "name":
							nm.setAbbr_name(getValue(null, hssfRow.getCell(col)));
							break;
						case "path":
							nm.setNode_path(getValue(null, hssfRow.getCell(col)));
							break;
						case "type":
							nm.setType(getValue(null, hssfRow.getCell(col)));
							break;
						case "rw":
							nm.setRw(getValue(null, hssfRow.getCell(col)));
							break;
						case "getc":
							nm.setGetc(getValue(null, hssfRow.getCell(col)));
							break;
						case "noc":
							nm.setNoc(getValue(null, hssfRow.getCell(col)));
							break;
						case "nocc":
							nm.setNocc(getValue(null, hssfRow.getCell(col)));
							break;
						case "acl":
							nm.setAcl(getValue(null, hssfRow.getCell(col)));
							break;
						case "nin":
							nm.setNin(getValue(null, hssfRow.getCell(col)));
							break;
						case "il":
							nm.setIl(getValue(null, hssfRow.getCell(col)));
							break;
						case "default_value":
							nm.setDefault_value(getValue(null,
									hssfRow.getCell(col)));
							break;
						case "min_value":
							nm.setMin_value(getValue(null, hssfRow.getCell(col)));
							break;
						case "max_value":
							nm.setMax_value(getValue(null, hssfRow.getCell(col)));
							break;
						case "other_value":
							nm.setOther_value(getValue(null,
									hssfRow.getCell(col)));
							break;
						case "value_type":
							nm.setValue_type(getValue(null,
									hssfRow.getCell(col)));
							break;
						case "unit":
							nm.setUnit(getValue(null, hssfRow.getCell(col)));
							break;
						case "nameZh":
							nm.setNameZh(getValue(null, hssfRow.getCell(col)));
							break;
						default:
							break;
						}
					}
				}
			}
			nodelist.add(nm);
//			System.out.println(nm.toString());
		}
	}

	@SuppressWarnings({ "static-access" })
	private String getValue(XSSFCell xssfCell, HSSFCell hssfCell) {
		if (null != xssfCell) {
			if (xssfCell.getCellType() == xssfCell.CELL_TYPE_BOOLEAN) {
				return String.valueOf(xssfCell.getBooleanCellValue());
			} else if (xssfCell.getCellType() == xssfCell.CELL_TYPE_NUMERIC) {
				// 数字去除后面无用的小数点
				String str = String.valueOf(xssfCell.getNumericCellValue());
				if (str.indexOf(".") > 0) {
					str = str.replaceAll("0+?$", "");
					str = str.replaceAll("[.]$", "");
				}
				return str;
			} else {
				return String.valueOf(xssfCell.getStringCellValue());
			}
		} else if (null != hssfCell) {
			if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
				return String.valueOf(hssfCell.getBooleanCellValue());
			} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
				String str = String.valueOf(hssfCell.getNumericCellValue());
				if (str.indexOf(".") > 0) {
					str = str.replaceAll("0+?$", "");
					str = str.replaceAll("[.]$", "");
				}
				return str;
			} else {
				return String.valueOf(hssfCell.getStringCellValue());
			}
		}
		return null;
	}
	
	public List<NodeModelDetailBean> getNodelist() {
		return nodelist;
	}

	public void setNodelist(List<NodeModelDetailBean> nodelist) {
		this.nodelist = nodelist;
	}

}
