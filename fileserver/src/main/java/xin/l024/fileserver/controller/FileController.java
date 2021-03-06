package xin.l024.fileserver.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import xin.l024.fileserver.domain.File;
import xin.l024.fileserver.service.FileService;
import xin.l024.fileserver.util.MD5Util;


@CrossOrigin(origins = "*", maxAge = 3600)  // 允许所有域名访问
@Controller
public class FileController {

    @Autowired
    private FileService fileService;
    
    //首页，获取所有文件列表
    @RequestMapping(value = "/")
    public String index(Model model) {
        model.addAttribute("files", fileService.listFiles());
        return "index";
    }
    //登录界面
    @GetMapping("/login")
    public String login(){
        return "login";
    }
    //登录错误
    @GetMapping("/login-error")
    public String error(Model model){
        System.out.println("xxxxxxxxx");
        model.addAttribute("loginError", true);
        model.addAttribute("errorMsg", "登陆失败，账号或者密码错误！");
        return "login";
    }
    
    //跳转错误页面
    @GetMapping("/error")
    public String error(){
        return "error";
    }

    /**
     * 获取文件片信息
     * @param id
     * @return
     */
    @GetMapping("/file/{id}")
    @ResponseBody
    public ResponseEntity serveFile(@PathVariable Long id) {

        File file = fileService.getFileById(id);

        if (file != null) {
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + file.getName() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream" )
                    .header(HttpHeaders.CONTENT_LENGTH, file.getSize()+"")
                    .header("Connection",  "close") 
                    .body( file.getContent());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File was not fount");
        }

    }
    
    /**
     * 在线显示文件
     * @param id
     * @return
     */
    @GetMapping("/file/view/{id}")
    @ResponseBody
    public ResponseEntity serveFileOnline(@PathVariable Long id) {

        File file = fileService.getFileById(id);

        if (file != null) {
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "fileName=\"" + file.getName() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, file.getContentType() )
                    .header(HttpHeaders.CONTENT_LENGTH, file.getSize()+"")
                    .header("Connection",  "close") 
                    .body( file.getContent());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File was not fount");
        }

    }
    
    /**
     * 上传
     * @param file
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/file/upload2")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        try {
        	File f = new File(file.getOriginalFilename(),  file.getContentType(), file.getSize(), file.getBytes());
        	f.setMd5( MD5Util.getMD5(file.getInputStream()) );
        	fileService.saveFile(f);
        } catch (IOException | NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("message",
                    "你上传的" + file.getOriginalFilename() +"文件有异常！！!");
            return "redirect:/";
        }

        redirectAttributes.addFlashAttribute("message",
                "你成功上传了" + file.getOriginalFilename() + "文件！！!");

        return "redirect:/";
    }
 
    @PostMapping("/file/upload")
    @ResponseBody
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
    	File returnFile = null;
        try {
        	File f = new File(file.getOriginalFilename(),  file.getContentType(), file.getSize(),file.getBytes());
        	f.setMd5( MD5Util.getMD5(file.getInputStream()) );
        	returnFile = fileService.saveFile(f);
        	returnFile.setPath("http://localhost:8080/view/"+f.getId());
        	returnFile.setContent(null) ;
        	return ResponseEntity.status(HttpStatus.OK).body("http://localhost:8080/view/"+f.getId());
 
        } catch (IOException | NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
    
    //根据id删除文件
    @GetMapping("/file/del/{id}")
    public String del(@PathVariable("id") Long id,Model model){
        System.out.println("id--->"+id);
        try {
            fileService.removeFile(id);
            model.addAttribute("message","删除成功");
        }catch (Exception e){
            model.addAttribute("message","删除失败");
        }finally {
            model.addAttribute("files", fileService.listFiles());
            return "index";
        }
    }
}
