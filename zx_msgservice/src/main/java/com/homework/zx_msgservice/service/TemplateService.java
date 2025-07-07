package com.homework.zx_msgservice.service;

import com.homework.zx_msgservice.domain.po.Template;
import com.homework.zx_msgservice.domain.vo.ResultVO;

import java.util.List;

public interface TemplateService {
    /**选择消息模板*/
    public ResultVO getTemplateById(String templateId);
    /**获取所有模板*/
    public ResultVO<List<Template>> getAllTemplates();
    /**更新消息模板*/
    public ResultVO updateTemplate(String id, String newTemplateContent);
    /**创建消息模板*/
    public ResultVO createTemplate(String content, String templateName);
    /**删除消息模板*/
    public ResultVO deleteTemplate(String templateId);
}
