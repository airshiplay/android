package com.airshiplay.mobile.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * /** ********************************************************************
 *  PNG Extensions
 *
 *  New private chunks that may be placed in PNG images.
 *
 *********************************************************************** */

/**
 * This chunk specifies how to split an image into segments for
 * scaling.
 *
 * There are J horizontal and K vertical segments.  These segments divide
 * the image into J*K regions as follows (where J=4 and K=3):
 *
 *      F0   S0    F1     S1
 *   +-----+----+------+-------+
 * S2|  0  |  1 |  2   |   3   |
 *   +-----+----+------+-------+
 *   |     |    |      |       |
 *   |     |    |      |       |
 * F2|  4  |  5 |  6   |   7   |
 *   |     |    |      |       |
 *   |     |    |      |       |
 *   +-----+----+------+-------+
 * S3|  8  |  9 |  10  |   11  |
 *   +-----+----+------+-------+
 *
 * Each horizontal and vertical segment is considered to by either
 * stretchable (marked by the Sx labels) or fixed (marked by the Fy
 * labels), in the horizontal or vertical axis, respectively. In the
 * above example, the first is horizontal segment (F0) is fixed, the
 * next is stretchable and then they continue to alternate. Note that
 * the segment list for each axis can begin or end with a stretchable
 * or fixed segment.
 *
 * The relative sizes of the stretchy segments indicates the relative
 * amount of stretchiness of the regions bordered by the segments.  For
 * example, regions 3, 7 and 11 above will take up more horizontal space
 * than regions 1, 5 and 9 since the horizontal segment associated with
 * the first set of regions is larger than the other set of regions.  The
 * ratios of the amount of horizontal (or vertical) space taken by any
 * two stretchable slices is exactly the ratio of their corresponding
 * segment lengths.
 *
 * xDivs and yDivs point to arrays of horizontal and vertical pixel
 * indices.  The first pair of Divs (in either array) indicate the
 * starting and ending points of the first stretchable segment in that
 * axis. The next pair specifies the next stretchable segment, etc. So
 * in the above example xDiv[0] and xDiv[1] specify the horizontal
 * coordinates for the regions labeled 1, 5 and 9.  xDiv[2] and
 * xDiv[3] specify the coordinates for regions 3, 7 and 11. Note that
 * the leftmost slices always start at x=0 and the rightmost slices
 * always end at the end of the image. So, for example, the regions 0,
 * 4 and 8 (which are fixed along the X axis) start at x value 0 and
 * go to xDiv[0] and slices 2, 6 and 10 start at xDiv[1] and end at
 * xDiv[2].
 *
 * The array pointed to by the colors field lists contains hints for
 * each of the regions.  They are ordered according left-to-right and
 * top-to-bottom as indicated above. For each segment that is a solid
 * color the array entry will contain that color value; otherwise it
 * will contain NO_COLOR.  Segments that are completely transparent
 * will always have the value TRANSPARENT_COLOR.
 *
 * The PNG chunk type is "npTc".
 *
 * @Create 2013-7-23
 * @author lig
 * @version 1.0
 * @since 1.0
 */
public class Res_png_9patch {
	byte wasDeserialized;
	byte numXDivs;
	byte numYDivs;
	byte numColors;
	// These tell where the next section of a patch starts.
	// For example, the first patch includes the pixels from
	// 0 to xDivs[0]-1 and the second patch includes the pixels
	// from xDivs[0] to xDivs[1]-1.
	// Note: allocation/free of these pointers is left to the caller.
	int[] xDivs;
	int[] yDivs;
	int paddingLeft, paddingRight;
	int paddingTop, paddingBottom;

	enum Color {

		// The 9 patch segment is not a solid color.
		NO_COLOR(0x00000001),

		// The 9 patch segment is completely transparent.
		TRANSPARENT_COLOR(0x00000000);
		private int color;

		Color(int color) {
			this.color = color;
		}
	};

	// Note: allocation/free of this pointer is left to the caller.
	int[] colors;

	private Res_png_9patch() {

	}

	// Convert data from device representation to PNG file representation.
	void deviceToFile() {

	}

	// Convert data from PNG file representation to device representation.
	void fileToDevice() {

	}

	// Serialize/Marshall the patch data into a newly malloc-ed block
	public	byte[] serialize() {
		byte[] chunk = new byte[serializedSize()];
		serialize(chunk);
		return chunk;
	}

	// Serialize/Marshall the patch data
	public	void serialize(byte[] outData) {
		ByteBuffer buffer = ByteBuffer.wrap(outData).order(ByteOrder.LITTLE_ENDIAN);
		buffer.put(wasDeserialized);
		buffer.put(numXDivs).put(numYDivs).put(numColors);
		
	}

	// Deserialize/Unmarshall the patch data
public	static Res_png_9patch deserialize(byte[] data) {
		ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
		Res_png_9patch patch = new Res_png_9patch();
		patch.wasDeserialized = buffer.get();// 0
		patch.numXDivs = buffer.get();// 1
		patch.numYDivs = buffer.get();// 2
		patch.numColors = buffer.get();// 3
		//4-11
		buffer.position(12);
		patch.paddingLeft = buffer.getInt();//12-15
		patch.paddingRight = buffer.getInt();//16-19
		patch.paddingTop = buffer.getInt();//20-23
		patch.paddingBottom = buffer.getInt();//24-27
		//28-31
		buffer.position(32);
		patch.xDivs = new int[patch.numXDivs];
		for (int i = 0; i < patch.numXDivs; i++) {
			patch.xDivs[i] = buffer.getInt();
		}
		patch.yDivs = new int[patch.numYDivs];
		for (int i = 0; i < patch.numYDivs; i++) {
			patch.yDivs[i] = buffer.getInt();
		}
		patch.colors = new int[patch.numColors];
		for (int i = 0; i < patch.numColors; i++) {
			patch.colors[i] = buffer.getInt();
		}
		return patch;
	}

	// Compute the size of the serialized data structure
	int serializedSize() {
		return Integer.SIZE * (numXDivs + numYDivs + numColors) + Integer.SIZE;
	}
}
