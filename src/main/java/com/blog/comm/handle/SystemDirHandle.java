package com.blog.comm.handle;

import com.blog.comm.util.EncryptUtil;

public class SystemDirHandle {
	static SystemDirHandle handle = new SystemDirHandle();
	public static String rootPath = handle.getClass().getResource("/").getFile()
			.toString();
	public static String pathKey = EncryptUtil.md5Code(rootPath);

}
