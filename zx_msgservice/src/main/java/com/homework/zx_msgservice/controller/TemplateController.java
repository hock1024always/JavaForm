package com.homework.zx_msgservice.controller;


import com.homework.zx_msgservice.domain.vo.ResultVO;
import com.homework.zx_msgservice.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.homework.zx_msgservice.domain.po.Template;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/msg/template")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    /**
     获取特定模板，已通过测试
     */
    @PostMapping("/getfixedtemplate")
    public ResultVO getTemplateById(@RequestParam String templateId) {
        ResultVO result= templateService.getTemplateById(templateId);
        return result;
    }

    /**
     获取全部模板，已通过测试
     */
    @PostMapping("/getalltemplates")
    public ResultVO<List<Template>> getAllTemplates() {
        return templateService.getAllTemplates();
    }

    /**
     更新模板内容，已通过测试
     */
    @PostMapping("/updateTemplate")
    public ResultVO updateTemplate(@RequestParam String id, @RequestParam String newTemplateContent) {
        ResultVO result=templateService.updateTemplate(id, newTemplateContent);
        return result;
    }

    //TODO:未实现
    @PostMapping("/getvariebles")
    public String getVariebles(@RequestParam String varieble) {
        return null;
    }

    /**
     创建模板，已通过测试
     */
    @PostMapping("/createtemplate")
    public ResultVO createTemplate(@RequestParam String content, @RequestParam String templateName) {
        ResultVO result=templateService.createTemplate(content, templateName);
        return result;
    }

    /**
     删除模板，已通过测试
     */
    @PostMapping("/delete")
    public ResultVO deleteTemplate(@RequestParam String templateId) {
        if (templateId == null || templateId.trim().isEmpty()) {
            return ResultVO.error("模板 ID 不能为空");
        }else{
            ResultVO result=templateService.deleteTemplate(templateId);
            return result;
        }
    }
}
