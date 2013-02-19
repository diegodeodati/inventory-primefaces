package it.betplus.inventory.utils;

import it.betplus.inventory.primitive.Barcode_print;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.print.PrintException;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class Printer {

	protected static final Log log = LogFactory.getLog(Printer.class);

	private static Printer instance;

	private Printer() {
		super();
		ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance()
				.getExternalContext().getContext();
		String deploymentDirectoryPath = ctx.getRealPath("/");
		new File(deploymentDirectoryPath + "export").mkdir();
	}

	public static synchronized Printer getInstance() {
		if (instance == null) {
			synchronized (Printer.class) {
				instance = new Printer();
			}
		}
		return instance;
	}

	public StreamedContent doPrint(List<Barcode_print> internalList)
			throws PrintException, IOException, DocumentException {

		ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance()
				.getExternalContext().getContext();
		String deploymentDirectoryPath = ctx.getRealPath("/");

		Document document = new Document(PageSize.A4, 10, 10, 20, -20);

		PdfWriter.getInstance(document, new FileOutputStream(
				deploymentDirectoryPath + "export\\barcode_list.pdf"));
		PdfPTable table = new PdfPTable(3);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.setWidthPercentage(100);

		document.open();

		/* tabella interna */
		int i = 1;
		for (Barcode_print bp : internalList) {

			for (int j = 0; j < bp.getQuantity(); j++) {

				PdfPTable internalTable = new PdfPTable(1);

				PdfPCell internalCellString = new PdfPCell();
				Font f = new Font(Font.HELVETICA, 8);
				Phrase p = new Phrase("CATEGORIA", f);
				internalCellString.addElement(p);
				internalCellString.setBorder(Rectangle.NO_BORDER);
				internalTable.addCell(internalCellString);

				Image image = Image.getInstance(deploymentDirectoryPath
						+ "images\\barcodes\\" + bp.getBarcode().getBarcode()
						+ ".png");
				image.setAlignment(Image.MIDDLE);
				PdfPCell internalCellImg = new PdfPCell(image, false);
				internalCellImg.setBorder(Rectangle.NO_BORDER);
				internalTable.addCell(internalCellImg);

				internalCellString = new PdfPCell();
				f = new Font(Font.HELVETICA, 8);
				p = new Phrase("String", f);
				internalCellString.addElement(p);
				internalCellString.setBorder(Rectangle.NO_BORDER);
				internalTable.addCell(internalCellString);

				internalTable.setSpacingAfter(20f);
				internalTable.setSpacingBefore(20f);

				/* cell della tabella */
				PdfPCell cell = new PdfPCell();
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.addElement(internalTable);

				cell.setPaddingTop(40);
				cell.setPaddingBottom(0);

				if (i % 3 == 1) {
					cell.setPaddingLeft(20);
					table.addCell(cell);
				}

				if (i % 3 == 2) {
					cell.setPaddingLeft(30);
					table.addCell(cell);
				}

				if (i % 3 == 0) {
					cell.setPaddingLeft(40);
					table.addCell(cell);
					cell.setPaddingTop(32);
				}

				i++;
			}
			/* fill to tre element on the table inside the pdf */

			if (internalList.lastIndexOf(bp) == internalList.size() - 1) {

				if ((i - 1) % 3 != 0) {

					for (int j = 0; j < 3 - internalList.get(
							internalList.size() - 1).getQuantity(); j++) {

						PdfPCell cell = new PdfPCell();
						cell.setBorder(Rectangle.NO_BORDER);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);

						if (i % 3 == 1) {
							cell.setPaddingLeft(20);
							table.addCell(cell);
						}

						if (i % 3 == 2) {
							cell.setPaddingLeft(30);
							table.addCell(cell);
						}

						if (i % 3 == 0) {
							cell.setPaddingLeft(40);
							table.addCell(cell);
							cell.setPaddingTop(32);
						}

						i++;

					}
				}
			}

		}

		document.add(table);
		document.close();

		FileInputStream fis = new FileInputStream(deploymentDirectoryPath
				+ "export\\barcode_list.pdf");
		StreamedContent file = new DefaultStreamedContent(fis,
				"application/pdf", "downloaded_optimus.pdf");

		return file;
	}

}
