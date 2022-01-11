package com.xueyi.tenant.controller;

import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.dict.feign.RemoteConfigService;
import com.xueyi.system.api.organize.feign.RemoteEnterpriseService;
import com.xueyi.tenant.domain.dto.TeTenantDto;
import com.xueyi.tenant.service.ITeTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 租户管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/tenant")
public class TeTenantController extends BaseController<TeTenantDto, ITeTenantService> {

    @Autowired
    private ITeTenantService tenantService;

    @Autowired
    private RemoteConfigService remoteConfigService;

    @Autowired
    private RemoteEnterpriseService remoteEnterpriseService;

    /** 定义节点名称 */
    protected String getNodeName() {
        return "租户";
    }

//    /**
//     * 查询租户信息列表
//     */
//    @RequiresPermissions("tenant:tenant:list")
//    @GetMapping("/list")
//    public AjaxResult list(TeTenantDto tenant) {
//        startPage();
//        List<TeTenantDto> list = tenantService.mainSelectTenantList(tenant);
//        return getDataTable(list);
//    }
//
//    /**
//     * 导出租户信息列表
//     */
//    @RequiresPermissions("tenant:tenant:export")
//    @Log(title = "租户信息", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, TeTenantDto tenant) throws IOException {
//        List<TeTenantDto> list = tenantService.mainSelectTenantList(tenant);
//        ExcelUtil<TeTenantDto> util = new ExcelUtil<TeTenantDto>(TeTenantDto.class);
//        util.exportExcel(response, list, "租户信息数据");
//    }
//
//    /**
//     * 获取租户信息详细信息
//     */
//    @RequiresPermissions("tenant:tenant:query")
//    @GetMapping(value = "/byId")
//    public AjaxResult getInfo(TeTenantDto tenant) {
//        return AjaxResult.success(tenantService.mainSelectTenantById(tenant));
//    }
//
//    /**
//     * 新增租户信息
//     */
//    @RequiresPermissions("tenant:tenant:add")
//    @Log(title = "租户信息", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody TeTenantDto tenant) {
//        if (StringUtils.equals(BaseConstants.Check.NOT_UNIQUE.getCode(), tenantService.mainCheckTenantNameUnique(new TeTenantDto(tenant.getTenantName())))) {
//            return AjaxResult.error("新增失败，该租户账号已存在，请修改后重试！");
//        }
//        return toAjax(addTenantCache(tenantService.mainInsertTenant(tenant), tenant));
//    }
//
//    /**
//     * 注册用户信息
//     */
//    @InnerAuth
//    @PostMapping("/register")
//    public R<Boolean> register(@RequestBody TenantRegister register) {
//        String key = remoteConfigService.getKey("sys.account.registerTenant").getData();
//        if (!("true".equals(key))) {
//            return R.fail("当前系统没有开启注册功能！");
//        }
//        TeTenantDto tenant = new TeTenantDto(register.getEnterpriseName());
//        if (StringUtils.equals(BaseConstants.Check.NOT_UNIQUE.getCode(), tenantService.mainCheckTenantNameUnique(tenant))) {
//            return R.fail("注册租户'" + register.getEnterpriseSystemName() + "'失败，注册账号已存在");
//        }
//        //租户信息
//        tenant.setStrategyId(TenantConstants.REGISTER_TENANT_STRATEGY_ID);
//        tenant.setTenantName(register.getEnterpriseName());
//        tenant.setTenantSystemName(register.getEnterpriseSystemName());
//        tenant.setTenantNick(register.getEnterpriseNick());
//        tenant.setTenantLogo(register.getLogo());
//
//        //部门信息
//        SysDept dept = new SysDept();
//        dept.setDeptName(register.getNickName());
//        tenant.getParams().put("dept", dept);
//        //个人信息
//        SysUser user = new SysUser();
//        user.setUserName(register.getUserName());
//        user.setNickName(register.getNickName());
//        user.setEmail(register.getEmail());
//        user.setPhone(register.getPhone());
//        user.setSex(register.getSex());
//        user.setAvatar(register.getAvatar());
//        user.setProfile(register.getProfile());
//        user.setPassword(register.getPassword());
//        tenant.getParams().put("user", user);
//        return R.ok(addTenantCache(tenantService.mainRegisterTenant(tenant), tenant) > 0);
//    }
//
//    /**
//     * 修改租户信息
//     */
//    @RequiresPermissions("tenant:tenant:edit")
//    @Log(title = "租户信息", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody TeTenantDto tenant) {
//        if (StringUtils.equals(BaseConstants.Check.NOT_UNIQUE.getCode(), tenantService.mainCheckTenantNameUnique(tenant))) {
//            return AjaxResult.error("修改失败，该租户账号已存在，请修改后重试！");
//        }
//        TeTenantDto check = tenantService.mainCheckTenantByTenantId(new TeTenantDto(tenant.getTenantId()));
//        if (StringUtils.equals(BaseConstants.Default.YES.getCode(), check.getIsChange())) {
//            return AjaxResult.error("禁止操作系统租户");
//        }
//        return toAjax(refreshTenantCache(tenantService.mainUpdateTenant(tenant), check));
//    }
//
//    /**
//     * 修改租户信息排序
//     */
//    @RequiresPermissions("tenant:tenant:edit")
//    @Log(title = "租户信息", businessType = BusinessType.UPDATE)
//    @PutMapping(value = "/sort")
//    public AjaxResult updateSort(@RequestBody TeTenantDto tenant) {
//        return toAjax(tenantService.mainUpdateTenantSort(tenant));
//    }
//
//    /**
//     * 删除租户信息
//     */
//    @RequiresPermissions("tenant:tenant:remove")
//    @Log(title = "租户信息", businessType = BusinessType.DELETE)
//    @DeleteMapping
//    public AjaxResult remove(@RequestBody TeTenantDto tenant) {
//        Set<TeTenantDto> before = tenantService.mainCheckTenantListByIds(tenant);
//        int rows = tenantService.mainDeleteTenantByIds(tenant);
//        if (rows > 0) {
//            Set<TeTenantDto> after = tenantService.mainCheckTenantListByIds(tenant);
//            before.removeAll(after);
//            for (TeTenantDto vo : before) {
//                EnterpriseUtils.deleteEnterpriseAllCache(vo.getTenantId(), vo.getTenantName());
//            }
//        }
//        return toAjax(rows);
//    }
//
//    /**
//     * 新增租户 cache
//     *
//     * @param rows   结果
//     * @param tenant 租户信息
//     * @return 结果
//     */
//    private int addTenantCache(int rows, TeTenantDto tenant) {
//        if (rows > 0 && !StringUtils.equals(BaseConstants.Status.DISABLE.getCode(), tenant.getStatus())) {
//            remoteEnterpriseService.refreshEnterpriseAllCache(tenant.getTenantId(), SecurityConstants.INNER);
//        }
//        return rows;
//    }
//
//    /**
//     * 修改租户 cache
//     *
//     * @param rows      结果
//     * @param oldTenant 原始租户信息
//     * @return 结果
//     */
//    private int refreshTenantCache(int rows, TeTenantDto oldTenant) {
//        if (rows > 0) {
//        }
//        return rows;
//    }
}