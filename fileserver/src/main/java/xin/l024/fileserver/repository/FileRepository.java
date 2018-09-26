package xin.l024.fileserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xin.l024.fileserver.domain.File;


/**
 * File 存储库.
 * 
 * @since 1.0.0 2017年3月28日
 * @author <a href="https://waylau.com">Way Lau</a> 
 */
//public interface FileRepository extends MongoRepository<File, String> {
//}
public interface FileRepository extends JpaRepository<File, Long> {
}
