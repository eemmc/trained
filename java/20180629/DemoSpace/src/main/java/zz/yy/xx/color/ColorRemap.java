package zz.yy.xx.color;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ColorRemap {
	static class Tuple implements Comparable<Tuple> {
		int index;
		int r, g, b;

		Tuple(int color) {
			this.r = (color >> 16) & 0xFF;
			this.g = (color >> 8) & 0xFF;
			this.b = color & 0xFF;
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
			Tuple other = (Tuple) obj;
			if (b != other.b)
				return false;
			if (g != other.g)
				return false;
			if (r != other.r)
				return false;
			return true;
		}

		@Override
		public int compareTo(Tuple o) {
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

		public int rgb() {
			return this.b | (this.g << 8) | (this.r << 16);
		}

		public int argb() {
			return this.b | (this.g << 8) | (this.r << 16) | 0xFF000000;
		}
	}

	static class Spacer implements Comparator<Tuple> {
		int max;

		private final int[][] limit;

		Spacer() {
			this.limit = new int[3][2];
		}

		void add(Tuple tuple) {
			if (tuple.r > limit[0][0]) {
				limit[0][0] = tuple.r;
			} else if (tuple.r < limit[0][1]) {
				limit[0][1] = tuple.r;
			} else {
				limit[0][1] = limit[0][0] = tuple.r;
			}

			if (tuple.g > limit[1][0]) {
				limit[1][0] = tuple.g;
			} else if (tuple.g < limit[1][1]) {
				limit[1][1] = tuple.g;
			} else {
				limit[1][1] = limit[1][0] = tuple.g;
			}

			if (tuple.b > limit[2][0]) {
				limit[2][0] = tuple.b;
			} else if (tuple.b < limit[2][1]) {
				limit[2][1] = tuple.b;
			} else {
				limit[2][1] = limit[2][0] = tuple.b;
			}
		}

		void calc() {
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

		void clean() {
			limit[0][0] = 0;
			limit[0][1] = 0;
			limit[1][0] = 0;
			limit[1][1] = 0;
			limit[2][0] = 0;
			limit[2][1] = 0;
		}

		@Override
		public int compare(Tuple o1, Tuple o2) {
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

	static class TupleBox {
		int start;
		int count;

		Tuple value;
		Spacer calc;
		Set<Tuple> data;

		TupleBox(int start, int count) {
			this.start = start;
			this.count = count;

			this.calc = new Spacer();
			this.data = new HashSet<>();
		}

		void add(Tuple tuple) {
			this.data.add(tuple);
			this.calc.add(tuple);
		}

		void addAll(Collection<Tuple> colors) {
			for (Tuple tuple : colors) {
				this.add(tuple);
			}
		}

		public void dump() {
			this.calc.clean();
			for (Tuple tuple : this.data) {
				this.calc.add(tuple);
			}

			this.value = new Tuple(0);
			this.value.r = ((calc.limit[0][0] + calc.limit[0][1]) / 2) & 0xFF;
			this.value.g = ((calc.limit[1][0] + calc.limit[1][1]) / 2) & 0xFF;
			this.value.b = ((calc.limit[2][0] + calc.limit[2][1]) / 2) & 0xFF;
		}

		public void frozen() {
			for (Tuple tuple : this.data) {
				tuple.index = this.start;
			}
		}
	}

	public static void findTupleIndex(Tuple tuple, List<Tuple> map) {
		int index = 0;
		int distance = Integer.MAX_VALUE;

		for (int i = 0, l = map.size(); i < l; ++i) {
			Tuple item = map.get(i);
			int dist = (int) Math.sqrt(
					Math.pow(tuple.r - item.r, 2) + 
					Math.pow(tuple.g - item.g, 2) + 
					Math.pow(tuple.b - item.b, 2));

			if (dist < distance) {
				index = i;
				distance = dist;
			}
		}

		tuple.index = index;
	}

	public static void medianCut(int index, TupleBox[] boxes) {
		TupleBox box = boxes[index];
		if (box.count <= 1) {
			return;
		}

		box.calc.calc();
		LinkedList<Tuple> sort = new LinkedList<>();
		sort.addAll(box.data);
		sort.sort(box.calc);

		int half = box.count / 2;
		int count = box.data.size() / 2;

		box.count = half;
		TupleBox newBox = new TupleBox(box.start + half, half);

		int i = 0;
		Iterator<Tuple> it = sort.iterator();
		while (it.hasNext() && i < count) {
			Tuple tuple = it.next();
			box.data.remove(tuple);
			newBox.add(tuple);
			++i;
		}

		boxes[newBox.start] = newBox;

		/* |递归分割| */
		medianCut(index, boxes);
	}

	public static HashMap<Integer, Tuple> dump(int[] pixels) {
		HashMap<Integer, Tuple> map = new HashMap<>();
		for (int pixel : pixels) {
			map.put(pixel & 0xFFFFFF, new Tuple(pixel));
		}
		return map;
	}

	public static int[] compress(int[] pixels, int limit) {
		HashMap<Integer, Tuple> map = dump(pixels);

		System.err.println("map:size:" + map.size());
		if (map.size() <= limit) {
			return pixels;
		}

		ArrayList<Tuple> colormap = new ArrayList<>();
		{
			TupleBox[] boxes = new TupleBox[limit];
			boxes[0] = new TupleBox(0, limit);
			boxes[0].addAll(map.values());

			for (int i = 0; i < limit; i++) {
				medianCut(i, boxes);
				boxes[i].dump();
				colormap.add(boxes[i].value);
			}
		}

		{
			Tuple tuple;
			for (int i = 0, l = pixels.length; i < l; ++i) {
				tuple = map.get(pixels[i] & 0xFFFFFF);
				if (colormap.contains(tuple)) {
					continue;
				}
				findTupleIndex(tuple, colormap);

				pixels[i] = colormap.get(tuple.index).argb();
			}
		}

		return pixels;
	}

	public static void saveColorMap(TupleBox[] boxes) {
		byte[] data = new byte[boxes.length * 3];
		int i = 0;
		for (TupleBox box : boxes) {
			data[i++] = (byte) box.value.r;
			data[i++] = (byte) box.value.g;
			data[i++] = (byte) box.value.b;
		}

		try {
			String name = "/tmp/swap/swap2.ppm";
			RandomAccessFile file = new RandomAccessFile(name, "rwd");
			file.write("P6\n16 16\n255\n".getBytes());
			file.write(data);
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
