/**
 * 
 */
package xin.l024.fileserver.service;

import xin.l024.fileserver.domain.File;

import java.util.List;


/**
 * File 服务接口.
 * 
 * @since 1.0.0 2017年3月28日
 * @author <a href="https://waylau.com">Way Lau</a> 
 */
public interface FileService {
	/**
	 * 保存文件
	 * @return
	 */
	File saveFile(File file);
	
	/**
	 * 删除文件
	 * @return
	 */
	void removeFile(Long id);
	
	/**
	 * 根据id获取文件
	 * @return
	 */
	File getFileById(Long id);
	
	/**
	 * 获取文件列表
	 * @return
	 */
	List<File> listFiles();
}
