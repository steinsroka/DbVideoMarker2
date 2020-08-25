package com.example.dbvideomarker.mediastore;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.dbvideomarker.database.entitiy.Media;
import com.example.dbvideomarker.database.entitiy.MediaDisplay;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MediaStoreLoader {

    private static int id;
    private static ArrayList<Integer> mediaIdList = new ArrayList<>();

    public static List<Media> getContent(Context context) {
        List<Media> mediaList = new ArrayList<>();

        ContentResolver resolver = context.getContentResolver();
        MediaStoreLoader loader = new MediaStoreLoader();

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String projections[] = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DATA
        };
        Cursor c = resolver.query(uri, projections, null, null, null);

        if (c != null) {
            while (c.moveToNext()) {
                int index = c.getColumnIndex(projections[0]);
                id = c.getInt(index);

                index = c.getColumnIndex(projections[1]);
                String name = c.getString(index);

                index = c.getColumnIndex(projections[2]);
                String millisDur = c.getString(index);
                long millis = Long.parseLong(millisDur);

                index = c.getColumnIndex(projections[3]);
                String bytesize = c.getString(index);
                int size = Integer.parseInt(bytesize);

                index = c.getColumnIndex(projections[4]);
                String mime = c.getString(index);

                index = c.getColumnIndex(projections[5]);
                String added = c.getString(index);

                index = c.getColumnIndex(projections[6]);
                String path = c.getString(index);

                Media data = new Media();

                data.setResId(id);
                data.setName(name);

                String readableDur = loader.getReadableDuration(millis);
                data.setDur(readableDur);

                String changedSize = loader.getReadableFileSize(size);
                data.setSize(changedSize);

                data.setMime(mime);
                data.setAdded(added);

                String ContentUri = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, String.valueOf(id)).toString();
                data.setContentUri(ContentUri);

                data.setPath(path);

                mediaList.add(data);
            }
        }
        c.close();
        return mediaList;
    }

    public static ArrayList<Integer> getIdArray(Context context) {

        ContentResolver resolver = context.getContentResolver();

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String projections[] = { MediaStore.Video.Media._ID };
        Cursor c = resolver.query(uri, projections, null, null, null);
        mediaIdList.clear();
        if (c != null) {
            while (c.moveToNext()) {
                int index = c.getColumnIndex(projections[0]);
                id = c.getInt(index);
                mediaIdList.add(id);
            }
        }
        c.close();
        return mediaIdList;
    }

    public String getReadableDuration(long millis) {
        //TODO: 60분 미만이어도 00:00:00 로 표시되는 현상
        String dur = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        return dur;
    }

    public String getReadableFileSize(int size) {
        final int BYTES_IN_KILOBYTES = 1024;
        final DecimalFormat dec = new DecimalFormat("###.#");
        final String KILOBYTES = "KB";
        final String MEGABYTES = "MB";
        final String GIGABYTES = "GB";
        float fileSize = 0;
        String suffix = KILOBYTES;

        if(size > BYTES_IN_KILOBYTES) {
            fileSize = size / BYTES_IN_KILOBYTES;
            if(fileSize > BYTES_IN_KILOBYTES) {
                fileSize = fileSize / BYTES_IN_KILOBYTES;
                if (fileSize > BYTES_IN_KILOBYTES) {
                    fileSize = fileSize / BYTES_IN_KILOBYTES;
                    suffix = GIGABYTES;
                } else {
                    suffix = MEGABYTES;
                }
            }
        }
        return String.valueOf(dec.format(fileSize) + suffix);
    }



    public List<MediaDisplay> getDisplayContent(Context context) {
        List<MediaDisplay> mediaDisplayList = new ArrayList<>();

        ContentResolver resolver = context.getContentResolver();
        MediaStoreLoader loader = new MediaStoreLoader();

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String projections[] = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DURATION,
        };

        String selection = MediaStore.Video.Media._ID + "=?";
        String[] selectionArgs = new String[] {"24", "25", "26"};
        Cursor c = resolver.query(uri, projections, selection, selectionArgs, null);

        if (c != null) {
            while (c.moveToNext()) {
                int index = c.getColumnIndex(projections[0]);
                id = c.getInt(index);

                index = c.getColumnIndex(projections[1]);
                String name = c.getString(index);

                index = c.getColumnIndex(projections[2]);
                String millisDur = c.getString(index);
                long millis = Long.parseLong(millisDur);


                MediaDisplay data = new MediaDisplay();

                data.setResId(id);
                data.setName(name);

                String changedTime = loader.getReadableDuration(millis);
                data.setDur(changedTime);

                String ContentUri = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, String.valueOf(id)).toString();
                data.setContentUri(ContentUri);

                mediaDisplayList.add(data);
            }
        }
        c.close();
        return mediaDisplayList;
    }
}
