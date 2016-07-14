/**
 * 
 */
package com.hcb.screensync.constansts;

import java.io.File;


public class Constant {
	private static final String ROOT = System.getProperty("user.dir");

	public static File getMinicap() {
		return new File(ROOT, "minicap");
	}

	public static File getMinicapBin() {
		return new File(ROOT, "minicap/bin");
	}

	public static File getMinicapSo() {
		return new File(ROOT, "minicap/shared");
	}

}
