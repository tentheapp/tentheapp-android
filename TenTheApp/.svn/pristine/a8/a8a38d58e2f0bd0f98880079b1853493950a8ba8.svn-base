package com.nvcomputers.ten.glide;

import android.content.Context;

import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.stream.StreamModelLoader;

import java.io.InputStream;
/**
 * Created by rkumar4 on 4/25/2017.
 */

public class HeaderLoaderFactory implements ModelLoaderFactory<String, InputStream> {
    @Override
    public StreamModelLoader<String> build(Context context, GenericLoaderFactory factories) {
        return new HeaderLoader(context);
    }

    @Override
    public void teardown() { /* nothing to free */ }
}