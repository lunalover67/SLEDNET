package com.example.slednet.utils;

import com.example.slednet.data.models.Activity;
import androidx.room.TypeConverter;


// Room cant comprehend the overwhelming power of enums!!!
// here we distill the idea to a string while keeping clean logic in code
public class EnumConverters {

    @TypeConverter
    public static String typeToString(Activity.TYPE type) {
        return type.name();
    }

    @TypeConverter
    public static Activity.TYPE stringToType(String value) {
        return Activity.TYPE.valueOf(value);
    }

}
