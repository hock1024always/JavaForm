package com.homework.topbiz.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.homework.topbiz.entity.po.ItAsset;

/**
 * IT资产服务接口
 * @author JavaForm
 */
public interface IItAssetService extends IService<ItAsset> {

    /**
     * 创建资产
     */
    ItAsset createAsset(ItAsset asset);

    /**
     * 更新资产
     */
    ItAsset updateAsset(ItAsset asset);

    /**
     * 分配资产给用户
     */
    ItAsset assignAsset(String assetId, String userId, String userName);

    /**
     * 回收资产
     */
    ItAsset recycleAsset(String assetId);

    /**
     * 资产报废
     */
    ItAsset scrapAsset(String assetId, String remark);

    /**
     * 分页查询资产
     */
    Page<ItAsset> pageAssets(Integer pageNum, Integer pageSize, String status, String assetType,
                              String institutionId, String keyword);

    /**
     * 根据资产编号查询
     */
    ItAsset getByAssetCode(String assetCode);
}
