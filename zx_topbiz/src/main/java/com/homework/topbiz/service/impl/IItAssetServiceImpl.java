package com.homework.topbiz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.homework.topbiz.entity.po.ItAsset;
import com.homework.topbiz.exception.LogicException;
import com.homework.topbiz.mapper.ItAssetMapper;
import com.homework.topbiz.service.IItAssetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * IT资产服务实现
 * @author JavaForm
 */
@Slf4j
@Service
public class IItAssetServiceImpl extends ServiceImpl<ItAssetMapper, ItAsset> implements IItAssetService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ItAsset createAsset(ItAsset asset) {
        // 检查资产编号是否重复
        if (this.getByAssetCode(asset.getAssetCode()) != null) {
            throw new LogicException("资产编号已存在");
        }
        asset.setStatus("AVAILABLE");
        asset.setCreateTime(LocalDateTime.now());
        asset.setUpdateTime(LocalDateTime.now());
        this.save(asset);
        log.info("创建资产成功: {}", asset.getId());
        return asset;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ItAsset updateAsset(ItAsset asset) {
        ItAsset existing = this.getById(asset.getId());
        if (existing == null) {
            throw new LogicException("资产不存在");
        }
        asset.setUpdateTime(LocalDateTime.now());
        this.updateById(asset);
        log.info("更新资产成功: {}", asset.getId());
        return this.getById(asset.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ItAsset assignAsset(String assetId, String userId, String userName) {
        ItAsset asset = this.getById(assetId);
        if (asset == null) {
            throw new LogicException("资产不存在");
        }
        if (!"AVAILABLE".equals(asset.getStatus())) {
            throw new LogicException("资产当前状态不可分配");
        }
        asset.setUserId(userId);
        asset.setUserName(userName);
        asset.setStatus("IN_USE");
        asset.setUpdateTime(LocalDateTime.now());
        this.updateById(asset);
        log.info("分配资产成功: {} -> {}", assetId, userName);
        return asset;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ItAsset recycleAsset(String assetId) {
        ItAsset asset = this.getById(assetId);
        if (asset == null) {
            throw new LogicException("资产不存在");
        }
        if (!"IN_USE".equals(asset.getStatus())) {
            throw new LogicException("资产当前状态不可回收");
        }
        asset.setUserId(null);
        asset.setUserName(null);
        asset.setStatus("AVAILABLE");
        asset.setUpdateTime(LocalDateTime.now());
        this.updateById(asset);
        log.info("回收资产成功: {}", assetId);
        return asset;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ItAsset scrapAsset(String assetId, String remark) {
        ItAsset asset = this.getById(assetId);
        if (asset == null) {
            throw new LogicException("资产不存在");
        }
        asset.setStatus("SCRAPPED");
        asset.setRemark(remark);
        asset.setUpdateTime(LocalDateTime.now());
        this.updateById(asset);
        log.info("资产报废成功: {}", assetId);
        return asset;
    }

    @Override
    public Page<ItAsset> pageAssets(Integer pageNum, Integer pageSize, String status, String assetType,
                                     String institutionId, String keyword) {
        LambdaQueryWrapper<ItAsset> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(status), ItAsset::getStatus, status)
                .eq(StringUtils.isNotBlank(assetType), ItAsset::getAssetType, assetType)
                .eq(StringUtils.isNotBlank(institutionId), ItAsset::getInstitutionId, institutionId)
                .and(StringUtils.isNotBlank(keyword), w -> w
                        .like(ItAsset::getAssetName, keyword)
                        .or()
                        .like(ItAsset::getAssetCode, keyword)
                        .or()
                        .like(ItAsset::getBrand, keyword)
                        .or()
                        .like(ItAsset::getModel, keyword))
                .orderByDesc(ItAsset::getCreateTime);

        return this.page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public ItAsset getByAssetCode(String assetCode) {
        return this.getOne(new LambdaQueryWrapper<ItAsset>()
                .eq(ItAsset::getAssetCode, assetCode));
    }
}
