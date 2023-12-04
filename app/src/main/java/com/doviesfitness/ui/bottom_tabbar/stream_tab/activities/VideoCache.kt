package com.doviesfitness.ui.bottom_tabbar.stream_tab.activities

import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.facebook.FacebookSdk.getCacheDir
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File


class VideoCache {
// manage caching of video
    companion object{
        public var sDownloadCache: SimpleCache? = null
        const val MAX_PREVIEW_CACHE_SIZE_IN_BYTES = 20L * 1024L * 1024L* 1024L

        public fun getInstance(): SimpleCache {
            if (sDownloadCache == null) sDownloadCache =
                SimpleCache(File(getCacheDir(), "exoCache"), LeastRecentlyUsedCacheEvictor(
                    MAX_PREVIEW_CACHE_SIZE_IN_BYTES
                )
                )
            return sDownloadCache as SimpleCache
        }

    }

}