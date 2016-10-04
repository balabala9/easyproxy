package org.easyproxy.cache;/**
 * Description : 
 * Created by YangZH on 16-10-4
 *  下午9:43
 */

import org.easyproxy.cache.ehcache.EHcache;
import org.easyproxy.cache.redis.RedisCache;
import org.easyproxy.util.Config;

import static org.easyproxy.constants.Const.*;

/**
 * Description :
 * Created by YangZH on 16-10-4
 * 下午9:43
 */

public class CacheUtil {

    private static Cache cache;

    static {
        String cacheType = Config.getString(CACHE_TYPE);
        switch (cacheType){
            case REDIS:
                cache = new RedisCache();
                break;
            case EHCACHE:
                cache = new EHcache();
                break;
            case JAVA:
                break;
        }
    }

    public static Cache getCache(){
        return cache;
    }

    public static void setCache(Cache cache){
        CacheUtil.cache = cache;
    }
}
