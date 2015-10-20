package com.mygdx.game.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.preloader.AssetDownloader;
import com.badlogic.gdx.backends.gwt.preloader.Preloader;
import com.google.gwt.dom.client.ImageElement;

public class MyAssetChecker {
    private static final AssetDownloader downloader = new AssetDownloader();
    private static Preloader preloader;

    public static void checkText(final String assetUrl, final DownloaderListener listener) {
        if (Gdx.files.internal(assetUrl).exists()) {
            // Asset already is loaded
            listener.finished();
        } else {
            if (preloader == null) preloader = ((GwtApplication) Gdx.app).getPreloader();
            final String url = preloader.baseUrl + assetUrl;
            //Logger.println("Downloading " + url);
            downloader.loadText(url, new AssetDownloader.AssetLoaderListener<String>() {
                @Override
                public void onProgress(double amount) {

                }

                @Override
                public void onFailure() {

                }

                @Override
                public void onSuccess(String result) {
                    //Logger.println("Registering text " + assetUrl);
                    preloader.texts.put(assetUrl, (String) result);
                    //Logger.println("Loaded text " + assetUrl);
                    listener.finished();
                }
            });
        }
    }

    public static void checkImage(final String assetUrl, final DownloaderListener listener) {
        if (Gdx.files.internal(assetUrl).exists()) {
            // Asset already is loaded
            listener.finished();
        } else {
            if (preloader == null) preloader = ((GwtApplication) Gdx.app).getPreloader();
            final String url = preloader.baseUrl + assetUrl;
            //Logger.println("Downloading " + url);
            downloader.loadImage(url, "", new AssetDownloader.AssetLoaderListener<ImageElement>() {
                @Override
                public void onProgress(double amount) {

                }

                @Override
                public void onFailure() {
                    listener.failure(url);
                }

                @Override
                public void onSuccess(ImageElement result) {
                    //Logger.println("Registering image " + assetUrl);
                    preloader.images.put(assetUrl, (ImageElement) result);
                    //Logger.println("Loaded image " + assetUrl);
                    listener.finished();
                }
            });
        }
    }

    /*
    private static AssetFilter.AssetType getType(String extension) {
        AssetFilter.AssetType type = AssetFilter.AssetType.Text;
        if (isImage(extension)) type = AssetFilter.AssetType.Image;;
        return type;
    }


    private static boolean isImage (String extension) {
        return extension.equals("png");
    }

    private static boolean isText (String extension) {
        return extension.equals("json") || extension.equals("xml") || extension.equals("txt") || extension.equals("glsl")
                || extension.equals("fnt") || extension.equals("atlas");
    }

    private static String extension (String file) {
        String name = file;
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex == -1) return "";
        return name.substring(dotIndex + 1);
    }
    */
}
