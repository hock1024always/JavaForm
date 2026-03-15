package com.homework.topbiz.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.homework.topbiz.entity.po.ItAsset;
import com.homework.topbiz.entity.vo.ResultVO;
import com.homework.topbiz.service.IItAssetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * IT资产控制器
 * @author JavaForm
 */
@Slf4j
@RestController
@RequestMapping("/asset")
@RequiredArgsConstructor
@Tag(name = "IT资产管理", description = "资产登记、分配、回收、报废等操作")
public class ItAssetController {

    private final IItAssetService assetService;

    @PostMapping("/create")
    @Operation(summary = "创建资产", description = "登记新的IT资产")
    public ResultVO<ItAsset> createAsset(@RequestBody ItAsset asset) {
        return ResultVO.success(assetService.createAsset(asset));
    }

    @PutMapping("/update")
    @Operation(summary = "更新资产", description = "更新资产信息")
    public ResultVO<ItAsset> updateAsset(@RequestBody ItAsset asset) {
        return ResultVO.success(assetService.updateAsset(asset));
    }

    @PostMapping("/assign")
    @Operation(summary = "分配资产", description = "将资产分配给用户使用")
    public ResultVO<ItAsset> assignAsset(
            @Parameter(description = "资产ID") @RequestParam String assetId,
            @Parameter(description = "用户ID") @RequestParam String userId,
            @Parameter(description = "用户姓名") @RequestParam String userName) {
        return ResultVO.success(assetService.assignAsset(assetId, userId, userName));
    }

    @PostMapping("/recycle")
    @Operation(summary = "回收资产", description = "回收用户使用的资产")
    public ResultVO<ItAsset> recycleAsset(
            @Parameter(description = "资产ID") @RequestParam String assetId) {
        return ResultVO.success(assetService.recycleAsset(assetId));
    }

    @PostMapping("/scrap")
    @Operation(summary = "资产报废", description = "标记资产为报废状态")
    public ResultVO<ItAsset> scrapAsset(
            @Parameter(description = "资产ID") @RequestParam String assetId,
            @Parameter(description = "备注") @RequestParam(required = false) String remark) {
        return ResultVO.success(assetService.scrapAsset(assetId, remark));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询资产", description = "分页查询资产列表")
    public ResultVO<Page<ItAsset>> pageAssets(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @Parameter(description = "状态") @RequestParam(required = false) String status,
            @Parameter(description = "资产类型") @RequestParam(required = false) String assetType,
            @Parameter(description = "机构ID") @RequestParam(required = false) String institutionId,
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword) {
        return ResultVO.success(assetService.pageAssets(pageNum, pageSize, status, assetType, institutionId, keyword));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询资产详情", description = "根据ID查询资产详情")
    public ResultVO<ItAsset> getAsset(@PathVariable String id) {
        return ResultVO.success(assetService.getById(id));
    }

    @GetMapping("/code/{assetCode}")
    @Operation(summary = "根据资产编号查询", description = "根据资产编号查询资产")
    public ResultVO<ItAsset> getByAssetCode(@PathVariable String assetCode) {
        return ResultVO.success(assetService.getByAssetCode(assetCode));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除资产", description = "删除资产（逻辑删除）")
    public ResultVO<Void> deleteAsset(@PathVariable String id) {
        assetService.removeById(id);
        return ResultVO.success();
    }
}
