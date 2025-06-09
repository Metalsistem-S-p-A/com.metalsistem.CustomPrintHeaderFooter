package com.metalsistem.CustomPrintHeaderFooter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.compiere.model.MQuery;
import org.compiere.model.MSysConfig;
import org.compiere.print.MPrintFormat;
import org.compiere.print.MPrintFormatItem;
import org.compiere.print.MPrintTableFormat;
import org.compiere.print.layout.HeaderFooter;
import org.compiere.print.layout.PrintElement;
import org.compiere.print.layout.StringElement;
import org.compiere.util.Env;
import org.compiere.util.Util;
import org.idempiere.print.IPrintHeaderFooter;
import org.osgi.service.component.annotations.Component;

@Component(name="com.metalsistem.CustomPrintHeaderFooter.CustomPrintHeaderFooter", immediate=true, service=IPrintHeaderFooter.class)
public class CustomPrintHeaderFooter implements IPrintHeaderFooter {

	@Override
	public void createHeaderFooter(MPrintFormat m_format, HeaderFooter m_headerFooter, Rectangle m_header,
			Rectangle m_footer, MQuery m_query) {
		PrintElement element = null;
		//
		MPrintTableFormat tf = m_format.getTableFormat();
		Font font = tf.getPageHeader_Font();
		Color color = tf.getPageHeaderFG_Color();
		//
		element = new StringElement("@#AD_Client_Name@ - @#AD_Org_Name@", font.deriveFont(10f), color, null, true);
		element.layout (m_header.width, 0, true, MPrintFormatItem.FIELDALIGNMENTTYPE_LeadingLeft);
		element.setLocation(m_header.getLocation());
		m_headerFooter.addElement(element);
		
		element = new StringElement("@*ReportName@", font.deriveFont(10f), color, null, true);
		element.layout (m_header.width, 0, true, MPrintFormatItem.FIELDALIGNMENTTYPE_Center);
		element.setLocation(m_header.getLocation());
		m_headerFooter.addElement(element);
		
		//	Footer
		font = tf.getPageFooter_Font();
		color = tf.getPageFooterFG_Color();
		//
		String timestamp = "";
		String s = MSysConfig.getValue(MSysConfig.ZK_FOOTER_SERVER_DATETIME_FORMAT, Env.getAD_Client_ID(Env.getCtx()));
		if (!Util.isEmpty(s, true))
			timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(s));
		else
			timestamp = "@*CurrentDateTime@";
		element = new StringElement(timestamp, font.deriveFont(7.5f), color, null, true);
		element.layout (m_footer.width, 0, true, MPrintFormatItem.FIELDALIGNMENTTYPE_LeadingLeft);
		element.setLocation(m_footer.getLocation());
		m_headerFooter.addElement(element);
		
		s = MSysConfig.getValue(MSysConfig.ZK_FOOTER_SERVER_MSG, "", Env.getAD_Client_ID(Env.getCtx()));
		if (!Util.isEmpty(s, true)) {
			element = new StringElement(s, font.deriveFont(7.5f), color, null, true);
			element.layout (m_footer.width, 0, true, MPrintFormatItem.FIELDALIGNMENTTYPE_Center);
			element.setLocation(m_footer.getLocation());
			m_headerFooter.addElement(element);
		}
		
		element = new StringElement("@*MultiPageInfo@", font.deriveFont(7.5f), color, null, true);
		element.layout (m_header.width, 0, true, MPrintFormatItem.FIELDALIGNMENTTYPE_TrailingRight);
		element.setLocation(m_footer.getLocation());
		m_headerFooter.addElement(element);
	}

	@Override
	public int getHeaderHeight() {
		return 15;
	}

	@Override
	public int getFooterHeight() {
		return 12;
	}

}
