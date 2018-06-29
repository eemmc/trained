package zz.yy.xx.color;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import javax.imageio.ImageIO;

import org.junit.Test;

public class ColorUtilsTest {

	public static int threshold(int value, double pre) {
		double b = value / pre;
		double t = Math.floor(b) * pre;
		if (t < 256) {
			t += pre / 2;
		}
		return (int) Math.round(t);
	}

	@Test
	public void demo01() {

		int V = 256 * 256 * 256;
		int C = 256;

		double a = (double) V / (256 * 256 * 256);
		double l = Math.floor(Math.cbrt(C / a));
		double p = 256 / l;

		System.err.println("r:" + l + ",p:" + p + ",h" + (p * 0.5));
		HashSet<Integer> set = new HashSet<>();
		for (int i = 0; i < 256; i++) {
			int value = threshold(i, p);
			System.err.printf("%03d=>%03d\n", i, value);
			set.add(value);
		}

		System.err.println("size:" + set.size());
	}

	@Test
	public void demo02() {
		int[] arr = { 1, 5, 2, 8, 6, 7, 4, 9, 3 };
		System.err.println(Arrays.toString(arr));
		Arrays.sort(arr);
		System.err.println(Arrays.toString(arr));

		System.err.println(Integer.compare(2, 3));
	}

	@Test
	public void demo03() throws IOException {
		String name = "/home/ssc/Pictures/demo.png";
		String last = "/home/ssc/Pictures/demo.last.png";
		BufferedImage img = ImageIO.read(new File(name));
		int[] pixels = new int[img.getWidth() * img.getHeight()];
		img.getRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());
		// System.err.println(Arrays.toString(Arrays.copyOf(pixels, 10)));
		pixels = ColorUtils.compress(pixels, 256);
		// System.err.println(Arrays.toString(Arrays.copyOf(pixels, 10)));
		img.setRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());
		ImageIO.write(img, "PNG", new File(last));
	}

	@Test
	public void demo04() throws IOException {

		String name = "/home/ssc/Pictures/demo.last.ppm";
		String last = "/home/ssc/Pictures/demo.last.ppm.png";
		int[] pixels = ColorUtils.readPPMImage(name);
		
		pixels = ColorUtils.compress(pixels, 256);
		
		//BufferedImage img = new BufferedImage(1200, 821, BufferedImage.TYPE_4BYTE_ABGR);
		//img.setRGB(0, 0, 1200, 821, pixels, 0, 1200);
		//ImageIO.write(img, "PNG", new File(last));
	}

}
