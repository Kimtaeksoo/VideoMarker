package com.example.videomarker.data.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.provider.MediaStore;

import androidx.annotation.Nullable;

import com.example.videomarker.data.entities.Data;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ContentLoader {

//    public static final List<Data> datas = new ArrayList<>();
//    private static int id;
//    private static String name;
//    private static String dur;
//    private static int size;
//    private static String mime;
//    private static String added;


    public static List<Data> getContent(Context context) {
        List<Data> datas = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver(); //데이터를 가져오는 커넥터

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String projections[] = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DATE_ADDED
        };
        Cursor c = resolver.query(uri, projections, null, null, null);

        if (c != null) {
            while (c.moveToNext()) {
                int index = c.getColumnIndex(projections[0]);
                int id = c.getInt(index);

                index = c.getColumnIndex(projections[1]);
                String name = c.getString(index);

                index = c.getColumnIndex(projections[2]);
                String duration = c.getString(index);

                //Todo: 밀리세컨값을 그냥 뒤에 3자리를 짤라버리고 코딩함. 나중에 오류생기면 TimeUnit으로 바꿀것
                //Todo: 밀리세컨 > 시분초, Byte > MegaByte : presenter로 나중에 옮겨야됨(재사용가능한 코드로)
                int millis = Integer.parseInt(duration.substring(0,duration.length()-3));

                int h, m, s;
                String hour, min, sec;
                m = millis / 60;
                h = m / 60;
                s = millis % 60;
                m = m % 60;

                hour = Integer.toString(h) + ":";
                if(m<10) { min = "0" + Integer.toString(m) + ":"; }
                else{ min = Integer.toString(m) + ":"; }
                if(s<10) { sec = "0" + Integer.toString(s); }
                else{ sec = Integer.toString(s); }

                String dur = (hour + min + sec);


                index = c.getColumnIndex(projections[3]);
                String bytesize = c.getString(index);
                int size = Integer.parseInt(bytesize);


                index = c.getColumnIndex(projections[4]);
                String mime = c.getString(index);

                index = c.getColumnIndex(projections[5]);
                String added = c.getString(index);

                Data data = new Data();

                data.setResId(id);
                data.setName(name);
                data.setDur(dur);

                ContentLoader reSize = new ContentLoader();
                String changedSize = reSize.getReadableFileSize(size);
                data.setSize(changedSize);

                data.setMime(mime);
                data.setAdded(added);

                datas.add(data);
            }
        }
        c.close();
        return datas;
    }
//    public static List<Data> getData(Context context) {
//        List<Data> datas = new ArrayList<>();
//
//        Data data = new Data();
//
//        data.setName(name);
//        data.setDur(dur);
//
//        datas.add(data);
//        return datas;
//    }

    //    public static List<Data> getInfoData(Context context) {
//        List<Data> datas = new ArrayList<>();
//
//        Data data = new Data();
//
//        data.setResId(id);
//        data.setName(name);
//        data.setDur(dur);
//        data.setSize(size);
//        data.setMime(mime);
//        data.setAdded(added);
//
//        datas.add(data);
//        return datas;
//    }
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

}
