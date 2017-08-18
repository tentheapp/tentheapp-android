package com.nvcomputers.ten.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;
/**
 * Created by rkumar4 on 4/25/2017.
 */

public class GlideModuleWithHeaders  implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {   /* nothing to do here */
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.register(String.class, InputStream.class, new HeaderLoaderFactory());
    }
}