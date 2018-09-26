package xin.l024.fileserver.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xin.l024.fileserver.domain.File;
import xin.l024.fileserver.repository.FileRepository;

/**
 * File 服务.
 * 
 * @since 1.0.0 2017年3月28日
 * @author <a href="https://waylau.com">Way Lau</a> 
 */
@Service
public class FileServiceImpl implements FileService {
	
	@Autowired
	public FileRepository fileRepository;

	/* (non-Javadoc)
	 * @see com.waylau.spring.boot.fileserver.service.FileService#saveFile(com.waylau.spring.boot.fileserver.domain.File)
	 */
	@Override
	public File saveFile(File file) {
		return fileRepository.save(file);
	}

	/* (non-Javadoc)
	 * @see com.waylau.spring.boot.fileserver.service.FileService#removeFile(java.lang.Long)
	 */
	@Override
	public void removeFile(Long id) {
		fileRepository.deleteById(id);
	}

	/* (non-Javadoc)
	 * @see com.waylau.spring.boot.fileserver.service.FileService#getFileById(java.lang.Long)
	 */
	@Override
	public File getFileById(Long id) {
		return fileRepository.findById(id).get();
	}

	/* (non-Javadoc)
	 * @see com.waylau.spring.boot.fileserver.service.FileService#listFiles()
	 */
	@Override
	public List<File> listFiles() {
		return fileRepository.findAll();
	}

}
