/***************************************************************************
 *                                                                         *
 * Auto Generated BY CodeComment                                           *
 * Copyright(C) 2000-2016, JiangSu Yinhe Electronic Co.Ltd.                *
 * All Rights Reserved.                                                    *
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Yinhe Elec Co.Ltd.       *
 * The copyright notice above does not evidence any actual or intended     *
 * publication of such source code.                                        *
 *                                                                         *
 **************************************************************************/

package com.yinhe.server.AcsServer.controller;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.Converter;
import javax.inject.Named;

import com.yinhe.server.AcsServer.util.DateTimeConverter;

@Named
@RequestScoped
public class ConverterFactory {
	private DateTimeConverter dateTimeConverter;
	
	public ConverterFactory() {
		System.out.println("ConverterFactory constructor start!");
	}
	
	public Converter getDateTimeConverter() {
		if (null == dateTimeConverter) {
			dateTimeConverter = new DateTimeConverter();
		}
		return dateTimeConverter;
	}
}