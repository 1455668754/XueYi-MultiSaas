package com.xueyi.common.web.entity.manager.impl.handle;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import com.xueyi.common.core.constant.basic.OperateConstants;
import com.xueyi.common.core.utils.core.ArrayUtil;
import com.xueyi.common.core.utils.core.CollUtil;
import com.xueyi.common.core.utils.core.MapUtil;
import com.xueyi.common.core.utils.core.NumberUtil;
import com.xueyi.common.core.utils.core.TypeUtil;
import com.xueyi.common.core.web.entity.base.BaseEntity;
import com.xueyi.common.core.web.entity.model.BaseConverter;
import com.xueyi.common.web.entity.domain.SlaveRelation;
import com.xueyi.common.web.entity.mapper.BaseMapper;
import com.xueyi.common.web.utils.MergeUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据封装层处理 操作方法 基类通用数据处理
 *
 * @param <Q>  Query
 * @param <D>  Dto
 * @param <P>  Po
 * @param <PM> PoMapper
 * @author xueyi
 */
public class BaseHandleManagerImpl<Q extends P, D extends P, P extends BaseEntity, PM extends BaseMapper<Q, D, P>, CT extends BaseConverter<Q, D, P>> {

    @Autowired
    protected PM baseMapper;

    @Autowired
    protected CT baseConverter;

    /** Dto泛型的类型 */
    @Getter
    private final Class<D> DClass = TypeUtil.getClazz(getClass().getGenericSuperclass(), NumberUtil.One);

    /** Po泛型的类型 */
    @Getter
    private final Class<P> PClass = TypeUtil.getClazz(getClass().getGenericSuperclass(), NumberUtil.Two);

    /** 子类操作泛型的类型 */
    @Setter
    private Map<String, SlaveRelation> subRelationMap;

    /**
     * 初始化从属关联关系
     *
     * @return 关系对象集合
     */
    protected List<SlaveRelation> subRelationInit() {
        return new ArrayList<>();
    }

    /**
     * 查询条件构造 | 列表查询
     *
     * @param query 数据查询对象
     * @return 条件构造器
     */
    protected LambdaQueryWrapper<P> selectListQuery(Q query) {
        return new LambdaQueryWrapper<>(query);
    }

    /**
     * 类型转换 | 持久化对象 -> 数据传输对象
     *
     * @param po 持久化对象
     * @return dto
     */
    protected D mapperDto(P po) {
        return baseConverter.mapperDto(po);
    }

    /**
     * 类型转换 | 持久化对象集合 -> 数据传输对象集合
     *
     * @param poList 持久化对象集合
     * @return dtoList
     */
    protected List<D> mapperDto(Collection<P> poList) {
        if (poList instanceof Page<P> pagePoList) {
            Page<D> pageDtoList = baseConverter.mapperPageDto(poList);
            pageCopy(pagePoList, pageDtoList);
            return pageDtoList;
        }
        return baseConverter.mapperDto(poList);
    }

    /**
     * 类型转换 | 数据传输对象 -> 持久化对象
     *
     * @param dto 数据传输对象
     * @return po
     */
    protected P mapperPo(D dto) {
        return baseConverter.mapperPo(dto);
    }

    /**
     * 类型转换 | 数据传输对象集合 -> 持久化对象集合
     *
     * @param dtoList 数据传输对象集合
     * @return poList
     */
    protected List<P> mapperPo(Collection<D> dtoList) {
        if (dtoList instanceof Page<D> pageDtoList) {
            Page<P> pagePoList = baseConverter.mapperPagePo(dtoList);
            pageCopy(pageDtoList, pagePoList);
            return pagePoList;
        }
        return baseConverter.mapperPo(dtoList);
    }

    /**
     * 分页参数复制
     *
     * @param copyPage  复制分页对象
     * @param pastePage 粘贴分页对象
     */
    @SuppressWarnings("rawtypes")
    protected void pageCopy(Page copyPage, Page pastePage) {
        pastePage.setPageNum(copyPage.getPageNum());
        pastePage.setPageSize(copyPage.getPageSize());
        pastePage.setStartRow(copyPage.getStartRow());
        pastePage.setEndRow(copyPage.getEndRow());
        pastePage.setTotal(copyPage.getTotal());
        pastePage.setPages(copyPage.getPages());
        pastePage.setCount(copyPage.isCount());
        pastePage.setReasonable(copyPage.getReasonable());
        pastePage.setPageSizeZero(copyPage.getPageSizeZero());
        pastePage.setCountColumn(copyPage.getCountColumn());
        pastePage.setOrderBy(copyPage.getOrderBy());
        pastePage.setOrderByOnly(copyPage.isOrderByOnly());
    }

    /**
     * 子数据组装 | 查询
     *
     * @param dto        数据传输对象
     * @param groupNames 分组名称
     * @return 数据传输对象
     */
    protected D subMerge(D dto, String... groupNames) {
        if (ArrayUtil.isNotEmpty(groupNames))
            Arrays.stream(groupNames).forEach(item -> MergeUtil.subMerge(dto, getSubRelationMap().get(item)));
        else
            getSubRelationMap().values().stream().filter(item -> item.getIsSelect() && item.getIsSingle()).forEach(item -> MergeUtil.subMerge(dto, item));
        return dto;
    }

    /**
     * 子数据组装 | 查询
     *
     * @param dtoList    数据传输对象集合
     * @param groupNames 分组名称
     * @return 数据传输对象集合
     */
    protected List<D> subMerge(List<D> dtoList, String... groupNames) {
        return subMerge(dtoList, OperateConstants.DataRow.LIST, groupNames);
    }

    /**
     * 子数据组装 | 查询
     *
     * @param dtoList    数据传输对象集合
     * @param dataRow    数据类型
     * @param groupNames 分组名称
     * @return 数据传输对象集合
     */
    protected List<D> subMerge(List<D> dtoList, OperateConstants.DataRow dataRow, String... groupNames) {
        if (ArrayUtil.isNotEmpty(groupNames))
            Arrays.stream(groupNames).forEach(item -> MergeUtil.subMerge(dtoList, getSubRelationMap().get(item)));
        else
            switch (dataRow) {
                case SINGLE ->
                        getSubRelationMap().values().stream().filter(item -> item.getIsSelect() && item.getIsSingle()).forEach(item -> MergeUtil.subMerge(dtoList, item));
                case LIST ->
                        getSubRelationMap().values().stream().filter(item -> item.getIsSelect() && item.getIsList()).forEach(item -> MergeUtil.subMerge(dtoList, item));
            }
        return dtoList;
    }

    /**
     * 子数据映射关联 | 新增
     *
     * @param dto        数据对象
     * @param groupNames 分组名称
     * @return 结果
     */
    protected int addMerge(D dto, String... groupNames) {
        return ArrayUtil.isNotEmpty(groupNames)
                ? Arrays.stream(groupNames).mapToInt(item -> MergeUtil.addMerge(dto, getSubRelationMap().get(item))).sum()
                : getSubRelationMap().values().stream().filter(SlaveRelation::getIsAdd).mapToInt(item -> MergeUtil.addMerge(dto, item)).sum();
    }

    /**
     * 集合子数据映射关联 | 新增
     *
     * @param dtoList    数据对象集合
     * @param groupNames 分组名称
     * @return 结果
     */
    protected int addMerge(Collection<D> dtoList, String... groupNames) {
        return ArrayUtil.isNotEmpty(groupNames)
                ? Arrays.stream(groupNames).mapToInt(item -> MergeUtil.addMerge(dtoList, getSubRelationMap().get(item))).sum()
                : getSubRelationMap().values().stream().filter(SlaveRelation::getIsAdd).mapToInt(item -> MergeUtil.addMerge(dtoList, item)).sum();
    }

    /**
     * 子数据映射关联 | 修改
     *
     * @param originDto  源数据对象
     * @param newDto     新数据对象
     * @param groupNames 分组名称
     * @return 结果
     */
    protected int editMerge(D originDto, D newDto, String... groupNames) {
        return ArrayUtil.isNotEmpty(groupNames)
                ? Arrays.stream(groupNames).mapToInt(item -> MergeUtil.editMerge(originDto, newDto, getSubRelationMap().get(item))).sum()
                : getSubRelationMap().values().stream().filter(SlaveRelation::getIsEdit).mapToInt(item -> MergeUtil.editMerge(originDto, newDto, item)).sum();
    }

    /**
     * 集合子数据映射关联 | 修改
     *
     * @param originList 源数据对象集合
     * @param newList    新数据对象集合
     * @param groupNames 分组名称
     * @return 结果
     */
    protected int editMerge(Collection<D> originList, Collection<D> newList, String... groupNames) {
        return ArrayUtil.isNotEmpty(groupNames)
                ? Arrays.stream(groupNames).mapToInt(item -> MergeUtil.editMerge(originList, newList, getSubRelationMap().get(item))).sum()
                : getSubRelationMap().values().stream().filter(SlaveRelation::getIsEdit).mapToInt(item -> MergeUtil.editMerge(originList, newList, item)).sum();
    }

    /**
     * 子数据映射关联 | 删除
     *
     * @param dto        数据对象
     * @param groupNames 分组名称
     * @return 结果
     */
    protected int delMerge(D dto, String... groupNames) {
        return ArrayUtil.isNotEmpty(groupNames)
                ? Arrays.stream(groupNames).mapToInt(item -> MergeUtil.delMerge(dto, getSubRelationMap().get(item))).sum()
                : getSubRelationMap().values().stream().filter(SlaveRelation::getIsDelete).mapToInt(item -> MergeUtil.delMerge(dto, item)).sum();
    }

    /**
     * 集合子数据映射关联 | 删除
     *
     * @param dtoList    数据对象集合
     * @param groupNames 分组名称
     * @return 结果
     */
    protected int delMerge(Collection<D> dtoList, String... groupNames) {
        return ArrayUtil.isNotEmpty(groupNames)
                ? Arrays.stream(groupNames).mapToInt(item -> MergeUtil.delMerge(dtoList, getSubRelationMap().get(item))).sum()
                : getSubRelationMap().values().stream().filter(SlaveRelation::getIsDelete).mapToInt(item -> MergeUtil.delMerge(dtoList, item)).sum();
    }

    /** 子类操作泛型的类型Getter */
    protected Map<String, SlaveRelation> getSubRelationMap() {
        if (MapUtil.isNull(this.subRelationMap)) {
            List<SlaveRelation> subList = subRelationInit();
            this.subRelationMap = CollUtil.isNotEmpty(subList)
                    ? subList.stream().peek(item -> item.setMainDtoClass(getDClass())).collect(Collectors.toMap(SlaveRelation::getGroupName, Function.identity()))
                    : new HashMap<>();
        }
        return this.subRelationMap;
    }
}
