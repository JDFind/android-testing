package com.example.android.testing.notes.util;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import com.example.android.testing.notes.BuildConfig;

import java.io.File;

/**
 * Created by anicolae on 31-Jul-17.
 */

public class FileProviderUtil {

    public static Uri getUriForFile(Context context, File file) {
        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
    }
}
