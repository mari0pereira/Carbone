package com.example.bike.model;

import androidx.room.TypeConverter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Conversor {

    // Formatador para datas
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    // ============== CONVERSORES PARA BIGDECIMAL ==============
    @TypeConverter
    public static BigDecimal stringParaBigDecimal(String value) {
        return value == null ? null : new BigDecimal(value);
    }

    @TypeConverter
    public static String bigDecimalParaString(BigDecimal bigDecimal) {
        return bigDecimal == null ? null : bigDecimal.toString();
    }

    // ============== CONVERSORES PARA LOCALDATE ==============
    @TypeConverter
    public static LocalDate stringParaLocalDate(String value) {
        return value == null ? null : LocalDate.parse(value, DATE_FORMATTER);
    }


    @TypeConverter
    public static String localDateParaString(LocalDate data) {
        return data == null ? null : data.format(DATE_FORMATTER);
    }
}