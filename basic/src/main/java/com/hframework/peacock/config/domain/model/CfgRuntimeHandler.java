package com.hframework.peacock.config.domain.model;

public class CfgRuntimeHandler {
    private Long id;

    private String module;

    private String name;

    private String title;

    private String description;

    private String path;

    private String version;

    private String remark;

    private Integer ctime;

    private Integer mtime;

    private Integer validTime;

    private Integer invalidTime;

    private Byte state;

    private Long moduleId;

    private Long programId;

    private String tags;

    private Long versionId;

    private String content;

    public CfgRuntimeHandler(Long id, String module, String name, String title, String description, String path, String version, String remark, Integer ctime, Integer mtime, Integer validTime, Integer invalidTime, Byte state, Long moduleId, Long programId, String tags, Long versionId, String content) {
        this.id = id;
        this.module = module;
        this.name = name;
        this.title = title;
        this.description = description;
        this.path = path;
        this.version = version;
        this.remark = remark;
        this.ctime = ctime;
        this.mtime = mtime;
        this.validTime = validTime;
        this.invalidTime = invalidTime;
        this.state = state;
        this.moduleId = moduleId;
        this.programId = programId;
        this.tags = tags;
        this.versionId = versionId;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public String getModule() {
        return module;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }

    public String getRemark() {
        return remark;
    }

    public Integer getCtime() {
        return ctime;
    }

    public Integer getMtime() {
        return mtime;
    }

    public Integer getValidTime() {
        return validTime;
    }

    public Integer getInvalidTime() {
        return invalidTime;
    }

    public Byte getState() {
        return state;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public Long getProgramId() {
        return programId;
    }

    public String getTags() {
        return tags;
    }

    public Long getVersionId() {
        return versionId;
    }

    public String getContent() {
        return content;
    }

    public void setId(Long id) {
        this.id=id;
    }

    public void setModule(String module) {
        this.module=module;
    }

    public void setName(String name) {
        this.name=name;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public void setDescription(String description) {
        this.description=description;
    }

    public void setPath(String path) {
        this.path=path;
    }

    public void setVersion(String version) {
        this.version=version;
    }

    public void setRemark(String remark) {
        this.remark=remark;
    }

    public void setCtime(Integer ctime) {
        this.ctime=ctime;
    }

    public void setMtime(Integer mtime) {
        this.mtime=mtime;
    }

    public void setValidTime(Integer validTime) {
        this.validTime=validTime;
    }

    public void setInvalidTime(Integer invalidTime) {
        this.invalidTime=invalidTime;
    }

    public void setState(Byte state) {
        this.state=state;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId=moduleId;
    }

    public void setProgramId(Long programId) {
        this.programId=programId;
    }

    public void setTags(String tags) {
        this.tags=tags;
    }

    public void setVersionId(Long versionId) {
        this.versionId=versionId;
    }

    public void setContent(String content) {
        this.content=content;
    }

    public CfgRuntimeHandler() {
        super();
    }
}