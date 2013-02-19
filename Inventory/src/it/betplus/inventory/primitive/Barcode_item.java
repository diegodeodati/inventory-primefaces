package it.betplus.inventory.primitive;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;

import com.lowagie.text.pdf.Barcode;
import com.lowagie.text.pdf.BarcodeEAN;

public class Barcode_item implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1539382097008223008L;
	String barcode;
	String description;
	boolean used;
	GeneralKind gKind;

	public Barcode_item(String i, String description, boolean used) {
		super();
		this.barcode = i;
		this.description = description;
		this.used = used;
		barcodeToImage();
	}

	public Barcode_item() {
		barcode = "none";
		description = "none";
		used = false;
		gKind = new GeneralKind();
		// TODO Auto-generated constructor stub
	}

	public String getBarcode() {
		if (barcode == null)
			return "none";
		else if (barcode.equals(""))
			return "none";
		else
			return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getDescription() {
		if (description != null)
			if (!description.equals("none"))
				return description;
			else
				return "";
		else
			return "";
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public GeneralKind getgKind() {
		return gKind;
	}

	public void setgKind(GeneralKind gKind) {
		this.gKind = gKind;
	}

	public void barcodeToImage() {
		try {
			if (!getBarcode().equals("none") && !getBarcode().equals("")) {

				ServletContext ctx = (ServletContext) FacesContext
						.getCurrentInstance().getExternalContext().getContext();
				String deploymentDirectoryPath = ctx.getRealPath("/");
				File checkFile = new File(deploymentDirectoryPath
						+ "images\\barcodes\\" + barcode + ".png");

				if (!checkFile.exists()) {
					BarcodeEAN barcodeEAN = new BarcodeEAN();
					barcodeEAN.setCode(barcode);
					barcodeEAN.setCodeType(Barcode.EAN13);
					Image img = barcodeEAN.createAwtImage(Color.BLACK,
							Color.WHITE);
					
					
					BufferedImage outImage = new BufferedImage(
							img.getWidth(null), img.getHeight(null) + 10,
							BufferedImage.TYPE_INT_ARGB);

					// outImage.getGraphics().drawImage(img, 0, 0, null);

					Graphics2D g2d = (Graphics2D) outImage.getGraphics();

					g2d.drawImage(img, 0, 0, null);

					g2d.setPaint(Color.BLACK);
					Font font = new Font("Serif", Font.PLAIN, 10);
					g2d.setFont(font);
					g2d.drawString(getBarcode(), 10, img.getHeight(null) + 10);

					ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
					ImageIO.write(outImage, "png", bytesOut);
					bytesOut.flush();
					byte[] pngImageData = bytesOut.toByteArray();

					FileOutputStream fos = new FileOutputStream(
							deploymentDirectoryPath + "images\\barcodes\\"
									+ barcode + ".png");
					fos.write(pngImageData);
					fos.flush();
					fos.close();
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((barcode == null) ? 0 : barcode.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + (used ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Barcode_item other = (Barcode_item) obj;
		if (barcode == null) {
			if (other.barcode != null)
				return false;
		} else if (!barcode.equals(other.barcode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Barcode_item [barcode=" + barcode + ", description="
				+ description + ", used=" + used + ", gKind=" + gKind + "]";
	}

}