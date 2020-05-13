package com.fammeo.app.util;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

@GlideModule
public class CustomeGlideModule  extends AppGlideModule {
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}