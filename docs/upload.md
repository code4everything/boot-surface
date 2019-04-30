## 简化文件上传

文件的上传与下载再项目中也是必不可少的，这里我们将使用最少的代码即可实现文件的上传下载

#### 新建一个文件实体类

``` java
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
@Repository
public interface FileDAO extends MongoRepository<FileInfo, String> {

    FileInfo getByLocalPathOrAccessUrl(String localPath, String accessUrl);

    FileInfo getByAccessUrl(String accessUrl);
}
```

#### 新建一个文件服务接口

``` java
import org.code4everything.boot.service.BootFileService;

public interface DemoFileService extends BootFileService<FileInfo> {}
```

#### 实现文件服务接口

``` java
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
    public FileInfo getBy(DustFile dustFile) {
        String localPath = "/your-storage-path/" + dustFile.getFilename();
        String accessUrl = "/your-controller-mapping/" + dustFile.getFilename();
        return fileDAO.getByLocalPathOrAccessUrl(localPath, accessUrl);
    }

    @Override
    public FileInfo save(DustFile dustFile) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setAccessUrl("/you-controller-mapping/" + dustFile.getFilename());
        fileInfo.setCreateTime(System.currentTimeMillis());
        fileInfo.setId(IdUtil.randomUUID());
        fileInfo.setLocalPath("/your-storage-path" + dustFile.getFilename());
        fileInfo.setSize(dustFile.getSize());
        fileInfo.setSuffix(FileUtil.extName(dustFile.getOriginalFilename()));
        return fileDAO.save(fileInfo);
    }
}
```

#### 新建文件控制器，实现上传下载

``` java
@RestController
@RequestMapping("/api/file")
public class FileController {

    private final DemoFileService demoFileService;

    @Autowired
    public FileController(DemoFileService demoFileService) {
        this.demoFileService = demoFileService;
    }

    @PostMapping("/upload")
    public Response<FileInfo> upload(@RequestBody MultipartFile file) {
        return HttpUtils.upload(demoFileService, file, "/your-storage-path/", true);
    }

    @GetMapping("/**")
    public ResponseEntity<InputStreamSource> get(HttpServletRequest request) throws IOException {
        return HttpUtils.responseFile(demoFileService, request);
    }
}
```

> 这里假设使用的是 `MongoDB` 数据库和 `Spring Data MongoDB` 框架，其他数据库和框架请根据实际情况进行相应的修改。另外，如果无需实体类（即不将文件信息保存至数据库），除控制器的代码外其他代码均无需参考
