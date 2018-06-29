package zz.yy.xx.color;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class ColorUtils {

	static class Color implements Comparable<Color> {
		final int color;
		int r, g, b;
		int last;

		Color(int color) {
			this.color = color;
			this.r = (color >> 16) & 0xFF;
			this.g = (color >> 8) & 0xFF;
			this.b = color & 0xFF;
		}

		Color last() {

			this.last = this.b | (this.g << 8) | (this.r << 16);

			return this;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + b;
			result = prime * result + g;
			result = prime * result + r;
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
			Color other = (Color) obj;
			if (b != other.b)
				return false;
			if (g != other.g)
				return false;
			if (r != other.r)
				return false;
			return true;
		}

		@Override
		public int compareTo(Color o) {
			int p;
			p = Integer.compare(this.r, o.r);
			if (p != 0) {
				return p;
			}
			p = Integer.compare(this.g, o.g);
			if (p != 0) {
				return p;
			}

			return Integer.compare(this.b, o.b);
		}

		@Override
		public String toString() {
			return "[r=" + r + ", g=" + g + ", b=" + b + "]";
		}

	}

	public static class SpacerCalculator implements Comparator<Color> {
		private final int[][] limit = new int[3][2];

		private int max;

		public void addUpColor(Color color) {
			if (color.r > limit[0][0]) {
				limit[0][0] = color.r;
			} else if (color.r < limit[0][1]) {
				limit[0][1] = color.r;
			} else {
				limit[0][1] = limit[0][0] = color.r;
			}

			if (color.g > limit[1][0]) {
				limit[1][0] = color.g;
			} else if (color.g < limit[1][1]) {
				limit[1][1] = color.g;
			} else {
				limit[1][1] = limit[1][0] = color.g;
			}

			if (color.b > limit[2][0]) {
				limit[2][0] = color.b;
			} else if (color.b < limit[2][1]) {
				limit[2][1] = color.b;
			} else {
				limit[2][1] = limit[2][0] = color.b;
			}
		}

		@Override
		public String toString() {
			return "SpacerCalculator [limit=" + Arrays.deepToString(limit) + "]";
		}

		public void calcMax() {
			int r = limit[0][0] - limit[0][1];
			int g = limit[1][0] - limit[1][1];
			int b = limit[2][0] - limit[2][1];

			if (r >= g && r >= b) {
				this.max = 0;
			} else if (g >= r && g >= b) {
				this.max = 1;
			} else if (b >= r && b >= g) {
				this.max = 2;
			} else {
				this.max = 0;
			}
		}

		public int getMax() {
			return this.max;
		}

		public void clean() {
			limit[0][0] = 0;
			limit[0][1] = 0;
			limit[1][0] = 0;
			limit[1][1] = 0;
			limit[2][0] = 0;
			limit[2][1] = 0;
		}

		@Override
		public int compare(Color o1, Color o2) {
			switch (this.max) {
			case 0:
				return Integer.compare(o1.r, o2.r);
			case 1:
				return Integer.compare(o1.g, o2.g);
			case 2:
				return Integer.compare(o1.b, o2.b);
			default:
				return 0;
			}
		}

	}

	public static class ColorBox {
		private HashSet<Color> colors = new HashSet<>();
		private SpacerCalculator calc = new SpacerCalculator();

		int start;
		int size;
		Color current;

		public ColorBox() {
			super();
		}

		public ColorBox(int start, int size) {
			this.start = start;
			this.size = size;
		}

		public void addUpAll(Set<Color> colors) {
			for (Color color : colors) {
				addUpColor(color);
			}
		}

		public void addUpColor(Color color) {
			this.colors.add(color);
			calc.addUpColor(color);
		}

		public ColorBox dump() {
			calc.clean();
			for (Color color : colors) {
				calc.addUpColor(color);
			}

			current = new Color(0);
			current.r = (calc.limit[0][0] + calc.limit[0][1]) / 2;
			current.g = (calc.limit[1][0] + calc.limit[1][1]) / 2;
			current.b = (calc.limit[2][0] + calc.limit[2][1]) / 2;

			return this;
		}

		public Color finalColor() {
			return current;
		}

		public static void medianCut(int index, ColorBox[] boxes) {

			ColorBox box = boxes[index];
			if (box.size <= 1) {
				return;
			}

			box.calc.calcMax();
			LinkedList<Color> sort = new LinkedList<>();
			sort.addAll(box.colors);
			sort.sort(box.calc);

			int half = box.size / 2;
			int count = box.colors.size() / 2;

			box.size = half;
			ColorBox newBox = new ColorBox(box.start + half, half);

			int i = 0;
			Iterator<Color> it = sort.iterator();
			while (it.hasNext() && i < count) {
				Color color = it.next();
				box.colors.remove(color);
				newBox.addUpColor(color);
				++i;
			}

			boxes[newBox.start] = newBox;

			/* |递归分割| */
			medianCut(index, boxes);

		}
	}

	public static Set<Color> dump(int[] pixels) {
		HashSet<Color> colors = new HashSet<>();
		for (int pixel : pixels) {
			Color color = new Color(pixel & 0xFFFFFF);

			colors.add(color);
		}
		return colors;
	}

	public static void sortColorBox(ColorBox box) {
		LinkedList<Color> sort = new LinkedList<>();
		sort.addAll(box.colors);
		sort.sort(box.calc);
		System.err.println("sort:" + box.calc.max + "," + sort.subList(0, 10));
	}

	public static Set<Color> remap(Set<Color> colors, int limit) {

		if (colors == null || colors.isEmpty() || colors.size() <= limit) {
			return colors;
		}

		ColorBox[] boxes = new ColorBox[limit];

		boxes[0] = new ColorBox(0, limit);
		boxes[0].addUpAll(colors);

		for (int i = 0; i < limit; i++) {
			ColorBox.medianCut(i, boxes);
		}

		colors.clear();
		for (int i = 0; i < limit; i++) {
			// sortColorBox(boxes[i]);
			colors.add(boxes[i].dump().finalColor());
		}

		return colors;
	}

	public static int[] compress(int[] pixels, int limit) {
		Set<Color> colors = dump(pixels);
		System.err.println("length:" + pixels.length);
		System.err.println("size:" + colors.size());

		Set<Color> map = remap(colors, limit);

		System.err.println("size:" + map.size());

		System.err.println("==================");
		for (Color color : colors) {
			System.err.printf("%06X\n", color.last().last);
		}

		saveColorMap(colors);
		System.err.println("==================");

		return pixels;
	}

	public static void saveColorMap(Set<Color> colors) {
		LinkedList<Color> swap = new LinkedList<>();
		swap.addAll(colors);
		Collections.sort(swap);

		byte[] data = new byte[swap.size() * 3];
		int i = 0;
		for (Color color : swap) {
			data[i++] = (byte) color.r;
			data[i++] = (byte) color.g;
			data[i++] = (byte) color.b;
		}

		try {
			String name = "/tmp/swap/swap.ppm";
			RandomAccessFile file = new RandomAccessFile(name, "rwd");
			file.write("P6\n16 16\n255\n".getBytes());
			file.write(data);
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int[] readPPMImage(String name) throws IOException {

		RandomAccessFile file = new RandomAccessFile(name, "r");

		System.err.println(file.readLine());// P6
		System.err.println(file.readLine());
		System.err.println(file.readLine());// 1200 821
		System.err.println(file.readLine());// 255

		int length = 1200 * 821;

		System.err.println(length);
		byte[] buffer = new byte[length * 3];
		System.err.println(file.read(buffer, 0, length * 3));

		file.close();

		int[] pixels = new int[length];
		for (int i = 0, p = 0; i < length; ++i) {

			int r = buffer[p++] & 0xFF;
			int g = buffer[p++] & 0xFF;
			int b = buffer[p++] & 0xFF;

			pixels[i] = 0xFF000000 | (r << 16) | (g << 8) | b;
		}

		return pixels;
	}

}
