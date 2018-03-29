package com.yinhe.server.AcsServer.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.yinhe.server.AcsServer.backbean.NodeModelDetailBean;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ExcelConverter {
	List<NodeModelDetailBean> nodelist = new ArrayList<NodeModelDetailBean>();

	public void parseExcel(String fileName) throws BiffException, IOException {
		System.out.println("[parseExcel] fileName = " + fileName);
		InputStream is = new FileInputStream(fileName);
		Workbook wb = Workbook.getWorkbook(is);
		Sheet sh = wb.getSheet(0);
		int clos = sh.getColumns();
		int rows = sh.getRows();
		for (int i = 1; i < rows; i++) {
			NodeModelDetailBean nm = new NodeModelDetailBean();
			for (int j = 0; j < clos; j++) {
				String cell_name = sh.getCell(j, 0).getContents();
				switch (cell_name) {
				case "name":
					nm.setAbbr_name(sh.getCell(j, i).getContents());
					break;
				case "path":
					nm.setNode_path(sh.getCell(j, i).getContents());
					break;
				case "type":
					nm.setType(sh.getCell(j, i).getContents());
					break;
				case "rw":
					nm.setRw(sh.getCell(j, i).getContents());
					break;
				case "getc":
					nm.setGetc(sh.getCell(j, i).getContents());
					break;
				case "noc":
					nm.setNoc(sh.getCell(j, i).getContents());
					break;
				case "nocc":
					nm.setNocc(sh.getCell(j, i).getContents());
					break;
				case "acl":
					nm.setAcl(sh.getCell(j, i).getContents());
					break;
				case "il":
					nm.setIl(sh.getCell(j, i).getContents());
					break;
				case "default_value":
					nm.setDefault_value(sh.getCell(j, i).getContents());
					break;
				case "min_value":
					nm.setMin_value(sh.getCell(j, i).getContents());
					break;
				case "max_value":
					nm.setMax_value(sh.getCell(j, i).getContents());
					break;
				case "other_value":
					nm.setOther_value(sh.getCell(j, i).getContents());
					break;
				case "value_type":
					nm.setValue_type(sh.getCell(j, i).getContents());
					break;
				case "unit":
					nm.setUnit(sh.getCell(j, i).getContents());
					break;
				case "nin":
					nm.setNin(sh.getCell(j, i).getContents());
					break;
				}
			}
			nodelist.add(nm);
			System.out.println("[parseExcel] " + nm.toString());
		}
	
	}

	public List<NodeModelDetailBean> getNodelist() {
		return nodelist;
	}

	public void setNodelist(List<NodeModelDetailBean> nodelist) {
		this.nodelist = nodelist;
	}

}
