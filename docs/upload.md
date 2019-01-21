## 简化文件上传

文件的上传与下载再项目中也是必不可少的，这里我们将使用最少的代码即可实现文件的上传下载

#### 新建一个文件实体类

``` java
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document

import java.io.Serializable;

@Document
public class FileInfo implements Serializable {

    @Id
    private String id;

    private String localPath;

    private String accessUrl;

    private String suffix;

    private Long createTime;

    private Long size;
}
```

### 新建文件访问对象

``` java
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDAO extends MongoRepository<FileInfo, String> {

    FileInfo getByLocalPathOrAccessUrl(String localPath, String accessUrl);

    FileInfo getByAccessUrl(String accessUrl);
}
```

#### 新建一个文件服务接口

``` java
import org.code4everything.boot.service.FileService;

public interface DemoFileService extends FileService<FileInfo> {}
```

#### 实现文件服务接口

``` java
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.IdUtil;
import org.code4everything.boot.annotations.AopLog;
import org.code4everything.boot.bean.MultipartFileBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoFileServiceImpl implements DemoFileService {

    private final FileDAO fileDAO;

    @Autowired
    public DemoFileServiceImpl(FileDAO fileDAO) {this.fileDAO = fileDAO;}

    @Override
    public String getLocalPathByAccessUrl(String accessUrl) {
        FileInfo fileInfo= fileDAO.getByAccessUrl(accessUrl);
        return Objects.isNull(fileInfo) ? "" : fileInfo.getAccessUrl();
    }

    @Override
    public FileInfo getBy(MultipartFileBean fileBean) {
        String localPath = "/your-storage-path/" + fileBean.getFilename();
        String accessUrl = "/your-controller-mapping/" + fileBean.getFilename();
        return fileDAO.getByLocalPathOrAccessUrl(localPath, accessUrl);
    }

    @Override
    public FileInfo save(MultipartFileBean fileBean) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setAccessUrl("/you-controller-mapping/" + fileBean.getFilename());
        fileInfo.setCreateTime(System.currentTimeMillis());
        fileInfo.setId(IdUtil.randomUUID());
        fileInfo.setLocalPath("/your-storage-path" + fileBean.getFilename());
        fileInfo.setSize(fileBean.getSize());
        fileInfo.setSuffix(FileUtil.extName(fileBean.getOriginalFilename()));
        return fileDAO.save(fileInfo);
    }
}
```

#### 新建文件控制器，实现上传下载

``` java
import org.code4everything.boot.bean.Response;
import org.code4everything.boot.web.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api/file")
public class FileController {

    private final DemoFileService demoFileService;

    @Autowired
    public FileController(DemoFileService demoFileService) {
        this.demoFileService = demoFileService;
    }

    @PostMapping("/upload")
    public ResponseResult<FileInfo> upload(@RequestBody MultipartFile file) {
        return HttpUtils.upload(demoFileService, file, "/your-storage-path/", true);
    }

    @GetMapping("/**")
    public ResponseEntity<InputStreamSource> get(HttpServletRequest request) throws IOException {
        return HttpUtils.responseFile(demoFileService, request);
    }
}
```

> 这里假设使用的是 `MongoDB` 数据库和 `Spring Data MongoDB` 框架，其他数据库和框架请根据实际情况进行相应的修改。另外，如果无需实体类（即不将文件信息保存至数据库），除控制器的代码外其他代码均无需参考
