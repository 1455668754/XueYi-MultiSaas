/**
 * Copyright (C), 2015-2023, Tide Team FileName: ASysDoMainController Author: kevin Date: 2023/11/1
 * 16:14 Description: 通过域名获取租户名称
 */
package com.xueyi.system.authority.controller.admin;

import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.security.annotation.ApiAuth;
import com.xueyi.system.authority.service.ISysLoginService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.xueyi.common.core.web.result.AjaxResult.success;

@ApiAuth(isAnonymous = true)
@RestController
@RequestMapping("/domain")
public class ASysDoMainController {
  @Autowired ISysLoginService loginService;

  @GetMapping
  @ApiAuth(isAnonymous = true)
  public AjaxResult getDomainTenant(String url) {
    String name = null;
    if (StringUtils.isNotBlank(url)) {
      name = loginService.getDomaingetTenant(url);
    }
    Map<String, String> map = new HashMap<>();
    map.put("enterpriseName", name);
    return success(StringUtils.isEmpty(name) ? "" : map);
  }
}
