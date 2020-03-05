/**
 * Copyright (c) 2015-2020, Michael Yang 杨福海 (fuhai999@gmail.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jboot.db.model;

import io.jboot.Jboot;
import io.jboot.app.config.annotation.ConfigModel;
import io.jboot.components.cache.JbootCache;
import io.jboot.components.cache.JbootCacheConfig;
import io.jboot.components.cache.JbootCacheManager;
import io.jboot.utils.ClassUtil;
import io.jboot.utils.StrUtil;

/**
 * @author Michael Yang 杨福海 （fuhai999@gmail.com）
 * @version V1.0
 * @Package io.jboot.db.model
 */
@ConfigModel(prefix = "jboot.model")
public class JbootModelConfig {

    private String scanPackage;
    private String unscanPackage;

    private String columnCreated = "created";
    private String columnModified = "modified";

    /**
     * 是否启用 id 缓存，如果启用，当根据 id 查询的时候，会自动存入缓存
     * 下次再通过 id 查询的时候，直接从缓存中获取 Model
     */
    private boolean idCacheEnable = true;

    /**
     * id 缓存的时间，默认为 1 个小时，单位：秒
     */
    private int idCacheTime = 60 * 60 * 1;

    /**
     * Model 过滤器，可以通过这个配置来防止 xss 等问题
     * filter 会在 save 和 update 的时候被执行
     */
    private String filter;

    /**
     * 主键的值的生成器，可以通过配置这个来自定义主键的生成策略
     */
    private String primarykeyValueGenerator;


    private String idCacheType = Jboot.config(JbootCacheConfig.class).getType();


    public String getScanPackage() {
        return scanPackage;
    }

    public void setScanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
    }

    public String getUnscanPackage() {
        return unscanPackage;
    }

    public void setUnscanPackage(String unscanPackage) {
        this.unscanPackage = unscanPackage;
    }

    public String getColumnCreated() {
        return columnCreated;
    }

    public void setColumnCreated(String columnCreated) {
        this.columnCreated = columnCreated;
    }

    public String getColumnModified() {
        return columnModified;
    }

    public void setColumnModified(String columnModified) {
        this.columnModified = columnModified;
    }

    public int getIdCacheTime() {
        return idCacheTime;
    }

    public void setIdCacheTime(int idCacheTime) {
        this.idCacheTime = idCacheTime;
    }

    public boolean isIdCacheEnable() {
        return idCacheEnable;
    }

    public void setIdCacheEnable(boolean idCacheEnable) {
        this.idCacheEnable = idCacheEnable;
    }

    public String getIdCacheType() {
        return idCacheType;
    }

    public void setIdCacheType(String idCacheType) {
        this.idCacheType = idCacheType;
    }


    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getPrimarykeyValueGenerator() {
        return primarykeyValueGenerator;
    }

    public void setPrimarykeyValueGenerator(String primarykeyValueGenerator) {
        this.primarykeyValueGenerator = primarykeyValueGenerator;
    }

    private JbootModelFilter filterObj;
    public JbootModelFilter getFilterObj() {
        if (filterObj == null){
            if (StrUtil.isNotBlank(filter)){
                synchronized (this){
                    if (filterObj == null){
                        filterObj = ClassUtil.newInstance(filter);
                    }
                }
            }else {
                filterObj = JbootModelFilter.DEFAULT;
            }
        }
        return filterObj;
    }


    private PrimarykeyValueGenerator primarykeyValueGeneratorObj;
    public PrimarykeyValueGenerator getPrimarykeyValueGeneratorObj() {
        if (primarykeyValueGeneratorObj == null){
            if (StrUtil.isNotBlank(primarykeyValueGenerator)){
                synchronized (this){
                    if (primarykeyValueGeneratorObj == null){
                        primarykeyValueGeneratorObj = ClassUtil.newInstance(primarykeyValueGenerator);
                    }
                }
            }else {
                primarykeyValueGeneratorObj = PrimarykeyValueGenerator.DEFAULT;
            }
        }
        return primarykeyValueGeneratorObj;
    }


    private static JbootModelConfig config;

    public static JbootModelConfig getConfig() {
        if (config == null) {
            config = Jboot.config(JbootModelConfig.class);
        }
        return config;
    }

    private JbootCache jbootCache;

    public JbootCache getCache() {
        if (jbootCache == null) {
            jbootCache = JbootCacheManager.me().getCache(idCacheType);
        }
        return jbootCache;
    }
}
